(ns gravity-maze.state-test
  (:require [gravity-maze.state :as st]
            [cljs.test :refer-macros [deftest testing is]]
            [gravity-maze.views.shooting :as shoot]
            [gravity-maze.engine :as eng]))

(deftest add-history-test
  (testing "Adds current state as history"
    (let [state {:history nil :ok :lol}
          res (st/add-history state)]
      (is (= {:ok :lol :history {:history nil :ok :lol}} res)))))

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

(comment (deftest restart-test
  (testing "Can restart after shooting a point"
    (let [point {:type :point :mass 50 :pos [0 0]
                 :vel [10 10] :fixed false :id 0}
          state (atom (assoc st/initial-state :elements [point]))
          shooting (shoot/shooting-btn-click state)
          res (last (take 10 (iterate eng/update-world shooting)))]
      (is (= shooting (st/restart res)))))
  (testing "Can restart multiple times"
    (let [point {:type :point :mass 50 :pos [0 0]
                 :vel [10 10] :fixed false :id 0}
          state (atom (assoc st/initial-state :elements [point]))
          shooting (shoot/shooting-btn-click state)
          res (last (take 10 (iterate eng/update-world shooting)))
          restarted (last (take 5 (iterate st/restart res)))]
      (is (= shooting restarted))))))

