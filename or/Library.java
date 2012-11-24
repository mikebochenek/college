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
 * This class draws all of the to select the library set 
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class Library extends Canvas implements Screen 
{
   /** width of the drawing area */
   private int width;

   /** height of the drawign area */
   private int height;

   private final int NUMBER_OF_BUTTONS = 8;

   /** all the buttons used */
   private SimpleButton [] button = new SimpleButton [NUMBER_OF_BUTTONS];

   /** all the options */
   private Options options;


   public static final int OUTSIDE = -1;

   private int selectedButton = OUTSIDE;

   private final int P1 = 8731;
   private final int P2 = 8732;
   private final int P3 = 8733;
   private final int P4 = 8734;
   private final int P5 = 8735;
   private final int P6 = 8736;
   private final int P7 = 8737;
   private final int P8 = 8738;


   /**
    * Constructor
    * @param w width of the drawing area
    * @param h height of the draw area
    */
   public Library (int w, int h, Options o)
   {
      width = w;
      height = h;

      options = o;

      createButtons ();
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
            else if (button[i].getAction() == P1)
            {
               // P1
               double [] zCoeff = {3.0, 5.0};
               options.zfunc = new ZFunction (zCoeff, 2, true);
               options.cList = new ComplexConstraint [5];
               options.cListSize = 5;
               zCoeff[0] = 1.0; zCoeff[1] = 0.0;
               options.cList[0] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 4.0);
               zCoeff[0] = 0.0; zCoeff[1] = 2.0;
               options.cList[1] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 12.0);
               zCoeff[0] = 3.0; zCoeff[1] = 2.0;
               options.cList[2] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 18.0);
               zCoeff[0] = 1.0; zCoeff[1] = 0.0;
               options.cList[3] = new ComplexConstraint (2, zCoeff, Constraint.GREATER_THAN_OR_EQUAL_TO, 0.0);
               zCoeff[0] = 0.0; zCoeff[1] = 1.0;
               options.cList[4] = new ComplexConstraint (2, zCoeff, Constraint.GREATER_THAN_OR_EQUAL_TO, 0.0);

               return SimpleButton.RELOAD;
            }

            else if (button[i].getAction() == P2)
            {
               // P2
               double [] zCoeff = {0.4, 0.5};
               options.zfunc = new ZFunction (zCoeff, 2, true);
               options.cList = new ComplexConstraint [5];
               options.cListSize = 5;
               zCoeff[0] = 0.3; zCoeff[1] = 0.1;
               options.cList[0] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 2.7);
               zCoeff[0] = 0.5; zCoeff[1] = 0.5;
               options.cList[1] = new ComplexConstraint (2, zCoeff, Constraint.EQUAL_TO, 6.0);
               zCoeff[0] = 0.6; zCoeff[1] = 0.4;
               options.cList[2] = new ComplexConstraint (2, zCoeff, Constraint.GREATER_THAN_OR_EQUAL_TO, 6.0);
               zCoeff[0] = 1.0; zCoeff[1] = 0.0;
               options.cList[3] = new ComplexConstraint (2, zCoeff, Constraint.GREATER_THAN_OR_EQUAL_TO, 0.0);
               zCoeff[0] = 0.0; zCoeff[1] = 1.0;
               options.cList[4] = new ComplexConstraint (2, zCoeff, Constraint.GREATER_THAN_OR_EQUAL_TO, 0.0);

               return SimpleButton.RELOAD;
            }

            else if (button[i].getAction() == P3)
            {
               // P3
               double [] zCoeff = {2.0, 1.0};
               options.zfunc = new ZFunction (zCoeff, 2, true);
               options.cList = new ComplexConstraint [6];
               options.cListSize = 6;
               zCoeff[0] = 0.0; zCoeff[1] = 1.0;
               options.cList[0] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 10.0);
               zCoeff[0] = 2.0; zCoeff[1] = 5.0;
               options.cList[1] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 60.0);
               zCoeff[0] = 1.0; zCoeff[1] = 1.0;
               options.cList[2] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 18.0);
               zCoeff[0] = 3.0; zCoeff[1] = 1.0;
               options.cList[3] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 44.0);
               zCoeff[0] = 0.0; zCoeff[1] = 1.0;
               options.cList[4] = new ComplexConstraint (2, zCoeff, Constraint.GREATER_THAN_OR_EQUAL_TO, 0.0);
               zCoeff[0] = 0.0; zCoeff[1] = 1.0;
               options.cList[5] = new ComplexConstraint (2, zCoeff, Constraint.GREATER_THAN_OR_EQUAL_TO, 0.0);

               return SimpleButton.RELOAD;
            }

            else if (button[i].getAction() == P4)
            {
               // P4
               double [] zCoeff = {10.0, 20.0};
               options.zfunc = new ZFunction (zCoeff, 2, true);
               options.cList = new ComplexConstraint [5];
               options.cListSize = 5;
               zCoeff[0] = 1.0; zCoeff[1] = 2.0;
               options.cList[0] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 15.0);
               zCoeff[0] = 1.0; zCoeff[1] = 1.0;
               options.cList[1] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 12.0);
               zCoeff[0] = 5.0; zCoeff[1] = 3.0;
               options.cList[2] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 45.0);
               zCoeff[0] = 1.0; zCoeff[1] = 0.0;
               options.cList[3] = new ComplexConstraint (2, zCoeff, Constraint.GREATER_THAN_OR_EQUAL_TO, 0.0);
               zCoeff[0] = 0.0; zCoeff[1] = 1.0;
               options.cList[4] = new ComplexConstraint (2, zCoeff, Constraint.GREATER_THAN_OR_EQUAL_TO, 0.0);

               return SimpleButton.RELOAD;
            }


            else if (button[i].getAction() == P5)
            {
               // P5
               double [] zCoeff = {3.0, 5.0};
               options.zfunc = new ZFunction (zCoeff, 2, true);
               options.cList = new ComplexConstraint [5];
               options.cListSize = 5;
               zCoeff[0] = 1.0; zCoeff[1] = 0.0;
               options.cList[0] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 4.0);
               zCoeff[0] = 0.0; zCoeff[1] = -2.0;
               options.cList[1] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 12.0);
               zCoeff[0] = 3.0; zCoeff[1] = -2.0;
               options.cList[2] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 18.0);
               zCoeff[0] = 1.0; zCoeff[1] = 0.0;
               options.cList[3] = new ComplexConstraint (2, zCoeff, Constraint.GREATER_THAN_OR_EQUAL_TO, 0.0);
               zCoeff[0] = 0.0; zCoeff[1] = 1.0;
               options.cList[4] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 0.0);

               return SimpleButton.RELOAD;
            }

            else if (button[i].getAction() == P6)
            {
               // P6
               double [] zCoeff = {0.4, 0.5};
               options.zfunc = new ZFunction (zCoeff, 2, true);
               options.cList = new ComplexConstraint [5];
               options.cListSize = 5;
               zCoeff[0] = 0.3; zCoeff[1] = 0.1;
               options.cList[0] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 2.7);
               zCoeff[0] = 0.5; zCoeff[1] = 0.5;
               options.cList[1] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 6.0);
               zCoeff[0] = 0.6; zCoeff[1] = 0.4;
               options.cList[2] = new ComplexConstraint (2, zCoeff, Constraint.GREATER_THAN_OR_EQUAL_TO, 6.0);
               zCoeff[0] = 1.0; zCoeff[1] = 0.0;
               options.cList[3] = new ComplexConstraint (2, zCoeff, Constraint.GREATER_THAN_OR_EQUAL_TO, 0.0);
               zCoeff[0] = 0.0; zCoeff[1] = 1.0;
               options.cList[4] = new ComplexConstraint (2, zCoeff, Constraint.GREATER_THAN_OR_EQUAL_TO, 0.0);

               return SimpleButton.RELOAD;
            }


            else
            {
               return button[i].getAction();
            }
         }
      }

      return OUTSIDE;
   }


   private void createButtons ()
   {
      int w = 500;
      int h = 30;
      int x = (width - w) / 2;
      int y = 50;
      Color c = options.getColor().getBoxC();
      Color sc = options.getColor().getSelectC();

      button[0] = new SimpleButton (w, h, "Problem #1:  The Prototype Example (p. 26)", x, y + 0, P1, c, sc); 
      button[1] = new SimpleButton (w, h, "Problem #2:  The Radiation Therapy Example (p. 45)", x, y += (h + 10), P2, c, sc); 
      button[2] = new SimpleButton (w, h, "Problem #3:  Problem 3.1-1 (p. 68)", x, y += (h + 10), P3, c, sc); 
      button[3] = new SimpleButton (w, h, "Problem #4:  Problem 3.1-2 (p. 68)", x, y += (h + 10), P4, c, sc); 
      button[4] = new SimpleButton (w, h, "Problem #5:  The Prototype Example modified", x, y += (h + 10), P5, c, sc); 
      button[5] = new SimpleButton (w, h, "Problem #5:  The Radiation Therapy Example Modified", x, y += (h + 10), P6, c, sc); 
      button[6] = new SimpleButton (w, h, "Main Menu", x, y += (h + 10), SimpleButton.MAIN_MENU, c, sc); 
      button[7] = new SimpleButton (w, h, "Exit", x, y += (h + 10), SimpleButton.EXIT_PROGRAM, c, sc); 
   }

}

