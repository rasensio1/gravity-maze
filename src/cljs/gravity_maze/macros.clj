(ns gravity-maze.macros)

(defmacro defn-elem-update [name criteria updater]
  `(defn ~name [~'atm {:keys [~'x ~'y]}]
     (when-let [~'point (~'find-elem ~criteria (deref ~'atm))]
       (swap! ~'atm update-in [:elements (:id ~'point)] ~updater))
     ~'atm))

