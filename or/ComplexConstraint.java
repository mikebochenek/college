/*
 * Independent Study Project
 * MATH*4240 Advanced Topics in Modelling (Winter 2001)
 * Michael Bochenek [ID: 0041056] mboche01@uoguelph.ca
 */


/**
 * This class contains the coefficients for a single constraint 
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class ComplexConstraint
{
   /** array contains coefficients of variables */ 
   private double [] coeff;

   /** flag that indicates what type of constraint this is.
       (one of LESS_THAN, LESS_THAN_OR_EQUAL_TO, GREATER_THAN,
       GREATER_THAN_OR_EQUAL_TO, or EQUAL_TO) */
   private int relation;

   /** RHS (right hand side) of constraint */
   private double constant;


   /**
    * Constructor that initializes all data needed to describe a constraint.
    * @param num number of coefficients
    * @param coefficients all the coefficients stored in an array
    * @param r flag that indicates the type of relation
    * @param c right hand side of the constraint
    */
   public ComplexConstraint (int num, double [] coefficients, int r, double c)
   {
      if (r != Constraint.LESS_THAN && r != Constraint.LESS_THAN_OR_EQUAL_TO && r != Constraint.GREATER_THAN && r!= Constraint.GREATER_THAN_OR_EQUAL_TO && r != Constraint.EQUAL_TO)
      {
         System.err.println ("Invalid relation type in constructor");
      }

      this.relation = r; 
      this.constant = c;

      if (coefficients.length != num)
      {
         System.err.println ("Specified size doesn't equal to size of array");
      }

      coeff = new double [coefficients.length];

      for (int i = 0; i < coefficients.length; i++)
      {
         coeff[i] = coefficients[i];
      }
   }


   /**
    * Convert the constraint to a string (adding appropriate variables and
    * equality signs).
    * @return string representation
    */
   public String toString ()
   {
      String retVal = new String ();

      for (int i = 0; i < coeff.length; i++)
      {
         if (coeff[i] < 0)
         {
            retVal += " - ";
            retVal += (-1 * coeff[i]);
            retVal += "x" + (i+1);
         }
         else
         {
            retVal += " + ";
            retVal += coeff[i];
            retVal += "x" + (i+1);
         }
      }

      if (relation == Constraint.LESS_THAN)
      {
         retVal += " < ";
      }
      else if (relation == Constraint.LESS_THAN_OR_EQUAL_TO)
      {
         retVal += " <= ";
      }
      else if (relation == Constraint.GREATER_THAN)
      {
         retVal += " > ";
      }
      else if (relation == Constraint.GREATER_THAN_OR_EQUAL_TO)
      {
         retVal += " >= ";
      }
      else if (relation == Constraint.EQUAL_TO)
      {
         retVal += " = ";
      }

      retVal += constant;

      return retVal;
   }



   /**
    * Get the type of constraint (relation)
    * @return integer flag that describes type of constraint
    */
   public int getRelation ()
   {
      return relation;
   }


   /**
    * Get the RHS (right hand side) of contraint
    * @return RHS (right hand side) of constraint
    */
   public double getConstant ()
   {
      return constant;
   }


   /**
    * Get the number of coefficients.
    * @return number of coefficients
    */
   public int size ()
   {
      return coeff.length;
   }


   /**
    * Get the value of the i-th coefficient
    * @return value of coefficient
    */ 
   public double getCoeffAt (int pos)
   {
      return coeff[pos];
   }


   public void setCoeffAt (int pos, double value)
   {
      coeff[pos] = value;
   }

}


