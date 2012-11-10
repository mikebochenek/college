/* -------------------------------------------------------------------------
   Date: June 18, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #2
   FileName: CIS245.h
   Comments: Include file for all library functions.
   ------------------------------------------------------------------------- */
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>
#include <unistd.h>
#include <dirent.h>
#include <sys/types.h>
#include <time.h>
#include <mysql/mysql.h>

#define MAX_QUERY 512
#define HOSTNAME "softeng.cis.uoguelph.ca"
#define USERNAME "cs1790"
#define PASSWORD "mboche01"
#define DATABASE "cs1790"

#define FALSE 0
#define TRUE 1

#define LENGTH 1024

#define UNDEFINED -1

#define BY_DATE 1
#define BY_SUBJECT 2
#define BY_FROM 3

typedef struct args_structure {
  char *newsgroup;
  int toc;
  int sort;
  int article;
  int thread;
} args;

typedef struct art_structure {
   int thread_number;
   int time;
   int lines;
   int references;
   int articles_in_thread;
   int file_number;
   char *filename;
   char *reference_field;
   char *message_id;
   char *from;
   char *subject;
   char *date_string;
} art;

char *After_Pattern (char *string, int end);
void Error (char *error_string);
char *File_Dir (char *dname, int number);
int Find_First (char *string, char *pattern);
int Get_Date (char *string);
void Match_ID (art *articles, int num_of_threads, int num_of_articles);
int Num_Files (char *dirname);
int Open_Dir (args *arguments, DIR **directory); 
void Print_Out (args *arguments, art *articles, int num_of_articles,
                int num_of_threads);
void Process_Args (args *arguments, int argv, char *argc []); 
int Process_Articles (args *arguments, art *articles, DIR *directory,
                       int *num_of_articles); 
int Substitute (char **string, char *b_pattern, char *a_pattern,
                int casesensitive, int globalsub);
void Verify_Args (args *arguments);
char *fgetline (char *buffer, int maxsize, FILE *fi);
void Print_All_Threads (const art *selected_articles, int num_of_articles);
void Print_Thread_Articles (const art *selected_articles, int art_in_thread,
                            int desired_thread);
void BuildDataBase (art *articles, int num_of_articles, char *newsgroup);
char *BuildInsertQuery (art *articles, int i, char *database_name);
char *BuildInsertQuerySpecial (art *articles, int i, char *database_name);
