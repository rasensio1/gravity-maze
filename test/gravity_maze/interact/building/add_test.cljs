(ns gravity-maze.interact.building.add-test
  (:require [gravity-maze.interact.building.add :as add]
            [gravity-maze.state.core :as state]
            [gravity-maze.interact.building.validation :as bval]
            [gravity-maze.test-helpers :refer [temp-elem]]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest build-line-mouse-press-test
  (testing "Adds a new line with correct parameters"
    (let [myatm (atom {:elements [{:type :point :pos [1 1]}]
                       :tmp {:building {:line {:mass 99 :range 22}}}})
          event {:x 100 :y 100}
          res (add/build-line-mouse-press myatm event)
          my-line (temp-elem @myatm)]
      (is (= [[100 100] [100 100]] (:pos my-line)))
      (is (= 22 (:range my-line)))
      (is (= 99 (:mass my-line)))
      (is (= 1 (:id my-line))))))

(deftest build-line-mouse-drag-test
  (testing "Adds end position to mousepressed line"
    (let [myatm (atom {:tmp {:editing-elem {:type :line
                                            :pos [[1 1] [1 1]] :id 1}}})
          event {:x 10 :y 10}]
      (add/build-line-mouse-drag myatm event)
      (is (= [[1 1] [10 10]] (:pos (temp-elem @myatm)))))))

(deftest build-finish-mouse-press-test
  (testing "Adds a new finish spot"
    (let [myatm (atom {:elements []
                       :tmp {:building {:finish {:range 20}}}})
          event {:x 100 :y 100}
          res (add/build-finish-mouse-press myatm event)
          my-fin (temp-elem @myatm)]
      (is (= [100 100] (:pos my-fin)))
      (is (= 20 (:range my-fin))))))

(deftest build-start-mouse-press-test
  (testing "Adds a new start with correct params"
    (let [myatm (atom {:elements [{:type :point :pos [1 1]}]
                       :tmp {:building {:start {:mass 55}}}})
          event {:x 100 :y 100}
          res (add/build-start-mouse-press myatm event)
          my-start (temp-elem @myatm)]
      (is (= [100 100] (:pos my-start)))
      (is (= [0 0] (:vel my-start)))
      (is (= 55 (:mass my-start)))
      (is (= 1 (:id my-start))))))

(deftest build-point-mouse-press-test
  (testing "Adds a new point with correct params"
    (let [myatm (atom {:elements [{:type :point :pos [1 1]}]
                        :tmp {:building {:point {:mass 55 :range 100}}}})
          event {:x 100 :y 100}
          res (add/build-point-mouse-press myatm event)
          my-point (temp-elem @myatm)]
      (is (= [100 100] (:pos my-point)))
      (is (= 55 (:mass my-point)))
      (is (= 1 (:id my-point)))
      (is (= 100 (:range my-point))))))

