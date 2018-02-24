(ns gravity-maze.core
  (:require
   [reagent.core :as r]
   [gravity-maze.state :as state]
   [gravity-maze.engine :as eng]
   [gravity-maze.draw :as drw]
   [gravity-maze.interact :as int]
   [quil.core :as q :include-macros true]
   [quil.middleware :as m]))

(def app-state (r/atom state/initial-state))

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
  :features [:no-start]
  :draw drw/main
  :update update-state
  :host "host"
  :mouse-dragged int/launch-drag
  :mouse-pressed int/launch-mouse-press
  :mouse-released int/launch-mouse-release
  :size [600 600]
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

(hello)
