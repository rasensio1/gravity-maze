(ns gravity-maze.helpers-test
  (:require [gravity-maze.helpers :as hps]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest get-kws-test
  (testing "Gets all keywords from a map"
    (is (= [:hi :ok] (hps/get-kws {:hi {:ok true}})))))

(deftest options-test
  (testing "gets all values within :options vectors"
    (let [tree {:lol {:options [:hi]
                      :another {:options [:ok]
                                :thing true}}}]
      (is (= [:hi :ok] (hps/options tree [:lol :another]))))
    (let [tree {:options [:cat]
                :lol {:options [:hi]
                      :another {:options [:ok]
                                :thing true}}}]
      (is (= [:cat :hi :ok] (hps/options tree [:lol :another]))))))

