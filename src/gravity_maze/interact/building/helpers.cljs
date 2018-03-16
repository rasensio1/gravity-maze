(ns gravity-maze.interact.building.helpers
  (:require [gravity-maze.swappers.state :as st!]
            [gravity-maze.interact.building.validation :as bval]
            [gravity-maze.swappers.interact.core :as intr!]))

(defn save-and-validate-tmp-elem
  "Validates and saves a element in temp storage. If not saveable,
  shows error message and does not save."
  [atm _]
  (when-let [validated-elem (bval/add-errors (get-in @atm [:tmp :editing-elem]))]
    (intr!/remove-tmp-elem! atm)

    (if-let [error (bval/some-not-saveable (:validation-errors validated-elem))]
      (js/alert (str "Element not added. " (:message error)))
      ;; else, add error messages, save and clear the tmp
      (let [elem (bval/add-error-msgs validated-elem)]
        (do (st!/add-history! atm) (intr!/add-elem! atm elem)))))
  atm)
