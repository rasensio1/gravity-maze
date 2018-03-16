(ns gravity-maze.interact.building.edit
  (:require [gravity-maze.interact.building.helpers :as bhlp]
            [gravity-maze.swappers.interact.core :as intr!]
            [gravity-maze.interact.helpers :refer [nothing
                                                   find-elem
                                                   clicked?
                                                   click-range]])
  (:require-macros [gravity-maze.macros :as mac]))

(defn edit-mouse-press [atm {:keys [x y]}]
  (bhlp/save-and-validate-tmp-elem atm nil)
    (when-let [elem (find-elem (partial clicked? click-range [x y]) @atm)]
      (intr!/add-placeholder-elem! atm (:id elem))
      (intr!/set-tmp-elem! atm elem))
  atm)



(def click-fns {:edit
                 {:mouse-pressed edit-mouse-press
                  ;; :mouse-dragged edit-mouse-drag
                  ;; :mouse-released edit-mouse-release
                  }})
