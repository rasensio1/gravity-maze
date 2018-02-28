(ns gravity-maze.state)

(def default-line {:type :line
                   :mass 800
                   :range 50
                   :pos [nil nil]
                   :fixed true
                   :id nil})

(def default-point {:type :point
                    :mass 50
                    :pos [nil nil]
                    :vel [0 0]
                    :fixed true
                    :id nil})

(def initial-state {:elements []
                    :g -500
                    :mode {:shooting true}
                    ;; :mode {:name :building
                    ;;        :options []
                    ;;        :sub-mode {:name :line
                    ;;                   :sub-mode {}
                    ;;                   :options []
                    ;;                   :range 50
                    ;;                   :mass 100}}
                    :dt 0.02
                    :drag 0.1
                    ;; defaults for building a line
                    :tmp {:build {:line {:mass 100 :range 50}
                                  :start {:mass 20}}}})

