(ns gravity-maze.helpers)

(defn get-kws
  "Returns all keys that are keywords from map."
  [tree]
  (->> (tree-seq map? #(interleave (keys %) (vals %)) tree)
       (filter keyword?)
       vec))

(defn get-mode [state]
  (-> state :mode get-kws))

(defn options
  "Returns all values in k-v pairs {:options [values.]}
  withing a `tree`, along `paths`"
  [tree paths]
  (let [paths (reductions conj [] paths)]
    (mapcat #(:options (get-in tree %)) paths)))

(defn tmp-elem [state]
  (get-in state [:tmp :editing-elem]))
