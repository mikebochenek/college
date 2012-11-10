/**
 * CIS*4430 Information Organization and Retrieval
 * Assignment #1, due: Tuesday, Febraury 27, 2001
 * Michael Bochenek [ID: 0041056]
 * email: mboche01@uoguelph.ca
 */

#include "a1.h"
#include "other.h"
#include "insert.h"

void * root = NULL;
int counter = 0;

int main (int argv, char ** argc) 
{
	int w = 0;
	void * temp = NULL;
	char * readinmsg = "________________\n(1) Insert\n(2) Delete\n(3) Search\n(4) Search Range\n(5) Quit\nEnter your choice: ";
	char readin [BLOCKSIZE] = "";

	root = (void *) create_new_leaf_node();
	((leaf_node *) root)->is_root = TRUE;

	read_all (&root);

	run();

	do
	{
		printf ("%s", readinmsg);
		scanf ("%s", readin);

		if (strcmp (readin, "1") == 0)
		{
			node input_node;

			printf ("Enter key: ");
			scanf ("%s", (input_node.key) );
			printf ("Enter pointer: ");
			scanf ("%s", (input_node.pointer) );

			insert (root, & input_node, &temp);

			assign_block_number (root, &w);
			write_all (root, w);
		}
		else if (strcmp (readin, "2") == 0)
		{
			node input_node;

			printf ("Enter key: ");
			scanf ("%s", (input_node.key) );

			delete (root, & input_node, NULL);

			assign_block_number (root, &w);
			write_all (root, w);
		}
		else if (strcmp (readin, "3") == 0)
		{
			char search_str [KEY_SIZE];
			node * result_node = NULL;

			printf ("Enter key: ");
			scanf ("%s", search_str);

			result_node = search_on_disk (root, search_str);

			if (result_node == NULL)
			{
				printf ("Not found.\n");
			}
			else
			{
				printf ("Found.\n");
				printf ("Key = %s ", result_node->key);
				printf ("Pointer = %s\n", result_node->pointer);
			}
		}
		else if (strcmp (readin, "4") == 0)
		{
			char search_str [KEY_SIZE];
			char upper_str [KEY_SIZE];
			node * result_node = NULL;
			int result_set = 0;

			printf ("Enter lower bound key: ");
			scanf ("%s", search_str);

			printf ("Enter upper bound key: ");
			scanf ("%s", upper_str);

			result_node = search_range_on_disk (root, &result_set, search_str, upper_str);

			if (result_node == NULL)
			{
				printf ("Not found.\n");
			}
			else
			{
				int j;
				printf ("Found.\n");
				for (j = 0; j < result_set; j++)
				{
					printf ("Key = %s ", (result_node[j]).key);
					printf ("Pointer = %s\n", (result_node[j]).pointer);
				}
			}
		}
		else if (strcmp (readin, "5") == 0)
		{
			exit (0);
		}

	} while (readin != NULL);

	return 0; 
}



