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

(defn pressed? [elem]
  (if (:mousepress? elem) elem false))

(defn find-point
  "Returns point that is clicked, else nil.
  Event comes in like: {:x 101, :y 100, ...}"
  [filter-fn world]
  (->> (:elements world)
       (filter #(= :point (:type %)))
       (some filter-fn)))

(defn launch-mouse-press [ratom {:keys [x y]}]
  (when-let [point (find-point (partial clicked? click-range [x y]) @ratom)]
    (as-> (:elements @ratom) elems
      ;;      <vec> JS requires this to be vector, not lazySeq
      (update (vec elems) (:id point) #(assoc % :mousepress? true))
      (swap! ratom assoc :elements elems))) ;; TODO swap!s to separate ns
  ratom)

(defn launch-drag [ratom {:keys [x y]}]
  (when-let [point (find-point pressed? @ratom)] ;; get mousepressed point
    (as-> (:elements @ratom) elems
      (update (vec elems) (:id point)
              #(assoc % :drag-vec (mth/v- (:pos point) [x y])))
      (swap! ratom assoc :elements elems)))
  ratom)


(defn launch-mouse-release [ratom event]
  ;; if element has mousepress?
  ;; set velocity to (:drag-vec)
  ;; reset mousepress? false
  (println "released")
  ratom
  )
