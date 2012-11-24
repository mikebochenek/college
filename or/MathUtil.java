/*
 * Independent Study Project
 * MATH*4240 Advanced Topics in Modelling (Winter 2001)
 * Michael Bochenek [ID: 0041056] mboche01@uoguelph.ca
 */


/**
 * Math utility function to take care of mathematical operations
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class MathUtil 
{


   /**
    * Constructor
    */
   public MathUtil ()
   {

   }


   public static boolean closeEnough (double x, double y, Options options)
   {
      boolean retVal = false;

      double newx = specializedRound (x, options);
      double newy = specializedRound (y, options);

      if (Math.abs (newx - newy) < 0.001)
      {
         return true;
      }
      else
      {
         return false;
      }

   }


   public static double specializedRound (double x, Options options)
   {
      return Math.round (x * Math.pow (10, options.getSigDigits())) / Math.pow (10, options.getSigDigits());
   }


   public static double specializedRound (double x, int digits)
   {
      return Math.round (x * Math.pow (10, digits)) / Math.pow (10, digits);
   }



}



