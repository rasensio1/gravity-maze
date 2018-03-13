(ns gravity-maze.math.draw
  (:require [gravity-maze.math.helpers :as mth]))

(defn triangle
  "Returns three points that define a triangle,
  pointing in the given direction (tuple)"
  [head pointing size]
   (let [raw-base (map mth/unit-vec (mth/normal-vectors pointing))
         unit-base (map mth/unit-vec raw-base)
         sized-base (map (partial mth/mult-v size) unit-base)
         offset (mth/v- head (mth/mult-v size (mth/unit-vec pointing)))
         base (mth/offset-line mth/v+ offset sized-base)]
    (cons head base)))
