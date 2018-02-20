(ns gravity-maze.draw
  (:require
   [gravity-maze.engine :as eng]
   [quil.core :as q :include-macros true]))

(defmulti draw-elem (fn [x] (x :type)))
(defmethod draw-elem :point [{:keys [pos r] :or {r 10}}]
  (q/fill 0)
  (q/ellipse (pos 0) (pos 1) r r))

(defmethod draw-elem :line [{:keys [pos]}]
  (let [[a b] pos] (q/line a b)))

(defn main [state]
  (q/background 255)
  (doseq [el (:elements @state)]
    (draw-elem el))
  (reset! state (eng/update-world @state))
  (js/setTimeout #(main state) 500))

