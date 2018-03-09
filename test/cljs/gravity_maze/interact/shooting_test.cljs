(ns gravity-maze.interact.shooting-test
  (:require [gravity-maze.interact.helpers :as hlp]
            [gravity-maze.interact.shooting :as shoot]
            [gravity-maze.test-helpers :refer [temp-elem]]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest launch-mouse-press-test
  (testing "Moves to tmp and sets placeholder when clicked"
    (let [point {:type :point :id 1}
          line {:type :line :id 0}
          myatm (atom {:elements [line point]})]
      (with-redefs [hlp/find-elem (fn [i j] point)]
        (let [res (shoot/launch-mouse-press myatm "event!")]
          (is (= point (temp-elem @res)))
          (is (= {:type :editing :id 1} (last (:elements @myatm)))))))
    (testing "Can only click a point"
      (let [finish {:type :finish :pos [10 10] :id 0}
            myatm (atom {:elements [finish]})
            res (shoot/launch-mouse-press myatm {:x 10 :y 10})]
        (is (= finish (first (:elements @myatm))))))))

(deftest launch-drag-test
  (testing "Stores vector of drag when mousepressed"
    (let [myatm (atom {:tmp {:editing-elem {:type :point :id 0 :pos [0 0]}}})
          event {:x 1 :y 1}
          res (shoot/launch-drag myatm event)]
      (is (= [-1 -1] (:drag-vec (temp-elem @res)))))))

(deftest launch-mouse-release-test
  (testing "Sets velocity if mouse is released with non-nil :drag-vec"
    (let [myatm (atom {:elements [{:type :line :id 1 :pos [0 0]}]
                       :tmp {:editing-elem {:type :point :id 0
                                            :pos [0 0] :drag-vec [10 10]}}})
          event {}
          res (shoot/launch-mouse-release myatm event)
          elem (first (:elements @res))]
      (is (= [10 10] (:vel elem)))
      (is (= nil (:drag-vec elem))))))
