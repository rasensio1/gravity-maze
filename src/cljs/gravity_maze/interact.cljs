(ns gravity-maze.interact
  (:require [gravity-maze.math.helpers :as mth]))

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

(defn find-point
  "Returns point that is clicked, else nil.
  Event comes in like: {:x 101, :y 100, ...}"
  [world {:keys [x y]}]
  (->> (:elements world)
      (filter #(= :point (:type %)))
      (some (partial clicked? click-range [x y]))))

(defn launch-mouse-press [ratom event]
  (when-let [point (find-point @ratom event)]
    (as-> (:elements @ratom) elems
      (update (vec elems) (:id point) #(assoc % :mousepress? true))
      (swap! ratom assoc :elements elems)))
  ratom)

(defn launch-drag [ratom event]
  ratom)


(defn launch-mouse-release [ratom event]
  (println "released")
  ratom
  )
