/* -------------------------------------------------------------------------
   Date: August 3, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #5
   ------------------------------------------------------------------------- */
#include "CIS245.h"

void BuildDataBase (art *articles, int num_of_articles, char *newsgroup);

int error (char *msg) {
   printf ("%s\n",msg);
   exit (1);
}

void clrstr (char *buf) {
   int x = 0;

   for (x = 0; x < MAX_QUERY; x++) {
      buf[x] = '\0';
   }	
}

void BuildDataBase (art *articles, int num_of_articles, char *newsgroup) {
   MYSQL mysql, *con;
   MYSQL_RES *res;
   MYSQL_ROW row;
   char query[MAX_QUERY];
   int x;
   char *table_name;

   table_name = (char *) malloc (1024);
   strcpy (table_name, newsgroup);
   Substitute (&table_name, ".", "", 1, 1);
   Substitute (&table_name, "/", "", 1, 1);

   /* printf ("(((%s))))", table_name); */

   /* ---- Connect to database server. ---- */
   if( !(con = mysql_connect(&mysql, HOSTNAME, USERNAME,PASSWORD)) ) {
      error("Could not connect.");
   }
	
   /* ---- Select DataBase. ---- */
   if (mysql_select_db(con, DATABASE)){
      error("Could not select DataBase.");
   }

   /* ---- Drop the students table ----<<<<<<<<<<<<<<<<<< DELETE */
   strcpy (query, "drop table ");
   strcat (query, table_name);
	
   if(mysql_query(con,query)) {
   }

   /* ----- Form Query for creating table --- */
   clrstr (query);
   strcat (query, "create table ");
   strcat (query, table_name);
   strcat (query, " (");
   strcat (query, "filename char(10),");
   strcat (query, "threadposn int,");
   strcat (query, "messageid char(100) not null,");
   strcat (query, "reference char(100),");
   strcat (query, "date char(40),");
   strcat (query, "subject char(100),");
   strcat (query, "msg_from char(100),");
   strcat (query, "nlines int,");
   strcat (query, "primary key(messageid) )");
	
   /* ----- Create table. ----- */
   if(mysql_query(con, query)){
      error("could not create table");
   }
	
   /* ----- insert records into the table --- */
   for (x = 0; x < num_of_articles; x++) {
      clrstr(query);
      if (articles[x].reference_field == NULL) {
         strcat (query, BuildInsertQuerySpecial (articles, x, table_name));
      } else {
         strcat (query, BuildInsertQuery (articles, x, table_name));
      }

      if (mysql_query (con, query)) {
         printf ("Could not insert record!!!! ");
         printf ("Failure to insert: %s\n", query);
      }
   }
	
   /* ---- Let us look at what we inserted ---- */
   clrstr(query);
   strcpy(query, "select * from ");
   strcat(query, table_name);
   strcat(query, " where threadposn = 1 order by messageid");
	
   if(mysql_query(con, query)) {
      error("! select 1");
   }	

   /* ---- Store results from query into res structure. ---- */
   if (!(res = mysql_store_result(con))){
      error("! store 1");
   }
   while ((row = mysql_fetch_row(res))) {
      printf("%s %s %s %s\n", row[2], row[4], row[5], row[6]);
   }

   /* ---- Drop the students table ---- */
   strcpy (query, "drop table ");
   strcat (query, table_name);

   /* ================== dropping table ===================== 	
   if(mysql_query(con,query)) {
      error("! drop 1");
   }
   =================== dropping table ===================== */
	
   /* ---- Finally close connection to server ---- */
   mysql_close(con);
}
