/* -------------------------------------------------------------------------
   Date: May 24, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #1
   FileName: Pattern.c  
   Function: Pattern - returns string (starting at 'start' and ending at 'end')
             that is within 'string'.
   Parameters: 'string' must be a pointer to an array of characters.  'start'
               and 'end' must be both positive integers.
   Return values: Return a pointer to the identified string.  If 'string' is
                  an invalid pointer, or if 'start' or 'end' are negative, or
                  if 'end' is greater than the length of 'string, or 
                  if 'start' is greater than 'end', or if 'malloc' fails for
                  some reason, then return NULL.  
   ------------------------------------------------------------------------- */
#include "CIS245.h"

char *Pattern (char *string, int start, int end);

char *Pattern (char *string, int start, int end) {
   char *cut_pattern; /* pointer will point to identified string */
   
   /* next 3 lines:  check for invalid parameters, 'string' must point to
      something, 'start' must be positive, 'end' must be less than the lenght 
      of 'string', and 'start' must be less than 'end'. */
   if (string == NULL || start < 0 || end > strlen(string) || start > end) {
      return NULL;
   }   
   
   cut_pattern = (char *) malloc (end - start + 1 + 1);
   /* allocate memory, (end-start+1) equals to lenght of new string and
      (+1) allocates memory for '\0' */
   if (cut_pattern == NULL) {
      return NULL;
   } /* if malloc fails, then return NULL */

   cut_pattern = strncpy (cut_pattern, &string[start], end - start + 1);
   /* copy (end-start+1) characters from 'string' starting at 'start' */

   return cut_pattern;
}
