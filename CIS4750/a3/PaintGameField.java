/* CIS*4750 Artificial Intelligence
   Assignment #3 (6.13), Due: Friday October 20, 2000
   Michael Bochenek ID: 0041056 mboche01@uoguelph.ca */

import java.awt.Graphics;
import java.awt.Color;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;
import java.net.*;


public class PaintGameField extends Canvas 
{
   private static final int NOTHING = 0;
   private static final int WUMPUS = 1;
   private static final int AGENT = 2;
   private static final int PIT = 3;
   private static final int GOLD = 4;

   private static final int EAST = 10;
   private static final int WEST = 11;
   private static final int SOUTH = 12;
   private static final int NORTH = 13;
   public int direction = WEST;

   private static final int MAP_SIZE = 4;

   public int map [][] = new int [MAP_SIZE][MAP_SIZE];
   private String things[] = {"NOTHING", "WUMPUS", "AGENT", "PIT", "GOLD"};
   public int agentX = 0;
   public int agentY = 0;
   public int score = 0;
   private boolean stench = false;
   private boolean glitter = false;
   private boolean breeze = false;
   private boolean scream = false;
   private boolean bump = false; 

   public boolean wumpus_killed = false;
   public boolean death = false;
   public boolean arrow = true;
   public boolean gold_found = false;

   public PaintGameField ()
   {
      // generateRandomMap();
   }


   public void paint (Graphics g)
   {
      g.setColor (Color.white);
      g.fillRect ( 10, 10, 740, 440 );
      g.setColor (Color.black);
      g.drawRect ( 30, 30, 400, 400 );
      g.drawRect ( 450, 30, 280, 200 );

      g.drawString ( "Click (on map) where to go next.", 460, 45);
      g.drawString ( "Score: " + score, 460, 45+20);
      g.drawString ( "stench: " + stench, 460, 45+2*20); 
      g.drawString ( "breeze: " + breeze, 460, 45+3*20);
      g.drawString ( "glitter: " + glitter, 460, 45+4*20);
      g.drawString ( "bump: " + bump, 460, 45+5*20);
      g.drawString ( "scream: " + scream, 460, 45+6*20);

      for (int i = 0; i < MAP_SIZE - 1; i++)
      {
         for (int j = 0; j < MAP_SIZE - 1; j++)
         {
            g.drawLine (30, 130+(i*100), 430, 130+(i*100));
            g.drawLine (130+(i*100), 30, 130+(i*100), 430);
         }
      }

      for (int i = 0; i < MAP_SIZE; i++)
      {
         for (int j = 0; j < MAP_SIZE; j++)
         {
            paintThing (i, j, map[i][j], g);
         }
      }

      g.drawString ( "Game field:", 30, 25 );
   }


   public void paintThing (int i, int j, int thing, Graphics g)
   {
      if (thing == AGENT)
      {
         g.setColor (Color.red);
         g.drawString ("(" + (i+1) + "," + (MAP_SIZE-j) + ")", 35+(i*100), 10+35+(j*100) ); 
         g.drawString ( things[thing], 35+(i*100), 40+35+(j*100) );
      }
      else
      {
         g.setColor (Color.black);
         g.drawString ("(" + (i+1) + "," + (MAP_SIZE-j) + ")", 35+(i*100), 10+35+(j*100) ); 
         g.drawString ( things[thing], 35+(i*100), 40+35+(j*100) );
      }
   }


   public String updateAgent(int newAgentX, int newAgentY)
   { 
      if (death == false)
      {
         if (map [newAgentX][newAgentY] == PIT)
         {
            score -= 10000;
            death = true;
            map[agentX][agentY] = AGENT;
            return ("You have died, you have fallen into a pit.");
         }

         if (map [newAgentX][newAgentY] == WUMPUS && wumpus_killed == false)
         {
            score -= 10000;
            death = true;
            map[agentX][agentY] = AGENT;
            return ("You have died, the wumpus has eaten your ass.");
         }

         int x_diff = 0, y_diff = 0;

         x_diff = newAgentX - agentX;
         y_diff = newAgentY - agentY;

         if (x_diff == 1)
            direction = WEST;
         if (x_diff == -1)
            direction = EAST;
         if (y_diff == 1)
            direction = SOUTH;
         if (y_diff == -1)
            direction = NORTH;

         if ( (x_diff == 1 && y_diff == 0) || (x_diff == -1 && y_diff == 0) || (x_diff == 0 && y_diff == 1) || (x_diff == 0 && y_diff == -1) )
      
         {
            score -= 1;

            map[agentX][agentY] = NOTHING;
      
            agentX = newAgentX;
            agentY = newAgentY; 

            stench = false;
            breeze = false;
            glitter = false;
            bump = false;

            if ( (direction == SOUTH && agentY == 3) || (direction == NORTH && agentY == 0) || (direction == EAST && agentX == 0) || (direction == WEST && agentX == 3) )
            {
               bump = true;
            }

            try
            {
               int agentXP, agentXM, agentYP, agentYM;
               if (agentX == 0)
               {
                  agentXM = 0;
                  agentXP = 1;
               }
               else if (agentX == 3)
               {
                  agentXM = 2;
                  agentXP = 3;
               }
               else
               {
                  agentXM = agentX - 1;
                  agentXP = agentX + 1;
               }

               if (agentY == 0)
               {
                  agentYM = 0;
                  agentYP = 1;
               }
               else if (agentY == 3)
               {
                  agentYM = 2;
                  agentYP = 3;
               }
               else
               {
                  agentYM = agentY - 1;
                  agentYP = agentY + 1;
               }


               if (map[agentXM][agentY] == WUMPUS || map[agentXP][agentY] == WUMPUS || map[agentX][agentYM] == WUMPUS || map[agentX][agentYP] == WUMPUS)
               {
                  stench = true;
               }

               if (map[agentXM][agentY] == PIT || map[agentXP][agentY] == PIT || map[agentX][agentYM] == PIT || map[agentX][agentYP] == PIT)
               {
                  breeze = true;
               }

               if (map[agentX][agentY] == GOLD)
               {
                  gold_found = true;
                  glitter = true;
                  map[agentX][agentY] = AGENT;
                  return "Good job!  You have found the gold (return to start position to end game).";
               }
            }
            catch (Exception e)
            {
               /* do nothing, this should never be! */
               System.out.println ("This should never be!");
            }
 
            if (agentY == (MAP_SIZE - 1) && agentX == 0)
            {
               score += 100;
               return "You won!  You have climbed out of the the cave with the gold!";
            }
 
            map[agentX][agentY] = AGENT;

            return "OK";
         }
         else
         {
            return ("You can only move one square up/down or one square left/right.");
         }
      }

      return ("You are dead.  Please click on 'Generate New Map'.");
   }


   public String fireArrow ()
   {
      String returnVal = "You did NOT kill the wumpus.";

      if (arrow == false)
      {
         returnVal = "Sorry but you already used up your only arrow.";
      }
  
      if (direction == SOUTH)
      {
         for (int i = agentY; i < MAP_SIZE; i++)
         {
            if (map[agentX][i] == WUMPUS)
            {
               wumpus_killed = true;
               returnVal = "YOu have killed the wumpus!";
            }
         }
      }

      if (direction == NORTH)
      {
         for (int i = agentY; i >= 0; i--)
         {
            if (map[agentX][i] == WUMPUS)
            {
               wumpus_killed = true;
               returnVal = "YOu have killed the wumpus!";
            }
         }
      }

      if (direction == WEST)
      {
         for (int i = agentX; i < MAP_SIZE; i++)
         {
            if (map[i][agentY] == WUMPUS)
            {
               wumpus_killed = true;
               returnVal = "YOu have killed the wumpus!";
            }
         }
      }

      if (direction == EAST)
      {
         for (int i = agentX; i >= 0; i--)
         {
            if (map[i][agentY] == WUMPUS)
            {
               wumpus_killed = true;
               returnVal = "YOu have killed the wumpus!";
            }
         }
      }

      arrow = false;

      return returnVal;
   }


   public void generateRandomMap ()
   {
      for (int i = 0; i < MAP_SIZE; i++)
      {
         for (int j = 0; j < MAP_SIZE; j++)
         {
            map[i][j] = NOTHING;
         }
      }

      Random rand = new Random();

      for (int i = 0; i < MAP_SIZE; i++)
      {
         for (int j = 0; j < MAP_SIZE; j++)
         {
            int probability = rand.nextInt() % 5;
            if (probability < 0) probability *= -1;
            if (probability == 3)
            {
               map[i][j] = PIT;
            }
         }
      }

      int wumpusPos, goldPos, agentPos;
      do
      {
         wumpusPos = rand.nextInt() % (MAP_SIZE * MAP_SIZE - 2);
         goldPos = rand.nextInt() % (MAP_SIZE * MAP_SIZE - 2);
         if (wumpusPos < 0) wumpusPos *= -1;
         if (goldPos < 0) goldPos *= -1;
      } while (goldPos == wumpusPos);


      goldPos++;
      wumpusPos++;

      int x, y;
 
      x = wumpusPos / MAP_SIZE;
      y = wumpusPos % MAP_SIZE;
      map[x][y] = WUMPUS; 

      x = goldPos / MAP_SIZE;
      y = goldPos % MAP_SIZE;
      map[x][y] = GOLD;

      agentY = MAP_SIZE - 1;
      agentX = 0;
      map[agentX][agentY] = AGENT;
   }


}

