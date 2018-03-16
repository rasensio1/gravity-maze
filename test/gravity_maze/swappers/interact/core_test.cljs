(ns gravity-maze.swappers.interact.core-test
  (:require [gravity-maze.swappers.interact.core :as swp]
            [gravity-maze.test-helpers :refer [temp-elem]]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest add-placeholder-elem!-test
  (testing "does the job"
    (let [atm (atom {})
          res (swp/add-placeholder-elem! atm 1)]
     (is (= 1 (count (:elements @atm)))))))

(deftest set-tmp-elem!-test
  (testing "does the job"
    (let [atm (atom {})
          res (swp/set-tmp-elem! atm {:hi :ok})]
      (is (= {:hi :ok} (temp-elem @atm))))))

