/*
Michael Bochenek
980-492-820
27-160 Foundations of Programming
Assignment #2:  More fun with metric conversions

-------------------------------------------------------------------------
NOTE:  Functions are used throughtout this program just because
       I wanted to play around with functions.  All functions are defined
       in the order that they are used.  Each function is used only once,
       except outputTime which is called 3 times.
-------------------------------------------------------------------------
*/

import ccj.*;

public class Assignment2
{  public static void main(String[] args)
   {  
      printAuthorInfo();      
      // call function to display name, student id#, assignment#

      String unit = getUnitFromUser();
      // function prompts and gets "unit" from user (includes varify)

      double distanceInKM = getDistanceFromUser(unit);
      // function prompts and gets "distanceInKM" from user (includes varify)

      int hours = getHoursFromUser();
      // function prompts and gets "hours" from user (includes varify)

      int minutes = getMinutesFromUser();
      // function gets "minutes" (just like hours)

      int seconds = getSecondsFromUser();
      // function gets "seconds" (just like hours)

      int timeInSeconds = Numeric.round(hours * SECONDS_IN_HOUR
                                      + minutes * SECONDS_IN_MINUTE
                                      + seconds);
      // calculate total time in seconds

      System.out.println("\nDISTANCE:");
      Console.out.printf("  Miles: %.2f \n", distanceInKM / CONVERSION);
      Console.out.printf("  Kilometers: %.2f \n", distanceInKM);
      // perform conversions and output to console the distance 
      // in miles and kilometers correct to 2 decimal places

      System.out.println("TIME:");
      System.out.println("  " + outputTime(timeInSeconds));
      // output to console the time using outputTime()

      System.out.println("PACE:");
      int secondsPerMile = Numeric.round(timeInSeconds
                           / (distanceInKM / CONVERSION));
      // calculate pace by dividing converted distance by time in seconds
      int secondsPerKilometer = Numeric.round(timeInSeconds / distanceInKM);
      // calculate pace by dividing distance by time in seconds

      System.out.println("  " + outputTime(secondsPerKilometer)
                       + "per kilometer");
      System.out.println("  " + outputTime(secondsPerMile) + "per mile");
      // output to console PACE information using outputTime
   }

   public static final double CONVERSION = 1.609;
   public static final int SECONDS_IN_HOUR = 60 * 60;
   public static final int SECONDS_IN_MINUTE = 60;
   public static final int MINUTES_IN_HOUR = 60;

   public static void printAuthorInfo()
   {  System.out.println("********************");
      System.out.println("* Michael Bochenek *");
      System.out.println("* ID# 980-492-820  *");
      System.out.println("* Assignment #2    *");
      System.out.println("********************\n");
   }

   public static String getUnitFromUser()
   {  System.out.print("\n");
      String unit;
      boolean goodInput = true;
      do
      {  System.out.print("Please enter units ('mi' for miles, 'km' for "
                        + "kilometers): ");
         unit = Console.in.readLine();
         goodInput = true;
         // prompt for input and get input from keyboard         

         if (unit.length() >= 2) unit = unit.substring(0, 2);
         else goodInput = false;
         // make sure input is at least 2 characters and then extract        
         // substring to prepare for comparison

         unit = unit.toUpperCase();
         // convert to uppercase

         if (!(unit.equals("MI") || unit.equals("KM"))) goodInput = false;
         // if string inputed is not MI or KM then set goodInput to false
         // so that the while loop will loop
      } while (!goodInput);
      return unit;
      // the only two possibilities that this function can return is
      // MI or KM
   }

   public static double getDistanceFromUser(String unit)
   {  System.out.print("\n");
      double distance;
      do
      {  if (unit.equals("MI"))
            System.out.print("Please enter the distance in miles: ");
         if (unit.equals("KM"))
            System.out.print("Please enter the distance in kilometers: ");
         // prompt user for distance in miles or kilometers given
         // what the user entered as their units of choice

         distance = Console.in.readDouble();
         // get number from user - NOTE: MAIN TERMINATES IF NOT A DOUBLE
      } while (distance <= 0);
      // continue looping unless user enters positive and non-zero

      if (unit.equals("MI")) return (distance * CONVERSION);
      else return distance;
      // return the distance in kilometers ONLY
   }

   public static int getHoursFromUser()
   {  System.out.println("\nPlease enter your time (hours, minutes, seconds)");
      int hours;
      do
      {  System.out.print("Hours: ");
         hours = Console.in.readInt();
         // prompt user and get hours from keyboard
      } while (hours < 0);
      // continue looping if user enters negative values

      return hours;
   }

   public static int getMinutesFromUser()
   {  int minutes;
      do
      {  System.out.print("Minutes: ");
         minutes = Console.in.readInt();
         // prompt user and get minutes from keyboard
      }  while (minutes < 0 || minutes >= MINUTES_IN_HOUR);
      // continue looping if user enters negative values and more than 59
      return minutes;
   }

   public static int getSecondsFromUser()
   {  int seconds;
      do
      {  System.out.print("Seconds: ");
         seconds = Console.in.readInt();
         // prompt user and get seconds from keyboard
      } while (seconds < 0 || seconds >= SECONDS_IN_MINUTE);
      // continue looping if user enters negative values and more than 59
      return seconds;
   }

   public static String outputTime(int totalSeconds)
   {  int hours = totalSeconds / SECONDS_IN_HOUR;
      totalSeconds = totalSeconds % SECONDS_IN_HOUR;
      int minutes = totalSeconds / SECONDS_IN_MINUTE;
      int seconds = totalSeconds % SECONDS_IN_MINUTE;
      // convert totalSeconds into hours, minutes, and seconds

      String timeString = "";  // string to which hours, minutes, seconds
      // will be concatenated.  This string is returned by function.

      if (hours == 1) timeString = "1 hour ";
      else if (hours > 1) timeString = hours + " hours ";
      // if hours is 1, omit the 's' in 'hours'
      // if hours is 0, do not do anything

      if (minutes == 1) timeString = timeString + "1 minute ";
      else if (minutes > 1) timeString = timeString + minutes + " minutes ";
      // if minutes is 1, omit the 's' in 'hours'
      // if minutes is 0, do not do anything

      if (seconds == 1) timeString = timeString + "1 second ";
      else if (seconds > 1) timeString = timeString + seconds + " seconds ";
      // if seconds is 1, omit the 's' in 'hours'
      // if seconds is 0, do not do anything
      
      return timeString;
   }
}
