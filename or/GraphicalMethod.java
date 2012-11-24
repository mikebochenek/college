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
 * Class deals with drawing the graph in Graphical Method 
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class GraphicalMethod extends Canvas implements Screen 
{
   /** width of the drawing area */
   private int width;

   /** height of the drawing area */
   private int height;

   /** array that intially holds the constraints */
   private Constraint [] cList = new Constraint [Const.LARGE_ARRAY];

   /** number of constraints */
   private int cListSize;

   /** list of vertices */
   private Vertex [] vList = new Vertex [Const.LARGE_ARRAY];

   /** number of vertices */
   private int vListSize = 0;

   /** largest real X value */
   private double largestX;

   /** largest real Y value */
   private double largestY;

   /** used in transformation from realX to an integer point on the graph */
   private int xFactor;

   /** used in transformation from realY to an integer point on the graph */
   private int yFactor;

   /** offset where the graph actually starts (x value) */
   private int xAdjust;

   /** offset where the graph actually starts (y value) */ 
   private int yAdjust;

   /** Z-function to optimize */
   private ZFunction zfunc;

   /** all options */
   private Options options;

   /** user select point line */
   private SimpleLine sel;

   /** draw select line */
   private boolean selectLine = false;

   /** number of buttons */
   private final int NUMBER_OF_BUTTONS = 2;

   /** all the buttons used */
   private SimpleButton [] button = new SimpleButton [NUMBER_OF_BUTTONS];

   public static final int OUTSIDE = -1;
   private int selectedButton = OUTSIDE;

   private String status = "Click on the graph to evaluate the Z-Function";

   private String zfunctionString;

   private final int graphWidth = 400;
   private final int graphHeight = 400;
   private final int graphStartOffsetX = 30;
   private final int graphStartOffsetY = 30;
   private final int offset = 5;
   private final int bigOffset = 10;
   private final int boxHeight = 200;
   private final double xFillFactor = 1.2;
   private final double yFillFactor = 1.2;

   /**
    * Constructor
    * @param w width of the drawing area
    * @param h height of the drawing area
    */
   public GraphicalMethod (int w, int h, Options o)
   {
      zfunc = o.zfunc;
      cListSize = o.cListSize;

      cList = new Constraint [cListSize];

      for (int i = 0; i < cListSize; i++)
      {
         cList[i] = new Constraint (o.cList[i].getCoeffAt(0), o.cList[i].getCoeffAt(1), o.cList[i].getRelation(), o.cList[i].getConstant());
      }


      width = w;
      height = h;
      options = o;

      //double [] zCoeff = {3.0, 5.0};
      //zfunc = new ZFunction (zCoeff, 2, true);

      if (zfunc.getMaximize() == true)
      {
         zfunctionString = "Maximize z = ";
      }
      else
      {
         zfunctionString = "Minimize z = ";
      }

      zfunctionString += zfunc.getCoeffAt (0) + "x + " + zfunc.getCoeffAt (1) + "y";

      double xInt = cList[0].getXInt();
      double yInt = cList[0].getYInt();

      createButtons();

      createVertices();

      setupParameters();

   }


   /**
    * Invoked each time the redraw manager decides to repaint the screen
    * @param g this component
    */
   public void paint (Graphics g)
   {
      g.setColor (options.getColor().getBackgroundC());

      g.fillRect (0, 0, width, height);
      //g.fillRect (bigOffset, bigOffset, width - 2 * bigOffset, height - 2 * bigOffset);

      g.setColor (options.getColor().getBoxC());

      g.drawRect (graphStartOffsetX, graphStartOffsetY, graphWidth, graphHeight);
      g.drawRect (graphStartOffsetX + graphWidth + bigOffset, graphStartOffsetY, width - (graphStartOffsetX + graphWidth + bigOffset) - (graphStartOffsetX), boxHeight);

      g.drawLine (graphStartOffsetX + graphWidth / 2, graphStartOffsetY + offset, graphStartOffsetX + graphWidth / 2, graphStartOffsetY + graphHeight - offset);
      g.drawLine (graphStartOffsetX + offset, graphStartOffsetY + graphHeight / 2, graphStartOffsetX + graphHeight - offset, graphStartOffsetY + graphHeight / 2);


      int textX = graphStartOffsetX + graphWidth + bigOffset + bigOffset;
      int textY = graphStartOffsetY + bigOffset + offset;;

      g.setColor (options.getColor().getTextC());

      for (int i = 0; i < cListSize; i++)
      {
         g.drawString (cList[i].toString(), textX, textY + i * Const.LINE_SPACE);
      }

      g.drawString (status, textX, textY + 220);

      g.drawString (zfunctionString, textX, textY + 220 + Const.LINE_SPACE);


      // draw the freakin' lines!
      g.setColor (options.getColor().getOtherC());
      for (int i = 0; i < cListSize - 2; i++)
      {
         SimpleLine sl = getLine (cList[i]);
         g.drawLine (trX (sl.getV1().getX()), trY (sl.getV1().getY()), trX (sl.getV2().getX()), trY (sl.getV2().getY()));
      }


      // draw the polygon, three triangles at a time.
      g.setColor (options.getColor().getFillC());
      for (int i = 0; i < vListSize; i++)
      {
         Polygon polygon = new Polygon();

         polygon.addPoint (trX (vList[i%vListSize].getX()), trY (vList[i%vListSize].getY()));
         polygon.addPoint (trX (vList[(i+1)%vListSize].getX()), trY (vList[(i+1)%vListSize].getY()));
         polygon.addPoint (trX (vList[(i+2)%vListSize].getX()), trY (vList[(i+2)%vListSize].getY()));

         g.fillPolygon (polygon);
      }


      if (selectLine == true)
      {
         g.setColor (options.getColor().getSelectC());

         g.drawLine (trX (sel.getV1().getX()), trY (sel.getV1().getY()), trX (sel.getV2().getX()), trY (sel.getV2().getY()));
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

   }


   /**
    * Transform the x value.  Given a real X value, translate it to a 
    * value x that will be plotted on the actual screen.
    * @param realX the real x value
    * @return x value that will be plotted
    */
   private int trX (double realX)
   {
      return (int) (realX / largestX * xFactor + xAdjust);
   }


   /**
    * Transform the y value.  Given a real Y value, translate it to a
    * value y that will be plotted on the actual screen.
    * @param realY the real Y value
    * @return y value that will be plotted
    */
   private int trY (double realY)
   {
      return (int) (realY / (-1 * largestY) * yFactor + yAdjust);
   }


   /**
    * Transform the y value.  Given the y-plot value, translate it to
    * the original real y value, the inverse of trY().
    * @param plotY the y-plot value
    * @return real y value
    */
   private double trYInv (double plotY)
   {
      return ((plotY - yAdjust) / yFactor * -1 * largestY);
   }


   /**
    * Transform the x value.  Given the x-plot value, translate it to
    * the original real x value, the inverse of trX().
    * @param plotX the x-plot value
    * @return real x value
    */
   private double trXInv (double plotX)
   {
      return ((plotX - xAdjust) / xFactor * largestX);
   }



   public int checkAction (int x, int y)
   {
      double [] tmp = {trXInv (x), trYInv (y)};
      boolean found = false;

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


      for (int i = 0; i < vListSize; i++)
      {
         Vertex v = vList[i];
      

         int xLoc = trX (v.getX()); 
         int yLoc = trY (v.getY()); 

         int xDiff = Math.abs (xLoc - x);
         int yDiff = Math.abs (yLoc - y);

         if (xDiff < 8 && yDiff < 8)
         {
            double [] tmpI = {v.getX(), v.getY()}; 
            status = ("At VERTEX (" + v.getX() + "," + v.getY() + ") Z = " + zfunc.eval (tmpI));
            found = true;
         }
      } 

      if (found == false)
      {
         status = ("At point (" + MathUtil.specializedRound (trXInv(x), options) + "," + MathUtil.specializedRound (trYInv(y), options) + ") Z = " + MathUtil.specializedRound (zfunc.eval (tmp), options));
      }

      double slope = -1 * zfunc.getCoeffAt (1) / zfunc.getCoeffAt (0);

      Constraint tmpC = new Constraint (zfunc.getCoeffAt (0), zfunc.getCoeffAt (1), Constraint.EQUAL_TO, zfunc.eval (tmp));

      sel = getLine (tmpC);
      selectLine = true;


      return SimpleButton.REPAINT;
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



   private void createVertices ()
   {
      vListSize = 0;

      for (int i = 0; i < cListSize; i++)
      {
         for (int j = 0; j < cListSize; j++)
         {
            Intersection inter = new Intersection (cList[i], cList[j]);
            double x = inter.getXInt();
            double y = inter.getYInt();


            if (isValid(x, y))
            {
               x = MathUtil.specializedRound (x, options);
               y = MathUtil.specializedRound (y, options);


               boolean found = false;
               for (int m = 0; m < vListSize; m++)
               {
                  if (MathUtil.closeEnough (vList[m].getX(), x, options) && MathUtil.closeEnough (vList[m].getY(), y, options))
                  {
                     found = true;
                  }
               }
   
               if (found == false)
               {
                  int trueForAll = 0;
                  for (int k = 0; k < cListSize; k++)
                  {
                     Constraint c = cList[k];

                     int test = 0;

                     double xR = trXInv (1 + graphStartOffsetX);
                     double yR = trYInv (1 + graphStartOffsetY);

                     test += testPoint (c, x, y); 
                     test += testPoint (c, x + xR, y + yR); 
                     test += testPoint (c, x + xR, y - yR); 
                     test += testPoint (c, x - xR, y + yR); 
                     test += testPoint (c, x - xR, y - yR); 

                     if (test > 0)
                     {
                        trueForAll++;
                     }
                  }

                  if (trueForAll == cListSize)
                  {
                     vList[vListSize] = new Vertex (x, y);
                     vListSize++;
                  }
               }
            }
         }
      }
   }



   private void setupParameters ()
   {
      largestX = 0.0;
      largestY = 0.0;
      for (int i = 0; i < vListSize; i++)
      {
         if (largestX < Math.abs (vList[i].getX()))
         {
            largestX = Math.abs (vList[i].getX());
         }

         if (largestY < Math.abs (vList[i].getY()))
         {
            largestY = Math.abs (vList[i].getY());
         }
      }

      largestX *= xFillFactor;
      largestY *= yFillFactor;

      largestX = Math.max (largestX, largestY);
      largestY = Math.max (largestX, largestY);

      xFactor = 195;
      yFactor = 195;
      xAdjust = xFactor + 35;
      yAdjust = 1 * (yFactor + 35);
   }



   private int testPoint (Constraint c, double x, double y)
   {
      if (c.testPoint (x, y) == true)
      {
         return 1;
      }
      else
      {
         return 0;
      }
   }



   private SimpleLine getLine (Constraint c)
   {
      Intersection i;

      Vertex v1 = new Vertex();
      Vertex v2 = new Vertex();
      Constraint bounds [] = new Constraint [4];

      bounds[0] = new Constraint (0, 1, Constraint.EQUAL_TO, largestY);
      bounds[1] = new Constraint (0, 1, Constraint.EQUAL_TO, -1 * largestY);
      bounds[2] = new Constraint (1, 0, Constraint.EQUAL_TO, largestX);
      bounds[3] = new Constraint (1, 0, Constraint.EQUAL_TO, -1 * largestX);

      for (int j = 0; j < 4; j++)
      {

         i = new Intersection (c, bounds[j]);

         if (isValid (i.getXInt(), i.getYInt()))
         {
            boolean validSoln = true;
            if (MathUtil.closeEnough (i.getXInt(), largestX, options) || MathUtil.closeEnough (i.getXInt(), -1 * largestX, options))
            {
               if (i.getYInt() > largestY || i.getYInt() < (-1 * largestY))
               {
                  validSoln = false;
               }
            }

            if (MathUtil.closeEnough (i.getYInt(), largestY, options) || MathUtil.closeEnough (i.getYInt(), -1 * largestY, options))
            {
               if (i.getXInt() > largestX || i.getXInt() < (-1 * largestX))
               {
                  validSoln = false;
               }
            }

            if (validSoln)
            {
               if (MathUtil.closeEnough (v1.getX(), 0, options) && MathUtil.closeEnough (v1.getY(), 0, options))
               {
                  v1 = new Vertex (i.getXInt(), i.getYInt());
               }
               else if (MathUtil.closeEnough (v2.getX(), 0, options) && MathUtil.closeEnough (v2.getY(), 0, options))
               {
                  v2 = new Vertex (i.getXInt(), i.getYInt());
               }
               else
               {
                  System.out.println ("Error in calculating intersecion - getLine()");
               }
            }
         }
      }

      return new SimpleLine (v1, v2);
   }


   private boolean isValid (double x, double y)
   {
      if (Double.isNaN (x) || Double.isNaN (y) || Double.isInfinite(x) || Double.isInfinite(y))
      {
         return false;
      }
      else if (x > 1000 || x < -1000 || y > 1000 || y < -1000)
      {
         return false;
      }
      else
      {
         return true;
      }
   }


   private void createButtons ()
   {
      int w = 200;
      int h = 40;
      int x = (width - w) / 2;
      int y = 400;
      Color c = options.getColor().getBoxC();
      Color sc = options.getColor().getSelectC();

      button[0] = new SimpleButton (w, h, "Main Menu", x, y += (h + 10), SimpleButton.MAIN_MENU, c, sc); 
      button[1] = new SimpleButton (w, h, "Exit", x, y += (h + 10), SimpleButton.EXIT_PROGRAM, c, sc); 
   }




}


