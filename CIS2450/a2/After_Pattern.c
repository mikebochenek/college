/* -------------------------------------------------------------------------
   Date: May 24, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #1
   ------------------------------------------------------------------------- */
#include "CIS245.h"

char *After_Pattern (char *string, int end);

char *After_Pattern (char *string, int end) {
   char *cut_pattern; /* pointer will point to identified string */

   /* next 3 lines:  check for invalid parameters.  'string' must be a valid
      pointer, 'end' must be less than the length of 'string' and 'end'
      must be positive */
   if (string == NULL || end > strlen(string) || end < -1) {
      return NULL;
   }

   cut_pattern = (char *) malloc (strlen(string) - end + 1);
   /* allocate memory - (length - endposition) is the size (+1 for '\0') */
   if (cut_pattern == NULL) {
      return NULL;
   } /* if malloc fails, then return NULL */

   cut_pattern = strncpy (cut_pattern, &string[end + 1], strlen(string) - end);
   /* copy (lenght - endpostion) characters starting at (end + 1) */

   return cut_pattern;
}
