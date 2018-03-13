(ns gravity-maze.swappers.core
  (:require [gravity-maze.engine :as eng]))

(defn shooting-updater [ratom]
  (when-not (:finished? @ratom)
    (reset! ratom (eng/update-world @ratom))) ratom)

