(ns gravity-maze.interact.shooting-test
  (:require [gravity-maze.interact.helpers :as hlp]
            [gravity-maze.interact.shooting :as shoot]
            [gravity-maze.test-helpers :refer [temp-elem]]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest launch-mouse-press-test
  (testing "Moves to tmp when clicked"
    (let [start {:type :start :id 1}
          line {:type :line :id 0}
          myatm (atom {:elements [line start]})]
      (with-redefs [hlp/find-elem (fn [i j] start)]
        (let [res (shoot/launch-mouse-press myatm "event!")]
          (is (= start (temp-elem @res)))))))
  (testing "Can only click a start"
    (let [finish {:type :finish :pos [10 10] :id 0}
          myatm (atom {:elements [finish]})
          res (shoot/launch-mouse-press myatm {:x 10 :y 10})]
      (is (= finish (first (:elements @myatm)))))
    (let [point {:type :point :pos [10 10] :id 0}
          myatm (atom {:elements [point]})
          res (shoot/launch-mouse-press myatm {:x 10 :y 10})]
      (is (= point (first (:elements @myatm)))))))

(deftest launch-drag-test
  (testing "Stores vector of drag when mousepressed"
    (let [myatm (atom {:tmp {:editing-elem {:type :start :id 0 :pos [0 0]}}})
          event {:x 1 :y 1}
          res (shoot/launch-drag myatm event)]
      (is (= [-1 -1] (:drag-vec (temp-elem @res)))))))

(deftest launch-mouse-release-test
  (testing "Sets velocity if mouse is released with non-nil :drag-vec"
    (let [myatm (atom {:elements [{:type :line :id 1 :pos [0 0]}]
                       :tmp {:editing-elem {:type :start :id 0
                                            :pos [0 0] :drag-vec [10 10]}}})
          event {}
          res (shoot/launch-mouse-release myatm event)
          elem (first (:elements @res))]
      (is (= [10 10] (:vel elem)))
      (is (= nil (:drag-vec elem))))))
