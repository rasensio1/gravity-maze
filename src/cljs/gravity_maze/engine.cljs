(ns gravity-maze.engine)

(defn update-state [state]
  ;; calculate new el values
  ;; swap with app-state atom
  ;; return updated @app-state
  state)

(defn addv [& args]
  (apply (partial map +) args))

(defn multv [n vec]
  (map #(* n %) vec))

(defn update-pos [dt {:keys [pos vel accel] :as el}]
  (let [velocity-disp (multv dt vel)
        accel-disp (multv (* 0.5 dt dt) accel)
        new-pos (addv pos velocity-disp accel-disp)]
    (assoc el :pos new-pos)))

;; ;; for each vector
;; position += velocity * time_step + ( 0.5 * last_acceleration * time_step^2 )
;; new_acceleration = force / mass 
;; avg_acceleration = ( last_acceleration + new_acceleration ) / 2
;; velocity += avg_acceleration * time_step
