(ns gravity-maze.engine
  (:require [gravity-maze.math.helpers :as mth :refer [v+ v- mult-v]]))

(defn calc-accel [force {:keys [mass] :as el}]
  (mth/div-v mass force))

(defn gravity-calc [gmm d2 vec]
  (if (zero? d2) [0 0]
      (mult-v (/ gmm d2) vec)))

(defn base-sides
  "Returns base sides of zone by offsetting in both + and - dirs."
  [line offset]
  ((juxt (partial mth/offset-line v+ offset)
       (partial mth/offset-line v- offset)) line))

(defn other-sides
  "Returns sides of zone by doubling normal vectors at line ends."
  [line offset] (mapv #((juxt v+ v-) % offset) line))

(defn sum-by-dot-prod [point lines]
  (reduce #(+ %1 (mth/perp-dot-prod %2 point)) 0 lines))

(defn in-zone?
  "Checks if a point is in the 'zone' of the line. Determines bounds of 'zone'
  (base-lines, side-lines) and checks if point is inside by making sure it is
  above and below one of each group of lines "
  [line point]
  (let [[line-pos pt-pos] (map :pos [line point])
        offset (mult-v (:range line) (mth/unit-normal-vec line-pos pt-pos))
        [b-lines s-lines] ((juxt base-sides other-sides) line-pos offset)
        [b-score s-score] (map (partial sum-by-dot-prod pt-pos)
                                       [b-lines s-lines])]
    (= [0.0 0.0] [b-score s-score])))

(defmulti force-between (fn [g e1 e2] (e2 :type)))

(defmethod force-between :line [g el line]
  (if (not (in-zone? line el)) [0 0] ;; No force if point is outside of zone
      (let [inputs (map :pos [line el])
            unit-force (apply mth/unit-normal-vec inputs)
            d2 (Math/pow (apply mth/line-dist inputs) 2)
            gmm (apply * (cons g (map :mass [el line])))]
        (gravity-calc gmm d2 unit-force))))

(defmethod force-between :point [g el1 point]
  (let [force-dir (apply v- (map :pos [point el1]))
        d2 (mth/sumsqs force-dir)
        unit-force (mth/unit-vec force-dir)
        gmm (apply * (cons g (map :mass [el1 point])))]
    (gravity-calc gmm d2 unit-force)))

(defn sum-interactions [interaction el {:keys [elements g]}]
  (reduce (fn [agg el2] (v+ agg (interaction g el el2))) [0 0] elements))

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
        fixed (filter :fixed elems)
        to-update (filter (complement :fixed) elems)
        updated (map update-elem to-update (repeat world))]
    (assoc world :elements (vec (concat updated fixed)))))
