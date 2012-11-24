/*
 * Independent Study Project
 * MATH*4240 Advanced Topics in Modelling (Winter 2001)
 * Michael Bochenek [ID: 0041056] mboche01@uoguelph.ca
 */

import java.awt.Color;

/**
 * Contains all options that can be changed. 
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class Options 
{
   public final static int NETSCAPE = 1001;
   public final static int WINDOWS = 1002;

   public ComplexConstraint [] cList;
   public int cListSize;
   public ZFunction zfunc;
   public SimpleConstraint [] sList;
   public int sListSize;

   private String name;
   private ColorSet color;
   private int width = 760;
   private int height = 460;
   private int browser = NETSCAPE;

   private int sigDigits;

   private String [] names = {"Black and White", "Guelph Gryphon", "Midnight", "Sea", "Green", "Groovy", "Sexy", "Intense"};

   private final int NUMBER_OF_COLOR_SETS = 8;


   /**
    * Constructor
    */
   public Options ()
   {
      name = new String ("Default");
      width = 760;
      height = 560;
      browser = NETSCAPE;
      sigDigits = 2;

      double [] zCoeff = {3.0, 5.0};
      zfunc = new ZFunction (zCoeff, 2, true);
      cList = new ComplexConstraint [5];
      cListSize = 5;
      zCoeff[0] = 1.0; zCoeff[1] = 0.0;
      cList[0] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 4.0);
      zCoeff[0] = 0.0; zCoeff[1] = 2.0;
      cList[1] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 12.0);
      zCoeff[0] = 3.0; zCoeff[1] = 2.0;
      cList[2] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 18.0);
      zCoeff[0] = 1.0; zCoeff[1] = 0.0;
      cList[3] = new ComplexConstraint (2, zCoeff, Constraint.GREATER_THAN_OR_EQUAL_TO, 0.0);
      zCoeff[0] = 0.0; zCoeff[1] = 1.0;
      cList[4] = new ComplexConstraint (2, zCoeff, Constraint.GREATER_THAN_OR_EQUAL_TO, 0.0);
   }


   /**
    * Constructor
    */
   public Options (String nameIn, int w, int h, int b)
   {
      name = new String (name);
      width = w;
      height = h;
      browser = b;
      sigDigits = 2;

      double [] zCoeff = {3.0, 5.0};
      zfunc = new ZFunction (zCoeff, 2, true);
      cList = new ComplexConstraint [5];
      cListSize = 5;
      zCoeff[0] = 1.0; zCoeff[1] = 0.0;
      cList[0] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 4.0);
      zCoeff[0] = 0.0; zCoeff[1] = 2.0;
      cList[1] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 12.0);
      zCoeff[0] = 3.0; zCoeff[1] = 2.0;
      cList[2] = new ComplexConstraint (2, zCoeff, Constraint.LESS_THAN_OR_EQUAL_TO, 18.0);
      zCoeff[0] = 1.0; zCoeff[1] = 0.0;
      cList[3] = new ComplexConstraint (2, zCoeff, Constraint.GREATER_THAN_OR_EQUAL_TO, 0.0);
      zCoeff[0] = 0.0; zCoeff[1] = 1.0;
      cList[4] = new ComplexConstraint (2, zCoeff, Constraint.GREATER_THAN_OR_EQUAL_TO, 0.0);
   }


   public String getName ()
   {
      return name;
   }


   public int getWidth ()
   {
      return width;
   }


   public int getHeight ()
   {
      return height;
   }


   public int getBrowser ()
   {
      return browser;
   }


   private int newC (double r, double g, double b)
   {
      return (new Color ((float) r, (float) g, (float) b)).getRGB();
   }


   public void setColor (int i)
   {
      switch (i)
      {
         case 0:
            // black and white
            color = new ColorSet (names[0], newC (0,0,0), newC (0,0,0), newC (1,1,1), newC (0,0,0), newC (0,0,0), newC(0,0,0));
            break;
         case 1:
            // guelph gryphon
            color = new ColorSet (names[1], newC (1,0,0), newC (1,1,0), newC (0,0,0), newC (1,0.2,0), newC (1,0,0), newC(1,0.7,0));
            break;
         case 2:
            // midnight
            color = new ColorSet (names[2], newC (0,0,1), newC (0,0.3,0.8), newC (0,0,0), newC (0.5,0,1), newC (0.4,0,0.6), newC(1,0,1));
            break;
         case 3:
            // sea
            color = new ColorSet (names[3], newC (0,0,1), newC (0,0.3,1), newC (0,0,0), newC (0.1,0,1), newC (0,0.7,0.7), newC(0,0,0.6));
            break;
         case 4:
            // grass and weed
            color = new ColorSet (names[4], newC (0,1,0), newC (0,0.7,0.1), newC (0,0,0), newC (0,1,0.4), newC (0.7,0.8,0), newC(0,0.9,0));
            break;
         case 5:
            // groovy
            color = new ColorSet (names[5], newC (0.4,0.8,0), newC (0,0.5,0.9), newC (0.4,0,0.1), newC (0.9,1,0.3), newC (1,0,0.3), newC(1,1,0));
            break;
         case 6:
            // sexy
         color = new ColorSet (names[6], newC (0,1,0), newC (0,0,0.5), newC (0,0,0), newC (0.4,0.7,0), newC (0,0.5,0.9), newC(0.1,0.9,0));
            break;
         case 7:
            // intense
            color = new ColorSet (names[7], newC (0,1,0), newC (1,1,0), newC (0,0,0), newC (0,1,1), newC (1,0,0), newC(0,0,1));
            break;
         default:
            break;
      }
   }


   public ColorSet getColor ()
   {
      return color;
   }


   public void setSigDigits (int s)
   {
      sigDigits = s;
   }


   public int getSigDigits ()
   {
      return sigDigits;
   }

}



