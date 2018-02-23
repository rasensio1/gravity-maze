(ns gravity-maze.math.helpers)

(defn v+ [& vecs]
  (apply (partial mapv +) vecs))

(defn v- [& vecs]
  (apply (partial mapv -) vecs))

(defn sumsqs [vec]
  (reduce #(+ %1 (* %2 %2)) 0 vec))

(defn pts-dist [pt1 pt2]
  (-> (v- pt1 pt2)
      sumsqs
      Math/sqrt))

