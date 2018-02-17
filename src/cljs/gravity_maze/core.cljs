(ns gravity-maze.core
  (:require
   [reagent.core :as reagent]
   [quil.core :as q :include-macros true]
   [quil.middleware :as m]))

(defonce app-state (reagent/atom {}))

(defn page [ratom]
  [:div
   [:div "Welcome to reagent-figwheel." ]])

(defn setup []
  {:radius 10})

(defn update-state [state]
  state)

(defn draw [state]
  (q/background 255)
  (q/fill 0)
  (let [r (:radius state)]
    (q/ellipse 56 46 r r)))

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
