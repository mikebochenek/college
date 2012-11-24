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
 * This class draws all of the main buttons buttons on the screen
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class OptionsMenu extends Canvas implements Screen 
{
   /** width of the drawing area */
   private int width;

   /** height of the drawign area */
   private int height;

   private final int NUMBER_OF_BUTTONS = 14;

   /** all the buttons used */
   private SimpleButton [] button = new SimpleButton [NUMBER_OF_BUTTONS];


   private Options options;

   public static final int OUTSIDE = -1;

   private int selectedButton = OUTSIDE;

   private final int R640x480 = 8101;
   private final int R800x600 = 8102;
   private final int R1024x768 = 8103;
   private final int R1600x1200 = 8104;


   /**
    * Constructor
    * @param w width of the drawing area
    * @param h height of the draw area
    */
   public OptionsMenu (int w, int h, Options o)
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

      g.setColor (options.getColor().getTextC());

      g.drawString ("Select Resolution", 50, 30);
      g.drawString ("Select Color", 300, 30);

      //System.out.println (options.getColor().getName());
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
               options.setColor (i);
               return SimpleButton.REPAINT;
            }
            else if (button[i].getAction() == R640x480)
            {

            }
            else if (button[i].getAction() == R800x600)
            {

            }
            else if (button[i].getAction() == R1024x768)
            {

            }
            else if (button[i].getAction() == R1600x1200)
            {

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
      int h = 30;
      int x = 300;
      int y = 50;
      Color c = options.getColor().getBoxC();
      Color sc = options.getColor().getSelectC();

      button[0] = new SimpleButton (w, h, "Black and White", x, y + 0, c, sc); 
      button[1] = new SimpleButton (w, h, "Guelph Gyphon", x, y += (h + 10), c, sc); 
      button[2] = new SimpleButton (w, h, "Midnight", x, y += (h + 10), c, sc); 
      button[3] = new SimpleButton (w, h, "Sea", x, y += (h + 10), c, sc); 
      button[4] = new SimpleButton (w, h, "Green", x, y += (h + 10), c, sc); 
      button[5] = new SimpleButton (w, h, "Groovy", x, y += (h + 10), c, sc); 
      button[6] = new SimpleButton (w, h, "Wild", x, y += (h + 10), c, sc); 
      button[7] = new SimpleButton (w, h, "Intense", x, y += (h + 10), c, sc); 
      button[8] = new SimpleButton (w, h, "Main Menu", x, y += (h + 10), SimpleButton.MAIN_MENU, c, sc); 
      button[9] = new SimpleButton (w, h, "Exit", x, y += (h + 10), SimpleButton.EXIT_PROGRAM, c, sc); 

      x = 50;
      y = 50;

      button[10] = new SimpleButton (w, h, "640x480", x, y, R640x480, c, sc);
      button[11] = new SimpleButton (w, h, "800x600", x, y += (h + 10), R800x600, c, sc);
      button[12] = new SimpleButton (w, h, "1024x768", x, y += (h + 10), R1024x768, c, sc);
      button[13] = new SimpleButton (w, h, "1600x1200", x, y += (h + 10), R1600x1200, c, sc);

   }

}

