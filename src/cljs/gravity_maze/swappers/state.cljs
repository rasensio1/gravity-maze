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

