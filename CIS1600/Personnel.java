/* --------------------------------------------------------------------
Michael Bochenek
980-492-820
27-160 Foundations of Programming
Assignment #3:  Personnel Records
-----------------------------------------------------------------------
*/

import ccj.*;

public class Personnel
{  public static void main(String[] args)
   {  outputAuthorInfo();      
      // write to screen author name, id#, assignment#

      if (!validArguments(args)) return;
      // make sure arguments are good, if not display usage message

      int numberOfWorkers = readFile(args[0]);
      if (numberOfWorkers == -1) return;
      // read the file into public array Workers[100], if file doesn't
      // exist, function returns -1, and thus program exists.
      // normally function returns number of workers in array

      int x;
      do // main loop that keeps showing menu after each operation
      {  outputMenu();
         x = Integer.parseInt(Console.in.readLine());
         // read user input from keyboard, ASSUME ITS AN INTEGER

         if (x == 1) outputAllWorkers(numberOfWorkers);
         // print to screen all workers

         else if (x == 2) outputNumberOfWorkers(numberOfWorkers);
         // print to screen the number of workers

         else if (x == 3) outputWorker(getLowestWorker(numberOfWorkers));
         else if (x == 4) outputWorker(getHighestWorker(numberOfWorkers));
         // print to screen worker returned by get_lowest/hightest_worker

         else if (x == 5) outputAverage(numberOfWorkers);
         // print to screen average

      } while (x != 9); // keep looping unless user enters 9
   }

   // *** this function returns index of highest paid employee
   public static int getHighestWorker(int numberOfWorkers)
   {  int highestPaid = 0;
      for (int x = 0; x < numberOfWorkers; x++) // loop through all workers
      {  if (Worker[x].getSalary() > Worker[highestPaid].getSalary())
            highestPaid = x;
      } // if any worker's salary is greater than the salary of the
        // highest paid employee, update the index of highest paid employee
      System.out.println("HIGHEST PAID EMPLOYEE:");
      return highestPaid;
   }

   // *** this function returns index of lowest paid employee
   public static int getLowestWorker(int numberOfWorkers)
   {  int lowestPaid = 0;
      for (int x = 0; x < numberOfWorkers; x++) // loop through all workers
      {  if (Worker[x].getSalary() < Worker[lowestPaid].getSalary())
            lowestPaid = x;
      } // if any worker's salary is lower than the salary of the
        // lowest paid employee, update the index of lowest paid employee
      System.out.println("LOWEST PAID EMPLOYEE:");
      return lowestPaid;
   }
   
   // *** this procedure waits for the user to press enter and continues
   public static void pause()
   {  System.out.print("Hit <ENTER> to continue ");
      String temp = Console.in.readLine();
   }

   // *** this procedure outputs the average salary to the screen
   public static void outputAverage(int numberOfWorkers)
   {  double averageSalary = 0;
      for (int x = 0; x < numberOfWorkers; x++) // loop through all workers
         averageSalary += Worker[x].getSalary() / numberOfWorkers;
         // calculate average
      Console.out.printf("AVERAGE SALARY: $ %.2f \n", averageSalary);
      // output to screen
      pause(); // wait for user to hit enter
   }

   // *** this procedure outputs the number of workers
   public static void outputNumberOfWorkers(int numberOfWorkers)
   {  System.out.println("NUMBER OF PERSONNEL: " + numberOfWorkers);
      pause(); // wait for user to hit enter
   }

   // *** this procedure outputs list of all workers
   public static void outputAllWorkers(int numberOfWorkers)
   {  int longestName = 0;
      for (int x = 0; x < numberOfWorkers; x++)
      {  if (Worker[x].getName().length() > longestName)
            longestName = Worker[x].getName().length() + 3;
      } // figure out the longest name, so that the columns may be 
        // adjust properly (longestname + 3)

      String parmForPrintf = "";
      String seperatorLine = "";
      for (int x = 0; x < numberOfWorkers; x++)
      {  if (x % (WORKERS_PER_SCREEN) == 0
             && x != 0 && x != numberOfWorkers) pause();
             // pause every time the screen gets filled up with 
             // too many names.
         if (x % WORKERS_PER_SCREEN == 0)
         {  parmForPrintf = "%-" + longestName + "s ";
            Console.out.printf(parmForPrintf, "NAME:");
            Console.out.printf("%s", "SALARY:\n");
            // output headings

            seperatorLine = "";
            for (int y = 0; y < longestName; y++)
               seperatorLine = seperatorLine + "-";
            Console.out.printf(parmForPrintf, seperatorLine);
            Console.out.printf("%s", "-----------\n");
            // output seperator line
         }
         parmForPrintf = "%-" + longestName + "s ";
         Console.out.printf(parmForPrintf, Worker[x].getName());
         Console.out.printf("$ %.2f \n", Worker[x].getSalary());
         // output name and salary of current worker
      }
      pause();  // wait for user to hit enter
   }

   // *** this procedure outputs one worker
   // *** used by lowest/highest employee printouts
   public static void outputWorker(int x)
   {  System.out.print("NAME: " + Worker[x].getName());
      Console.out.printf("   SALARY: $ %.2f \n", Worker[x].getSalary());
      pause();
   }

   // *** this function reads the file into the public array WORKERS[]
   // it stops reading up to 100 workers or endoffile
   // if file specified doesn't exist, returns -1, which exits program
   // normally it returns the number of workers
   public static int readFile(String filename)
   {  TextInputStream inputData = new TextInputStream(filename);
      if (inputData.fail()) return -1;
      // if file doesn't exist, return -1

      int x = 0;
      do // loop up until 100 employees or endoffile
      {  String name = inputData.readLine();
         // read name
         if (inputData.fail()) continue; // check for odd # of lines
         double salary = Numeric.parseDouble(inputData.readLine());
         // read salary
         Worker[x] = new Employee(name, salary);
         // update array
         x = x + 1;
     } while (x < 100 && (!inputData.fail()));
     return x;
   }

   // *** this function makes sure that user inputs only one argument
   // if not, it displays an usgae message
   public static boolean validArguments(String[] args)
   {  if (args.length != 1)
      {  System.out.println("Invalid number of parameters.\n"
               + "\n      USAGE:  java Personnel filename\n"
               + "\n (where 'filename' is the name of the file "
               + "containing the data)");
         return false;
      }
      return true;
   }

   // *** this procedure displays the menu
   public static void outputMenu()
   {  System.out.print("\n============= MENU ===============\n"
                     + "1. List personnel records\n"
                     + "2. List number of personnel\n"
                     + "3. Determine lowest paid employee\n"
                     + "4. Determine highest paid employee\n"
                     + "5. Determine average salary\n"
                     + "9. Quit\n"
                     + "Enter: ");
   }

   // *** this procedure diplays the author info
   public static void outputAuthorInfo()
   {  System.out.println("********************");
      System.out.println("* Michael Bochenek *");
      System.out.println("* ID# 980-492-820  *");
      System.out.println("* Assignment #3    *");
      System.out.println("********************\n");
   }

   public static Employee[] Worker = new Employee[100];
   // public array that holds all the workers
   public static final int WORKERS_PER_SCREEN = 22;
   // option #1 displays this many workers per screen, and pauses

}
