%{
/* CIS*3650 Organization and Implementation of Programming Languages
   Assignment #2, Due: Sunday October 22, 2000
   Michael Bochenek ID: 0041056 */

#include "y.tab.h"

int EM_tokPos;

int charPos=1;
int line_count=1;


int yywrap(void)
{
   charPos=1;
   return 1;
}


void adjust(void)
{
   EM_tokPos=charPos;
   charPos+=yyleng;
}

void incr_line_count(void)
{
   line_count++;
   adjust();
}

%}

%%

[ \t]+	{adjust(); continue;}
[\n]     {incr_line_count(); continue;}
";"      {adjust(); return SEMICOLON;}
"{"      {adjust(); return LBRACE;}
"}"      {adjust(); return RBRACE;}
int      {adjust(); return INT;}
real     {adjust(); return REAL;}
struct   {adjust(); return STRUCT;}
a        {adjust(); return ID_A;}
b        {adjust(); return ID_B;}
c        {adjust(); return ID_C;}
x        {adjust(); return ID_X;}
y        {adjust(); return ID_Y;}
z        {adjust(); return ID_Z;}
.	      {adjust(); printf ("Scanner: illegal token at line %d\n", line_count);}



