/**	
 * This class will get the options from 'whereever' 
 */
import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Random;

public class OptionInterface 
{
   private Vector data;
   private String dataFilename;
   private Option options;
   

   /**
    * Constructor that uses defualt options file
    */
   public OptionInterface (Option o)
   {
      options = o;
      data = new Vector();
      dataFilename = "options.dat"; 
      readDatabase ();
   }


   /**
    * Costructor that uses the specified options file.
    */
   public OptionInterface (Option o, String databaseFilename)
   {
      options = o;
      data = new Vector();
      dataFilename = databaseFilename; 
      readDatabase ();
   }
   

   /**
    * Reads the options file.
    */
   public int readDatabase () 
   {
      return readDatabase (dataFilename);
   }


   /**
    * Reads the options file.
    */
   public int readDatabase (String databaseFilename) 
   {
      int valueReturnedByRead = 0;
   
      try 
      {
         RandomAccessFile file = new RandomAccessFile (databaseFilename, "r");
         long filePointer = 0;
         long length = file.length();
      
         while (filePointer < length)
         {
            String s = file.readLine();
            filePointer = file.getFilePointer();

            if ( ! (s.startsWith ("#") == true || s.length() <= 1) )
            {
               data.addElement (s);
               parse (s);
            }

         }

         if ((options.redUnits + options.blueUnits) > (Const.numOfRows * Const.numOfColumns)) 
         {
            System.out.println ("Too many units requested.");
            System.out.println ("Total units requested must be less than " + (Const.numOfRows * Const.numOfColumns)); 
            System.exit (-1);
         }

         Random rand = new Random ();
         if (options.redRandom == 1)
         {
            options.redUnits = rand.nextInt (options.redUnits) + 1;
         }
         if (options.blueRandom == 1)
         {
            options.blueUnits = rand.nextInt (options.blueUnits) + 1;
         }

         file.close();
         
      }
      
      catch (Exception e)
      {
         System.out.println ("Unable to read the file: " + dataFilename);
         valueReturnedByRead = -1;
         System.exit(-1);
      }

      return valueReturnedByRead;
   }


   /**
    * FOr debugging purposes only.
    */
   public void printAll()
	{
		for (int i = 0; i < data.size(); i++)
		{
         System.out.print ( ((String) data.elementAt(i)) + "\n");
	   }
   }


   /**
    * Check if the string specifies one of the possible options.
    * If it does, parse it and put the value into the Options object.
    */
   public void parse (String s)
   {
      if (s.startsWith ("speed=") == true)
      {
         options.speed = getInteger (s.substring (6));
         if (options.speed == 0)
         {
            options.speed = 1000;
         }
      }
      else if (s.startsWith ("gridlines=") == true)
      {
         if ( (s.substring (10)).equals ("1")) options.gridlines = true;
         else if ( (s.substring (10)).equals ("0")) options.gridlines = false;
      }
      else if (s.startsWith ("display=") == true)
      {
         if ( (s.substring (8)).equals ("1")) options.display = true;
         else if ( (s.substring (8)).equals ("0")) options.display = false;
      }
      else if (s.startsWith ("firepower=") == true)
      {
         options.firepower = getInteger (s.substring (10));
         if (options.firepower == 0)
         {
            options.firepower = 10;
         }
      }
 
      else if (s.startsWith ("lizUnits=") == true)
      {
         options.redUnits = getInteger (s.substring (9));
      }
      else if (s.startsWith ("lizRandom=") == true)
      {
         options.redRandom = getInteger (s.substring (10));
      }
      else if (s.startsWith ("dinoUnits=") == true)
      {
         options.blueUnits = getInteger (s.substring (10));
      }
      else if (s.startsWith ("dinoRandom=") == true)
      {
         options.blueRandom = getInteger (s.substring (11));
      }
      else if (s.startsWith ("lizAI=") == true)
      {
         options.redAI = getInteger (s.substring (6));
      }
      else if (s.startsWith ("dinoAI=") == true)
      {
         options.blueAI = getInteger (s.substring (7));
      }
      else if (s.startsWith ("dinoFun=") == true)
      {
         options.blueFun = getInteger (s.substring (8));
      }
      else if (s.startsWith ("lizFun=") == true)
      {
         options.redFun = getInteger (s.substring (7));
      }
      else if (s.startsWith ("dinoVisibility=") == true)
      {
         options.blueVisibility = getInteger (s.substring (15));
      }
      else if (s.startsWith ("lizVisibility=") == true)
      {
         options.redVisibility = getInteger (s.substring (14));
      }
      else if (s.startsWith ("dinoCom=") == true)
      {
         options.blueCom = getInteger (s.substring (8));
      }
      else if (s.startsWith ("lizCom=") == true)
      {
         options.redCom = getInteger (s.substring (7));
      }
      else 
      {
         System.out.println ("Token in user options file not recognized: " + s);
         System.exit (-1);
      }

   }


   /**
    * Simple utility function for converting string to integer.
    */
   public int getInteger (String s)
   {
      try
      {
         Integer tempInt = new Integer (s);
         return tempInt.intValue();
      }
      catch (Exception e)
      {
         return 0;
      }
   }


}

