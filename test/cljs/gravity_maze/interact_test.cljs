(ns gravity-maze.interact-test
  (:require [gravity-maze.interact :as intr]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest clicked?-test
  (testing "Knows if point pos is in range of click"
    (is (= true (intr/clicked? 20 [0 0] {:pos [10 10]})))
    (is (= true (intr/clicked? 10 [0 0] {:pos [0 0]})))
    (is (= true (intr/clicked? 25 [10 10] {:pos [27 27]}))))
  (testing "Knows if point pos isn't in range of click"
    (is (= false (intr/clicked? 5 [0 0] {:pos [10 10]})))
    (is (= false (intr/clicked? 10 [20 20] {:pos [35 20]})))))

;; (deftest on-point-test
;;   (testing "Returns point if clicked"
;;     (is (= {:type :point :id 1 :pos [10 10]}
;;            (intr/on-point? world [[0 0]])))
;;     )
;;   (testing "Returns nil if no point"
;;     )
;;   )
