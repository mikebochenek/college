/*  Michael Bochenek
    ID: 0041056 (980492820)
    mboche01@uoguelph.ca
    CIS 242
    Assignment #2
    June 9, 1999

    This program reads in exactly 360 words from the file data2.dat
    and then performs a SHELL sort.  At that point the user is asked
    to enter a word that he/she wants to search for.  The program uses
    a binary search to find the word.  It reports the index at which
    the word was found, and the number of key comparisons that were
    required to find it (or determine that it could not be found).  
    It also reports the number of element moves that were made when
    the words were sorted.  This number does not change, since the list is
    sorted only once.  The user is asked to enter '-q' to quit.
*/

#include <stdio.h>
#include <stdlib.h>

#define QUIT_STRING "-q"
#define MAX_WORDS 360          /* number of words in the file */
#define MAX_CHARS_IN_WORD 32   /* maxi length of word, for sake of simplicity */

/* ----- FUNCTION PROTOTYPES ------ */
void ShellSort (char list [MAX_WORDS] [MAX_CHARS_IN_WORD]); 
void SortInterval (int start, int increment, 
                   char word [MAX_WORDS] [MAX_CHARS_IN_WORD]);  
int Binary1Search (char list [MAX_WORDS] [MAX_CHARS_IN_WORD],
                   char target [MAX_CHARS_IN_WORD] );

/* ----- MACRO DEFINITIONS ------ */
#define EQ(A, B) ( !strcmp((A), (B)) )
#define LE(A, B) ( strcmp ((A), (B)) <= 0 )
#define LT(A, B) ( strcmp ((A), (B)) < 0 )

/* ---- GLOBAL VARIABLES FOR STATISTICS ----- */
int Num_Of_Moves = 0;
int Num_Of_Key_Comp = 0;

/* ------------------------------- MAIN ------------------------------ */
int main () {
   int i;
   FILE *filename = NULL;

   char user_input[1024];
   /* string to store what the user enters */

   char word [MAX_WORDS] [MAX_CHARS_IN_WORD];  
   /* array of strings with all the 360 words */

   /* NEXT 5 LINES:  open the file, and check for errors */
   filename = fopen ("data2.dat", "r");
   if (filename == NULL) {
      printf("Error opening file\n");
      return 0;
   } /* open file data2.dat and check for 'fopen' errors */

   /* NEXT 4 LINES:  read all the words and close the file */
   for (i = 0; i < MAX_WORDS; i++) {
      fscanf (filename, "%s", &word[i][0] );
   }
   fclose (filename);

   /* +++ PERFORM SHELL SORT +++ */
   ShellSort (word);

   do {
      printf ("\nEnter a word to search for, or '%s' to quit: ", QUIT_STRING);
      scanf ("%s", user_input);
      /* print prompt and get user input */

      if (strcmp (user_input, QUIT_STRING) != 0) {
         /* perform binary search */
         i = Binary1Search (word, user_input);

         printf ("# OF MOVES IN SORT = %d\n", Num_Of_Moves);
         printf ("# OF KEY COMPARISONS IN SEARCH = %d\n", Num_Of_Key_Comp);
         /* ============ THIS LINE WAS USED TO TEST 5 CASES:
            printf ("%d\n", Num_Of_Key_Comp); ============== */
         Num_Of_Key_Comp = 0;
         /* print out statistics */

         if (i == -1) {
            printf ("Sorry, %s not found\n", user_input);
         } /* display message that string was not found */
         else {
            printf ("%s was found at position %d.\n", user_input, i);
         } /* display message that string was found */
      }
   } while (strcmp (user_input, QUIT_STRING) != 0);
   /* continue looping until user enters '-q' */

   for (i = 0; i < MAX_WORDS; i++) {
      printf ("%s\n", &word[i][0] );
   }
   return 0; /* bye-bye */
}

/* -------------------------- SHELL SORT -----------------------------
   --> this is the shell sort from the textbook pg 295 */
void ShellSort (char list [MAX_WORDS] [MAX_CHARS_IN_WORD]) {
   int increment = MAX_WORDS;
   int start = 0;
   do {
      /* update the increment */
      increment = increment / 3 + 1;
      for (start = 0; start < increment; start++) {
         /* sort the interval in 'increments' */
         SortInterval (start, increment, list);
      }
   } while (increment > 1);
}

/* -------------------------- INSERTION SORT ----------------------------
   --> this is the insertion sort (slightly modified) from pg. 283 */ 
void SortInterval (int start, int increment,
                   char list [MAX_WORDS] [MAX_CHARS_IN_WORD]) {
   int fu;
   int place;
   char current [MAX_CHARS_IN_WORD];
   /* temp variable to hold a word */

   for (fu = start+increment; fu < MAX_WORDS; fu = fu + increment) { 
      if ( LT (list[fu], list[fu-increment]) ) {
         /* Num_Of_Moves++; <--- took it out cuz it assigns to temp */
         strcpy (current, list[fu]); /* assign to temp */
         for (place = fu - increment; place >= start; place -= increment) {
            Num_Of_Moves++;
            strcpy (list[place + increment], list[place]);
            /* keep shifting until appropriate element is found */
            if (place == start || LE (list[place - increment], current))
               break; /* ALSO account for looping to 'first' element */
         } 
         Num_Of_Moves++;
         strcpy (list[place], current); /* assign temp to right place */
      }
   }
}
 
/* ----------------------------- BINARY SEARCH 1 ------------------------
   --> this is the binary search from the textbook pg 251
   ------------ Binary search is exactly like the one in the book--------
   -----------  thus no need to explain/comment it --------------- */
int Binary1Search (char list [MAX_WORDS] [MAX_CHARS_IN_WORD],
                   char target [MAX_CHARS_IN_WORD] ) {
   int buttom, middle, top;
   top = MAX_WORDS - 1;
   buttom = 0;
   while (top > buttom) {
      middle = (top + buttom) / 2;
      Num_Of_Key_Comp++;
      if ( LT (list[middle], target)) {
         buttom = middle + 1;
      }
      else {
         top = middle;
      }
   }

   if (top == -1)
      return -1;
 
   Num_Of_Key_Comp++;
   if ( EQ (list[top], target))
      return top;
   else 
      return -1;
   return -1;
} 


