(ns gravity-maze.interact.shooting-test
  (:require [gravity-maze.interact.helpers :as hlp]
            [gravity-maze.interact.shooting :as shoot]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest launch-mouse-press-test
  (testing "Sets element clicked value to true if clicked"
    (let [point {:type :point :id 0 :mousepress? false}
          myatm (atom {:elements [point]})]
      (with-redefs [hlp/find-elem (fn [i j] point)]
        (let [res (shoot/launch-mouse-press myatm "event!")]
          (is (= true (-> (:elements @res)
                          first
                          :mousepress?))))))
    (testing "Can only click a point"
      (let [finish {:type :finish :pos [10 10] :id 0}
            myatm (atom {:elements [finish]})
            res (shoot/launch-mouse-press myatm {:x 10 :y 10})]
        (is (= finish (first (:elements @myatm))))))))

(deftest launch-drag-test
  (testing "Stores vector of drag when mousepressed"
    (let [myatm (atom {:elements
                       [{:type :point :id 0 :pos [0 0] :mousepress? true}]})
          event {:x 1 :y 1}
          res (shoot/launch-drag myatm event)]
      (is (= [-1 -1] (-> (:elements @res) first :drag-vec)))))
  (testing "Does nothing for no mousepress"
    (let [myatm (atom {:elements
                       [{:type :point :id 0 :pos [0 0] :mousepress? false}
                        {:type :line :id 1 :pos [0 0]}]})
          event {:x 1 :y 1}
          res (shoot/launch-drag myatm event)]
      (is (= nil (-> (:elements @res) first :drag-vec))))))

(deftest launch-mouse-release-test
  (testing "Sets velocity if mouse is released with non-nil :drag-vec"
    (let [myatm (atom {:elements
                       [{:type :point :id 0
                         :pos [0 0] :mousepress? true :drag-vec [10 10]}
                        {:type :line :id 1 :pos [0 0]}]})
          event {}
          res (shoot/launch-mouse-release myatm event)
          elem (first (:elements @res))]
      (is (= [10 10] (:vel elem)))
      (is (= nil (:mousepress? elem)))
      (is (= nil (:drag-vec elem))))))
