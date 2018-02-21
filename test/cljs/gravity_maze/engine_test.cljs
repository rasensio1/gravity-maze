(ns gravity-maze.engine-test
  (:require [gravity-maze.engine :as eng]
            [cljs.test :refer-macros [deftest testing is]]))

(def zero-point {:type :point
                 :mass 1
                 :pos [0 0]
                 :vel [0 0]
                 :accel [0 0]
                 :fixed false})

(def zero-x-line {:type :line
                  :mass 1
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

(defn roundme [decs n]
  (.toFixed n decs))

(defn simple-forces [i j k]
  (if (apply = (map :pos [j k]))
    [0 0] [1 1]))

(deftest v--test
  (testing "subtracts first from second, element-wise"
    (is (= [1 4] (eng/v- [3 4] [2 0])))))

(deftest unit-vec-test
  (testing "can do a [0 0]"
    (is (= [0 0] (eng/unit-vec [0 0]))))
  (testing "can do a 1x1"
    (let [exp (/ (Math/sqrt 2) 2)
          res (first (eng/unit-vec [1 1]))]
      (is (apply = (map (partial roundme 2) [exp res]))))))

(deftest pts-dist-test
  (testing "can do right triangles"
    (is (= 5 (eng/pts-dist [0 0] [3 4])))
    (is (= 5 (eng/pts-dist [1 1] [4 5]))))
  (testing "can do non-right triangles"
    (is (= "8.06" (roundme 2 (eng/pts-dist [1 1] [2 9]))))))

(deftest det3x3-test
  (testing "can calculate determinant"
    (is (= 0 (eng/det3x3 [[1 4] [2 5] [3 6]])))
    (is (= -9 (eng/det3x3 [[10 4] [2 5] [3 6]])))))

(deftest line-dist-test
  (testing "can find simple distances"
    (is (= 2 (eng/line-dist [[0 0] [5 0]] [0 2])))
    (is (= 5 (eng/line-dist [[0 0] [5 0]] [0 5]))))
  (testing "can find non-simple distances"
    (is (= "4.95" (roundme 2 (eng/line-dist [[0 0] [10 10]] [2 9]))))))

(deftest unit-normal-vec-test
  (testing "can find the normal vector of a line"
    (let [res (eng/unit-normal-vec [[0 0] [2 20]] [1 12])]
      (is (= ["0.995" "-0.100"] (mapv (partial roundme 3) res))))
    (let [res (eng/unit-normal-vec [[3 7] [7 3]] [6 6])]
      (is (= ["-0.7" "-0.7"] (mapv (partial roundme 1) res)))))
  (testing "point on line returns [0 0]"
    (is (= [0 0] (eng/unit-normal-vec [[0 0] [3 3]] [2 2])))))

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
    (let [point (assoc zero-point :pos [1 0])]
      (is (= [-1 0] (eng/force-between 1 point zero-x-line)))))

  (testing "Zero force if on line"
    (let [point {:type :point :mass 30 :fixed false}
          line {:type :line :mass 30 :pos [[0 0] [0 200] :fixed true]}]
      (is (= [0 0] (eng/force-between 1 point line)))))
  (testing "Zero force if not in 'zone' of line"
    (let [point {:type :point :mass 30 :pos [10 10] :fixed false}
          line {:type :line :mass 30 :pos [[0 20] [0 200] :fixed true]}]
      (is (= [0 0] (eng/force-between 1 point line))))))

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

