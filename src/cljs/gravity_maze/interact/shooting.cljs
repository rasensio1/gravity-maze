(ns gravity-maze.interact.shooting
  (:require [gravity-maze.math.helpers :as mth]
            [gravity-maze.interact.helpers :refer [find-elem
                                                   clicked?
                                                   pressed?]])
  (:require-macros [gravity-maze.macros :as mac]))

(def click-range 10)

(mac/defn-elem-update launch-mouse-press
  {:criteria #(when (= :point (:type %))
                ((partial clicked? click-range [x y]) %))
   :updater (fn [el] (assoc el :mousepress? true))})

(mac/defn-elem-update launch-drag
  {:criteria pressed?
   :updater (fn [el] (assoc el :drag-vec (mth/v- (:pos point) [x y])))})

(mac/defn-elem-update launch-mouse-release
  {:criteria pressed?
   :updater (fn [el] (-> (if-let [new-vel (:drag-vec el)]
                 (assoc el :vel new-vel :fixed false) el)
               (dissoc :mousepress? :drag-vec)))})

(def click-fns {:shooting {:mouse-pressed launch-mouse-press
                           :mouse-dragged launch-drag
                           :mouse-released launch-mouse-release
                           }})
