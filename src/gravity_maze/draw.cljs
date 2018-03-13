(ns gravity-maze.draw
  (:require [quil.core :as q :include-macros true]
            [gravity-maze.math.helpers :as mth]
            [gravity-maze.helpers :refer [options
                                          get-kws
                                          tmp-elem]]
            [gravity-maze.math.draw :as mth-drw]))

(def arrow-size 5)

(defn context []
  (-> (.getElementById js/document "host")
      (.getContext "2d")))

(defn draw-line-range [{:keys [pos range]}]
  (let [[ptA ptB] pos
        [[ptAx ptAy][ptBx ptBy]] [ptA ptB]
        n-vecs (mth/normal-vectors (mth/v- ptB ptA))
        range-vecs (map (comp (partial mth/mult-v range)
                              mth/unit-vec)
                        n-vecs )
        [[stX stY] [edX edY]] (map #(mth/v+ ptA %) range-vecs)
        ctxt (context)
        grad (.createLinearGradient ctxt stX stY edX edY)]
    (do
      (.save ctxt)
      (.addColorStop grad 0 "transparent")
      (.addColorStop grad 0.5 "pink")
      (.addColorStop grad 1 "transparent")
      (goog.object/set ctxt "strokeStyle" grad)
      (.beginPath ctxt)
      (.moveTo ctxt ptAx ptAy)
      (goog.object/set ctxt "lineWidth" (* 2 range))
      (goog.object/set ctxt "lineCap" "butt")
      (.lineTo ctxt ptBx ptBy)
      (.stroke ctxt)
      (.restore ctxt))))

(def draw-line-opts {:show-line-range draw-line-range})

(defn draw-arrow [pos drag-vec]
  (when drag-vec
    (let [end-drag (mth/v+ drag-vec pos)] ;; tip of arrow
      (q/line end-drag pos)
      (->> (mth-drw/triangle end-drag drag-vec arrow-size)
          flatten
          (apply q/triangle)))))

(defmulti draw-elem (fn [el opts] (:type el)))
(defmethod draw-elem :point [{:keys [pos r drag-vec] :or {r 10}} opts]
  (q/fill 0)
  (q/ellipse (pos 0) (pos 1) r r))

(defmethod draw-elem :start [{:keys [pos r drag-vec] :or {r 5}} opts]
  (q/fill 0 255 0)
  (q/ellipse (pos 0) (pos 1) r r)
  (draw-arrow pos drag-vec))

(defmethod draw-elem :line [{:keys [pos] :as ln} opts]
  (doseq [opt opts]
    ((draw-line-opts opt) ln))
  (let [[a b] pos] (q/line a b)))

(defmethod draw-elem :finish [{:keys [pos range] :as fin} opts]
  (q/fill 100)
  (q/ellipse (pos 0) (pos 1) (* 2 range) (* 2 range)))

(defmethod draw-elem :default [i j] nil)

(defn main [state]
 (q/background 250)
  (let [opts (options (:tmp @state) (get-kws (:mode @state)))]
    (doseq [el (conj (:elements @state) (tmp-elem @state))]
    (draw-elem el opts))))

