(ns gravity-maze.interact)

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

(defn on-point
  "Returns point that is clicked, else nil"
  [world pos]
  true

  )

(defn launch-drag [ratom event]
  ratom)

(defn launch-mouse-press [ratom event]
  (if-let [pos (on-point @ratom event)]
    (println "on point :)")
    (println "not on point"))
  ratom
  )

(defn launch-mouse-release [ratom event]
  (println "released")
  ratom
  )
