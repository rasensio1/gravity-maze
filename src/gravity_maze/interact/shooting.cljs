(ns gravity-maze.interact.shooting
  (:require [gravity-maze.math.helpers :as mth]
            [gravity-maze.swappers.interact.core :as intr!]
            [gravity-maze.interact.helpers :refer [find-elem
                                                   clicked?]])
  (:require-macros [gravity-maze.macros :as mac]))

(def click-range 10)

(mac/defn-elem-set-update launch-mouse-press
   (fn [el] (when (= :point (:type el))
                ((partial clicked? click-range [x y]) el))))

(mac/defn-elem-update launch-drag
   (fn [el] (assoc el :drag-vec (mth/v- (:pos el) [x y]))))

(defn launch-mouse-release
  "Sets velocity of clicked point, removes it from tmp and adds to
  :elements. "
  [atm _]
  (let [elem (get-in @atm [:tmp :editing-elem])]
    (intr!/remove-tmp-elem! atm)
    (when-let [new-vel (:drag-vec elem)]
      (as-> (assoc elem :vel new-vel :fixed false) prepped-elem
        (dissoc prepped-elem :drag-vec)
        (intr!/add-elem! atm prepped-elem))))
  atm)

(def click-fns {:shooting {:mouse-pressed launch-mouse-press
                           :mouse-dragged launch-drag
                           :mouse-released launch-mouse-release
                           }})

