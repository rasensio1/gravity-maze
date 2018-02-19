# gravity-maze

Ever wondered what throwing a ball would be like if walls had a repulsive
gravity forces?
EXTREME GAMES OF CATCH!


### TODO
 - design world-store
   - ball
     - mass, position, velocity (mag, dir), accel (mag, dir)
     - :fixed false
   - walls
     - position (line, width?) (model as points? -> research)
     - :fixed true
     - mass (kg/m?)
   - end point
     - pos, radius (no mass, etc.)

 - calculate multi-mass interactions
 - collision detection
 
 
 - Simplify fixed masses by averaging?
