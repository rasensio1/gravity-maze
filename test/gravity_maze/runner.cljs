(ns gravity-maze.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [gravity-maze.core-test]
              [gravity-maze.engine-test]
              [gravity-maze.state-test]
              [gravity-maze.interact.building.core-test]
              [gravity-maze.interact.building.validation-test]
              [gravity-maze.interact.shooting-test]
              [gravity-maze.interact.helpers-test]
              [gravity-maze.interact.core-test]
              [gravity-maze.math.helpers-test]
              [gravity-maze.math.draw-test]
              [gravity-maze.helpers-test]
              ))

(doo-tests 'gravity-maze.core-test
           'gravity-maze.engine-test
           'gravity-maze.state-test
           'gravity-maze.interact.building.core-test
           'gravity-maze.interact.building.validation-test
           'gravity-maze.interact.shooting-test
           'gravity-maze.interact.helpers-test
           'gravity-maze.interact.core-test
           'gravity-maze.math.helpers-test
           'gravity-maze.math.draw-test
           'gravity-maze.helpers-test
           )
