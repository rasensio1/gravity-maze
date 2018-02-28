(ns gravity-maze.draw
  (:require [quil.core :as q :include-macros true]
            [gravity-maze.math.helpers :as mth]
            [gravity-maze.helpers :refer [options get-kws]]
            [gravity-maze.math.draw :as mth-drw]))

(def arrow-size 5)

(defn draw-line-range [{:keys [pos range]}]
  ;; do stuff
  (println "drew range")
  )

(def draw-line-opts {:show-line-range draw-line-range})

(defn draw-arrow [pos drag-vec]
  (when drag-vec
    (let [end-drag (mth/v+ drag-vec pos)] ;; tip of arrow
      (q/line end-drag pos)
      (->> (mth-drw/triangle end-drag drag-vec arrow-size)
          flatten
          (apply q/triangle)))))

(defmulti draw-elem (fn [el opts] (:type el)))
(defmethod draw-elem :point [{:keys [pos r drag-vec] :or {r 5}} opts]
  (q/fill 0)
  (q/ellipse (pos 0) (pos 1) r r)
  (draw-arrow pos drag-vec))

(defmethod draw-elem :line [{:keys [pos] :as ln} opts]
  (doseq [opt opts]
    ((draw-line-opts opt) ln))
  (let [[a b] pos] (q/line a b)))

(defn main [state]
 (q/background 250)
  (let [opts (options (:tmp @state) (get-kws (:mode @state)))]
    (doseq [el (:elements @state)]
    (draw-elem el opts))))

