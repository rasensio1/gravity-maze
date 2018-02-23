(ns gravity-maze.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [gravity-maze.core-test]
              [gravity-maze.engine-test]
              [gravity-maze.interact-test]
              [gravity-maze.math.helpers-test]))

(doo-tests 'gravity-maze.core-test
           'gravity-maze.engine-test
           'gravity-maze.interact-test
           'gravity-maze.interact-test
           'gravity-maze.math.helpers-test)
