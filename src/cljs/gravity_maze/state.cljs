(ns gravity-maze.state)

(def default-line {:type :line
                   :mass 800
                   :range 50
                   :pos [nil nil]
                   :fixed true
                   ;; validation-errors []
                   :id nil})

(def default-point {:type :point
                    :mass 50
                    :pos [nil nil]
                    :vel [0 0]
                    :fixed true
                    :id nil})

(def default-finish {:type :finish
                     :range nil
                     :pos [nil nil]
                     :fixed true
                     :id nil})

(def initial-state
  {:elements []
   :g -500
   :mode {:building {:line true}}
   :dt 0.02
   :drag 0.1
   :finished? false

    ;; defaults for building
   :tmp {:building {:line {:mass 100 :range 50}
                    :start {:mass 20}
                    :finish {:range 20}
                    :options [:show-line-range]}}})
