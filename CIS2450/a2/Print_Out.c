/* -------------------------------------------------------------------------
   Date: June 18, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #2
   ------------------------------------------------------------------------- */
#include "CIS245.h"

/* int strcasecmp (const char *a, const char *b); */

void Print_Out (args *arguments, art *articles, int num_of_articles,
                int num_of_threads);

int Compare_BY_FROM (const void *a, const void *b) {
   return strcmp (((art *) a)->from, ((art *) b)->from);
}

int Compare_BY_SUBJECT (const void *a, const void *b) {
   return strcmp (((art *) a)->subject, ((art *) b)->subject);
} 

void Print_Out (args *arguments, art *articles, int num_of_articles,
                int num_of_threads) {
   int i = 0;

   /* -------------- PRINT OUT MAIN -TOC --------------- */
   if (arguments->thread == UNDEFINED && arguments->article == UNDEFINED) {

      /* if by_subject was specified than must sort again... */
      if (arguments->sort == BY_SUBJECT) {
         qsort (articles, num_of_articles, sizeof (art), 
                                   Compare_BY_SUBJECT);
      }
 
      /* if by_from was specified than must sort again... */
      if (arguments->sort == BY_FROM) {
         qsort (articles, num_of_articles, sizeof (art), 
                                   Compare_BY_FROM);
      }

      /* if by_date was specified, not necessary to sort again... */

      Print_All_Threads (articles, num_of_articles);
      /* call my cool function */
   }

   /* ---------------- PRINT OUT -TOC FOR A PARTICULAR THREAD -------- */
   if (arguments->thread != UNDEFINED && arguments->article == UNDEFINED) {
      int art_in_thread = num_of_articles;
 
      /* if by_subject was specified than must sort again... */
      if (arguments->sort == BY_SUBJECT) {
         qsort (articles, art_in_thread, sizeof (art), 
                                   Compare_BY_SUBJECT);
      }
 
      /* if by_from was specified than must sort again... */
      if (arguments->sort == BY_FROM) {
         qsort (articles, art_in_thread, sizeof (art), 
                                   Compare_BY_FROM);
      }
      
      /* if by_date was specified, not necessary to sort again... */

      Print_Thread_Articles (articles, art_in_thread, arguments->thread);
      /* PROTEND that all the articles are the articles in the thread
         and inside the function worry about which articles to print.
         Thus the need to pass the USER-SPECIFIED thread# */
   }

   /* ---------------- PRINT OUT A PARTICULAR ARTICLE ----------- */
   if (arguments->thread != UNDEFINED && arguments->article != UNDEFINED) {
      
      int found = 0;
      /* counter of printed articles */

      for (i = 0; i < num_of_articles; i++) {
         if (articles[i].thread_number == arguments->thread) {
            if (articles[i].references + 1 == arguments->article) {
               FILE *file;
               char *filepath;
               char *line;

               found++; /* !!!!!! increment the printed articles counter */

               line = (char *) malloc (LENGTH);

               /* get the proper path name */
               filepath = (char *) File_Dir (arguments->newsgroup, 
                                             articles[i].file_number);
               
               /* open the file for reading */
               file = fopen (filepath, "r");

               line = (char *) fgets (line, LENGTH, file);
               /* read a line of stuff */              
 
               while ( (!feof (file)) && (line[0] != '\n') ) { 
                  line = (char *) fgets (line, LENGTH, file);
               } /* ++++++ traverse all header info */

               line = (char *) fgets (line, LENGTH, file);
               /* read a line of stuff */              

               /* keep reading and printing until fgets() reads a NULL */
               while (line != NULL) { 
                  printf ("%s", line);
                  line = (char *) fgets (line, LENGTH, file);
               } 

               free (filepath);
               free (line);
            }
         }
      }
      
      /* if nothing was printed display error */
      if (found == 0) {
         Error ("Print_Out:  Invalid thread# and articles#.");
      }
       
   }
}
