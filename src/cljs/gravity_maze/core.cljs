(ns gravity-maze.core
  (:require
   [reagent.core :as r]
   [gravity-maze.state :as state]
   [gravity-maze.engine :as eng]
   [gravity-maze.draw :as drw]
   [quil.core :as q :include-macros true]
   [quil.middleware :as m]))

(defonce app-state (r/atom state/initial-state))

(defn page [ratom]
  [:div
   [:div "Welcome to reagent-figwheel." ]])

(defn update-state [ratom]
  (reset! ratom (eng/update-world @ratom))
  ratom)

(defn setup []
  (q/frame-rate 100)
  (q/background 250)
  app-state)


(q/defsketch hello
  :setup setup
  :draw drw/main
  :update update-state
  :host "host"
  :size [800 800]
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
