(ns gravity-maze.core
  (:require
   [reagent.core :as reagent]
   [quil.core :as q :include-macros true]))

(defonce app-state (reagent/atom {}))

(defn page [ratom]
  [:div
   [:div "Welcome to reagent-figwheel." ]])

(defn draw []
  (q/background 255)
  (q/fill 0)
  (q/ellipse 56 46 55 55))

(q/defsketch hello
  :draw draw
  :host "host"
  :size [300 300])

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
