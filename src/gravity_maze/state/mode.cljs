(ns gravity-maze.state.mode
  (:require [gravity-maze.helpers :refer [get-mode]]
            [gravity-maze.state.actions :as act]))

(defmulti enter-mode (fn [state mode] mode))

(defmethod enter-mode [:shooting] [state mode]
  (-> state
      (act/set-mode mode)
      act/add-shoot-start))

