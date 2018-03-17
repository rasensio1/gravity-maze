(ns gravity-maze.interact.building.helpers-test
  (:require [gravity-maze.interact.building.helpers :as hlp]
            [gravity-maze.interact.building.validation :as bval]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest save-and-validate-temp-elem-test
  (testing "Adds valid element"
    (let [line {:type :line :mass 10 :pos [[0 0] [10 10]] :range 10 :id 2}
          state {:elements [:hi :ok] :tmp {:editing-elem line}}
          res (hlp/save-and-validate-tmp-elem state)]
      (is (= line (last (:elements res))))
      (is (= 3 (count (:elements res))))))
  (testing "Doesn't add invalid element"
    (with-redefs [bval/some-not-saveable (fn [x] {:message "HI"})]
      (let [line {:type :line :pos [[0 0 ] [10 10]] :id 2}
            state {:elements [:hi :ok] :tmp {:editing-elem line}}
            res (hlp/save-and-validate-tmp-elem state)]
        (is (= 2 (count (:elements res))))))))
