(ns gravity-maze.views.shooting
  (:require [reagent.core :as r]
            [gravity-maze.swappers.state :as st!]))

(defn shooting-btn-click [ratom]
  (do (st!/add-history! ratom)
      (swap! ratom assoc :mode {:shooting {}})))

(defn shooting-btn [ratom]
  [:button.btn.btn-default
   {:key {:shooting {}} :on-click #(shooting-btn-click ratom)}
   "Shooting"])

(defn restart-button [ratom]
  [:button.btn.btn-default
   {:on-click
    #(st!/restart! ratom)}
   "Restart"])

(defn shoot-mode-form [ratom]
  [:div {:field :container
         :visible? #(contains? (:mode %) :shooting)}
   (restart-button ratom)])
