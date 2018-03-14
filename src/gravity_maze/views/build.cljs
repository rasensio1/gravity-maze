(ns gravity-maze.views.build
  (:require [reagent.core :as r]
            [gravity-maze.swappers.state :as st!]))

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

(def point-params
  [:div {:field :container
         :visible? #(get-in % [:mode :building :point])}
   [:h3 "point-opts"]
   (build-param "Mass" :tmp.building.point.mass)
   (build-param "Range" :tmp.building.point.range)])

(def start-params
  [:div {:field :container
         :visible? #(get-in % [:mode :building :start])}
   [:h3 "start-opts"]
   (build-param "Mass" :tmp.building.start.mass)])

(def finish-params
  [:div {:field :container
         :visible? #(get-in % [:mode :building :finish])}
   [:h3 "start-opts"]
   (build-param "Range" :tmp.building.finish.range)])

(def build-opts
  [:div.build-opts
   [:h3 "Options"]
   [:div.btn-group {:field :multi-select :id :tmp.building.options}
    [:button.btn.btn-default {:key :show-line-range} "Show line ranges"]
    [:button.btn.btn-default {:key :show-point-range} "Show point ranges"]]])

(def build-sub-modes
  [:div.btn-group {:field :single-select :id :mode.building}
   [:button.btn.btn-default
    {:key {:line true}} "Add a line"]
   [:button.btn.btn-default
    {:key {:point true}} "Add a point"]
   [:button.btn.btn-default
    {:key {:start true}} "Add the start"]
   [:button.btn.btn-default
    {:key {:finish true}} "Add the finish"]])

(defn undo-button [ratom]
  [:button.btn.btn-default
   {:on-click
    #(st!/undo! ratom)}
   "Undo"])

(defn redo-button [ratom]
  [:button.btn.btn-default
   {:on-click
    #(st!/redo! ratom)}
   "Redo"])

(defn building-btn [ratom]
  [:button.btn.btn-default
   {:key {:building {}}} "Building"])

(defn build-mode-form [ratom]
  [:div {:field :container
        :visible? #(contains? (:mode %) :building)}
   build-sub-modes
   (undo-button ratom)
   (redo-button ratom)
   build-opts
   [:div.sub-options
    line-params
    point-params
    start-params
    finish-params]])

