(ns gravity-maze.interact-test
  (:require [gravity-maze.interact :as intr]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest clicked?-test
  (testing "Knows if point pos is in range of click"
    (let [pt {:pos [10 10]}]
      (is (= pt (intr/clicked? 20 [0 0] pt))))
    (let [pt {:pos [0 0]}]
      (is (= pt (intr/clicked? 10 [0 0] pt))) )
    (let [pt {:pos [27 27]}]
      (is (= pt (intr/clicked? 25 [10 10] pt)))))
  (testing "Knows if point pos isn't in range of click"
    (is (= false (intr/clicked? 5 [0 0] {:pos [10 10]})))
    (is (= false (intr/clicked? 10 [20 20] {:pos [35 20]})))))

(deftest find-elem-test
  (testing "Returns elem based on filter"
    (let [point {:type :point :pos [5 5]}
          world {:elements [point]}]
      (is (= true (intr/find-elem #(= [5 5] (:pos %)) world))))
    (let [point {:type :point :pos [5 5] :id 1}
          world {:elements [{:type :point :pos [10 10]} point]}]
      (is (= point (intr/find-elem #(if (:id %) % nil) world))))
    (let [line {:type :line :pos [[5 5] [10 10]] :id 1}
          world {:elements [{:type :point :pos [10 10]} line]}]
      (is (= line (intr/find-elem #(if (= :line (:type %)) % nil) world))))
    (let [point {:type :point :pos [0 0] :id 0 :mousepress? true}
          world {:elements [{:type :point :pos [10 10]} point]}]
      (is (= point (intr/find-elem intr/pressed? world)))))

  (testing "Returns nil if no point"
    (let [point {:type :point :pos [20 20]}
          world {:elements [{:type :point :pos [10 10]} point]}]
      (is (= nil (intr/find-elem #(= [0 0] (:pos %)) world))))))

(deftest launch-mouse-press-test
  (testing "Sets element clicked value to true if clicked"
    (let [point {:type :point :id 0 :mousepress? false}
          myatm (atom {:elements [point]})]
      (with-redefs [intr/find-elem (fn [i j] point)]
        (let [res (intr/launch-mouse-press myatm "event!")]
          (is (= true (-> (:elements @res)
                          first
                          :mousepress?))))))))

(deftest launch-drag-test
  (testing "Stores vector of drag when mousepressed"
    (let [myatm (atom {:elements
                       [{:type :point :id 0 :pos [0 0] :mousepress? true}]})
          event {:x 1 :y 1}
          res (intr/launch-drag myatm event)]
      (is (= [-1 -1] (-> (:elements @res) first :drag-vec)))))
  (testing "Does nothing for no mousepress"
    (let [myatm (atom {:elements
                       [{:type :point :id 0 :pos [0 0] :mousepress? false}
                        {:type :line :id 1 :pos [0 0]}]})
          event {:x 1 :y 1}
          res (intr/launch-drag myatm event)]
      (is (= nil (-> (:elements @res) first :drag-vec))))))

(deftest launch-mouse-release-test
  (testing "Sets velocity if mouse is released with non-nil :drag-vec"
    (let [myatm (atom {:elements
                       [{:type :point :id 0
                         :pos [0 0] :mousepress? true :drag-vec [10 10]}
                        {:type :line :id 1 :pos [0 0]}]})
          event {}
          res (intr/launch-mouse-release myatm event)
          elem (first (:elements @res))]
      (is (= [10 10] (:vel elem)))
      (is (= nil (:mousepress? elem)))
      (is (= nil (:drag-vec elem))))))

(deftest build-line-mouse-press-test
  (testing "Adds a new line with correct parameters"
    (let [myatm (atom {:elements [{:type :point :pos [1 1]}]
                       :tmp {:building {:line {:mass 99 :range 22}}}})
          event {:x 100 :y 100}
          res (intr/build-line-mouse-press myatm event)
          my-line (get-in @myatm [:elements 1])]
      (is (= 2 (count (:elements @myatm))))
      (is (= [[100 100] [100 100]] (:pos my-line)))
      (is (= true (:mousepress? my-line)))
      (is (= 22 (:range my-line)))
      (is (= 99 (:mass my-line)))
      (is (= 1 (:id my-line))))))

(deftest build-line-mouse-drag-test
  (testing "Adds end position to mousepressed line"
    (let [myatm (atom {:elements [{:type :point :id 0}
                                  {:type :line :mousepress? true
                                   :pos [[1 1] [1 1]] :id 1}]})
          event {:x 10 :y 10}]
      (intr/build-line-mouse-drag myatm event)
      (is (= [[1 1] [10 10]] (get-in @myatm [:elements 1 :pos]))))))

(deftest build-line-mouse-release-test
  (testing "dissocs mouspress? on line"
    (let [line {:type :line :pos [[0 0 ] [10 10]] :mousepress? true :id 2}
          myatm (atom {:elements [{:type :point} {:type :line} line]})]
      (intr/build-line-mouse-release myatm {})
      (is (= (dissoc line :mousepress?) (last (:elements @myatm))))
      (is (= 3 (count (:elements @myatm)))))))

(deftest build-finish-mouse-press-test
  (testing "Adds a new finish spot"
    (let [myatm (atom {:elements []
                       :tmp {:building {:finish {:range 20}}}})
          event {:x 100 :y 100}
          res (intr/build-finish-mouse-press myatm event)
          my-fin (get-in @myatm [:elements 0])]
      (is (= 1 (count (:elements @myatm))))
      (is (= [100 100] (:pos my-fin)))
      (is (= 20 (:range my-fin))))))

(deftest build-start-mouse-press-test
  (testing "Adds a new point with correct params"
    (let [myatm (atom {:elements [{:type :point :pos [1 1]}]
                       :tmp {:building {:start {:mass 55}}}})
          event {:x 100 :y 100}
          res (intr/build-start-mouse-press myatm event)
          my-point (get-in @myatm [:elements 1])]
      (is (= 2 (count (:elements @myatm))))
      (is (= [100 100] (:pos my-point)))
      (is (= [0 0] (:vel my-point)))
      (is (= 55 (:mass my-point)))
      (is (= 1 (:id my-point))))))

