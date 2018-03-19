(ns gravity-maze.interact.building.core
  (:require [gravity-maze.interact.building.add :as add]
            [gravity-maze.swappers.interact.core :as intr!]
            [gravity-maze.interact.helpers :refer [find-elem
                                                   nothing
                                                   clicked?
                                                   click-range]]))

(defn build-mouse-press [atm {:keys [x y]}]
  (intr!/save-and-validate-tmp-elem! atm)
  (when-let [elem (find-elem (partial clicked? click-range [x y]) @atm)]
    (intr!/add-placeholder-elem! atm (:id elem))
    (intr!/set-tmp-elem! atm elem))
  atm)

(def click-fns {:building
                {:mouse-pressed build-mouse-press
                 :mouse-dragged nothing
                 :mouse-released nothing
                 :add add/click-fns}})
