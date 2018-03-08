(ns gravity-maze.interact.building.validation
  (:require [gravity-maze.math.helpers :as mth]))

(defn mass-not-zero [{:keys [mass]}]
  (when (zero? mass)
    {:action :add-error-msg
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
  "Adds all validation-error information to element.
  Returns a new element."
  [el] (merge el (error-map el)))

(defn add-error-msgs-action
  "Adds error messages from validation to an element."
  [{:keys [validation-errors] :as el}]
  (-> (assoc el :error-msgs (map :message validation-errors))
      (dissoc :validation-errors)))

(defn delete-action
  "'Removes' and element by returning an empty map, but preserving
  the ordering of the :elements vector"
  [{:keys [id]} error]
  (js/alert "Element not added. " (:message error))
  {:id id :type nil})

(defn do-validation-actions [{:keys [validation-errors] :as elem}]
  (if-let [error (some #(= :delete (:action %)) validation-errors)]
    (delete-action elem error)
    (add-error-msgs-action elem)))

