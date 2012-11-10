/* -------------------------------------------------------------------------
   Date: May 24, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #1
   FileName: Find_Pattern.c 
   Function: Find_Pattern = finds 'pattern' inside of 'string'.  Note the 
             flag 'casesensitive'.
   Parameters: 'string' and 'pattern' must be valid char pointers and
               'casesensitive' can only be 1 or 0. 
   Return values: Returns the integer position of string found.  IF there
                  is any type of error -1 is returned.  
   ------------------------------------------------------------------------- */
#include "CIS245.h"

int Find_Pattern (char *string, char *pattern, int casesensitive);

int Find_Pattern (char *string, char *pattern, int casesensitive) {
   int i;
   int position; /* integer position that is returned */
   int string_start = 0;
   int string_end = 0;
   char *found;
   char *new_pattern; /* copies of pattern (uppercase) */
   char *new_string; /* copy of string (uppercase) */
 
   /* test for invalid parameters */
   if ( ! (casesensitive == 1 || casesensitive == 0) 
        || (string == NULL) || (pattern == NULL) ) {
      return -1;
   }   
 
   /* check FOR *** ^$ ***    */
   if (strcmp (pattern, "^$") == 0) {
      if (strlen (string) == 0) { 
         return 0;
      } else {
         return -1; 
      }
   }

   /* check for ***  $ and ^  ***   */
   if ( (strcmp (pattern, "$") == 0) || (strcmp (pattern, "^") == 0) 
        || (strcmp (pattern, "") == 0) ) {
      return 0;
   }

   /* check for $^ */
   if (strcmp (pattern, "$^") == 0) {
      return -1;
   } 

   /* malloc and copy the strings */
   new_pattern = (char *) malloc (strlen(pattern) + 1);
   new_pattern = strcpy (new_pattern, pattern);
   new_string = (char *) malloc (strlen(string) + 1);
   new_string = strcpy (new_string, string);
   
   /* if necessary convert everything to uppercase */
   if (casesensitive == 1) {
      for (i = 0; string[i] != '\0'; i++) {
         new_string[i] = toupper (string[i]);
      }
      for (i = 0; pattern[i] != '\0'; i++) {
         new_pattern[i] = toupper (pattern[i]);
      }
   }
   
   /* check for carot flag */
   if (pattern[0] == '^') {
      string_start = 1;
   }

   /* check for dollar flag */
   if (pattern[ strlen(pattern) - 1 ] == '$') {
      if (pattern[ strlen(pattern) - 2 ] != '\\' ) {
         string_end = 1;
      }
   }

   /* remove the flags as necessary */
   if (string_start && string_end) {
      new_pattern = strncpy (new_pattern, &pattern[1], strlen(pattern) - 2);
      new_pattern[ strlen(pattern) - 2 ] = '\0';
   } 
   if (string_start && !string_end) {
      new_pattern = strncpy (new_pattern, &pattern[1], strlen(pattern) - 1);
      new_pattern[ strlen(pattern) - 1 ] = '\0';
   } 
   if (!string_start && string_end) {
      new_pattern = strncpy (new_pattern, pattern, strlen(pattern) - 1);
      new_pattern[ strlen(pattern) - 1 ] = '\0';
   } 
   if (!string_start && !string_end) {
      new_pattern = strcpy (new_pattern, pattern);
   }

   /* interpret the string, ie remove the '\'s */
   for (i = 0; new_pattern[i] != '\0'; i++) {
      if (new_pattern[i] == '\\') {
         if (new_pattern[i + 1] == '^' ||
             new_pattern[i + 1] == '$' ||
             new_pattern[i + 1] == '\\') {
                /* remove backslash */
                strcpy (&new_pattern[i], &new_pattern[i + 1]);
                i++;
         } 
         else {
            return -1;
         }
      }
   }

   /* find the pattern inside the string */
   found = strstr (new_string, new_pattern);
   
   /* if not found exit right away */
   if (found == NULL) {
      return -1;
   }

   /* determine if the position should be returned or not */
   else {
      position = strlen (new_string) - strlen (found);
      if (string_start && string_end && (position == 0)
           && (strlen (new_string) == strlen (new_pattern) ) ) {
         return position;
      }
      if (string_start && !string_end && (position == 0)) {
         return position;
      }
      if (!string_start && string_end && 
          ( position == ( strlen (new_string) - strlen (new_pattern) ) ) ) {
         return position;
      }
      if (!string_start && !string_end) {
         return position;
      }
   }

   return -1;
}
