(ns gravity-maze.core
  (:require
   [reagent.core :as reagent]
   [gravity-maze.state :as state]
   [quil.core :as q :include-macros true]
   [quil.middleware :as m]))

(defonce app-state (reagent/atom {}))

(defn page [ratom]
  [:div
   [:div "Welcome to reagent-figwheel." ]])

(defn setup []
  state/initial-state)

(defn update-state [state]
  state)

(defmulti draw-elem (fn [x] (x :type)))
(defmethod draw-elem :point [{:keys [pos r] :or {r 10}}]
    (q/fill 0)
    (q/ellipse (pos 0) (pos 1) r r))

(defmethod draw-elem :line [{:keys [pos]}]
  (let [[a b] pos]
    (q/line a b)))

(defn draw-point [{:keys [pos]}]
  (q/fill 0)
  (q/ellipse (pos 0) (pos 1) 10 10))

(defn draw [state]
  (q/background 255)
  (doseq [el (:elements state)]
    (draw-elem el)))

(q/defsketch hello
  :draw draw
  :setup setup
  :host "host"
  :size [300 300]
  :update update-state
  :middleware [m/fun-mode])

(defn reload []
  (reagent/render [page app-state]
                  (.getElementById js/document "app")))

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    (println "dev mode")))

(defn ^:export main []
  (dev-setup)
  (reload))
