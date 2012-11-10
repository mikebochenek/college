/* CIS*4600 Elements of Theory of Computation
   Assignment #4, Due: Thursday, November 30, 2000
   Michael Bochenek ID: 0041056 */

#include "a4.h"

int main(int argv, char ** argc) 
{
   if (argv != 2)
   {
      printf ("Usage Error: you must specify the string to test\n"
              "in the command line arguments\n");
      exit (0);
   }

   init (argc[1]);

   process_character ();

   if (state == RE)
   {
      printf ("\t\t%s: reject\n", argc[1]);
   }
   else if (state == AC)
   {
      printf ("\t\t\%s: accept\n", argc[1]);
   }

   return 0;
}

void init (char * str)
{
   string = (char *) malloc ( (strlen (str) + 6) * sizeof (char) );
   string[0] = '\0';
   if (str[0] != '<')
   {
      strcat (string, "<"); 
   }
   strcat (string, str);
   strcat (string, "____");

   position = 0;
   direction = RHT;
   state = 1;
}

void process_character (void)
{
   while (TRUE)
   {
      if (state == RE)
      {
         return;
      }
      else if (state == AC)
      {
         return;
      }

      if (state > STATE_NUM)
      {
         printf ("Fatal Error: state is set to more than STATE_NUM\n");
         printf ("state = %d and STATE_NUM = %d\n", state, STATE_NUM);
         exit (0);
      }

      switch (string [position])
      {
         case '<':
            move_on (0);
            break;
         case 'a':
            move_on (1);
            break;
         case 'b':
            move_on (2);
            break;
         case '_':
            move_on (3);
            break;
         case '>':
            move_on (4);
            break;
         default:
            printf ("Fatal error: character encountered is not a valid\n");
            printf ("valid characters are: '<', 'a', 'b', '_', '>'\n");
            exit (0);
            break;
      }
   }
}

void move_on (int current_char)
{
   int new_state = table [state-1][current_char][0];
   int new_character = table [state-1][current_char][1];
   int new_direction = table [state-1][current_char][2];
   char *direction_str = NULL;
   int i;

   string[position] = new_character;
   direction = new_direction;
   state = new_state;

   if (state == RE)
   {
      printf ("\nREJECT!\n");
      return;
   }
   else if (state == AC)
   {
      printf ("\nACCEPT!\n");
      return;
   }

   if (direction == RHT)
   {
      position++;
      direction_str = "right";
   }
   else if (direction == LFT)
   {
      position--;
      direction_str = "left";
   }
   else
   {
      printf ("Fatal error in move_on():  invalid direction (%d)\n", direction);
      exit(0);
   }

   printf ("\nNEW SYMBOL: %c    NEW DIRECTION: %s    NEW STATE: %d\n",
           new_character, direction_str, state);
   printf ("STRING: \"%s\"\n         ", string);
   for (i = 0; i < position; i++) printf (" ");
   printf ("*\n");
}


