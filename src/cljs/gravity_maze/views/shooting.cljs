(ns gravity-maze.views.shooting
  (:require [reagent.core :as r]
            [gravity-maze.state :as st]
            [gravity-maze.views.helpers :as hlp]))

(defn shooting-btn-click [ratom]
  (do (swap! ratom assoc :mode {:shooting {}})
      (st/add-history! ratom)))

(defn shooting-btn [ratom]
  [:button.btn.btn-default
   {:key {:shooting {}} :on-click #(shooting-btn-click ratom)}
   "Shooting"])

(defn restart-button [ratom]
  [:button.btn.btn-default
   {:on-click
    #(hlp/restart! ratom)}
   "Restart"])

(defn shoot-mode-form [ratom]
  [:div {:field :container
         :visible? #(contains? (:mode %) :shooting)}
   (restart-button ratom)])
