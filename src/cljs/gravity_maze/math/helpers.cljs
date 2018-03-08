(ns gravity-maze.math.helpers)

(defn sign-of [n]
  (if (zero? n) 0 (/ n (Math/abs n))))

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

(defn normal-vectors
  "Returns normal vectors of the given vector"
  [[x y]]
  [[(- 0 y) x] [y (- 0 x)]])

(defn offset-line
  "Offsets each point in 'line' by offset vector"
  [vfn offset line] (mapv #(vfn % offset) line))

(defn pts-dist
  "Distance between two points"
  [pt1 pt2]
  (-> (v- pt1 pt2)
      sumsqs
      Math/sqrt))

(defn unit-vec [v]
  (if (= [0 0] v) [0 0]
      (let [d2 (sumsqs v)
            norm (/ 1 (Math/sqrt d2))]
        (mult-v norm v))))

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

(defn perp-dot-prod
  "Perpendicular dot-product. Used to determine which 'side' of a line a point
  resides on. Returns either -1 0 1"
  [[[lnAX lnAY] [lnBX lnBY]] [ptX ptY]]
  (letfn [(multminus [v1 v2] (apply * (v- v1 v2)))]
    (-> (- (multminus [lnBX ptY] [lnAX lnAY])
           (multminus [lnBY ptX] [lnAY lnAX]))
        sign-of)))

(defn unit-normal-vec
  "Finds the normal vector of the line that points away from the point. Uses
  perpendicular dot product to tell if point is 'above' or 'below' the line.
  Second, uses the sign to chose the appropriate normal vector.
  Third, makes the normal vector a unit vector "
  [[lnA lnB :as line ] point]

  (let [[lnx lny] (v- lnA lnB) ;; represent line as vector
        sign (perp-dot-prod line point)
        vec (if (= 0 sign) [0 0] ;; sign is '0' if point is on the line.
                ((normal-vectors [lnx lny]) (neg? sign)))]
    (unit-vec vec)))
