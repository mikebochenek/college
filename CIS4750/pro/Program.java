/**	
 * This class starts the whole system
 */
public class Program 
{
   private static Option options;

   /**
    * Main function, this is starts everything and it is 
    * executed by JVM.
    */
   public static void main (String args[]) 
   {
      try
      {
         options = new Option(args[0]);
      }
      catch (Exception e) 
      {
         System.out.println ("Using default options file: \"options.dat\".\n");
         options = new Option();
      }
      options.readOptions();

      Main mainGui = new Main (options);
   }
}
