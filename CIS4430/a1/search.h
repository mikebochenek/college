/**
 * CIS*4430 Information Organization and Retrieval
 * Assignment #1, due: Tuesday, Febraury 27, 2001
 * Michael Bochenek [ID: 0041056]
 * email: mboche01@uoguelph.ca
 */

#ifndef _SEARCH_H_
#define _SEARCH_H_


node * search (void * node_ptr, char * value);
node * search_range (void * node_ptr, int * size, char * value, char *upper_value);


#endif

