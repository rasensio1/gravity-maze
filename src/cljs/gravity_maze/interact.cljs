(ns gravity-maze.interact
  (:require [gravity-maze.math.helpers :as mth])
  (:require-macros [gravity-maze.macros :as mac]))

;; future stuff...
;; for handling clicks in different modes...

;; {:launch {:handle-click "somefn"
;;           :mouse-dragged "some"
;;           ...}

              ;nested modes [:build :add-line]
;;  :build {:add-line {:handle-click "somethingelse"
;;                     ...}
;;          :add-end {:handle-click "lkjsf"}
;;          }
;;  }

(def click-range 10)

(defn clicked? [range click-pos {:keys [pos] :as elem}]
  (if (>= range (mth/pts-dist click-pos pos))
    elem false))

(defn pressed? [elem]
  (if (:mousepress? elem) elem false))

(defn drag-vec? [elem]
  (if (:drag-vec elem) elem false))

(defn find-point
  "Returns point based on filter-fn, else nil."
  [filter-fn world]
  (->> (:elements world)
       (filter #(= :point (:type %)))
       (some filter-fn)))

(mac/defn-point-event launch-mouse-press
  (partial clicked? click-range [x y])
  (fn [el] (assoc el :mousepress? true)))

(mac/defn-point-event launch-drag
  pressed?
  (fn [el] (assoc el :drag-vec (mth/v- (:pos point) [x y]))))

(mac/defn-point-event launch-mouse-release
  drag-vec?
  (fn [el] (as-> el point
               (assoc point :vel (:drag-vec el))
               (assoc point :fixed false)
               (dissoc point :mousepress? :drag-vec))))

