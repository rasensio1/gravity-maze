# gravity-maze

Ever wondered what throwing a ball would be like if walls had a repulsive
gravity forces?
EXTREME GAMES OF CATCH!


### TODO
  - REFACTOR
     - remove :accel from all tests

  - Building
    - add line
      - display range

    - Add finish point
    - Add start point
      - diff between just any points and the start point 
        (only allow starting from start)

    - display element information
    
  - Undo/Redo build actions
  - Retry (reset start)
  
  - build-mode path simulator
  
  - Fix ball speeding up on bounces
    - energy balance? 
      - calculate initial energy, and store it. set kinetic eng (velocity) 
        equal to TE - GPE(wall)
      - calculate max velocity (+ (initial-velocity) (GPE->velocity)).
        make sure it never goes above this. 
        (would break if added something to speed up ball)
      - refer next state energy to previous state energy 

  - Fix balls hopping over lines at high speeds
    - Seems to not be an issue with higher wall mass.
    we'll wait to do this if it becomes necessary

### Other Ideas

  - Would be fun to play on a BIG screen. More like a maze that way.

  - New element: "Tarpit" something that slows down the ball
