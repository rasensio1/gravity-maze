(ns gravity-maze.views.core
  (:require [reagent.core :as r]
            [reagent-forms.core :refer [bind-fields]]
            [gravity-maze.views.build :as build]
            ))

(def mode-btns
  [:div
   [:div.btn-group {:field :single-select :id :mode}
    [:button.btn.btn-default
     {:key {:shooting true}} "Shooting"]
    [:button.btn.btn-default
     {:key {:building true}} "Building"]]])

(def title
  [:div [:h2 "GRAVITY MAZE"]])

(defn dash [ratom]
  (fn []
    [:div
      [:h3 "Mode"]
      [:p (str @ratom)]
     [bind-fields mode-btns ratom]
     (when (build/building-mode? @ratom)
       [bind-fields build/build-mode-opts ratom])]))


