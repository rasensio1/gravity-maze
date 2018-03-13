(ns gravity-maze.test-helpers
  (:require [gravity-maze.helpers :refer [tmp-elem]]))

(defn roundme [decs n]
  (js/parseFloat (.toFixed n decs)))

(defn temp-elem [state]
  (tmp-elem state))
