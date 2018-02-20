(ns gravity-maze.engine-test
  (:require [gravity-maze.engine :as eng]
            [cljs.test :refer-macros [deftest testing is]]))

(def zero-point {:type :point
                 :mass 1
                 :pos [0 0]
                 :vel [0 0]
                 :accel [0 0]
                 :fixed false})

(def simple-point-world {:elements [zero-point
                                    (assoc zero-point
                                           :pos [1 1]
                                           :fixed true)]
                         :g 1
                         :dt 1})

(deftest div-v-test
  (testing "divides numerator by each element in vector"
    (is (= [3 1.5] (eng/div-v 3 [1 2]))))
  (testing "return zero force if distance is zero"
    (is (= [0 0] (eng/div-v 3 [0 0])))))

(deftest vec-exp-test
  (testing "exponents each element"
    (is (= [1 4 9] (eng/vec-exp 2 [1 2 3])))))

(deftest vec-sub-test
  (testing "subtracts first from second, element-wise"
    (is (= [1 4] (eng/vec-sub [3 4] [2 0])))))

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

(deftest calc-accel-test
  (testing "calculates acceleration given force and element"
    (is (= [0 0] (eng/calc-accel [0 0] zero-point)))
    (is (= [1 1] (eng/calc-accel [1 1] zero-point)))))

(deftest force-between-test
  (testing "Force between self is zero"
    (is (= [0 0] (eng/force-between 1 zero-point zero-point))
        (= [1 1] (eng/force-between 1 zero-point (assoc zero-point :pos [1 1]))))))

(deftest calc-force-test
  (testing "calculates the total force on an element"
    (is (= [1 1] (eng/calc-force zero-point simple-point-world)))))

(deftest update-elem-test
  (testing "updates element attrs given a world"
    (is (= {:vel [0.5 0.5] :accel [1 1]}
           (select-keys (eng/update-elem zero-point simple-point-world) [:vel :accel]))
        (let [res (->> (iterate #(eng/update-elem % simple-point-world) zero-point)
                      (take 3)
                      last)]
          (= [1 1] (:pos res))))))

(deftest update-world-test
  (testing "updates all non-fixed elements in state"
    (let [new-world (eng/update-world simple-point-world)]
      (is (= (set [[1 1] [0 0]]) (set (mapv :pos (:elements new-world)))))
      (is (= (set [[1 1] [0 0]]) (set (mapv :accel (:elements new-world)))))
      (is (= (set [[0.5 0.5] [0 0]]) (set (mapv :vel (:elements new-world))))))))

