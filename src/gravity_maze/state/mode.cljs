(ns gravity-maze.state.mode
  (:require [gravity-maze.helpers :refer [get-mode]]
            [gravity-maze.interact.building.helpers :as bhlp]
            [gravity-maze.state.actions :as act]))

(defmulti enter-mode (fn [state mode] mode))
(defmethod enter-mode [:shooting] [state mode]
  (-> state
      (act/set-mode mode)
      act/add-shoot-start))

(defmethod enter-mode :default [state mode]
  (-> state
      (act/set-mode mode)))

(defmulti exit-mode get-mode)
(defmethod exit-mode [:shooting] [state]
  (-> state
      (dissoc :shoot-start)))

(defmethod exit-mode [:building] [state]
  (-> state
      bhlp/save-and-validate-tmp-elem))

(defmethod exit-mode :default [state]
  state)

