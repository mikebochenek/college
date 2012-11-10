/* CIS*4750 Artificial Intelligence
   Assignment #3 (6.5 & 6.13), Due: Friday October 20, 2000
   Michael Bochenek ID: 0041056 mboche01@uoguelph.ca */

****************
* Question 6.5 *
****************

"If the unicorn is mythical, then it is immortal, but if it is not mythical, 
then it is a mortal mammal.  If the unicorn is either immortal or a mammal, 
then it is horned.  The unicorn is magical if it is horned."

Let Y denote that the unicorn is mythical.
Let O denote that the unicorn is mortal.
Let A denote that the unicorn is a mammal.
Let H denote that the unicorn is horned.
Let M denote that the unicorn is magical.

"If the unicorn is mythical, then it is immortal,"
Y --> not O

"but if it is not mythical, then it is a mortal mammal."
(not Y) --> O
(not Y) --> A

"If the unicorn is either immortal or a mammal, then it is horned."
(not O or A) --> H

"The unicorn is magical if it is horned."
H --> M


 Is the unicorn mythical?
--------------------------

Given the set of statements, it cannot be proven that the unicorn is mythical. 

 Is the unicorn horned?
------------------------
(Y or (not Y))                  /* It's either mythical or not */
(Y --> not O) or ( (not Y) --> O and ( (not Y) --> A )
                                /* adding "if a then b" */
(Y --> not O) or ( (not Y) --> A )
                     /* is still true (just eliminated first part of AND */
(not O or A)      /* following the "if a then b" */
H             /* because we know that (not O or A) --> H */

Therefore we can prove that the unicorn is horned.

 Is the unicorn magical?
-------------------------
H --> M
M         /* because we have just proved that H is true */

Therefore we can prove that the unicorn is magical.

*****************
* Question 6.13 *
*****************

The performance score that one would expect from the optimal wumpus world 
completely depends on the random map generated.  If a world is generated
that surrounds the agent with pits and a wumpus, there is nothing to be done.
In any other world, the agent will typically receive a score of 80 to 95
points.

Designing an experiment consists of creating an environment where the
score is calculated as follows:
   minus 1 point for each action taken.
   minus 10000 points for dying (pit or wumpus).
   plus 100 points for returning with the gold to the starting position.
This environment is implemented in the Java Applet "Wumpus.class".

We only look at a random world, because looking at all possible 4x4 worlds 
is practically impossible because of the number of possible worlds (n).

n = 1 x 15 x 14 x 2 ^ 13
    |    |    |    |
    |    |    |    +---- each of the REMAINING 13 squares can either
    |    |    |            have a pit or not (boolean).
    |    |    +--------- the gold can be in any of the OTHER 14 squares.
    |    +-------------- the wumpus can be in any of the OTHER 15 squares.
    +------------------- the agent MUST start in position (1,1).

n = 1 720 320

Assuming that gold and a wumpus and a pit and the agent cannot occupy the same 
square, and that there is only one sack of gold, and only one wumpus.  
These assumptions are made based on the original definition of the problem 
(p. 153).

An omniscient agent knows the actual outcome of its actions and thus can 
act accordingly.  Thus, in the wumpus world, and omniscent agent would
"try" all the possible paths before actually going through them, and it would
select the path with the minimum cost (least number of steps taken).
This is what is done in the Java Applet, where the user can see the
entire world and is able to select the best path based on a wide information
base.  The performance score will be lower because no redundent steps will
be taken.
 


