(ns gravity-maze.interact.shooting
  (:require [gravity-maze.math.helpers :as mth]
            [gravity-maze.interact.helpers :refer [find-elem
                                                   clicked?
                                                   pressed?]])
  (:require-macros [gravity-maze.macros :as mac]))

(def click-range 10)

(mac/defn-elem-set-update launch-mouse-press
   (fn [el] (when (= :point (:type el))
                ((partial clicked? click-range [x y]) el))))

(mac/defn-elem-update launch-drag
   (fn [el] (assoc el :drag-vec (mth/v- (:pos el) [x y]))))

(defn launch-mouse-release
  "Similar to save-and-validate-temp-elem, but without validators
  and with some prep to the element before saving"
  [atm _]
  (let [elem (get-in @atm [:tmp :editing-elem])]
    (swap! atm assoc-in [:tmp :editing-elem] nil)
    (when-let [new-vel (:drag-vec elem)]
      (as-> (assoc elem :vel new-vel :fixed false) prepped-elem
        (dissoc prepped-elem :drag-vec)
        (swap! atm assoc-in [:elements (:id elem)] prepped-elem))))
  atm)

(def click-fns {:shooting {:mouse-pressed launch-mouse-press
                           :mouse-dragged launch-drag
                           :mouse-released launch-mouse-release
                           }})

