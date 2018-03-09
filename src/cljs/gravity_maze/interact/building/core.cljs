(ns gravity-maze.interact.building.core
  (:require [gravity-maze.state :as state]
            [gravity-maze.interact.shooting :as shoot]
            [gravity-maze.interact.building.validation :as bval]
            [gravity-maze.interact.helpers :refer [find-elem
                                                   pressed?
                                                   nothing]])

  (:require-macros [gravity-maze.macros :as mac]))

(defn build-updater
  "Updates state by adding validation info to each element,
  then performs the 'action' specified by the validation
  on the element (deleting it, or adding error msgs)"

  [{:keys [elements] :as state}]
  (let [validated (map bval/add-errors elements)
        acted-on (map bval/do-validation-actions validated)]
    (assoc state :elements acted-on)))

(defn add-validations! [atm]
  (reset! atm (build-updater @atm))
  atm)

(mac/defn-elem-create build-line-mouse-press
  {:new-element
   (assoc state/default-line
          :id (count (:elements @atm))
          :mousepress? true
          :pos [[x y] [x y]]
          :mass (get-in @atm [:tmp :building :line :mass])
          :range (get-in @atm [:tmp :building :line :range]))})

(mac/defn-elem-update build-line-mouse-drag
  {:criteria pressed?
   :updater (fn [el] (assoc-in el [:pos 1] [x y]))})

(mac/defn-elem-update build-line-mouse-release
  {:criteria pressed?
   :updater (fn [el] (dissoc el :mousepress?))
   :side-effects [add-validations!
                  state/add-history!]})

(mac/defn-elem-create build-finish-mouse-press
  {:new-element
   (assoc state/default-finish
          :id (count (:elements @atm))
          :pos [x y]
          :range (get-in @atm [:tmp :building :finish :range]))
   :side-effects [state/add-history!]})

(mac/defn-elem-create build-start-mouse-press
  {:new-element (assoc state/default-point
         :id (count (:elements @atm))
         :pos [x y]
         :mass (get-in @atm [:tmp :building :start :mass]))
   :side-effects [state/add-history!]})

(def click-fns {:building
                {:line
                 {:mouse-pressed build-line-mouse-press
                  :mouse-dragged build-line-mouse-drag
                  :mouse-released build-line-mouse-release
                  }
                 :start
                 {:mouse-pressed build-start-mouse-press
                  :mouse-dragged nothing
                  :mouse-released add-validations!
                  }
                 :finish
                 {:mouse-pressed build-finish-mouse-press
                  :mouse-dragged nothing
                  :mouse-released add-validations!}}})
