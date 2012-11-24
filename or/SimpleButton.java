/*
 * Independent Study Project
 * MATH*4240 Advanced Topics in Modelling (Winter 2001)
 * Michael Bochenek [ID: 0041056] mboche01@uoguelph.ca
 */


import java.awt.Color;

/**
 * Class stores color information, text, location, size, etc.
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class SimpleButton 
{
   /** undefined action */
   public final static int UNDEFINED = 9000;

   /** load main manu action */
   public final static int MAIN_MENU = 9001;

   /** exit the program action */
   public final static int EXIT_PROGRAM = 9002;

   public final static int REPAINT = 9003;

   public final static int OUTSIDE = -1;

   public final static int RELOAD = 9011;

   /** width of button in pixels */
   private int width;

   /** height of button in pixels */
   private int height;

   /** text displayed by button */
   private String text;

   /** x offset of button in pixels */
   private int xLocation;

   /** y offset of button in pixels */
   private int yLocation;

   /** color of button and button text */
   private Color drawColor;

   /** color of button and button text when selected */
   private Color selectedDrawColor;

   /** weather button is selected or not */
   private boolean selected;

   /** value of action */
   private int action;


   /**
    * Constructor that defaults to action = UNDEFINED
    * @param w width in pixels
    * @param h height in pixels
    * @param s button text
    * @param x x offset in pixels
    * @param y y offset in pixels
    * @param c color of button and button text 
    * @param sc color of button and button text when selected 
    */
   public SimpleButton (int w, int h, String s, int x, int y, Color c, Color sc)
   {
      width = w;
      height = h;
      text = new String (s);
      xLocation = x;
      yLocation = y;
      action = UNDEFINED;
      drawColor = new Color (c.getRGB());
      selectedDrawColor = new Color (sc.getRGB());
      selected = false;
   }



   /**
    * Constructor
    * @param w width in pixels
    * @param h height in pixels
    * @param s button text
    * @param x x offset in pixels
    * @param y y offset in pixels
    * @param a action flag
    * @param c color of button and button text 
    * @param sc color of button and button text when selected 
    */
   public SimpleButton (int w, int h, String s, int x, int y, int a, Color c, Color sc)
   {
      width = w;
      height = h;
      text = new String (s);
      xLocation = x;
      yLocation = y;
      action = a;
      drawColor = new Color (c.getRGB());
      selectedDrawColor = new Color (sc.getRGB());
      selected = false;
   }



   public boolean isSelected ()
   {
      return selected;
   }


   public void select ()
   {
      selected = true;
   }


   public void deselect ()
   {
      selected = false;
   }


   public boolean isInside (int x, int y)
   {
      return (x > xLocation && x < (xLocation + width) && y > yLocation && y < (yLocation + height));
   }


   public void setText (String s)
   {
      text = new String (s);
   }


   public void setColor (Color c)
   {
      drawColor = new Color (c.getRGB());
   }


   public int getWidth ()
   {
      return width;
   }


   public int getHeight ()
   {
      return height;
   }


   public String getText ()
   {
      return text;
   }


   public int getX ()
   {
      return xLocation;
   }


   public int getY ()
   {
      return yLocation;
   }


   public Color getColor ()
   {
      return drawColor;
   }

   public Color getSelectedColor ()
   {
      return selectedDrawColor;
   }


   public int getAction ()
   {
      return action;
   }

}



