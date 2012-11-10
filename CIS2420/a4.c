/* ------------------------------------------------------------------------  
    Michael Bochenek
    ID: 0041056 (980492820)
    mboche01@uoguelph.ca
    CIS 242
    Assignment #4
    July 28, 1999
   ------------------------------------------------------------------------ */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_ENTRY 20
#define KILOBYTE 1024
#define MAX 4
#define MIN 2

#define Key char *

/* ----- MACRO DEFINITIONS ------ */
#define EQ(A, B) ( !strcmp((A), (B)) )
#define LE(A, B) ( strcmp ((A), (B)) <= 0 )
#define LT(A, B) ( strcmp ((A), (B)) < 0 )

typedef enum boolean {FALSE, TRUE} Boolean;

/* -------- STRUCTURES ---------- */
typedef struct entry {
   int table_index;
   char *key;
} Treeentry;

typedef struct treenode Treenode;
struct treenode {
   int count;
   Treeentry entry[MAX + 1];
   Treenode *branch[MAX + 1];
};

typedef struct student {
   int student_ID;
   int phone;
   char *first_name;
   char *last_name;
   char *name;
} Student;

/* -------------------- FUNCTION PROTOTYPES ------------------------ */
void ReadFile (Student *table);
void UserInteraction (Student *table, Treenode **root);
char *GetName (void);
void PrintStuff (Student *table);
void RecQuickSort (Student *table, int low, int high);
int Partition (Student *table, int low, int high);
void Swap (Student *table, int x, int y); 

Treenode *StartBTree (Treenode *root, Student *table);

Treenode *SearchTree (Key target, Treenode *root, int *targetpos, int flag);
Boolean SearchNode (Key target, Treenode *current, int *pos, int flag);
Treenode *InsertTree (Treeentry newentry, Treenode *root);
Boolean PushDown (Treeentry newentry, Treenode *current, Treeentry *medentry,
                  Treenode **medright);
void PushIn (Treeentry medentry, Treenode *medright, Treenode *current, 
             int pos);
void Split (Treeentry medentry, Treenode *medright, Treenode *current, int pos,
            Treeentry *newmedian, Treenode **newright);
Treenode *DeleteTree (Key target, Treenode *root);
void RecDeleteTree (Key target, Treenode *current);
void Remove (Treenode *current, int pos); 
void Successor (Treenode *current, int pos);
void Restore (Treenode *current, int pos);
void MoveRight (Treenode *current, int pos);
void MoveLeft (Treenode *current, int pos);
void Combine (Treenode *current, int pos);
void Combine (Treenode *current, int pos);
void PrintBTree (Treenode *root);
void InOrder (Treenode *root);

/* ------------------------------- MAIN ------------------------------ */
int main () {
   Treenode *myTree;
   Student table [MAX_ENTRY]; /* <-- DATA TABLE */

   ReadFile (table);
   RecQuickSort (table, 0, MAX_ENTRY - 1);

   PrintStuff (table);
   myTree = StartBTree (myTree, table);
   PrintBTree (myTree);   

   UserInteraction (table, &myTree);

   return 0; /* bye-bye */
}

/* -------------------------- START-B-TREE ---------------------------- */
Treenode *StartBTree (Treenode *root, Student *table) {
   int i;
   Treeentry newentry;

   root = NULL;
   for (i = 0; i < MAX_ENTRY; i++) {
      newentry.table_index = i;
      newentry.key = table[i].name;
      root = InsertTree (newentry, root);   
   } 
   return root;
}

/* ----------------------------- READFILE ---------------------------- */
void ReadFile (Student *table) {
   int i;
   char *token;
   char one_line[KILOBYTE];
   FILE *filename = NULL;

   /* initialize values in the table */
   for (i = 0; i < MAX_ENTRY; i++) {
      table[i].student_ID = -1;
      table[i].phone = -1;
      table[i].first_name = NULL;
      table[i].last_name = NULL;
      table[i].name = NULL;
   }

   /* NEXT 5 LINES:  open the file, and check for errors */
   filename = fopen ("data4.dat", "r");
   if (filename == NULL) {
      printf("Error opening file\n");
      exit (-1);
   } /* open file data3.dat and check for 'fopen' errors */

   /* NEXT x LINES:  read all the words and close the file */
   for (i = 0; i < MAX_ENTRY; i++) {
      fgets (one_line, KILOBYTE, filename);
      /* read in a line */

      /* get the student id */
      strtok (one_line, " ");
      table[i].student_ID = atoi (one_line);

      /* get the first name */
      token = (char *) strtok (NULL, " ");
      table[i].first_name = (char *) malloc (strlen (token) + 1);
      strcpy (table[i].first_name, token);
      /* allocate memory for the first name, and copy it from the token */

      /* get the last name */
      token = (char *) strtok (NULL, " \n");
      table[i].last_name = (char *) malloc (strlen (token) + 1);
      strcpy (table[i].last_name, token);
      /* allocate memory for the last name, and copy it from the token */

      /* get the phone number */
      token = (char *) strtok (NULL, " ");
      table[i].phone = atoi (token);

      /* NEXT couple of lines:  create last_name+first_name key for sorting */
      table[i].name = (char *) malloc ( strlen (table[i].last_name)
	                              + strlen (table[i].first_name) + 1);
      /* allocate memory for the new key */
      strcpy (table[i].name, table[i].last_name);
      strcat (table[i].name, table[i].first_name);
      /* first copy the last name, and then concatentate the first name */
   }

   fclose (filename);
}

/* ---------------------------- USERINTERACTION --------------------------- */
void UserInteraction (Student *table, Treenode **root) {
   int selection;
   char user_input[KILOBYTE];
   Treenode *found;
   int targetpos = 0;

   do {
      /* print the two tables every single time */
      
      printf ("\n------- MENU -------\n");
      printf ("1.  Search with a target name \n");
      printf ("2.  Delete an entry with a target name \n");
      printf ("3.  Exit \n");
      printf ("Enter you selection: ");

      fgets (user_input, KILOBYTE, stdin);
      selection = atoi (user_input);
      /* get the user input from the keyboard and convert it integer */

      if (selection == 1) {
         char *search_for;
         search_for = GetName(); 
         if (search_for == NULL) {
            continue;
         }
         found = SearchTree (search_for, *root, &targetpos, 1);
         if (found == NULL) { 
            printf ("\n%s not found.", search_for);
         } else {
            int i = found->entry[targetpos].table_index;
            printf ("\n%3d ", i); /* always print the number */
            printf ("%6d ", table[i].student_ID);
            printf ("%-15s ", table[i].first_name);
            printf ("%-15s ", table[i].last_name);
            printf ("%-15d \n", table[i].phone);
         }
      }

      else if (selection == 2) {
         char *delete;
         delete = GetName();
         if (delete == NULL) { 
            continue;
         }
         found = SearchTree (delete, *root, &targetpos, 1);
         if (found == NULL) {
            printf ("\n%s not found and not deleted.", delete);
         } else {
            int i = found->entry[targetpos].table_index;
            table[i].student_ID = 0;
            table[i].first_name = " ";
            table[i].last_name = " ";
            table[i].phone = 0;
            
            printf ("\n%s found and deleted.\n", delete);

            *root = DeleteTree (delete, *root); 
            PrintStuff (table);
            PrintBTree (*root);  
         } 
      }

   } while (selection != 3);
   /* keep loopint until the user enters '4' */

}

/* ----------------------------- GETNAME ----------------------------- */
char *GetName (void) {
   char user_search[KILOBYTE];
   char *search_for;        
   char first[KILOBYTE];
   int i = 0;
 
   printf ("Please enter the name (first_name last_name): ");
   fgets (user_search, KILOBYTE, stdin); 
   /* prompt for names, get them, and REVERSE..... */         

   if (strstr (user_search, "\n") == NULL ||
       strstr (user_search, " ") == NULL) {
      return NULL;
   } 

   /* ### REVERSE PROCESS */
   search_for = (char *) malloc (strlen (user_search) + 1);
   user_search[strlen (user_search) - 1] = '\0';
   while (user_search[i] != ' ') {
      first[i] = user_search[i];
      i++;
   }
   first[i] = '\0';
   i++;
   strcpy (search_for, &user_search[i]);
   strcat (search_for, first);
   /* ### END OF REVERSE PROCESS */ 

   return search_for;
}

/* ------------------------ PRINT STUFF --------------------------- */
void PrintStuff (Student *table) {
   int i;

   printf ("--- DATA TABLE --- \n");

   /* loop through all entries in the table */
   for (i = 0; i < MAX_ENTRY; i++) {
      printf ("%3d ", i); /* always print the number */
      printf ("%6d ", table[i].student_ID);
      printf ("%-15s ", table[i].first_name);
      printf ("%-15s ", table[i].last_name);
      printf ("%-15d \n", table[i].phone);
   }
}

/* --------------------------- PRINT B-TREE ---------------------------- */
void PrintBTree (Treenode *root) {
   printf ("--- IN-ORDER B-TREE ---\n");

   InOrder (root);
}

/* ---------------------------- IN ORDER -------------------------------- */
void InOrder (Treenode *my_root) {
   int i; 
   if (my_root) {
      InOrder (my_root->branch[0]);

      for (i = 1; i < my_root->count + 1; i++) {
         printf ("%-30s ", my_root->entry[i].key);
         printf ("%4d\n", my_root->entry[i].table_index);
         InOrder (my_root->branch[i]);
      }
   }   
}

/* ---------------------------- RECURSIVE QUICKSORT ---------------------- */
void RecQuickSort (Student *table, int low, int high) {
		   
   int pivotpos = 0;
   if (low < high) {
      pivotpos = Partition (table, low, high);
      /* divide into two sublists */
      RecQuickSort (table, low, pivotpos - 1);
      /* sort lower sublist */
      RecQuickSort (table, pivotpos + 1, high);
      /* sort upper sublist */
   }
}

/* ------------------------------- PARTITION --------------------------- */
int Partition (Student *table, int low, int high) {
   /* will not comment cuz it's straight from the book */
   int pivot;
   int i;
   int pivotpos;

   /* SWAP */
   Swap (table, low, (low + high) / 2);

   pivot = table[low].student_ID;
   pivotpos = low;
   for (i = low + 1; i <= high; i++) {
      if (table[i].student_ID < pivot) {
	 /* SWAP */
	 pivotpos++;
         Swap (table, i, pivotpos);
      }
   }

   /* SWAP */
   Swap (table, low, pivotpos);

   return pivotpos;
}

/* ----------------------------------- SWAP ------------------------- */
void Swap (Student *table, int x, int y) {
   /* just swaps two (x and y) student entries in table */
   Student temp;

   temp.student_ID = table[x].student_ID;
   temp.phone = table[x].phone;
   temp.first_name = table[x].first_name;
   temp.last_name = table[x].last_name;
   temp.name = table[x].name;

   table[x].student_ID = table[y].student_ID;
   table[x].phone = table[y].phone;
   table[x].first_name = table[y].first_name;
   table[x].last_name = table[y].last_name;
   table[x].name = table[y].name;
   
   table[y].student_ID = temp.student_ID; 
   table[y].phone = temp.phone; 
   table[y].first_name = temp.first_name; 
   table[y].last_name = temp.last_name;
   table[y].name = temp.name; 
}

/* ------------------------------ SEARCH TREE ----------------------- */
Treenode *SearchTree (Key target, Treenode *root, int *targetpos, int flag) {
   if (!root) {
      return NULL;
   }

   else if (SearchNode (target, root, targetpos, flag)) {
      return root;
   }

   else {
      return SearchTree (target, root->branch[*targetpos], targetpos, flag);
   }
}

/* ---------------------------- SEARCH NODE ------------------------ */
Boolean SearchNode (Key target, Treenode *current, int *pos, int flag) {
   if (LT (target, current->entry[1].key)) {
      if (flag == 1) {
         printf ("Comparing %s to %s\n", target, current->entry[1].key);
      }
      *pos = 0;
      return FALSE;
   } else {
      for (*pos = current->count; 
           LT (target, current->entry[*pos].key) && *pos > 1;
           (*pos)--) {
         if (flag == 1) {
            printf ("Comparing %s to %s\n", target, current->entry[*pos].key);
         }
      } 
      if (flag == 1) {
         printf ("Comparing %s to %s\n", target, current->entry[*pos].key);
      }
      return EQ (target, current->entry[*pos].key);
   }
}

/* ------------------------ INSERT TREE ---------------------------- */
Treenode *InsertTree (Treeentry newentry, Treenode *root) {
   Treeentry medentry;
   Treenode *medright;
   Treenode *newroot;

   if (PushDown (newentry, root, &medentry, &medright)) {
      newroot = (Treenode *) malloc (sizeof (Treenode));
      newroot->count = 1;
      newroot->entry[1] = medentry;
      newroot->branch[0] = root;
      newroot->branch[1] = medright;
      return newroot;
   }
   return root;
}

/* -------------------------- PUSH DOWN --------------------------- */
Boolean PushDown (Treeentry newentry, Treenode *current, Treeentry *medentry,
                  Treenode **medright) {
   int pos;
   if (current == NULL) {
      *medentry = newentry;
      *medright = NULL;
      return TRUE;
   } else {
      if (SearchNode (newentry.key, current, &pos, 0)) {
         printf ("Inserting duplicate key into B-tree\n");
      }
      if (PushDown (newentry, current->branch[pos], medentry, medright)) {
         if (current->count < MAX) {
            PushIn (*medentry, *medright, current, pos);
            return FALSE;
         } else {
            Split (*medentry, *medright, current, pos, medentry, medright);
            return TRUE;
         }
      }
      return FALSE;
   }
}
 
/* ------------------------------ PUSH IN --------------------- */
void PushIn (Treeentry medentry, Treenode *medright, Treenode *current, 
             int pos) {
   int i;
   for (i = current->count; i > pos; i--) {
      current->entry[i + 1] = current->entry[i];
      current->branch[i + 1] = current->branch[i];
   }

   current->entry[pos + 1] = medentry;
   current->branch[pos + 1] = medright;
   current->count++;
}

/* -------------------------------- SPLIT --------------------------- */
void Split (Treeentry medentry, Treenode *medright, Treenode *current, int pos,
            Treeentry *newmedian, Treenode **newright) {
   int i;
   int median;
   if (pos <= MIN) {
      median = MIN;
   } else {
      median = MIN + 1;
   }

   *newright = (Treenode *) malloc (sizeof (Treenode));
   for (i = median + 1; i <= MAX; i++) {
      (*newright)->entry[i - median] = current->entry[i];
      (*newright)->branch[i - median] = current->branch[i];
   }
   (*newright)->count = MAX - median;
   current->count = median;
   if (pos <= MIN) {
      PushIn (medentry, medright, current, pos);
   } else {
      PushIn (medentry, medright, *newright, pos - median);
   }

   *newmedian = current->entry[current->count];
   (*newright)->branch[0] = current->branch[current->count];
   current->count --;
}

/* ----------------------------- DELETE TREE ------------------------- */
Treenode *DeleteTree (Key target, Treenode *root) {
   Treenode *oldroot;
   RecDeleteTree (target, root);
   if (root->count == 0) {
      oldroot = root;
      root = root->branch[0];
      free(oldroot);
   }
   return root;
}

/* ------------------------- RECURSIVE DELETE TREE -------------------- */
void RecDeleteTree (Key target, Treenode *current) {
   int pos;
   if (!current) {
      printf ("Target was not in the B-tree\n");
      return;
   } else {
      if (SearchNode (target, current, &pos, 0)) {
         if (current->branch[pos - 1]) {
            Successor (current, pos);
            RecDeleteTree (current->entry[pos].key, current->branch[pos]);
         } else {
            Remove (current, pos);
         }
      } else {
         RecDeleteTree (target, current->branch[pos]);
      }
      if (current->branch[pos]) {
         if (current->branch[pos]->count < MIN) {
            Restore (current, pos);
         }
      }
   }
}

/* ---------------------------- REMOVE ---------------------------- */
void Remove (Treenode *current, int pos) {
   int i;
   for (i = pos + 1; i <= current->count; i++) {
      current->entry[i - 1] = current->entry[i];
      current->branch[i - 1] = current->branch[i];
   }
   current->count--;
}

/* ---------------------------- SUCCESSOR ------------------------------ */
void Successor (Treenode *current, int pos) {
   Treenode *leaf;
   for (leaf = current->branch[pos]; leaf->branch[0]; leaf = leaf->branch[0])
      ;
   current->entry[pos] = leaf->entry[1];
}

/* --------------------------- RESTORE --------------------------------- */
void Restore (Treenode *current, int pos) {
   if (pos == 0) {
      if (current->branch[1]->count > MIN) {
         MoveLeft (current, 1);
      } else {
         Combine (current, 1);
      }
   } else if (pos == current->count) {
      if (current->branch[pos - 1]->count > MIN) {
         MoveRight (current, pos);
      } else {
         Combine (current, pos);
      }
   } else if (current->branch[pos - 1]->count > MIN) {
      MoveRight (current, pos);
   } else if (current->branch[pos + 1]->count > MIN) {
      MoveLeft (current, pos + 1);
   } else {
      Combine (current, pos);
   }
}

/* ----------------------------- MOVE RIGHT --------------------------- */
void MoveRight (Treenode *current, int pos) {
   int c;
   Treenode *t;
   t = current->branch[pos];
   for (c = t->count; c > 0; c--) {
      t->entry[c + 1] = t->entry[c];
      t->branch[c + 1] = t->branch[c];
   }
   t->branch[1] = t->branch[0];
   t->count++;
   t->entry[1] = current->entry[pos];
   t = current->branch[pos - 1];
   current->entry[pos] = t->entry[t->count];
   current->branch[pos]->branch[0] = t->branch[t->count];
   t->count--;
}

/* ----------------------------------- MOVE LEFT ---------------------- */
void MoveLeft (Treenode *current, int pos) {
   int c;
   Treenode *t;
   t = current->branch[pos - 1];
   t->count++;
   t->entry[t->count] = current->entry[pos];
   t->branch[t->count] = current->branch[pos]->branch[0];
   t = current->branch[pos];
   current->entry[pos] = t->entry[1];
   t->branch[0] = t->branch[1];
   t->count--;
   for (c = 1; c <= t->count; c++) {
      t->entry[c] = t->entry[c + 1];
      t->branch[c] = t->branch[c + 1];
   }
}

/* ----------------------------- COMBINE ------------------------------ */
void Combine (Treenode *current, int pos) {
   int c;
   Treenode *right;
   Treenode *left;
   right = current->branch[pos];
   left = current->branch[pos - 1];
   left->count++;
   left->entry[left->count] = current->entry[pos];
   left->branch[left->count] = right->branch[0];
   for (c = 1; c <= right->count; c++) {
      left->count++;
      left->entry[left->count] = right->entry[c];
      left->branch[left->count] = right->branch[c];
   }
   for (c = pos; c < current->count; c++) {
      current->entry[c] = current->entry[c + 1];
      current->branch[c] = current->branch[c + 1];
   }
   current->count--;
   free (right);
}


