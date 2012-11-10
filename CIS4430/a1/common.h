/**
 * CIS*4430 Information Organization and Retrieval
 * Assignment #1, due: Tuesday, Febraury 27, 2001
 * Michael Bochenek [ID: 0041056]
 * email: mboche01@uoguelph.ca
 */

#ifndef _COMMON_H_
#define _COMMON_H_

#define BLOCKSIZE 1024
#define N_PARAMETER 10
#define POINTER_SIZE 8
#define KEY_SIZE 42

#define EQ(A, B) (!strcmp((A), (B)))
#define GT(A, B) (strcmp((A), (B)) > 0)
#define LT(A, B) (strcmp((A), (B)) < 0)

typedef enum boolean_enum {FALSE, TRUE} boolean;

typedef struct node_struct
{
 	char pointer [POINTER_SIZE];
	char key [KEY_SIZE];
} node;

typedef struct interior_node_struct
{
	boolean is_leaf;
	boolean is_root;
	void * memory_pointer [N_PARAMETER + 1];
	int block_number [N_PARAMETER + 1];
	char key [N_PARAMETER] [KEY_SIZE];
	int this_block_number;
} interior_node;

typedef struct leaf_node_struct
{
	boolean is_leaf;
	boolean is_root;
	struct leaf_node_struct * next_pointer;
	struct leaf_node_struct * prev_pointer;
	int next_block_number;
	int prev_block_number;
	int this_block_number;
	char key [N_PARAMETER] [KEY_SIZE];
	node data [N_PARAMETER];
} leaf_node;

#endif

