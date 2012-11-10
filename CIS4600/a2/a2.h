/* CIS*4600 Elements of Theory of Computation
   Assignment #2, Due: Thursday November 2, 2000
   Michael Bochenek ID: 0041056 */


#include <stdio.h>
#include <stdlib.h>

/* *** #define's *** */

#define ZERO 257
#define ONE 258
#define EPSILON 259
#define RPAREN 260
#define LPAREN 261
#define PLUS 262
#define ASTERIX 263

#define TRUE 1
#define FALSE 0

/* *** structures *** */

struct sub_section 
{
   int start_node;
   int end_node;
};

struct node
{
   int node_num;
   int trans_E;
   int trans_1;
   int trans_0;
   int start_node;
   int accept_node;
   int disabled;
   int other;
};

struct dfa_node
{
   int node_num;
   int * fsa_node;
   int on1;
   int on0;
   int start;
   int accept;
};

/* *** global variables *** */

struct sub_section cur_sub_section;
struct sub_section prev_sub_section;

struct sub_section sub_section_queue[100];
int queue_ptr = 0;
struct sub_section sub_section_stack[100];
int stack_ptr = 0;

struct dfa_node * dfa;
int dfa_nodes = 0;

int cur_tok = 0;
int prev_tok = 0;
int level = 0;

int counter = 0;
char * str;

struct node * nfa;
int node_num = 0;
int real_node_num_adjust = 0;

char * regexp;
int regexp_ptr = 0;
int print_flag = FALSE;

/* *** function prototypes *** */

void eat(int token); 
void advance (void);

void program (void);
void expr (void);
void literal (void);

void debug_print_stack (void);
void debug_print_queue (void);
void black_box (int type_of_box);

void remove_spaces (void);
void remove_E_transitions (void);
void debug_print_nfa (void);
void add_node (int x1, int x2, int x3, int x4, int x5, int x6, int x7, int x8);
void mark_start_states (int start_node);

void build_dfa (void);
void add_dfa_node (int current, int on1, int on0, int * fsa_nodes);
void build_dfa_branch (int * current_set);
int * calculate_set (int * current_set, int input_char);
void debug_print_dfa(void);

void print_final_dfa (void);
int search_dup (int * trans_on_1);

