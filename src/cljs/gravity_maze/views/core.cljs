(ns gravity-maze.views.core
  (:require [reagent.core :as r]
            [reagent-forms.core :refer [bind-fields]]
            [gravity-maze.views.build :as build]
            [gravity-maze.views.shooting :as shoot]))

(defn mode-btns [ratom]
  [:div
   [:div.btn-group {:field :single-select :id :mode}
    (shoot/shooting-btn ratom)
    (build/building-btn ratom)]])

(defn mode-form [ratom]
  [:div.mode-form
   (mode-btns ratom)
   (shoot/shoot-mode-form ratom)
   (build/build-mode-form ratom)])

(def title
  [:div [:h2 "GRAVITY MAZE"]])

(defn -debugging [ratom]
  [:div  (map (fn [el] [:p {:key (:id el)} (str el) ]) (:elements @ratom) )
  [:p (str (dissoc @ratom :history :elements :fwd))]  ])

(defn dash [ratom]
  (fn []
    [:div
      [:h3 "Mode"]
     (-debugging ratom)
     [bind-fields (mode-form ratom) ratom]]))


