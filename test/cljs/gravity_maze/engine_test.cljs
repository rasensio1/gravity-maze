(ns gravity-maze.engine-test
  (:require [gravity-maze.engine :as eng]
            [cljs.test :refer-macros [deftest testing is]]))

(def zero-point {:type :point
                 :mass 0
                 :pos [0 0]
                 :vel [0 0]
                 :accel [0 0]
                 :fixed false})

(deftest update-pos-test
  (testing "Updates a position with velocity and acceleration"
    (is (= zero-point (eng/update-pos 1 zero-point)))
    (let [init (assoc zero-point :vel [0 1])
          res (assoc zero-point :pos [0 1] :vel [0 1])]
      (is (= res (eng/update-pos 1 init))))
    (let [init (assoc zero-point :vel [1 1] :accel [1 1])
          res (assoc zero-point :pos [1.5 1.5] :vel [1 1] :accel [1 1])]
      (is (= res (eng/update-pos 1 init))))))

(deftest update-vel-test
  (testing "updates the velocity with acceleration"
    (is (= zero-point (eng/update-vel 1 [0 0] zero-point)))
    (is (= {:vel [1 1] :accel [2 2]}
           (eng/update-vel 1 [0 0] {:vel [0 0 ] :accel [2 2]})))))

