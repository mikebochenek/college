/* ------------------------------------------------------------------------  
    Michael Bochenek
    ID: 0041056 (980492820)
    mboche01@uoguelph.ca
    CIS 242
    Assignment #3
    July 6, 1999
   ------------------------------------------------------------------------ */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define HASH_SIZE 15
#define MAX_ENTRY 10
#define KILOBYTE 1024

#define INSERT 1
#define SEARCH_ID 2
#define SEARCH_NAME 3
#define SELECT_EXIT 4

typedef struct student {
   int student_ID;
   char *first_name;
   char *last_name;
   char *name;
} Student;

/* ----- FUNCTION PROTOTYPES ------ */
void ReadFile (Student *hash_table, int *access_table);
int QuickSort (Student *hash_table, int *access_table);
int Partition (Student *hash_table, int *access_table, int low, int high);
int BinarySearch (Student *hash_table, int *access_table, char *name,
                  int elements);
int Hash (Student *hash_table, int student_ID);
void UserInteraction (Student *hash_table, int *access_table, int *elements);
void RecQuickSort (Student *hash_table, int *access_table, int low,
		   int high);
void PrintStuff (Student *hash_table, int *access_table);

/* ------------------------------- MAIN ------------------------------ */
int main () {
   int elements; /* number of elements currently in the tables */

   Student hash_table [HASH_SIZE]; /* HASH TABLE */
   int access_table [HASH_SIZE];   /* ACCESS TABLE */

   ReadFile (hash_table, access_table);

   elements = QuickSort (hash_table, access_table);

   UserInteraction (hash_table, access_table, &elements);

   return 0; /* bye-bye */
}

/* ----------------------------- READFILE ---------------------------- */
void ReadFile (Student *hash_table, int *access_table) {
   int student_ID;
   int i;
   int hash_index;

   char *token;
   char one_line[KILOBYTE];
   FILE *filename = NULL;

   /* initialize values in the two tables */
   for (i = 0; i < HASH_SIZE; i++) {
      hash_table[i].student_ID = -1;
      hash_table[i].first_name = NULL;
      hash_table[i].last_name = NULL;
      hash_table[i].name = NULL;

      access_table[i] = -1;
   }

   /* NEXT 5 LINES:  open the file, and check for errors */
   filename = fopen ("data3.dat", "r");
   if (filename == NULL) {
      printf("Error opening file\n");
      exit (-1);
   } /* open file data3.dat and check for 'fopen' errors */

   /* NEXT x LINES:  read all the words and close the file */
   for (i = 0; i < MAX_ENTRY; i++) {
      fgets (one_line, KILOBYTE, filename);
      /* read in a line */

      strtok (one_line, " ");
      student_ID = atoi (one_line);
      /* get the student id */

      hash_index = Hash (hash_table, student_ID);
      /* determine the HASH location according to student ID */

      hash_table[hash_index].student_ID = student_ID;
      /* insert student id into hash table */

      token = (char *) strtok (NULL, " ");
      /* get the first name */
      hash_table[hash_index].first_name =
	   (char *) malloc (strlen (token) + 1);
      strcpy (hash_table[hash_index].first_name, token);
      /* allocate memory for the first name, and copy it from the token */

      token = (char *) strtok (NULL, " \n");
      /* get the last name */
      hash_table[hash_index].last_name =
	   (char *) malloc (strlen (token) + 1);
      strcpy (hash_table[hash_index].last_name, token);
      /* allocate memory for the last name, and copy it from the token */

      /* NEXT couple of lines:  create last_name+first_name key for sorting */
      hash_table[hash_index].name = (char *) malloc
	    (strlen (hash_table[hash_index].last_name)
	   + strlen (hash_table[hash_index].first_name) + 1);
      /* allocate memory for the new key */
      strcpy (hash_table[hash_index].name,
	   hash_table[hash_index].last_name);
      strcat (hash_table[hash_index].name,
	   hash_table[hash_index].first_name);
      /* first copy the last name, and then concatentate the first name */
   }

   fclose (filename);
}

/* ----------------------------- QUICKSORT ---------------------------- */
int QuickSort (Student *hash_table, int *access_table) {
   int i;
   int j = 0;
   int elements;

   /* this for loop fills the access table BUT not in the correct order */
   for (i = 0; i < HASH_SIZE; i++) {

      /* look for "valid" entries in the hash table */ 
      while (hash_table[i].student_ID == -1 && i < HASH_SIZE) {
	 i++;
      }

      /* once a "valid" entry is found, link it to the access table
         by storing the hash-table index in the access table array */
      if (hash_table[i].student_ID != -1 && i < HASH_SIZE) {
	 access_table[j] = i;
	 j++;
      }
   }

   elements = j; /* number of "valid" entries found !!! */

   RecQuickSort (hash_table, access_table, 0, elements - 1);
   /* call to REAL quicksort function */

   return elements;
}

/* ---------------------------- RECURSIVE QUICKSORT ---------------------- */
void RecQuickSort (Student *hash_table, int *access_table, int low,
		   int high) {
   int pivotpos = 0;
   if (low < high) {
      pivotpos = Partition (hash_table, access_table, low, high);
      /* divide into two sublists */
      RecQuickSort (hash_table, access_table, low, pivotpos - 1);
      /* sort lower sublist */
      RecQuickSort (hash_table, access_table, pivotpos + 1, high);
      /* sort upper sublist */
   }
}

/* ------------------------------- PARTITION --------------------------- */
int Partition (Student *hash_table, int *access_table, int low, int high) {
   /* will not comment cuz it's straight from the book */
   int pivot;
   int i;
   int pivotpos;
   int temporary;

   /* SWAP */
   temporary = access_table[low];
   access_table[low] = access_table[(low + high) / 2];
   access_table[(low + high) / 2] = temporary;

   pivot = access_table[low];
   pivotpos = low;
   for (i = low + 1; i <= high; i++) {
      if (strcmp (hash_table[access_table[i]].name,
		  hash_table[pivot].name) < 0) {

	 /* SWAP */
	 pivotpos++;
	 temporary = access_table[i];
	 access_table[i] = access_table[pivotpos];
	 access_table[pivotpos] = temporary;

      }
   }

   /* SWAP */
   temporary = access_table[low];
   access_table[low] = access_table[pivotpos];
   access_table[pivotpos] = temporary;

   return pivotpos;
}

/* ------------------------------ BINARYSEARCH ------------------------ */
int BinarySearch (Student *hash_table, int *access_table, char *target,
                  int elements) {
   /* will not comment cuz it's straight from the book */
   int bottom;
   int middle;
   int top;

   top = elements - 1;
   bottom = 0;

   while (top >= bottom) {
      middle = (top + bottom) / 2;
      
      printf ("%d ", access_table[middle]); /* ?Gay */

      if (strcmp (hash_table[access_table[middle]].name, target) == 0) { 
         return access_table[middle];
      } 
      else if (strcmp (hash_table[access_table[middle]].name, target) > 0) {
         top = middle - 1;
      } 
      else {
          bottom = middle + 1;
      }
   }
 
   return -1;
}

/* ------------------------------- HASH --------------------------- */
int Hash (Student *hash_table, int student_ID) {
   int hash_index; /* this variables is returned */
   int probe_number = 0;

   /* HASH FUNCTION !!!!!! */
   hash_index = student_ID % HASH_SIZE;

   /* if studentID is not -1, then space is already taken and we have
      to look for another space by quadratic probing */
   while (hash_table[hash_index].student_ID != -1
       && probe_number < ( (HASH_SIZE + 1) / 2) ) {
      probe_number++;
      hash_index = hash_index + (2 * probe_number - 1);
      hash_index = hash_index % HASH_SIZE;
   }

   /* if probe_number == HASH_SIZE / 2 then table is FULL!!! */
   if (probe_number == (HASH_SIZE + 1) / 2) {
      printf ("Error, hash table is full\n");
      exit (-1);
   } /* exit the program with an error message */

   return hash_index;
}

/* ---------------------------- USERINTERACTION --------------------------- */
void UserInteraction (Student *hash_table, int *access_table, int *elements) {
   int selection;
   char user_input[KILOBYTE];

   do {
      PrintStuff (hash_table, access_table);
      /* print the two tables every single time */
      
      printf ("\n------- MENU -------\n");
      printf ("1.  Insert a new entry.\n");
      printf ("2.  Search with an ID number.\n");
      printf ("3.  Search with a name\n");
      printf ("4.  Exit\n");
      printf ("Enter you selection: ");

      fgets (user_input, KILOBYTE, stdin);
      selection = atoi (user_input);
      /* get the user input from the keyboard and convert it integer */

      if (selection == INSERT) {
         char one_line[KILOBYTE];
         int student_ID = 0;
         char *token;
         int hash_index;

         printf ("Please enter data (student_ID, first_name last_name): ");
         fgets (one_line, KILOBYTE, stdin); 
         /* prompt user for data and read from keyboard */

         /* ### START of cut and paste from ReadFile() */
         strtok (one_line, " ");
         student_ID = atoi (one_line);

         hash_index = Hash (hash_table, student_ID);

         hash_table[hash_index].student_ID = student_ID;

         token = (char *) strtok (NULL, " ");
         hash_table[hash_index].first_name =
            (char *) malloc (strlen (token) + 1);
         strcpy (hash_table[hash_index].first_name, token);

         token = (char *) strtok (NULL, " \n");
         hash_table[hash_index].last_name =
	      (char *) malloc (strlen (token) + 1);
         strcpy (hash_table[hash_index].last_name, token);

         hash_table[hash_index].name = (char *) malloc
	       (strlen (hash_table[hash_index].last_name)
	      + strlen (hash_table[hash_index].first_name) + 1);
         strcpy (hash_table[hash_index].name,
	      hash_table[hash_index].last_name);
         strcat (hash_table[hash_index].name,
	      hash_table[hash_index].first_name);
         /* ### END of cut and paste from ReadFile() */

         /* MAKE SURE elements and the access table get updated */
         *elements = QuickSort (hash_table, access_table);
      }

      else if (selection == SEARCH_ID) {
         int hash_index;
         int probe_number = 0;
         char user_search[KILOBYTE];
         int student_ID = 0;

         printf ("Please enter the student ID: ");
         fgets (user_search, KILOBYTE, stdin); 
         student_ID = atoi (user_search);
         /* prompt user for student ID, get it, and conver to integer */

         hash_index = student_ID % HASH_SIZE;
         /* do the hash function !!! */

         /* if studentID does not match, then keep probing */
         while (hash_table[hash_index].student_ID != student_ID 
                && probe_number < ( (HASH_SIZE + 1) / 2) ) {
            probe_number++;
            hash_index = hash_index + (2 * probe_number - 1);
            hash_index = hash_index % HASH_SIZE;
         }

         /* if probe_number == HASH_SIZE / 2 then NOT FOUND */
         if (probe_number == (HASH_SIZE + 1) / 2) {
            hash_index = -1;
            /* -- printf ("--> Unable to find \"%d\".\n", student_ID); -- */
         }

         /* ... otherwise print out what has been found */
         /* else { */
	    printf ("--> \"%d\" found at location %d.\n", student_ID, 
                    hash_index);
         /* } */
      }

      else if (selection == SEARCH_NAME) {
         char user_search[KILOBYTE];
         char *search_for;        
         char first[KILOBYTE];
         int i = 0;
 
         printf ("Please enter the name (first_name last_name): ");
         fgets (user_search, KILOBYTE, stdin); 
         /* prompt for names, get them, and REVERSE..... */         

         /* ### REVERSE PROCESS */
         search_for = (char *) malloc (strlen (user_search) + 1);
         user_search[strlen (user_search) - 1] = '\0';
         while (user_search[i] != ' ') {
            first[i] = user_search[i];
            i++;
         }
         first[i] = '\0';
         i++;
         strcpy (search_for, &user_search[i]);
         strcat (search_for, first);
         /* ### END OF REVERSE PROCESS */ 

         printf ("(Comparing: "); /* ?Gay */
         i = BinarySearch (hash_table, access_table, search_for, *elements);
         /* perform the binary search here */
         printf (")"); /* ?Gay */

	 printf ("--> \"%s\" found at location %d.\n", user_search, i);
         /* print out what was found */
      }

   } while (selection != SELECT_EXIT);
   /* keep loopint until the user enters '4' */

}

/* ------------------------ PRINT STUFF --------------------------- */
void PrintStuff (Student *hash_table, int *access_table) {
   int i;

   printf ("------- HASH TABLE -------\n");

   /* loop through all entries in the hash table */
   for (i = 0; i < HASH_SIZE; i++) {
      printf ("%2d ", i); /* always print the number */
      
      /* ... BUT print the other information only if it is there */
      if (hash_table[i].student_ID != -1) {
         printf ("%d ", hash_table[i].student_ID);
         printf ("%s ", hash_table[i].first_name);
         printf ("%s\n", hash_table[i].last_name);
      }
      /* if nothing is there, then print nothing */
      else {
         printf ("\n");
      }
   }

   printf ("------- ACCESS TABLE -------\n");

   /* print all entries in the access table */
   for (i = 0; i < HASH_SIZE && access_table[i] != -1; i++) {
      printf ("%d ", access_table[i]);
   }
}
