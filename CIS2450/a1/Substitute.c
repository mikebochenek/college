/* -------------------------------------------------------------------------
   Date: May 24, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #1
   FileName: Substitute.c  
   Function: Substitute - substitutes occurances of b_pattern with a_pattern
             within string.  Two flags (casesensitive and globalsub) 
             specify how to do the substitutions 
   Parameters:  'string', 'b_pattern', and 'a_pattern' must all point to 
                valid character arrays.  Note that 'string' is a double
                pointer.  casesensitive and globalsub must be either 1 or 0.
   Return values:  Return the number of substitutions made.  If there is an 
                   error of any type, then -1 is returned. 
   ------------------------------------------------------------------------- */
#include "CIS245.h"

int Substitute (char **string, char *b_pattern, char *a_pattern,
                int casesensitive, int globalsub);

int Substitute (char **string, char *b_pattern, char *a_pattern,
                int casesensitive, int globalsub) {
   int position = 0; /* position of b_string within 'string' */
   int i;
   int difference = 0; /* stringlenght (a) - stringlenght (b) */
   int substitutions = 0; /* this value is RETURNED */
   char *new_string; /* copy of 'string' */
   char *new_b_pattern; /* copy of 'new_b_pattern' */
   char *found = *string; /* pointer to b_string within 'string' */
   char *temp_string = *string;

   position = -1 * strlen (a_pattern); /* ummm..... */

   /* test for invalid parameters.  casesensitive and globalsub must both
      be either 1 or 0.  'string', 'b_pattern' and 'a_pattern' must
      all be valid char pointers */
   if ( (!(casesensitive == 1 || casesensitive == 0)) ||
        (!(globalsub == 1 || globalsub == 0)) ||
        (*string == NULL) || (b_pattern == NULL) || (a_pattern == NULL) ) {
      return -1;
   }

   /* for each of the 3 strings, malloc enough memory and then copy
      the contents of 'string' (or 'b_pattern') into the new string */
   new_string = (char *) malloc (strlen(*string) + 1);
   new_string = strcpy (new_string, *string);
   temp_string = (char *) malloc (strlen(*string) + 1);
   temp_string = strcpy (temp_string, *string);
   new_b_pattern = (char *) malloc (strlen(b_pattern) + 1);
   new_b_pattern = strcpy (new_b_pattern, b_pattern);
   
   /* if caseinsensitive, then convert all chars in new_b_pattern to upperc */
   if (casesensitive == 1) {
      for (i = 0; new_b_pattern[i] != '\0'; i++) {
         new_b_pattern[i] = toupper (new_b_pattern[i]);
      }
   }

   difference = strlen (a_pattern) - strlen (b_pattern);
   /* determine the difference in stringlenght */   

   do { /* --- MAIN LOOP STARTS HERE --- */
      new_string = realloc (new_string, 1 + strlen(*string) );
      if (new_string == NULL) {
         free (new_string);
         free (temp_string);
         free (new_b_pattern);
         return -1; /* realloc fails */
      }
      new_string = strcpy (new_string, *string);
      /* copy the "new" 'string' into 'new_string' */ 

      if (casesensitive == 1) { /* if necessary convert all chars to upperc */
         for (i = 0; new_string[i] != '\0'; i++) {
            new_string[i] = toupper (new_string[i]);
         }
      }

      /* find 'b_pattern' within 'new_string', except for start searching
         at position + lenght of a_pattern */
      found = strstr (&new_string[position + strlen (a_pattern)], 
                      new_b_pattern);
      if (found == NULL) {
         continue; /* if nothing is found, go to condition part of loop */
      }
      
      position = strlen (new_string) - strlen (found);
      /* determine the actual integer position of 'found' */

      strcpy (found, &((*string)[position]) );
      /* found previously pointed to all uppercase, now points to original */

      substitutions++;

      if (difference >= 0) {
      /* necessary to realloc more memory */
         *string = realloc (*string, (difference) + 1 + strlen (*string)); 
         if (*string == NULL) {
            free (new_string);
            free (temp_string);
            free (new_b_pattern);
            return -1;
         }  

         /* next two lines do the actual substitution */
         strcpy ( &( (*string) [position + difference]), found);
         for (i = 0; a_pattern[i] != '\0'; i++) {
            (*string) [position + i] = a_pattern[i];
         }  
      } 
      else {
      /* not necessary to realloc more memory */
         /* next three lines do the actual substitution */
         strcpy ( &( (*string) [position]), a_pattern);
         (*string) [position + strlen (a_pattern)] = '\0';
         strcat (*string, (& ( (*string)[position + strlen(new_b_pattern)])));
      } 
   } while (found != NULL && globalsub == 1);
   /* continue looping until "not found" or execute only once if global==0 */
   
   free (new_string);
   free (temp_string);
   free (new_b_pattern);
   return substitutions; /* return number of subs made */
}
