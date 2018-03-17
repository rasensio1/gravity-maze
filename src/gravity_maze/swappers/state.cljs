(ns gravity-maze.swappers.state
  (:require [gravity-maze.state.actions :as act]
            [gravity-maze.state.mode :as mode]))

(defn enter-mode! [ratom mode]
  (reset! ratom (mode/enter-mode @ratom mode)))

(defn add-history! [atm]
  (reset! atm (act/add-history @atm)))

(defn undo! [ratom]
  (reset! ratom (act/undo @ratom)))

(defn redo! [ratom]
  (reset! ratom (act/redo @ratom)))

(defn restart! [ratom]
  (reset! ratom (act/restart @ratom)))

(defn building-mode! [ratom]
  (swap! ratom assoc :mode {:building {}}))

;; TODO would be great in the future to nest all temporary vars
;; in the mode, so that they go away when mode is changed
(defn remove-shooting-tmps! [ratom]
  (swap! ratom dissoc :shoot-start))

