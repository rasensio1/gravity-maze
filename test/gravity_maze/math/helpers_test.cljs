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
    (is (= 8.06 (roundme 2 (mth/pts-dist [1 1] [2 9])))))
  (testing "knows zero distance"
    (is (= 0 (roundme 2 (mth/pts-dist [1 1] [1 1]))))))

(deftest offset-line-test
  (testing "Creates offset lines"
    (let [line [[0 0] [3 0]]
          offset [0 1]]
      (is (= [[0 1] [3 1]]
             (mth/offset-line mth/v+ offset line))))
    (let [line [[0 0] [2 2]]
          offset [-1 1]]
      (is (= [[-1 1] [1 3]]
             (mth/offset-line mth/v+ offset line))))))

(deftest unit-normal-vec-test
  (testing "can find the normal vector of a line"
    (let [res (mth/unit-normal-vec [[0 0] [2 20]] [1 12])]
      (is (= [0.995 -0.100] (mapv (partial roundme 3) res))))
    (let [res (mth/unit-normal-vec [[3 7] [7 3]] [6 6])]
      (is (= [-0.7 -0.7] (mapv (partial roundme 1) res)))))
  (testing "point on line returns [0 0]"
    (is (= [0 0] (mth/unit-normal-vec [[0 0] [3 3]] [2 2])))))

(deftest unit-vec-test
  (testing "can do a [0 0]"
    (is (= [0 0] (mth/unit-vec [0 0]))))
  (testing "can do a 1x1"
    (let [exp (/ (Math/sqrt 2) 2)
          res (first (mth/unit-vec [1 1]))]
      (is (apply = (map (partial roundme 2) [exp res]))))))

(deftest perp-dot-prod-test
  (testing "Finds if point is above line"
    (is (= 1 (mth/perp-dot-prod [[0 0] [10 10]] [2 3])))
    (is (= 1 (mth/perp-dot-prod [[0 0] [10 0]] [3 6]))))
  (testing "Finds if point is below line"
    (is (= -1 (mth/perp-dot-prod [[0 0] [10 10]] [2 1])))
    (is (= -1 (mth/perp-dot-prod [[0 0] [10 0]] [3 -2]))))
  (testing "Finds if point is on line"
    (is (= 0 (mth/perp-dot-prod [[0 0] [10 10]] [1 1])))
    (is (= 0 (mth/perp-dot-prod [[0 0] [10 0]] [3 0])))))

(deftest det3x3-test
  (testing "can calculate determinant"
    (is (= 0 (mth/det3x3 [[1 4] [2 5] [3 6]])))
    (is (= -9 (mth/det3x3 [[10 4] [2 5] [3 6]])))))

(deftest other-sides-test
  (testing "Creates side lines"
    (let [line [[0 0] [3 0]]
          offset [-1 1]]
      (is (= [[[-1 1] [1 -1]] [[2 1] [4 -1]]]
             (mth/other-sides line offset))))))

(deftest base-sides-test
  (testing "returns base sides"
    (is (= [[[0 1] [1 1]] [[0 -1] [1 -1]]]
           (mth/base-sides [[0 0] [1 0]] [0 1])))))

(deftest out-of-line?-test
  (testing "knows when out of line"
    (is (= true (mth/out-of-line? [[0 0] [10 0]] [11 1])))
    (is (= true (mth/out-of-line? [[10 0] [20 0]] [1 1])))
    (is (= true (mth/out-of-line? [[0 0] [10 0]] [11 0])))
    (is (= true (mth/out-of-line? [[10 0] [20 0]] [21 1]))))
  (testing "knows not out of line trianges"
    (is (= false (mth/out-of-line? [[10 0] [20 0]] [15 30])))
    (is (= false (mth/out-of-line? [[10 0] [20 0]] [15 1])))))

(deftest line-dist-test
  (testing "can find simple distances"
    (is (= 2 (mth/line-dist [[0 0] [5 0]] [0 2])))
    (is (= 5 (mth/line-dist [[0 0] [5 0]] [0 5]))))
  (testing "can find non-simple distances"
    (is (= 4.95 (roundme 2 (mth/line-dist [[0 0] [10 10]] [2 9])))))
  (testing "can find distance when line has start and end"
    (is (= 2 (mth/line-dist [[0 0] [0 5]] [0 7])))))
