(ns gravity-maze.state)

(def initial-state {:elements [{:type :point
                                 :mass 30
                                 :id 0
                                 :pos [200 200]
                                 :vel [0 0]
                                 :accel [0 0]
                                 :fixed true}]
                     :g -500
                     :dt 0.02
                     :drag 0.1})

