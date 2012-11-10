/* CIS*3650 Organization and Implementation of Programming Languages
   Assignment #2, Due: Sunday October 22, 2000
   Michael Bochenek ID: 0041056 */

#include "parsedec.h"

extern int line_count; 


int main(int argc, char *argv[]) 
{
   advance(); /* read in the first token */
   S(); /* the program must consist of a SINGLE struct{} */ 

   if (tok != 0) /* at the end, there should be nothing left */
   {
      printf ("Parse Error (bad token '%s' at line %d): Unexpected keywords at the end of root structure.\n", bad_token (tok), line_count);
   }

   /* print a status message depending on number of errors */
   if (error_count == 0)
   {
      printf ("The input is valid, parsing was successful!!!\n");
   }
   else
   {
      printf ("\nParsing failed: %d error(s) were found!!!\n", error_count);
   }

   return 0;
}


void advance() /* simply call yylex() */ 
{
   // printf ("\t\t\tadvance() current token %d - ", tok);
   tok = yylex();
   // printf ("\t\t\tnext token %d \n", tok);
}


void eat(int token) /* consume a single token */ 
{
   // printf ("\t\t\tcalling advance (%d).\n", tok);
   if (token == tok)
   {
      advance();
   }
   else
   {
      // printf ("line %d: expecting '%s' and current is '%s'.  ", line_count, bad_token (token), bad_token (tok));
      // printf ("insert token here:  %s\n", bad_token (token));
      
      printf ("Parse Error (bad token: '%s' at line %d): Expecting '%s'.\n", bad_token (tok), line_count, bad_token (token));

      token = tok;
   }
} 


void S(void)  /* STRUCTDEC */
{
   // printf ("\t\t\tinside of S() [%d]\n", tok);
   switch (tok)
   {
      case STRUCT:
         eat (STRUCT);
         T();
         eat (LBRACE);
         M();
         eat (RBRACE);
         eat (SEMICOLON);
         break;
      default:
         printf ("Parse Error (bad token '%s' at line %d): Expecting keyword 'struct'.\n", bad_token (tok), line_count);
   }
}


void T(void)  /* TAG */
{
   // printf ("\t\t\tinside of T() [%d]\n", tok);
   switch (tok)
   {
      case ID_A: case ID_B: case ID_C: case ID_X: case ID_Y: case ID_Z:
         I();
         break;
      case LBRACE: /* LBRACE can follow because TAG is nullable */
         break;
      default:
         printf ("Parse Error (bad token '%s' at line %d): Expecting '{' or variable name (a,b,c,x,y,z).\n", bad_token (tok), line_count);
   }
}


void M(void)  /* MEMBERS */
{
   // printf ("\t\t\tinside of M() [%d]\n", tok);
   switch (tok)
   {
      case INT:
      case REAL:
      case STRUCT:
         E();
         P();
         break;
      case RBRACE:
         printf ("Parse Error (bad token '%s' at line %d): Expecting a member inside structure (expecting keyword 'int' or 'real' or 'struct').\n", bad_token (tok), line_count);
         break;
      default:
         printf ("Parse Error (bad token '%s' at line %d): Expecting keyword 'int' or 'real' or 'struct' or '}'.\n", bad_token (tok), line_count);
   }
}


void P(void)  /* MEMBERS PRIME */
{
   // printf ("\t\t\tinside of P() [%d]\n", tok);
   switch (tok)
   {
      case INT:
      case REAL:
      case STRUCT: /* then it must be a member */
         E();
         P();
         break;
      case RBRACE: /* end of member list */
         break;
      default:
         printf ("Parse Error (bad token '%s' at line %d): Expecting keyword 'int' or 'real' or 'struct' or '}'.\n", bad_token (tok), line_count);
   }
}


void E(void)  /* MEMB */
{
   // printf ("\t\t\tinside of E() [%d]\n", tok);
   switch (tok)
   {
      case INT:
      case REAL:
         Y();
         I();
         eat (SEMICOLON);
         break;
      case STRUCT:
         S();
         break;
      default:
         printf ("Parse Error (bad token '%s' at line %d): Expecting keyword 'int' or 'real' or 'struct'.\n", bad_token (tok), line_count);
   }
}


void Y(void)  /* TYPE */
{
   // printf ("\t\t\tinside of Y() [%d]\n", tok);
   switch (tok)
   {
      case INT:
         eat (INT);
         break;
      case REAL:
         eat (REAL);
         break;
      default:
         printf ("Parse Error (bad token '%s' at line %d): Expecting keyword 'int' or 'real'.\n", bad_token (tok), line_count);
   }
}


void I(void)  /* ID */
{
   // printf ("\t\t\tinside of I() [%d]\n", tok);
   switch (tok)
   {
      case ID_A:
         eat (ID_A);
         break;
      case ID_B: 
         eat (ID_B);
         break;
      case ID_C: 
         eat (ID_C);
         break;
      case ID_X: 
         eat (ID_X);
         break;
      case ID_Y: 
         eat (ID_Y);
         break;
      case ID_Z:
         eat (ID_Z);
         break;
      default:
         printf ("Parse Error (bad token: '%s' at line %d): Expecting variable name (a,b,c,x,y,z).\n", bad_token(tok), line_count);
   }
}


char * bad_token (int token_num) /* return pointer to a string that the integer represents.  This function must be changed in y.tab.h is changed in any way (order, addition, removing) etc. */
{
   if (error_line != line_count)
   {
      error_count++;
      error_line = line_count;
   }

   switch (token_num)
   {
      case STRUCT:
         return "struct";
      case INT:
         return "int";
      case REAL:
         return "real";
      case LBRACE:
         return "{";
      case RBRACE:
         return "}";
      case SEMICOLON:
         return ";";
      case ID_A:
         return "a";
      case ID_B:
         return "b";
      case ID_C:
         return "c";
      case ID_X:
         return "x";
      case ID_Y:
         return "z";
      case ID_Z:
         return "y";
      default:
         return "unknown token";
   }

}

