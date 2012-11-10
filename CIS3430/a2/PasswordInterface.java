// Michael Bochenek (ID) 0041056
// CIS 3430 Assignment #1

import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.*;
import java.util.AbstractCollection.*;

public class PasswordInterface 
{
   // holds the entire database in a tree
   private TreeSet data;

   // name of the database file, taken from constructor, or if none specified
   // in constructor than defaults to customer.dat
   private String dataFilename;

   
   // constructor that inits filename to password.dat and
   // creates a tree and starts reading data from the datafile...
   public PasswordInterface ()
   {
      data = new TreeSet (new EmailComparator());
      dataFilename = "password.dat"; 
      readDatabase ();
   }


   // constructor that inits filename to the passed in string and
   // creates a tree and starts reading data from the datafile...
   public PasswordInterface (String databaseFilename)
   {
      data = new TreeSet (new EmailComparator());
      dataFilename = databaseFilename; 
      readDatabase ();
   }
   

   // add the line the the tree, returns 0 on success and -1 on failure
   public int addEntry (String singleEntry)
   {
      boolean valueReturned = false;

      try
      {
         // actually use the TreeSet.add function to add the entry
         valueReturned = data.add (singleEntry);
      }
      catch (Exception e)
      {
         System.out.println ("The value could not be added since the" +
                             " entries could not be compared.");
      }
                             
      // valueReturned is true when successful, and false if not...
      if (valueReturned == false)
      {
         return -1;
      }
      else
      {
         return 0;
      }

   }


   // check if singleEntry is contained, return 0 on success and -1 on fail
   public int containsEntry (String singleEntry)
   {
      boolean valueReturned = false;

      try
      {
         // actually perform the removal
         valueReturned = data.contains (singleEntry);
      }
      catch (Exception e)
      {
         System.out.println ("Unable to find entry because unable" +
                             "to compare entries using the comparator");
      }
      
      // valueReturned is true when successful, and false if not...
      if (valueReturned == false)
      {
         return -1;
      }
      else
      {
         return 0;
      }

   }


   // remove the line from the tree, return 0 on success and -1 on fail
   public int removeEntry (String singleEntry)
   {
      boolean valueReturned = false;

      try
      {
         // actually perform the removal
         valueReturned = data.remove (singleEntry);
      }
      catch (Exception e)
      {
         System.out.println ("Unable to remove entry because unable" +
                             "to compare entries using the comparator");
      }
      
      // valueReturned is true when successful, and false if not...
      if (valueReturned == false)
      {
         return -1;
      }
      else
      {
         return 0;
      }

   }


   // read the database given a filename
   public int readDatabase () 
   {
      return readDatabase (dataFilename);
   }


   // overides the previous function, it takes a filename as a parameter
   public int readDatabase (String databaseFilename) 
   {
      // this flag is changed only if unable to read file, or add a line
      int valueReturnedByRead = 0;
   
      try 
      {
         // open the file for reading and setup file pointers
         RandomAccessFile file = new RandomAccessFile (databaseFilename, "r");
         long filePointer = 0;
         long length = file.length();
      
         // keep reading until eof()
         while (filePointer < length)
         {
            // read a line
            String s = file.readLine();
            filePointer = file.getFilePointer();
            
            // add the entry to the tree
            int value = this.addEntry (s);
            
            if (value != 0)
            {
               System.out.println ("Unable to add this entry: " + s); 
               valueReturnedByRead = -1;
            }
         }

         file.close();
         
      }
      
      catch (Exception e)
      {
         System.out.println ("Unable to read the file");
         valueReturnedByRead = -1;
      }

      return valueReturnedByRead;
   }


   // save the database, given a file
   public int writeDatabase ()
   {
      return writeDatabase (dataFilename);
   }


   // overides the previous function, it takes a filename as a parameter
   public int writeDatabase (String databaseFilename) 
   {
      try 
      {
         // setup the file for writing
         RandomAccessFile file = new RandomAccessFile (databaseFilename, "rw");
         
         Iterator x = data.iterator();

         // iterate through all the items in the list.
         while (x.hasNext())
         {
            // write a line (plus \n) to the file
            file.writeBytes ( ((String) x.next()) + "\n");
         }

         long filePointer = file.getFilePointer();
         file.setLength (filePointer);
         
         file.close();
         
      }
      
      catch (Exception e)
      {
         System.out.println ("Unable to write to database");
         return -1;
      }
         
      return 0;
   }
   

   // print the entire database the the screen, for testing mostly
   public void printDatabase ()
   {
      System.out.println ("----START LIST---------");

      Iterator x = data.iterator();
      while (x.hasNext())
      {
         System.out.println ( (String) x.next());
      }
      
      System.out.println ("----END LIST---------");
   }
   
}

