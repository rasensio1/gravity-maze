(ns gravity-maze.engine)

(defn v+ [& args]
  (apply (partial mapv +) args))

(defn v* [n vec]
  (mapv #(* n %) vec))

(defn v-div [n vec]
  (v* (/ 1 n) vec))

(defn div-v
  " 3 / [1 2] => [3/1 3/2]
  Vector value of zero returns zero"
  [n vec]
  (mapv #(if (= 0 %)
          0
          (/ n %)) vec))

(defn v- [vec1 vec2]
  (mapv - vec1 vec2))

(defn sumsqs [vec]
  (reduce #(+ %1 (* %2 %2)) 0 vec))

(defn unit-vec [v]
  (if (= [0 0] v) [0 0]
    (let [d2 (sumsqs v)
          norm (/ 1 (Math/sqrt d2))]
      (v* norm v))))

(defn update-pos [dt {:keys [pos vel accel] :as el}]
  (let [velocity-disp (v* dt vel)
        accel-disp (v* (* 0.5 dt dt) accel)
        new-pos (v+ pos velocity-disp accel-disp)]
    (assoc el :pos new-pos)))

(defn update-vel [dt new-accel {:keys [vel accel] :as el}]
  (let [avg-accel (v-div 2 (v+ accel new-accel))
        accel-dv (v* dt avg-accel)
        new-vel (v+ vel accel-dv)]
    (assoc el :vel new-vel)))

(defn calc-accel [force {:keys [mass] :as el}]
  (v-div mass force))

(defn update-accel [new-accel el]
  (assoc el :accel new-accel))

(defn force-between [g el1 el2]
  (let [force-dir (apply v- (map :pos [el2 el1]))
        d2 (sumsqs force-dir)
        unit-force (unit-vec force-dir)
        gmm (apply * (cons g (map :mass [el1 el2])))]
    (if (zero? d2) [0 0]
        (v* (/ gmm d2) unit-force))))

(defn sum-interactions [interaction el {:keys [elements g]}]
  (reduce (fn [agg el2] (v+ agg (interaction g el el2))) [0 0] elements))

(defn update-elem [el {:keys [dt] :as world}]
  (let [force (sum-interactions force-between el world)
        new-accel (calc-accel force el)]
    (->> el
        (update-pos dt)
        (update-vel dt new-accel)
        (update-accel new-accel))))

(defn update-world [world]
  (js-debugger)
  (let [elems (:elements world)
        fixed (filter :fixed elems)
        to-update (filter (complement :fixed) elems)
        updated (map update-elem to-update (repeat world))]
    (assoc world :elements (concat updated fixed))))
