/* -------------------------------------------------------------------------
   Date: June 18, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #2
   ------------------------------------------------------------------------- */
#include "CIS245.h"

int Find_First (char *string, char *pattern);

int Find_First (char *string, char *pattern) {
   int position; /* integer position that is returned */
   char *found;

   /* find the pattern inside the string */
   found = strstr (string, pattern);
   
   /* if not found exit right away */
   if (found == NULL) {
      return -1;
   }

   /* determine the 'integer' position in the string' */
   position = strlen (string) - strlen (found);
   if (position == 0) {
      /* return 0, only if the string is found right at the beginning */
      return position;
   }

   return -1;
}
