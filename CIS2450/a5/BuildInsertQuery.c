/* -------------------------------------------------------------------------
   Date: August 3, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #5
   ------------------------------------------------------------------------- */
#include "CIS245.h"

char *BuildInsertQuery (art *articles, int i, char *database_name);

char *BuildInsertQuery (art *articles, int i, char *database_name) {
   char *query;
   char threadposition[8] = "";
   char lines[8] = "";
   char *subject;

   query = (char *) malloc (512);
   query[0] = '\0';

   sprintf (threadposition, "%d", articles[i].references + 1);
   sprintf (lines, "%d", articles[i].lines);   

   subject = (char *) malloc (strlen (articles[i].subject) + 1);
   strcpy (subject, articles[i].subject);
   if (strstr (articles[i].subject, "'") != NULL) {
      Substitute (&subject, "'", "\\'", 1, 1);
   }

   strcat (query, "insert into ");
   strcat (query, database_name);
   strcat (query, " values ('" );
   strcat (query, articles[i].filename);
   strcat (query, "', ");
   strcat (query, threadposition);
   strcat (query, ", '");
   strcat (query, articles[i].message_id);
   strcat (query, "', '");
   strcat (query, articles[i].reference_field);
   strcat (query, "', '");
   strcat (query, articles[i].date_string);
   strcat (query, "', '");
   strcat (query, subject);
   strcat (query, "', '");
   strcat (query, articles[i].from);
   strcat (query, "', ");
   strcat (query, lines);
   strcat (query, ")");

   return query;
}
