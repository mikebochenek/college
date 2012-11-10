/* -------------------------------------------------------------------------
   Date: June 18, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #2
   ------------------------------------------------------------------------- */
#include "CIS245.h"

void Process_Args (args *arguments, int argv, char *argc []); 

void Process_Args (args *arguments, int argv, char *argc []) {
   int i;
   
   arguments->newsgroup = NULL;
   arguments->toc = UNDEFINED;
   arguments->sort = UNDEFINED;
   arguments->article = UNDEFINED;
   arguments->thread = UNDEFINED;

   for (i = 1; i < argv; i++) { /* loop through all args in command line */
      
      /* find "-g", and make sure there is a valid newsgroup following */
      if (strcmp (argc[i], "-g") == 0) {
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
            Error ("Process_Args:  -g must be followed by the newsgroup");
         }
      }

      /* find "-toc", and set the flag */
      else if (strcmp (argc[i], "-toc") == 0) {
         arguments->toc = TRUE;
      }

      /* find "-s", and set the flag accordingly (0, 1, 2) */
      else if (strcmp (argc[i], "-s") == 0) {
         if (i + 1 < argv) {
            int j;

            char *temp; /* copy of 'sort' technique in uppercase */
            temp = (char *) malloc (strlen (argc[i + 1]) + 1);

            i++; /* worry about next argument */
            /* convert to all uppercase */ 
            for (j = 0; argc[i][j] != '\0'; j++) {
               temp[j] = toupper (argc[i][j]);
            }
            temp[j] = '\0'; /* seems to be necessary */

            if (strcmp (temp, "DATE") == 0) {
               arguments->sort = BY_DATE; /* find date and set flag */
            }
            else if (strcmp (temp, "SUBJECT") == 0) {
               arguments->sort = BY_SUBJECT; /* find subject and set flag */
            }
            else if (strcmp (temp, "FROM") == 0) {
               arguments->sort = BY_FROM; /* find from and set flag */
            }
            else {
               Error ("Process_Args:  -s must be followed by sort method");
            }
            
            free (temp);
         }
         else {
            Error ("Process_Args:  -s must be followed by sort method"); 
         }
      }

      /* find the "-thread" and store the the thread number */
      else if (strcmp (argc[i], "-thread") == 0) {
         if (i + 1 < argv) {
            i++; /* deal with next argument */
            arguments->thread = atoi (argc[i]);
            if (arguments->thread <= 0) {
               Error ("Process_Args:  -thread must be followed by a number");
            }
         }
         else {
            Error ("Process_Args:  -thread must be followed by a number");
         }
      }

      /* find the "-article" and store the article number */
      else if (strcmp (argc[i], "-article") == 0) {
         if (i + 1 < argv) {
            i++; /* deal with next argument */
            arguments->article = atoi (argc[i]);
            if (arguments->article <= 0) {
               Error ("Process_Args:  -article must be followed by number");
            }
         }
         else {
            Error ("Process_Args:  -article must be followed by a number");
         }
      }

      /* !!!!! if not matched then something is wrong */
      else {
         Error ("USAGE_ERROR");
      }

   } /* end of big for loop */

   Verify_Args (arguments); /* call MY FUNCTION */
}
