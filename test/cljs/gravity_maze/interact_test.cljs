(ns gravity-maze.interact-test
  (:require [gravity-maze.interact :as intr]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest clicked?-test
  (testing "Knows if point pos is in range of click"
    (let [pt {:pos [10 10]}]
      (is (= pt (intr/clicked? 20 [0 0] pt))))
    (let [pt {:pos [0 0]}]
      (is (= pt (intr/clicked? 10 [0 0] pt))) )
    (let [pt {:pos [27 27]}]
      (is (= pt (intr/clicked? 25 [10 10] pt)))))
  (testing "Knows if point pos isn't in range of click"
    (is (= false (intr/clicked? 5 [0 0] {:pos [10 10]})))
    (is (= false (intr/clicked? 10 [20 20] {:pos [35 20]})))))

(deftest find-point-test
  (testing "Returns point if clicked"
    (let [point {:type :point :pos [5 5]}
          world {:elements [point]}]
      (is (= point (intr/find-point world [0 0]))))
    (let [point {:type :point :pos [5 5]}
          world {:elements [{:type :point :pos [10 10]} point]}]
      (is (= point (intr/find-point world [0 0])))))
  (testing "Returns nil if no point"
    (let [point {:type :point :pos [20 20]}
          world {:elements [{:type :point :pos [10 10]} point]}]
      (is (= nil (intr/find-point world [0 0]))))))
