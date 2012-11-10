/* -------------------------------------------------------------------------
   Date: June 18, 1999
   By: Michael Bochenek (980492820)
   For: CIS-245 Assignment #2
   ------------------------------------------------------------------------- */
#include "CIS245.h"

int Num_Files (char *dirname);

int Num_Files (char *dirname) {
   int number_of_files = 0; /* returned as the number of files in 'dirname' */
   DIR *directory; /* directory stream for 'dirname' */
   struct dirent *dir_entry; /* pointer to invididual directory entry */

   DIR *test_dir; /* used to test if directory entry is a directory itself */
   FILE *test_file; /* used to test if a file can be opened */

   /* check for invalid arguments:  'dirname' must point to something */
   if (dirname == NULL) {
      return -1;
   }

   /* open directory stream, and return -1 if 'opendir()' fails */
   directory = opendir (dirname);
   if (directory == NULL) {
      return -1;
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
 
      chdir(dirname); /* change into 'dirname' (cd dirname) */
      
      test_file = fopen (dir_entry->d_name, "r"); /* try to open file */
      if (test_file != NULL) { /* if not NULL, open was successful */

         number_of_files++; /* keep track of successful opens */
         fclose (test_file);
      }
      dir_entry = readdir (directory); /* read another directory entry */
   } 
   closedir (directory); /* close the open directory stream */
   
   return number_of_files;
}
