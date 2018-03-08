(ns gravity-maze.interact.core-test
  (:require [gravity-maze.interact.core :as intr]
            [cljs.test :refer-macros [deftest testing is]]
            [gravity-maze.interact.building.validation :as bval]))

(deftest build-updater-test
  (testing "updates elements based on validations"
    (with-redefs [bval/add-errors (fn [el] (assoc el :tmp 0))
                  bval/do-validation-actions (fn [el] (update-in el [:tmp] inc))]
      (let [state {:elements [{:id 0}] :g 0}]
        (is (= {:elements [{:id 0 :tmp 1}] :g 0} (intr/build-updater state)))))))

