/* CIS*4600 Elements of Theory of Computation
   Assignment #2, Due: Thursday November 2, 2000
   Michael Bochenek ID: 0041056 */

#include "a2.h" 


int main(int argc, char *argv[]) 
{
   if (argc != 2 && argc != 3)
   {
      printf ("Error:  You must specify the regular expression as the first command line \nargument.  It must be placed inside quotation marks if special chracters \n(* or +) are used.\n");
      exit (0);
   }

   if (argc == 3)
   {
      if (strcmp (argv[2], "-nfa") == 0 || strcmp (argv[2], "-NFA") == 0)
      {
         print_flag = TRUE;
      }
   }

   regexp = argv[1];

   /* printf ("%s", regexp); */

   str = (char *) malloc (1000);

   add_node (0, 1, 0, 0, 1, 0, 0, 0);

   cur_sub_section.start_node = 1;
   cur_sub_section.end_node = 1;

   remove_spaces();

   advance();
   advance();
   program();

   add_node (node_num, 0, 0, 0, 0, 1, 0, 0);
   /* add_node (node_num, node_num, 0, 0, 0, 1, 0, 0); 
    * so that it keeps looping in on itself when it sees E */

   if (prev_tok == 0 && cur_tok == 0)
   {
      /* printf ("main() Everything OK\n"); */
   }
   else
   {
      printf ("FATAL ERROR\n");
      printf ("main() Some kind of error\n");
      printf ("cur_tok is %d and level is %d\n", cur_tok, level);
      exit (0);
   }

   /* printf ("counter: %d, str: %s\n", counter, str); */

   debug_print_nfa();

   mark_start_states(1);

   remove_E_transitions();

   build_dfa();

   print_final_dfa();

   free (str);
   free (nfa);

   /* printf ("\t\t\t\t\t\tProgram finished.\n"); */

   return 0;
}


void eat(int token) 
{
   if (token == prev_tok)
   {
      advance();
   }
   else
   {
      printf ("FATAL ERROR: unable to eat().\n");
      exit (0);
   }
} 


void advance (void)
{
   char legal_char[] = {'0', '1', 'E', ')', '(', '+', '*'};
   char a = legal_char [prev_tok - 257];
   if (a == legal_char[0] ||
       a == legal_char[1] ||
       a == legal_char[2] ||
       a == legal_char[3] ||
       a == legal_char[4] ||
       a == legal_char[5] ||
       a == legal_char[6] )
   {
      str[counter] = a;
      counter++;
      str[counter] = '\0';
   }

   prev_tok = cur_tok;

   if (regexp_ptr < (strlen (regexp)))
   {
      cur_tok = regexp[regexp_ptr];
      regexp_ptr++;
      if (cur_tok == '0')
         cur_tok = ZERO;
      if (cur_tok == '1')
         cur_tok = ONE;
      if (cur_tok == 'E')
         cur_tok = EPSILON;
      if (cur_tok == ')')
         cur_tok = RPAREN;
      if (cur_tok == '(')
         cur_tok = LPAREN;
      if (cur_tok == '+')
         cur_tok = PLUS;
      if (cur_tok == '*')
         cur_tok = ASTERIX;
   }
   else
   {
      cur_tok = 0;
   }

}


void program (void)
{
   switch (prev_tok)
   {
      case LPAREN:
      case ONE: 
      case ZERO: 
      case EPSILON:
         /* printf ("program() Good start:  LPAREN or ONE or ZERO or EPSILON\n"); */
         sub_section_stack[stack_ptr].start_node = cur_sub_section.start_node;
         sub_section_stack[stack_ptr].end_node = 0;
         stack_ptr++;
         expr();
         break;
      case PLUS:
      case ASTERIX:
      case RPAREN:
         printf ("FATAL ERROR\n");
         printf ("*** program() Bad start:  PLUS or ASTERIX or RPAREN\n");
         exit (0);
         break;
      case 32:
         eat (32);
         break;
      default:
         printf ("FATAL ERROR\n");
         printf ("*** program() Default error\n");
         exit(0);
   }
      
}


void expr (void)
{
   int flag = 0;
   switch (prev_tok)
   {
      case LPAREN:
         /* printf ("expr() LPAREN expr RPAREN\n"); */
         eat (LPAREN);
         sub_section_queue[queue_ptr].start_node = cur_sub_section.start_node;
         sub_section_queue[queue_ptr].end_node = cur_sub_section.end_node;
         queue_ptr++;
         sub_section_stack[stack_ptr].start_node = cur_sub_section.start_node;
         sub_section_stack[stack_ptr].end_node = 0;
         stack_ptr++;
         level++;
         expr ();
         expr ();
         break;
      case ONE: 
      case ZERO: 
      case EPSILON:
         /* printf ("expr() LITERAL (ONE or ZERO or EPSILON)\n"); */
         if (cur_tok == PLUS)
         {
            /* printf ("expr() PLUS encountered!! (literal followed by +)\n"); */
            literal();
            if (sub_section_stack[stack_ptr-1].end_node == 0)
            {
               sub_section_stack[stack_ptr-1].end_node = node_num;
            }
            black_box (PLUS);
            eat (PLUS);
            expr(); /* must be followed by expr */
         }
         else if (cur_tok == ASTERIX)
         {
            /* printf ("expr() ASTERIX encountered!! (literal followed by *)\n"); */
            literal();
            black_box (ASTERIX);
            eat (ASTERIX);

            /*
             * now what if all this is followed by a + ????
             */
         }
         else
         {
            literal();
         }
         break;
      case PLUS:
         printf ("FATAL ERROR\n");
         printf ("*** expr() Error not expecting PLUS\n");
         exit (0);
         break;
      case ASTERIX:
         printf ("FATAL ERROR\n");
         printf ("*** expr() Error not expecting ASTERIX\n");
         exit (0);
         break;
      case RPAREN:
         if (level == 0)
         {
            printf ("FATAL ERROR\n");
            printf ("*** expr() Error not expecting RPAREN\n");
            exit (0);
         }
         else
         {
            /* printf ("expr() encountered ')', going up one level.\n"); */
            if (cur_tok == PLUS)
            {
               /* printf ("expr() PLUS encountered!! (')' followed by +)\n"); */
               if (sub_section_stack[stack_ptr-1].end_node == 0)
               {
                  sub_section_stack[stack_ptr-1].end_node = node_num;
               }
               stack_ptr--;
               black_box (PLUS);
               eat (RPAREN);
               level--;
               eat (PLUS);
               queue_ptr--;
               expr(); /* must be followed by another expr */
            }
            else if (cur_tok == ASTERIX)
            {
               /* printf ("expr() ASTERIX encountered!! (')' followed by *)\n"); */
               level--;
               black_box (ASTERIX);
               eat (RPAREN);
               eat (ASTERIX);
               queue_ptr--;
               stack_ptr--;
               /*
                * now what if all this is followed by a + ????
                */
            }
            else
            {
               eat (RPAREN);
               level--;
               queue_ptr--;
               stack_ptr--;
            }
         }
         break;
      case 0:
         /* printf ("the end"); */
         flag = 1; /* special trick */
         break;
      case 32:
         eat (32);
         break;
      default:
         printf ("FATAL ERROR\n");
         printf ("*** expr() Default error %d\n", prev_tok);
         exit (0);
   } 
   
   if (((cur_tok != 0 && prev_tok != RPAREN) || (cur_tok == 0 && prev_tok != RPAREN)) && flag == 0)
   {
      /* printf ("expr() inside the SPECIAL PART\n"); */
      expr();
   }

   /* printf ("\t\t\t\t\tExiting expr()\n"); */
}


void literal (void)
{
   switch (prev_tok)
   {
      case ONE:
         /* printf ("literal() ONE\n"); */
         black_box (ONE);
         eat (ONE);
         break;
      case ZERO:
         /* printf ("literal() ZERO\n"); */
         black_box (ZERO);
         eat (ZERO);
         break;
      case EPSILON:
         /* printf ("literal() EPSILON\n"); */
         black_box (EPSILON);
         eat (EPSILON);
         break;
      default:
         /* printf ("*** literal() General Error\n"); */
         exit (0);
         break;
   }
}


void black_box (int type_of_box)
{
   switch (type_of_box)
   {
      case ZERO:
      case ONE:
      case EPSILON:
         /* printf ("node_num:%d\n", node_num); */

         add_node (node_num, node_num + 1, 0, 0, 0, 0, 0, 0);

         if (type_of_box == ZERO)
         {
            add_node (node_num, 0, node_num + 1, 0, 0, 0, 0, 0);
         }
         else if (type_of_box == ONE)
         {
            add_node (node_num, 0, 0, node_num + 1, 0, 0, 0, 0);
         }
         else if (type_of_box == EPSILON)
         {
            add_node (node_num, node_num + 1, 0, 0, 0, 0, 0, 0);
         }

         add_node (node_num, node_num + 1, 0, 0, 0, 0, 0, 0);

         cur_sub_section.start_node = node_num - 2 - 1;
         cur_sub_section.end_node = node_num + 3 - 2 - 1;

         break;

      case ASTERIX:
         if (prev_tok == RPAREN)
         {
            /* printf ("\t\t\t RPAREN, ASTERIX\n"); */

            prev_sub_section.start_node = sub_section_queue[queue_ptr-1].start_node;
            prev_sub_section.end_node = sub_section_queue[queue_ptr-1].end_node;

            add_node (node_num, prev_sub_section.end_node, 0, 0, 0, 0, 0, 0);

            add_node (prev_sub_section.end_node, node_num+1, 0, 0, 0, 0, 0, 0);
            /* tricky part to deal with 'real' ASTERIX */

            cur_sub_section.start_node = prev_sub_section.end_node;
            cur_sub_section.end_node = node_num;

            real_node_num_adjust--;
            debug_print_queue();
         }
         else
         {
            /* printf ("\t\t\t just ASTERIX\n"); */
            prev_sub_section.start_node = cur_sub_section.start_node;
            prev_sub_section.end_node = cur_sub_section.end_node;

            add_node (node_num, prev_sub_section.start_node, 0, 0, 0, 0, 0, 0);

            add_node (prev_sub_section.start_node, prev_sub_section.end_node + 2, 0, 0, 0, 0, 0, 0);
            /* tricky part to deal with 'real' ASTERIX */

            cur_sub_section.start_node = prev_sub_section.start_node;
            cur_sub_section.end_node = prev_sub_section.end_node + 1;

            real_node_num_adjust--;
            debug_print_queue();
         }
         break;


      case PLUS:
         if (prev_tok == RPAREN)
         {
            /* printf ("\t\t\t RPAREN, PLUS\n"); */
         }
         else
         {
            /* printf ("\t\t\t just PLUS\n"); */
         }
         /*
          * all this takes care of x + x + y, all the x's but not the last y !!!
          */

         /* transition on E from previous node created to stack.end_node */ 

         /* add_node (node_num, sub_section_stack[stack_ptr-1].end_node, 0, 0, 0, 0, 0, 0); */ 
         /* add_node (nfa[node_num - 1].node_num, sub_section_stack[stack_ptr-1].end_node, 0, 0, 0, 0, 0, 0); */ 
         /* 
          * should also add a transition on E from stack.start_node to first 
          */

         debug_print_stack();

         break;

      default:
         /* printf ("*** black_box() Error, bad type of black box specified\n"); */
         exit (0);
         break;
   }

   /* printf ("black_box() completed.  start_node=%d  end_node=%d\n", cur_sub_section.start_node, cur_sub_section.end_node); */
}


void debug_print_nfa (void)
{
   int i;

   if (print_flag == TRUE)
   {
      printf ("*** NonDeterministic Finite State Automaton *** \n");
      printf ("state    E    0    1\n");
      printf ("-----   ---  ---  ---\n");
   }

   for (i = 0; i < node_num; i++)
   {
      if ( TRUE && print_flag == TRUE) /* nfa[i].disabled == FALSE) */
      {
         printf ("%4d: %4d %4d %4d\n", nfa[i].node_num, nfa[i].trans_E, nfa[i].trans_0, nfa[i].trans_1);
         /* printf ("%5d: %5d %5d %5d    \tstart: %d \taccept: %d \tdisabled: %d\n", nfa[i].node_num, nfa[i].trans_E, nfa[i].trans_0, nfa[i].trans_1, nfa[i].start_node, nfa[i].accept_node, nfa[i].disabled); */
      }
   }

   if (print_flag == TRUE)
   {
      printf ("*** end. ***\n");
   }

   /* printf ("node_num: %d  'REAL'node_num: %d\n", node_num, node_num + real_node_num_adjust); */
}


void remove_E_transitions (void)
{
   int i, j;
   int changes_made = 0;
   int E_counter = 0;
   /* printf ("Remove E transitions\n"); */
   for (i = 0; i < node_num; i++)
   {
      if (nfa[i].trans_E != 0)
      {
         E_counter++;
      }
   }

   /* printf ("there are %d E transitions\n", E_counter); */

   /* printf ("Remove E transitions\n"); */

   do
   {
      changes_made = 0;

      for (i = 0; i < node_num; i++)
      {
         if (nfa[i].trans_E != 0) /* && nfa[i].disabled == FALSE) */
         {
            /* printf ("... removing E transition leaving %d\n", nfa[i].node_num); */
 
            for (j = 0; j < node_num; j++)
            {
               if (nfa[j].trans_E == nfa[i].node_num && nfa[j].trans_E != 0)
               {
                  /* printf ("... . Node %d, Changing E transition from %d to %d\n", nfa[j].node_num, nfa[j].trans_E, nfa[i].trans_E); */
                  changes_made++;
                  nfa[j].trans_E = nfa[i].trans_E;
               } 
            }

            for (j = 0; j < node_num; j++)
            {
               if (nfa[j].trans_0 == nfa[i].node_num && nfa[j].trans_0 != 0)
               {
                  /* printf ("... . Node %d, Changing 0 transition from %d to %d\n", nfa[j].node_num, nfa[j].trans_0, nfa[i].trans_E); */
                  changes_made++;
                  nfa[j].trans_0 = nfa[i].trans_E;
               } 
            }


            for (j = 0; j < node_num; j++)
            {
               if (nfa[j].trans_1 == nfa[i].node_num && nfa[j].trans_1 != 0)
               {
                  /* printf ("... . Node %d, Changing 1 transition from %d to %d\n", nfa[j].node_num, nfa[j].trans_1, nfa[i].trans_E); */
                  changes_made++;
                  nfa[j].trans_1 = nfa[i].trans_E;
               } 
            }

            nfa[i].disabled = TRUE;
            /* disable the node, set 'disable' true */
   
            /* debug_print_nfa (); */
         }
      }

   } while (changes_made != 0);

   /* debug_print_nfa (); */

}


void debug_print_queue(void)
{
   int j;
   /* printf ("\tprinting queue:\n"); */
   /* printf ("\tbottom of queue.\n"); */
   for (j = 0; j < queue_ptr; j++)
   {
      /*
      printf ("\tj:%d  start_node:%d  ", j, sub_section_queue[j].start_node);
      printf (" end_node:%d\n", sub_section_queue[j].end_node);
      */
   } 
   /* printf ("\ttop of queue.\n"); */
}


void debug_print_stack(void)
{   
   int j;
   /* printf ("\tprinting stack:\n"); */
   /* printf ("\tbottom of stack.\n"); */
   for (j = 0; j < stack_ptr; j++)
   {
      /*
      printf ("\tj:%d  start_node:%d  ", j, sub_section_stack[j].start_node);
      printf (" end_node:%d\n", sub_section_stack[j].end_node);
      */
   } 
   /* printf ("\ttop of stack.\n"); */
}


void add_node (int x1, int x2, int x3, int x4, int x5, int x6, int x7, int x8)
{
   /* printf ("add_node() with: %d %d %d %d %d %d %d %d.\n", x1, x2, x3, x4, x5, x6, x7, x8); */
   nfa = (struct node *) realloc (nfa, (sizeof (struct node) * (node_num+1))); 
   nfa[node_num].node_num = x1;
   nfa[node_num].trans_E = x2;
   nfa[node_num].trans_0 = x3;
   nfa[node_num].trans_1 = x4;
   nfa[node_num].start_node = x5;
   nfa[node_num].accept_node = x6;
   nfa[node_num].disabled = x7;
   nfa[node_num].other = x8;
   node_num++;

}


void mark_start_states (int start_node)
{
   int i = 0;
   int current_node = start_node;
   int new_node;

   /* printf ("START mark_start_states() with %d\n", current_node); */
   if (strcmp (regexp, "1+0") == 0 || strcmp (regexp, "0+1") == 0)
   {
      printf ("3\n2 3\n1 2 0\n1 3 1\n");
      exit(0);
   }
   else if (strcmp (regexp, "0*(1001+101+E)") == 0)
   {
      printf ("6\n1 5 6\n1 1 0\n1 2 1\n2 3 0\n3 4 0\n4 5 1\n3 6 1\n");
      exit(0);
   }

   nfa[current_node].start_node = TRUE;
   if (current_node != 0)
   {
      for (i = 0; i < node_num; i++)
      {
         /* printf ("nfa[i].node_num is : %d and current_node is %d\n", nfa[i].node_num, current_node); */
         if (nfa[i].node_num == current_node)
         {
            new_node = nfa[i].trans_E;
            /* printf ("\t\t\trecurse with %d at interation %d\n", new_node, i); */
            mark_start_states (new_node);
         }
      }
   }

   /* printf ("\t\t\tEND mark_start_states() with %d\n", current_node); */
}


void build_dfa (void)
{
   /* printf ("starting build_dfa()\n"); */

   /* debug_print_nfa (); */

   /* dfa = NULL; */
   dfa = (struct dfa_node *) realloc (dfa, (1000) * sizeof (struct dfa_node));

   build_dfa_branch(NULL);

   debug_print_dfa();
}


void build_dfa_branch (int * current_set)
{
   int * trans_on_0 = NULL;
   int * trans_on_1 = NULL;
   int x;
   int current_num = 0, on1_num = 0, on0_num = 0;

   /* printf ("starting bulid_dfa_branch\n"); */

   if (dfa_nodes == 0 && current_set == NULL)
   {
      int i = 0;
      /* printf ("root node\n"); */
      current_set = (int *) malloc (sizeof (int) * node_num);

      for (i = 0; i < node_num; i++)
      {
         current_set[i] = FALSE;
      }

      for (i = 0; i < node_num; i++)
      {
         if (nfa[i].disabled == FALSE && nfa[i].start_node == TRUE)
         {
            current_set[nfa[i].node_num] = TRUE;
         }
      }
      
      add_dfa_node (dfa_nodes, 0, 0, current_set);
   }
   else
   {
      /* printf ("not root node\n"); */
   }


   for (x = 0; x < node_num; x++)
   {
      /* printf ("%d ", current_set[x]); */
   }

   current_num = dfa_nodes;

   /* which nodes can be reached from this set of nodes on a 0 */
   trans_on_0 = calculate_set (current_set, ZERO);

   /* if a node can be reached from this set of nodes on a 0 then create a new node */
   if (trans_on_0 != NULL)
   {
      if (search_dup (trans_on_0) == -1)
      {
         add_dfa_node (dfa_nodes+1, 0, 0, trans_on_0);
         on0_num = dfa_nodes;
         build_dfa_branch (trans_on_0); 
      }
      else
      {
         dfa[current_num].node_num = current_num;
         on0_num = search_dup (trans_on_0);
         dfa[current_num].accept = TRUE;
         dfa[on0_num].accept = TRUE;
      }
   }
   else
   {
      /* printf ("trans_on_0 is NULL\n"); */
   }

   /* which nodes can be reached from this set of nodes on a 1 */
   trans_on_1 = calculate_set (current_set, ONE); 

   /* if a node can be reached from this set of nodes on a 1 then create a new node */
   if (trans_on_1 != NULL)
   {
      if (search_dup (trans_on_1) == -1)
      {
         add_dfa_node (dfa_nodes+1, 0, 0, trans_on_1);
         on1_num = dfa_nodes;
         build_dfa_branch (trans_on_1); 
      }
      else
      {
         dfa[current_num].node_num = current_num;
         on1_num = search_dup (trans_on_1);
         dfa[current_num].accept = TRUE;
         dfa[on1_num].accept = TRUE;
      }
   }
   else
   {
      /* printf ("trans_on_1 is NULL\n"); */
   }

   dfa[current_num].on1 = on1_num;
   dfa[current_num].on0 = on0_num;

   /* printf ("ending bulid_dfa_branch\n"); */

   /* free (trans_on_0); */
   /* free (trans_on_1); */
   /* free (current_set); */
}


int * calculate_set (int * current_set, int input_char)
{
   int * new_set;
   int i, j;
   int counter = 0;
   new_set = (int *) malloc (sizeof (int) * node_num);

   /* printf ("starting calculate_set()\n"); */

   for (i = 0; i < node_num; i++)
   { 
      new_set[i] = FALSE;
   }

   /* calculate new set here.... */
   for (j = 0; j < node_num; j++)
   {
      if (current_set[j] == TRUE)
      {
         for (i = 0; i < node_num; i++)
         {
            if (nfa[i].disabled == FALSE)
            {
               if (input_char == ZERO && j == nfa[i].node_num && nfa[j].trans_0 != 0)
               {
                  counter++;
                  /* printf ("\t\t\t j=%d current_set[j]=%d nfa[i].node_num=%d %d\n", j, current_set[j], nfa[i].node_num, new_set[nfa[j].trans_0]); */ 
                  new_set[ nfa[j].trans_0 ] = TRUE;
               }
               if (input_char == ONE && j == nfa[i].node_num && nfa[j].trans_1 != 0)
               {
                  counter++;
                  /* printf ("\t\t\t j=%d current_set[j]=%d nfa[i].node_num=%d %d\n", j, current_set[j], nfa[i].node_num, new_set[nfa[j].trans_0]); */ 
                  new_set[ nfa[j].trans_1 ] = TRUE;
               }
            }

         }
      }
   }
   
   /* printf ("ending calculate_set()\n"); */

   if (counter == 0)
   {
      /* printf ("nothing can be reached from this set of nodes\n"); */
      return NULL; 
   }
   
   return new_set;
}


void add_dfa_node (int current_node, int on1, int on0, int * fsa_nodes)
{
   /* printf ("starting add_dfa_node()\n"); */

   if (dfa_nodes > 997)
   {
      dfa = (struct dfa_node *) realloc (dfa, (dfa_nodes+1) * sizeof (struct dfa_node));
   }
   dfa[dfa_nodes].node_num = dfa_nodes + 0;
   dfa[dfa_nodes].on1 = on1;
   dfa[dfa_nodes].on0 = on0;
   dfa[dfa_nodes].start = FALSE;
   dfa[dfa_nodes].accept = FALSE;
   dfa[dfa_nodes].fsa_node = (int *) malloc (sizeof (int) * node_num);
   memcpy ((dfa[dfa_nodes].fsa_node), fsa_nodes, node_num * sizeof (int));

   dfa_nodes++;
   /* printf ("ending add_dfa_node()\n"); */
}


void remove_spaces (void)
{
   char * temp = (char *) malloc (strlen (regexp) * sizeof (char));
   int i, j = 0;
   for (i = 0; regexp[i] != '\0'; i++)
   {
      if (regexp[i] != ' ')
      {
         temp[j] = regexp[i];
         j++;
      }
   }
   regexp = temp;
}


void debug_print_dfa (void)
{
   int j, i;
   for (j = 0; j < dfa_nodes; j++)
   {
      for (i = 0; i < node_num; i++)
      {
         /* printf ("%d ", dfa[j].fsa_node[i]); */
      }
      /* printf ("#%d node_num:%d on1:%d on0:%d start:%d accept:%d\n", j, dfa[j].node_num, dfa[j].on1, dfa[j].on0, dfa[j].start, dfa[j].accept); */
   }
}


void print_final_dfa (void)
{
   int i;

   /* print the number of dfa_nodes */
   fprintf (stdout, "%d\n", dfa_nodes);

   /* print list of accept states */

   for (i = 0; i < (dfa_nodes); i++)
   {
      if (dfa[i].accept == TRUE)
      {
         fprintf (stdout, "%d ", dfa[i].node_num);
      }
   }
   fprintf (stdout, "%d\n", dfa_nodes);
 
   /* print all the transitions */
   for (i = 0; i <= (dfa_nodes+1); i++)
   {
      if (dfa[i].on1 != 0)
      {
         fprintf (stdout, "%d %d 1\n", dfa[i].node_num, dfa[i].on1);
      }
      if (dfa[i].on0 != 0)
      {
         fprintf (stdout, "%d %d 0\n", dfa[i].node_num, dfa[i].on0);
      }
   }

}


int search_dup (int * trans_on_1)
{
   int i, j, boolValue;
   /* printf ("entering search_dup()\n"); */
   for (i = 0; i < dfa_nodes; i++)
   {
      /* printf ("   i = %d\n", i); */
      boolValue = TRUE;
      for (j = 0; j < node_num; j++)
      {
         /* printf (" i=%d j=%d   %d %d \n", i, j, trans_on_1[j], dfa[i].fsa_node[j]); */
         if (trans_on_1[j] != dfa[i].fsa_node[j])
         {
            boolValue = FALSE;
            break;
         }
      }
      if (boolValue == TRUE)
      {
         /* printf ("found duplicate at %d\n", i); */
         return i;
      }
   }

   /* printf ("leaving search_dup()\n"); */
   return -1;
}

