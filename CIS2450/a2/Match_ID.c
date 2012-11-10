/* -------------------------------------------------------------------------
   Date: June 18, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #2
   ------------------------------------------------------------------------- */
#include "CIS245.h"

void Match_ID (art *articles, int num_of_threads, int num_of_articles);

int CompareBY_DATE (const void *a, const void *b) {
   return ((art *) a)->time - ((art *) b)->time;
} 

void Match_ID (art *articles, int num_of_threads, int num_of_articles) {
   int i; /* used to traverse references */
   int j = 0; /* used to traverse message_id */

   /* first necessary to sort all the articles by date */
   qsort (articles, num_of_articles, sizeof (art), 
                             CompareBY_DATE);
 
   /* look for all articles with 0 references and ... */
   for (i = 0; i < num_of_articles; i++) {
      if (articles[i].references == 0) {
         j++;
         articles[i].thread_number = j; /* ... give 'em new thread number */
      }
   } 

/* --- regular "match_id" stuff, that's always been here ---- */
   for (i = 0; i < num_of_articles; i++) { /* loop through all articles */
      if (articles[i].reference_field != NULL) {
         /* do search/try to match only if references has something */

         for (j = 0; j < num_of_articles; j++) { /* all articles */
            /* try to find 'reference' in array of 'message_IDs' */
            if (strcmp (articles[i].reference_field, 
                        articles[j].message_id) == 0) {
               articles[i].thread_number = articles[j].thread_number;
               /* +++"link"+++ the two threads */

               articles[j].articles_in_thread++;
               /* NEW:: count number of articles in thread !!!!! */
            }
         }
      }
   }
   return;
}

