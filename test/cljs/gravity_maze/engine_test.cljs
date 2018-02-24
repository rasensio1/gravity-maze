(ns gravity-maze.engine-test
  (:require [gravity-maze.engine :as eng]
            [gravity-maze.math.helpers :as mth]
            [gravity-maze.test-helpers :refer [roundme]]
            [cljs.test :refer-macros [deftest testing is]]))

(def zero-point {:type :point
                 :mass 1
                 :pos [0 0]
                 :vel [0 0]
                 :accel [0 0]
                 :fixed false})

(def zero-x-line {:type :line
                  :mass 1
                  :range 3
                  :pos [[0 0] [0 10]]
                  :fixed true})

;; lines create force-gradients like so

;; 0 0 0 0 0 0 0 0 0 0 0
;; ... 1 2 3 | 3 2 1 ...
;; ... 1 2 3 | 3 2 1 ...
;; ... 1 2 3 | 3 2 1 ...
;; ... 1 2 3 | 3 2 1 ...
;; 0 0 0 0 0 0 0 0 0 0 0

(def simple-point-world {:elements [zero-point
                                    (assoc zero-point
                                           :pos [1 1]
                                           :fixed true)]
                         :g 1
                         :dt 1})

(defn simple-forces [i j k]
  (if (apply = (map :pos [j k]))
    [0 0] [1 1]))

(deftest base-sides-test
  (testing "returns base sides"
    (is (= [[[0 1] [1 1]] [[0 -1] [1 -1]]]
           (eng/base-sides [[0 0] [1 0]] [0 1])))))

(deftest other-sides-test
  (testing "Creates side lines"
    (let [line [[0 0] [3 0]]
          offset [-1 1]]
      (is (= [[[-1 1] [1 -1]] [[2 1] [4 -1]]]
             (eng/other-sides line offset))))))

(deftest in-zone?-test
  (testing "Point in zone returns true"
    (let [line {:pos [[0 0] [3 0]] :range 3}
          point {:pos [1 1]}]
      (is (= true (eng/in-zone? line point))))
    (let [line zero-x-line
          point (assoc zero-point :pos [2 1])]
      (is (= true (eng/in-zone? line point)))))
  (testing "Point outside of zone returns false"
    (let [line {:pos [[0 0] [3 0]] :range 3}
          point {:pos [1 4]}]
      (is (= false (eng/in-zone? line point))))))

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

(deftest gravity-calc-test
  (testing "returns zero with zero distance"
    (is (= [0 0] (eng/gravity-calc 1 0 [1 1]))))
  (testing "calculates gravity"
    (is (= [1 1] (eng/gravity-calc 1 1 [1 1])))))

(deftest force-between-test
  ;; Point - Point
  (testing "Force between self is zero"
    (is (= [0 0] (eng/force-between 1 zero-point zero-point))))
  (testing "Force in x=y direction"
    (let [res (eng/force-between 1 zero-point (assoc zero-point :pos [1 1]))
          fmt-res (mapv (partial roundme 2) res)]
      (is (= ["0.35" "0.35"] fmt-res))))
  (testing "Force in x-direction mainly"
    (let [res (eng/force-between 1 zero-point (assoc zero-point :pos [10 1]))
          fmt-res (mapv (partial roundme 3) res)]
      (is (= ["0.010" "0.001"] fmt-res))))

  ;; Point - line
  (testing "Calculates force"
    (let [point (assoc zero-point :pos [1 1])]
      (is (= [-1 0] (eng/force-between 1 point zero-x-line)))))

  (comment (testing "Zero force if on line"
    (let [point {:type :point :mass 30 :fixed false}
          line {:type :line :mass 30 :pos [[0 0] [0 200] :fixed true]}]
      (is (= [0 0] (eng/force-between 1 point line))))))
  (comment (testing "Zero force if not in 'zone' of line"
    (let [point {:type :point :mass 30 :pos [10 10] :fixed false}
          line {:type :line :mass 30 :pos [[0 20] [0 200] :fixed true]}]
      (is (= [0 0] (eng/force-between 1 point line)))))))

(deftest sum-interactions-test
  (testing "calculates the total force on an element"
    (is (= [2 2] (eng/sum-interactions (fn [g h i] [1 1])
                                       zero-point
                                       simple-point-world)))))

(deftest update-elem-test
  (with-redefs [eng/force-between simple-forces]
    (testing "updates element attrs given a point world"
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

