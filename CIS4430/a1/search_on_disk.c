/**
 * CIS*4430 Information Organization and Retrieval
 * Assignment #1, due: Tuesday, Febraury 27, 2001
 * Michael Bochenek [ID: 0041056]
 * email: mboche01@uoguelph.ca
 */

#include "a1.h"


node * search_on_disk (void * node_ptr, char * value)
{
	node * search_result = NULL;

	/* if *node_ptr is leaf, return nodepointer */
	if ( ((leaf_node *) node_ptr)->is_leaf == TRUE)
	{
		leaf_node * current = (leaf_node *) node_ptr;
		int i;
		
		printf ("Search: Reading block %d\n", current->this_block_number); 
		for (i = 0; i < N_PARAMETER && current->key[i][0] != '\0'; i++)
		{
			if (strcmp (value, current->key[i]) == 0)
			{
				search_result = (node *) malloc (sizeof (node));
				memcpy (search_result, & (current->data[i]), sizeof (node));
			}
		}
	}

	/* else */
	/* 	if K < K[i] then return tree search (P[0], value) */
	/* 	else */
	/* 		if K >= K[N_PARAMETER] then return search (P[N_PARAMETER, value) */
	/* 		else */
	/* 			find i such taht K[i] <= K < K[i+1] */
	/* 			return tree search (P[i], K) */

	else if ( ((interior_node *) node_ptr)->is_leaf == FALSE)
	{
		interior_node * current = (interior_node *) node_ptr;
		int i;

		printf ("Search: Reading block %d\n", current->this_block_number); 
		if (strcmp (value, current->key[0]) < 0)
		{
			return search_on_disk ( current->memory_pointer[0], value);
		}

		for (i = 0; i < N_PARAMETER && current->key[i][0] != '\0'; i++)
		{
			int compare = strcmp (value, current->key[i]);

			
			if (compare < 0)
			/* if (compare <= 0) */
			{
				return search_on_disk ( current->memory_pointer[i], value);
			}
		}

			/* execution should never get here unless ... */
			return search_on_disk ( current->memory_pointer[i], value);
	}

	else
	{
		fatal_error ("in search(): node neither interior or leaf\n");
	}

	return search_result;
}



node * search_range_on_disk (void * node_ptr, int * size, char * value, char *upper_value)
{
	node * search_result = NULL;

	/* if *node_ptr is leaf, return nodepointer */
	if ( ((leaf_node *) node_ptr)->is_leaf == TRUE)
	{
		leaf_node * current = (leaf_node *) node_ptr;
		int i;
		
		printf ("Search: Reading block %d\n", current->this_block_number); 

		for (i = 0; i < N_PARAMETER && current->key[i][0] != '\0'; i++)
		{
			if (strcmp (value, current->key[i]) == 0)
			{
				boolean keep_looping = TRUE;
				(* size) ++;
				search_result = (node *) realloc (search_result, sizeof (node) * (* size));
				memcpy ( & (search_result[ (* size) - 1]), & (current->data[i]), sizeof (node));

				while (keep_looping == TRUE)
				{
					i++;

					if (i == N_PARAMETER || current->key[i][0] == '\0')
					{
						/* try sibling */
						if (current->next_pointer != NULL)
						{
							current = current->next_pointer;
							i = 0;
						}
						else
						{
							keep_looping = FALSE;
						}
					}
					else
					{
						/* exhaust this puppy first */
						if (strcmp (upper_value, current->key[i]) >= 0)
						{
							(* size) ++;
							search_result = (node *) realloc (search_result, sizeof (node) * (* size));
							memcpy ( & (search_result[ (* size) - 1]), & (current->data[i]), sizeof (node));
							
						}
						else
						{
							keep_looping = FALSE;
						}
					}
				}

				break;
			}
		}
	}

	/* else */
	/* 	if K < K[i] then return tree search (P[0], value) */
	/* 	else */
	/* 		if K >= K[N_PARAMETER] then return search (P[N_PARAMETER, value) */
	/* 		else */
	/* 			find i such taht K[i] <= K < K[i+1] */
	/* 			return tree search (P[i], K) */

	else if ( ((interior_node *) node_ptr)->is_leaf == FALSE)
	{
		interior_node * current = (interior_node *) node_ptr;
		int i;

		printf ("Search: Reading block %d\n", current->this_block_number); 
		if (strcmp (value, current->key[0]) < 0)
		{
			return search_range_on_disk( current->memory_pointer[0], size, value, upper_value);
		}

		for (i = 0; i < N_PARAMETER && current->key[i][0] != '\0'; i++)
		{
			int compare = strcmp (value, current->key[i]);

			
			if (compare < 0)
			/* if (compare <= 0) */
			{
				return search_range_on_disk ( current->memory_pointer[i], size, value, upper_value);
			}
		}

		/* execution should never get here unless ... */
		return search_range_on_disk ( current->memory_pointer[i], size, value, upper_value);
	}

	else
	{
		fatal_error ("in search(): node neither interior or leaf\n");
	}

	return search_result;
}
