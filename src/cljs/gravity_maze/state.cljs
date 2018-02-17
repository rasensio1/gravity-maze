(ns gravity-maze.state)

(def initial-state {})


  {:elements [{:type :point
               :mass 23
               :pos [0 20]
               :fixed true}

              {:type :point
               :mass 10
               :pos [0 20]
               :vel {:mag 3 :dir [1 1]}
               :accel {:mag 1 :dir [1 1]}
               :fixed false ;; only calculate forces on non-fixed objs
               }

              {:type :line
               :mass 1 ;kg/px
               :pos [[0 10] [10 20]]
               :fixed true
               }]


   ;; how we can modify the overall force.
   ;; F = Cma   (C)
   :f-const 10


   ;; some arbitrary force to be applied in the negative direction of
   ;; motion
   :drag 0.1

   }
