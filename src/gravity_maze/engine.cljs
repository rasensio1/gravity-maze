(ns gravity-maze.engine
  (:require [gravity-maze.math.helpers :as mth :refer [v+ v-
                                                       mult-v]]
            [gravity-maze.state :as st]))

(defn fixed-elem? [elem]
  (or (:fixed elem)
      (nil? (:fixed elem))))

(defn calc-accel [force {:keys [mass] :as el}]
  (mth/div-v mass force))

(defn gravity-calc [gmm d2 vec]
  (if (zero? d2) [0 0]
      (mult-v (/ gmm d2) vec)))

(defmulti in-zone?
  "Checks if a element is in the 'zone' of the other element.
  'zone' is determined by element-specific attrs (e.g :line -> within sides)
  (e.g. :point -> closer than :range)"
  #(:type %1)
  :hierarchy st/elem-hierarchy)

(defmethod in-zone? :line
  [line ball]
  (let [[line-pos pt-pos] (map :pos [line ball])
        offset (mult-v (:range line) (mth/unit-normal-vec line-pos pt-pos))
        [b-lines s-lines] ((juxt mth/base-sides mth/other-sides) line-pos offset)
        [b-score s-score] (map (partial mth/sum-by-dot-prod pt-pos)
                                       [b-lines s-lines])]
    (= [0.0 0.0] [b-score s-score])))

(defmethod in-zone? :point [{:keys [range] :as el-z} pt]
  (if range
    (> range (apply mth/pts-dist (map :pos [el-z pt])))
    true))

(defn is-finished?
  "Checks if a non-fixed point is within the range of a ':finish' element."
  [world]
  (let [finish-pts (filter #(= :finish (:type %)) (:elements world))
        moving-pts (filter (complement fixed-elem?)
                           (:elements world))]
    (boolean (some true? (for [f finish-pts
                      m moving-pts] (in-zone? f m))))))

(defmulti force-between
  "Calculates force between elem and ball."
  #(:type %1)
  :hierarchy st/elem-hierarchy)

(defmethod force-between :line [line ball g]
  (if (not (in-zone? line ball)) [0 0] ;; No force if point is outside of zone
      (let [inputs (map :pos [line ball])
            unit-force (apply mth/unit-normal-vec inputs)
            d2 (Math/pow (apply mth/line-dist inputs) 2)
            gmm (apply * (cons g (map :mass [ball line])))]
        (gravity-calc gmm d2 unit-force))))

(defmethod force-between :point [point ball g]
  (if (not (in-zone? point ball)) [0 0]
    (let [force-dir (apply v- (map :pos [point ball]))
          d2 (mth/sumsqs force-dir)
          unit-force (mth/unit-vec force-dir)
          gmm (apply * (cons g (map :mass [ball point])))]
    (gravity-calc gmm d2 unit-force))))

(defmethod force-between :default [def ball g] [0 0])

(defn sum-interactions [interaction el {:keys [elements g]}]
  (reduce (fn [agg el2] (v+ agg (interaction el2 el g))) [0 0] elements))

(defn update-elem
  "Uses the fourth order Runge-Kutta numerical integration
  to solve the position ODE.
  http://doswa.com/2009/01/02/fourth-order-runge-kutta-numerical-integration.html"

  [{:keys [id pos vel accel] :as el}
   {:keys [dt] :as world}]

  (letfn [(accel [el wld](calc-accel (sum-interactions
                                      force-between el wld) el))]
    (let [world2 (update-in world [:elements]
                          (fn [els] (remove #(= id (:id %)) els)))

          a1 (accel el world2)

          pos2 (v+ pos (mult-v (* 0.5 dt) vel))
          v2 (v+ vel (mult-v (* 0.5 dt) a1))
          a2 (accel (assoc el :pos pos2) world2)

          pos3 (v+ pos (mult-v (* 0.5 dt) v2))
          v3 (v+ vel (mult-v (* 0.5 dt) a2))
          a3 (accel (assoc el :pos pos3) world2)

          pos4 (v+ pos (mult-v dt v3))
          v4 (v+ vel (mult-v dt a3))
          a4 (accel (assoc el :pos pos4) world2)

          posf (v+ pos (mult-v (/ dt 6) (v+ vel (mult-v 2 v2)
                                            (mult-v 2 v3) v4)))

          vf (v+ vel (mult-v (/ dt 6) (v+ a1 (mult-v 2 a2)
                                         (mult-v 2 a3) a4)))]
    (assoc el :pos posf :vel vf))))

(defn update-world [world]
  (let [elems (:elements world)
        fixed (filter fixed-elem? elems)
        to-update (filter (complement fixed-elem?) elems)
        updated (map update-elem to-update (repeat world))]
    (assoc world
           ;; needs to be sorted, because we do some updates based
           ;; on index
           :elements (vec (sort-by :id (concat updated fixed)))
           :finished? (is-finished? world))))

