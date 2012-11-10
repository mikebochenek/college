import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Random;
import javax.swing.*;

/**
 * Main 'executable' class.  For now it does not contain much.
 */
public class Main extends JFrame implements ActionListener, WindowListener
{
   private JFrame frame;
   private Vector unitVector = new Vector();
   private Unit unit;
   private DrawArea da;
   private Timer clock;
   private Random random = new Random();
   private Option option;
   private boolean redFirst = true;
   private int epoch = 0;
   int dir = Const.EAST;


   /**
    * Constructor that invokes pretty much everything!
    */
   public Main (Option options)
   {
      option = options;
      clock = new Timer (options.speed, this);

      int tempRow [] = new int [options.redUnits + options.blueUnits];
      int tempColumn [] = new int [options.redUnits + options.blueUnits];
      int unitID = 0;
      int row, column;
      for (int i = 0; i < options.redUnits; i++)
      {
         boolean emptySpot = true;
         do
         {
            emptySpot = true; 
            row = random.nextInt (Const.numOfRows);
            column =  random.nextInt (Const.numOfColumns);
            for (int x = 0; x < (unitID-1); x++)
            {
               if (tempRow[x] == row && tempColumn[x] == column)    
               {
                  emptySpot = false;
               }
            }
         } while (emptySpot == false);

         unit = new Unit (unitID, Const.REDUNIT, options.redAI);
         unit.setLocation (row, column);
         tempRow [unitID] = row;
         tempColumn [unitID] = column;

         unitVector.add (unit);
         unitID++;
      }
      for (int i = 0; i < options.blueUnits; i++)
      {
         boolean emptySpot = true;
         do
         {
            emptySpot = true; 
            row = random.nextInt (Const.numOfRows);
            column =  random.nextInt (Const.numOfColumns);
            for (int x = 0; x < (unitID-1); x++)
            {
               if (tempRow[x] == row && tempColumn[x] == column)    
               {
                  emptySpot = false;
               }
            }
         } while (emptySpot == false);

         unit = new Unit (unitID, Const.BLUEUNIT, options.blueAI);
         unit.setLocation (row, column);
         tempRow [unitID] = row;
         tempColumn [unitID] = column;

         unitVector.add (unit);
         unitID++;
      }

      clock.start();
      da = new DrawArea(options, unitVector, clock);      
      getContentPane().add (da);
      
      setTitle ("Main screen.");
      setLocation ( 100, 100 );
      setSize ( Const.drawAreaWidth, Const.drawAreaHeight);
      setVisible (true);      
      
      //debugPrint();
   }


   /**
    * Action Listener that checks for timer events
    */
   public void actionPerformed ( ActionEvent evt )	
   {
      Object source = evt.getSource();
      String pressed = evt.getActionCommand ();

      if (evt.getSource() == clock)
      {
         epoch++;
         int rand = -1;

         if (redFirst)
         {
            redFirst = false;
            for (int i = 0; i < unitVector.size(); i++)
            {
               rand = random.nextInt (1000000);
               unit = (Unit) unitVector.elementAt (i);
               if (unit.alive == true)
               {
                  unit.go(rand, unit.getID(), option, unitVector);
               }
               else
               {
                  System.out.println ("Error: This unit is dead!");
               }
            }
         }
         else
         {
            redFirst = true;
            for (int i = unitVector.size() - 1; i > 0; i--)
            {
               rand = random.nextInt (1000);
               unit = (Unit) unitVector.elementAt (i);
               if (unit.alive == true)
               {
                  unit.go(rand, unit.getID(), option, unitVector);
               }
               else
               {
                  System.out.println ("Error: This unit is dead!");
               }
            }
         }

         for (int i = 0; i < unitVector.size(); i++)
         {
            unit = (Unit) unitVector.elementAt (i);
            if (unit.alive == true)
            {
               unit.checkContact(unit.getID(), option, unitVector);
            }
            else
            {
               System.out.println ("This unit is dead!");
            }
         }

         for (int i = 0; i < unitVector.size(); i++)
         {
            unit = (Unit) unitVector.elementAt (i);
            unit.evaluateUtility (unitVector);
         }

         int redUtil = 0;
         int blueUtil = 0;
         int redCount = 0;
         int blueCount = 0;
         for (int i = 0; i < unitVector.size(); i++)
         {
            unit = (Unit) unitVector.elementAt (i);
            if (unit.getUnitType() == Const.REDUNIT)
            {
               redCount++;
               redUtil += unit.getTotalUtility();
            }
            else if (unit.getUnitType() == Const.BLUEUNIT)
            {
               blueCount++;
               blueUtil += unit.getTotalUtility();
            }
            else
            {
               System.out.println ("Error: invalid unit type: " + unit.getUnitType());
            }
         }

         System.out.println ("Epoch = " + epoch);
         System.out.print ("Number of Lizards = " + redCount);
         System.out.println ("   Total Utility of Lizards = " + redUtil);
         System.out.print ("Number of Dinosaurs = " + blueCount);
         System.out.println ("   Total Utility of Dinosaurs = " + blueUtil);


         if (option.display == true)
         {
            da.repaint();
         }
      }
   }

   public void windowOpened (WindowEvent event) {}
   public void windowClosed (WindowEvent event) { System.exit(0); }
   public void windowClosing (WindowEvent event) { System.exit(0); }
   public void windowIconified (WindowEvent event) {}
   public void windowDeiconified (WindowEvent event) {}
   public void windowActivated (WindowEvent event) {}
   public void windowDeactivated (WindowEvent event) {}


   /**
    * Used to print user specified options.
    */
   private void debugPrint ()
   {
      System.out.println ("----------------------");
      System.out.println ("firepower=" + option.firepower); 
      System.out.println ("speed=" + option.speed); 
      System.out.println ("redUnits=" + option.redUnits); 
      System.out.println ("redRandom=" + option.redRandom); 
      System.out.println ("blueUnits=" + option.blueUnits); 
      System.out.println ("blueRandom=" + option.blueRandom); 
      System.out.println ("redAI=" + option.redAI); 
      System.out.println ("blueAI=" + option.blueAI); 
      System.out.println ("blueFun=" + option.blueFun); 
      System.out.println ("redFun=" + option.redFun); 
      System.out.println ("blueVisibility=" + option.blueVisibility); 
      System.out.println ("redVisibility=" + option.redVisibility); 
      System.out.println ("blueCom=" + option.blueCom); 
      System.out.println ("redCom=" + option.redCom); 
      System.out.println ("----------------------");
   }

}


