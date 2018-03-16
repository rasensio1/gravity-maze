(ns gravity-maze.interact.building.core
  (:require [gravity-maze.state :as state]
            [gravity-maze.swappers.state :as st!]
            [gravity-maze.interact.building.validation :as bval]
            [gravity-maze.interact.helpers :refer [nothing]]
            [gravity-maze.swappers.interact.core :as intr!])

  (:require-macros [gravity-maze.macros :as mac]))

(mac/defn-elem-create build-line-mouse-press
  (assoc state/default-line
         :id (count (:elements @atm))
         :pos [[x y] [x y]]
         :mass (get-in @atm [:tmp :building :line :mass])
         :range (get-in @atm [:tmp :building :line :range])))

(mac/defn-elem-update build-line-mouse-drag
   (fn [el] (assoc-in el [:pos 1] [x y])))

(mac/defn-elem-create build-finish-mouse-press
  (assoc state/default-finish
         :id (count (:elements @atm))
         :pos [x y]
         :range (get-in @atm [:tmp :building :finish :range])))

(mac/defn-elem-create build-start-mouse-press
  (assoc state/default-start
         :id (count (:elements @atm))
         :pos [x y]
         :mass (get-in @atm [:tmp :building :start :mass])))

(mac/defn-elem-create build-point-mouse-press
  (assoc state/default-point
         :id (count (:elements @atm))
         :pos [x y]
         :mass (get-in @atm [:tmp :building :point :mass])
         :range (get-in @atm [:tmp :building :point :range])))

(defn save-and-validate-tmp-elem
  "Validates and saves a element in temp storage. If not saveable,
  shows error message and does not save."
  [atm _]
  (when-let [validated-elem (bval/add-errors (get-in @atm [:tmp :editing-elem]))]
    (intr!/remove-tmp-elem! atm)

    (if-let [error (bval/some-not-saveable (:validation-errors validated-elem))]
      (js/alert (str "Element not added. " (:message error)))
      ;; else, add error messages, save and clear the tmp
      (let [elem (bval/add-error-msgs validated-elem)]
        (do (st!/add-history! atm) (intr!/add-elem! atm elem)))))
  atm)

(def click-fns {:building
                {:add
                 {:line
                  {:mouse-pressed build-line-mouse-press
                   :mouse-dragged build-line-mouse-drag
                   :mouse-released save-and-validate-tmp-elem
                   }
                  :start
                  {:mouse-pressed build-start-mouse-press
                   :mouse-dragged nothing
                   :mouse-released save-and-validate-tmp-elem
                   }
                  :point
                  {:mouse-pressed build-point-mouse-press
                   :mouse-dragged nothing
                   :mouse-released save-and-validate-tmp-elem
                   }
                  :finish
                  {:mouse-pressed build-finish-mouse-press
                   :mouse-dragged nothing
                   :mouse-released save-and-validate-tmp-elem}}}})
