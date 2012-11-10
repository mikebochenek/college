/* CIS*4600 Elements of Theory of Computation
   Assignment #3, Due: Thursday, November 23, 2000
   Michael Bochenek ID: 0041056 */

#include<stdio.h>
#include<stdlib.h>
#include<string.h>

#define LENGTH 5120 
#define POSSIBLE_CHARS 256

enum boolean {FALSE, TRUE};

int testFSA (char *token, short *fsa, short accept_states[], int num_accept_states);
void process_dfa_file (char *filename, short **dfa, short **dfa_accept_states, short *accept_num);
void process_test_input (char *filename, short *dfa, short *dfa_accept_states, short accept_num);
char *eliminate_commas (char *str);

char *stack;
int stack_ptr = 0;
char *pop_char;
char *push_char; 

int main(int argc, char * argv[]) 
{
   short *dfa = NULL; /* will hold the entire DFA in a table. */
   short *dfa_accept_states = NULL; /* an array of accept states */
   short accept_num = 0; /* number of accept states */
   stack = (char *) malloc (10 * LENGTH);

   if (argc != 3)
   {
      printf ("Usage Error: you must specify 2 files as command line arguments\n");
      exit (0);
   }

   /* creates the DFA, and stores all the relevant info in the 3 variables */
   process_dfa_file (argv[1], &dfa, &dfa_accept_states, &accept_num);

   /* uses the DFA to process the strings contained in file argv[2] */
   process_test_input (argv[2], dfa, dfa_accept_states, accept_num);

   return 0;
}


int testFSA (char *token, short *fsa, short accept_states[], int num_accept_states)
{
   int i = 0, j = 0;
   short current_state = 1;
   stack_ptr = 0;

   if (strlen (token) == 0)
   {
      return TRUE;
   }

   for (i = 0; i < strlen (token) && current_state != 0; i++)
   {
      current_state = fsa [(current_state - 1) * POSSIBLE_CHARS + (int) token[i] ]; 

      /* check if anything has to be pushed onto the stack */
      for (j = 0; j < strlen (push_char); j++)
      {
         if (token[i] == push_char[j])
         {
            stack[stack_ptr] = push_char [j];
            stack_ptr++;
         }
      } 

      /* check if anything has to be popped off the stack */
      for (j = 0; j < strlen (pop_char); j++)
      {
         if (token[i] == pop_char[j])
         {
            /* char returnVal = stack[stack_ptr]; */
            if (stack_ptr == 0)
            {
               return FALSE;
            }
            stack_ptr--;
         }
      }
   }

   for (i = 0; i < num_accept_states; i++)
   {
      if (current_state == accept_states[i] && stack_ptr == 0)
      {
         return TRUE;
      }
   }

   return FALSE;
}


void process_test_input (char *filename, short *dfa, short *dfa_accept_states, short accept_num)
{
   FILE *file;
   char single_line[LENGTH]; 
   char *temp_string = NULL;
   int test_results;
 
   /* open the file, and check for error */
   file = fopen (filename, "r");
   if (file == NULL) {
      printf ("Fatal Error: Unable to open the file %s\n", filename);
      exit (0);
   }

   do
   {
      /* start looping through the file... */
      fgets (single_line, LENGTH, file);

      /* if this is the end, we just read in an eof... */
      if (feof(file))
      {
         continue;
      }

      /* eliminate commas and the trailing \n (if it's there) */
      temp_string = eliminate_commas (single_line);
      if (single_line [strlen (single_line) - 1] == '\n')
      {
         single_line [strlen (single_line) - 1] = '\0';
      }

      printf ("%s:", single_line);

      /* validate the string against the DFA here !!! */
      test_results = testFSA (temp_string, dfa, dfa_accept_states, accept_num);

      /* based on the value returned, print the appropriate message */
      if (test_results == 1)
      {
         printf (" accept\n");
      }
      else if (test_results == 0)
      {
         printf (" reject\n");
      }
      
   } while ( !feof(file) ); 
   /* keep reading in lines */
}
 

void process_dfa_file (char *filename, short **dfa, short **dfa_accept_states, short *accept_num)
{
   FILE *file;
   char single_line[LENGTH]; 
   int i, dfa_states = 0, counter = 0;
   char *temp_string;
   short one_accept_state = 0;

   /* open the file, and check for error */
   file = fopen (filename, "r");
   if (file == NULL) {
      printf ("Fatal Error: Unable to open the file %s\n", filename);
      exit (0);
   }

   /* read in the first line as the total number of fsa states */
   fgets (single_line, LENGTH, file);
   dfa_states = atoi (single_line);
   if (dfa_states == 0)
   {
      printf ("Fatal Error: Number of DFA states must be > 0\n");
      exit (0);
   }

   /* allocate memory for the dfa */
   *dfa = malloc (sizeof (short) * dfa_states * POSSIBLE_CHARS);
   if (*dfa == NULL)
   {
      printf ("Fatal Error: Unable to allocate memory for DFA\n");
      exit (0);
   }

   /* initialize the allocated memory to all zeros */
   for (i = 0; i < (dfa_states * POSSIBLE_CHARS); i++)
   {
      (*dfa) [i] = 0;  
   }

   /* read in the second line as a list of accept states */
   fgets (single_line, LENGTH, file);
   temp_string = single_line;

   /* start parsing the list of accept states */
   one_accept_state = atoi (strtok (temp_string, " "));
   do
   {
      char *next_token = NULL;
      *dfa_accept_states = realloc (*dfa_accept_states, (counter + 1) * sizeof (short));
      (*dfa_accept_states) [counter] = one_accept_state;
      counter++;
      next_token = strtok (NULL, " ");
      if (next_token == NULL) break;
      one_accept_state = atoi (next_token);

      /* if we are unable to convert to an int, then quit */
      if (one_accept_state == 0)
      {
         printf ("Fatal Error: Could not obtain a valid accept state from %s\n", single_line);
         exit (0);
      }
   } while (one_accept_state != 0 && counter < 10); 

   /* update the number of accept states that will be 'returned'
      through a pointer. */
   *accept_num = counter;

   /* get the list of pop characters */
   fgets (single_line, LENGTH, file);
   temp_string = eliminate_commas (single_line);
   pop_char = (char *) malloc (strlen (temp_string) + 1);
   strcpy (pop_char, temp_string);

   /* get the list of push characters */
   fgets (single_line, LENGTH, file);
   temp_string = eliminate_commas (single_line);
   push_char = (char *) malloc (strlen (temp_string) + 1);
   strcpy (push_char, temp_string);

   /* process the rest of the file, the actual transitions between states */
   while ( !feof(file) )
   { 
      char *next_token = NULL;
      int start_state = 0, end_state = 0, i = 0;
      fgets (single_line, LENGTH, file);
      if (feof(file) || strlen (single_line) < 2) continue;

      temp_string = single_line;
      next_token = strtok (temp_string, " ");
      start_state = atoi (next_token);

      /* if we are unable to convert to an int, then quit */
      if (next_token == 0)
      {
         printf ("Fatal Error: Unable to convert string to integer\n");
         exit (0);
      }

      next_token = strtok (NULL, " ");
      end_state = atoi (next_token);

      /* if we are unable to convert to an int, then quit */
      if (end_state == 0)
      {
         printf ("Fatal Error: Unable to convert string to integer\n");
         exit (0);
      }

      temp_string = strtok (NULL, " ");
      temp_string = eliminate_commas(temp_string);

      /* do real processing here !!! */
      for (i = 0; i < strlen (temp_string); i++)
      {
         if (start_state <= 0 || start_state > dfa_states ||
             end_state <= 0 || end_state > dfa_states)
         {
            printf ("Fatal Error: start/end state in definition is out of range of possible states.\n"); 
            exit (0);
         }
         (*dfa) [(start_state-1) * POSSIBLE_CHARS + ((int) temp_string[i]) ] = end_state;  
      }

      free (temp_string);
   } /* keep reading in lines */

   return;
}


char * eliminate_commas (char *str)
{
   int i = 0, j = 0;
   char * new_str = NULL;

   /* allocate memory for the new string that will be returned */
   new_str = malloc (sizeof (char) * strlen (str));
   if (new_str == NULL)
   {
      printf ("Fatal Error: Unable to allocate memory for data structure\n");
      exit (0);
   }

   for (i = 0; i < strlen (str); i++)
   {
      if (str[i] == ' ' || str[i] == '\n' || str[i] == ',')
      {
         /* do nothing, as in, ignore all whitespace, commas, and newlines */
      }
      else
      {
         /* copy all other characters into the new string */
         new_str[j] = str[i];
         j++;
      }
   }
   /* add the end of string char */
   new_str[j] = '\0';

   return new_str;
}
