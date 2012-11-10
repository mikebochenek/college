// Michael Bochenek (ID) 0041056
// CIS 3430 Assignment #1

import java.util.StringTokenizer;
import java.util.Vector;
import java.util.*;
import java.util.AbstractCollection.*;
import java.io.*;
import java.net.*;

public class RemoteDatabaseInterface
{
   // holds the entire database in a tree
   private TreeSet data;

   // name of the database file, taken from constructor, or if none specified
   // in constructor than defaults to customer.dat
   private String dataFilename;

   
   // constructor that inits filename to customer.dat and
   // creates a tree and starts reading data from the datafile...
   public RemoteDatabaseInterface ()
   {
      data = new TreeSet (new BasicComparator());
      dataFilename = "customer.dat"; 
      readDatabase ();
   }


   // constructor that inits filename to the passed in string and
   // creates a tree and starts reading data from the datafile...
   public RemoteDatabaseInterface (String databaseFilename)
   {
      data = new TreeSet (new BasicComparator());
      dataFilename = databaseFilename; 
      readDatabase ();
   }
   

	// gets the size of the data set
	public int getSize()
	{
	   return data.size();
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


   // overrides the previous function in that it takes a Vector
   // with individual parts, assembles it into a string and adds it
   public int addEntry (Vector individualParts)
   {
      String tempSingleEntry = "";
      
      // for each element in Vector...
      for (int i = 0; i < individualParts.size(); i++)
      {
         // process each element in Vector
         String oneElement = ((String)individualParts.elementAt(i)); 
         tempSingleEntry += oneElement;
         
         // add a colon after each element, except the last one
         if (i != (individualParts.size() - 1))
         {
            // if the element is empty add a single space
            if (oneElement.equals(""))
            {
               tempSingleEntry += " ;";
            }
            else
            {
               tempSingleEntry += ";";
            }
         }
         else
         {
            if (oneElement.equals(""))
            {
               tempSingleEntry += " ";
            }
         }
         
      }

      // add entry using old method, and return whatever it returns
      return this.addEntry (tempSingleEntry);
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


   // get the Nth entry from the database (in order)
	public String getNthEntry (int entryNum)
   {
      // will "iterate" through all entries in the tree
      Iterator x = data.iterator();

		int entryCounter = 0;
      
      // iterate...
      while (x.hasNext())
      {
         String s = (String) x.next(); 
         if (entryCounter == entryNum)
			{
            // get the line...
				return s;
			}
			
			entryCounter++;
      }
      
      return new String ();
   }

   
   // find all lines that have partOfEntry as the partNum'th element
   // and return all of them in a vector
   public Vector queryEntry (String partOfEntry, int partNum)
   {
      // this vector will be used to contain and return the results
      Vector queryResults = new Vector();
      
      // will "iterate" through all entries in the tree
      Iterator x = data.iterator();
      
      // iterate...
      while (x.hasNext())
      {
         // get the line...
         String s = (String) x.next(); 
         
         // init the stringTokenizer
         StringTokenizer t = new StringTokenizer (s, ";");
         int temp = 1;

         // keep looping while there is more tokens
         while (t.hasMoreTokens() && temp <= partNum)
         {
            String tempToken = t.nextToken();
            
            // if the tempToken equals to partOfEntry then add it to Vector
            if (temp == partNum && tempToken.equals (partOfEntry))
            {
               queryResults.addElement (s);
            }

            // count the number of elements
            temp++;
         }  
      }
      
      return queryResults;
   }


   // search the tree for lines that have the specified surname, name, email
   // (if any one of the three strings is blank, then treat that as a 
   //  wild card equal to * (ie all) )
   public Vector queryEntryByThreeFields (String surname, String name, String email)
   {
      // will contain and return the results
      Vector queryResults = new Vector();
      
      // iterate through all the objects in the tree
      Iterator x = data.iterator();
      
      // iterate...
      while (x.hasNext())
      {
         // get a line
         String s = (String) x.next();
         
         // setup the stringtokenizer
         StringTokenizer t = new StringTokenizer (s, ";");
         
         // get the surname
         String currentSurname = t.nextToken();
         
         // if surname matches search criteria or if search surname is blank
         // continue comparing other 2 search criteria (name, and email)
         if ( ((currentSurname.toLowerCase()).equals (surname.toLowerCase()) 
               || surname.equals(""))
             && t.hasMoreTokens())
         {
            // get the name
            String currentName = t.nextToken();
            
            // if name matches search criteria or if search name is blank
            // continue comparing other 1 search critera (email)
            if ( ((currentName.toLowerCase()).equals (name.toLowerCase()) 
                  || name.equals(""))
                && t.hasMoreTokens())
            {
               // get the email
               String currentEmail = t.nextToken();
               
               // if email matches search email or if search email is blank
               if ((currentEmail.toLowerCase()).equals (email.toLowerCase()) 
                   || email.equals(""))
               {
                  // then and only then, is a matching item found
                  // and can be added to result Vector
                  queryResults.addElement (s);
               }
            }
         }
      }        
      
      return queryResults;
   }


   // given a single line of text, tokenize it and get the fieldNum'th entry
   public String getField (String singleEntry, int fieldNum)
   {
      // will contain and return the value
      String requestedField = "";
      
      // init the stringTokenizer
      StringTokenizer t = new StringTokenizer (singleEntry, ";");
      int temp = 1;
      
      // loop while there are more tokens
      while (t.hasMoreTokens() && temp <= fieldNum)
      {
         // gets updated with the given token
         requestedField = t.nextToken();
         temp++;
      }
      
      return requestedField;  
   }
   
   
   // return the vector representation of the entries in a single line
   public Vector parseEntry (String singleEntry)
   {
      // will contain and return the results
      Vector parsedEntry = new Vector();

      // setup string tokenizer
      StringTokenizer t = new StringTokenizer (singleEntry, ";");
      
      while (t.hasMoreTokens())
      {
         // get the next token and add it to the vector
         String s = t.nextToken();
         parsedEntry.addElement (s);
      }

      return parsedEntry;
   }

   // create a string that contains customer representation of a vector
   // return blank if the Vector does not have enough data to make a customer
   public String createCustomerString (Vector oneEntry) 
   {
      String messageField = new String();
     
      // return blank if vector doesn't have enough elements
      if (oneEntry.size() < 12)
      {
         return messageField;
      }

      messageField += "Surname: ";
      messageField += oneEntry.elementAt(0);
      messageField += "\nName: "; 
      messageField += oneEntry.elementAt(1);
      messageField += "\nE-mail: ";
      messageField += oneEntry.elementAt(2);
      messageField += "\nPhone: "; 
      messageField += oneEntry.elementAt(3);
      messageField += "\nAge: "; 
      messageField += oneEntry.elementAt(7);
      messageField += "\nSex: ";
      messageField += oneEntry.elementAt(8);
      messageField += "\nMoney Spent: ";
      messageField += oneEntry.elementAt(9);
      messageField += "\nDate of Last Purchase: ";
      messageField += oneEntry.elementAt(10);
      messageField += "\nPromo Permission: ";
      messageField += oneEntry.elementAt(11);
      
      return messageField;
   }


   // create a string that contains product representation of a vector
   // return blank if the Vector does not have enough data to make a product
   public String createProductString (Vector oneEntry) 
   {
      String messageField = new String();
     
      // return blank if vector doesn't have enough elements
      if (oneEntry.size() < 7)
      {
         return messageField;
      }

      messageField += "Reference Name: ";
      messageField += oneEntry.elementAt(0);
      messageField += "\nUPC: "; 
      messageField += oneEntry.elementAt(1);
      messageField += "\nSupplier: ";
      messageField += oneEntry.elementAt(2);
      messageField += "\nPrice: "; 
      messageField += oneEntry.elementAt(3);
      messageField += "\nAmount: "; 
      messageField += oneEntry.elementAt(4);
      messageField += "\nDelievery Time: ";
      messageField += oneEntry.elementAt(5);
      messageField += "\nCost: ";
      messageField += oneEntry.elementAt(6);
      
      return messageField;
   }


   // delete all the entries in the database
   public int deleteAllEntries()
   {
      data = new TreeSet (new BasicComparator());
		return 0;
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
      String s = new String(); 
  
      try 
      {
         if (dataFilename.equals("customer.txt"))
			{  
			   talkToServer ("c_srr_");
			}
			else if (dataFilename.equals("product.txt"))
			{
				talkToServer ("p_srr_");
			}

         // keep reading until eof()
         while ( ! (s.equals ("_F")))
	      {
				if (dataFilename.equals("customer.txt"))
				{  
				   s = talkToServer ( "c_rrr_");
				}
				else if (dataFilename.equals("product.txt"))
				{
					s = talkToServer ( "p_rrr_");
				}
            int value = this.addEntry (s);
         }
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
         Iterator x = data.iterator();

         if (dataFilename.equals("customer.txt"))
			{  
			   talkToServer ("c_sww_");
			}
			else if (dataFilename.equals("product.txt"))
			{
				talkToServer ("p_sww_");
			}

         // iterate through all the items in the list.
         while (x.hasNext())
         {
				if (dataFilename.equals("customer.txt"))
				{  
				   talkToServer ( "c_www_" + ((String) x.next()));
				}
				else if (dataFilename.equals("product.txt"))
				{
					talkToServer ( "p_www_" + ((String) x.next()));
				}
         }
			
			if (dataFilename.equals("customer.txt"))
			{  
			   talkToServer ("c_www__F");
			}
			else if (dataFilename.equals("product.txt"))
			{
				talkToServer ("p_www__F");
			}

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


   // talk to the server
   private static String talkToServer (String sentenceToServer)  
	{
	   try {
		   // get a datagram socket
         DatagramSocket socket = new DatagramSocket();

         // send request
         byte[] buf = new byte[256];

			// fix the sentenceToServer 
			for (int i = sentenceToServer.length(); i < buf.length; i++)
		      sentenceToServer += " ";
			
		   String s = new String (sentenceToServer);
		   buf = s.getBytes();
         //InetAddress address = InetAddress.getByName(args[0]);
		   InetAddress address = InetAddress.getLocalHost();
         DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
         //DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
         socket.send(packet);

         // get response
			buf = new byte[256];
         packet = new DatagramPacket(buf, buf.length);
         socket.receive(packet);

         // display response
         String received = new String(packet.getData());
         //String received = new String(packet.getData(), 0);

         socket.close();

			return received.trim();
		} 
		catch (Exception e)
		{
		   System.out.println ("Unable to open Socket");
		}
		
		return new String();
   }
   
}


