#include <stdio.h>
main()
{
int line[10],numbers[241],win[6];
int count1,count2,count3,input_file,temp_int,bonus,test;
int count,line_number,temp,to_display,loseorwin;

FILE *fin;
fin=fopen("numbers.dat","r");

count1 = 0;
while (fscanf(fin,"%d",&input_file) !=EOF)
  {
  count1 = count1 + 1;
  numbers[count1] = input_file;
  }

for (temp_int=1; temp_int<7; temp_int++) {
  printf ("Enter winning number %d: ", temp_int);
  scanf ("%d",  &win[temp_int]);
  }

printf ("Enter the bonus number: ");
scanf ("%d", &bonus);
printf ("\n");

for (temp_int=1; temp_int<11; temp_int++)
  line[temp_int] = 0;
for (temp_int=1; temp_int<8; temp_int++)
  {
  if (temp_int == 1) test = win[1];
  if (temp_int == 2) test = win[2];
  if (temp_int == 3) test = win[3];
  if (temp_int == 4) test = win[4];
  if (temp_int == 5) test = win[5];
  if (temp_int == 6) test = win[6];
  if (temp_int == 7) test = bonus;
  for (temp=1; temp<61; temp++)
    {
    if (temp < 61) line_number = 10;
    if (temp < 55) line_number = 9;
    if (temp < 49) line_number = 8;
    if (temp < 43) line_number = 7;
    if (temp < 37) line_number = 6;
    if (temp < 31) line_number = 5;
    if (temp < 25) line_number = 4;
    if (temp < 19) line_number = 3;
	 if (temp < 13) line_number = 2;
    if (temp < 6) line_number = 1;

    if (test == bonus)
      {
      if (numbers[temp] == test) line[line_number] = line[line_number] + 10;
      }
    else if (numbers[temp] == test) line[line_number] ++;
  }
}

count3 = 0;
count = 1;
for (count2 = 1; count2 < 61; count2 = count2 + 1) {
  if (numbers[count2] < 10) printf ("  ");
  else printf (" ");
  printf ("%d",numbers[count2]);
  count3 = count3 + 1;
  if (count3 == 6) {
	 count3 = 0;
    if (line[count] > 9) to_display = line[count] - 10;
      else to_display = line[count];
    printf ("   <-- Matches: %d", to_display);
      if (line[count] > 9) printf (" & bonus");
    printf ("\n");
    count ++;
    }
  }
  loseorwin = 0;
  for (temp=0; temp<10; temp++)
    {
	 if (line[temp] > 2) loseorwin ++;
    }
  if (loseorwin > 0) printf ("\n$ $ $ You Won!!! $ $ $");
  if (loseorwin == 0) printf ("\nYou did not win anything");

  return(0);
}
