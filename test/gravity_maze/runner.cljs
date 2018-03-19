(ns gravity-maze.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [gravity-maze.core-test]
              [gravity-maze.engine-test]
              [gravity-maze.state.actions-test]
              [gravity-maze.state.mode-test]
              [gravity-maze.interact.building.add-test]
              [gravity-maze.interact.building.core-test]
              [gravity-maze.interact.building.helpers-test]
              [gravity-maze.interact.building.validation-test]
              [gravity-maze.interact.shooting-test]
              [gravity-maze.interact.helpers-test]
              [gravity-maze.math.helpers-test]
              [gravity-maze.math.draw-test]
              [gravity-maze.helpers-test]
              [gravity-maze.swappers.interact.core-test]))

(doo-tests 'gravity-maze.core-test
           'gravity-maze.engine-test
           'gravity-maze.state.actions-test
           'gravity-maze.state.mode-test
           'gravity-maze.interact.building.add-test
           'gravity-maze.interact.building.core-test
           'gravity-maze.interact.building.helpers-test
           'gravity-maze.interact.building.validation-test
           'gravity-maze.interact.shooting-test
           'gravity-maze.interact.helpers-test
           'gravity-maze.math.helpers-test
           'gravity-maze.math.draw-test
           'gravity-maze.helpers-test
           'gravity-maze.swappers.interact.core-test
           )
