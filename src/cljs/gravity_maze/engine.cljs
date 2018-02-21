(ns gravity-maze.engine)

(defn v+ [& vecs]
  (apply (partial mapv +) vecs))

(defn v- [& vecs]
  (apply (partial mapv -) vecs))

(defn mult-v [n vec]
  (mapv #(* n %) vec))

(defn div-v [n vec]
  (mult-v (/ 1 n) vec))

(defn sumsqs [vec]
  (reduce #(+ %1 (* %2 %2)) 0 vec))

(defn unit-vec [v]
  (if (= [0 0] v) [0 0]
    (let [d2 (sumsqs v)
          norm (/ 1 (Math/sqrt d2))]
      (mult-v norm v))))

(defn update-pos [dt {:keys [pos vel accel] :as el}]
  (let [velocity-disp (mult-v dt vel)
        accel-disp (mult-v (* 0.5 dt dt) accel)
        new-pos (v+ pos velocity-disp accel-disp)]
    (assoc el :pos new-pos)))

(defn update-vel [dt new-accel {:keys [vel accel] :as el}]
  (let [avg-accel (div-v 2 (v+ accel new-accel))
        accel-dv (mult-v dt avg-accel)
        new-vel (v+ vel accel-dv)]
    (assoc el :vel new-vel)))

(defn calc-accel [force {:keys [mass] :as el}]
  (div-v mass force))

(defn update-accel [new-accel el]
  (assoc el :accel new-accel))

(defn pts-dist [pt1 pt2]
  (-> (v- pt1 pt2)
      sumsqs
      Math/sqrt))

(defn det3x3
  "Constructs 3x3 matrix from 3 points and calculates determinant"
  [[[xa ya][xb yb][xc yc]]]
  (letfn [(add-prods [vec1 vec2] (apply + (map * vec1 vec2)))]
    (- (add-prods [xa xb xc] [yb yc ya])
       (add-prods [xa xc xb] [yc yb ya]))))

(defn line-dist
  "Calculates the distance between a point and line.
  Finds area by creating a triangle out of line and point."
  [[lna lnb] point]
  (let [pts [lna lnb point]
        area (/ (Math/abs (det3x3 pts)) 2)
        base (pts-dist lna lnb)]
    (* 2 (/ area base))))

(defn normal-vec
  "Finds the normal vector of the line that points away from the point.
  First, uses the sign of the determinant to find out whether the point is
  'above' or 'below' the line.
  Second, uses the sign to chose the appropriate normal vector. 
  Third, makes the normal vector a unit vector
  "
  [[[lnAx lnAy] [lnBx lnBy]] [elx ely]]
  (letfn [(multminus [v1 v2] (apply * (v- v1 v2)))]
    (let [[lnx lny] (v- [lnAx lnAy] [lnBx lnBy])
          above? (neg? (- (multminus [lnBx ely] [lnAx lnAy])
                          (multminus [lnBy elx] [lnAy lnAx])))
        ;; normal vectors for a line '[x y]' are [-y x] & [y -x]
          perpens [[(- 0 lny) lnx] [lny (- 0 lnx)]]
          vec (perpens above?)]
      (unit-vec vec))))

(defmulti force-between (fn [g e1 e2] (e2 :type)))

(defmethod force-between :line [g el1 line]
  ;; (let [force-dir (normal-vec line el)
  ;;       d2 (sumsqs force-dir)
  ;;       unit-force (unit-vec force-dir)
  ;;       gmm (apply * (cons g (map :mass [el1 line])))]
  ;;   (if (zero? d2) [0 0]
  ;;       (mult-v (/ gmm d2) unit-force)))
  [1 1]
  )

(defmethod force-between :point [g el1 point]
  (let [force-dir (apply v- (map :pos [point el1]))
        d2 (sumsqs force-dir)
        unit-force (unit-vec force-dir)
        gmm (apply * (cons g (map :mass [el1 point])))]
    (if (zero? d2) [0 0]
        (mult-v (/ gmm d2) unit-force))))

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
  (let [elems (:elements world)
        fixed (filter :fixed elems)
        to-update (filter (complement :fixed) elems)
        updated (map update-elem to-update (repeat world))]
    (assoc world :elements (concat updated fixed))))
