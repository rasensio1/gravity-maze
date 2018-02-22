(ns gravity-maze.state)

(def initial-state {:elements [{:type :point
                                 :mass 30
                                 :pos [20 40]
                                 :vel [40 110]
                                 :accel [0 0]
                                 :fixed false}

                               {:type :point
                                :mass 500
                                :pos [190 190]
                                :vel [0 0]
                                :accel [0 0]
                                :fixed true}

                               {:type :point
                                :mass 30
                                :pos [150 180]
                                :vel [0 0]
                                :accel [0 0]
                                :fixed false}

                               {:type :line
                                :mass 300
                                :range 100
                                :pos [[150 0] [250 100]]
                                :fixed true}

                               {:type :line
                                :mass 300
                                :range 100
                                :pos [[0 280] [280 280]]
                                :fixed true}

                               {:type :line
                                :mass 300
                                :range 100
                                :pos [[0 0] [0 280]]
                                :fixed true}
                                ]
                     :g -500
                     :dt 0.02
                     :drag 0.1})

