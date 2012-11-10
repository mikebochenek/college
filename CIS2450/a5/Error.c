/* -------------------------------------------------------------------------
   Date: June 18, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #2
   ------------------------------------------------------------------------- */
#include "CIS245.h"

void Error (char *error_string);

void Error (char *error_string) {
   
   /* if no errorstring is specified, exit with "unspecified error" */
   if (error_string == NULL) {
      fprintf (stderr, "Unspecified Error\n");
   }
   
   /* if USAGE_ERROR, then print general error on command line usage */
   fprintf (stderr, "\n**************************************************");
   fprintf (stderr, "\nInvalid Arguments were passed.\n");
   fprintf (stderr, "USAGE:  dbnews _newsgroupname_\n");
   fprintf (stderr, "**************************************************\n");

   fprintf (stderr, error_string); 
   /* print the string passed to the function */

   fprintf (stderr, "\nExiting...\n");
   exit (-1); /* quit execution!!!!!! */
}
 
