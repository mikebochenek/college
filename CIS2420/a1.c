/*  Michael Bochenek
    ID: 0041056 (980492820)
    mboche01@uoguelph.ca
    CIS 242
    Assignment #1
    May 26, 1999

    This program reads 10 integers from data.txt and displays the integers
    so that those in odd positions keep their original order, but those
    in even positions are displayed in an inverse order.
*/

#include <stdio.h>

#define INTEGERS 10
#define MAXSTACK 5
#define MAXQUEUE 5
/* maximums for number of items in stack and in queue */

typedef int stack_entry; /* ------------ struct definitions for a STACK */
typedef struct stack {
   int top;
   stack_entry entry[MAXSTACK];
} Stack;

typedef int queue_entry; /* ------------ struct definitions for a QUEUE */
typedef struct queue {
   int front;
   int rear;
   queue_entry entry[MAXQUEUE];
} Queue;

void push (stack_entry item, Stack *s); /* ---- stack function prototypes */
void pop (stack_entry *item, Stack *s); /* described in detail below */
int stack_empty (Stack *s);
int stack_full (Stack *s);
void create_stack (Stack *s);

void append (queue_entry x, Queue *q); /* ----- queue function prototypes */
void serve (queue_entry *x, Queue *q); /* described in detail below */
int queue_empty (Queue *q);
int queue_full (Queue *q);
void create_queue (Queue *q);

/* ------------------------------- MAIN ------------------------------ */
int main () {
   int i, j;
   int number[INTEGERS];
   FILE *filename = NULL;

   Stack s; /* main stack structure */
   Queue q; /* main queue structure */

   create_stack (&s); /* init stack administrative values */
   create_queue (&q); /* init queue administrative values */

   filename = fopen ("data.txt", "r");
   if (filename == NULL) {
      printf("Error opening file\n");
      return 0;
   } /* open file data.txt and check for 'fopen' errors */

   for (i = 0; !feof(filename) || i < INTEGERS; i++) {
      fscanf (filename, "%d", &number[i]);
   } /* read at most 10 integers and store them in the array.  Keep
	reading until EOF or until 10 integers have been read. */

   for (i = 0; i < INTEGERS; i = i + 2) {
      /* append to queue the ODD numbers, 1st ,3rd ,5th ,7th, 9th, etc */
      append (number[i], &q);
      /* push onto stack the EVEN numbers, 2nd, 4th, 6th, 8th, 10th, etc */
      push (number[i + 1], &s);
   }

   for (i = 0; i < INTEGERS; i = i + 2) {
      /* remove items fron queue and print them out (ODD) */
      serve (&j, &q);
      printf ("%d ", j);
      /* pop items from the stack and print them out (EVEN) */
      pop (&j, &s);
      printf ("%d ", j);
   }
   printf ("\n");

   return 0; /* bye-bye */
}
/* ---------------------------- STACK FUNCTIONS ------------------------ */

/* Passed a integer and a pointer to a stack.  Function adds the item
   to the 'top' of the stack.  Then the value of 'top' is incremented.
   If 'stack_full' returns non-zero then print error message.  */
void push (stack_entry item, Stack *s) {
   if (stack_full (s)) {
      printf ("Stack is full\n");
   } else {
      s->entry[s->top] = item; /* add item to top of stack */
      s->top = s->top + 1; /* increment 'top' */
   }
}

/* passed a pointer to an integer, and a pointer to a stack.  Function
   removes the 'top' item from the stack and stores it in the integer.
   Then the value of 'top' is decrement.  If 'stack_empty returns
   non-zero then print error message */
void pop (stack_entry *item, Stack *s) {
   if (stack_empty (s)) {
      printf ("Stack is empty\n");
   } else {
      *item = s->entry[ (s->top-1) ]; /* remove item from top of stack */
      s->top = s->top - 1; /* decrement 'top' */
   }
}

/* passed a pointer to a stack, checks if stack is empty or not */
int stack_empty (Stack *s) {
   return s->top <= 0;
}

/* passed a pointer to a stack, checks if stack if full or not */
int stack_full (Stack *s) {
   return s->top >= MAXSTACK;
}

/* passed a pointer to a stack, inits 'top' to zero */
void create_stack (Stack *s) {
   s->top = 0;
}
/* -------------------------- QUEUE FUNCTIONS ------------------------- */

/* passed an integer and a pointer to a queue.  Function adds integer
   to the queue, and then increments 'rear'.  If queue_full returns
   a non-zero value, then print error */
void append (queue_entry x, Queue *q) {
   if (queue_full (q)) {
      printf ("Queue is full\n");
   } else {
      q->entry[q->rear] = x; /* add integer to rear of queue */
      q->rear = q->rear + 1; /* increment 'rear' */
   }
}

/* passed a pointer to an integer and a pointer to a queue.  Function
   removes the first item in the queue, and stores it in the integer.
   Then 'front' is incremented.  If queue_empty returns a non-zero value
   then print out error message */
void serve (queue_entry *x, Queue *q) {
   if (queue_empty (q)) {
      printf ("Queue is empty %d %d\n", q->front, q->rear);
   } else {
      *x = q->entry[q->front]; /* remove integer from front of queue */
      q->front = q->front + 1; /* increment 'front' */
   }
}

/* passed a pointer to a queue, check if it's empty or not */
int queue_empty (Queue *q) {
   return !(q->rear - q->front);
}

/* passed a pointer to a queue, check if it's full or not */
int queue_full (Queue *q) {
   return (q->rear - q->front) >= MAXQUEUE;
}

/* passed a pointer to a queue, inits 'front' and 'rear' to 0 */
void create_queue (Queue *q) {
   q->front = 0;
   q->rear = 0;
}
