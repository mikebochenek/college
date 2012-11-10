/* --------------------------------------------------------------------
Michael Bochenek
980-492-820
27-160 Foundations of Programming
Assignment #4:  Drawing Applet
-----------------------------------------------------------------------
*/

import ccj.*;

public class Draw extends GraphicsApplet
{  public void run()
   {  initWindow(); // call procedure to initialize drawing area
      while(mode != EXIT) // infinite loop until user presses exit
      {  Point p1 = readMouse(""); 
         int newMode = activateButton(p1); //check if 1of7 buttons is clicked
         if (newMode >= 0)
         {  mode = newMode; // if button is clicked, update 'mode'
            if (mode == CLEAR)
            {  clearWindow(); // clear the window and
               initWindow();  // re-initialize the drawing area
            }
            continue;
         }
         else if (mode == POINT) p1.draw(); // draw a point
         else if (mode == LINE) drawLine(p1); // draw a straight line
         else if (mode == CIRCLE) drawCircle(p1); // draw a circle
         else if (mode == TRIANGLE) drawTriangle(p1); // draw a triangle
         else if (mode == RECTANGLE) drawRectangle(p1); // draw a rectangle
      }
   }

   // *** Procedure that draws a line, taking the first point as an argument
   public void drawLine(Point p1)
   {  Point p2 = readMouse(""); // get the second point
      int newMode = activateButton(p2); // check if 1of7 buttons is clicked
      if (newMode >= 0) 
      {  mode = newMode; // if button is clicked, update 'mode'
         return; // exit procedure before drawing line
      }
      new Line(p1, p2).draw(); // draw line
   }

   // *** Procedure that draws a circle, taking centre point as argument
   public void drawCircle(Point p1)
   {  Point p2 = readMouse(""); // get the second point
      int newMode = activateButton(p2); // check if 1of7 buttons is clicked
      if (newMode >= 0)
      {  mode = newMode; // if button is clicked, update 'mode'
         return; // exit procedure before drawing circle
      }
      double radius = (p1.getX() - p2.getX()) * (p1.getX() - p2.getX())
                    + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY());
      radius = Math.sqrt(radius);
      // calculate radius, by finding the distance between the two points
      new Circle(p1, radius).draw(); // draw the circle
   }

   // *** Procedure that draws a triangle, taking a point as argument
   public void drawTriangle(Point p1)
   {  Point p2 = readMouse(""); // get the second point
      int newMode = activateButton(p2); // check if 1of7 buttons is clicked
      if (newMode >= 0)
      {  mode = newMode; // if button is clicked, update 'mode'
         return; // exit procedure before drawing triangle
      }
      Point p3 = readMouse(""); // get the third point
      newMode = activateButton(p3); // check if 1of7 buttons is clicked
      if (newMode >= 0)
      {  mode = newMode; // if button is clicked, update 'mode'
         return; // exit procedure before drawing triangle
      }
      new Triangle(p1, p2, p3).draw(); // draw the triangle
   }
   // *** Procedure that draws a rectangle, taking a point as argument
   public void drawRectangle(Point p1)
   {  Point p2 = readMouse(""); // get the second (opposite) point
      int newMode = activateButton(p2); // check if 1of7 buttons is clicked
      if (newMode >= 0)
      {  mode = newMode; // if button is clicked, update 'mode'
         return; // exit procedure before drawing rectagle
      }
      new Rectangle(p1, p2).draw(); // draw the rectangle
   }

   // *** Function that checks if a button has been clicked
   // returns the index of the button that has been clicked
   // and returns -1 if a button has not been clicked
   // takes a point as a argument (where moose has been clicked)
   public int activateButton(Point clicked)
   {  for (int x = 0; x < NUMBER_OF_BUTTONS; x++) // loop through 7 buttons
      {  if (button[x].containsPoint(clicked)) return x;
         // if point is inside the rectangle (button) return index of button
      }
      return -1; // if point is not inside any buttons, return -1
   }

   // *** Procedure that changes coordinate system, draws buttons,
   // rectangular border, author name and student number
   public void initWindow()
   {  setCoord(0, 0, MAX, MAX); // coordinate system
      new Rectangle(new Point(0,MAX), new Point(MAX,0)).draw();
      // draw a rectengular border around drawing area
      new Message(new Point(AUTHOR_X_POSITION, AUTHOR_Y_POSITION),
                  "By: Michael Bochenek, #980 492 820").draw();
      // print author name and student number
      drawButtons(); // call procedure to draw the buttons
   }

   // *** Procedure to draw the buttons, and appropriate labels
   public void drawButtons()
   {  String[] buttonName = { "RECTANGLE", "TRIANGLE", "CIRCLE",
                              "LINE", "POINT", "ERASE", "EXIT" };
      // define the labels
      for (int x = 0; x < NUMBER_OF_BUTTONS; x++) // loop through buttons
      {  button[x] = new Rectangle
           (new Point(OFFSET,
                      OFFSET + x * BUTTON_HEIGHT),
            new Point(OFFSET + BUTTON_WIDTH,
                      (x + 1) * BUTTON_HEIGHT - OFFSET));
         // create a rectangle object
         button[x].draw(); // draw the created rectangle
         new Message(new Point(OFFSET + OFFSET, OFFSET + x * BUTTON_HEIGHT),
                     buttonName[x]).draw(); // print label inside button
      }
   }

   // ****** GENERAL CONSTANTS ******
   public static final int OFFSET = 5;
   public static final int BUTTON_WIDTH = 100;
   public static final int BUTTON_HEIGHT = 30;
   public static final int MAX = 500;
   public static final int AUTHOR_X_POSITION = 150;
   public static final int AUTHOR_Y_POSITION = 475;
   public static final int NUMBER_OF_BUTTONS = 7;

   // ****** DRAWING MODES ******
   public static final int RECTANGLE = 0;
   public static final int TRIANGLE = 1;
   public static final int CIRCLE = 2;
   public static final int LINE = 3;
   public static final int POINT = 4;
   public static final int CLEAR = 5;
   public static final int EXIT = 6;

   public Rectangle[] button = new Rectangle[NUMBER_OF_BUTTONS];
   // global array of type rectangle for the buttons
   public int mode = 4;
   // global variable for the drawing mode, init to 4 --> point
}

// -------------------------- TRIANGLE CLASS ---------------------------
class Triangle
{  // data declrations
   private Point point1, point2, point3;

   // constructor, note: default constructor not required
   public Triangle(Point p1, Point p2, Point p3)
   {  point1 = p1;
      point2 = p2;
      point3 = p3;
   }

   // method to draw the triangle (i.e. joint the points in special order)
   public void draw()
   {  new Line(point1, point2).draw();
      new Line(point2, point3).draw();
      new Line(point3, point1).draw();
   }
}

// --------------------------- RECTANGLE CLASS ------------------------
class Rectangle
{  // data declarations
   private Point topLeft, topRight, bottomRight, bottomLeft;
   private double xmin, xmax, ymin, ymax;

   // constructor, note: default constructor not required
   public Rectangle(Point p1, Point p2)
   {  xmin = p1.getX() < p2.getX() ? p1.getX() : p2.getX();
      xmax = p1.getX() > p2.getX() ? p1.getX() : p2.getX();
      ymin = p1.getY() < p2.getY() ? p1.getY() : p2.getY();
      ymax = p1.getY() > p2.getY() ? p1.getY() : p2.getY();
      topLeft     = new Point(xmin, ymax);
      topRight    = new Point(xmax, ymax);
      bottomRight = new Point(xmax, ymin);
      bottomLeft  = new Point(xmin, ymin);
   }

   // method to draw the rectangle
   public void draw()
   {  new Line(topLeft, topRight).draw();
      new Line(topRight, bottomRight).draw();
      new Line(bottomRight, bottomLeft).draw();
      new Line(bottomLeft, topLeft).draw();
   }

   // method to check if a point (argument) is inside the rectangle
   public boolean containsPoint(Point p)
   {  return p.getX() >= topLeft.getX()  &&
             p.getX() <= topRight.getX() &&
             p.getY() <= topLeft.getY()  &&
             p.getY() >= bottomLeft.getY();
   }
}
