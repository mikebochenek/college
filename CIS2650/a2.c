/* Assignment #2 --- .C CODE
   Finite State Automaton Testing
   by:  Michael Bochenek 980492820 */

#include<stdio.h>
#include<stdlib.h>

struct node {
   int num; 
   int end_node;
   struct node * branch[128]; 
   /* if x is an acceptable transition then branch[x] points to the
      appropriate state, if not, then it points to NULL */
};

int test_word(char *, struct node *, FILE *);
struct node * build_fsa(FILE *);
/* john's function protytypes */

void determine_three_streams(int, char * [], FILE **, FILE **, FILE **);
char * determine_argument(int, char * [], char);
void test_strings(struct node *, FILE *, FILE *);
void fsa_error(void);
void file_error(void);
int get_number_of_nodes(FILE *);
void create_transition_rules(FILE *, struct node **);
void build_accept_states(FILE *, int, struct node **);
/* my function prototypes */

/* ------------------------------ M A I N ------------------------------- */
int main(int argc, char * argv[]) {
   struct node * fsa_start; /* points to start of fsa list */
   FILE * fsa_specifications, * strings_to_test, * output_target;
   /* three streams that are used throughout */

   /* determine based on command line arguments, and open streams */
   determine_three_streams(argc, argv, &fsa_specifications,
			 &strings_to_test, &output_target);

   /* build fsa list based on the fsa specification file opened */
   fsa_start = build_fsa(fsa_specifications);

   /* test strings from file specified or stdin */
   test_strings(fsa_start, strings_to_test, output_target);

   if (strings_to_test != stdin)
      fclose(strings_to_test); /* close file unless there is no file (stdin) */
   if (output_target != stdout)
      fclose(output_target); /* close file unless there is no file (stdout) */

   return 0; /* bye-bye */
}

void determine_three_streams(int argc, char * argv[],
   FILE ** fsa_specifications, FILE ** strings_to_test,
   FILE ** output_target) {
   /* pointer to a pointer, meaning these variables in main will be modified */

   char * fsa_specs, * test_strings, * output;

   /* if no command line arguments output error and exit */
   if (argc < 3)
      fsa_error();

   fsa_specs = determine_argument(argc, argv, 'f');
   /* check if a fsa specification is in command line and return filename */
   if (fsa_specs == 0)
      fsa_error(); /* if no fsa spex file, print error and exit */
   else {
      *fsa_specifications = fopen(fsa_specs, "r");
      /* ----- open fsa spex file and MODIFY the fsa file pointer in MAIN */
      if (fsa_specifications == NULL)
         file_error(); /* if unable to open file print error and exit */
   }

   test_strings = determine_argument( argc, argv, 's');
   /* check if a test string file is in command file name and return filename */
   if (test_strings == 0)
      *strings_to_test = stdin; /* if no file specified, use stdin */
   else {
      *strings_to_test = fopen(test_strings, "r");
      /* ----- open test strings file and MODIFY strings file pointer in MAIN */
      if (strings_to_test == NULL)
         file_error(); /* if unable to open file print error and exit */
   }

   output = determine_argument( argc, argv, 'o');
   /* check if an output file is in command line and return filename */
   if (output == 0)
      *output_target = stdout; /* if no output file specified, use stdout */
   else {
      *output_target = fopen(output, "w");
      /* ----- open output file and MODIFY the output file pointer in MAIN */
      if (output_target == NULL)
         file_error(); /* if unable to open file print error and exit */
   }
}

void fsa_error(void) { /* print error if no fsa spex file */
   fprintf(stderr, "You must specify the FSA specifications file\n");
   fprintf(stderr, "(ie:  a.out -f fsa.dat\n");
   exit(-1);
}

void file_error(void) { /* print error if unable to open any file */
   fprintf(stderr, "There was an error opening the file\n");
   fprintf(stderr, "Please try again.\n");
   exit(-1);
}    

char * determine_argument(int argc, char * argv[] ,char letter) {
   /* check if the 1th, 3th, or 5th argument equals to "-f" / "-s" / "-o"
      assume (n+1)th argument contains the appropriate filename.
      if no argument equals "-f" / "-s" / "-o", return 0 */

   if (argv[1][0] == '-' && argv[1][1] == letter)
      return argv[2]; 
      /* if 1st argument is '-letter' return the string that follows */

   else if (argc >= 5) /* check if there is at least 5 command line args */
      if (argv[3][0] == '-' && argv[3][1] == letter)
	 return argv[4];
         /* if 3rd argument is '-letter' return the string that follows */

   else if (argc >= 7) /* check if there is at least 7 command line args */
      if (argv[5][0] == '-' && argv[5][1] == letter)
	 return argv[6];
         /* if 5th argument is '-letter' return the string that follows */

   return 0; /* '0' is returned if '-letter' is not found */
}

struct node * build_fsa(FILE * fsa_specifications)  {
/* create finite state automaton based on specifications in the 'fsa file' */
   struct node * start_node;
   int number_of_nodes, i; /* temp */
   struct node ** list_of_nodes; /* will become array of pointers to nodes */

   number_of_nodes = get_number_of_nodes(fsa_specifications);

   list_of_nodes = malloc(sizeof(struct node *) * number_of_nodes);
   /* create an array of pointers that will point to each node */

   for (i = 0; i < number_of_nodes; i++) { /* for each state/node */
      list_of_nodes[i] = (struct node *) malloc(sizeof(struct node));
      /* create a node in memory (according to structure provided)
	 and add the address of the node to array of pointers */
      list_of_nodes[i]->num = i;
      /* label th node as state# (set 'num' field of node) */
   }

   start_node = list_of_nodes[0]; /* point start_node to first node */

   build_accept_states(fsa_specifications, number_of_nodes, list_of_nodes);

   create_transition_rules(fsa_specifications, list_of_nodes);

   free(list_of_nodes);
   /* array of pointers to each node is no longer necessary
      (in other words, this is not a linked list, meaning that the n-th
      item does not point to the n+1-th item (or n-1-th item) ) */

   fclose(fsa_specifications);
   /* close file */
   return start_node;
}

int get_number_of_nodes(FILE * fsa_specifications) {
   int number_of_nodes;
   fscanf(fsa_specifications, "%d", &number_of_nodes);
   /* read number of state/nodes from file */

   if (number_of_nodes < 1) { /* error and exit if there is less than 1 node */
      fprintf(stderr, "You need to have at least one node\n");
      fprintf(stderr, "(The FSA file specifies only %d nodes)\n",
	      number_of_nodes);
      exit(-1);
   } /* make sure that there is at least one state */

   return number_of_nodes;
}

void build_accept_states(FILE * fsa_specifications, int number_of_nodes,
			 struct node ** list_of_nodes) {
   int accept_state;

   /* read the list of accept states */
   fscanf(fsa_specifications, "%d", &accept_state); /* read */

   while (accept_state != -1) { /* keep reading until '-1' */
      if (accept_state < 0 || accept_state >= number_of_nodes) {
      /* if accept state is inappropriate then print error and exit
         (inappropriate = less then 0 or higher or equal to number of nodes) */
	 fprintf(stderr, "Inappropriate accept state\n");
	 exit(-1);
      }

      list_of_nodes[accept_state]->end_node = 1;
      /* go to the appropriate node (according to array of pointers)
	 set the 'end_node' field of the node to true */
      fscanf(fsa_specifications, "%d", &accept_state); /* read */
   }
}

void create_transition_rules(FILE * fsa_specifications,
			     struct node ** list_of_nodes) {

   /* read the transition rules */
   while (!feof(fsa_specifications)) { /* keep reading until eof */
      int from_node, to_node;
      char transition;

      fscanf(fsa_specifications, "%d", &from_node); /* read stuff from file */
      fscanf(fsa_specifications, "%d", &to_node);
      fscanf(fsa_specifications, " %c", &transition);

      list_of_nodes[from_node]->branch[(int) transition] =
			     list_of_nodes[to_node];
   /* go to the 'from' node and mark that there is a path to the 'to' node
      (in the 'from' node set the right pointer to point to the 'to' node) */
   }
}

void test_strings(struct node * fsa_start, FILE * strings_to_test,
		  FILE * output_target) { /* read and test strings */
/* (read either from stdin or file specified in command line)
   (output results to stdout or file specified in command line) */
   char *one_line = NULL;
   int i;

   while (!feof(strings_to_test)) { /* loop until end of file */
      i = 0;
      do {
	 one_line = realloc(one_line, i  + 1); /* increase size of array */
	 fscanf(strings_to_test, "%c", &one_line[i]); /* read from file */
	 i++;
      } while (one_line[i - 1] != '\n'); /* keep reading until '\n' */
      one_line[i - 1] = '\0'; /* change '\n' to '\0' */

      if (!feof(strings_to_test)) /* if end-of-file then "nothing to do" */
	 if (test_word(one_line, fsa_start, output_target) == 1)
	 /* output "path of string" and return 0=REJECT or 1=ACCEPT */
	    fprintf(output_target, " -> ACCEPT\n");
	 else
	    fprintf(output_target, " -> REJECT\n");
       free(one_line); /* free mem used by one_line */
   }
}

int test_word(char * one_line, struct node * start_node, FILE * output) {
   struct node * current_node = start_node;
   int i;

   fprintf(output, "%s START 0", one_line);
   /* print out the entire string and the string  "START 0" */

   for (i = 0; one_line[i] != '\0'; i++) {
   /* start at state #0 read characters until '\0' */

      if (current_node->branch[(int) one_line[i]] != NULL) {
      /* if this character allows for a transition */
	 current_node = current_node->branch[(int) one_line[i]];
	 /* follow the pointer to the 'to' node */
	 fprintf(output, " -> %d", current_node->num);
	 /* print out " -> " followed by the number of the 'to' node */
      }
      else { /* if this character does not allow for a transition */
	 return 0;
	 /* stop testing the other characters
	    (ignore everything up to and including '\0') */
      }
   }

   if (current_node->end_node == 0)
   /* if the current node is not accept state print out " -> REJECT\n" */
      return 0;
   return 1;
   /* if the current node is an accept state print out " -> ACCEPT\n" */
}
