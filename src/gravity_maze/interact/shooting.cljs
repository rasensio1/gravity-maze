(ns gravity-maze.interact.shooting
  (:require [gravity-maze.math.helpers :as mth]
            [gravity-maze.swappers.interact.core :as intr!]
            [gravity-maze.interact.helpers :refer [find-elem
                                                   clicked?
                                                   click-range]])
  (:require-macros [gravity-maze.macros :as mac]))

(defn launchable?
  "Returns launchable element if clicked, else nil"
  [{:keys [x y]} el ]
  (when (= :start (:type el))
    ((partial clicked? click-range [x y]) el)))

(defn launch-mouse-press [atm event]
    (when-let [elem (find-elem (partial launchable? event) @atm)]
      (intr!/set-tmp-elem! atm elem))
    atm)

(mac/defn-elem-update launch-drag
   (fn [el] (assoc el :drag-vec (mth/v- (:pos el) [x y]))))

(defn launch-mouse-release
  "Sets velocity of clicked point, removes it from tmp and adds to
  :elements. "
  [atm _]
  (let [elem (get-in @atm [:tmp :editing-elem])]
    (when-let [new-vel (:drag-vec elem)]
      (intr!/remove-tmp-elem! atm)
      (as-> (assoc elem :vel new-vel :fixed false) prepped-elem
        (dissoc prepped-elem :drag-vec)
        (intr!/add-elem! atm prepped-elem))))
  atm)

(def click-fns {:shooting {:mouse-pressed launch-mouse-press
                           :mouse-dragged launch-drag
                           :mouse-released launch-mouse-release}})

