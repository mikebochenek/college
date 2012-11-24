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
public class HelpMenu extends Canvas implements Screen 
{
   /** width of the drawing area */
   private int width;

   /** height of the drawign area */
   private int height;

   private final int NUMBER_OF_BUTTONS = 2;

   /** all the buttons used */
   private SimpleButton [] button = new SimpleButton [NUMBER_OF_BUTTONS];

   private Options options;


   public static final int OUTSIDE = -1;

   private int selectedButton = OUTSIDE;


   /**
    * Constructor
    * @param w width of the drawing area
    * @param h height of the draw area
    */
   public HelpMenu (int w, int h, Options o)
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

      g.drawString ("For help, please refer to the online manual", Const.GRAPH_START_OFFSET_X, Const.GRAPH_START_OFFSET_Y + Const.LINE_SPACE);
      g.drawString ("(Click 'BACK' on your browser and the select 'Online Help')", Const.GRAPH_START_OFFSET_X, Const.GRAPH_START_OFFSET_Y + 2 * Const.LINE_SPACE);
      g.drawString ("For complicated questions, suggestions, or bug reports,", Const.GRAPH_START_OFFSET_X, Const.GRAPH_START_OFFSET_Y + 3 * Const.LINE_SPACE);
      g.drawString ("contact the author of this program:", Const.GRAPH_START_OFFSET_X, Const.GRAPH_START_OFFSET_Y + 4 * Const.LINE_SPACE);
      g.drawString ("Michael Bochenek at mboche01@uoguelph.ca", Const.GRAPH_START_OFFSET_X, Const.GRAPH_START_OFFSET_Y + 5 * Const.LINE_SPACE);
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
      int y = 200;
      Color c = options.getColor().getBoxC();
      Color sc = options.getColor().getSelectC();

      button[0] = new SimpleButton (w, h, "Main Menu", x, y += (h + 10), SimpleButton.MAIN_MENU, c, sc); 
      button[1] = new SimpleButton (w, h, "Exit", x, y += (h + 10), SimpleButton.EXIT_PROGRAM, c, sc); 
   }

}

