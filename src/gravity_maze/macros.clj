(ns gravity-maze.macros)

(defmacro defn-elem-set-update [name criteria]
  `(defn ~name [~'atm {:keys [~'x ~'y]}]
     (when-let [~'elem (~'find-elem ~criteria (deref ~'atm))]
       (swap! ~'atm assoc-in [:elements (:id ~'elem)] {:type :editing
                                                       :id (:id ~'elem)})
       (swap! ~'atm assoc-in [:tmp :editing-elem] ~'elem))
     ~'atm))

(defmacro defn-elem-update [name updater]
  `(defn ~name [~'atm {:keys [~'x ~'y]}]
       (swap! ~'atm update-in [:tmp :editing-elem] ~updater)
     ~'atm))

(defmacro defn-elem-create [name new-el]
  `(defn ~name [~'atm {:keys [~'x ~'y]}]
       (swap! ~'atm assoc-in [:tmp :editing-elem] ~new-el)
     ~'atm))

