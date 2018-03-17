(ns gravity-maze.state.mode-test
  (:require [gravity-maze.state.mode :as mode]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest enter-mode-test
  (testing "enters [:shooting] properly"
    (let [state {:hi :ok :mode {:building true}}
          res (mode/enter-mode state [:shooting])]
      ;; sets mode
      (is (= {:shooting true} (:mode res)))
      ;; sets restart state
      (is (= {:hi :ok :mode {:shooting true}} (:shoot-start res))))))


