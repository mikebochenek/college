// Michael Bochenek (ID) 0041056
// CIS 3430 Assignment #1

import java.util.StringTokenizer;
import java.util.*;

public class BasicComparator implements Comparator { 
   public int compare (Object a1, Object a2)
	{
	   // create two Tokenizers, one for each string (casted object)
		StringTokenizer t1 = new StringTokenizer ((String) a1, ";");
		StringTokenizer t2 = new StringTokenizer ((String) a2, ";");

      // if Tokenizers do not return anything, pretend they are equal
		// (since any other returned value would not make any sense)
      if ( !(t1.hasMoreTokens() && t2.hasMoreTokens()))
		{
		   return 0;
		}

      // get the surname from each Tokenizer
	   String name1 = new String (t1.nextToken());
		String name2 = new String (t2.nextToken());
		
		// convert both surnames to lowercase
		name1 = name1.toLowerCase();
		name2 = name2.toLowerCase();

      // actually perform string comparison
      int compareValue = name1.compareTo (name2);
      
      //
      // if last names are the same, then compare first names...
      //
      if (compareValue == 0 && t1.hasMoreTokens() && t2.hasMoreTokens())
      {
		   // get the first names
         name1 = new String (t1.nextToken());
         name2 = new String (t2.nextToken());

         // convert both names to lowercase
		   name1 = name1.toLowerCase();
		   name2 = name2.toLowerCase();
		   
			// actually compare the two names
		   compareValue = name1.compareTo (name2);
		   
		   //
		   // if first names are the same, then compare e-mail addresses
		   //
         if (compareValue == 0 && t1.hasMoreTokens() && t2.hasMoreTokens())
         {
			   // get the email addresses
            name1 = new String (t1.nextToken());
            name2 = new String (t2.nextToken());

            // convert email addresses to lowercase
   		   name1 = name1.toLowerCase();
	   	   name2 = name2.toLowerCase();
		   
			   // actually perform the comparison
		      compareValue = name1.compareTo (name2);
		   }
		}
		
		// whichever happens to be the final comparison, the value
		// is stored in compareValue, and this value is returned.
      return compareValue;
   }
}
