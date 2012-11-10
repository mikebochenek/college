import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;

/**
 * Class that ocntrols main playing field.
 */
public class DrawArea extends Canvas implements MouseListener
{
   private static Image logo;
   public boolean paintEnabled = true;
   private Timer clock;
   private Option options;

   private static Unit unit;
   private Vector unitVector;

   /**
    * Constructor that takes pointers to all units and the timer object
    */
   public DrawArea (Option op, Vector unitIn, Timer cl) 
   {
      this.setSize ( Const.drawAreaWidth, Const.drawAreaHeight );
      //logo = Toolkit.getDefaultToolkit().getImage ( "moon.jpg" );
      setBackground (new Color ( (float) 0, (float) 0.5, (float) 0));
      unitVector = unitIn;
      options = op;
      clock = cl;
      addMouseListener (this);
   }


   /**
    * utility function used for debugging
    */
   public void setU (Vector u)
   {
      unitVector = u;
   }


   /**
    * Called only if options.display is true.  It repaints the gamefield.
    */
   public void paint (Graphics g)
   {
      if (paintEnabled)  
      { 
         //g.drawImage (logo, 0, 0, this);

         for (int i = 0; i < unitVector.size(); i++)
         {
            unit = (Unit) unitVector.elementAt (i); 
            unit.paint (g, this);      
         }
 

         if (options.gridlines == true)
         {
            for (int i = 0; i < Const.numOfRows + 1; i++)
            {
               for (int j = 0; j < Const.numOfColumns + 1; j++)
               {
                  g.drawLine (j * (Const.drawAreaWidth / Const.numOfColumns), 0, j * (Const.drawAreaWidth / Const.numOfColumns), Const.drawAreaHeight);
                  g.drawLine (0, i * (Const.drawAreaHeight / Const.numOfRows), Const.drawAreaWidth, i * (Const.drawAreaHeight / Const.numOfRows));
               }
            }
         }

         //System.out.println ("redrawing drawarea canvas.");
      } 
   }


   /**
    * This function is called everytime the user clicks the mouse anywhere
    * on the canvas.
    */
   public void mouseClicked (MouseEvent e)
   {
      boolean clickedUnit = false;
      int x = e.getX();
      int y = e.getY();
      x /= (Const.drawAreaWidth / Const.numOfColumns);
      y /= (Const.drawAreaHeight / Const.numOfRows);

      for (int i = 0; i < unitVector.size(); i++)
      {
         unit = (Unit) unitVector.elementAt (i); 
         if (unit.getRow() == y && unit.getColumn() == x)
         {
            clickedUnit = true;
            if (clock.isRunning() == true)
            {
               clock.stop();
            }

            String s = unit.getInfo();
            JOptionPane msg = new JOptionPane();
            msg.showMessageDialog (this, s, "Unit Information", JOptionPane.INFORMATION_MESSAGE);
         }
      }

      if (clickedUnit == false)
      {
         if (clock.isRunning() == true)
         {
            clock.stop();
         }
         else
         {
            clock.restart();
         }
      }
 
   }

   public void mouseEntered (MouseEvent e) {}
   public void mouseExited (MouseEvent e) {}
   public void mousePressed (MouseEvent e) {}
   public void mouseReleased (MouseEvent e) {}

}
