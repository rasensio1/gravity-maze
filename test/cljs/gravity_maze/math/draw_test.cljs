(ns gravity-maze.math.draw-test
  (:require [gravity-maze.math.draw :as mth-drw]
            [gravity-maze.test-helpers :refer [roundme]]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest triangle-test
  (testing "fails"
    (is (= [1 1 -0.4 1 1 -0.4]
           (->> (mth-drw/triangle [1 1] [1 1] 1)
               flatten
               (map (partial roundme 1))
               (map js/parseFloat))))
    (is (= [10 10 8.6 10 10 8.6]
           (->> (mth-drw/triangle [10 10] [10 10] 1)
                flatten
                (map (partial roundme 1))
                (map js/parseFloat))))))
