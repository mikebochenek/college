/*
Michael Bochenek
980-492-820
27-160 Foundations of Programming
Assignment #1:  Metric Conversion
*/

import ccj.*;

public class AssignmentOne
{  public static void main(String[] args)
   {  System.out.println("Michael Bochenek");
      System.out.println("ID# 980-492-820\n");
      System.out.print("Please enter a weight in pounds: ");
      // Display author info and prompt the user for input
      
      int pounds = Console.in.readInt();
      // Get input and assign it to the integer pounds

      double kilograms = pounds * CONVERSION_FACTOR;
      // Perform the conversion and assign results to the double kilograms

      System.out.print("Metric-equivalent weight = ");
      Console.out.printf("%.2f kilograms.", kilograms);
      // Output the results to the screen, including value of kilograms
   }
   public static final double CONVERSION_FACTOR = 0.4536;
}
 

