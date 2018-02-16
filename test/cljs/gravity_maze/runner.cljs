(ns gravity-maze.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [gravity-maze.core-test]))

(doo-tests 'gravity-maze.core-test)
