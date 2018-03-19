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
      (is (= {:hi :ok :mode {:shooting true}} (:shoot-start res)))))

  (testing "enters [:building] properly"
    (let [state {:hi :ok :mode {:shooting true}}
          res (mode/enter-mode state [:building])]
      ;; sets mode
      (is (= {:building true} (:mode res))))))

(deftest exit-mode-test
  (testing "exits [:shooting] properly"
    (let [state {:shoot-start {:lol :yeah} :hi :ok :mode {:shooting true}}
          res (mode/exit-mode state)]
      (is (= (dissoc state :shoot-start) res))))

  (testing "exits [:building] properly"
    (let [elem {:id 0 :hi :ok}
          state {:elements []
                 :mode {:building true}
                 :tmp {:editing-elem elem}}
          res (mode/exit-mode state)]
      (is (= [elem] (:elements res))))))

