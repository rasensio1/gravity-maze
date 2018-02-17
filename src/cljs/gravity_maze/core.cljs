(ns gravity-maze.core
  (:require
   [reagent.core :as reagent]
   [gravity-maze.state :as state]
   [gravity-maze.draw :as drw]
   [quil.core :as q :include-macros true]
   [quil.middleware :as m]))

(defonce app-state (reagent/atom {}))

(defn page [ratom]
  [:div
   [:div "Welcome to reagent-figwheel." ]])

(defn setup [] state/initial-state)

(defn update-state [state]
  state)

(q/defsketch hello
  :draw drw/main
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
