/* CIS*4750 Artificial Intelligence
   Assignment #2, Due: Tuesday October 3, 2000
   Michael Bochenek ID: 0041056 */

#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<time.h>
#include "a2.h"


int main(int argc, char * argv[]) 
{
   int vacuum_start = 0, current_pos = 0;
   int direction = EAST;   

   vacuum_start = get_start_pos();
   init_state (vacuum_start);
   current_pos = vacuum_start;

   while (TRUE) 
   {
      current_pos = move (current_pos, &direction);
   }

   return 0;
}


void print_pic (void)
{
   printf ("+-------+\n");
   printf ("| %c %c %c |\n", map[0][0], map[1][0], map[2][0]);
   printf ("| %c %c %c |\n", map[0][1], map[1][1], map[2][1]);
   printf ("| %c %c %c |\n", map[0][2], map[1][2], map[2][2]);
   printf ("+-------+\n\n");
   
   printf ("<press any key to continue>\n");
   getchar();
}


void init_state (int vacuum_position)
{
   int x, y;

   srand((int) time(NULL));
   for (x = 0; x < SIZE; x++)
   {
      for (y = 0; y < SIZE; y++)
      {
         int probablity;
         probablity = rand() % 5;
         if (probablity == 0)
         {
            map[x][y] = DIRTY;
         }
         else
         {
            map[x][y] = CLEAN;
         } 
         visited[x][y] = FALSE;
      }
   }

   x = (vacuum_position - 1) % 3;
   y = (vacuum_position - 1) / 3;

   map[x][y] = VACUUM;
}


int get_start_pos (void)
{
   char input[100];
   int start_pos;

   printf ("+-------+\n");
   printf ("| 1 2 3 |\n");
   printf ("| 4 5 6 |\n");
   printf ("| 7 8 9 |\n");
   printf ("+-------+\n");

   do 
   {
      printf ("Please enter the starting position: ");
      scanf ("%s", input);
      start_pos = atoi (input);
   } while (start_pos == 0 || start_pos >= 10);

   printf ("\nLegend:\n");
   printf (" %c - dirty spot\n", DIRTY);
   printf (" %c - clean spot\n", CLEAN);
   printf (" %c - the vacuum cleaner\n", VACUUM);

   getchar();
   return start_pos;
}


int move (int current_pos, int *direction)
{
   char dir_label[4][6] = {"EAST", "WEST", "SOUTH", "NORTH"};
   int x, y, new_pos;

   x = (current_pos-1) % 3;
   y = (current_pos-1) / 3;

   printf ("Vacuum cleaner is now facing: %s.  Current score: %d.\n", dir_label[(*direction)], score); 
   print_pic();

   map [x][y] = CLEAN;


   if (visited[x][y] == FALSE)
   {
      visit_count++;
      visited[x][y] = TRUE;
   }

   if (visit_count == (SIZE*SIZE))
   {
      exit(0);
   }

   score -= 1; /* minus 1 point for each action taken */

   if (*direction == EAST)
   {
      if (x == 2) /* hit a the east wall */
      {
         *direction = SOUTH;
         printf ("Touch sensor detected east wall, changing direction.\n"); 
      }
      else
      {
         x++;
         if (map [x][y] == DIRTY) /* detect if there is dirt */
         {
            printf ("Dirt found!  Sucking dirt now.\n");
            map [x][y] = CLEAN; 
            score += 100;
         }
         else
         {
            printf ("No dirt was found.\n");
         }
      }
   }

   else if (*direction == SOUTH)
   {
      if (y == 2) /* hit a the south wall */
      {
         *direction = WEST;
         printf ("Touch sensor detected south wall, changing direction.\n"); 
      }
      else
      {
         y++;
         if (map [x][y] == DIRTY) /* detect if there is dirt */
         {
            printf ("Dirt found!  Sucking dirt now.\n");
            map [x][y] = CLEAN; 
            score += 100;
         }
         else
         {
            printf ("No dirt was found.\n");
         }
      }
   }

   else if (*direction == WEST)
   {
      if (x == 0) /* hit a the west wall */
      {
         *direction = NORTH;
         printf ("Touch sensor detected west wall, changing direction.\n"); 
      }
      else
      {
         x--;
         if (map [x][y] == DIRTY) /* detect if there is dirt */
         {
            printf ("Dirt found!  Sucking dirt now.\n");
            map [x][y] = CLEAN; 
            score += 100;
         }
         else
         {
            printf ("No dirt was found.\n");
         }
      }
   }

   else if (*direction == NORTH)
   {
      if (y == 0) /* hit a the north wall */
      {
         *direction = EAST;
         printf ("Touch sensor detected north wall, changing direction.\n"); 
      }
      else
      {
         y--;
         if (map [x][y] == DIRTY) /* detect if there is dirt */
         {
            printf ("Dirt found!  Sucking dirt now.\n");
            map [x][y] = CLEAN; 
            score += 100;
         }
         else
         {
            printf ("No dirt was found.\n");
         }
      }
   }

   map [x][y] = VACUUM;

   new_pos = y * 3 + x + 1;

   if (x == 0 && y == 1 && visited[1][1] == FALSE)
   {
      *direction = EAST;
   }
   return new_pos;
}




