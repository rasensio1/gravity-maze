(ns gravity-maze.interact.building.core
  (:require [gravity-maze.state :as state]
            [gravity-maze.interact.shooting :as shoot]
            [gravity-maze.interact.building.validation :as bval]
            [gravity-maze.interact.helpers :refer [find-elem
                                                   pressed?
                                                   nothing]])

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
  (assoc state/default-point
         :id (count (:elements @atm))
         :pos [x y]
         :mass (get-in @atm [:tmp :building :start :mass])))

(defn save-and-validate-tmp-elem
  "Validates and saves a element in temp storage. If not saveable,
  shows error message and does not save."
  [atm _]
  (let [validated-elem (bval/add-errors (get-in @atm [:tmp :editing-elem]))]
    (swap! atm assoc-in [:tmp :editing-elem] nil) ;; clear tmp

    (if-let [error (bval/some-not-saveable (:validation-errors validated-elem))]
      (js/alert (str "Element not added. " (:message error)))
      ;; else, add error messages, save and clear the tmp
      (let [elem (bval/add-error-msgs validated-elem)]
        (do (state/add-history! atm)
            (swap! atm assoc-in [:elements (:id elem)] elem)))))
  atm)

(def click-fns {:building
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
                 :finish
                 {:mouse-pressed build-finish-mouse-press
                  :mouse-dragged nothing
                  :mouse-released save-and-validate-tmp-elem}}})
