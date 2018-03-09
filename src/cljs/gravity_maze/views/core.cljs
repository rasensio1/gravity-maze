(ns gravity-maze.views.core
  (:require [reagent.core :as r]
            [reagent-forms.core :refer [bind-fields]]
            [gravity-maze.views.build :as build]
            ))

(def mode-btns
  [:div
   [:div.btn-group {:field :single-select :id :mode}
    [:button.btn.btn-default
     {:key {:shooting {}}} "Shooting"]
    [:button.btn.btn-default
     {:key {:building {}}} "Building"]]])

(def mode-form
  [:div.mode-form
   mode-btns
   build/build-mode-form])

(def title
  [:div [:h2 "GRAVITY MAZE"]])

(defn dash [ratom]
  (fn []
    [:div
      [:h3 "Mode"]
     (map (fn [el] [:p {:key (:id el)} (str el) ]) (:elements @ratom) )
      [:p (str (dissoc @ratom :history :elements))]
     [bind-fields mode-form ratom]]))


