/**
 * CIS*4430 Information Organization and Retrieval
 * Assignment #1, due: Tuesday, Febraury 27, 2001
 * Michael Bochenek [ID: 0041056]
 * email: mboche01@uoguelph.ca
 */

#ifndef _FILEIO_H_
#define _FILEIO_H_


void assign_block_number (void * n, int * block_number);
void write_all (void * root, int start_node);
void write_block (void * n, int block_number, int f);
void write_wrapper (void * n, int f);
void read_all (void ** root);
void read_block (void * n, int f, int block);
void read_wrapper (void * n, int f, int block_number);

#endif

