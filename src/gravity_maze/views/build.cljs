(ns gravity-maze.views.build
  (:require [reagent.core :as r]
            [gravity-maze.swappers.state :as st!]))

(defn building-mode? [state]
  (get-in state [:mode :building]))

(defn building-line? [state]
  (get-in state [:mode :building :line]))

(defn build-param [idx {:keys [label id]}]
  ;; requires idx because react will complain
  [:div.param-group {:key idx}
   [:div [:p label]]
   [:div [:input.form-control
          {:field :numeric :id id}]]])

(defn elem-params [type params]
  [:div {:field :container
         :visible? #(get-in % [:mode :building :add type])}
   [:h3 (str (name type) "opts")]
   (map-indexed build-param params)])

(def line-params
  (elem-params :line [{:label "Mass" :id :tmp.building.line.mass}
                      {:label "Range" :id :tmp.building.line.range}]))

(def point-params
  (elem-params :point [{:label "Mass" :id :tmp.building.point.mass}
                       {:label "Range" :id :tmp.building.point.range}]))

(def start-params
  (elem-params :start [{:label "Mass" :id :tmp.building.start.mass}]))

(def finish-params
  (elem-params :finish [{:label "Range" :id :tmp.building.finish.range}]))

(def build-opts
  [:div.build-opts
   [:h3 "Options"]
   [:div.btn-group {:field :multi-select :id :tmp.building.options}
    [:button.btn.btn-default {:key :show-line-range} "Show line ranges"]
    [:button.btn.btn-default {:key :show-point-range} "Show point ranges"]]])

(def build-elem-params
  [:div.build-elem-params
   line-params
   point-params
   start-params
   finish-params])

(def build-add-modes
  [:div.build-add-modes
   [:div.btn-group {:field :single-select
                   :id :mode.building.add
                   :visible? #(get-in % [:mode :building :add])}
   [:button.btn.btn-default
    {:key {:line true}} "Add a line"]
   [:button.btn.btn-default
    {:key {:point true}} "Add a point"]
   [:button.btn.btn-default
    {:key {:start true}} "Add the start"]
   [:button.btn.btn-default
    {:key {:finish true}} "Add the finish"]]
   build-elem-params])

(defn edit-btn-click [ratom]
  (do (st!/exit-mode! ratom)
      (st!/enter-mode! ratom [:building :edit])))

(defn build-modes [ratom]
  [:div.build-modes
   [:div.btn-group {:field :single-select :id :mode.building}
   [:button.btn.btn-default
    {:key {:add true}} "Add elements"]
   [:button.btn.btn-default
    {:key {:edit true} :on-click #(edit-btn-click ratom)} "Edit elements"]]
   build-add-modes])

(defn undo-button [ratom]
  [:button.btn.btn-default
   {:on-click #(st!/undo! ratom)} "Undo"])

(defn redo-button [ratom]
  [:button.btn.btn-default
   {:on-click #(st!/redo! ratom)} "Redo"])

(defn building-btn-click [ratom]
  (do (st!/exit-mode! ratom)
      (st!/enter-mode! ratom [:building])))

(defn building-btn [ratom]
  [:button.btn.btn-default
   {:key {:building {}} :on-click #(building-btn-click ratom)}
   "Building"])

(defn build-mode-form [ratom]
  [:div {:field :container
        :visible? #(contains? (:mode %) :building)}
   (undo-button ratom)
   (redo-button ratom)
   (build-modes ratom)
   build-opts])

