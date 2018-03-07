(ns gravity-maze.interact.core
  (:require [gravity-maze.helpers :refer [get-kws]]
            [gravity-maze.state :as state]
            [gravity-maze.interact.shooting :as shoot]
            [gravity-maze.interact.helpers :refer [find-elem
                                                   pressed?]])
  (:require-macros [gravity-maze.macros :as mac]))

(mac/defn-elem-create build-line-mouse-press
  (assoc state/default-line
         :id (count (:elements @atm))
         :mousepress? true
         :pos [[x y] [x y]]
         :mass (get-in @atm [:tmp :building :line :mass])
         :range (get-in @atm [:tmp :building :line :range])))

(mac/defn-elem-update build-line-mouse-drag
  pressed?
  (fn [el] (assoc-in el [:pos 1] [x y])))

(mac/defn-elem-update build-line-mouse-release
  pressed?
  (fn [el] (dissoc el :mousepress?)))

(mac/defn-elem-create build-finish-mouse-press
  (assoc state/default-finish
         :id (count (:elements @atm))
         :pos [x y]
         :range (get-in @atm [:tmp :building :finish :range])))

(mac/defn-elem-create build-start-mouse-press
  (assoc state/default-point
         :id (count (:elements @atm))
         :pos [x y]
         :mass (get-in @atm [:tmp :building :start :mass])))

(defn nothing [atm _] atm)


(def click-fns (merge shoot/click-fns
                      {:building {:line {:mouse-pressed build-line-mouse-press
                                  :mouse-dragged build-line-mouse-drag
                                  :mouse-released build-line-mouse-release
                                  }
                           :start {:mouse-pressed build-start-mouse-press
                                   :mouse-dragged nothing
                                   :mouse-released nothing
                                   }
                           :finish {:mouse-pressed build-finish-mouse-press
                                    :mouse-dragged nothing
                                    :mouse-released nothing}}
                }))

(defn handle-mouse [event-name ratom event]
  (let [path (conj (get-kws (:mode @ratom)) event-name)]
    ((get-in click-fns path) ratom event)))
