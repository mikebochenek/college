/**
 * CIS*4430 Information Organization and Retrieval
 * Assignment #1, due: Tuesday, Febraury 27, 2001
 * Michael Bochenek [ID: 0041056]
 * email: mboche01@uoguelph.ca
 */

#ifndef _OTHER_H_
#define _OTHER_H_

node * read_KPtxt (int * list_counter_in); 
void fatal_error (char *error_string);

leaf_node * create_new_leaf_node ();
interior_node * create_new_interior_node ();

void print_node (void * n);
void print_node_recursively (void * n, int indent);
void print_node_recursively_short (void * n, int indent);
void print_node_recursively_word (void * n, int indent);
void * run();

#endif


