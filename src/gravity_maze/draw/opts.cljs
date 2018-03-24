(ns gravity-maze.draw.opts
  (:require [quil.core :as q]
            [gravity-maze.math.helpers :as mth]
            [gravity-maze.state.core :as st]))

(defn context []
  (-> (.getElementById js/document "host")
      (.getContext "2d")))

(defn highlight-circle [{:keys [pos range]}]
  (q/fill 0 255 255)
  (let [ctxt (context)
        [x y] pos]
    (.save ctxt)
    (.beginPath ctxt)
    (.arc ctxt x y (+ 8 range) 0 (* 2 Math/PI))
    (goog.object/set ctxt "fillStyle" "#00FFFF")
    (goog.object/set ctxt "strokeStyle" "transparent")
    (.fill ctxt)
    (.stroke ctxt)
    (.restore ctxt)))

(defmulti highlight  (fn [el] (when (:highlight el) (:type el)))
  :hierarchy st/elem-hierarchy)

(defmethod highlight :start [st]
  (highlight-circle (assoc st :range 8)))

(defmethod highlight :line [{:keys [pos]}]
  (let [ctxt (context)]
   (goog.object/set ctxt "lineWidth" 3)
   (goog.object/set ctxt "strokeStyle" "#00FFFF"))
  (apply q/line pos))

(defmethod highlight :finish [el]
  (highlight-circle el))

(defmethod highlight :default [el] nil)

(defmulti draw-range (fn [el] (:type el))
  :hierarchy st/elem-hierarchy)

(defmethod draw-range :point [{:keys [pos range]}]
  (let [[x y] pos
        ctxt (context)
        grad (.createRadialGradient ctxt x y range x y 0)]
    (do
      (.save ctxt)
      (.addColorStop grad 0 "transparent")
      (.addColorStop grad 1 "pink")
      (.beginPath ctxt)
      (.arc ctxt x y, range, 0, (* 2 Math/PI))
      (goog.object/set ctxt "fillStyle" grad)
      (.fill ctxt)
      (.restore ctxt))))

(defmethod draw-range :line [{:keys [pos range]}]
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

(defmethod draw-range :default [el] nil)


(def opts-fns {:highlight highlight
               :show-range draw-range})

(defn draw-opts [elem-opts elem]
  (doseq [opt elem-opts]
    ((opts-fns opt) elem)))
