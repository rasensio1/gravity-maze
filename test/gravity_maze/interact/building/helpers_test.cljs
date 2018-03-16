(ns gravity-maze.interact.building.helpers-test
  (:require [gravity-maze.interact.building.helpers :as hlp]
            [gravity-maze.interact.building.validation :as bval]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest save-and-validate-temp-elem-test
  (testing "Adds valid element"
    (let [line {:type :line :pos [[0 0 ] [10 10]] :id 2}
          myatm (atom {:elements [:hi :ok] :tmp {:editing-elem line}})]
      (hlp/save-and-validate-tmp-elem myatm {})
      (is (= line (last (:elements @myatm))))
      (is (= 3 (count (:elements @myatm))))))
  (testing "Doesn't add invalid element"
    (let [line {:type :line :pos [[0 0 ] [10 10]] :id 2}
          myatm (atom {:elements [:hi :ok] :tmp {:editing-elem line}})]
      (with-redefs [bval/some-not-saveable (fn [x] {:message "HI"})]
        (hlp/save-and-validate-tmp-elem myatm {}))
      (is (= 2 (count (:elements @myatm)))))))
