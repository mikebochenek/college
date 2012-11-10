/* CIS*4600 Elements of Theory of Computation
   Assignment #4, Due: Thursday, November 30, 2000
   Michael Bochenek ID: 0041056 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define STATE_NUM 23 
#define INPUT_CHAR_NUM 5 
#define REJECTION RE,'?',OTH

enum {LFT, RHT, OTH};
enum {RE = 9901, AC, UNKNOWN};
enum {FALSE, TRUE};

void init (char * str);
void process_character (void);
void move_on (int current_char);

short direction;
int position;
int state;
int print_mode;
char * string;

int table [STATE_NUM][INPUT_CHAR_NUM][3] = { 
  /* "<"            "a"           "b"           "_"           ">"  
    -----          -----         ------        -----         ----- */ 
{ { 2,'<',RHT}, {REJECTION }, {REJECTION }, {REJECTION }, {REJECTION } }, 
{ {REJECTION }, { 3,'a',RHT}, {REJECTION }, {REJECTION }, {REJECTION } }, 
{ {REJECTION }, { 3,'a',RHT}, { 4,'_',RHT}, { 6,'_',RHT}, {REJECTION } },
{ {REJECTION }, { 5,'b',LFT}, { 4,'b',RHT}, { 7,'_',LFT}, {REJECTION } },
{ {REJECTION }, {REJECTION }, { 5,'b',LFT}, { 3,'a',RHT}, {REJECTION } },

{ {REJECTION }, {REJECTION }, {REJECTION }, { 7,'_',LFT}, {REJECTION } },
{ {REJECTION }, { 8,'a',RHT}, { 7,'_',LFT}, { 7,'_',LFT}, {REJECTION } },
{ {REJECTION }, {REJECTION }, {REJECTION }, { 9,'_',RHT}, {REJECTION } },
{ {REJECTION }, {REJECTION }, {REJECTION }, {10,'_',RHT}, {REJECTION } },
{ {REJECTION }, {REJECTION }, {REJECTION }, {11,'>',LFT}, {REJECTION } },

{ {REJECTION }, {REJECTION }, {REJECTION }, {12,'a',LFT}, {REJECTION } },
{ {REJECTION }, {13,'_',LFT}, {REJECTION }, {13,'_',LFT}, {REJECTION } },
{ {REJECTION }, {14,'a',LFT}, {REJECTION }, {REJECTION }, {REJECTION } },
{ {AC,'?',OTH}, {15,'a',LFT}, {REJECTION }, {REJECTION }, {REJECTION } },
{ {16,'<',RHT}, {15,'a',LFT}, {REJECTION }, {REJECTION }, {REJECTION } },

{ {REJECTION }, {17,'a',RHT}, {REJECTION }, {REJECTION }, {REJECTION } },
{ {REJECTION }, {18,'_',RHT}, {REJECTION }, {REJECTION }, {REJECTION } },
{ {REJECTION }, {18,'a',RHT}, {REJECTION }, {19,'a',LFT}, {REJECTION } },
{ {REJECTION }, {20,'_',LFT}, {REJECTION }, {22,'_',RHT}, {REJECTION } },
{ {REJECTION }, {20,'a',LFT}, {REJECTION }, {21,'a',RHT}, {REJECTION } },

{ {REJECTION }, {18,'_',RHT}, {REJECTION }, {REJECTION }, {REJECTION } },
{ {REJECTION }, {23,'_',RHT}, {REJECTION }, {REJECTION }, {REJECTION } },
{ {REJECTION }, {11,'>',LFT}, {REJECTION }, {REJECTION }, {11,'>',LFT} },
};


