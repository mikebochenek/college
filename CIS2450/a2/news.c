/* -------------------------------------------------------------------------
   Date: June 18, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #2
   ------------------------------------------------------------------------- */
#include "CIS245.h"

int main(int argv, char *argc []) {
   int num_of_articles = 0;
   int num_of_threads = 0;

   args arguments;
   DIR *directory;
   art *articles;
   int i;   

   i = 0;
   Process_Args (&arguments, argv, argc); 
   
   num_of_articles = Open_Dir (&arguments, &directory); 

   articles = (art *) malloc (sizeof (art) * num_of_articles);
   if (articles == NULL) {
      Error ("main: Unable to allocate memory");
   }

   num_of_threads = Process_Articles (&arguments, articles, directory, 
                                      &num_of_articles); 

   Print_Out (&arguments, articles, num_of_articles, num_of_threads);

   closedir (directory);
   return 0;
}
