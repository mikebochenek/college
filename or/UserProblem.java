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
 * This class gets the user input for a linear problem 
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class UserProblem extends Canvas implements Screen 
{
   /** width of the drawing area */
   private int width;

   /** height of the drawign area */
   private int height;

   private final int NUMBER_OF_BUTTONS = 3;

   /** all the buttons used */
   private SimpleButton [] button = new SimpleButton [NUMBER_OF_BUTTONS];

   private Options options;

   public static final int REPAINT = 10029;
   public static final int DO_NOT_REPAINT = 10028;

   public static final int CLEAR = 10034;

   public static final int OUTSIDE = -1;

   private int selectedButton = OUTSIDE;

   private String status = "Move with arrow keys to edit table:  ";

   private String buffer = "";

   private int stage = 0;
   private int row = -1;
   private int column = -1;
   private int currentRow = 0;
   private int currentColumn = 0;

   private boolean drawConstraints = false;

   private ComplexConstraint [] constraints;

   private int constraintsSize;

   private ZFunction zfunc;

   private String zfunctionString;

   /**
    * Constructor
    * @param w width of the drawing area
    * @param h height of the draw area
    */
   public UserProblem (int w, int h, Options o)
   {
      width = w;
      height = h;

      options = o;

      zfunc = options.zfunc;
      constraintsSize = options.cListSize;
      constraints = options.cList;

      column = options.cList[0].size();
      row = constraintsSize;
      drawConstraints = true;

      if (zfunc.getMaximize() == true)
      {
         zfunctionString = "Maximize z = ";
      }
      else
      {
         zfunctionString = "Minimize z = ";
      }

      for (int i = 0; i < column; i++)
      {
         zfunctionString += zfunc.getCoeffAt (i) + "x" + (i + 1); 
         if (i != column - 1)
         {
            zfunctionString += " + ";
         }
      }


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


      g.setColor (options.getColor().getTextC());

      g.drawString (status + buffer, 20, 380);



      if (drawConstraints == true)
      {
         g.drawString ("Number of variables: " + column + ".  Number of constraints: " + row, 20, 20);

         g.drawString (zfunctionString, 20, 20 + Const.LINE_SPACE); 

         for (int i = 0; i < row; i++)
         {
            g.drawString (constraints[i].toString(), 20, 2 * Const.LINE_SPACE + 20 + i * Const.LINE_SPACE);

         }

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
            else if (button[i].getAction() == CLEAR)
            {

               stage = -10;
               row = -1;
               column = -1;
               currentRow = 0;
               currentColumn = 0;
               drawConstraints = false;

               constraints = null;

               constraintsSize = 0;

               zfunc = null;

               zfunctionString = "";

               status = "Enter the number of variables:  "; 


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


   private void createButtons ()
   {
      int w = 200;
      int h = 40;
      int x = (width - w) / 2;
      int y = 350;
      Color c = options.getColor().getBoxC();
      Color sc = options.getColor().getSelectC();

      button[0] = new SimpleButton (w, h, "Clear", x, y += (h + 10), CLEAR, c, sc); 
      button[1] = new SimpleButton (w, h, "Main Menu", x, y += (h + 10), SimpleButton.MAIN_MENU, c, sc); 
      button[2] = new SimpleButton (w, h, "Exit", x, y += (h + 10), SimpleButton.EXIT_PROGRAM, c, sc); 
   }


   public int processMove (int code)
   {
      if (code == KeyEvent.VK_LEFT) 
      {
         return processBuffer();
      }
      else if (code == KeyEvent.VK_RIGHT) 
      {
         return processBuffer();
      }
      else if (code == KeyEvent.VK_UP) 
      {
         return processBuffer();
      }
      else if (code == KeyEvent.VK_DOWN) 
      {
         return processBuffer();
      }
      else if (code == KeyEvent.VK_TAB) 
      {
         return processBuffer();
      }
      else if (code == KeyEvent.VK_ENTER) 
      {
         return processBuffer();
      }
      else if (code == KeyEvent.VK_BACK_SPACE)
      {
         if (buffer.length() != 0)
         {
            buffer = buffer.substring (0, buffer.length() - 1);
            return REPAINT;
         }
         else
         {
            return DO_NOT_REPAINT;
         }
      }

      return DO_NOT_REPAINT;

   }


   public int processKey (char c)
   {

      if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9')
      {
         buffer += c;
         return REPAINT;
      }
      else if (c == 'M' || c == 'm')
      {
         buffer += c;
         return REPAINT;
      }
      else if (c == '<' || c == '>' || c == '=')
      {
         buffer += c;
         return REPAINT;
      }
      else if (c == '+' || c == '-')
      {
         buffer += c;
         return REPAINT;
      }
      else 
      {
         return DO_NOT_REPAINT;
      }
   }


   private int processBuffer ()
   {
      if (stage == -10)
      {
         try
         {
            int c = Integer.parseInt (buffer);
            column = c;
            status = "Enter the number of constraints:  ";
            buffer = "";
            stage++;
         }
         catch (Exception e)
         {
            status = "You must enter a number.  " + status;
            buffer = "";
         }
      }

      else if (stage == -9)
      {
         try
         {
            int c = Integer.parseInt (buffer);
            row = c;
            buffer = "";
            stage++;
            createConstraints ();
            status = "For constraint " + (currentRow + 1) + ", enter the coefficient of x" + (currentColumn + 1) + ":  ";
         }
         catch (Exception e)
         {
            status = "You must enter a number.  " + status;
            buffer = "";
         }
      }

      else if (stage == -8)
      {
         try
         {
            int c = Integer.parseInt (buffer);
            row = c;
            status = "For constraint " + (currentRow + 1) + ", enter the coefficient of x" + (currentColumn + 1) + ":  ";
            buffer = "";
            currentColumn++;
            stage++;
         }
         catch (Exception e)
         {
            status = "You must enter a number.  " + status;
            buffer = "";
         }
      }

      else if (stage == -7)
      {
         try
         {
            double c = Double.parseDouble (buffer);
            buffer = "";


            buffer = "";
            currentColumn++;

            constraints[currentRow].setCoeffAt (currentColumn-1, c);

            if (currentRow == row && currentColumn != column)
            {
               currentColumn = 0;
               currentRow++;  
            }


            if (currentRow == row && currentColumn == column)
            {
               stage++;
            }
            status = "For constraint " + (currentRow + 1) + ", enter the coefficient of x" + (currentColumn + 1) + ":  ";
         }
         catch (Exception e)
         {
            status = "You must enter a number.  " + status;
            buffer = "";
         }
   

      }


      else if (stage == -6)
      {
         // get Z function

      }

      else
      {
         System.out.println ("Undefined stage");
      }
         

      return REPAINT;
   }


   private void createConstraints ()
   {
      double [] d = new double [column];
      for (int i = 0; i < column; i++)
      {
         d[i] = 0;
      }

      constraints = new ComplexConstraint [row];

      for (int i = 0; i < row; i++)
      {
         constraints[i] = new ComplexConstraint (column, d, Constraint.LESS_THAN_OR_EQUAL_TO, 0.0);
      }

      drawConstraints = true;

   }

}

