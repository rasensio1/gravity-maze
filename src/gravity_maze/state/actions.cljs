(ns gravity-maze.state.actions)

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
