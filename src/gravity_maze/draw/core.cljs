(ns gravity-maze.draw.core
  (:require [quil.core :as q]
            [gravity-maze.draw.opts :as opt]
            [gravity-maze.draw.parts :as prt]
            [gravity-maze.helpers :refer [options
                                          get-kws
                                          tmp-elem]]))

(def point-r 10)
(def start-r 5)

(defmulti draw-elem (fn [el opts] (:type el)))
(defmethod draw-elem :point [{:keys [pos drag-vec] :as pt} opts]
  (opt/draw-opts opts opt/draw-point-opts pt)
  (q/fill 0)
  (q/ellipse (pos 0) (pos 1) point-r point-r))


(defmethod draw-elem :start [{:keys [pos drag-vec] :as start} opts]
  (opt/draw-opts opts opt/draw-start-opts start)
  (q/fill 0 255 0)
  (q/ellipse (pos 0) (pos 1) start-r start-r)
  (prt/draw-arrow pos drag-vec))

(defmethod draw-elem :line [{:keys [pos] :as ln} opts]
  (opt/draw-opts opts opt/draw-line-opts ln)
  (apply q/line pos))

(defmethod draw-elem :finish [{:keys [pos range] :as fin} opts]
  (opt/draw-opts opts opt/draw-finish-opts fin)
  (q/fill 100)
  (q/ellipse (pos 0) (pos 1) (* 2 range) (* 2 range)))

(defmethod draw-elem :default [i j] nil)

(defn main [state]
 (q/background 250)
  (let [opts (options (:tmp @state) (get-kws (:mode @state)))
        highlighted (assoc (tmp-elem @state) :highlight true)
        elems (conj (:elements @state) highlighted)]
    (doseq [el elems]
      (draw-elem el opts))))

