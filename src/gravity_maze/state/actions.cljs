(ns gravity-maze.state.actions)

(defn add-elem [state {:keys [id] :as elem}]
  (assoc-in state [:elements id] elem))

(defn remove-tmp-elem [state]
  (assoc-in state [:tmp :editing-elem] nil))

(defn set-mode [state mode]
  (->> (assoc-in {} mode true)
       (assoc state :mode)))

(defn add-history [state]
  (-> (assoc state :history state)
      (dissoc :fwd)))

(defn undo [{:keys [history] :as state}]
  (if (not-empty history)
    (assoc (:history state) :fwd (dissoc state :history))
    state))

(defn redo [{:keys [fwd] :as state}]
  (if (not-empty fwd)
    (assoc fwd :history (dissoc state :fwd))
    state))

(defn restart [{:keys [shoot-start] :as state}]
  (assoc shoot-start :shoot-start shoot-start))

(defn add-shoot-start
  "Sets current state as :shoot-start"
  [state]
  (assoc state :shoot-start (dissoc state :shoot-start)))
