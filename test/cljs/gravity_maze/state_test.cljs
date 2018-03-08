(ns gravity-maze.state-test
  (:require [gravity-maze.state :as st]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest undo-test
  (testing "Can undo last action"
    ;; given history tree, resets to state before
    (let [old-state {:history [] :elements [{:type :point}]}
          new-state { :elements [{:type :point} {:type :line}]
                     :history old-state}]
      (is (= (assoc old-state :fwd (dissoc new-state :history))
             (st/undo new-state))))))

(deftest redo-test
  (testing "Can redo after an undo"
    (let [state {:history {:history nil :elements []}
                 :fwd nil
                 :elements [{:type :point}]}
          undone-state {:history nil
                        :elements []
                        :fwd {:fwd nil :elements [{:type :point}]}}]
      (is (= state (st/redo undone-state))))))

