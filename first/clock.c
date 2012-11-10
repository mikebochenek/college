#include <stdio.h>
#include <conio.h>
#include "zero.h"
#include "one.h"
#include "two.h"
#include "three.h"
#include "four.h"
#include "five.h"
#include "six.h"
#include "seven.h"
#include "eight.h"
#include "nine.h"
#include "startup.h"
main()
{
int anything;
int one_hour;
int ten_minute;
int one_minute;
int ten_second;
int one_second;
int tenth_second;
int hundredth_second;
one_hour = 0;
ten_minute = 0;
one_minute = 0;
ten_second = 0;
one_second = 0;
tenth_second = 0;
hundredth_second = 1;

clrscr();
puttext (1,1,15,4,STARTUP);
for (anything = 1; anything > 0; anything = anything)
  {
  delay(10);
  hundredth_second = hundredth_second + 1;
  if (hundredth_second == 1) puttext (13,3,13,3,ONE);
  else if (hundredth_second == 2) puttext (13,3,13,3,TWO);
  else if (hundredth_second == 3) puttext (13,3,13,3,THREE);
  else if (hundredth_second == 4) puttext (13,3,13,3,FOUR);
  else if (hundredth_second == 5) puttext (13,3,13,3,FIVE);
  else if (hundredth_second == 6) puttext (13,3,13,3,SIX);
  else if (hundredth_second == 7) puttext (13,3,13,3,SEVEN);
  else if (hundredth_second == 8) puttext (13,3,13,3,EIGHT);
  else if (hundredth_second == 9) puttext (13,3,13,3,NINE);
  else if (hundredth_second == 10)
    {
    hundredth_second = 0;
    tenth_second = tenth_second + 1;
    puttext (13,3,13,3,ZERO);
    }
  if (tenth_second == 1) puttext (12,3,12,3,ONE);
  else if (tenth_second == 2) puttext (12,3,12,3,TWO);
  else if (tenth_second == 3) puttext (12,3,12,3,THREE);
  else if (tenth_second == 4) puttext (12,3,12,3,FOUR);
  else if (tenth_second == 5) puttext (12,3,12,3,FIVE);
  else if (tenth_second == 6) puttext (12,3,12,3,SIX);
  else if (tenth_second == 7) puttext (12,3,12,3,SEVEN);
  else if (tenth_second == 8) puttext (12,3,12,3,EIGHT);
  else if (tenth_second == 9) puttext (12,3,12,3,NINE);
  else if (tenth_second == 10)
    {
    tenth_second = 0;
    one_second = one_second + 1;
    puttext (12,3,12,3,ZERO);
    }
  if (one_second == 1) puttext (10,3,10,3,ONE);
  else if (one_second == 2) puttext (10,3,10,3,TWO);
  else if (one_second == 3) puttext (10,3,10,3,THREE);
  else if (one_second == 4) puttext (10,3,10,3,FOUR);
  else if (one_second == 5) puttext (10,3,10,3,FIVE);
  else if (one_second == 6) puttext (10,3,10,3,SIX);
  else if (one_second == 7) puttext (10,3,10,3,SEVEN);
  else if (one_second == 8) puttext (10,3,10,3,EIGHT);
  else if (one_second == 9) puttext (10,3,10,3,NINE);
  else if (one_second == 10)
    {
    one_second = 0;
    ten_second = ten_second + 1;
    puttext (10,3,10,3,ZERO);
    }
  if (ten_second == 1) puttext (9,3,9,3,ONE);
  else if (ten_second == 2) puttext (9,3,9,3,TWO);
  else if (ten_second == 3) puttext (9,3,9,3,THREE);
  else if (ten_second == 4) puttext (9,3,9,3,FOUR);
  else if (ten_second == 5) puttext (9,3,9,3,FIVE);
  else if (ten_second == 6)
    {
    ten_second = 0;
    one_minute = one_minute + 1;
    puttext (9,3,9,3,ZERO);
    }
  if (one_minute == 1) puttext (7,3,7,3,ONE);
  else if (one_minute == 2) puttext (7,3,7,3,TWO);
  else if (one_minute == 3) puttext (7,3,7,3,THREE);
  else if (one_minute == 4) puttext (7,3,7,3,FOUR);
  else if (one_minute == 5) puttext (7,3,7,3,FIVE);
  else if (one_minute == 6) puttext (7,3,7,3,SIX);
  else if (one_minute == 7) puttext (7,3,7,3,SEVEN);
  else if (one_minute == 8) puttext (7,3,7,3,EIGHT);
  else if (one_minute == 9) puttext (7,3,7,3,NINE);
  else if (one_minute == 10)
    {
    one_minute = 0;
    ten_minute = ten_minute + 1;
    puttext (7,3,7,3,ZERO);
    }
  if (ten_minute == 1) puttext (6,3,6,3,ONE);
  else if (ten_minute == 2) puttext (6,3,6,3,TWO);
  else if (ten_minute == 3) puttext (6,3,6,3,THREE);
  else if (ten_minute == 4) puttext (6,3,6,3,FOUR);
  else if (ten_minute == 5) puttext (6,3,6,3,FIVE);
  else if (ten_minute == 6)
    {
    ten_minute = 0;
    one_hour = one_hour + 1;
    puttext (6,3,6,3,ZERO);
    }
  if (one_minute == 1)
    {
    printf ("\a");
    gotoxy(1,5);
    break;
    }
  }
return(0);
}
