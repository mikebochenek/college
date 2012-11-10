/*
 ******************************************************************
 * CIS*3110 Assignment #2
 * February 17, 2000
 * Michael Bochenek, ID#0041056                                   
 ******************************************************************
 * This program emulates either a two drive striped disk subsystem
 * (often inaccurately referred to as RAID 0) or a RAID 4 disk subsystem
 * with two data drives plus a parity drive. The emulation uses separate
 * Unix processes to emulate the subsystem controller and the drives. A
 * Unix Alarm Clock Signal is used to simulate the disk interrupts.
 */
#include <stdio.h>
#include <signal.h>
#include <sys/time.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <errno.h>

/*
 * Define tunable parameters.
 * QUEUESIZE - the size of the fixed length request queues
 * NUMREQUEST - the number of requests to generate during the simulation
 *		run
 */
#define	QUEUESIZE	6
#define	NUMREQUEST	50

/*
 * Define any global variables that are not shared and any other
 * handy constants and macros.
 */
extern int errno;
int clock_time = 0;
void ctrl_striped(), ctrl_raid(), alarm_clock(), drive_io(), disk_drive();
#define	BLK_SIZE	512
#define	DISK_SIZE	10240
#define	N_TICKS(o, b)	(((b) >= (o)) ? (((b) - (o)) / 1000 + 1) : \
				(((o) - (b)) / 1000 + 1))

/*
 * The shared memory region consists of the I/O request queue, the
 * disk control registers and summary statistics.
 */
struct io_request {
	int	op;			/* See OP_xxx			*/
	int	block_num;		/* Block number			*/
	char	buf_addr[BLK_SIZE];	/* Memory address		*/
};

struct disk_ctrl {
	int	op;			/* See OP_xxx			*/
	int	diskblk_num;		/* Disk block number		*/
	char	*buf_addr;		/* Memory address		*/
};

/*
 * Op field: 0 - read, 1 - write
 *	-1 - request generation (ie. simulation) done
 */
#define	OP_SIMDONE	-1
#define	OP_READ		0
#define	OP_WRITE	1

/*
 * Define a data structure for the shared memory region.
 * Everything that is to be shared between processes, except for the
 * semaphores, goes in here.
 */
struct shared_data {
	struct io_request	ctrl_q[QUEUESIZE];
	int			start_pos;
	int			end_pos;
	struct disk_ctrl	disk_registers[3];
	int			io_count;
};

/*
 * Assign a number/name to each semaphore.
 * Semaphores are used to co-ordinate access to the request queues and
 * to co-ordinate activity with the disks.
 * There is one of each of the three disk semaphores for each disk.
 * (The macros DISK_AVAIL(), DISK_START(), and DISK_DONE() refer to
 *  these semaphores.)
 */
#define	Q_NOTFULL		0
#define	Q_NOTEMPTY		1
#define	DISK_AVAIL0		2
#define	DISK_START0		5
#define	DISK_DONE0		8
#define	NUM_SEM			11

#define	DISK_AVAIL(d)	(DISK_AVAIL0 + (d))
#define	DISK_START(d)	(DISK_START0 + (d))
#define	DISK_DONE(d)	(DISK_DONE0 + (d))

/*
 * The Linux semaphore ops are done using the fields of sembuf.
 * sem_num - Which semaphore of the set, as defined below
 * sem_op  - set to 1 for "signal" and -1 for "wait"
 * sem_flg - set to 0 for our purposes
 * The macros WAIT_SEM, SIGNAL_SEM and INITIALIZE_SEM are defined
 * to, hopefully, simplify the code.
 */

#define	WAIT_SEM(s) { \
	struct sembuf sb; \
	sb.sem_num = (s); \
	sb.sem_op = -1; \
	sb.sem_flg = 0; \
	if (semop(semid, &sb, 1) < 0) { \
		fprintf(stderr, "Wait sem %d failed errno=%d\n", s, errno); \
		exit(4); \
	} }

#define	SIGNAL_SEM(s) { \
	struct sembuf sb; \
	sb.sem_num = (s); \
	sb.sem_op = 1; \
	sb.sem_flg = 0; \
	if (semop(semid, &sb, 1) < 0) { \
		fprintf(stderr, "Signal sem %d failed errno=%d\n", s, errno); \
		exit(4); \
	} }

#define INITIALIZE_SEM(s, n) { \
	union semun semarg; \
	semarg.val = (n); \
	if (semctl(semid, (s), SETVAL, semarg) < 0) { \
		fprintf(stderr, "Initialize sem %d failed errno=%d\n", s, errno); \
		exit(4); \
	} }

/*
 * Global data, (not shared between processes).
 */
int semid;
struct shared_data *shared_ptr;

/*
 * Mask and unmask the interrupt (signal).
 */
#define	MASK_INTR	sigprocmask(SIG_BLOCK, &sigmsk, (sigset_t *)0)
#define	UNMASK_INTR	sigprocmask(SIG_UNBLOCK, &sigmsk, (sigset_t *)0)
#define	WAIT_FOR_INTR	sigsuspend(&emptymsk)


/* 
 * Little function to ease printing of simple messages ...
 */
void print_msg (char *s)
{
#ifdef DEBUG
   fprintf(stderr, "%s\n", s);
#endif
}

	 
/*
 * This function is the body of the child process that emulates a
 * 2 drive striped disk subsystem.
 */
void ctrl_striped()
{
   int op;
   int drive_blkno;
   int drive = 0;
   char read_addr_buffer[BLK_SIZE];

   print_msg ("Starting disk subsystem controller... ");

   do { 
      WAIT_SEM(Q_NOTEMPTY);
      op = shared_ptr->ctrl_q[shared_ptr->start_pos].op;
      drive_blkno = shared_ptr->ctrl_q[shared_ptr->start_pos].block_num;
      shared_ptr->start_pos = (shared_ptr->start_pos + 1) % QUEUESIZE;
      SIGNAL_SEM(Q_NOTFULL);

#ifdef DEBUG
	   fprintf(stderr, "ctrl_striped() Processing IO request: ");
	   fprintf(stderr, "operation = %d  ", op);
	   fprintf(stderr, "block = %d\n", drive_blkno);
#endif

      // striped stuff..
      drive = drive_blkno % 2;
      drive_blkno = drive_blkno / 2;

      if (op == OP_SIMDONE)
      {
         drive_io (op, 0, drive_blkno, read_addr_buffer);
         drive_io (op, 1, drive_blkno, read_addr_buffer);
      }
      else
      {
         drive_io (op, drive, drive_blkno, read_addr_buffer);
      }


   } while (op != OP_SIMDONE);

   print_msg ("Shutting down disk subsystem controller... ");
   // ...ok
}


/*
 * This function is the body of the child process that emulates a
 * 2 drive raid disk subsystem.
 */
void ctrl_raid()
{
   int j;
   int op;
   int drive_blkno, new_drive_blkno;
   int drive = 0;
   char read_addr_buffer[BLK_SIZE];
   char second_read_addr_buffer[BLK_SIZE];
   char parity_buffer[BLK_SIZE];

   print_msg ("Starting disk subsystem controller... ");

   do { 

      WAIT_SEM(Q_NOTEMPTY);
      op = shared_ptr->ctrl_q[shared_ptr->start_pos].op;
      drive_blkno = shared_ptr->ctrl_q[shared_ptr->start_pos].block_num;
      shared_ptr->start_pos = (shared_ptr->start_pos + 1) % QUEUESIZE;
      SIGNAL_SEM(Q_NOTFULL);

#ifdef DEBUG
	   fprintf(stderr, "ctrl_raid() Processing IO request: ");
	   fprintf(stderr, "operation = %d  ", op);
	   fprintf(stderr, "block = %d\n", drive_blkno);
#endif

      // RAID 4 stuff..
      drive = drive_blkno % 2;
      new_drive_blkno = drive_blkno / 2;

      if (op == OP_SIMDONE)
      {
         drive_io (op, 0, new_drive_blkno, read_addr_buffer);
         drive_io (op, 1, new_drive_blkno, read_addr_buffer);
         drive_io (op, 2, new_drive_blkno, read_addr_buffer);
      }
      else
      {
         drive_io (op, drive, new_drive_blkno, read_addr_buffer);
         // perform requested IO operation

         if (op == OP_WRITE)
         {
            drive_io (OP_READ, (drive_blkno + 1) % 2, new_drive_blkno, second_read_addr_buffer);
            // read the other disk, needed to create parity block

            // create parity block
            for (j = 0; j < BLK_SIZE; j++)
            {
               parity_buffer[j] = read_addr_buffer[j] ^ second_read_addr_buffer[j];
            }
 
#ifdef DEBUG
	         fprintf(stderr, "ctrl_raid(): Calculate and write parity to disk.\n");
#endif
            drive_io (OP_WRITE, 2, new_drive_blkno, parity_buffer);
            // write parity block to the parity drive, will be 2 for RAID 4
         
         }
      }

   } while (op != OP_SIMDONE);

   print_msg ("Shutting down disk subsystem controller... ");
   // ...ok
}


/*
 * This function performs the given operation on the associated drive.
 * It basically fills in the disk_registers and then waits for completion.
 */
void
drive_io(op, drive, drive_blkno, addr)
	int op;
	int drive;
	int drive_blkno;
	char *addr;
{

#ifdef DEBUG
	fprintf(stderr, "drive_io() drive = %d operation = %d block = %d\n", drive, op, drive_blkno);
#endif
	/*
	 * First, wait to see if the drive is available.
	 */
	WAIT_SEM(DISK_AVAIL(drive));
	shared_ptr->disk_registers[drive].op = op;
	shared_ptr->disk_registers[drive].diskblk_num = drive_blkno;
	shared_ptr->disk_registers[drive].buf_addr = addr;
	SIGNAL_SEM(DISK_START(drive));
	WAIT_SEM(DISK_DONE(drive));
}


/*
 * This function is the body of the processes that emulate the disks.
 * They basically wait for the disk_registers to be filled in and then
 * wait for an interrupt to indicate completion.
 * An actual disk would interrupt upon completion, but for the purpose
 * of this simulation, this process will wait N clock ticks, where N
 * is a function of the seek distance.
 */
void disk_drive(drive_num)
	int drive_num;
{
	register int i;
	register char *cp;
	int op, disk_blkno, end_time, keep_loopin = 1;
	static int block_pos = 0;
	struct itimerval val;
	sigset_t sigmsk, emptymsk;
   char *data_io = NULL;

#ifdef DEBUG
	fprintf(stderr, "Starting disk drive#%d\n", drive_num);
#endif

	/*
	 * Just make the signal mask the Alarm clock signal.
	 */
	sigemptyset(&emptymsk);
	sigemptyset(&sigmsk);
	sigaddset(&sigmsk, SIGALRM);

	/*
	 * Make alarm_clock() the handler for the Alarm clock signal.
	 */
	signal(SIGALRM, alarm_clock);

	/*
	 * Set the timer to generate an Alarm clock signal every 100 msec.
	 */
	val.it_interval.tv_sec = 0;
	val.it_interval.tv_usec = 100000;
	val.it_value.tv_sec = 0;
	val.it_value.tv_usec = 100000;
	setitimer(ITIMER_REAL, &val, (struct itimerval *)0);

	/*
	 * Just loop around doing disk ops, until OP_SIMDONE.
	 */

   do {
      int temp_clock_time = 0, last_disk_blkno = 0;

MASK_INTR;
      WAIT_SEM(DISK_START(drive_num));
UNMASK_INTR;
      op = shared_ptr->disk_registers[drive_num].op;
      disk_blkno = shared_ptr->disk_registers[drive_num].diskblk_num;
      data_io = shared_ptr->disk_registers[drive_num].buf_addr;

      // actual disk IO would take place NOW!

      temp_clock_time = clock_time + N_TICKS(disk_blkno, last_disk_blkno);
      while (clock_time < temp_clock_time)
         ;     
   
      // read/write has been performed to data_io..

      (shared_ptr->io_count)++;

      last_disk_blkno = disk_blkno;

	   SIGNAL_SEM(DISK_DONE(drive_num));
      SIGNAL_SEM(DISK_AVAIL(drive_num));

   } while (op != OP_SIMDONE);
    
   (shared_ptr->io_count)--; // to account for the OP_SIMDONE extra

#ifdef DEBUG
	fprintf(stderr, "Shutting down disk drive#%d\n", drive_num);
#endif
   // ...ok
}

/*
 * The alarm clock timer. Just increment clock_time.
 */
void
alarm_clock()
{
	clock_time++;
}


/*
 * The main program creates the shared memory region and forks off the
 * processes to emulate the subsystem.
 * This process then generates I/O requests at random and inserts
 * them in the queue. When done generating requests, it marks the
 * end of requests and waits for the children to terminate, so that
 * it can print out the total I/O count.
 */
main(argc, argv)
	int argc;
	char *argv[];
{
	register int i;
   int j;
	int shmid, child_status, num_drives = 2, raid = 0;
	pid_t disk_pids[3], subsystem_pid;

	/*
	 * Check for the "-r" argument.
	 */
	if (argc == 2 && !strcmp(argv[1], "-r")) {
		raid = 1;
		num_drives = 3;
	} else if (argc != 1) {
		printf("Usage: disk_sim [-r]\n");
		exit(1);
	}

	/*
	 * Seed the random number generator.
	 */
	srandom(time(0));

	/*
	 * Create the shared memory region.
	 */
	shmid = shmget(IPC_PRIVATE, sizeof (struct shared_data), 0600);
	if (shmid < 0) {
		fprintf(stderr, "Unable to create shared memory region\n");
		exit (1);
	}
   // ...ok

	/*
	 * and map the shared data region into our address space at an
	 * address selected by Linux.
	 */
	shared_ptr = (struct shared_data *)shmat(shmid, (char *)0, 0);
	if (shared_ptr == (struct shared_data *)0) {
		fprintf(stderr, "Unable to map shared memory region\n");
		exit (2);
	}
   // ...ok

	/*
	 * Now, create the semaphores, by creating the semaphore set.
	 * Under Linux, semaphore sets are stored in an area of memory
	 * handled much like a shared region. (A semaphore set is simply
	 * a bunch of semaphores allocated to-gether.)
	 */
	semid = semget(IPC_PRIVATE, NUM_SEM, 0600);
	if (semid < 0) {
		fprintf(stderr, "Unable to create semaphore set\n");
		exit(3);
	}
	// ...ok

	/*
	 * and initialize them.
	 * Semaphores are meaningless if they are not initialized.
	 */
	INITIALIZE_SEM(Q_NOTFULL, QUEUESIZE);
	INITIALIZE_SEM(Q_NOTEMPTY, 0);

	INITIALIZE_SEM(DISK_AVAIL(0), 1);
	INITIALIZE_SEM(DISK_START(0), 0);
	INITIALIZE_SEM(DISK_DONE(0), 0);
	
	INITIALIZE_SEM(DISK_AVAIL(1), 1);
	INITIALIZE_SEM(DISK_START(1), 0);
	INITIALIZE_SEM(DISK_DONE(1), 0);

	INITIALIZE_SEM(DISK_AVAIL(2), 1);
	INITIALIZE_SEM(DISK_START(2), 0);
	INITIALIZE_SEM(DISK_DONE(2), 0);
   // ...ok

	/*
	 * And initialize the shared data
	 */
	shared_ptr->start_pos = shared_ptr->end_pos = 0;

#ifdef DEBUG
	fprintf(stderr, "main() after all initialization\n");
#endif

	/*
	 * Fork off the children that simulate the disks.
	 */
	for (j = 0; j < num_drives; j++) 
	{
#ifdef DEBUG
      fprintf(stderr, "main() Creating child process for disk drive #%d\n", j);
#endif

      if (fork() == 0)
	   {
	      disk_pids[j] = getpid();
#ifdef DEBUG
	      fprintf(stderr, "main() PID of disk drive #%d process is %d\n", j, disk_pids[j] );
#endif
         disk_drive (j);

         exit ();
	   }
   }
   // ...ok


	/*
	 * And the subsystem controller process.
	 */
	if (raid == 1 && num_drives == 3) 
	{
      print_msg ("*** Using RAID 4 ***");
      if (fork() == 0)
	   {
	      subsystem_pid = getpid();
#ifdef DEBUG
	      fprintf(stderr, "main() PID of RAID 4 controller process is %d\n", subsystem_pid);
#endif
         ctrl_raid();

         exit();
	   }
   }
	if (raid == 0 && num_drives == 2) 
	{
      print_msg ("*** Using STRIPED (RAID 0) ***");
      if (fork() == 0)
	   {
	      subsystem_pid = getpid();
#ifdef DEBUG
	      fprintf(stderr, "main() PID of STRIPED controller process is %d\n", subsystem_pid);
#endif
         ctrl_striped();

         exit ();
	   }
   }
   // ...ok

	/*
	 * Loop around generating I/O requests at random.
	 * (the parent)
	 */
	for (i = 0; i < NUMREQUEST; i++) {
		/*
		 * Add a request to the queue for the controller.
		 */
		
      WAIT_SEM(Q_NOTFULL);
		shared_ptr->ctrl_q[shared_ptr->end_pos].op = (random() & 0x1);
		shared_ptr->ctrl_q[shared_ptr->end_pos].block_num =
			(random() % DISK_SIZE);

#ifdef DEBUG
   fprintf(stderr, "main() IO request generated: ");
	fprintf(stderr, "operation = %d ", shared_ptr->ctrl_q[shared_ptr->end_pos].op);
	fprintf(stderr, "block = %d\n", shared_ptr->ctrl_q[shared_ptr->end_pos].block_num);
#endif

		shared_ptr->end_pos = (shared_ptr->end_pos + 1) % QUEUESIZE;
		SIGNAL_SEM(Q_NOTEMPTY);
	}

	/*
	 * Mark the end of requests on both queues.
	 */
	WAIT_SEM(Q_NOTFULL);
	shared_ptr->ctrl_q[shared_ptr->end_pos].op = OP_SIMDONE;
	shared_ptr->ctrl_q[shared_ptr->end_pos].block_num = -1;

#ifdef DEBUG
   fprintf(stderr, "main() IO request generated: END SIMULATION\n");
#endif

	shared_ptr->end_pos = (shared_ptr->end_pos + 1) % QUEUESIZE;
	SIGNAL_SEM(Q_NOTEMPTY);
   // ...ok


#ifdef DEBUG
	fprintf(stderr, "main() producer (parent) wait for children to terminate\n");
#endif
	/*
	 * Wait for the drive processes and controller process to terminate.
	 */
	for (i = 0; i < num_drives; i++)
		wait(&child_status);
	wait(&child_status);

	/*
	 * All done, just print out the results.
	 */
	printf("*** Total I/O ops = %d *** \n", shared_ptr->io_count);

#ifdef DEBUG
	fprintf(stderr, "main() Destroy shared memory\n");
#endif
	/*
	 * And destroy the shared data area and semaphore set.
	 * First detach the shared memory segment via shmdt() and then
	 * destroy it with shmctl() using the IPC_RMID command.
	 * Destroy the semaphore set in a similar manner using a semctl()
	 * call with the IPC_RMID command.
	 */
	shmdt((char *)shared_ptr);
	shmctl(shmid, IPC_RMID, (struct shmid_ds *)0);
	semctl(semid, 0, IPC_RMID, (union semun)0);
}
