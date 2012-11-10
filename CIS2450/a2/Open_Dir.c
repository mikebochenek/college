/* -------------------------------------------------------------------------
   Date: June 18, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #2
   ------------------------------------------------------------------------- */
#include "CIS245.h"

int Open_Dir (args *arguments, DIR **directory); 

int Open_Dir (args *arguments, DIR **directory) {
   int x = 0;

   /* get the number of files */
   x = (Num_Files (arguments->newsgroup));

   if (x == -1) {
      Error ("Open_Dir:  invalid newsgroup name was specified");
   }

   /* keep directory open */
   *directory = opendir (arguments->newsgroup);

   if (*directory == NULL) {
      Error ("Open_Dir:  unable to open the directory");
   }

   return x;
}
