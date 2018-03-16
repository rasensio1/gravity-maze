(ns gravity-maze.engine-test
  (:require [gravity-maze.engine :as eng]
            [gravity-maze.math.helpers :as mth]
            [gravity-maze.test-helpers :refer [roundme]]
            [cljs.test :refer-macros [deftest testing is]]))

(def zero-point {:type :point
                 :pos [0 0]
                 :id 0
                 :range 20
                 :mass 1
                 :fixed true})

(def zero-start {:type :start
                 :pos [0 0]
                 :vel [0 0]
                 :id 2
                 :mass 1
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

(def simple-point-world {:elements [zero-start
                                    (assoc zero-point :pos [1 1])]
                         :g 1
                         :dt 1})

(defn simple-forces [i j k]
  (if (apply = (map :pos [j k]))
    [0 0] [1 1]))

(deftest in-zone?-test
  ;; :line
  (testing "Point in line zone returns true"
    (let [line {:type :line :pos [[0 0] [3 0]] :range 3}
          point {:pos [1 1]}]
      (is (= true (eng/in-zone? line point))))
    (let [line zero-x-line
          point (assoc zero-point :pos [2 1])]
      (is (= true (eng/in-zone? line point)))))
  (testing "Point outside of line zone returns false"
    (let [line {:type :line :pos [[0 0] [3 0]] :range 3}
          point {:pos [1 4]}]
      (is (= false (eng/in-zone? line point)))))

  ;; :point
  (testing "Point outside of point zone returns false"
    (let [point-z {:type :point :pos [10 10] :range 10}
          point {:pos [20 20]}]
      (is (= false (eng/in-zone? point-z point)))))
  (testing "start inside of point zone returns true"
    (let [point-z {:type :point :pos [10 10] :range 20}
          point {:pos [20 20]}]
      (is (= true (eng/in-zone? point-z point)))))

  ;; :finish
  (testing ":finish elem is a sub-group of :point"
    (let [point-z {:type :finish :pos [10 10] :range 20}
          point {:pos [20 20]}]
      (is (= true (eng/in-zone? point-z point))))))

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
  (testing "Force between self is zero"
    (is (= [0 0] (eng/force-between zero-start zero-start 1))))

  ;; Start - Point
  (testing "Force in x=y direction"
    (let [res (eng/force-between (assoc zero-point :pos [1 1]) zero-start  1)
          fmt-res (mapv (partial roundme 2) res)]
      (is (= [0.35 0.35] fmt-res))))

  (testing "Force in x-direction mainly"
    (let [res (eng/force-between (assoc zero-point :pos [10 1]) zero-start 1)
          fmt-res (mapv (partial roundme 3) res)]
      (is (= [0.010 0.001] fmt-res))))
  (testing "No force when ouside of range"
    (let [res (eng/force-between (assoc zero-point :range 1)
                                 (assoc zero-start :pos [10 1])
                                 10)
          fmt-res (mapv (partial roundme 3) res)]
      (is (= [0 0] fmt-res))))

  ;; Point - line
  (testing "Calculates force"
    (let [point (assoc zero-point :pos [1 1])]
      (is (= [-1 0] (eng/force-between zero-x-line point 1)))))
  (testing "Zero force if on line"
    (let [point {:type :point :mass 30 :fixed false}
          line {:type :line :mass 30 :pos [[0 0] [0 200]] :fixed true}]
      (is (= [0 0] (eng/force-between line point 1)))))
  (testing "Zero force if not in 'zone' of line"
    (let [point {:type :point :mass 30 :pos [10 10] :fixed false}
          line {:type :line :range 1 :mass 30 :pos [[0 20] [0 200]] :fixed true}]
      (is (= [0 0] (eng/force-between line point 1)))))

  ;; start - start
  (testing "Calculates force"
    (let [start (assoc zero-start :pos [1 0] :id 100)]
      (is (= [-1 0] (eng/force-between zero-start start 1)))))

  ;; anything else
  (testing "other elements always returns zero"
    (let [point {:type :point :mass 30 :pos [10 10] :fixed false}
          finish {:type :finish :pos [0 20] :fixed true}]
      (is (= [0 0] (eng/force-between point finish 1 )))))
    (let [point {:type :point :mass 30 :pos [10 10] :fixed false}
          niler {:type nil :id 10}]
      (is (= [0 0] (eng/force-between point niler 1 )))))

(deftest sum-interactions-test
  (testing "calculates the total force on an element"
    (is (= [2 2] (eng/sum-interactions (fn [g h i] [1 1])
                                       zero-point
                                       simple-point-world)))))

(deftest update-elem-test
  (with-redefs [eng/force-between simple-forces]
    (testing "updates element attrs given a point world"
    (let [res (eng/update-elem zero-start simple-point-world)]
      (is (= [1 1] (:vel res))))

    (let [res (->> (iterate #(eng/update-elem % simple-point-world) zero-start)
                   (take 3)
                   last)]
      (is (= [2 2] (map (partial roundme 2) (:pos res))))))))

(deftest update-world-test
  (with-redefs [eng/force-between simple-forces]
    (testing "updates all non-fixed start elements in state"
      (let [new-world (eng/update-world simple-point-world)]
        (is (= (set [[1 1] [0.5 0.5]]) (set (mapv :pos (:elements new-world)))))
        (is (= (set [[1 1] nil]) (set (mapv :vel (:elements new-world)))))))
    (testing "updates :finished attr when appropriate"
      (with-redefs [eng/update-elem (fn [x] x)
                    eng/is-finished? (fn [x] true)]
        (let [world {:elements [{:type :point}
                                {:type :finish}]
                     :finished? false}
              res (eng/update-world world)]
          (is (= true (:finished? res))))))))

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

