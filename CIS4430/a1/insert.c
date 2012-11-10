/**
 * CIS*4430 Information Organization and Retrieval
 * Assignment #1, due: Tuesday, Febraury 27, 2001
 * Michael Bochenek [ID: 0041056]
 * email: mboche01@uoguelph.ca
 */

#include "a1.h"

extern void * root;

/* inserts 'entry' into the subtree with root '*nodepointer'
   'newchildentry is NULL at first, and NULL upon return, 
   unless the child is split */
void insert (void * nodepointer, void * entry, void ** newchildentry)
{
	/* if '*nodepointer' is a non-leaf node, say N */
	if (((interior_node *) nodepointer)->is_leaf == FALSE)
	{
		int i, insert_index;
		interior_node * current_node = (interior_node *) nodepointer;
		node * new_entry = (node *) entry;

		/* choose subtree: find i such that K[i] <= entry's key value < K[i+1] */
		/* CHOOSE SUBTREE */

		/* finding insertion point */
		for (i = 0; i < N_PARAMETER && current_node->key[i][0] != '\0'; i++)
		{
			if (GT (current_node->key[i], new_entry->key))
			{
				break;
			}
		}
		insert_index = i;

#ifdef DEBUG
		printf ("INTERIOR insert_index=%d\n", insert_index);
#endif


		/* insert (Pi, entry, newchildentry) RECURSIVELY INSERT ENTRY */
		insert (current_node->memory_pointer[insert_index], entry, newchildentry);

#ifdef DEBUG
		print_node (current_node->memory_pointer[insert_index]);
#endif


		/* there is no way that the 11th pointer will ever get called.. */


		/* if null, then return - USUAL CASE; DIDN'T SPLIT CHILD */
		if ( (*newchildentry) == NULL)
		{
#ifdef DEBUG
			printf ("  returning cuz newchildentry is NULL\n");
#endif
			return;
		}

		/* else.. - WE SPLIT CHILD, MUST INSERT *newchildentry in N */
		else
		{
			int i;
			boolean has_space = FALSE;
			interior_node * new_entry = (interior_node *) *newchildentry; 

			for (i = 0; i < N_PARAMETER; i++)
			{
				if (current_node->key[i][0] == '\0')
				{
					has_space = TRUE;
				}
			}

			/* if N has space - USUAL CASE */
			if (has_space == TRUE)
			{
				int insert_index = 0;
				int last_taken_ptr = 0;
			
				/* put *newchildentry on it, set newchildentry to NULL, return */

				/* finding insertion point */
				for (i = 0; i < N_PARAMETER && current_node->key[i][0] != '\0'; i++)
				{
#ifdef DEBUG
					printf ("finding insertion point for '%s'\n", new_entry->key[0]);
					printf ("___ current_node=%s entry=%s\n", current_node->key[i], new_entry->key[0]);
#endif

					if (GT (current_node->key[i], new_entry->key[0]))
					{
						break;
					}

				}
				insert_index = i;
				
#ifdef DEBUG
				printf ("INTERIOR #2 insert_index=%d\n", insert_index);
#endif

				/* shift all entries up one spot */
				last_taken_ptr = insert_index;
				while (current_node->key[last_taken_ptr][0] != '\0')
				{
					last_taken_ptr++;
				}

#ifdef DEBUG
				printf ("last_taken_ptr=%d\n", last_taken_ptr);

				printf ("BEFORE_B\n");
				printf ("insert_index=%d last_taken_ptr=%d\n", insert_index, last_taken_ptr);
				printf ("inserting=%s ptr=%p\n", new_entry->key[0], new_entry->memory_pointer[0]);
				print_node (current_node);
#endif

				/* there is an additional entry at the end.
				   (N_PARAMETER + 1) */
				memcpy (& (current_node->memory_pointer[last_taken_ptr+1]), & (current_node->memory_pointer[last_taken_ptr]), sizeof (void *));
				memcpy (& (current_node->block_number[last_taken_ptr+1]), & (current_node->block_number[last_taken_ptr]), sizeof (int));

				for (i = last_taken_ptr; i > insert_index; i--)
				{
					memcpy (current_node->key[i], current_node->key[i-1], KEY_SIZE);
					memcpy (& (current_node->memory_pointer[i]), & (current_node->memory_pointer[i-1]), sizeof (void *));
					memcpy (& (current_node->block_number[i]), & (current_node->block_number[i-1]), sizeof (int));
				}

				/* insert the new entry */
				memcpy (current_node->key[insert_index], new_entry->key[0], KEY_SIZE);
				memcpy (& (current_node->memory_pointer[insert_index+1]), & (new_entry->memory_pointer[0]), sizeof (void *)); 
				memcpy (& (current_node->block_number[insert_index+1]), & (new_entry->block_number[0]), sizeof (int));


				/* ADDED +1 to insert_index */


#ifdef DEBUG
				printf ("AFTER_B\n");
				print_node (current_node);
#endif

				/* set newchildentry (recursion) to NULL and return */
				*newchildentry = NULL;
				return;
			}

			/* if N does not have space */
			else if (has_space == FALSE)
			{
				void * oversized_node_memory_pointer [N_PARAMETER + 2];
				int oversized_node_block_number [N_PARAMETER + 2];
				char oversized_node_key [N_PARAMETER + 1] [KEY_SIZE];
				interior_node * new_node_N = create_new_interior_node();
				void * temp2;
				int temp_int;
				int midpoint;
				int i;

				/* split N, 2d + 1 key values and 2d + 2 nodepointers */

				/* first d key values and d + 1 nodepointers stay */

				/* last d keys and d + 1 pointers move to new node, N2 */

#ifdef DEBUG
				printf ("<<<<<<<<<<< BEFORE >>>>>>>>>>>>>>>>>>>>>> \n");
				print_node (current_node);
#endif

				for (i = 0; i < N_PARAMETER; i++)
				{
					/* changed order of 3-memcpies and if-statment */
					if (GT (current_node->key[i], new_entry->key[0]))
					{
						temp_int = i;
						break;
					}

					memcpy (& (oversized_node_memory_pointer[i]), & (current_node->memory_pointer[i]), sizeof (void *));
					memcpy (& (oversized_node_block_number[i]), & (current_node->block_number[i]), sizeof (int));
					memcpy (& (oversized_node_key[i]), & (current_node->key[i]), KEY_SIZE);
				}

				memcpy (& (oversized_node_memory_pointer[i]), & (new_entry->memory_pointer[0]), sizeof (void *)); 
				memcpy (& (oversized_node_block_number[i]), & (new_entry->block_number[0]), sizeof (int));
				memcpy (& (oversized_node_key[i]), & (new_entry->key[0]), KEY_SIZE);

				for (i = i; i < N_PARAMETER; i++)
				{
					memcpy (& (oversized_node_memory_pointer[i+1]), & (current_node->memory_pointer[i]), sizeof (void *));
					memcpy (& (oversized_node_block_number[i+1]), & (current_node->block_number[i]), sizeof (int));
					memcpy (& (oversized_node_key[i+1]), & (current_node->key[i]), KEY_SIZE);
				}

				memcpy (& (oversized_node_memory_pointer[N_PARAMETER + 1]), & (current_node->memory_pointer[N_PARAMETER]), sizeof (void *));
				memcpy (& (oversized_node_block_number[N_PARAMETER + 1]), & (current_node->block_number[N_PARAMETER]), sizeof (int));


				/* this is the worst patch ever... */
				if (temp_int < N_PARAMETER + 2)
{
				temp2 = oversized_node_memory_pointer[temp_int];
				oversized_node_memory_pointer[temp_int] = oversized_node_memory_pointer[temp_int + 1];
				oversized_node_memory_pointer[temp_int + 1] = temp2;
}


				midpoint = N_PARAMETER / 2;

				for (i = 0; i < midpoint; i++)
				{
					memcpy (& (current_node->memory_pointer[i]), & (oversized_node_memory_pointer[i]), sizeof (void *));
					memcpy (& (current_node->block_number[i]), & (oversized_node_block_number[i]), sizeof (int));
					memcpy (& (current_node->key[i]), & (oversized_node_key[i]), KEY_SIZE);
				}
				memcpy (& (current_node->memory_pointer[i]), & (oversized_node_memory_pointer[i]), sizeof (void *));
				memcpy (& (current_node->block_number[i]), & (oversized_node_block_number[i]), sizeof (int));


				for (i = midpoint; i < N_PARAMETER; i++)
				{
					current_node->memory_pointer[i + 1] = NULL;
					current_node->block_number[i + 1] = 0;
					current_node->key[i][0] = '\0';
				} 

				/** 
				 * not needed ...
				 *
				current_node->memory_pointer[N_PARAMETER] = NULL;
				current_node->block_number[N_PARAMETER] = 0;
				*/


				for (i = midpoint + 1; i < (N_PARAMETER + 1); i++)
				{
					memcpy (& (new_node_N->memory_pointer[i-midpoint-1]), & (oversized_node_memory_pointer[i]), sizeof (void *));
					memcpy (& (new_node_N->block_number[i-midpoint-1]), & (oversized_node_block_number[i]), sizeof (int));
					memcpy (& (new_node_N->key[i-midpoint-1]), & (oversized_node_key[i]), KEY_SIZE);
				}
				memcpy (& (new_node_N->memory_pointer[i-midpoint-1]), & (oversized_node_memory_pointer[i]), sizeof (void *));
				memcpy (& (new_node_N->block_number[i-midpoint-1]), & (oversized_node_block_number[i]), sizeof (int));


				/**
				 * not needed ...
				 *
				for (i = (N_PARAMETER + 1 - midpoint); i < N_PARAMETER; i++)
				{
					new_node_N->memory_pointer[i + 1] = NULL;
					new_node_N->block_number[i + 1] = 0;
					new_node_N->key[i][0] = '\0';
				} 

				new_node_N->memory_pointer[N_PARAMETER] = NULL;
				new_node_N->block_number[N_PARAMETER] = 0;
				*/


				/* NOTE: *newchildentry set to guide searches between N and N2 */
				/* newchildentry = & ((smallest key value on N2, pointer to N2)) */
				*newchildentry = (void *) create_new_interior_node();
				((interior_node *) (*newchildentry) )->memory_pointer[0] = (void *) new_node_N;

/*
 * WHAT IS THE RIGHT THING TO PUT IN THERE?!?!??!
 */


				memcpy ( ((interior_node *) (*newchildentry) )->key[0], new_node_N->key[0], KEY_SIZE);
/*
				memcpy ( ((interior_node *) (*newchildentry) )->key[0], ((leaf_node *) new_node_N->memory_pointer[0])->key[0], KEY_SIZE);
				memcpy ( ((interior_node *) (*newchildentry) )->key[0], & (oversized_node_key[midpoint]), KEY_SIZE);
*/




#ifdef DEBUG
				for (i = 0; i < (N_PARAMETER + 1); i++)
				{
					printf ("*** %d %s \n", i, oversized_node_key[i]);
				}
#endif

#ifdef DEBUG
				printf ("<<<<<<<<<<<<<<<<<< AFTER >>>>>>>>>>>>>>>>>>>>>>\n");
				print_node (current_node);
				print_node (new_node_N);
#endif


				/*************************
				 * KEY: "if node is root!"
				 *************************/

				/* if N is the root, -> root node was just split */
				if (current_node->is_root == TRUE)
				{
					/* create new node with (pointer to N, *newchildentry) */
					/* make the tree's root-node pointer point to the new node */
					interior_node * right = new_node_N;
					interior_node * left = create_new_interior_node();
					interior_node * temp = create_new_interior_node();

					memcpy (left, current_node, sizeof (interior_node));
					memcpy (current_node, temp, sizeof (interior_node));

					current_node->is_root = TRUE;

					current_node->memory_pointer[0] = left;
					current_node->block_number[0] = left->block_number[0];

					memcpy (& (current_node->key[0]), & (oversized_node_key[midpoint]), KEY_SIZE);
					current_node->memory_pointer[1] = right;
					current_node->block_number[1] = right->block_number[0];

					right->is_root = FALSE;
					left->is_root = FALSE;

					*newchildentry = NULL;
					
					free (temp);
				}

				return;
			}
			else
			{
				fatal_error ("Boolean value neither TRUE nor FALSE in interior node");
			}

		}

	}

	/* if '*nodepointer' is a leaf node, say L */
	else if (((leaf_node *) nodepointer)->is_leaf == TRUE )
	{
		int i;
		boolean has_space = FALSE;
		leaf_node * current_node = (leaf_node *) nodepointer;
		node * new_entry = (node *) entry;

		for (i = 0; i < N_PARAMETER; i++)
		{
			if (current_node->key[i][0] == '\0')
			{
				has_space = TRUE;
			}
		}

		/* if L has space */
		if (has_space == TRUE)
		{
			int insert_index = 0;
			int last_taken_ptr = 0;
			
			/* put entry on it, set newchildentry to NULL and return */

			/* finding insertion point */
			for (i = 0; i < N_PARAMETER && current_node->key[i][0] != '\0'; i++)
			{
#ifdef DEBUG
				printf ("finding insertion point for '%s'\n", new_entry->key);
				printf ("___ current_node=%s entry=%s\n", current_node->key[i], new_entry->key);
#endif

				if (GT (current_node->key[i], new_entry->key))
				{
					break;
				}

			}
			insert_index = i;

#ifdef DEBUG
			printf ("insert_index=%d\n", insert_index);
#endif

			/* shift all entries up one spot */
			last_taken_ptr = insert_index;
			while (current_node->key[last_taken_ptr][0] != '\0')
			{
				last_taken_ptr++;
			}

#ifdef DEBUG
			printf ("last_taken_ptr=%d\n", last_taken_ptr);
#endif

			for (i = last_taken_ptr; i > insert_index; i--)
			{
				memcpy (current_node->key[i], current_node->key[i-1], KEY_SIZE);
				memcpy (& (current_node->data[i]), & (current_node->data[i-1]), sizeof (node));
			}
 
			/* insert the new entry */
			memcpy (current_node->key[insert_index], new_entry->key, KEY_SIZE);
			memcpy (& (current_node->data[insert_index]), new_entry, sizeof (node));

			/* set newchildentry (recursion) to NULL and return */
			*newchildentry = NULL;
			return;


		}
		else if (has_space == FALSE)
		{
			/* split L, first d entries stay, rest move to new node L2 */
			leaf_node * new_node_L = create_new_leaf_node();
			node oversized_node [N_PARAMETER + 1];
			int midpoint;
			int i;

			for (i = 0; i < N_PARAMETER; i++)
			{
				memcpy (& (oversized_node[i]), & (current_node->data[i]), sizeof (node));
				if (GT (current_node->key[i], new_entry->key))
				{
					break;
				}
			}

			memcpy (& (oversized_node[i]), new_entry, sizeof (node));

			for (i = i; i < N_PARAMETER; i++)
			{
				memcpy (& (oversized_node[i+1]), & (current_node->data[i]), sizeof (node));
			}

#ifdef DEBUG
			for (i = 0; i < (N_PARAMETER + 1); i++)
			{
				printf ("i=%d key=%s\n", i, oversized_node[i].key);
			}
#endif

			/* calculate midpoint */
			midpoint = N_PARAMETER / 2 + 1;

			for (i = 0; i < midpoint; i++)
			{
				memcpy (current_node->key[i], oversized_node[i].key, KEY_SIZE);
				memcpy (current_node->data[i].key, oversized_node[i].key, KEY_SIZE);
				memcpy (current_node->data[i].pointer, oversized_node[i].pointer, POINTER_SIZE);
			}
			for (i = midpoint; i < N_PARAMETER; i++)
			{
				current_node->key[i][0] = '\0';
				current_node->data[i].pointer[0] = '\0';
				current_node->data[i].key[0] = '\0';
			} 

			for (i = midpoint; i < (N_PARAMETER + 1); i++)
			{
				memcpy (new_node_L->key[i-midpoint], oversized_node[i].key, KEY_SIZE);
				memcpy (new_node_L->data[i-midpoint].key, oversized_node[i].key, KEY_SIZE);
				memcpy (new_node_L->data[i-midpoint].pointer, oversized_node[i].pointer, POINTER_SIZE);
			}
			for (i = (N_PARAMETER + 1 - midpoint); i < N_PARAMETER; i++)
			{
				new_node_L->key[i][0] = '\0';
				new_node_L->data[i].pointer[0] = '\0';
				new_node_L->data[i].key[0] = '\0';
			} 

#ifdef DEBUG
			for (i = 0; i < N_PARAMETER; i++)
			{
				printf ("* key=%s data.pointer=%s data.key=%s [ L2 ]\n", new_node_L->key[i], new_node_L->data[i].pointer, new_node_L->data[i].key);
			}
			for (i = 0; i < N_PARAMETER; i++)
			{
				printf ("* key=%s data.pointer=%s data.key=%s [ L ]\n", current_node->key[i], current_node->data[i].pointer, current_node->data[i].key);
			}
#endif

			/* newchildentry = & ((smallest key value on L2, pointer to L2)) */
			*newchildentry = (void *) create_new_interior_node();
			((interior_node *) (*newchildentry) )->memory_pointer[0] = (void *) new_node_L;
			memcpy ( ((interior_node *) (*newchildentry) )->key[0], new_node_L->key[0], KEY_SIZE);

#ifdef DEBUG
			printf ("recursively returning %s and %p\n", ((interior_node *) (*newchildentry))->key[0], ((interior_node *) (*newchildentry))->memory_pointer[0]);
#endif


			/* set sibling pointers in L and L2 */
			new_node_L->next_pointer = current_node->next_pointer;
			new_node_L->prev_pointer = current_node;
			current_node->next_pointer = new_node_L;
			current_node->prev_pointer = current_node->prev_pointer;

			if (current_node->next_pointer != NULL)
			{
				current_node->next_pointer->prev_pointer = new_node_L;
			}


			/* THIS PART SHOULD BE OK IN THEORY */


			/* if N is the root, -> root node was just split */
			if (current_node->is_root == TRUE)
			{
				leaf_node * right = new_node_L;
				leaf_node * left = create_new_leaf_node();

				memcpy (left, current_node, sizeof (leaf_node));

				free (current_node);

				root = (void *) create_new_interior_node();
				((interior_node *) root)->is_root = TRUE;

				((interior_node *) root)->memory_pointer[0] = left;
				memcpy (& ( ((interior_node *) root)->key[0]), & (right->key[0]), KEY_SIZE);
				((interior_node *) root)->memory_pointer[1] = right;

				right->is_root = FALSE;
				left->is_root = FALSE;

				right->next_pointer = NULL;
				right->prev_pointer = left;
				left->next_pointer = right;
				left->prev_pointer = NULL;

				*newchildentry = NULL;
			}

			return;
		}
		else
		{
			fatal_error ("Boolean is neither TRUE or FALSE");
		}
	}

	else
	{
		fatal_error ("In insert(): node is neither interior or leaf node, why?");
	}

} 



