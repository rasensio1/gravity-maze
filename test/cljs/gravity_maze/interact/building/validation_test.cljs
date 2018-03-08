(ns gravity-maze.interact.building.validation-test
  (:require [gravity-maze.interact.building.validation :as bval]
            [cljs.test :refer-macros [deftest testing is]]
            [gravity-maze.math.helpers :as mth]))

(deftest range-not-zero-test
  (testing "Knows when invalid"
    (is (not-empty (bval/range-not-zero {:range 0})))
    (is (empty? (bval/range-not-zero {:range 10})))))

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
      (is (=  {:hi :ok :id 2} (bval/add-errors {:id 2}))))))

(deftest add-error-msgs-action-test
  (testing "Adds error messages"
    (let [elem {:id 0 :validation-errors [{:message "HI"}
                                          {:message "ok"}]}]
      (is (= ["HI" "ok"]
             (:error-msgs (bval/add-error-msgs-action elem))))))
  (testing "Doesn't add message when no validation-errors"
    (let [elem {:id 0 :validation-errors []}
          res (bval/add-error-msgs-action elem)]
      (is (= {:id 0} res )))))

(deftest delete-action-test
  (with-redefs [js/alert println]
    (testing "Removes an element"
      (is (= {:id 0 :type nil}
             (bval/delete-action {:id 0} {:message "booo"})))
      (is (= "Element not added. booo\n"
             (with-out-str (bval/delete-action {:id 0} {:message "booo"})))))))

(deftest do-validation-actions-test
  (testing "deletes when apropos"
    (with-redefs [bval/delete-action (fn [i j] "deleted")]
      (let [elem {:validation-errors [{:action :delete}]}]
        (is (= "deleted" (bval/do-validation-actions elem)))))))
