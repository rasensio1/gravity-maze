(ns gravity-maze.swappers.state
  (:require [gravity-maze.state :as st]))

(defn add-history! [atm]
  (reset! atm (st/add-history @atm)))

(defn undo! [ratom]
  (reset! ratom (st/undo @ratom)))

(defn redo! [ratom]
  (reset! ratom (st/redo @ratom)))

(defn restart! [ratom]
  (reset! ratom (st/restart @ratom)))

(defn shooting-mode! [ratom]
  (swap! ratom assoc :mode {:shooting {}}))

(defn building-mode! [ratom]
  (swap! ratom assoc :mode {:building {}}))

(defn add-shoot-start! [ratom]
  (reset! ratom (st/add-shoot-start @ratom)))

;; TODO would be great in the future to nest all temporary vars
;; in the mode, so that they go away when mode is changed
(defn remove-shooting-tmps! [ratom]
  (swap! ratom dissoc :shoot-start))

