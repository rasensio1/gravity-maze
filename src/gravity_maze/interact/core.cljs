(ns gravity-maze.interact.core
  (:require [gravity-maze.helpers :refer [get-mode]]
            [gravity-maze.interact.shooting :as shoot]
            [gravity-maze.interact.building.core :as build]
            [gravity-maze.interact.building.validation :as bval]
            [gravity-maze.math.helpers :as mth]))

(def click-fns (merge shoot/click-fns
                      build/click-fns))

(defn handle-mouse [event-name ratom event]
  (let [path (conj (get-mode @ratom) event-name)]
    ((get-in click-fns path) ratom event)))


