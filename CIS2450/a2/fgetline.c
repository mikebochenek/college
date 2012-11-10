/* -------------------------------------------------------------------------
   Date: June 18, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #2
   ------------------------------------------------------------------------- */
#include "CIS245.h"

char *fgetline (char *buffer, int maxsize, FILE *fi);

char *fgetline (char *buffer, int maxsize, FILE *fi) {
   /* next 3 lines:  test for invalid parameters, 'fi' and 'buffer' must
      be valid pointers, and maxsize must positive */
   if (fi == NULL || buffer == NULL || maxsize < 0) { 
      return NULL;
   }

   buffer = fgets (buffer, maxsize, fi);
   /* read a line from file using fgets */
   
   if (buffer == NULL) { /* if fgets fails, return NULL */
      return NULL;
   }
   
   /* next 3 lines: if last character of buffer is a newline character, 
      delete it by changing it to string termination character */
   if ( buffer [strlen (buffer) - 1] == '\n') {
      buffer [strlen (buffer) - 1] = '\0';
   }

   return buffer; /* return (modified) line read by fgets */
}
