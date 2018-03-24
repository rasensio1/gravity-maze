(ns gravity-maze.interact.building.main
  (:require [gravity-maze.swappers.interact.core :as intr!]
            [gravity-maze.math.helpers :as mth]
            [gravity-maze.state.core :as st]
            [gravity-maze.helpers :as hlp]
            [gravity-maze.interact.helpers :refer [find-elem
                                                   nothing
                                                   clicked?
                                                   click-range]]))

(defmulti transpose (fn [el drag-pos & init-pos] (:type el))
  :hierarchy st/elem-hierarchy)

(defmethod transpose :line [el drag-pos init-pos]
  (let [disp (mth/v- drag-pos init-pos)]
    (map (partial mth/v+ disp) (:pos el))))

(defmethod transpose :point [el drag-pos] drag-pos)

(defmethod transpose :default [i j] [0 0])

(defn build-mouse-drag [atm {:keys [x y]}]
  (when (hlp/tmp-elem @atm)
    (let [init-pos (hlp/init-click @atm)]
      (swap! atm assoc-in [:tmp :click-pos] [x y])
      (swap! atm update-in [:tmp :editing-elem]
             (fn [el] (assoc el :pos (transpose el [x y] init-pos)))) ))
  atm)

(defn build-mouse-press [atm {:keys [x y]}]
  (intr!/save-and-validate-tmp-elem! atm)
  (when-let [elem (find-elem (partial clicked? click-range [x y]) @atm)]
    (intr!/add-placeholder-elem! atm (:id elem))
    (intr!/set-tmp-elem! atm elem)
    (intr!/set-tmp-click-pos! atm [x y]))
  atm)

(def click-fns
  {:mouse-pressed build-mouse-press
   :mouse-dragged build-mouse-drag
   :mouse-released nothing})
