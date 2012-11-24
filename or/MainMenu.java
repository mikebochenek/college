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
 * This class draws all of the main menu buttons on the screen
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class MainMenu extends Canvas implements Screen 
{
   /** width of the drawing area */
   private int width;

   /** height of the drawign area */
   private int height;

   private final int NUMBER_OF_BUTTONS = 7;

   /** all the buttons used */
   private SimpleButton [] button = new SimpleButton [NUMBER_OF_BUTTONS];

   /** all the options */
   private Options options;

   public static final int OUTSIDE = -1;

   private int selectedButton = OUTSIDE;


   /**
    * Constructor
    * @param w width of the drawing area
    * @param h height of the draw area
    */
   public MainMenu (int w, int h, Options o)
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
      //g.fillRect (Const.BIG_OFFSET, Const.BIG_OFFSET, width - 2 * Const.BIG_OFFSET, height - 2 * Const.BIG_OFFSET);

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

      g.drawString ("Speedy Solver 1.0", 20, height - 20 - Const.LINE_SPACE);
      g.drawString ("By Michael Bochenek 2001", 20, height - 20);
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
            return i;
         }
      }

      return OUTSIDE;
   }


   private void createButtons ()
   {
      int w = 400;
      int h = 40;
      int x = (width - w) / 2;
      int y = 50;
      Color c = options.getColor().getBoxC();
      Color sc = options.getColor().getSelectC();

      button[0] = new SimpleButton (w, h, "Edit a linear programming problem", x, y + 0, c, sc); 
      button[1] = new SimpleButton (w, h, "Problem Library", x, y += (h + 10), c, sc); 
      button[2] = new SimpleButton (w, h, "Solve using Graphical Method", x, y += (h + 10), c, sc); 
      button[3] = new SimpleButton (w, h, "Solve using Simplex Method", x, y += (h + 10), c, sc); 
      button[4] = new SimpleButton (w, h, "Options", x, y += (h + 10), c, sc); 
      button[5] = new SimpleButton (w, h, "Help", x, y += (h + 10), c, sc); 
      button[6] = new SimpleButton (w, h, "Exit", x, y += (h + 10), SimpleButton.EXIT_PROGRAM, c, sc); 
   }

}

