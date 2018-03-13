(ns gravity-maze.state)

(def default-line {:type :line
                   :mass 800
                   :range 50
                   :pos [nil nil]
                   :fixed true
                   ;; validation-errors []
                   :id nil})

(def default-point {:type :point
                    :mass 50
                    :range 100
                    :fixed true
                    :id nil})

(def default-start {:type :start
                    :mass 50
                    :pos [nil nil]
                    :vel [0 0]
                    :fixed true
                    :id nil})

(def default-finish {:type :finish
                     :range nil
                     :pos [nil nil]
                     :fixed true
                     :id nil})

(def initial-state
  {:elements []
   :g -500
   :mode {:building {:line true}}
   :dt 0.02
   :drag 0.1
   :finished? false
   :history nil
   :fwd nil
    ;; defaults for building
   :tmp {:editing-elem nil
         :building {:line {:mass 100 :range 50}
                    :start {:mass 20}
                    :finish {:range 20}
                    :options [:show-line-range]
                    }}})

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

(defn restart
  "Sets current state as first state in :history. Preserves
  :history for multiple restarts."
  [{:keys [history]}]
  (assoc history :history history))
