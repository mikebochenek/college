/* CIS*4750 Artificial Intelligence
   Assignment #3 (6.13), Due: Friday October 20, 2000
   Michael Bochenek ID: 0041056 mboche01@uoguelph.ca */

public class Agent
{
   private static final int NOTHING = 0;
   private static final int WUMPUS = 1;
   private static final int AGENT = 2;
   private static final int PIT = 3;
   private static final int GOLD = 4;
   private static final int STENCH = 5;
   private static final int BREEZE = 6;

   private static final int MAP_SIZE = 4;

   private int map [][] = new int [MAP_SIZE][MAP_SIZE];

   public int agentX;
   public int agentY;

   public Agent()
   {
      agentX = 0;
      agentY = MAP_SIZE - 1;
   }

   public String stepThrough (boolean stench,
                           boolean breeze,
                           boolean glitter,
                           boolean bump,
                           boolean scream)
   {
      String returnStr = "Agent moves from (" + (agentX+1) + "," + (MAP_SIZE-agentY) + ") ";

      agentX++;

      returnStr += " to (" + (agentX+1) + "," + (MAP_SIZE-agentY) + ").";

      return returnStr;
   }

}

