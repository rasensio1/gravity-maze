(ns gravity-maze.macros)

;; TODO add-history happens before swap
;; TODO validation happens after swap

(defmacro defn-elem-update [name {:keys [criteria updater side-effects]}]
  `(defn ~name [~'atm {:keys [~'x ~'y]}]
     (when-let [~'point (~'find-elem ~criteria (deref ~'atm))]
       ;; this relies on the order of :elements
       (swap! ~'atm update-in [:elements (:id ~'point)] ~updater))
     (doseq [~'effect ~side-effects]
       (~'effect ~'atm))
     ~'atm))

(defmacro defn-elem-create [name {:keys [new-element side-effects]}]
  `(defn ~name [~'atm {:keys [~'x ~'y]}]
       (swap! ~'atm update :elements (fn [~'elems] (conj ~'elems ~new-element)))
     (doseq [~'effect ~side-effects]
       (~'effect ~'atm))
     ~'atm))

