/* -------------------------------------------------------------------------
   Date: June 18, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #2
   ------------------------------------------------------------------------- */
#include "CIS245.h"

void Process_Args (args *arguments, int argv, char *argc []); 

void Process_Args (args *arguments, int argv, char *argc []) {
   int i = 0;
   
   arguments->newsgroup = NULL;
   arguments->toc = UNDEFINED;
   arguments->sort = UNDEFINED;
   arguments->article = UNDEFINED;
   arguments->thread = UNDEFINED;

      
   /* find "-g", and make sure there is a valid newsgroup following */
   if (i + 1 < argv) { /* make sure there is 'another' argument */
      i++; /* worry about next argument */

      /* !!!!! NEW LAST MINUTE STUFF */
      if (argc[i][0] != '.' && argc[i][0] != '/') {
         char *full_path;
         full_path = (char *) malloc (1024);
         getcwd (full_path, 1024);
         strcat (full_path, "/");
         strcat (full_path, argc[i]);
         Substitute (&full_path, ".", "/", 0, 1);
         arguments->newsgroup = full_path;
      } /* LAST MINUTE STUFF ENDS HERE (END THE ELSE IS "NULLIFIED") */

      else {
         char *temp; 
         temp = (char *) malloc (strlen (argc[i]) + 1);
         strcpy (temp, argc[i]);
         Substitute (&temp, ".", "/", 0, 1);
         arguments->newsgroup = temp;
      }
      /* ---- old ---- arguments->newsgroup = argc[i]; */
   } 

   else {
      printf ("You must specify the newsgroup name.\n");
      exit (-1);
   }
}
