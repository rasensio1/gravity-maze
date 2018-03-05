# gravity-maze

Ever wondered what throwing a ball would be like if walls had a repulsive
gravity forces?
EXTREME GAMES OF CATCH!


### TODO

  - Fix one-point lines breaking game
     - Create validators for creating new elements... (point-lines, zero mass, etc)
       - stores temporary elemtns in :tmp-elements [el]
       - Swaps into :elements on mouse-release after validating

  - Fix bug breaking everything... make sure to get rid of mousepress?
  - option for bigger 'start' and click radius 

    - Add finish point
    - Add start point
      - diff between just any points and the start point 
        (only allow starting from start)

    - display element information
    
  - Undo/Redo build actions
  - Retry (reset start)
  
  - build-mode path simulator
  
  - Stop blocking shooting to right when against left wall
  
### Other Ideas

  - Would be fun to play on a BIG screen. More like a maze that way.

  - New element: "Tarpit" something that slows down the ball
