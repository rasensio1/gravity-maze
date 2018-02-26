(ns gravity-maze.interact
  (:require [gravity-maze.math.helpers :as mth]
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

(mac/defn-point-event launch-mouse-press
  (partial clicked? click-range [x y])
  (fn [el] (assoc el :mousepress? true)))

(mac/defn-point-event launch-drag
  pressed?
  (fn [el] (assoc el :drag-vec (mth/v- (:pos point) [x y]))))

(mac/defn-point-event launch-mouse-release
  drag-vec?
  (fn [el] (as-> el point
               (assoc point :vel (:drag-vec el))
               (assoc point :fixed false)
               (dissoc point :mousepress? :drag-vec))))

(defn build-line-mouse-press [ratom {:keys [x y]}]
  (let [id (count (:elements @ratom))
        new-line (-> (assoc state/default-line :id id :mousepress? true)
                     (assoc-in [:pos] [[x y] [x y]]))]
    (swap! ratom update :elements #(conj (vec %) new-line)))
  ratom)

(defn build-line-mouse-drag [ratom {:keys [x y]}]
  (let [line (find-elem pressed? @ratom)]
    (as-> (:elements @ratom) elems
      (update (vec elems) (:id line) #(assoc-in % [:pos 1] [x y]))
      (swap! ratom assoc :elements elems)))
  ratom)

(def click-fns {:building {:line {:mouse-pressed build-line-mouse-press
                                  :mouse-dragged build-line-mouse-drag
                                  :mouse-released (fn [x y] (println x y))
                                  }}

                :shooting {:mouse-pressed launch-mouse-press
                           :mouse-dragged launch-drag
                           :mouse-released launch-mouse-release
                           }})

(defn get-kws
  "Returns all keys that are keywords from map."
  [tree]
  (->> (tree-seq map? #(interleave (keys %) (vals %)) tree)
       (filter keyword?)
       vec))

(defn handle-mouse [event-name ratom event]
  (let [path (conj (get-kws (:mode @ratom)) event-name)]
    ((get-in click-fns path) ratom event)))
