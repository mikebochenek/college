/* -------------------------------------------------------------------------
   Date: June 18, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #2
   ------------------------------------------------------------------------- */
#include "CIS245.h"

void Verify_Args (args *arguments);

void Verify_Args (args *arguments) {

   /* if '-toc' is specified, '-article' cannot be requested */
   if (arguments->toc == TRUE) {
      if (arguments->article != UNDEFINED) {
         Error ("Verify_Args:  -toc and -article cannot be used together");
      } 
   }

   /* if '-article' is specified, '-s' cannot be requested */
   if (arguments->sort != -1) {
      if (arguments->article != UNDEFINED) {
         Error ("Verify_Args:  -s and -articles cannot be used together");
      }
   }  

   /* if '-article' is specified, '-thread' must be specified */
   if (arguments->article != UNDEFINED) {
      if (arguments->thread == UNDEFINED) {
         Error ("Verify_Args:  -thread must be specified if -article is");
      }
   }

   /* make sure that a newsgroup name is specified */
   if (arguments->newsgroup == NULL) {
      Error ("Verify_Args:  newsgroup must be specifed");
   }

   /* set the default '-s' to sort-BY_DATE */
   if (arguments->sort == UNDEFINED) {
      arguments->sort = BY_DATE;
   }
}
