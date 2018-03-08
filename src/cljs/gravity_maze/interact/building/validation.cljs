(ns gravity-maze.interact.building.validation
  (:require [gravity-maze.math.helpers :as mth]))

(defn mass-not-zero [{:keys [mass]}]
  (when (zero? mass)
    {:action :add-error-message
     :message "Mass cannot be zero"}))

(defn length-not-zero [{:keys [pos]}]
  (when (zero? (apply mth/pts-dist pos))
    {:action :delete
     :message "Length cannot be zero"}))

(def validators-for {:point [mass-not-zero]
                     :line [mass-not-zero
                            length-not-zero]})

(defn error-map
  "Returns a map of validation-errors to be merged with an element.
  If no errors, returns nil."
  [el]
  (when-let [validations (->> (map #(% el) (validators-for (:type el)))
                              (remove nil?)
                              not-empty)]
    {:validation-errors validations}))

(defn add-errors
  "Adds all validation-error information to each element.
  Returns a new vector of elements."
  [elems]
  (->> (mapv #(merge % (error-map %)) elems)
       (sort-by :id)))

