(ns gravity-maze.interact.building.validation
  (:require [gravity-maze.math.helpers :as mth]))

(defn some-not-saveable [errors]
  (some #(when-not (:saveable? %) %) errors))

(defn range-not-zero [{:keys [range]}]
  (when (zero? range)
    {:saveable? false
     :message "Range cannot be zero"}))

(defn mass-not-zero [{:keys [mass]}]
  (when (zero? mass)
    {:saveable? true
     :message "Mass cannot be zero"}))

(defn length-not-zero [{:keys [pos]}]
  (when (zero? (apply mth/pts-dist pos))
    {:saveable? false
     :message "Length cannot be zero"}))

(def validators-for {:point [mass-not-zero]
                     :line [mass-not-zero
                            length-not-zero]
                     :finish [range-not-zero]})

(defn error-map
  "Returns a map of validation-errors to be merged with an element.
  If no errors, returns nil."
  [el]
  (when-let [validations
             (->> (map #(% el) (validators-for (:type el)))
                  (remove nil?)
                  not-empty)]
    {:validation-errors validations}))

(defn add-errors
  "Adds all validation-error information to element.
  Returns a new element."
  [el] (merge el (error-map el)))

(defn add-error-msgs
  "Adds error messages from validation to an element if present."
  [{:keys [validation-errors] :as el}]
  (-> (if (not-empty validation-errors)
        (assoc el :error-msgs (map :message validation-errors)) el)
      (dissoc :validation-errors)))

