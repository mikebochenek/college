/**
 * CIS*4430 Information Organization and Retrieval
 * Assignment #1, due: Tuesday, Febraury 27, 2001
 * Michael Bochenek [ID: 0041056]
 * email: mboche01@uoguelph.ca
 */

#include "a1.h"

extern void * root;
extern int counter;

void print_node_recursively_word (void * n, int indent_int)
{
#ifdef DEBUG
	int i;
	char * indent;

	indent = (char *) malloc (2048);	
	if (indent == NULL)
	{
		fatal_error ("Couldn't malloc memory");
	}

	for (i = 0; i < indent_int; i++)
	{
		indent[i] = ' ';
	}
	indent[indent_int] = '\0';

	indent_int++;

	if (((interior_node *) n)->is_leaf == FALSE)
	{
		interior_node *tmp = (interior_node *) n;
		for (i = 0; i < N_PARAMETER; i++)
		{
			if (tmp->memory_pointer[i] != NULL)
			{
				print_node_recursively_word (tmp->memory_pointer[i], indent_int);
			}
		}
		
		if (tmp->memory_pointer[N_PARAMETER] != NULL)
		{
			print_node_recursively_word (tmp->memory_pointer[i], indent_int);
		}
	}
	else if (((leaf_node *) n)->is_leaf == TRUE )
	{
		leaf_node *tmp = (leaf_node *) n;
		for (i = 0; i < N_PARAMETER && tmp->data[i].key[0] != '\0'; i++)
		{
			printf ("%-25s\n", tmp->data[i].key);
		}
	}
	else
	{
		fatal_error ("In print_node_resursively_word(): node is neither interior or leaf node, why?");
	}

	free (indent);
#endif
}


void print_node_recursively_short (void * n, int indent_int)
{
#ifdef DEBUG
	int i;
	char * indent;

	indent = (char *) malloc (2048);	
	if (indent == NULL)
	{
		fatal_error ("Couldn't malloc memory");
	}

	for (i = 0; i < indent_int; i++)
	{
		indent[i] = ' ';
	}
	indent[indent_int] = '\0';

	indent_int++;

	if (((interior_node *) n)->is_leaf == FALSE)
	{
		interior_node *tmp = (interior_node *) n;
		for (i = 0; i < N_PARAMETER; i++)
		{
			if (tmp->memory_pointer[i] != NULL)
			{
				print_node_recursively_short (tmp->memory_pointer[i], indent_int);
			}
			printf ("_%s %35s KEY[%d]=%s\n", indent, indent, i, tmp->key[i]);
		}
		
		if (tmp->memory_pointer[N_PARAMETER] != NULL)
		{
			print_node_recursively_short (tmp->memory_pointer[i], indent_int);
		}
	}
	else if (((leaf_node *) n)->is_leaf == TRUE )
	{
		leaf_node *tmp = (leaf_node *) n;
		for (i = 0; i < N_PARAMETER; i++)
		{
			printf ("_%s data.pointer[%d]=%10s", indent, i, tmp->data[i].pointer);
			printf (" data.key[%d]=%-25s\n", i, tmp->data[i].key);
		}
	}
	else
	{
		fatal_error ("In print_node_resursively_short(): node is neither interior or leaf node, why?");
	}

	free (indent);
#endif
}


void print_node_recursively (void * n, int indent_int)
{
/* #ifdef DEBUG */
	int i;
	char * indent;

	indent = (char *) malloc (2048);	
	if (indent == NULL)
	{
		fatal_error ("Couldn't malloc memory");
	}

	for (i = 0; i < indent_int; i++)
	{
		indent[i] = ' ';
	}
	indent[indent_int] = '\0';

	indent_int++;

	printf ("_%s ptr=%p\n", indent, n);

	if (((interior_node *) n)->is_leaf == FALSE)
	{
		interior_node *tmp = (interior_node *) n;
		printf ("_%s node is an INTERIOR NODE\n", indent);
		for (i = 0; i < N_PARAMETER; i++)
		{
			if (tmp->memory_pointer[i] != NULL)
			{
				print_node_recursively (tmp->memory_pointer[i], indent_int);
			}
			printf ("_%s memory_pointer[%d]=%p", indent, i, tmp->memory_pointer[i]);
			printf (" block_number[%d]=%9d", i, tmp->block_number[i]);
			printf (" KEY[%d]=%s\n", i, tmp->key[i]);
		}
		
		if (tmp->memory_pointer[N_PARAMETER] != NULL)
		{
			print_node_recursively (tmp->memory_pointer[i], indent_int);
		}
		printf ("_%s memory_pointer[%d]=%p", indent, i, tmp->memory_pointer[N_PARAMETER]);
		printf (" block_number[%d]=%9d\n", i, tmp->block_number[N_PARAMETER]);
	}
	else if (((leaf_node *) n)->is_leaf == TRUE )
	{
		leaf_node *tmp = (leaf_node *) n;
		printf ("_%s node is an LEAF NODE\n", indent);
		printf ("_%s next_pointer=%p\n", indent, tmp->next_pointer);
		printf ("_%s prev_pointer=%p\n", indent, tmp->prev_pointer);
		printf ("_%s next_block_number=%d\n", indent, tmp->next_block_number);
		printf ("_%s prev_block_number=%d\n", indent, tmp->prev_block_number);
		for (i = 0; i < N_PARAMETER; i++)
		{
			printf ("_%s data.pointer[%d]=%10s", indent, i, tmp->data[i].pointer);
			printf (" data.key[%d]=%-15s", i, tmp->data[i].key);
			printf (" key[%d]=%s\n", i, tmp->key[i]);
		}
	}
	else
	{
		fatal_error ("In print_node_resursively(): node is neither interior or leaf node, why?");
	}

	free (indent);
/*
#endif
*/
}


void print_node (void * n)
{
#ifdef DEBUG
	int i;	

	printf ("@@@ start print_node\n");
	printf ("@ ptr=%p\n", n);

	if (((interior_node *) n)->is_leaf == FALSE)
	{
		interior_node *tmp = (interior_node *) n;
		printf ("@ node is an INTERIOR NODE\n");
		for (i = 0; i < N_PARAMETER; i++)
		{
			printf ("@ memory_pointer[%d]=%p", i, tmp->memory_pointer[i]);
			printf (" block_number[%d]=%9d", i, tmp->block_number[i]);
			printf (" key[%d]=%s\n", i, tmp->key[i]);
		}
		printf ("@ memory_pointer[%d]=%p", i, tmp->memory_pointer[N_PARAMETER]);
		printf (" block_number[%d]=%9d\n", i, tmp->block_number[N_PARAMETER]);
	}
	else if (((leaf_node *) n)->is_leaf == TRUE )
	{
		leaf_node *tmp = (leaf_node *) n;
		printf ("@ node is an LEAF NODE\n");
		printf ("@ next_pointer=%p\n", tmp->next_pointer);
		printf ("@ prev_pointer=%p\n", tmp->prev_pointer);
		printf ("@ next_block_number=%d\n", tmp->next_block_number);
		printf ("@ prev_block_number=%d\n", tmp->prev_block_number);
		for (i = 0; i < N_PARAMETER; i++)
		{
			printf ("@ data.pointer[%d]=%10s", i, tmp->data[i].pointer);
			printf (" data.key[%d]=%-15s", i, tmp->data[i].key);
			printf (" key[%d]=%s\n", i, tmp->key[i]);
		}
	}
	else
	{
		fatal_error ("In print_node(): node is neither interior or leaf node, why?");
	}

	printf ("@@@ end print_node\n");
#endif
}


interior_node * create_new_interior_node ()
{
	int i;
	interior_node * new_node = (interior_node *) malloc (sizeof (interior_node));
	if (new_node == NULL)
	{
		fatal_error ("Could not allocate memory for new interior node");
	}

	for (i = 0; i < N_PARAMETER; i++)
	{
		new_node->memory_pointer[i] = NULL;
		new_node->block_number[i] = 0;
		new_node->key[i][0] = '\0';
	}
	new_node->memory_pointer[i+1] = NULL;
	new_node->block_number[i+1] = 0;

	new_node->is_leaf = FALSE;

	new_node->is_root = FALSE;

	new_node->this_block_number = 0;

	return new_node;
}

	
leaf_node * create_new_leaf_node ()
{
	int i;
	leaf_node * new_node = (leaf_node *) malloc (sizeof (leaf_node));
	if (new_node == NULL)
	{
		fatal_error ("Could not allocate memory for new leaf node");
	}

	new_node->next_pointer = NULL;
	new_node->prev_pointer = NULL;
	new_node->next_block_number = 0;
	new_node->prev_block_number = 0;
	new_node->this_block_number = 0;

	for (i = 0; i < N_PARAMETER; i++)
	{
		new_node->key[i][0] = '\0';
		new_node->data[i].pointer[0] = '\0';
		new_node->data[i].key[0] = '\0';
	} 

	new_node->is_leaf = TRUE;

	return new_node;
}


void fatal_error (char *error_string)
{
	printf ("%s\n", error_string);
	exit (-1);
}


node * read_KPtxt (int * list_counter_in)
{
	FILE *file_ptr = NULL;
	char buffer [1024];
	int list_counter = 0, i, j;
	node *list = NULL;

	/* open the file for reading, report error if unable to open file */
	file_ptr = fopen ("KP.txt", "r");
	if (file_ptr == NULL)
	{
		fatal_error ("Error opening file, exiting...");
	}

	/* keep looping until EOF, for each line parse it up into the proper
	   structure, and store the structure in an array. */
	do
	{
		fgets (buffer, 1024, file_ptr);

		if (feof(file_ptr)) continue;

		/* reallocate memory for the list, kinda inefficient but hey.. */
		list = realloc (list, ++list_counter * sizeof (node));
		if (list == NULL)
		{
			fatal_error ("Could not allocate memory, exiting...");
		}

		/* copy the key to the newly allocated structure */
		for (i = 0; i < KEY_SIZE && buffer[i] != ' '; i++)
		{
			list[list_counter - 1].key[i] = buffer[i];
		}
		list[list_counter - 1].key[i] = '\0';

		/* this error occurs if the above loop terminates 'prematurely' */
		if (buffer[i] != ' ')
		{
			fatal_error ("Lenght of key in the file KP.txt is too long");
		}

		j = i + 1;

		/* copy the pointer to the newly allocated structure */
		for (i = 0; i < POINTER_SIZE && buffer[i+j] != '\0' && buffer[i+j] != '\n'; i++)
		{
			list[list_counter - 1].pointer[i] = buffer[i+j];
		}
		list[list_counter - 1].pointer[i] = '\0';

	} while (!feof(file_ptr));

	*list_counter_in = list_counter;
	return list;
}


void * run()
{
	node *list = NULL;
	int list_counter = 0;
	int i;
	int start_number = 0;
	void * temp = NULL;

	root = (void *) create_new_leaf_node();
	((leaf_node *) root)->is_root = TRUE;

	list = read_KPtxt(&list_counter);

	for (i = 0; i < list_counter; i++)
	{
		insert (root, &list[i], &temp);
	}

	assign_block_number (root, &start_number);

	write_all (root, start_number);

	return root; 
}

