(ns gravity-maze.state)

(def default-line {:type :line
                   :mass 800
                   :range 50
                   :pos [nil nil]
                   :fixed true
                   :id nil
                   })

(def initial-state {:elements [{:type :point
                                 :mass 30
                                 :id 0
                                 :pos [200 200]
                                 :vel [0 0]
                                 :accel [0 0]
                                :fixed true}

                               {:type :line
                                :mass 800
                                :range 50
                                :pos [[0 500] [500 500]]
                                :fixed true
                                }
                               ]
                    :g -500
                    :mode {:shooting true}
                    :dt 0.02
                    :drag 0.1})

