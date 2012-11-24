/*
 * Independent Study Project
 * MATH*4240 Advanced Topics in Modelling (Winter 2001)
 * Michael Bochenek [ID: 0041056] mboche01@uoguelph.ca
 */


/**
 * Contains all the numbers that go into the simplex table 
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class TableData 
{
   /** array of Number objects */
   private Number [][] data;

   /** number of rows in table */
   private int rows;

   /** number of columns in table */
   private int columns;

   /** selected or not */
   private boolean [][] selected;


   /**
    * Constructor that populates the array
    * @param c contains all the constraints
    * @param z contains the z function
    * @param s array of simple constraints
    * @param rowsIn number of rows
    * @param columnsIn number of columns
    */
   public TableData (ConstraintContainer c, ZFunction z, SimpleConstraint [] s, int rowsIn, int columnsIn)
   {
      if (c == null || z == null || s == null)
      {
         //System.err.println ("Important parameter is init to null in TableData.");
      }

      rows = rowsIn;
      columns = columnsIn;

      data = new Number [rows + 1][columns + 1];

      selected = new boolean [rows + 1][columns + 1];
      for (int i = 0; i < (rows + 1); i++)
      {
         for (int j = 0; j < (columns + 1); j++)
         {
            selected[i][j] = false;
         }
      }

      processConstraints (c);

      processZFunction (z);

      processSimple (s);
   }


   /**
    * Populate section associated with all constraints
    * @param con contains all the constraints
    */
   private void processConstraints (ConstraintContainer con)
   {
      if (rows != con.size())
      {
         System.out.println ("Size of constraints doesn't equal to number of rows in TableData");
      }

      for (int i = 0; i < rows; i++)
      {
         ComplexConstraint tmpC = con.getConstraint (i);

         if (columns != tmpC.size())
         {
            System.err.println ("Size of particular constraint doesn't equal to the number of columns in TableData");
         }

         for (int j = 0; j < columns; j++)
         {
            data[i+1][j] = new Number (tmpC.getCoeffAt (j));
         }
      
         data[i+1][columns] = new Number (tmpC.getConstant());
      }

   }


   /**
    * Populate section associated with the z function
    * @param zfunc contains the z function
    */
   private void processZFunction (ZFunction zfunc)
   {
      if (columns != zfunc.size())
      {
         System.err.println ("Size of zfunction doesn't equal to number of columns in TableData");
      }

      for (int i = 0; i < columns; i++)
      {
         data[0][i] = new Number (zfunc.getCoeffAt (i));
      } 

      data[0][columns] = new Number();
   }


   /**
    * Populate section associated with simple constraints
    * @param simple array of simple constraints
    */
   private void processSimple (SimpleConstraint [] simple)
   {

   }


   /**
    * Get the number at the i-th and j-th position in the 2-D array
    * @param i index (constraint i)
    * @param j index (coefficient of variable j)
    * @return the number at the specified position
    */
   public Number getEntry (int i, int j)
   {
      return data[i][j];
   }


   /**
    * Get the string representation of the i-th and j-th position in the array
    * @param i index (constraint i)
    * @param j index (coefficient of variable j)
    * @return the string representation of the number at the specified position
    */
   public String getEntryString (int i, int j)
   {
      return data[i][j].toString();
   }


   /**
    * Debug function to print table to the screen
    */
   public void printDebug ()
   {
      for (int i = 0; i < rows + 1; i++)
      {
         for (int j = 0; j < columns; j++)
         {
            System.out.print (data[i][j].toString() + "  ");
         }
         System.out.println (" ");
      }
   }


   public boolean getSelected (int i, int j)
   {
      return selected[i][j];
   }


   public void setSelected (int i, int j, boolean value)
   {
      selected[i][j] = value;
   }


}



