(ns gravity-maze.swappers.interact.core)

(defn remove-tmp-elem! [ratom]
  (swap! ratom assoc-in [:tmp :editing-elem] nil))

(defn add-elem! [ratom {:keys [id] :as elem}]
  (swap! ratom assoc-in [:elements id] elem))

