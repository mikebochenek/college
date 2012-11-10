/* CIS*4600 Elements of Theory of Computation
   Assignment #1, Due: Tuesday October 3, 2000
   Michael Bochenek ID: 0041056 */

#include<stdio.h>
#include<stdlib.h>
#include<string.h>


void print_6_combinations (int n, char *array);
void print_5_combinations (int n, char *array);
void print_4_combinations (int n, char *array);
void print_3_combinations (int n, char *array);
void print_2_combinations (int n, char *array);
void print_1_combinations (int n, char *array);

#define LEN 2 

int main(int argc, char * argv[]) 
{
   /* char array[LEN] = {'1', '4', '8'}; */
   /* char array[LEN] = {'a'}; */
   /* char array[LEN] = {'0', '1'}; */
   /* char array[LEN] = {'a', 'b'}; */
   /* char array[LEN] = {'0', '1', '2'}; */
   char array[LEN] = {'(', ')'};

   printf ("\n");
   print_1_combinations (LEN, array);
   print_2_combinations (LEN, array);
   print_3_combinations (LEN, array);
   print_4_combinations (LEN, array);
   print_5_combinations (LEN, array);
   print_6_combinations (LEN, array);

   return 0;
}

void print_6_combinations (int n, char *array)
{
   int a, b, c, d, e, f;

   for (a = 0; a < n; a++)
   {
      for (b = 0; b < n; b++)
      {
         for (c = 0; c < n; c++)
         {
            for (d = 0; d < n; d++)
            {
               for (e = 0; e < n; e++)
               {
                  for (f = 0; f < n; f++)
                  {
                      printf ("%c,%c,%c,%c,%c,%c\n", array[a], array[b], 
                                array[c], array[d], array[e], array[f]);
                  }
               }
            }
         }
      }
   }
}

void print_5_combinations (int n, char *array)
{
   int a, b, c, d, e;

   for (a = 0; a < n; a++)
   {
      for (b = 0; b < n; b++)
      {
         for (c = 0; c < n; c++)
         {
            for (d = 0; d < n; d++)
            {
               for (e = 0; e < n; e++)
               {
                   printf ("%c,%c,%c,%c,%c\n", array[a], array[b], array[c],
                                             array[d], array[e]);
               }
            }
         }
      }
   }
}


void print_4_combinations (int n, char *array)
{
   int a, b, c, d;

   for (a = 0; a < n; a++)
   {
      for (b = 0; b < n; b++)
      {
         for (c = 0; c < n; c++)
         {
            for (d = 0; d < n; d++)
            {
                printf ("%c,%c,%c,%c\n", array[a], array[b], array[c],
                                      array[d]);
            }
         }
      }
   }
}


void print_3_combinations (int n, char *array)
{
   int a, b, c;

   for (a = 0; a < n; a++)
   {
      for (b = 0; b < n; b++)
      {
         for (c = 0; c < n; c++)
         {
             printf ("%c,%c,%c\n", array[a], array[b], array[c]);
         }
      }
   }
}


void print_2_combinations (int n, char *array)
{
   int a, b;

   for (a = 0; a < n; a++)
   {
      for (b = 0; b < n; b++)
      {
          printf ("%c,%c\n", array[a], array[b]);
      }
   }
}


void print_1_combinations (int n, char *array)
{
   int a;

   for (a = 0; a < n; a++)
   {
      printf ("%c\n", array[a]);
   }
}

