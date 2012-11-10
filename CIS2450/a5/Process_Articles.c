/* -------------------------------------------------------------------------
   Date: June 18, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #2
   ------------------------------------------------------------------------- */
#include "CIS245.h"

int Process_Articles (args *arguments, art *articles, DIR *directory,
                       int *num_of_articles); 

int Process_Articles (args *arguments, art *articles, DIR *directory,
                       int *num_of_articles) {
   int thread = 1; /* keep track of number of threads - this is returned */
   int i; /* loop through all articles/files in directory */
   int jj;
   int pos;
   int file_num = 0;

   FILE *article_file; /* file pointer to current articles */
   char *filename; /* using File_Dir() get the full path name */
   char line[LENGTH]; /* this is used to read one line from a file */

   /* initialize all the values in the 'big' array and ALSO init
      the two arrays for reference and message IDs */
   for (i = 0; i < *num_of_articles; i++) {
      articles[i].reference_field = NULL;
      articles[i].message_id = NULL;
      articles[i].thread_number = UNDEFINED;
      articles[i].from = NULL;
      articles[i].subject = NULL;
      articles[i].time = UNDEFINED;
      articles[i].lines = UNDEFINED;
      articles[i].references = UNDEFINED;
      articles[i].articles_in_thread = 1;
      articles[i].file_number = UNDEFINED;
      articles[i].date_string = NULL;
   }

   /* MAIN PROCESSING LOOP */
   for (i = 1; i <= *num_of_articles; i++) { /* for all files in dir */

      file_num++;

      /* get exact path/filename using File_Dir */
      filename = (char *) File_Dir (arguments->newsgroup, file_num);

      /* printf ("{%s}.{%s}\n", filename, arguments->newsgroup); */
      /* !!!!!!!!!!!!!!!!!!!! DEBUG ONLY !!!!!!!!!!!!!! */

      /* _-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_ this code gets filename */
      articles[i - 1].filename = (char *) malloc (512);
      for (jj = strlen (filename); jj > 0; jj--) {
         if (filename[jj] == '/') {
            strcpy (articles[i - 1].filename, &filename[jj + 1]);
            jj = 0;
            /* printf ("{%s}, {%s}\n", filename, &filename[jj + 1]); */
         }
      }

      /* open the file, and check for error */
      article_file = fopen (filename, "r");
      if (article_file == NULL) {
         Error ("Process_Articles:  unable to open the file");
      }
       
      articles[i - 1].file_number = file_num;

      do { /* LOOP TROUGH THE ENTIRE HEADER */
         fgetline(line, LENGTH, article_file);

         /* find ++++++++ MESSAGE ID ++++++++ in the current line */
         pos = Find_First (line, "Message-ID: ");
         if (pos == 0) {
            /* message id follows "Message-ID: " */
            articles[i - 1].message_id = After_Pattern (line,
                                                   strlen ("Message-ID:"));
         }

         /* find ++ FROM ++ in the current line */
         pos = Find_First (line, "From: ");
         if (pos == 0) {
            /* from follows "From: " */
            articles[i - 1].from = After_Pattern (line, strlen ("From:")); 
         }

         /* find ++ SUBJECT ++ in the current line */
         pos = Find_First (line, "Subject: ");
         if (pos == 0) {
            /* subject follows "Subject: " */
            articles[i - 1].subject = After_Pattern (line,strlen ("Subject:")); 
         }

         /* find ++ LINES ++ in the current line */
         pos = Find_First (line, "Lines: ");
         if (pos == 0) {
            /* lines follows "Lines: " , convert to an integer */
            char *temp;
            temp = After_Pattern (line, strlen ("Lines:"));  
            articles[i - 1].lines = atoi (temp); 
            free (temp);
         }

         /* find ++ DATE ++ in the current line */
         pos = Find_First (line, "Date: ");
         if (pos == 0) {
            /* time is a string after "Date: ", convert it to seconds */

            int the_time; /* integer to hold the time in seconds */
            char *temp; /* string used before conversion is made */

            /* using After_Pattern() get string, and convert using Get_Date */
            temp = After_Pattern (line, strlen ("Date:"));

            /* !!!!!! NEW STUFF;  store the date_string in main structure */
            articles[i - 1].date_string = (char *) malloc (strlen (temp) + 1);
            if (articles[i - 1].date_string == NULL) {
               Error ("Process_Articles: unable to allocate memory.");
            }

            strcpy (articles[i - 1].date_string, temp);
            /* end of new stuff */ 

            the_time = Get_Date (temp);
 
            free (temp); /* free temporary malloced by After_Pattern */
            
            articles[i - 1].time = the_time; 
         }

         /* find ++ REFERENCES ++ in the current line */
         pos = Find_First (line, "References: ");
         if (pos == 0) {
            /* reference field goes right after "References: " */

            int one_char = UNDEFINED; /* read one char at a time */
            int counter = 0; /* count the number of ">" */

            char temp[LENGTH]; /* these 4 are used in the NEXT 7 LINES */
            char temporary[LENGTH];
            char *first_reference;
            char *a;

            /* next 7 LINES:  make a copy of line, and find the first "<"
               inside of it.  Make a copy of it (seg fault otherwise) and
               use strtok() to get everything up to the next whitespace.
               Copy this selected string into a malloced space called 
               (first_reference). */
            strcpy (temporary, line);
            a = strstr (temporary, "<");
            strcpy (temp, a);
            strtok (temp, " ");
            first_reference = (char *) malloc (strlen (temp) + 1);
            strcpy (first_reference, temp);

            articles[i - 1].reference_field = first_reference; 

            
            /* position the file pointer, where it was before this line
               was read it, done because this line could be really long */
            fseek (article_file, -1 * strlen (line), SEEK_CUR); 

            do {  /* keep looping until '\n' */ 
               one_char = fgetc (article_file);
               if (one_char == '>') {
                  counter++; /* keep track of the number of ">" */
               }
            } while (one_char != '\n');

            /* ++++++++++ number of ">" is the number OF REFERENCES */
            articles[i - 1].references = counter;
         }

      } while ( (strcmp (line, "") != 0) && (!feof(article_file)) ); 
      /* keep reaing in lines until a blank line is encountered */

      /* watch for non-email type files -> all e-mail type files must
         have at least a from field, or subject, or lines field. */
      if (articles[i - 1].from == NULL ||
          articles[i - 1].message_id == NULL ||
          articles[i - 1].date_string == NULL ||
          articles[i - 1].time == UNDEFINED || 
          articles[i - 1].lines == UNDEFINED) {

         (*num_of_articles)--; /* decrease total number of files int */
         i--; /* in main array of information, 'mark for deletion */
      }
       
      /* if there is no references, then the message must start of thread */   
      if (articles[i - 1].references == UNDEFINED) {
         articles[i - 1].thread_number = thread;
         articles[i - 1].references = 0;
         thread++; /* increment thread counter */
      }
   } /* MAIN FOR LOOP ENDS HERE <---- */

   /* call function to give a thread number to each article that is not the
      first in a thread.  This MUST be done after all the message_id
      fields have been read into the array. */
   Match_ID (articles, thread - 1, *num_of_articles);

   /* thread counter is 'prepered' for next possible new thread in the loop */
   return thread - 1; 
}
 
