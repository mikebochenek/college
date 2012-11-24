/*
 * Independent Study Project
 * MATH*4240 Advanced Topics in Modelling (Winter 2001)
 * Michael Bochenek [ID: 0041056] mboche01@uoguelph.ca
 */


/**
 * 
 * @author Michael Bochenek (mboche01@uoguelph.ca)
 */
public class SimplexMethod 
{  
   /** number of rows in simplex tableau */
   private static int rows;

   /** number of columns in the simplex tableau */
   private static int columns;

   /** single iteration was fine */
   public static final int ITERATION_OK = 6101;

   /** the current simplex tableau is optimal */
   public static final int OPTIMAL = 6102;

   /** the current simplex tableau is unbounded */
   public static final int UNBOUNDED_Z = 6103;

   /** an internal error occured during the iteration */
   public static final int INTERNAL_ERROR = 6104;

   /** initialize integer values */
   public static final int UNDEFINED = -1;


   /**
    * Constructor 
    */
   public SimplexMethod()
   {

   }


   /**
    * Perform a single iteration of the algorithm
    * @param td table date (a 2-D array of Number objects)
    * @param rowsIn number of rows in table
    * @param columnsIn number of columns in table
    * @return error code
    */ 
   public static int performIteration (TableData td, int rowsIn, int columnsIn)
   {
      int returnCode = ITERATION_OK;

      rows = rowsIn;
      columns = columnsIn;


      int pivotColumn = 0;
      Number minimum = td.getEntry (0, 0);
      for (int i = 0; i < columns; i++)
      {
         Number tmp = td.getEntry (0, i);
         if (minimum.greaterThan (tmp))
         {
            pivotColumn = i;
            minimum = tmp;
         }
      }

      if (pivotColumn == UNDEFINED)
      {
         System.err.println ("pivotColumn in performIteration is still UNDEFINED"); 
         return INTERNAL_ERROR; 
      }

      for (int i = 0; i < rows; i++)
      {
         td.setSelected (i, pivotColumn, true);
      }


      return pivotColumn;
   }


   /**
    * Perform the minimum ratio test
    * @param td table date (a 2-D array of Number objects)
    * @param pivotColumn identifies which column to perform the test on
    * @return row index of value with minimum ratio
    */
   public static int minimumRatioTest (TableData td, int pivotColumn, int rowsIn, int columnsIn)
   {
      int pivotRow = 0;

      rows = rowsIn;
      columns = columnsIn;

      Number minimum = new Number (td.getEntry (1, pivotColumn));
      minimum.divide (td.getEntry (1, columns).getBaseOne());

      for (int i = 0; i < rows; i++)
      {
         Number tmp = new Number (td.getEntry (i+1, pivotColumn));
         tmp.divide (td.getEntry (i+1, columns).getBaseOne());
         System.out.println (td.getEntry (i+1, columns).getBaseOne());

         if (minimum.lessThan (tmp))
         {
            pivotRow = i+1;
            minimum = tmp;
         }
      }

      if (pivotRow == UNDEFINED)
      {
         System.out.println ("pivotRow in minimumRatioTest is still UNDEFINED");
         return INTERNAL_ERROR; 
      }

      for (int i = 0; i < columns + 1; i++)
      {
         td.setSelected (pivotRow, i, true);
      }


      return pivotRow;
   }


   public static int rowOperations (TableData td, int pivotColumn, int pivotRow, int rowsIn, int columnsIn)
   {
      rows = rowsIn;
      columns = columnsIn;

      double pivotNum = td.getEntry (pivotRow, pivotColumn).getBaseOne();

      for (int i = 0; i < columns + 1; i++)
      {
         td.getEntry (pivotRow, i).divide (pivotNum);
      }

      return ITERATION_OK; 
   }


   public static int moreRowOperations (TableData td, int pivotColumn, int pivotRow, int rowsIn, int columnsIn)
   {
      rows = rowsIn;
      columns = columnsIn;

      for (int i = 0; i < rows + 1; i++)
      {
         if (i != pivotRow)
         {
            double tmp = td.getEntry (i, pivotColumn).getBaseOne();

            for (int j = 0; j < columns + 1; j++)
            {
               Number n = new Number (td.getEntry (pivotRow,j));
               n.multiply (tmp);
               td.getEntry (i,j).subtract (n); 
            }
         }
      }

      return ITERATION_OK; 
   }


   public static int clearSelections (TableData td, int rowsIn, int columnsIn)
   {
      rows = rowsIn;
      columns = columnsIn;

      for (int i = 0; i < columns + 1; i++)
      {
         for (int j = 0; j < rows + 1; j++)
         {
            td.setSelected (j, i, false);
         }
      }

      return ITERATION_OK; 
   }


}



