(ns gravity-maze.interact.building.core-test
  (:require [gravity-maze.interact.building.core :as build]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest build-line-mouse-press-test
  (testing "Adds a new line with correct parameters"
    (let [myatm (atom {:elements [{:type :point :pos [1 1]}]
                       :tmp {:building {:line {:mass 99 :range 22}}}})
          event {:x 100 :y 100}
          res (build/build-line-mouse-press myatm event)
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
      (build/build-line-mouse-drag myatm event)
      (is (= [[1 1] [10 10]] (get-in @myatm [:elements 1 :pos]))))))

(deftest build-line-mouse-release-test
  (testing "dissocs mouspress? on line"
    (let [line {:type :line :pos [[0 0 ] [10 10]] :mousepress? true :id 2}
          myatm (atom {:elements [{:type :point} {:type :line} line]})]
      (build/build-line-mouse-release myatm {})
      (is (= (dissoc line :mousepress?) (last (:elements @myatm))))
      (is (= 3 (count (:elements @myatm)))))))

(deftest build-finish-mouse-press-test
  (testing "Adds a new finish spot"
    (let [myatm (atom {:elements []
                       :tmp {:building {:finish {:range 20}}}})
          event {:x 100 :y 100}
          res (build/build-finish-mouse-press myatm event)
          my-fin (get-in @myatm [:elements 0])]
      (is (= 1 (count (:elements @myatm))))
      (is (= [100 100] (:pos my-fin)))
      (is (= 20 (:range my-fin))))))

(deftest build-start-mouse-press-test
  (testing "Adds a new point with correct params"
    (let [myatm (atom {:elements [{:type :point :pos [1 1]}]
                       :tmp {:building {:start {:mass 55}}}})
          event {:x 100 :y 100}
          res (build/build-start-mouse-press myatm event)
          my-point (get-in @myatm [:elements 1])]
      (is (= 2 (count (:elements @myatm))))
      (is (= [100 100] (:pos my-point)))
      (is (= [0 0] (:vel my-point)))
      (is (= 55 (:mass my-point)))
      (is (= 1 (:id my-point))))))

