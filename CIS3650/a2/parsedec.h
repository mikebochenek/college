/* CIS*3650 Organization and Implementation of Programming Languages
   Assignment #2, Due: Sunday October 22, 2000
   Michael Bochenek ID: 0041056 */

#include "y.tab.h"

int tok = 0;
int error_count = 0;
int error_line = 0;

int yylex(void);
void eat(int token); 
void advance(void); 
char * bad_token (int token_num);

void S(void);
void T(void);
void M(void);
void P(void);
void E(void);
void Y(void);
void I(void);


