(ns gravity-maze.state)

(def initial-state {:elements [{:type :point
                                 :mass 830
                                 :pos [0 20]
                                 :vel [80 80]
                                 :accel [0 0]
                                 :fixed false}

                               {:type :point
                                :mass 50
                                :pos [190 190]
                                :vel [0 0]
                                :accel [0 0]
                                :fixed true}

                               {:type :point
                                :mass 1
                                :pos [150 180]
                                :vel [0 0]
                                :accel [0 0]
                                :fixed false}
                                ]
                     :g -500
                     :dt 0.05
                     :drag 0.1})

