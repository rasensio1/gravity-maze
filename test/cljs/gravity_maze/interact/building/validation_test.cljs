(ns gravity-maze.interact.building.validation-test
  (:require [gravity-maze.interact.building.validation :as bval]
            [cljs.test :refer-macros [deftest testing is]]
            [gravity-maze.math.helpers :as mth]))


(deftest mass-not-zero-test
  (testing "Knows when invalid"
    (is (not-empty (bval/mass-not-zero {:mass 0})))
    (is (empty? (bval/mass-not-zero {:mass 10})))))

(deftest length-not-zero-test
  (testing "Knows when invalid"
    (with-redefs [mth/pts-dist (fn [i j] 0)]
      (is (not-empty (bval/length-not-zero {:pos [[0 0] [0 0]]}))))
    (with-redefs [mth/pts-dist (fn [i j] 10)]
      (is (empty? (bval/length-not-zero {:pos [[0 0] [10 10]]}))))))

(deftest error-map-test
  (testing "Conglomerates errors"
    (with-redefs [bval/validators-for
                  {:point [(fn [el] {:hi :ok})
                           (fn [el] {:lol :yeah})
                           (fn [el] nil)]}]
      (is (= {:validation-errors [{:hi :ok} {:lol :yeah}]}
             (bval/error-map {:type :point})))))
  (testing "returns nil if no errors"
    (with-redefs [bval/validators-for
                  {:point [(fn [el] nil)
                           (fn [el] nil)]}]
      (is (= nil (bval/error-map {:type :point}))))))

(deftest add-errors-test
  (testing "Adds errors for each element"
    (with-redefs [bval/error-map (fn [el] {:hi :ok :id (:id el)})]
      (is (= [{:hi :ok :id 0} {:hi :ok :id 1} {:hi :ok :id 2}]
             (bval/add-errors [{:id 2} {:id 1} {:id 0}]))))))

