(ns gravity-maze.macros)

(defmacro defn-point-event [name criteria updater]
  `(defn ~name [~'atm {:keys [~'x ~'y]}]
     (when-let [~'point (~'find-elem ~criteria (deref ~'atm))]
       (as-> (:elements (deref ~'atm)) ~'elems
         ;; here we are assuming we never 'delete' and element,
         ;; preserving the index as associated with the id
         (update (vec ~'elems) (:id ~'point) ~updater)
         (swap! ~'atm assoc :elements ~'elems)))
     ~'atm))

