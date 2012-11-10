/* -------------------------------------------------------------------------
   Date: June 18, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #2
   ------------------------------------------------------------------------- */
#include "CIS245.h"

char *strptime (char *buff, const char *format, const struct tm *time);
int Get_Date (char *string);

int Get_Date (char *string) {
   struct tm *the_date; /* time structure */ 
   time_t the_time_sec; /* time in seconds integer - gets returned */  
 
   /* char temporary[80]; */

   int correction = 0;

   /* allocate memory for structure and make sure it works */
   the_date = (struct tm *) malloc (sizeof (struct tm));
   if (the_date == NULL) {
      Error ("Get_Date:  unable to allocate memory");
   } 

   if (string[3] == ',') { /* ++++ FIRST METHOD, "Thu, ...." */
      char *temp;

      temp = strptime (string, "%A, %e %h %Y %T", the_date); 
      /* -----------------     "Thu, 23 Jul 1999 21:36:43" --- 
         the strptime() function parses string and puts all relevant
         information into the_date structure.  It returns the part
         of the string where it LEFT OFF parseing */

      the_time_sec = mktime (the_date);
      /* convert the structure time into an integer (in seconds) */

      /* must take care of -+0400 */ 
      correction = atoi (temp); /* convert to integer */
      correction = correction * (60 * 60 / 100); /* convert to seconds */
      /* doesn't do -0430 exactly, actually there is an error, since
         it thinks that the '30' is really 3/10 hr (it should be 1/2 hr).
         It doesn't make any difference as long as everything is consistent */

      the_time_sec -= correction;
      /* if -0400 then you have to ADD, if +0400 then you SUBTRACT */

   }

   if (string[3] != ',') { /* +++++ SECOND METHOD, "May 24 ... " */
      strptime (string, "%e %h %Y %T", the_date);
      /* --------------- "Mar 24 1999 13:43:43" --- 
         the strptime() function parses string and puts all relevant
         information into the_date structure. */ 

      the_time_sec = mktime (the_date);
      /* convert the structure time into an integer (in seconds) */
   }

   /* the_time_sec = the_time_sec - (4 * 60 * 60);
      the fix - for GMT (we are 4 hours from GMT) */

   /* ++++++ a bizzare SOB fix that takes care of daylight savings time 
   strftime (temporary, 80, "%H", gmtime (&the_time_sec));
   if (atoi (temporary) != (the_date->tm_hour - (correction / 3600)) % 24) {
      the_time_sec = the_time_sec - (1 * 60 * 60);
   }  ++++++ */

   free (the_date); /* free time structure space */
   return (int) the_time_sec; 

}
