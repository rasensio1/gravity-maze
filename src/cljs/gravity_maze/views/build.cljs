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

(def line-params
  [:div {:field :container
         :visible? #(get-in % [:mode :building :line])}
   [:h3 "line-opts"]
   (build-param "Mass" :tmp.building.line.mass)
   (build-param "Range" :tmp.building.line.range)])

(def start-params
  [:div {:field :container
         :visible? #(get-in % [:mode :building :start])}
   [:h3 "start-opts"]
   (build-param "Mass" :tmp.building.start.mass)])

(def build-opts
  [:div.build-opts
   [:h3 "Options"]
   [:div.btn-group {:field :multi-select :id :tmp.building.options}
    [:button.btn.btn-default {:key :show-line-range} "Show line ranges"]
   ]])

(def build-mode-form
  [:div {:field :container
        :visible? #(contains? (:mode %) :building)}
   [:div.btn-group {:field :single-select :id :mode.building}
    [:button.btn.btn-default
     {:key {:line true}} "Add a line"]
    [:button.btn.btn-default
     {:key {:start true}} "Add the start"]]
   build-opts
   [:div.sub-options
    line-params
    start-params]])

