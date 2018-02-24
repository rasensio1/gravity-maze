(ns gravity-maze.views.core
  (:require [reagent.core :as r]
            [reagent-forms.core :refer [bind-fields]]
            ))

(def mode-btns
  [:div
   [:div.btn-group {:field :single-select :id :mode}
    [:button.btn.btn-default
     {:key {:shooting true}} "Shooting"]
    [:button.btn.btn-default
     {:key {:building true} } "Building"]]])

;; later put this in the "building" dashboard page, for selecting sub-modes

(def build-mode-opts
   [:div
   [:div.btn-group {:field :single-select :id :mode.building}
    [:button.btn.btn-default
     {:key {:line true}} "Add a line"]
    [:button.btn.btn-default
     {:key {:point true}} "Add a point"]]])

(defn page [ratom]
  (fn []
    [:div
      [:div [:h2 "GRAVITY MAZE"] ]
      [:h3 "Mode"]
      [:p (str @ratom)]
     [bind-fields mode-btns ratom]
     [bind-fields build-mode-opts ratom]
     ]))


