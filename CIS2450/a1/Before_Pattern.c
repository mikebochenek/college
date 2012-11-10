/* -------------------------------------------------------------------------
   Date: May 24, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #1
   FileName: Before_Pattern.c 
   Function: Before_Pattern - returns string with all characters before the 
             index (exclusive) position inside of 'string'
   Parameters: 'string' must be a valid pointer and 'start' an integer
   Return values: Returns the identified string.  If 'string' is not a valid
                  pointer, or if 'start' is negative, or if 'start' is
                  greater than the lenght of 'string', or if 'malloc()'
                  fails for some reason, then return NULL.  
   ------------------------------------------------------------------------- */
#include "CIS245.h"

char *Before_Pattern (char *string, int start);

char *Before_Pattern (char *string, int start) {
   char *cut_pattern; /* pointer will point to identified string */

   /* next 3 lines:  check for invalid parameters, 'string' must be a valid
      pointer, 'start' must be less than the lenght of 'string', and 'string'
      must be positive */
   if (string == NULL || start > strlen(string) || start < 0) {
      return NULL;
   }

   cut_pattern = (char *) malloc (start + 1);
   /* allocate memory for 'start' characters */
   if (cut_pattern == NULL) {
      return NULL;
   } /* if malloc fails then return NULL */

   cut_pattern = strncpy (cut_pattern, string, start);
   /* copy 'start' characters from beginning of string */

   return cut_pattern;
}
