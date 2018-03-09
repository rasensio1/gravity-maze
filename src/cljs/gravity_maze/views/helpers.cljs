(ns gravity-maze.views.helpers
  (:require [gravity-maze.state :refer [undo redo]]))

(defn undo! [ratom]
  (reset! ratom (undo @ratom)))

(defn redo! [ratom]
  (reset! ratom (redo @ratom)))
