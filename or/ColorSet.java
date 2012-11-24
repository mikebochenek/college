/*
 * Independent Study Project
 * MATH*4240 Advanced Topics in Modelling (Winter 2001)
 * Michael Bochenek [ID: 0041056] mboche01@uoguelph.ca
 */

import java.awt.Color;

/**
 * Contains color settings for a single color set
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class ColorSet 
{
   private Color textC; 
   private Color boxC;
   private Color backgroundC;
   private Color fillC;
   private Color selectC;
   private Color otherC;
   private String name;

   /**
    * Constructor
    */
   public ColorSet (String nameIn, int t, int box, int b, int f, int s, int o)
   {
      name = new String (nameIn);
      textC = new Color (t);
      boxC = new Color (box);
      backgroundC = new Color (b);
      fillC = new Color (f);
      selectC = new Color (s);
      otherC = new Color (o);
   }


   public String getName ()
   {
      return name;
   }


   public Color getTextC ()
   {
      return textC;
   }


   public Color getBoxC ()
   {
      return boxC;
   }


   public Color getBackgroundC ()
   {
      return backgroundC;
   }


   public Color getFillC ()
   {
      return fillC;
   }


   public Color getSelectC ()
   {
      return selectC;
   }


   public Color getOtherC ()
   {
      return otherC;
   }

}



