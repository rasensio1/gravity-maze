(ns gravity-maze.engine)

(defn update-state [state]
  ;; calculate new el values
  ;; swap with app-state atom
  ;; return updated @app-state
  state)

(defn v+ [& args]
  (apply (partial map +) args))

(defn v* [n vec]
  (map #(* n %) vec))

(defn v-div [n vec]
  (v* (/ 1 n) vec))

(defn div-v
  " 3 / [1 2] => [3/1 3/2]
  Vector value of zero returns zero"
  [n vec]
  (map #(if (= 0 %)
          0
          (/ n %)) vec))

(defn vec-sub [vec1 vec2]
  (map - vec1 vec2))

(defn vec-exp [n vec]
  (map #(Math/pow % n) vec))

(defn update-pos [dt {:keys [pos vel accel] :as el}]
  (let [velocity-disp (v* dt vel)
        accel-disp (v* (* 0.5 dt dt) accel)
        new-pos (v+ pos velocity-disp accel-disp)]
    (assoc el :pos new-pos)))

(defn update-vel [dt new-accel {:keys [vel accel] :as el}]
  (let [avg-accel (v-div 2 (v+ accel new-accel))
        accel-dv (v* dt avg-accel)
        new-vel (v+ vel accel-dv)]
    (assoc el :vel new-vel)))

(defn calc-accel [force {:keys [mass] :as el}]
  (v-div mass force))

(defn update-accel [new-accel el]
  (assoc el :accel new-accel))

(defn force-between [g el1 el2]
  (let [gmm (apply * (cons g (map :mass [el1 el2])))
        dist (apply vec-sub (map :pos [el2 el1]))
        d2 (vec-exp 2 dist)]
    (div-v gmm d2)))

(defn calc-force [el {:keys [elements g]}]
  (reduce (fn [agg el2] (v+ agg (force-between g el el2))) [0 0] elements))

;; (defn update-elem [el {:keys [dt] :as world}]
;;   ;;calculate force on element -> (new accel)
;;   ;; update pos -> velocity (w/ new accel) -> accel
;;   (let [force (calc-force el world)
;;         new-accel (calc-accel force el)]
;;     (->> el
;;         (update-pos dt)
;;         (update-vel dt new-accel)
;;         (update-accel new-accel)
;;       ) ))
