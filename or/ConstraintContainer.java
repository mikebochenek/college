/*
 * Independent Study Project
 * MATH*4240 Advanced Topics in Modelling (Winter 2001)
 * Michael Bochenek [ID: 0041056] mboche01@uoguelph.ca
 */


/**
 * This class holds the list of constraints 
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class ConstraintContainer
{
   /** array of constraints */
   private ComplexConstraint [] cArray;


   /**
    * Constructor that allocates memory in array
    * @param size number of constraints that this container will hold
    */
   public ConstraintContainer (int size)
   {
      cArray = new ComplexConstraint [size];
   }


   /**
    * Add constraint to list by setting the i-th constraint to the new object.
    * @param c the constraint to be added
    * @param pos the position in the list to set equal to c
    */
   public void setConstraint (ComplexConstraint c, int pos)
   {
      cArray[pos] = c;
   }


   /**
    * Get the i-th constraint
    * @param pos the position in the array of constraints
    * @return the constraint at position pos
    */
   public ComplexConstraint getConstraint (int pos)
   {
      return cArray[pos];
   }


   /**
    * Get size of the constraint ocntainer
    * @return number of constrains held
    */
   public int size()
   {
      return cArray.length;
   }

}


