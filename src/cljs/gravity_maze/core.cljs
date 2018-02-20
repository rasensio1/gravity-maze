(ns gravity-maze.core
  (:require
   [reagent.core :as r]
   [gravity-maze.state :as state]
   [gravity-maze.draw :as drw]
   [gravity-maze.engine :as eng]
   [quil.core :as q :include-macros true]
   [quil.middleware :as m]))

(defonce app-state (r/atom state/initial-state))

(defn page [ratom]
  [:div
   [:div "Welcome to reagent-figwheel." ]])

(q/defsketch hello
  :draw #(drw/main app-state)
  :host "host"
  :size [300 300]
  :middleware [m/fun-mode])

(defn reload []
  (r/render [page app-state]
                  (.getElementById js/document "app")))

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)))

(defn ^:export main []
  (dev-setup)
  (reload))
