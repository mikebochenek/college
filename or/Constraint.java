/*
 * Independent Study Project
 * MATH*4240 Advanced Topics in Modelling (Winter 2001)
 * Michael Bochenek [ID: 0041056] mboche01@uoguelph.ca
 */


/**
 * Used for the graphical method, this class contains values for a 
 * 2-D constraint, only two variables are present in the contraint.
 * This class is similar to the ComplexConstraint class and they should
 * probably be integerated into one class that contains the functionality
 * of for both of them. 
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class Constraint
{
   /** less than as in x < y */
   public static final int LESS_THAN = 4101;

   /** less than or equal to as in x <= y */
   public static final int LESS_THAN_OR_EQUAL_TO = 4102;

   /** greater than as in x > y */
   public static final int GREATER_THAN = 4103;

   /** greater than or equal to as in x >= y */
   public static final int GREATER_THAN_OR_EQUAL_TO = 4104;

   /** equal to as in x = y */
   public static final int EQUAL_TO = 4105;
   
   /** the cofficient of x */
   private double xCoeff;

   /** the coefficient of y */
   private double yCoeff;

   /** flag that describes the type of constraint */
   private int relation;

   /** RHS (right hand side) of equation */
   private double constant;


   /**
    * Constructor initializes all variables associated with this type of
    * constraint.
    * @param x the x-coefficient 
    * @param y the y-coefficient
    * @param r flag to describe the type of relation
    * @param c RHS (right hand side of equation
    */
   public Constraint (double x, double y, int r, double c)
   {
      if (r != LESS_THAN && r != LESS_THAN_OR_EQUAL_TO && r != GREATER_THAN && r!= GREATER_THAN_OR_EQUAL_TO && r != EQUAL_TO)
      {
         System.err.println ("Invalid relation type in constructor");
      }

      this.xCoeff = x;
      this.yCoeff = y;
      this.relation = r; 
      this.constant = c;
   }


   /**
    * Test a particular point against this constraint
    */
   public boolean testPoint (double x, double y)
   {
      double value = xCoeff * x + yCoeff * y;

      if (value < constant && relation == LESS_THAN)
      {
         return true;
      }
      else if (value <= constant && relation == LESS_THAN_OR_EQUAL_TO)
      {
         return true;
      }
      else if (value > constant && relation == GREATER_THAN)
      {
         return true;
      }
      else if (value >= constant && relation == GREATER_THAN_OR_EQUAL_TO)
      {
         return true;
      }
      else if (value == constant && relation == EQUAL_TO)
      {
         return true;
      }
      else
      {
         return false;
      }

   }


   /**
    * Convert the constraint to a string (adding the appropriate symbols)
    * @return string representation
    */
   public String toString ()
   {
      String retVal = new String ();

      if (xCoeff != 0)
      {
         retVal += xCoeff;
         retVal += "x";
      }

      if (yCoeff < 0)
      {
         retVal += "-";
         retVal += (-1 * yCoeff);
         retVal += "y";
      }
      else if (yCoeff > 0)
      {
         retVal += "+";
         retVal += yCoeff;
         retVal += "y";
      }
      else
      {
      
      }

      if (relation == LESS_THAN)
      {
         retVal += " < ";
      }
      else if (relation == LESS_THAN_OR_EQUAL_TO)
      {
         retVal += " <= ";
      }
      else if (relation == GREATER_THAN)
      {
         retVal += " > ";
      }
      else if (relation == GREATER_THAN_OR_EQUAL_TO)
      {
         retVal += " >= ";
      }
      else if (relation == EQUAL_TO)
      {
         retVal += " = ";
      }

      retVal += constant;

      return retVal;
   }


   /**
    * get value of x-coefficient
    * @return x coefficient
    */
   public double getXCoeff ()
   {
      return xCoeff;
   }


   /**
    * get the value of the y-coefficient
    * @return y coefficient
    */
   public double getYCoeff ()
   {
      return yCoeff;
   }


   /**
    * get the type of constraint
    * @return the type of constraint
    */
   public int getRelation ()
   {
      return relation;
   }


   /**
    * get the RHS (right hand side)
    * @return RHS (right hand side)
    */
   public double getConstant ()
   {
      return constant;
   }


   /**
    * get the x-intercept (where y = 0) in a 2-D graphical representation
    * @return x-intercept
    */
   public double getXInt ()
   {
      return constant / xCoeff;
   }


   /**
    * get the y-intercept (where x = 0) in a 2-D graphical representation
    * @return y-intercept
    */
   public double getYInt ()
   {
      return constant / yCoeff;
   }


   /**
    * get the slope (rise / run) in a 2-D graphical representation
    * @return slope
    */
   public double getSlope ()
   {
      return -1 * xCoeff / yCoeff;
   }

}


