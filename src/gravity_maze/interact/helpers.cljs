(ns gravity-maze.interact.helpers
  (:require [gravity-maze.math.helpers :as mth]
            [gravity-maze.state.core :as st]))

(def click-range 10)

(defn find-elem
  "Returns element based on filter-fn, else nil."
  [filter-fn world]
  (->> (:elements world)
       (some filter-fn)))

(defmulti clicked?
  (fn [range click-pos elem] (:type elem))
  :hierarchy st/elem-hierarchy)

(defmethod clicked? :point [range click-pos {:keys [pos] :as elem}]
  (if (>= range (mth/pts-dist click-pos pos))
    elem false))

(defmethod clicked? :line [range click-pos {:keys [pos] :as line}]
  (if (>= range (mth/line-dist pos click-pos))
    line false))

(defmethod clicked? :default [i j k] false)

(defn drag-vec? [elem]
  (if (:drag-vec elem) elem false))

(defn nothing [atm _] atm)
