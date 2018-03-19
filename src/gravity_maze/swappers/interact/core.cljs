(ns gravity-maze.swappers.interact.core
  (:require [gravity-maze.interact.building.helpers :as bhlp]
            [gravity-maze.state.actions :as act]))

(defn set-tmp-click-pos! [ratom pos]
  (swap! ratom assoc-in [:tmp :click-pos] pos))

(defn add-placeholder-elem! [ratom id]
  (swap! ratom assoc-in [:elements id] {:type :editing :id id}))

(defn set-tmp-elem! [ratom elem]
  (swap! ratom assoc-in [:tmp :editing-elem] elem))

(defn remove-tmp-elem! [ratom]
  (reset! ratom (act/remove-tmp-elem @ratom)))

(defn add-elem! [ratom elem]
  (reset! ratom (act/add-elem @ratom elem)))

(defn save-and-validate-tmp-elem! [ratom]
  (reset! ratom (bhlp/save-and-validate-tmp-elem @ratom))
  ratom)

