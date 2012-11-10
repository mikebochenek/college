#include <stdio.h>  
#define MAXSTUDENTS 2048  /* maximum number of students in class */
#define FIELDS 4
#define ID     0
#define MARK   1
#define ASTRIX 2
#define RANK   3

/* --- FUNCTION PROTOTYPES --- */ 
int readFile(char[], int[][FIELDS]);  /* each is explained in detail later... */
int inputStillOK(FILE *);
void rankAllStudents(int, int[][FIELDS]);
void printMenu(void); 
int getUserInput(void); 
void listAllStudents(int, int[][FIELDS]); 
void printBest(int, int[][FIELDS]); 
void printWorst(int, int[][FIELDS]); 
void printHistogram(int, int[][FIELDS]); 

/* --- MAIN --- */
int main(int argc, char *argv[]) { 
   int userInput = 0, numberOfStudents; 
   int allStudents[MAXSTUDENTS][FIELDS] = {{0}};  /* array that stores all the 
     data for each student.  For each student (up to MAXSTUDENTS] 
     1st field stores student ID, 2nd field  stores final mark, 3rd field 
     stores if student failed exams (-1) or not (0), 4th field stores rank */
   
   numberOfStudents = readFile(argv[1], allStudents);  
   /* read data from file specified as first command-line argument 
      (or read from stdin if there is an error reading from file) */ 
   if (numberOfStudents == -1) return 0;
   /* -1 is returned by this function if there is any type of fatal error
      and thus the program ends if -1 is returned */
 
   while (userInput != 5) { /* loop until user selects option '5' */ 
      printMenu(); /* print menu to screen */
      userInput = getUserInput(); /* get user input and verify its correct */ 
      if (userInput == 1) 
         listAllStudents(numberOfStudents, allStudents); 
      else if (userInput == 2) 
         printBest(numberOfStudents, allStudents); 
      else if (userInput == 3) 
         printWorst(numberOfStudents, allStudents); 
      else if (userInput == 4) 
         printHistogram(numberOfStudents, allStudents); 
      /* call appropriate function (ie depending on user input) */ 
   } 
 
   return 0; /* bye-bye */
} 
 
/* --- FUNCTIONS --- */ 
int readFile(char filename[], int students[][FIELDS]) { 
   /* this function is passed a string (filename) and a pointer to the
      array that stores the student information.  NOTE the array allStudents
      is MODIFIED inside this function */
   
   int i, assignment, counter; /* temp-type variables */
   int numberOfStudents, numberOfAssignments, assignmentWeight; 
   int midterm, final, midtermWeight, finalWeight; 
   FILE * inputSource; /* pointer to input from where data comes */
 
   inputSource = fopen(filename, "r"); 
   /* open file for reading */ 
 
   if (inputSource == NULL) { /* if error during file opening */ 
      fprintf(stderr, "\nError opening file: \n%s ", filename); 
      fprintf(stderr, "\nEnter marks manually:\n");
      /* if error during opening then output error message */ 
      inputSource = stdin; /* and set input pointer to stdin */
   } /* if there is no error input pointer still points to the file */ 

   /* read 'header' information from the input */
   if (!inputStillOK(inputSource)) return -1; /* check for EOF */
   fscanf(inputSource, "%d", &numberOfStudents);
   if (!inputStillOK(inputSource)) return -1; /* check for EOF */
   fscanf(inputSource, "%d", &numberOfAssignments); 
   if (!inputStillOK(inputSource)) return -1; /* check for EOF */
   fscanf(inputSource, "%d", &assignmentWeight); 
   if (!inputStillOK(inputSource)) return -1; /* check for EOF */
   fscanf(inputSource, "%d", &midtermWeight); 
   if (!inputStillOK(inputSource)) return -1; /* check for EOF */
   fscanf(inputSource, "%d", &finalWeight); 

   if ((assignmentWeight + midtermWeight + finalWeight) != 100) {
      fprintf(stderr, "\nThe mark weights do not add up to 100");
      fprintf(stderr, "\nExiting program....\n");
      return -1;
   } /* if the mark weights do not add up to 100 print an error
        and return to main error code (-1) */
 
   for (counter = 0; counter < numberOfStudents; counter++) { 
   /* for each student read (1) student ID  &  (2) student grade */ 
      if (!inputStillOK(inputSource)) return -1; /* check for EOF */
      fscanf(inputSource, "%d", &students[counter][ID]);
      /* read the student ID */ 
 
      for (i = 1; i <= numberOfAssignments; i++) { 
         if (!inputStillOK(inputSource)) return -1; /* check for EOF */
	 fscanf(inputSource, "%d", &assignment); 
	 students[counter][MARK] = students[counter][MARK] + assignment; 
      } /* read all the assignments and total them in marks array */ 
 
      if (!inputStillOK(inputSource)) return -1; /* check for EOF */
      fscanf(inputSource, "%d", &midterm); /* read midterm mark */ 
      if (!inputStillOK(inputSource)) return -1; /* check for EOF */
      fscanf(inputSource, "%d", &final); /* read final mark */ 
 
      students[counter][MARK] = 
         students[counter][MARK] / numberOfAssignments * assignmentWeight / 100
	 + midterm * midtermWeight / 100 
	 + final * finalWeight / 100; 
	 /* calculate overall mark according to assignment, midterm, 
	    final weight and store in mark array */ 

      if (students[counter][MARK] > 100) /* do not allow a mark above 100% */
         students[counter][MARK] = 100;
      if (students[counter][MARK] < 0) /* do not allow a mark below 0% */
         students[counter][MARK] = 0;

      if ((midterm * midtermWeight + final * finalWeight) 
       / (midtermWeight + finalWeight) < 50) {
         students[counter][ASTRIX] = -1; /* if student fails exam portion */
      }  /* then note this in the array */
      
      rankAllStudents(numberOfStudents, students); /* rank the students */
   } 
   if (inputSource != stdin) fclose(inputSource); /* close input file */
   return numberOfStudents; 
} 

int inputStillOK(FILE * inputSource) {
/* called by readFile before each scanf statement.  Function is passed
   a file pointer and checks for EOF */
   if (feof(inputSource)) { /* output error message */
      fprintf(stderr, "\nEnd-Of-File: Data file may be corrupt!!!\n");
      fprintf(stderr, "a) Number of students specified doesn't");
      fprintf(stderr, " equal to number of entries\n");
      fprintf(stderr, "b) At least one of the students is missing");
      fprintf(stderr, " a mark for assignments/midterm/final\n");
      fprintf(stderr, "Exiting program....\n");

      return 0; /* return 0 if EOF */
   }
   return 1; /* return 1 if not EOF */
}

void rankAllStudents(int numOfStudents, int students[][FIELDS]) { 
/* this function is called from within readFile, and is passed
   number of students and the student array */
   
   int mark, j, currentRank = 1, atLeastOneMatch = 0;
   
   /* nested 'for' tries to find student with a 'mark' of 100, 99, 98, etc.
      the first student(s) found is assigned a rank of 1.  After going
      through all students, rank is incremented (if at least one match). */
   for (mark = 100; mark > 0; mark--) {
      for (j = 0; j < numOfStudents; j++) { /* loop through all students */
         if (students[j][MARK] == mark) { 
            students[j][RANK] = currentRank;
            atLeastOneMatch = 1; /* if student 'i' has a mark equal */
         }  /* to 'mark' then give him/her a rank equal to currentRank 
               and note that at least one match has been found */
      }
      if (atLeastOneMatch == 1) currentRank++;
      /* increment 'rank' only if at least one match has been found */
      atLeastOneMatch = 0; /* reset for next 'mark' */
   }
   return; 
}
  
void printMenu(void) { /* output MENU to the screen */ 
   printf("\n");
   printf("1.  [L]ist all students along with their mark and class ranking\n"); 
   printf("2.  Determine student with the [B]est grade\n"); 
   printf("3.  Determine student with the [W]orst grade\n"); 
   printf("4.  [P]rint a histogram of the final grades\n"); 
   printf("5.  [E]nd the program\n"); 
   return; 
} 
 
int getUserInput(void) { 
   char userInput, nextChar; 
 
   printf("\nEnter your selection: "); /* prompt user for input */ 
 
   userInput = getchar(); /* store first char entered in variable */ 
   nextChar = userInput; /* NOTE: the rest of the input is ignored */ 
   while (nextChar != '\n') { /* (ie, all chars and '\n' after 1st char */ 
      nextChar = getchar(); 
   } 
 
   if (userInput == '1' || userInput == 'l' || userInput == 'L') 
      userInput = 1; /* if user enters 1 or l or L then return '1' */ 
   else if (userInput == '2' || userInput == 'b' || userInput == 'B') 
      userInput = 2; /* if user enters 2 or b or B then return '2' */ 
   else if (userInput == '3' || userInput == 'w' || userInput == 'W') 
      userInput = 3; /* if user enters 3 or w or W then return '3' */ 
   else if (userInput == '4' || userInput == 'p' || userInput == 'P') 
      userInput = 4; /* if user enters 4 or p or P then return '4' */ 
   else if (userInput == '5' || userInput == 'e' || userInput == 'E') 
      userInput = 5; /* if user enters 5 or e or E then return '5' */ 
 
   return userInput; 
} 
 
void listAllStudents(int numOfStudents, int students[][FIELDS]) { 
   int j; 
 
   for (j = 0; j < numOfStudents; j++) { /* loop through all students */

      /* print id, mark, a star (only if failed exam portion), and rank */
      printf("Student ID: %9d  Mark: %3d", students[j][ID], students[j][MARK]); 
      if (students[j][ASTRIX] == -1) /* print a star if necessary */
         printf(" * ");
      else
         printf("   ");
      printf("  Rank: %d\n", students[j][RANK]);
      
      if ( ((j + 1) % 24 == 0 && j != 0) 
        || (j == (numOfStudents - 1) && numOfStudents / 15 > 1)) {
         /* every 24 students pause or before menu is printed if there is 
            more than 15 students (prevents students 1-9 being 'pushed'
            off the screen by the menu */
         char nextChar;  
         printf("Press 'Enter' to continue: "); /* prompt for 'enter' */
         nextChar = getchar(); /* get character from keyboard */
         while (nextChar != '\n') { 
            nextChar = getchar(); /* ignore all characters entered */
         } 
      } 
   } 
 
   return; 
} 
 
void printBest(int numOfStudents, int students[][FIELDS]) { 
   int i; 
   int best = 0; /* temporary var stores best student's index */ 
 
   for (i = 0; i < numOfStudents; i++) { /* loop through all students */ 
      if (students[best][MARK] < students[i][MARK]) 
      best = i; /* if someone is better than best, update index */ 
   } 
   printf("\n\nStudent(s) with best mark\n"); 
 
   /* NOTE; the following makes sure that if two (or more) students have 
      the same mark, they both get printed */ 
   for (i = 0; i < numOfStudents; i++) { /* loop through all students */ 
      if (students[best][MARK] == students[i][MARK]) 
         printf("Student ID: %9d  Mark: %3d\n", students[i][ID], 
                                                students[i][MARK]); 
   }     /* if best is equal to i then print i */ 
 
   printf("\n\n"); 
   return; 
} 
   
void printWorst(int numOfStudents, int students[][FIELDS]) { 
   int i; 
   int worst = 0; /* temporary var stores worst student's index */ 
 
   for (i = 0; i < numOfStudents; i++) { /* loop through all students */ 
      if (students[worst][MARK] > students[i][MARK]) 
	 worst = i; /* if somone is worse than worst, update index */ 
   } 
   printf("\n\nStudent(s) with worst mark\n"); 
 
   /* NOTE; the following makes sure that if two students have 
      the same mark, they both get printed */ 
   for (i = 0; i < numOfStudents; i++) { /* loop through all students */ 
      if (students[worst][MARK] == students[i][MARK]) 
         printf("Student ID: %9d  Mark: %3d\n", students[i][ID], 
                                                students[i][MARK]); 
   }     /* if worst is equal to i then print i */ 
 
   printf("\n\n"); 
   return; 
} 
 
void printHistogram(int numOfStudents, int students[][FIELDS]) { 
   int i, j, scale = 1; 
   int counter[10] = {0}; /* array stores number of students in each range
      (there are ten mark ranges, 0-9, 10-19, ..., 80-89, and 90-100) */

   for (i = 0; i < numOfStudents; i++) { /* loop through all students */
      counter[students[i][MARK] / 10] ++;
      if (students[i][MARK] == 100) counter[9]++; 
      /* count the number of students in each mark range:  for a student
         with 45 increment the 5th [4] element of the array (45/10=4).
         Students with a mark of 100 are placed in 10th group [9] */
   } 

   /* this 'for' adjusts the scale if necessary: if too many students
      have achieved marks in the same mark range.  The 67 is the number
      of X's that can fit on the screen (80 - labels on the side) */
   for (i = 0; i < 10; i++) { /* loop through all mark groups */
      if (counter[i] / 67 > 0) { 
         scale = counter[i] / 67 + 1; /* if more than 67 adjust scale */
         for (j = 0; j < 10; j++) { 
            if (counter[j] > 0 && counter[j] / ((counter[i] / 67) + 1) < 1)
               counter[j] = 1;
               /* if there is at least one person in that range print
                  one X to show this (only done when adjusting) */
            else 
               counter[j] = counter[j] / ((counter[i] / 67) + 1);
               /* adjust the number of students in each mark range */
         } 
      } 
   } 

   printf("\n  marks:\n"); 
   printf("        +-------------------------------------"); 
   printf("-------------------------------+\n"); 
   /* print the top of the graph */
 
   for (i = 0; i < 10; i++) { /* loop through all mark ranges */
      int lowerBound = i * 10;
      int upperBound = i * 10 + 9;
      if (upperBound == 99) upperBound = 100; 
      printf("%3d-%3d | ", lowerBound, upperBound); /* print labels on side */
      for (j = 0; j < counter[i]; j++)
         /* print an X for 'each' student that has a mark in this range
            ('each' - meaning after this number has been scaled down) */
         printf("X"); 
      for (j = 13 + counter[i]; j < 80; j++) 
         /* print spaces until the last character in column */
         printf(" "); 
 
      printf("|\n"); /* print right side of box */
   } 
 

   printf("        +-----------------------------------"); 
   printf("---------------------------------+"); 
   printf("\n          ");          
   /* print the bottom of the graph */
   
   for (i = 1; i < 7; i++) {
       printf("         |");
   } /* makin' it look pretty */

   printf("\n students ");          
   for (i = 1; i < 7; i++) {
       printf("%10d", scale * i * 10);
   } /* print the scale on the bottom */

   printf("\n"); 
   return; 
} 
   
