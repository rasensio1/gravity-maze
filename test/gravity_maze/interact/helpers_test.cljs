(ns gravity-maze.interact.helpers-test
  (:require [gravity-maze.interact.helpers :as hlp]
            [cljs.test :refer-macros [deftest testing is]]))

(deftest find-elem-test
  (testing "Returns elem based on filter"
    (let [point {:type :point :pos [5 5]}
          world {:elements [point]}]
      (is (= true (hlp/find-elem #(= [5 5] (:pos %)) world))))
    (let [point {:type :point :pos [5 5] :id 1}
          world {:elements [{:type :point :pos [10 10]} point]}]
      (is (= point (hlp/find-elem #(if (:id %) % nil) world))))
    (let [line {:type :line :pos [[5 5] [10 10]] :id 1}
          world {:elements [{:type :point :pos [10 10]} line]}]
      (is (= line (hlp/find-elem #(if (= :line (:type %)) % nil) world))))
    (let [point {:type :point :pos [0 0] :id 0}
          world {:elements [point]}
          myfn #(when (= :point (:type %))
                  ((partial hlp/clicked? 20 [0 0]) %))]
      (is (= point (hlp/find-elem myfn world)))))

  (testing "Returns nil if no point"
    (let [point {:type :point :pos [20 20]}
          world {:elements [{:type :point :pos [10 10]} point]}]
      (is (= nil (hlp/find-elem #(= [0 0] (:pos %)) world))))))

(deftest clicked?-test
  (testing "Knows if point pos is in range of click"
    (let [pt {:pos [10 10] :type :point}]
      (is (= pt (hlp/clicked? 20 [0 0] pt))))
    (let [pt {:pos [0 0] :type :point}]
      (is (= pt (hlp/clicked? 10 [0 0] pt))) )
    (let [pt {:pos [27 27] :type :point}]
      (is (= pt (hlp/clicked? 25 [10 10] pt)))))
  (testing "Knows if point pos isn't in range of click"
    (is (= false (hlp/clicked? 5 [0 0] {:pos [10 10] :type :point})))
    (is (= false (hlp/clicked? 10 [20 20] {:pos [35 20] :type :point})))))
