import java.awt.*;
import java.util.Vector;
import java.util.Random;

/**
 * Class that contains all properties and methods associated
 * with the graphical appearance of a unit.
 */
public class GraphicalUnit
{
   protected int x_coord;
   protected int y_coord;
   protected int column = 0;
   protected int row = 0;
   protected Image img;
   protected int unitType;
   protected boolean engaged = false;
   protected boolean visible = true;
   protected int unitID = 0;
   private Vector localUnits;
   private int localID;
   private Random randGen = new Random();
   protected Option options;


   /**
    * Empty constructor that is never explicitly called.
    */
   public GraphicalUnit()
   {
   }


   /**
    * Moves the picture to row R and column C.  If R and C is outside
    * the playing field, no move is performed.
    */
   protected void setLocation (int r, int c)
   {
      if (r >= 0 && r < Const.numOfRows)
      { 
         row = r;
      }
      if (c >= 0 && c < Const.numOfColumns)
      {
         column = c;
      }
      x_coord = column * (Const.drawAreaWidth / Const.numOfColumns);
      y_coord = row * (Const.drawAreaHeight / Const.numOfRows);
   }


   /**
    * This function only paints this object to the canvas.
    */
   public void paint (Graphics g, DrawArea area)
   {
      if (engaged == true)
      {
         if (unitType == Const.REDUNIT)
         {
            img = Toolkit.getDefaultToolkit().getImage ( "lizzard2.gif" );
         }
         else if (unitType == Const.BLUEUNIT)
         {
            img = Toolkit.getDefaultToolkit().getImage ( "dino2.gif" );
         }
      }
      else if (engaged == false)
      {
         if (unitType == Const.REDUNIT)
         {
            img = Toolkit.getDefaultToolkit().getImage ( "lizzard1.gif" );
         }
         else if (unitType == Const.BLUEUNIT)
         {
            img = Toolkit.getDefaultToolkit().getImage ( "dino1.gif" );
         }
      }
 
      if (visible == true)
      {
         g.drawImage (img, x_coord, y_coord, area);
      }
   }


   /**
    * Used for debugging purposes only.
    */
   protected void move (int direction)
   {
      if (direction == Const.SOUTH)
      {
         if (row < (Const.numOfRows - 1))
         {
            row++;
         }      
      }
      else if (direction == Const.NORTH)
      {
         if (row > 0)
         {
            row--;
         }      
      }
      else if (direction == Const.EAST)
      {
         if (column < (Const.numOfColumns - 1))
         {
            column++;
         }
      }
      else if (direction == Const.WEST)
      {
         if (column > 0)
         {
            column--;
         }
      }

      x_coord = column * (Const.drawAreaWidth / Const.numOfColumns);
      y_coord = row * (Const.drawAreaHeight / Const.numOfRows);
   }


   /**
    * Randomly generates a new direction EAST, WEST, SOUTH, NORTH.
    */
   protected int getRandomDir ()
   {
      int newColumn = getColumn();
      int newRow = getRow();

      int randInteger = randGen.nextInt (1000000);

      int newDir = randInteger% 4;

      if (newDir == 0) 
      {
         newDir = Const.EAST;
      }
      else if (newDir == 1) 
      {
         newDir = Const.WEST;
      }
      else if (newDir == 2) 
      {
         newDir = Const.SOUTH;
      }
      else if (newDir == 3) 
      {
         newDir = Const.NORTH;
      }

      return newDir;
   }


   /**
    * Check if this unit is touching unit U.
    */
   protected boolean isTouching (Unit u)
   {
      return this.isVisible (u, 1);
   }


   /**
    * Check if this unit can "see" unit U.  In other words
    * if the distance between the two units is <= the visibility range.
    */
   protected boolean isVisible (Unit u, int range)
   {
      boolean returnVal = false;
      int rowA = getRow();
      int rowB = u.getRow();
      int columnA = getColumn();
      int columnB = u.getColumn();

      if (Math.abs (rowA - rowB) <= range && Math.abs (columnA - columnB) <= range)
      {
         return (returnVal = true); 
      }

      return returnVal;
   }


   /**
    * Move towards unit U.
    */
   protected int moveTowards (Unit u, Vector units, int myID)
   {
      localUnits = units;
      localID = myID;
      int rowDiff = row - u.getRow();
      int columnDiff = column - u.getColumn();
      int proposedDir = -1;

      if (Math.abs (rowDiff) >= Math.abs (columnDiff))
      {
         if (rowDiff < 0)
         {
            proposedDir = Const.SOUTH;
         }
         else
         {
            proposedDir = Const.NORTH;
         }
      }
      else
      {
         if (columnDiff < 0)
         {
            proposedDir = Const.EAST;
         }
         else
         {
            proposedDir = Const.WEST;
         }
      }

      int maxTries = 20;
      while (attemptMove(proposedDir) == false)
      {
         proposedDir = getRandomDir(); 
         maxTries--;
         if (maxTries == 0)
         {
            break;
         }
      }

      return proposedDir;
   }


   /**
    * Move away from unit U.
    */
   protected int moveAwayFrom (Unit u, Vector units, int myID)
   {
      localUnits = units;
      localID = myID;
      int rowDiff = row - u.getRow();
      int columnDiff = column - u.getColumn();
      int proposedDir = -1;

      if (Math.abs (rowDiff) >= Math.abs (columnDiff))
      {
         if (rowDiff < 0)
         {
            proposedDir = Const.NORTH;
         }
         else
         {
            proposedDir = Const.SOUTH;
         }
      }
      else
      {
         if (columnDiff < 0)
         {
            proposedDir = Const.WEST;
         }
         else
         {
            proposedDir = Const.EAST;
         }
      }

      int maxTries = 20;
      while (attemptMove(proposedDir) == false)
      {
         proposedDir = getRandomDir(); 
         maxTries--;
         if (maxTries == 0)
         {
            break;
         }
      }

      return proposedDir;
   }


   /**
    * Called by moveTowards and MoveAwayFrom to see if the move
    * is possible, ie if any other unit is in the way.
    */
   protected boolean attemptMove (int direction)
   {
      int tempRow = row;
      int tempColumn = column;
      boolean returnVal = false;

      if (direction == Const.SOUTH)
      {
         if (row < (Const.numOfRows - 1))
         {
            tempRow++;
            returnVal = true;
         }      
      }
      else if (direction == Const.NORTH)
      {
         if (row > 0)
         {
            tempRow--;
            returnVal = true;
         }      
      }
      else if (direction == Const.EAST)
      {
         if (column < (Const.numOfColumns - 1))
         {
            tempColumn++;
            returnVal = true;
         }
      }
      else if (direction == Const.WEST)
      {
         if (column > 0)
         {
            tempColumn--;
            returnVal = true; 
         }
      }

      if (returnVal == true)
      {
         returnVal = squareEmpty (localUnits, localID, tempRow, tempColumn); 
      }

      return returnVal;
   }


   /**
    * Check if the square (newRow, newColumn) is occupied
    */
   protected boolean squareEmpty(Vector units, int meID, int newRow, int newColumn)
   {
      boolean emptySquare = true;

      for (int k = 0; k < units.size(); k++)
      {
         Unit u = (Unit) units.elementAt (k);
         if (u.getID() != meID)
         {
            if (u.getRow() == newRow && u.getColumn() == newColumn)
            {
               emptySquare = false;
            }
         } 
      }

      return emptySquare;
   }


   /**
    * Evaluate the situation.  Used for communication purposes.
    */
   protected int evalSituation (Vector units, int meID, Unit me)
   {
      int returnVal = 0;

      int range = 0;
      if (unitType == Const.REDUNIT)
      {
         range = options.redVisibility;
      }
      else if (unitType == Const.BLUEUNIT)
      {
         range = options.blueVisibility;
      }

      for (int k = 0; k < units.size(); k++)
      {
         Unit u = (Unit) units.elementAt (k);
         if (isVisible (u, range))
         {
            if (u.getUnitType() == me.getUnitType())
            {
               returnVal += u.getPower();
            }
            else
            {
               returnVal -= u.getPower();
            }
         } 
      }

      return returnVal;
   }


   /**
    * Accessor function.
    */
   public int getColumn ()
   {
      return column;
   }


   /**
    * Accessor function.
    */
   public int getRow ()
   {
      return row;
   }


   /**
    * Accessor function.
    */
   public int getUnitType ()
   {
      return unitType;
   }


}

