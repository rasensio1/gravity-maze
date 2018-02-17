(ns gravity-maze.core
  (:require
   [reagent.core :as reagent]))

(defonce app-state (reagent/atom {}))

(defonce context
  #(-> (.getElementById js/document "ctxt")
       (.getContext "2d")))

(defn context-container [ratom]
  [:canvas#ctxt])

(defn page [ratom]
  [:div
   [:div "Welcome to reagent-figwheel." ]
   (context-container ratom)])



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    (println "dev mode")))

(defn reload []
  (reagent/render [page app-state]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (reload)
  (context))
