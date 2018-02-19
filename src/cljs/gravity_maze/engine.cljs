(ns gravity-maze.engine)

(defn update-state [state]
  ;; calculate new el values
  ;; swap with app-state atom
  ;; return updated @app-state
  state)

(defn v+ [& args]
  (apply (partial map +) args))
/
(defn v* [n vec]
  (map #(* n %) vec))

(defn v-div [n vec]
  (v* (/ 1 n) vec))

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

;; ;; for each vector
;; position += velocity * time_step + ( 0.5 * last_acceleration * time_step^2 )
;; new_acceleration = force / mass 
;; avg_acceleration = ( last_acceleration + new_acceleration ) / 2
;; velocity += avg_acceleration * time_step
