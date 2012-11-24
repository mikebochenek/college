/*
 * Independent Study Project
 * MATH*4240 Advanced Topics in Modelling (Winter 2001)
 * Michael Bochenek [ID: 0041056] mboche01@uoguelph.ca
 */

import java.awt.*;
import java.awt.event.*;
import java.applet.*;


/**
 * This is the applet class for the graphical solving method
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class Main extends Applet implements MouseListener, MouseMotionListener, KeyListener
{
   /** this is the canvas that draws the main menu */
   //private MainMenu main;

   /** this is the canvas taht draws the options menu */
   //private OptionsMenu options;

   /** layout manager */
   private GridBagLayout layout;

   /** constraints that will be applied to the components */
   private GridBagConstraints constraints;

   /** array of canvases to be used/processed */
   private Screen [] screenList = new Screen [7];

   /** indicate what is the current canvas used */
   private Screen screen;

   /** all the options */
   private Options options;


   /**
    * This method is called when the applet is first started.
    * (Create actionListeners and place all object on the applet 
    */
   public void start ()
   {
      addMouseListener (this);
      addMouseMotionListener (this);

      options = new Options();
      options.setColor (7);

      screenList[0] = new UserProblem (options.getWidth(), options.getHeight(), options);
      screenList[1] = new Library (options.getWidth(), options.getHeight(), options);
      screenList[2] = new GraphicalMethod (options.getWidth(), options.getHeight(), options);
      screenList[3] = new SimplexTableau (options.getWidth(), options.getHeight(), options);
      screenList[4] = new OptionsMenu (options.getWidth(), options.getHeight(), options);
      screenList[5] = new HelpMenu (options.getWidth(), options.getHeight(), options);
      screenList[6] = new MainMenu (options.getWidth(), options.getHeight(), options);

      screen = screenList[6];

      layout = new GridBagLayout();
      this.setLayout (layout);

      constraints = new GridBagConstraints();

      constraints.anchor = GridBagConstraints.NORTH;
      constraints.weighty = 0;

      layout.setConstraints ((MainMenu)screen, constraints);
      this.add ((MainMenu)screen);

      this.requestFocus();
      addKeyListener (this);
 
   }


   private void handleCommonAction (int actionNum)
   {
      switch (actionNum)
      {
         case SimpleButton.MAIN_MENU:
            screen = screenList[6];
            repaint();
            break;
         case SimpleButton.UNDEFINED:
            break;
         case SimpleButton.OUTSIDE:
            break;
         case SimpleButton.EXIT_PROGRAM:
            destroy();
            break;
         case SimpleButton.REPAINT:
            repaint();
            break;
         case 2:
         case 3:
            screenList[0] = new UserProblem (options.getWidth(), options.getHeight(), options);
            screenList[2] = new GraphicalMethod (options.getWidth(), options.getHeight(), options);
            screenList[3] = new SimplexTableau (options.getWidth(), options.getHeight(), options);
            screen = screenList [actionNum];
            repaint();
            break;
         case SimpleButton.RELOAD:
            screenList[0] = new UserProblem (options.getWidth(), options.getHeight(), options);
            screenList[2] = new GraphicalMethod (options.getWidth(), options.getHeight(), options);
            screenList[3] = new SimplexTableau (options.getWidth(), options.getHeight(), options);
            screen = screenList[6];
            break;
         default:
            screen = screenList [actionNum];
            repaint();
            break;
      }
   }


   /**
    * Invoked each time the redraw manager decides to repaint the screen
    * @param g this component
    */
   public void paint (Graphics g)
   {
      screen.paint(g);
   }


   public void mouseMoved (MouseEvent e)
   {
      if (screen.mouseMoveChanged (e.getX(), e.getY())) 
      {
         repaint();
      }
   }

   public void mouseDragged (MouseEvent e)
   {

   }

   /**
    * Invoked when the mouse has been clicked on this component
    * @param e contains mouse event data
    */
   public void mouseClicked (MouseEvent e) 
   {
      int clickedButton = screen.checkAction (e.getX(), e.getY());

      handleCommonAction (clickedButton);

   }
   

   /**
    * Invoked when the mouse button has been pressed on a component
    * @param e contains mouse event data
    */
   public void mousePressed (MouseEvent e) 
   {

   }
   

   /**
    * Invoked when the mouse enters a component
    * @param e contains mouse event data
    */ 
   public void mouseEntered (MouseEvent e) 
   {
   }
   

   /**
    * Invoked when a mouse exits a component
    * @param e contains mouse event data
    */
   public void mouseExited (MouseEvent e) 
   {

   }
   

   /**
    * Invoked when a mouse button is released
    * @param e contains mouse event data
    */
   public void mouseReleased (MouseEvent e) 
   {

   }


   public void keyPressed (KeyEvent e)
   {
      int code = e.getKeyCode();
      char c = e.getKeyChar();

      if (screen == screenList[0])
      {
         int retVal = UserProblem.DO_NOT_REPAINT;

         if (code == KeyEvent.VK_LEFT) 
         {
            retVal = ((UserProblem) screen).processMove(code);
         }
         else if (code == KeyEvent.VK_RIGHT) 
         {
            retVal = ((UserProblem) screen).processMove(code);
         }
         else if (code == KeyEvent.VK_UP) 
         {
            retVal = ((UserProblem) screen).processMove(code);
         }
         else if (code == KeyEvent.VK_DOWN) 
         {
            retVal = ((UserProblem) screen).processMove(code);
         }
         else if (code == KeyEvent.VK_TAB) 
         {
            retVal = ((UserProblem) screen).processMove(code);
         }
         else if (code == KeyEvent.VK_ENTER) 
         {
            retVal = ((UserProblem) screen).processMove(code);
         }
         else if (code == KeyEvent.VK_BACK_SPACE)
         {
            retVal = ((UserProblem) screen).processMove(code);
         }

         if (retVal == UserProblem.REPAINT)
         {
            repaint();
         }
      }

   }


   public void keyReleased (KeyEvent e)
   {
   }


   public void keyTyped (KeyEvent e)
   {
      int code = e.getKeyCode();
      char c = e.getKeyChar();

      if (screen == screenList[0])
      {
         int retVal = UserProblem.DO_NOT_REPAINT;

         if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9')
         {
            retVal = ((UserProblem) screen).processKey (c);
         }
         else if (c == 'M' || c == 'm')
         {
            retVal = ((UserProblem) screen).processKey (c);
         }
         else if (c == '<' || c == '>' || c == '=')
         {
            retVal = ((UserProblem) screen).processKey (c);
         }
         else if (c == '+' || c == '-')
         {
            retVal = ((UserProblem) screen).processKey (c);
         }

         if (retVal == UserProblem.REPAINT)
         {
            repaint();
         }
      }


   }

}



