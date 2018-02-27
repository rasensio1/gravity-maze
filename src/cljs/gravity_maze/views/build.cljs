(ns gravity-maze.views.build
  (:require [reagent.core :as r]
            [reagent-forms.core :refer [bind-fields]]))

(defn building-mode? [state]
  (get-in state [:mode :building]))

(defn building-line? [state]
  (get-in state [:mode :building :line]))

(defn build-param [label id]
  [:div.param-group
   [:div [:p label]]
   [:div [:input.form-control
          {:field :numeric :id id}]]])

(def line-opts
  [:div [:h3 "line-opts"]
   (build-param "Mass" :tmp.build.line.mass)
   (build-param "Range" :tmp.build.line.range)])

(def start-opts
  [:div [:h3 "start-opts"]
   (build-param "Mass" :tmp.build.start.mass)])

(def build-mode-opts
  [:div
   [:div.btn-group {:field :single-select :id :mode.building}
    [:button.btn.btn-default
     {:key {:line true}} "Add a line"]
    [:button.btn.btn-default
     {:key {:start true}} "Add the start"]]
   [:div.sub-options
    line-opts
    start-opts]])

