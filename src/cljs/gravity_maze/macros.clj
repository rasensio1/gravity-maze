(ns gravity-maze.macros)

(defmacro defn-elem-update [name {:keys [criteria updater side-effects]
                                  :or {side-effects 'identity}}]
  `(defn ~name [~'atm {:keys [~'x ~'y]}]
     (when-let [~'point (~'find-elem ~criteria (deref ~'atm))]
       ;; this relies on the order of :elements
       (swap! ~'atm update-in [:elements (:id ~'point)] ~updater))
     (~side-effects ~'atm)
     ~'atm))

(defmacro defn-elem-create [name new-el]
  `(defn ~name [~'atm {:keys [~'x ~'y]}]
       (swap! ~'atm update :elements (fn [~'elems] (conj ~'elems ~new-el)))
     ~'atm))

