(ns gravity-maze.macros)

(defmacro defn-elem-update [name updater]
  `(defn ~name [~'atm {:keys [~'x ~'y]}]
       (swap! ~'atm update-in [:tmp :editing-elem] ~updater)
     ~'atm))

(defmacro defn-elem-create [name new-el]
  `(defn ~name [~'atm {:keys [~'x ~'y]}]
       (swap! ~'atm assoc-in [:tmp :editing-elem] ~new-el)
     ~'atm))

