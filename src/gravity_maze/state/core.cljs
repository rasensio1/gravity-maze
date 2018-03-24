(ns gravity-maze.state.core)

(def elem-hierarchy
  (-> (make-hierarchy)
      (derive :finish :point)
      (derive :start :point)
      atom))

(def default-line {:type :line
                   :mass 800
                   :range 50
                   :pos [nil nil]
                   :fixed true
                   ;; validation-errors []
                   :id nil})

(def default-point {:type :point
                    :mass 50
                    :range 100
                    :fixed true
                    :id nil})

(def default-start {:type :start
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
   :mode {:building {:add {:line true}}}
   :dt 0.02
   :drag 0.1
   :finished? false
   :history nil
   :fwd nil
    ;; defaults for building
   :tmp {:editing-elem nil
        ;:click-pos 
         :building {:line {:mass 100 :range 50}
                    :start {:mass 20}
                    :point {:mass 50 :range 50}
                    :finish {:range 20}
                    :options [:highlight :show-range]
                    }}})

