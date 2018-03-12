(ns gravity-maze.views.helpers
  (:require [gravity-maze.state :refer [undo redo restart]]))

(defn undo! [ratom]
  (reset! ratom (undo @ratom)))

(defn redo! [ratom]
  (reset! ratom (redo @ratom)))

(defn restart! [ratom]
  (reset! ratom (restart @ratom)))
