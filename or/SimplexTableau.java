/*
 * Independent Study Project
 * MATH*4240 Advanced Topics in Modelling (Winter 2001)
 * Michael Bochenek [ID: 0041056] mboche01@uoguelph.ca
 */

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;


/**
 * This class implements functionality for the drawing area for the
 * simplex tableau. 
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class SimplexTableau extends Canvas implements Screen
{
   /** width of the drawing area */
   private int width;

   /** height of the drawign area */
   private int height;

   /** contains all the constraints */
   private ConstraintContainer constraints;

   /** the function to be optimized */
   private ZFunction zfunc;

   /** number of rows in simplex tableau */
   private int rows;

   /** number of columns in simplex tableau */
   private int columns;

   /** array of simple constraints */
   private SimpleConstraint [] simpleCons;

   /** data that contains the actual number is the simplex tableau */
   private TableData td;

   /** all the options */
   private Options options;

   private final int NEXT_ITERATION = 4501;

   private final int NUMBER_OF_BUTTONS = 3;

   /** all the buttons used */
   private SimpleButton [] button = new SimpleButton [NUMBER_OF_BUTTONS];

   public static final int OUTSIDE = -1;

   private int selectedButton = OUTSIDE;

   private int nextCounter = 0;
   private int pivotColumn = 0;
   private int pivotRow = 0;

   private String status1 = "Iteration 0";
   private String status2 = "Press 'Next' to begin";
   private String status3 = "(Find the pivot column)";

   /**
    * Constructor
    * @param w width of the drawing area
    * @param h height of the draw area
    */
   public SimplexTableau (int w, int h, Options o)
   {
      width = w;
      height = h;
      options = o;

      createButtons ();

      //constraints = options.cList;

      constraints = new ConstraintContainer (3);

      double [] tmp1 = {1, 0, 1, 0, 0};
      constraints.setConstraint ( new ComplexConstraint (5, tmp1, Constraint.LESS_THAN_OR_EQUAL_TO, 4.0), 0);

      double [] tmp2 = {0, 2, 0, 1, 0};
      constraints.setConstraint ( new ComplexConstraint (5, tmp2, Constraint.LESS_THAN_OR_EQUAL_TO, 12.0), 1);

      double [] tmp3 = {3, 2, 0, 0, 1};
      constraints.setConstraint ( new ComplexConstraint (5, tmp3, Constraint.LESS_THAN_OR_EQUAL_TO, 18.0), 2);

      double [] zfunctioncoeff = {-3, -5, 0, 0, 0};
      zfunc = new ZFunction (zfunctioncoeff, 5, true);

      rows = 3;
      columns = 5;

      td = new TableData (constraints, zfunc, simpleCons, rows, columns);

      //td.printDebug();

   }



   /**
    * Invoked each time the redraw manager decides to repaint the screen
    * @param g this component
    */
   public void paint (Graphics g)
   {
      g.setColor (options.getColor().getBackgroundC());

      g.fillRect (0, 0, width, height);
      //g.fillRect (10, 10, width - 20, height - 20);

      g.setColor (options.getColor().getBoxC());

      g.drawLine (10, 10, width - 20, 10);

      g.drawLine (70, 10, 70, height - 20 - 100);

      g.drawLine (105, 10, 105, height - 20 - 100);

      g.drawLine (105, 28, width - 20 - 60, 28);

      g.drawLine (140, 28, 140, height - 20 - 100);

      g.drawLine (width - 20 - 60, 10, width - 20 - 60, height - 20 - 100);

      g.drawLine (10, 50, width - 20, 50);


      g.setColor (options.getColor().getTextC());

      g.drawString ("Basic", 12, 26); 
      g.drawString ("Variable", 12, 42);

      g.drawString ("Eq.", 84, 26);

      g.drawString ("Coefficient of:", 340, 26);

      g.drawString ("Z", 120, 42);

      g.drawString ("Right", width - 20 - 60 + 5, 26);
      g.drawString ("Side", width - 20 - 60 + 5, 42);

      int xInterval = (width - 20 - 60 - 120) / columns;

      g.drawString ("-1", 120, 60 + Const.LINE_SPACE);
      for (int i = 0; i < columns; i++)
      {
         g.setColor (options.getColor().getTextC());
         g.drawString (("x" + (i+1)), 150 + xInterval * i, 42);

         if (td.getSelected (0,i) == true)
         {
            g.setColor (options.getColor().getSelectC());
         }
         else
         {
            g.setColor (options.getColor().getTextC());
         }

         g.drawString (td.getEntryString (0, i), 150 + xInterval * i, 60 + Const.LINE_SPACE);
      }
      g.drawString (td.getEntryString (0, columns), width - 20 - 60 + 5, 60 + Const.LINE_SPACE);

      for (int i = 0; i < rows; i++)
      {
         g.drawString ("0", 120, 60 + (i + 2) * Const.LINE_SPACE);

         for (int j = 0; j < columns; j++)
         {
            if (td.getSelected (i+1,j) == true)
            {
               g.setColor (options.getColor().getSelectC());
            }
            else
            {
               g.setColor (options.getColor().getTextC());
            }

            g.drawString (td.getEntryString (i+1,j), 150 + xInterval * j, 60 + (i + 2) * Const.LINE_SPACE);
         }

         g.setColor (options.getColor().getTextC());
         g.drawString (td.getEntryString (i+1, columns), width - 20 - 60 + 5, 60 + (i + 2) * Const.LINE_SPACE);
      }


      g.setColor (options.getColor().getBoxC());

      for (int i = 0; i < NUMBER_OF_BUTTONS; i++)
      {
         SimpleButton b = button[i];

         if (b.isSelected())
         {
            g.setColor (options.getColor().getSelectC());
         }
         else
         {
            g.setColor (options.getColor().getBoxC());
         }

         g.drawRect (b.getX(), b.getY(), b.getWidth(), b.getHeight());

         int xLoc = b.getX() + (b.getWidth() / 2) - (int) (b.getText().length() * 3.2);
         int yLoc = b.getY() + (b.getHeight() / 2) + 3;
         g.drawString (b.getText(), xLoc, yLoc);

      }

      g.setColor (options.getColor().getTextC());

      g.drawString (status1, 20, 480);
      g.drawString (status2, 20, 480 + Const.LINE_SPACE);
      g.drawString (status3, 20, 480 + 2 * Const.LINE_SPACE);

   }


   public int checkAction (int x, int y)
   {
      for (int i = 0; i < NUMBER_OF_BUTTONS; i++)
      {
         if (button[i].isInside (x, y))
         {
            if (button[i].getAction() == SimpleButton.UNDEFINED)
            {
               return i;
            }
            else if (button[i].getAction() == NEXT_ITERATION)
            {
               int commandType = nextCounter % 5;

               if (commandType == 0)
               {
                  pivotColumn = SimplexMethod.performIteration (td, rows + 1, columns);

                  if (td.getEntry (0, pivotColumn).getBaseOne() >= 0.0)
                  {
                     status1 = "Iteration " + (nextCounter / 5 + 1);
                     status2 = "The current tableau is optimal";
                     status3 = "(Every coefficient in 1st row is non-negative)";

                     int retVal = SimplexMethod.clearSelections (td, rows, columns);

                     // ensure nextCounter stays at current count
                     nextCounter--;
                  } 
                  else
                  {
                     status1 = "Iteration " + (nextCounter / 5 + 1);
                     status2 = "Press 'next' to find pivot row";
                     status3 = "(using the Minimum Ratio Rule)";
                  }
               }
               else if (commandType == 1)
               {
                  pivotRow = SimplexMethod.minimumRatioTest (td, pivotColumn, rows, columns); 
                  status1 = "Iteration " + (nextCounter / 5 + 1);
                  status2 = "Press 'next' to divide the pivot row by the pivot number";
                  status3 = "";
               }

               else if (commandType == 2)
               {
                  int retVal = SimplexMethod.rowOperations (td, pivotColumn, pivotRow, rows, columns);

                  status1 = "Iteration " + (nextCounter / 5 + 1);
                  status2 = "Press 'next' to perform elementary row operations on other rows";
                  status3 = "";
               }

               else if (commandType == 3)
               {
                  int retVal = SimplexMethod.moreRowOperations (td, pivotColumn, pivotRow, rows, columns);

                  status1 = "Iteration " + (nextCounter / 5 + 1);
                  status2 = "The iteration is complete";
                  status3 = "Press 'next' to remove the highlights from the tableau";
               }

               else if (commandType == 4)
               {
                  int retVal = SimplexMethod.clearSelections (td, rows, columns);

                  status1 = "Iteration " + ((nextCounter) / 5 + 1);
                  status2 = "Press 'Next' to begin next iteration";
                  status3 = "(Find the pivot column)";
               }



               nextCounter++;

               return SimpleButton.REPAINT;
            }
            else
            {
               return button[i].getAction();
            }
         }
      }

      return OUTSIDE;
   }


   public boolean mouseMoveChanged (int x, int y)
   {
      boolean hasChanged = false;
      int newSelectedButton = OUTSIDE;

      for (int i = 0; i < NUMBER_OF_BUTTONS; i++)
      {
         if (button[i].isInside (x, y))
         {
            button[i].select();
            newSelectedButton = i;
         }
         else
         {
            button[i].deselect();
         } 
      }


      if (newSelectedButton != selectedButton)
      {
         selectedButton = newSelectedButton;

         hasChanged = true;
      }

      return hasChanged;
   }


   private void createButtons ()
   {
      int w = 100;
      int h = 40;
      int x = (width - w) / 2 + 100;
      int y = 400;
      Color c = options.getColor().getBoxC();
      Color sc = options.getColor().getSelectC();

      button[0] = new SimpleButton (w, h, "Main Menu", x, y += (h + 10), SimpleButton.MAIN_MENU, c, sc); 
      button[1] = new SimpleButton (w, h, "Exit", x, y += (h + 10), SimpleButton.EXIT_PROGRAM, c, sc); 

      button[2] = new SimpleButton (w, h, "Next", x + w + 20, y, NEXT_ITERATION, c, sc);
   }


}

