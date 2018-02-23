(ns gravity-maze.interact-test
  (:require [gravity-maze.interact :as intr]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest on-point-test
  (testing "Returns point if clicked"
    (is (= 0 1))
    )
  (testing "Returns nil if no point"
    )
  )
