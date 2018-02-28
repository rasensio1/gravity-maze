(ns gravity-maze.interact
  (:require [gravity-maze.math.helpers :as mth]
            [gravity-maze.helpers :refer [get-kws]]
            [gravity-maze.state :as state])
  (:require-macros [gravity-maze.macros :as mac]))

(def click-range 10)

(defn clicked? [range click-pos {:keys [pos] :as elem}]
  (if (>= range (mth/pts-dist click-pos pos))
    elem false))

(defn pressed? [elem]
  (if (:mousepress? elem) elem false))

(defn drag-vec? [elem]
  (if (:drag-vec elem) elem false))

(defn find-elem
  "Returns element based on filter-fn, else nil."
  [filter-fn world]
  (->> (:elements world)
       (some filter-fn)))

(mac/defn-elem-update launch-mouse-press
  (partial clicked? click-range [x y])
  (fn [el] (assoc el :mousepress? true)))

(mac/defn-elem-update launch-drag
  pressed?
  (fn [el] (assoc el :drag-vec (mth/v- (:pos point) [x y]))))

(mac/defn-elem-update launch-mouse-release
  drag-vec?
  (fn [el] (-> (assoc el :vel (:drag-vec el))
               (assoc :fixed false)
               (dissoc :mousepress? :drag-vec))))

(mac/defn-elem-create build-line-mouse-press
  (assoc state/default-line
         :id (count (:elements @atm))
         :mousepress? true
         :pos [[x y] [x y]]
         :mass (get-in @atm [:tmp :build :line :mass])
         :range (get-in @atm [:tmp :build :line :range])))

(mac/defn-elem-update build-line-mouse-drag
  pressed?
  (fn [el] (assoc-in el [:pos 1] [x y])))

(mac/defn-elem-update build-line-mouse-release
  pressed?
  (fn [el] (dissoc el :mousepress?)))

(mac/defn-elem-create build-start-mouse-press
  (assoc state/default-point
         :id (count (:elements @atm))
         :mousepress? true
         :pos [x y]
         :mass (get-in @atm [:tmp :build :start :mass])))

(mac/defn-elem-update build-start-mouse-drag
  pressed?
  (fn [el] (assoc el :pos [x y])))

(mac/defn-elem-update build-start-mouse-release
  pressed?
  (fn [el] (dissoc el :mousepress?)))


(def click-fns {:building {:line {:mouse-pressed build-line-mouse-press
                                  :mouse-dragged build-line-mouse-drag
                                  :mouse-released build-line-mouse-release
                                  }
                           :start {:mouse-pressed build-start-mouse-press
                                   :mouse-dragged build-start-mouse-drag
                                   :mouse-released build-start-mouse-release
                                   }}

                :shooting {:mouse-pressed launch-mouse-press
                           :mouse-dragged launch-drag
                           :mouse-released launch-mouse-release
                           }})

(defn handle-mouse [event-name ratom event]
  (let [path (conj (get-kws (:mode @ratom)) event-name)]
    ((get-in click-fns path) ratom event)))
