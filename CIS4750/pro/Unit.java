import java.util.Vector;
import java.util.Random;

/**
 * This class will contain all non-graphical information
 * for each unit, artificial intelligence stuff will probably
 * go here.
 */
public class Unit extends GraphicalUnit
{
   private int power = 100;
   private int dir = Const.EAST;
   private int AIType = 0;
   private int utility = 0;
   private int cumUtility = 0;
   public boolean alive = true;
   private boolean attacked = false;
   private boolean killed = false;
   private int situation = 0;


   /**
    * Empty constructor never explicitly called.
    */
   public Unit ()
   {
   }


   /**
    * This constructor on the other hand is used all the time.
    */
   public Unit (int id, int uType, int ai) 
   {
      unitID = id;
      unitType = uType;
      AIType = ai;
   }

  
   /**
    * This function is called each time the timer fires and it start all the
    * "artificial intelligence stuff".
    */ 
   public void go(int random, int uID, Option op, Vector units)
   {
      options = op;
      engaged = false;

      if (AIType == Const.AI_None)
      {
         int newColumn = getColumn();
         int newRow = getRow();

         if (dir == Const.EAST)
         {
            newColumn++;
            if (column == Const.numOfColumns - 1)
            {
               dir = Const.WEST;
            }
         }
         else if (dir == Const.WEST)
         { 
            newColumn--;
            if (column == 0)
            {
               dir = Const.EAST;
            }
         }
         else if (dir == Const.SOUTH)
         {
            newRow++;
            if (row == Const.numOfRows - 1)
            {
               dir = Const.NORTH;
            }
         }
         else if (dir == Const.NORTH)
         {
            newRow--;
            if (row == 0)
            {
               dir = Const.SOUTH;
            }
         }

         if (squareEmpty (units, uID, newRow, newColumn) == true)
         {
            setLocation (newRow, newColumn);
         }
         else
         {
            if (dir == Const.SOUTH)
            {
               dir = Const.EAST;
            }
            else if (dir == Const.NORTH)
            {
               dir = Const.WEST;
            }
            else if (dir == Const.EAST)
            {
               dir = Const.SOUTH;
            }
            else if (dir == Const.WEST)
            {
               dir = Const.NORTH;
            }
         }
 
      }


      else if (AIType == Const.AI_Random)
      { 
         int newColumn = getColumn();
         int newRow = getRow();

         int newDir = getRandomDir();

         if (newDir == Const.EAST) 
         {
            newColumn++;
         }
         else if (newDir == Const.WEST) 
         {
            newColumn--;
         }
         else if (newDir == Const.SOUTH) 
         {
            newRow++;
         }
         else if (newDir == Const.NORTH) 
         {
            newRow--;
         }

         if (squareEmpty (units, uID, newRow, newColumn) == true)
         {
            setLocation (newRow, newColumn);
         }

         dir = newDir;
      } 


      else if (AIType == Const.AI_Aggressive)
      { 
         int newColumn = getColumn();
         int newRow = getRow();

         dir = getRandomDir();

         int range = 0;
         if (unitType == Const.REDUNIT)
         {
            range = options.redVisibility;
         }
         else if (unitType == Const.BLUEUNIT)
         {
            range = options.blueVisibility;
         }

         boolean haveSeenUnit = false;
         for (int i = 0; i <= range && haveSeenUnit == false; i++)
         {
            for (int k = 0; k < units.size(); k++)
            {
               Unit u = (Unit) units.elementAt (k);
               if (u.getID() != uID && u.unitType != unitType)
               {
                  if (isVisible (u, i))
                  {
                     haveSeenUnit = true;
                     dir = moveTowards (u, units, getID());
                     break;
                  }
               } 
            }
         }

         if (dir == Const.EAST)
         {
            newColumn++;
            if (column == Const.numOfColumns - 1)
            {
               newColumn--;
            }
         }
         else if (dir == Const.WEST)
         { 
            newColumn--;
            if (column == 0)
            {
               newColumn++;
            }
         }
         else if (dir == Const.SOUTH)
         {
            newRow++;
            if (row == Const.numOfRows - 1)
            {
               newRow--;
            }
         }
         else if (dir == Const.NORTH)
         {
            newRow--;
            if (row == 0)
            {
               newRow++;
            }
         }

         if (squareEmpty (units, uID, newRow, newColumn) == true)
         {
            setLocation (newRow, newColumn);
         }
      } 


      else if (AIType == Const.AI_Defensive)
      { 
         int newColumn = getColumn();
         int newRow = getRow();

         dir = getRandomDir();

         int range = 0;
         if (unitType == Const.REDUNIT)
         {
            range = options.redVisibility;
         }
         else if (unitType == Const.BLUEUNIT)
         {
            range = options.blueVisibility;
         }

         boolean haveSeenUnit = false;
         for (int i = 0; i <= range && haveSeenUnit == false; i++)
         {
            for (int k = 0; k < units.size(); k++)
            {
               Unit u = (Unit) units.elementAt (k);
               if (u.getID() != uID && u.unitType != unitType)
               {
                  if (isVisible (u, i))
                  {
                     haveSeenUnit = true;
                     dir = moveAwayFrom (u, units, getID());
                     break;
                  }
               } 
            }
         }

         if (dir == Const.EAST)
         {
            newColumn++;
            if (column == Const.numOfColumns - 1)
            {
               newColumn--;
            }
         }
         else if (dir == Const.WEST)
         { 
            newColumn--;
            if (column == 0)
            {
               newColumn++;
            }
         }
         else if (dir == Const.SOUTH)
         {
            newRow++;
            if (row == Const.numOfRows - 1)
            {
               newRow--;
            }
         }
         else if (dir == Const.NORTH)
         {
            newRow--;
            if (row == 0)
            {
               newRow++;
            }
         }

         if (squareEmpty (units, uID, newRow, newColumn) == true)
         {
            setLocation (newRow, newColumn);
         }
      } 


      else if (AIType == Const.AI_Unify)
      { 
         int newColumn = getColumn();
         int newRow = getRow();

         dir = getRandomDir();

         int range = 0;
         if (unitType == Const.REDUNIT)
         {
            range = options.redCom;
         }
         else if (unitType == Const.BLUEUNIT)
         {
            range = options.blueCom;
         }

         int maxSituation = 0;
         int maxSituationUnitID = 0;
         Unit maxUnit = this;
         boolean maxUnitExists = false;

         for (int i = 0; i <= range; i++)
         {
            for (int k = 0; k < units.size(); k++)
            {
               Unit u = (Unit) units.elementAt (k);
               if (u.getID() != uID && u.unitType == unitType)
               {
                  if (isVisible (u, i))
                  {
                     if (u.getSituation() > maxSituation)
                     {
                        maxSituation = u.getSituation();
                        maxSituationUnitID = u.getID ();
                        maxUnit = u;
                        maxUnitExists = true;
                     }
                  }
               } 
            }
         }

         if (maxUnitExists == true)
         {
            dir = moveTowards (maxUnit, units, getID());
         }

         if (dir == Const.EAST)
         {
            newColumn++;
            if (column == Const.numOfColumns - 1)
            {
               newColumn--;
            }
         }
         else if (dir == Const.WEST)
         { 
            newColumn--;
            if (column == 0)
            {
               newColumn++;
            }
         }
         else if (dir == Const.SOUTH)
         {
            newRow++;
            if (row == Const.numOfRows - 1)
            {
               newRow--;
            }
         }
         else if (dir == Const.NORTH)
         {
            newRow--;
            if (row == 0)
            {
               newRow++;
            }
         }

         if (squareEmpty (units, uID, newRow, newColumn) == true)
         {
            setLocation (newRow, newColumn);
         }
      } 


      else
      {
         System.out.println ("Error: Type of intelligence not defined, AIType=" + AIType);
         System.exit(-1);
      }


      situation = evalSituation (units, uID, this);

   } 


   /**
    * Will return the string that is placed in the message.
    */
   public String getInfo ()
   {
      String retVal = new String();

      retVal += "Unit ID: " + unitID;

      retVal += "\nUnit Type: ";
      if (unitType == Const.REDUNIT) 
      {
         retVal += "Lizard";
      }
      else if (unitType == Const.BLUEUNIT) 
      {
         retVal += "Dinosaur";
      }

      retVal += "\nAlive: " + alive;

      retVal += "\nPower: " + power;

      retVal += "\nRow: " + (getRow() + 1);

      retVal += "\nColumn: " + (getColumn() + 1);

      retVal += "\nDirection: ";
      if (dir == Const.EAST) 
      {
         retVal += "East";
      }
      else if (dir == Const.WEST) 
      {
         retVal += "West";
      }
      else if (dir == Const.SOUTH) 
      {
         retVal += "South";
      }
      else if (dir == Const.NORTH) 
      {
         retVal += "North";
      }

      retVal += "\nEngaged: " + (engaged);
   
      retVal += "\nIntelligence: " + Const.AITypes[AIType];;

      retVal += "\nUtility: " + utility;

      retVal += "\nCumulative Utility: " + cumUtility;

      retVal += "\nSituation Evaluation: " + situation;

      return retVal;


   }

  
   /**
    * Checks if this unit is touching any other units.
    */ 
   public void checkContact (int uID, Option op, Vector units)
   {
      int newColumn = getColumn();
      int newRow = getRow();
      attacked = false;
      killed = false;
      for (int k = 0; k < units.size(); k++)
      {
         Unit u = (Unit) units.elementAt (k);
         if (u.getID() != uID && u.unitType != unitType)
         {
            if (isTouching (u))
            {
               engage (u, units);
            }
         } 
      }
   }


   /**
    * Completely kill unit enemy.
    */
   public void kill (Unit enemy)
   {
      enemy.alive = false;
      enemy.visible = false;
      enemy.utility = -1 * enemy.utility;
      enemy.cumUtility = -1 * enemy.cumUtility;
   }


   /**
    * Engage in combat with unit enemy.
    */
   private void engage (Unit enemy, Vector allUnits)  
   {
      enemy.engaged = true;
      enemy.power -= options.firepower;

      attacked = true;

      if (enemy.power <= 0)
      {
         killed = true;
         kill (enemy);
         allUnits.remove (enemy);
      }

      /* engaged = true; not necessary! */
      /* power -= options.firepower; not necessary */
   }


   /**
    * Function evaluates the utility based on which utility
    * function is specified in the options file.
    */
   public void evaluateUtility (Vector units)
   {
      if (unitType == Const.REDUNIT)
      {
         if (options.redFun == 0)
         {
            utility = utilityFunctionA();
         }
         else if (options.redFun == 1)
         {
            utility = utilityFunctionB();
         }
         else if (options.redFun == 2)
         {
            utility = utilityFunctionC();
         }
         else
         {
            System.out.println ("Error: Type of utility function not defined, lizFun=" + options.redFun);
            System.exit(-1);
         }
      }

      else if (unitType == Const.BLUEUNIT)
      {
         if (options.blueFun == 0)
         {
            utility = utilityFunctionA();
         }
         else if (options.blueFun == 1)
         {
            utility = utilityFunctionB();
         }
         else if (options.blueFun == 2)
         {
            utility = utilityFunctionC();
         }
         else
         {
            System.out.println ("Error: Type of utility function not defined, dinoFun=" + options.blueFun);
            System.exit(-1);
         }
      }

      else
      {
         System.out.println ("Error: Invalid unittype");
      }

      cumUtility += utility;
   }


   /**
    * One of three utility functions.
    */
   private int utilityFunctionA ()
   {
      if (power < 50 && engaged)
      {
         return 5;
      }
      else if (power < 50 && ! engaged)
      {
         return 1;
      }
      else if (power >= 50 && engaged)
      {
         return 10;
      }
      else
      {
         return 1;
      }
   }


   /**
    * One of three utility functions.
    */
   private int utilityFunctionB ()
   {
      return (100 - power);
   }


   /**
    * One of three utility functions.
    */
   private int utilityFunctionC ()
   {
      return (power);
   }


   /**
    * Accessor function 
    */
   public int getID ()
   {
      return unitID;
   }


   /**
    * Accessor function 
    */
   public int getPower ()
   {
      return power;
   }


   /**
    * Accessor function 
    */
   public int getUtility ()
   {
      return utility;
   }


   /**
    * Accessor function 
    */
   public int getTotalUtility ()
   {
      return cumUtility;
   }


   /**
    * Accessor function 
    */
   public int getSituation ()
   {
      return situation;
   }


}
