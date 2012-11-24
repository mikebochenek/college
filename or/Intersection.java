/*
 * Independent Study Project
 * MATH*4240 Advanced Topics in Modelling (Winter 2001)
 * Michael Bochenek [ID: 0041056] mboche01@uoguelph.ca
 */


/**
 * Calculates intersection point between two lines
 * (Defined by two Constraint structures) 
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */

public class Intersection
{
   /** x-value of int */
   private double x;

   /** y-value of int */
   private double y;


   public Intersection (Constraint ci, Constraint cj)
   {
            double xc1 = ci.getXCoeff();
            double xc2 = cj.getXCoeff();
            double yc1 = ci.getYCoeff();
            double yc2 = cj.getYCoeff();
            double c1 = ci.getConstant();
            double c2 = cj.getConstant();

            if (yc1 == 0.0 || yc1 == -0.0) 
            {
               yc1 = 0.00001;
            }
            if (yc2 == 0.0 || yc2 == -0.0) 
            {
               yc2 = 0.00001;
            }

            double xC1 = -1 * xc1 / yc1;
            double xC2 = -1 * xc2 / yc2;
            double C1 = c1 / yc1;
            double C2 = c2 / yc2;

            x = (C2 - C1) / (xC1 - xC2);
            y = x * xC1 + C1;
   }


   public double getXInt ()
   {
      return x;
   }


   public double getYInt ()
   {
      return y;
   }

}

