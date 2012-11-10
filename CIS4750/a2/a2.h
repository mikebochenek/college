/* CIS*4750 Artificial Intelligence
   Assignment #2, Due: Tuesday October 3, 2000
   Michael Bochenek ID: 0041056 */

#define SIZE 3 

#define CLEAN 'C' 
#define DIRTY 'D' 
#define VACUUM '*'

int map[SIZE][SIZE];
int visited[SIZE][SIZE];
int score = 0, visit_count = 0;
enum boolean {FALSE, TRUE};
enum dir {EAST, WEST, SOUTH, NORTH};

void print_pic (void);
void init_state (int vacuum_position);
int get_start_pos (void);
int move (int current_pos, int *direction);


