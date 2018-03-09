(ns gravity-maze.state-test
  (:require [gravity-maze.state :as st]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest add-history!-test
  (testing "Adds current state as history"
    (let [atm (atom {:history nil :ok :lol})
          res (st/add-history! atm)]
      (is (= {:ok :lol :history {:history nil :ok :lol}} @atm)))))

(deftest undo-test
  (testing "Can undo last action"
    (let [old-state {:history [] :elements [{:type :point}]}
          new-state { :elements [{:type :point} {:type :line}]
                     :history old-state}]
      (is (= (assoc old-state :fwd (dissoc new-state :history))
             (st/undo new-state)))))
  (testing "Does nothing if no history"
    (let [state {:history [] :elements [{:type :point}]}]
      (is (= state (st/undo state))))))

(deftest redo-test
  (testing "Can redo after an undo"
    (let [state {:history {:history nil :elements []}
                 :fwd nil
                 :elements [{:type :point}]}
          undone-state {:history nil
                        :elements []
                        :fwd {:fwd nil :elements [{:type :point}]}}]
      (is (= state (st/redo undone-state)))))
  (testing "Cant redo if fwd is empty"
    (let [state {:history {:history nil :elements []}
                 :fwd nil
                 :elements [{:type :point}]}]
      (is (= state (st/redo state))))))

