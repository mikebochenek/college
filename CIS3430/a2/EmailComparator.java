// Michael Bochenek (ID) 0041056
// CIS 3430 Assignment #1

import java.util.StringTokenizer;
import java.util.*;

public class EmailComparator implements Comparator { 
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

      // get the e-mail from each Tokenizer
      String name1 = new String (t1.nextToken());
      String name2 = new String (t2.nextToken());
      
      // convert both e-mails to lowercase
      name1 = name1.toLowerCase();
      name2 = name2.toLowerCase();

      // actually perform string comparison
      int compareValue = name1.compareTo (name2);
       
      //
      // if last names are the same, then compare first names...
      //
      if (compareValue == 0 && t1.hasMoreTokens() && t2.hasMoreTokens())
      {
         // get the passwords
         name1 = new String (t1.nextToken());
         name2 = new String (t2.nextToken());

         // convert both passwords to lowercase
         name1 = name1.toLowerCase();
         name2 = name2.toLowerCase();

         // actually compare the two names
         compareValue = name1.compareTo (name2);
		}
         
      // whichever happens to be the final comparison, the value
      // is stored in compareValue, and this value is returned.
      return compareValue;
   }
}
