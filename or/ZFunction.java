/*
 * Independent Study Project
 * MATH*4240 Advanced Topics in Modelling (Winter 2001)
 * Michael Bochenek [ID: 0041056] mboche01@uoguelph.ca
 */


/**
 * This function holds the information for a z function (function that
 * is to be optimized) 
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class ZFunction 
{
   /** array of coefficients */
   private double [] coeff;

   /** if true than we need to maximize, false means minimize */
   private boolean maximize;


   /**
    * Constructor
    * @param nums array of coefficients
    * @param size size of the array of coefficients
    * @param maxIn true for maximize, and false for minimize
    */
   public ZFunction (double [] nums, int size, boolean maxIn)
   {
      coeff = new double [size];

      if (size != nums.length)
      {
         System.err.println ("Specified size doesn't equal to size of array in ZFunction.");
      }

      for (int i = 0; i < nums.length; i++)
      {
         coeff[i] = nums[i];
      }

      maximize = maxIn;
   }


   public boolean getMaximize ()
   {
      return maximize;
   }


   /**
    * Get the number of coefficients in the function
    * @return size of array
    */
   public int size()
   {
      return coeff.length;
   }


   /**
    * Get the the coefficients
    * @return array that holds the coefficients
    */
   public double [] getCoeffs ()
   {
      return coeff;
   }


   /**
    * Get the i-th coefficient
    * @return value of the coefficient
    */
   public double getCoeffAt (int pos)
   {
      return coeff[pos];
   }


   public double eval (double [] values)
   {
      double retVal = 0.0;

      if (values.length != coeff.length)
      {
         return 0;
      }
      else
      {
         for (int i = 0; i < values.length; i++)
         {
            retVal += coeff[i] * values[i];
         }
      }

      return retVal;
   }

}



