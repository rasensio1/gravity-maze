(ns gravity-maze.interact.building.core
  (:require [gravity-maze.interact.building.add :as add]
            [gravity-maze.interact.building.edit :as edit]))

(def click-fns {:building (merge add/click-fns edit/click-fns)})
