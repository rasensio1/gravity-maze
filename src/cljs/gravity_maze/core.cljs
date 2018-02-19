(ns gravity-maze.core
  (:require
   [reagent.core :as reagent]
   [gravity-maze.state :as state]
   [gravity-maze.draw :as drw]
   [gravity-maze.engine :as eng]
   [quil.core :as q :include-macros true]
   [quil.middleware :as m]))

(defonce app-state (atom state/initial-state))

(defn page [ratom]
  [:div
   [:div "Welcome to reagent-figwheel." ]])

(defn setup [] @app-state)

(q/defsketch hello
  :draw drw/main
  :setup setup
  :host "host"
  :size [300 300]
  :update eng/update-state
  :middleware [m/fun-mode])

(defn reload []
  (reagent/render [page app-state]
                  (.getElementById js/document "app")))

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)))

(defn ^:export main []
  (dev-setup)
  (reload))
