(ns gravity-maze.interact.building.core
  (:require [gravity-maze.interact.building.add :as add]
            [gravity-maze.interact.building.main :as main]))

(def click-fns
  {:building (merge main/click-fns {:add add/click-fns})})
