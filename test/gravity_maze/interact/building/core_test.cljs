(ns gravity-maze.interact.building.core-test
  (:require [gravity-maze.test-helpers :refer [temp-elem]]
            [cljs.test :refer-macros [deftest testing is]]

            [gravity-maze.interact.building.core :as bld]))

(deftest edit-mouse-press-test
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

