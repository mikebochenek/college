/* -------------------------------------------------------------------------
   Date: June 18, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #2
   ------------------------------------------------------------------------- */
#include "CIS245.h"

void Print_All_Threads (const art *selected_articles, int num_articles);

void Print_All_Threads (const art *selected_articles, int num_articles) {
   int i;

   /* go through all the articles */
   for (i = 0; i < num_articles; i++) {
      /* *** BUT only proceed for articles with no references */
      if (selected_articles[i].references == 0) {

         /* get the THREAD_NUMBER and print it */
         printf ("%d ", selected_articles[i].thread_number);
      
         /* get the FROM field and print it */
         printf ("%s ", selected_articles[i].from);
      
         /* get the SUBJECT field and print it */
         printf ("%s ", selected_articles[i].subject);
      
         /* get the TIME field and print it */  
         printf ("%s ", selected_articles[i].date_string);

         /* get the NUMBER_OF_LINES field and print it */
         printf ("%d ", selected_articles[i].lines);
      
         /* print the NUMBER_OF_ARTICLES_IN_THREAD */
         printf ("%d\n", (selected_articles[i].articles_in_thread - 1));

      } 
   }
}
