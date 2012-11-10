/**
 * CIS*4430 Information Organization and Retrieval
 * Assignment #1, due: Tuesday, Febraury 27, 2001
 * Michael Bochenek [ID: 0041056]
 * email: mboche01@uoguelph.ca
 */

#include "a1.h"
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

extern void * root;

void assign_block_number (void * n, int * block_number)
{
	if (((leaf_node *) n)->is_leaf == TRUE)
	{
		leaf_node * leaf = (leaf_node *) n;
		leaf->this_block_number = (* block_number);
		(* block_number) ++;

		if (leaf->prev_pointer != NULL)
		{
			leaf->prev_pointer->next_block_number = leaf->this_block_number;
			leaf->prev_block_number = leaf->prev_pointer->this_block_number;
		}
	}
	else if (((interior_node *) n)->is_leaf == FALSE)
	{
		int i;
		interior_node * current = (interior_node *) n;

		current->this_block_number = (* block_number);
		(* block_number) ++;

		for (i = 0; i < N_PARAMETER && current->key[i][0] != '\0'; i++)
		{
			current->block_number[i] = (* block_number);
			assign_block_number ( (void *) current->memory_pointer[i], block_number);
		}
		current->block_number[i] = (* block_number);
		assign_block_number ( (void *) current->memory_pointer[i], block_number);

	}
}



void write_block (void * n, int block_number, int f)
{
	if (((interior_node *) n)->is_leaf == FALSE)
	{
		int i;
		interior_node * current = (interior_node *) n;

		write_wrapper (n, f);

		for (i = 0; i < N_PARAMETER && current->key[i][0] != '\0'; i++)
		{
			write_block ( (void *) current->memory_pointer[i], 0, f);
		}
		write_block ( (void *) current->memory_pointer[i], 0, f);

	}
	else if (((leaf_node *) n)->is_leaf == TRUE)
	{
		write_wrapper (n, f);
	}
}


void write_wrapper (void * n, int f)
{
	char buffer [BLOCKSIZE];
	int i;

	if (((leaf_node *) n)->is_leaf == TRUE)
	{
		leaf_node * l = ((leaf_node *) n);

		l->next_pointer = NULL;
		l->prev_pointer = NULL;

		lseek (f, ((leaf_node *) n)->this_block_number * BLOCKSIZE, SEEK_SET);

		memcpy (buffer, & (l->is_leaf), sizeof (boolean));
		write (f, buffer, sizeof (boolean));

		memcpy (buffer, & (l->is_root), sizeof (boolean));
		write (f, buffer, sizeof (boolean));

		memcpy (buffer, & (l->next_pointer), sizeof (void *));
		write (f, buffer, sizeof (void *));

		memcpy (buffer, & (l->prev_pointer), sizeof (void *));
		write (f, buffer, sizeof (void *));

		memcpy (buffer, & (l->next_block_number), sizeof (int));
		write (f, buffer, sizeof (int));

		memcpy (buffer, & (l->prev_block_number), sizeof (int));
		write (f, buffer, sizeof (int));

		memcpy (buffer, & (l->this_block_number), sizeof (int));
		write (f, buffer, sizeof (int));

		for (i = 0; i < N_PARAMETER; i++)
		{
			memcpy (buffer, (l->key[i]), KEY_SIZE);
			write (f, buffer, KEY_SIZE);
		}

		for (i = 0; i < N_PARAMETER; i++)
		{
			memcpy (buffer, (l->data[i].key), KEY_SIZE);
			write (f, buffer, KEY_SIZE);
			memcpy (buffer, (l->data[i].pointer), POINTER_SIZE);
			write (f, buffer, POINTER_SIZE);

		}
	}
	else if (((interior_node *) n)->is_leaf == FALSE)
	{
		interior_node * l = ((interior_node *) n);

		lseek (f, ((interior_node *) n)->this_block_number * BLOCKSIZE, SEEK_SET);

		memcpy (buffer, & (l->is_leaf), sizeof (boolean));
		write (f, buffer, sizeof (boolean));
 
		memcpy (buffer, & (l->is_root), sizeof (boolean));
		write (f, buffer, sizeof (boolean));

/*
		for (i = 0; i < (N_PARAMETER + 1); i++)
		{
			memcpy (buffer, & (l->memory_pointer[i]), sizeof (void *));
			write (f, buffer, sizeof (void *));
		}
*/

		for (i = 0; i < (N_PARAMETER + 1); i++)
		{
			memcpy (buffer, & (l->block_number[i]), sizeof (int));
			write (f, buffer, sizeof (int));
		}

		for (i = 0; i < N_PARAMETER; i++)
		{
			memcpy (buffer, (l->key[i]), KEY_SIZE);
			write (f, buffer, KEY_SIZE);
		}

		memcpy (buffer, & (l->this_block_number), sizeof (int));
		write (f, buffer, sizeof (int));
	}
	else
	{
		fatal_error ("in write_wrapper");
	}


}


void write_all (void * root, int start_node)
{
	int f, i;
	char buffer [BLOCKSIZE];

	creat ("tree.dat", S_IRWXU);
	f = open ("tree.dat", O_RDWR);

	for (i = 0; i < start_node + 1; i++)
	{
		write (f, buffer, BLOCKSIZE);
	}

	write_block (root, 0, f);

	close (f);
}



void read_block (void * n, int f, int block)
{
	if ( ((interior_node *) n)->is_leaf == FALSE)
	{
		int i;
		interior_node * current = (interior_node *) n;

		read_wrapper (current, f, block);

		for (i = 0; i < N_PARAMETER && current->key[i][0] != '\0'; i++)
		{
			current->memory_pointer[i] = create_new_interior_node();
			read_wrapper ( current->memory_pointer[i], f, current->block_number[i]);
			read_block ( current->memory_pointer[i], f, current->block_number[i]);
		}
		current->memory_pointer[i] = create_new_interior_node();
		read_wrapper ( current->memory_pointer[i], f, current->block_number[i]);
		read_block ( current->memory_pointer[i], f, current->block_number[i]);
	}
	else if ( ((leaf_node *) n)->is_leaf == TRUE)
	{
		/* do not have to read nothing */
	}

	
}



void read_wrapper (void * n, int f, int block_number)
{
	char buffer [BLOCKSIZE];
	boolean is_leaf = FALSE;
	int i;


	lseek (f, block_number * BLOCKSIZE, SEEK_SET);

	read (f, buffer, sizeof (boolean)); 
	memcpy (& (is_leaf), buffer, sizeof (boolean));

	if (is_leaf == TRUE)
	{
		leaf_node * l = ((leaf_node *) n);

		memcpy ( & (l->is_leaf), buffer, sizeof (boolean));

		read (f, buffer, sizeof (boolean));
		memcpy ( & (l->is_root), buffer, sizeof (boolean));

		read (f, buffer, sizeof (void *));
		memcpy ( & (l->next_pointer), buffer, sizeof (void *));

		read (f, buffer, sizeof (void *));
		memcpy ( & (l->prev_pointer), buffer, sizeof (void *));

		read (f, buffer, sizeof (int));
		memcpy ( & (l->next_block_number), buffer, sizeof (int));

		read (f, buffer, sizeof (int));
		memcpy ( & (l->prev_block_number), buffer, sizeof (int));

		read (f, buffer, sizeof (int));
		memcpy ( & (l->this_block_number), buffer, sizeof (int));

		for (i = 0; i < N_PARAMETER; i++)
		{
			read (f, buffer, KEY_SIZE);
			memcpy (l->key[i], buffer, KEY_SIZE);
		}

		for (i = 0; i < N_PARAMETER; i++)
		{
			read (f, buffer, KEY_SIZE);
			memcpy (l->data[i].key, buffer, KEY_SIZE);
			read (f, buffer, POINTER_SIZE);
			memcpy (l->data[i].pointer, buffer, POINTER_SIZE);
		}
	}
	else if (is_leaf == FALSE)
	{
		interior_node * l = ((interior_node *) n);

		memcpy ( & (l->is_leaf), buffer, sizeof (boolean));

		read (f, buffer, sizeof (boolean));
		memcpy ( & (l->is_root), buffer, sizeof (boolean));

/*
		for (i = 0; i < (N_PARAMETER + 1); i++)
		{
			read (f, buffer, sizeof (void *));
			memcpy ( & (l->memory_pointer[i]), buffer, sizeof (void *));
		}
*/

		for (i = 0; i < (N_PARAMETER + 1); i++)
		{
			read (f, buffer, sizeof (int));
			memcpy ( & (l->block_number[i]), buffer, sizeof (int));
		}

		for (i = 0; i < N_PARAMETER; i++)
		{
			read (f, buffer, KEY_SIZE);
			memcpy ( l->key[i], buffer, KEY_SIZE);
		}

		read (f, buffer, sizeof (int));
		memcpy ( & (l->this_block_number), buffer, sizeof (int));
	}
	else
	{
		fatal_error ("yeah baby error");
	}


}



void read_all (void ** rootr)
{
	int f;

	f = open ("tree.dat", O_RDWR);

	/* read_block (rootr, f, 0); */

	close (f);

}




