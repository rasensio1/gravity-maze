(ns gravity-maze.swappers.interact.core)

(defn add-placeholder-elem! [ratom id]
  (swap! ratom assoc-in [:elements id] {:type :editing :id id}))

(defn set-tmp-elem! [ratom elem]
  (swap! ratom assoc-in [:tmp :editing-elem] elem))

(defn remove-tmp-elem! [ratom]
  (swap! ratom assoc-in [:tmp :editing-elem] nil))

(defn add-elem! [ratom {:keys [id] :as elem}]
  (swap! ratom assoc-in [:elements id] elem))

