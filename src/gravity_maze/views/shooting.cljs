(ns gravity-maze.views.shooting
  (:require [reagent.core :as r]
            [gravity-maze.swappers.state :as st!]))

(defn shooting-btn-click [ratom]
  ;; (exit-mode ratom)  any cleanup that needs to be done before switching mode
      (st!/enter-mode! ratom [:shooting]))

(defn restart-btn-click [ratom]
  (st!/restart! ratom))

(defn shooting-btn [ratom]
  [:button.btn.btn-default
   {:key {:shooting {}} :on-click #(shooting-btn-click ratom)}
   "Shooting"])

(defn restart-button [ratom]
  [:button.btn.btn-default
   {:on-click #(restart-btn-click ratom)}
   "Restart"])

(defn shoot-mode-form [ratom]
  [:div {:field :container
         :visible? #(contains? (:mode %) :shooting)}
   (restart-button ratom)])
