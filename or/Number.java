/*
 * Independent Study Project
 * MATH*4240 Advanced Topics in Modelling (Winter 2001)
 * Michael Bochenek [ID: 0041056] mboche01@uoguelph.ca
 */


/**
 * This class is need to take care of the two components of a number,
 * the normal component, and the Big M component
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class Number
{
   /** value of the normal component */
   private double baseOne;

   /** value of the Big M component */
   private double baseM;


   /**
    * Constructor with values initialized to the parameters
    * @param baseOne_in value of normal component
    * @param baseM_in value of Big M component
    */
   public Number (double baseOne_in, double baseM_in)
   {
      this.baseOne = baseOne_in;
      this.baseM = baseM_in;
   }


   public Number (Number n)
   {
      this.baseOne = n.getBaseOne();
      this.baseM = n.getBaseM();
   }


   /**
    * Constructor, with the normal component initialized to the parameter
    * and the Big M component initialized to 0. 
    * @param baseOne_in value of the normal component
    */
   public Number (double baseOne_in)
   {
      this.baseOne = baseOne_in;
      this.baseM = 0;
   }


   /**
    * Constructor, both components are initialized to 0.
    */
   public Number ()
   {
      this.baseOne = 0;
      this.baseM = 0;
   }


   /**
    * Convert the number to a string representation, taking into account
    * both components
    * @return string representation
    */
   public String toString ()
   {
      String retVal = new String();

      if (baseM != 0)
      {
         retVal += MathUtil.specializedRound (baseM, 2) + "M";

         if (baseOne < 0)
         {
            retVal += " - ";
            retVal += (-1 * MathUtil.specializedRound (baseOne, 2));
         }
         else if (baseOne > 0)
         {
            retVal += " + ";
            retVal += MathUtil.specializedRound (baseOne, 2);
         }
      }
      else
      {
         retVal += MathUtil.specializedRound (baseOne, 2);
      }
      
      return retVal;
   }


   /**
    * Add a second number to this number
    * @param n number to be added
    */
   public void add (Number n)
   {
      this.baseOne += n.getBaseOne();
      this.baseM += n.getBaseM();
   }


   /**
    * Subtract a second number to this number
    * @param n number to be subtracted
    */
   public void subtract (Number n)
   {
      this.baseOne -= n.getBaseOne();
      this.baseM -= n.getBaseM();
   }


   /**
    * Multiply this number by another number
    * @param n number to multiply by
    */
   public void multiply (double d)
   {
      this.baseOne *= d;
      this.baseM *= d;
   }


   /**
    * Divide this number by another number
    * @param n number to divide by
    */
   public void divide (double d)
   {
      this.baseOne /= d;
      this.baseM /= d;
   }


   /**
    * Get the normal component
    * @return value of the normal component
    */
   public double getBaseOne ()
   {
      return baseOne;
   }


   /**
    * Get the Big M component
    * @return value of the Big M component
    */
   public double getBaseM ()
   {
      return baseM;
   }


   /**
    * Check if this number equals to another number
    * @param n number to compare to
    * @return true if they are equal, false otherwise
    */
   public boolean equalTo (Number n)
   {
      return (n.getBaseOne() == baseOne && n.getBaseM() == baseM);
   }


   /**
    * Check if this number is less than another number
    * @param n number to compare to
    * @return true if this number is less than, false otherwise
    */
   public boolean lessThan (Number n)
   {
      if (baseM < n.getBaseM())
      {
         return true;
      }
      else if (baseM == n.getBaseM())
      {
         if (baseOne < n.getBaseOne())
         {
            return true;
         }
         else
         {
            return false;
         }
      }
      else
      {
         return false;
      }
   }


   /**
    * Check if this number is greater than another number
    * @param n number to compare to
    * @return true if this number is greater than, false otherwise
    */
   public boolean greaterThan (Number n)
   {
      if (baseM > n.getBaseM())
      {
         return true;
      }
      else if (baseM == n.getBaseM())
      {
         if (baseOne > n.getBaseOne())
         {
            return true;
         }
         else
         {
            return false;
         }
      }
      else
      {
         return false;
      }
   }


   /**
    * Check if this number is greater than or equal to another number
    * @param n number to compare to
    * @return true if this number is greater than or equal to, false otherwise
    */
   public boolean greaterThanOrEqualTo (Number n)
   {
      return greaterThan (n) || equalTo (n);
   }


   /**
    * Check if this number is less than or equal to another number
    * @param n number to compare to
    * @return true if this number is less than or equal to, false otherwise
    */
   public boolean lessThanOrEqualTo (Number n)
   {
      return lessThan (n) || equalTo (n);
   }

}


