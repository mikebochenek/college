/* CIS*3650 Organization and Implementation of Programming Languages
   Assignment #1, Due: Sunday October 1, 2000
   Michael Bochenek ID: 0041056 */

#include<stdio.h>
#include<stdlib.h>
#include"fsa_def.h"

int main()
{
   char string[5120];

   while (!feof(stdin))
   {
      /* input entire line from stdin */
      scanf ("%s", string);
      if (feof(stdin)) continue;
      /* parse line into separate 'tokens' */
      /* pass each token to processing function */
      analyze_token (string);
   }

   return 0;
}


/* this function is used to test the FSA against each token and then
   print out the result. */
void analyze_token (char *token)
{
   int final_state = 0;

   /* call function to determine the final state */
   final_state = testFSA (token, FSA_DEF, ACCEPT_STATES, NUM_ACCEPT_STATES);
   
   /* based on the combination of the four booleans either
      display 'is a INTEGER/FLOAT/STRING/CHARACTER'
      or display an error message
   */
   if (final_state == 3)
   {
      printf ("%s is an INTEGER\n", token);
   }
   else if (final_state == 5 || 
            final_state == 8)
   {
      printf ("%s is a FLOAT\n", token);
   }
   else if (final_state == 4 ||
            final_state == 2 ||
            final_state == 9)
   {
      printf ("%s is a CHARACTER\n", token);
   }
   else if (final_state == 10 ||
            final_state == 6 ||
            final_state == 7 ||
            final_state == 0 ||
            final_state == 1)
   {
      printf ("%s is a STRING\n", token);
   }
   else
   {
      printf ("%s [? - this should never print]\n", token);
   }
 
   return;
}


/* this function returns the final state after a sequence of characters
   is run through the fsa. */
int testFSA (char *token, short fsa[][256], short accept_states[], int num_accept_states)
{
   int i = 0;
   short current_state = 1;

   /* determine next_state based on table */
   for (i = 0; i < strlen (token) && current_state != 0; i++)
   {
      current_state = fsa [current_state - 1] [ (int) token[i] ]; 
      /* printf ("char %c state %d\n", token[i], current_state); */
   }

   return (int) current_state;
}


