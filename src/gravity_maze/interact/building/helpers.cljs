(ns gravity-maze.interact.building.helpers
  (:require [gravity-maze.state.actions :as act]
            [gravity-maze.helpers :as hlp]
            [gravity-maze.interact.building.validation :as bval]))

(defn save-and-validate-tmp-elem [state]
  (if-let [val-elem (-> state hlp/tmp-elem bval/add-errors)]
    (if-let [error (bval/some-not-saveable (:validation-errors val-elem))]
      (do ;; alert message and return original state
        (js/alert (str "Element not added. " (:message error))) state)
      (-> state
          act/remove-tmp-elem
          act/add-history
          (act/add-elem (bval/add-error-msgs val-elem))))
    state)) ;; if no 'tmp-elem' just return state

