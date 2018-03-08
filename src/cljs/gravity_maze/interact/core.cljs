(ns gravity-maze.interact.core
  (:require [gravity-maze.helpers :refer [get-kws]]
            [gravity-maze.interact.shooting :as shoot]
            [gravity-maze.interact.building.core :as build]
            [gravity-maze.interact.building.validation :as bval]
            [gravity-maze.math.helpers :as mth]))

(def click-fns (merge shoot/click-fns
                      build/click-fns))

(defn handle-mouse [event-name ratom event]
  (let [path (conj (get-kws (:mode @ratom)) event-name)]
    ((get-in click-fns path) ratom event)))

;; TODO test
(defn build-updater
  "Validates each element, then performs an 'action'
  on the element (delting it,or adding error msgs)"
  [{:keys [elements] :as state}]
  (let [validated-elems (bval/add-errors elements)]
    (map bval/do-validation-actions validated-elems)))
