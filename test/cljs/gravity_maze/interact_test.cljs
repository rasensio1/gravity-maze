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

(deftest find-point-test
  (testing "Returns point based on filter"
    (let [point {:type :point :pos [5 5]}
          world {:elements [point]}]
      (is (= true (intr/find-point #(= [5 5] (:pos %)) world))))
    (let [point {:type :point :pos [5 5] :id 1}
          world {:elements [{:type :point :pos [10 10]} point]}]
      (is (= point (intr/find-point #(if (:id %) % nil) world)))))
  (testing "Returns nil if no point"
    (let [point {:type :point :pos [20 20]}
          world {:elements [{:type :point :pos [10 10]} point]}]
      (is (= nil (intr/find-point #(= [0 0] (:pos %)) world))))))

(deftest launch-mouse-press-test
  (testing "Sets element clicked value to true if clicked"
    (let [point {:type :point :id 0 :mousepress? false}
          myatm (atom {:elements [point]})]
      (with-redefs [intr/find-point (fn [i j] point)]
        (let [res (intr/launch-mouse-press myatm "event!")]
          (is (= true (-> (:elements @res)
                          first
                          :mousepress?))))))))

(deftest launch-drag-test
  (testing "Stores vector of drag when mousepressed"
    (let [myatm (atom {:elements [{:type :point :id 0
                                   :pos [0 0] :mousepress? true}]})
          event {:x 1 :y 1}
          res (intr/launch-drag myatm event)]
      (is (= [-1 -1] (-> (:elements @res)
                       first
                       :drag-vec)))))
  (testing "Does nothing for no mousepress"
    (is (= 1 0))
    )
  )
