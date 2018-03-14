(ns gravity-maze.draw.parts
  (:require
   [quil.core :as q]
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
