(ns gravity-maze.math.helpers-test
  (:require [gravity-maze.math.helpers :as mth]
            [gravity-maze.test-helpers :refer [roundme]]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest v--test
  (testing "subtracts first from second, element-wise"
    (is (= [1 4] (mth/v- [3 4] [2 0])))))

(deftest pts-dist-test
  (testing "can do right triangles"
    (is (= 5 (mth/pts-dist [0 0] [3 4])))
    (is (= 5 (mth/pts-dist [1 1] [4 5]))))
  (testing "can do non-right triangles"
    (is (= "8.06" (roundme 2 (mth/pts-dist [1 1] [2 9]))))))
