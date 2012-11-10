/* -------------------------------------------------------------------------
   Date: June 18, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #2
   ------------------------------------------------------------------------- */
#include "CIS245.h"

void Print_Thread_Articles (const art *selected_articles, int art_in_thread,
                            int desired_thread);

void Print_Thread_Articles (const art *selected_articles, int art_in_thread,
                            int desired_thread) {
   int i; 
   int found = 0;

   /* go through all the articles, NOT just art-in-thread */ 
   for (i = 0; i < art_in_thread; i++) {

      /* *** BUT only the onces with the desired thread# number get printed */
      if (selected_articles[i].thread_number == desired_thread) {

         found++;
         /* increment found counter */ 

         /* get and print the REFERENCES field */
         printf ("%d ", selected_articles[i].references + 1);
      
         /* get and print the FROM field */
         printf ("%s ", selected_articles[i].from);
      
         /* get and print the SUBJECT field */
         printf ("%s ", selected_articles[i].subject);
      
         /* get and print the TIME field */
         printf ("%s ", selected_articles[i].date_string);

         /* get the number of LINES and print it */
         printf ("%d\n", selected_articles[i].lines);
      }
   }
   
   /* if nothing was printed, then incorrect thread# */
   if (found == 0) {
      Error ("Print_Thread_Articles:  no such thread.");
   }
}
