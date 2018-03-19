(ns gravity-maze.interact.building.core-test
  (:require [gravity-maze.test-helpers :refer [temp-elem]]
            [cljs.test :refer-macros [deftest testing is]]

            [gravity-maze.interact.building.core :as bld]))

(deftest build-mouse-press-test
  (testing "Removes any temp elem"
    (let [myatm (atom {:elements []
                       :tmp {:editing-elem {:type :point
                                            :id 0
                                            :mass 10
                                            :fixed true
                                            :pos [100 100]}}})
          res (bld/build-mouse-press myatm {:x 10 :y 10})]
      (is (= nil (temp-elem @res)))))
  (testing "If clicked, adds to tmp"
    (let [point {:type :point :id 0
                 :mass 10 :fixed true
                 :pos [10 10]}
          myatm (atom {:elements [point]
                       :tmp {:editing-elem nil}})
          res (bld/build-mouse-press myatm {:x 10 :y 10})]
      (is (= point (temp-elem @res))))))

(deftest build-mouse-drag-test
  (testing "transposes point in tmp to drag location"
    (let [point {:type :point :pos [10 10]}
          myatm (atom {:tmp {:editing-elem point
                             :click-pos [11 11]}})
          res (bld/build-mouse-drag myatm {:x 12 :y 12})]
      (is (= [12 12] (:pos (temp-elem @res))))))
  (testing "transposes line in tmp to drag location"
    (let [point {:type :line :pos [[10 10] [20 20]]}
          myatm (atom {:tmp {:editing-elem point
                             :click-pos [15 15]}})
          res (bld/build-mouse-drag myatm {:x 18 :y 18})]
      (is (= [[13 13] [23 23]] (:pos (temp-elem @res))))))
  (testing "Handles multiple events"
    (let [point {:type :line :pos [[10 10] [20 20]]}
          myatm (atom {:tmp {:editing-elem point
                             :click-pos [15 15]}})
          res (bld/build-mouse-drag myatm {:x 18 :y 18})
          res2 (bld/build-mouse-drag myatm {:x 20 :y 20})]
      (is (= [[15 15] [25 25]] (:pos (temp-elem @res)))))))

