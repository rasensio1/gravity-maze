(ns gravity-maze.core
  (:require
   [reagent.core :as r]
   [gravity-maze.state :as state]
   [gravity-maze.engine :as eng]
   [gravity-maze.draw :as drw]
   [gravity-maze.interact :as int]
   [gravity-maze.views.core :as vw]
   [quil.core :as q :include-macros true]
   [quil.middleware :as m]))

(def app-state (r/atom state/initial-state))

(defn update-state [ratom]
  (when-not (:finished? @ratom)
    (reset! ratom (eng/update-world @ratom))) ratom)

(defn setup []
  (q/frame-rate 100)
  (q/background 250)
  app-state)

(defn mouse-handler [event-name]
  (fn [ratom event] (int/handle-mouse event-name ratom event)))

(q/defsketch hello
  :setup setup
  :draw drw/main
  :update update-state
  :host "host"
  :mouse-dragged (mouse-handler :mouse-dragged)
  :mouse-pressed (mouse-handler :mouse-pressed)
  :mouse-released (mouse-handler :mouse-released)
  :size [600 600]
  :middleware [m/fun-mode]
  )

(defn reload []
  (r/render vw/title
            (.getElementById js/document "title"))

  (r/render [vw/dash app-state]
            (.getElementById js/document "dash")))

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)))

(defn ^:export main []
  (dev-setup)
  (reload))
