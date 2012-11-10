/* -------------------------------------------------------------------------
   Date: May 24, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #1
   FileName: File_Dir.c  
   Function: File_Dir - returns the full path name of the 'number'th readable
             entry in 'dname'. 
   Parameters: 'dname' is the directory name, and 'number' is the wanted file. 
   Return values: Pointer to char array containing full path name is returned.
                  If 'dname' is invalid or if number is negative or if for 
                  some other reason the directory cannot be opened, 
                  or if memory cannot be allocated, then NULL is returned.
   ------------------------------------------------------------------------- */
#include "CIS245.h"

char *File_Dir (char *dname, int number);

char *File_Dir (char *dname, int number) {
   char *dirname = NULL;
   int file_counter = 0; /* keeps track of opened files */

   DIR *directory; /* directory stream for 'dirname' */
   struct dirent *dir_entry; /* pointer to individual directory entry */   
   
   DIR *test_dir; /* used to test if directory entry is a directory itself */
   FILE *test; /* used to test if a file can be opened */
   
   if (number <= 0 || dname == NULL) {
      return NULL;
   } 
  
   /* open directory stream, and return -1 if 'opendir()' fails */
   directory = opendir (dname);
   if (directory == NULL) {
      return NULL;
   }
 
   dir_entry = readdir (directory); /* read first directory entry */
   /* only continue reading if valid entries are returned by 'readdir()' */
   while (dir_entry != NULL) { 
      /* as soon as '.' or '..' is hit, repeat 'readdir()', and start over */
      if (strcmp (".", dir_entry->d_name) == 0 ||
          strcmp ("..", dir_entry->d_name) == 0) {
         dir_entry = readdir (directory);
         continue;
      }

      /* try to open current entry as a directory.  If non-NULL value is
         returned, it means that it is a directory, and it should be skipped.
         Therefore, repeat 'readdir()', and start loop from start */      
      test_dir = opendir (dir_entry->d_name);
      if (test_dir != NULL) {
         closedir (test_dir);
         dir_entry = readdir (directory);
         continue;
      }
 
      chdir(dname); /* change into 'dirname' (cd dirname) */
      
      test = fopen (dir_entry->d_name, "r"); /* try to open file */
      if (test != NULL) { /* if not NULL, open was successful */
         file_counter++; /* keep track of successful opens */
         fclose (test);
      }
      
      if (file_counter == number) { /* when 'number'th file is reached... */
         dirname = malloc (1024); /* declare an array for dir */
         getcwd (dirname, 1024); /* get current working dir */

         /* only append the extra '/' if it is not already there */
         if (dirname [strlen (dirname) - 1] != '/') {
            dirname = strcat (dirname, "/");
         }

         strcat (dirname, dir_entry->d_name);
         /* add name-of-current-entry to 'full_path' */
         
         closedir (directory); /* close the open directory stream */
         return dirname; /* no need to go on... */         
      }
      dir_entry = readdir (directory); /* read another directory entry */
   }
   closedir (directory); /* close the open directory stream */

   return dirname;
}
