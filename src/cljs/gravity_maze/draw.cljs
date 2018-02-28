(ns gravity-maze.draw
  (:require [quil.core :as q :include-macros true]
            [gravity-maze.math.helpers :as mth]
            [gravity-maze.math.draw :as mth-drw]))

(def arrow-size 5)

(defn draw-arrow [pos drag-vec]
  (when drag-vec
    (let [end-drag (mth/v+ drag-vec pos)] ;; tip of arrow
      (q/line end-drag pos)
      (->> (mth-drw/triangle end-drag drag-vec arrow-size)
          flatten
          (apply q/triangle)))))

(defmulti draw-elem :type)
(defmethod draw-elem :point [{:keys [pos r drag-vec] :or {r 10}}]
  (q/fill 0)
  (q/ellipse (pos 0) (pos 1) r r)
  (draw-arrow pos drag-vec))

(defmethod draw-elem :line [{:keys [pos]}]
  (let [[a b] pos] (q/line a b))
  )

(defn options [tree]
  ;; get all ":options {stuff}" from along the "mode" path.
  )

(defn main [state]
 (q/background 250)
  (doseq [el (:elements @state)]
    (draw-elem el (options (:tmp @state)))))

