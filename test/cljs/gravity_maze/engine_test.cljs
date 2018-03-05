(ns gravity-maze.engine-test
  (:require [gravity-maze.engine :as eng]
            [gravity-maze.math.helpers :as mth]
            [gravity-maze.test-helpers :refer [roundme]]
            [cljs.test :refer-macros [deftest testing is]]))

(def zero-point {:type :point
                 :id 0
                 :mass 1
                 :pos [0 0]
                 :vel [0 0]
                 :fixed false})

(def zero-x-line {:type :line
                  :id 1
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
                                           :id 2
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
      (is (= [0.35 0.35] fmt-res))))
  (testing "Force in x-direction mainly"
    (let [res (eng/force-between 1 zero-point (assoc zero-point :pos [10 1]))
          fmt-res (mapv (partial roundme 3) res)]
      (is (= [0.010 0.001] fmt-res))))

  ;; Point - line
  (testing "Calculates force"
    (let [point (assoc zero-point :pos [1 1])]
      (is (= [-1 0] (eng/force-between 1 point zero-x-line)))))
  (testing "Zero force if on line"
    (let [point {:type :point :mass 30 :fixed false}
          line {:type :line :mass 30 :pos [[0 0] [0 200]] :fixed true}]
      (is (= [0 0] (eng/force-between 1 point line)))))
  (testing "Zero force if not in 'zone' of line"
    (let [point {:type :point :mass 30 :pos [10 10] :fixed false}
          line {:type :line :mass 30 :pos [[0 20] [0 200]] :fixed true}]
      (is (= [0 0] (eng/force-between 1 point line)))))

  ;; :finish
  (testing "Finish points Always returns zero"
    (let [point {:type :point :mass 30 :pos [10 10] :fixed false}
          finish {:type :finish :pos [0 20] :fixed true}]
      (is (= [0 0] (eng/force-between 1 point finish))))))

(deftest sum-interactions-test
  (testing "calculates the total force on an element"
    (is (= [2 2] (eng/sum-interactions (fn [g h i] [1 1])
                                       zero-point
                                       simple-point-world)))))

(deftest update-elem-test
  (with-redefs [eng/force-between simple-forces]
    (testing "updates element attrs given a point world"
    (let [res (eng/update-elem zero-point simple-point-world)]
      (is (= [1 1] (:vel res))))

    (let [res (->> (iterate #(eng/update-elem % simple-point-world) zero-point)
                   (take 3)
                   last)]
      (is (= [1.83 1.83] (map (partial roundme 2) (:pos res))))))))

(deftest update-world-test
  (with-redefs [eng/force-between simple-forces]
    (testing "updates all non-fixed elements in state"
      (let [new-world (eng/update-world simple-point-world)]
        (is (= (set [[1 1] [0.5 0.5]]) (set (mapv :pos (:elements new-world)))))
        (is (= (set [[1 1] [0 0]]) (set (mapv :vel (:elements new-world)))))))
    (testing "updates :finished attr when appropriate"
      (with-redefs [eng/update-elem (fn [x] x)
                    eng/is-finished? (fn [x] true)]
        (let [world {:elements [{:type :point}
                                {:type :finish}]
                     :finished? false}
              res (eng/update-world world)]
          (is (= true (:finished? res))))))))

(deftest in-finish?-test
  (testing "Knows when true"
    (let [fin {:pos [0 0] :range 5}
          pt {:pos [3 3]}]
      (is (= true (eng/in-finish? fin pt)))))
  (testing "Knows when false"
    (let [fin {:pos [0 0] :range 5}
          pt {:pos [3 6]}]
      (is (= false (eng/in-finish? fin pt))))))

(deftest is-finished?-test
  (testing "Can tell when a game is finished"
    (let [world {:elements [{:type :point :pos [10 10] :fixed false}
                            {:type :finish :pos [20 20] :range 15}]}]
      (is (= true (eng/is-finished? world))))
    (let [world {:elements [{:type :point :pos [10 10] :fixed true}
                            {:type :point :pos [20 30] :fixed false}
                            {:type :finish :pos [20 20] :range 15}]}]
      (is (= true (eng/is-finished? world))))
    (let [world {:elements [{:type :point :pos [10 10] :fixed true}
                            {:type :point :pos [20 30] :fixed false}
                            {:type :point :pos [85 85] :fixed false}
                            {:type :finish :pos [20 20] :range 1}
                            {:type :finish :pos [80 80] :range 10}]}]
      (is (= true (eng/is-finished? world)))))
  (testing "Knows when a game is not finished"
    (let [world {:elements [{:type :point :pos [10 10] :fixed true}
                            {:type :point :pos [20 30] :fixed false}
                            {:type :finish :pos [20 20] :range 1 :fixed true}
                            {:type :finish :pos [80 80] :range 10 :fixed true}]}]
      (is (= false (eng/is-finished? world))))))

