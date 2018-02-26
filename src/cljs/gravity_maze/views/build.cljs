(ns gravity-maze.views.build
  (:require [reagent.core :as r]
            [reagent-forms.core :refer [bind-fields]]))

(defn building-mode? [state]
  (get-in state [:mode :building]))

(def build-mode-opts
  [:div
   [:div.btn-group {:field :single-select :id :mode.building}
    [:button.btn.btn-default
     {:key {:line true}} "Add a line"]
    [:button.btn.btn-default
     {:key {:point true}} "Add a point"]]])
