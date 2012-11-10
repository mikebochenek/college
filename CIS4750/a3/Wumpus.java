/* CIS*4750 Artificial Intelligence
   Assignment #3 (6.13), Due: Friday October 20, 2000
   Michael Bochenek ID: 0041056 mboche01@uoguelph.ca */

import java.awt.Graphics;
import java.awt.Color;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.Random;
import java.net.*;


public class Wumpus extends Applet implements MouseListener
{
   private static final int NOTHING = 0;
   private static final int WUMPUS = 1;
   private static final int AGENT = 2;
   private static final int PIT = 3;
   private static final int GOLD = 4;

   private static final int MAP_SIZE = 4;


   private PaintGameField p = new PaintGameField();
   private Button generateButton = new Button ("Generate New Map");
   private Button startButton = new Button ("Shoot Arrow");
   private Label label = new Label ();

   private GridBagLayout layout;
   private GridBagConstraints constraints;

   private Agent agent = new Agent();

   public void start ()
   {
      label.setBackground (Color.white);

      addMouseListener (this);

      generateButton.addActionListener ( new ActionListener ()
         {
            public void actionPerformed (ActionEvent e)
            {
               p.generateRandomMap ();
               agent = new Agent();
               String returnVal = p.updateAgent (agent.agentX, agent.agentY);
               label.setText (returnVal);
               p.score = 0;
               p.death = false;
               p.wumpus_killed = false;

               p.arrow = true;
               p.gold_found = false;

               repaint();
            }
         }
      ) ; 


      startButton.addActionListener ( new ActionListener ()
         {
            public void actionPerformed (ActionEvent e)
            {

               String returnValue = p.fireArrow();
               label.setText (returnValue);

            }
         }
      ) ; 
      
      layout = new GridBagLayout();
      this.setLayout (layout);

      constraints = new GridBagConstraints();

      constraints.anchor = GridBagConstraints.NORTH;
      constraints.weighty = 0;

      layout.setConstraints ( p, constraints );
      this.add (p);
 
      constraints.anchor = GridBagConstraints.SOUTHEAST;
      constraints.insets = new Insets (10, 10, 10, 10);
      constraints.fill = GridBagConstraints.NONE;
      constraints.weighty = 1;

      layout.setConstraints ( startButton, constraints );
      this.add (startButton);

      layout.setConstraints ( generateButton, constraints );
      this.add (generateButton);


      constraints.anchor = GridBagConstraints.SOUTH;
      constraints.fill = GridBagConstraints.HORIZONTAL;
      constraints.weighty = 3;
      constraints.weightx = 10;

      layout.setConstraints ( label, constraints );
      this.add (label);
  }



   public void paint (Graphics g)
   {

      g.setColor (Color.white);
      g.fillRect (0, 0, 760, 560);


      p.paint(g);
   }

   public void mouseClicked (MouseEvent e) 
   {
      int real_i=0, real_j=0;
      for (int i = 0; i < MAP_SIZE; i++)
      {
         for (int j = 0; j < MAP_SIZE; j++)
         {
            //g.drawString ( things[thing], 35+(i*100), 35+(j*100) );
            if ((e.getX() < 35+(i+1)*100) && (e.getX() > 35+(i)*100) && (e.getY() < 35+(j+1)*100) && (e.getY() > 35+(j)*100)) 
            {
               real_i = i;
               real_j = j;
               break;
            }
         }
      }

      String returnVal = p.updateAgent (real_i, real_j);
      label.setText (returnVal);
      repaint();
   }
   
   public void mousePressed (MouseEvent e) {}
   public void mouseEntered (MouseEvent e) {}
   public void mouseExited (MouseEvent e) {}
   public void mouseReleased (MouseEvent e) {}

}

