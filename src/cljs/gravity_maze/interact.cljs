(ns gravity-maze.interact
  (:require [gravity-maze.math.helpers :as mth]))

;; future stuff...
;; for handling clicks in different modes...

;; {:launch {:handle-click "somefn"
;;           :mouse-dragged "some"
;;           ...}

              ;nested modes [:build :add-line]
;;  :build {:add-line {:handle-click "somethingelse"
;;                     ...}
;;          :add-end {:handle-click "lkjsf"}
;;          }
;;  }

(def click-range 10)

(defn clicked? [range click-pos {:keys [pos]}]
  (>= range (mth/pts-dist click-pos pos)))

(defn find-point
  "Returns point that is clicked, else nil"
  [world {:keys [x y]}]
  (->> (:elements world)
      (filter #(= :point (:type %)))
      (filter (partial clicked? click-range [x y]))
      first))

(defn launch-drag [ratom event]
  ratom)

(defn launch-mouse-press [ratom event]
  (if-let [pos (find-point @ratom event)]
    (println "on point :)")
    (println "NOT on point")
    )
  ratom
  )

(defn launch-mouse-release [ratom event]
  (println "released")
  ratom
  )
