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

(defn roundme [round n]
  (.toFixed n round))

(defn simple-forces [i j k]
  (if (apply = (map :pos [j k]))
    [0 0] [1 1]))

(deftest div-v-test
  (testing "divides numerator by each element in vector"
    (is (= [3 1.5] (eng/div-v 3 [1 2]))))
  (testing "return zero force if distance is zero"
    (is (= [0 0] (eng/div-v 3 [0 0])))))

(deftest v--test
  (testing "subtracts first from second, element-wise"
    (is (= [1 4] (eng/v- [3 4] [2 0])))))

(deftest unit-vec-test
  (testing "can do a [0 0]"
    (is (= [0 0] (eng/unit-vec [0 0]))))
  (testing "can do a 1x1"
    (let [exp (/ (Math/sqrt 2) 2)
          res (first (eng/unit-vec [1 1]))])
    (is (= exp res))))

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
    (is (= [0 0] (eng/force-between 1 zero-point zero-point))))
  (testing "Force in x=y direction"
    (let [res (eng/force-between 1 zero-point (assoc zero-point :pos [1 1]))
          fmt-res (mapv (partial roundme 2) res)]
      (is (= ["0.35" "0.35"] fmt-res))))
  (testing "Force in x-direction mainly"
    (let [res (eng/force-between 1 zero-point (assoc zero-point :pos [10 1]))
          fmt-res (mapv (partial roundme 3) res)]
      (is (= ["0.010" "0.001"] fmt-res)))))

(deftest sum-interactions-test
  (testing "calculates the total force on an element"
    (is (= [2 2] (eng/sum-interactions (fn [g h i] [1 1])
                                       zero-point
                                       simple-point-world)))))
(deftest update-elem-test
  (with-redefs [eng/force-between simple-forces]
    (testing "updates element attrs given a world"
    (let [res (select-keys (eng/update-elem zero-point simple-point-world)
                           [:vel :accel])]
      (is (= {:vel [0.5 0.5] :accel [1 1]} res)))

    (let [res (->> (iterate #(eng/update-elem % simple-point-world) zero-point)
                   (take 3)
                   last)]
      (is (= [1 1] (:pos res)))))))

(deftest update-world-test
  (with-redefs [eng/force-between simple-forces]
    (testing "updates all non-fixed elements in state"
      (let [new-world (eng/update-world simple-point-world)]
        (is (= (set [[1 1] [0 0]]) (set (mapv :pos (:elements new-world)))))
        (is (= (set [[1 1] [0 0]]) (set (mapv :accel (:elements new-world)))))
        (is (= (set [[0.5 0.5] [0 0]]) (set (mapv :vel (:elements new-world)))))))))

