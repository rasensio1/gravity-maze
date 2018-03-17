(ns gravity-maze.state.actions-test
  (:require [gravity-maze.state.actions :as act]
            [cljs.test :refer-macros [deftest testing is]]
            [gravity-maze.views.shooting :as shoot]
            [gravity-maze.engine :as eng]))

(deftest add-history-test
  (testing "Adds current state as history"
    (let [state {:history nil :ok :lol}
          res (act/add-history state)]
      (is (= {:ok :lol :history {:history nil :ok :lol}} res)))))

(deftest undo-test
  (testing "Can undo last action"
    (let [old-state {:history [] :elements [{:type :point}]}
          new-state { :elements [{:type :point} {:type :line}]
                     :history old-state}]
      (is (= (assoc old-state :fwd (dissoc new-state :history))
             (act/undo new-state)))))
  (testing "Does nothing if no history"
    (let [state {:history [] :elements [{:type :point}]}]
      (is (= state (act/undo state))))))

(deftest redo-test
  (testing "Can redo after an undo"
    (let [state {:history {:history nil :elements []}
                 :fwd nil
                 :elements [{:type :point}]}
          undone-state {:history nil
                        :elements []
                        :fwd {:fwd nil :elements [{:type :point}]}}]
      (is (= state (act/redo undone-state)))))
  (testing "Cant redo if fwd is empty"
    (let [state {:history {:history nil :elements []}
                 :fwd nil
                 :elements [{:type :point}]}]
      (is (= state (act/redo state))))))

(deftest shoot-start-test
  (testing "Can restart a game after shooting"
    (let [state {:hi :ok}]
      (is (= {:hi :ok :shoot-start state} (act/add-shoot-start state)))))
  (testing "Can click multiple times"
    (let [state {:hi :ok :shoot-start :blah}]
      (is (= {:hi :ok :shoot-start {:hi :ok}} (act/add-shoot-start state))))))

(deftest restart-test
  (testing "Can restart a game"
    (let [state {:hi :ok :lol :yeah}
          saved (act/add-shoot-start state)
          changed (assoc saved :hi :booo)]
      (is (= saved (act/restart changed))))))
