(ns gravity-maze.swappers.state
  (:require [gravity-maze.state.actions :as act]
            [gravity-maze.state.mode :as mode]))

(defn enter-mode! [ratom mode]
  (reset! ratom (mode/enter-mode @ratom mode)))

(defn exit-mode! [ratom]
  (reset! ratom (mode/exit-mode @ratom)))

(defn add-history! [atm]
  (reset! atm (act/add-history @atm)))

(defn undo! [ratom]
  (reset! ratom (act/undo @ratom)))

(defn redo! [ratom]
  (reset! ratom (act/redo @ratom)))

(defn restart! [ratom]
  (reset! ratom (act/restart @ratom)))

