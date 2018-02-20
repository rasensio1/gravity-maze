(ns gravity-maze.state)

(def initial-state {:elements [{:type :point
                                 :mass 23
                                 :pos [100 120]
                                 :vel [5 5]
                                 :accel [0 0]
                                 :fixed false}

                               {:type :point
                                :mass 50
                                :pos [180 180]
                                :vel [0 0]
                                :accel [0 0]
                                :fixed true}

                               {:type :point
                                :mass 50
                                :pos [200 100]
                                :vel [0 0]
                                :accel [0 0]
                                :fixed false}
                                ]
                     :g -1
                     :dt 0.08
                     :drag 0.1})

