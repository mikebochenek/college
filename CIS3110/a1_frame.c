/*
 ******************************************************************
 * CIS*3110 Assignment #1
 * February 2, 2000
 * Michael Bochenek, ID#0041056                                   
 ******************************************************************
 * This program emulates a security system controller that handles
 * access to an entrance. It uses co-routines for unlatching/latching
 * the door and activation of a camera when the door opens. An alarm
 * clock interrupt serves as a time counter and also updates random
 * events for the emulation.
 */
#include <stdio.h>
#include "/u4/cs311/corout.h"
#include <signal.h>
#include <sys/time.h>

/*
 * Some macro definitions that can be used with assignment #1.
 */
#define	DISPLAY_SIZE		(8+1)
#define	CODE_VALID(c)		((c) & 0x8)

/*
 * Control/Status register bits
 */

#define	DOOR_LATCHED		0x1
#define	CAMERA_ACTIVATE		0x2
#define	DOOR_OPEN		0x4
#define	ALL_BITS		0x7

void alarm_clock(), display_status();

/*
 * Use CODEFINE to define the global (shared) storage for each co-routine.
 */

CODEFINE(camera_activate);
CODEFINE(door_unlatch);
CODEFINE(door_code_check);

/*
 * Global (shared) variables.
 * display_buffer	- a 8 character + null character buffer that
 *			  holds a display string
 * door_code_register	- an integer that emulates the door code input
 *			  The macro CODE_VALID() checks for a valid code.
 * control_status	- an integer that simulates a control/status
 *			  register on the device. Only the 3 low order
 *			  bits are used.
 */
char		display_buffer[DISPLAY_SIZE];
unsigned int	door_code_register;
unsigned int	control_status;
unsigned int	clock_time;

/*
 * Stuff to handle the interrupts. The variables are used by the
 * macros after being initialized in main().
 */
sigset_t sigmsk, emptymsk;
#define	WAIT_FOR_INTERRUPT	sigsuspend(&emptymsk)
#define	MASK_INTERRUPT		sigprocmask(SIG_BLOCK, &sigmsk, (sigset_t *)0)
#define	UNMASK_INTERRUPT	sigprocmask(SIG_UNBLOCK, &sigmsk, (sigset_t *)0)

/*
 * Camera activate is called when the door is found open while unlatched.
 * It sets the camera activate bit, waits until that bit is cleared, to
 * indicate that the camera has taken the picture and then calls back to
 * the Door Unlatch routine.
 */
COBEGIN(camera_activate)

   while (1) {

      MASK_INTERRUPT;
         // change buffer to SMILE! and output status
         control_status |= CAMERA_ACTIVATE;
         strcpy (display_buffer, "SMILE!");
         display_status();
      UNMASK_INTERRUPT;

      // Wait until the CAMERA_ACTIVATE bit is clearted by alarm_clock()...
      while ( (control_status & CAMERA_ACTIVATE) ) {
      }

      MASK_INTERRUPT;
         // change buffer to CAM OFF and output status
         strcpy (display_buffer, "CAM OFF");
         display_status();
      UNMASK_INTERRUPT;

      // wait for the alarm_clock() to close the door...
      while ( (control_status & DOOR_OPEN) ) {
	   }

      MASK_INTERRUPT;
         // change buffer to CLOSED and output status
         strcpy (display_buffer, "CLOSED");
         display_status();
		
         // calls back to door_unlatch
         COCALL(camera_activate, door_unlatch);
      UNMASK_INTERRUPT;
   }

COEND

/*
 * Door Unlatch unlatches the door, then waits 5 clock ticks and relatches
 * the door. If the door is found open during the 5 clock ticks,
 * camera_activate is called to take a picture. After the 5 clock ticks
 * and the door is closed, it can be relatched.
 */
COBEGIN(door_unlatch)

   int original_clock_time;

   while (1) {

      MASK_INTERRUPT;
         // change buffer to UNLOCKED and output status
         control_status &= ~DOOR_LATCHED;
         strcpy (display_buffer, "UNLOCKED");
         display_status();
      UNMASK_INTERRUPT;

      MASK_INTERRUPT;
         original_clock_time = clock_time;
		UNMASK_INTERRUPT;

      while ( clock_time <= original_clock_time + 5 )
	   // loop until 5 clock-ticks has expired
      { 
         MASK_INTERRUPT;
			   // if alarm_clock() "opens" the door ...	  
            if (control_status & DOOR_OPEN) { 

				   // change buffer to OPENED and output status
               strcpy (display_buffer, "OPENED");
               display_status();
					
		         // call camera_activate 	
               COCALL(door_unlatch, camera_activate);
            }
         UNMASK_INTERRUPT;
      }
      
      MASK_INTERRUPT;
         // immediately afterwards, set DOOR_LATCHED, and output status
	      control_status |= DOOR_LATCHED;
         strcpy (display_buffer, "LOCKED");
         display_status();

         // calls back to door_code_check, and ready for another operation
         COCALL(door_unlatch, door_code_check);
      UNMASK_INTERRUPT;

   }

COEND

/*
 * Door Code Check. This routine keeps checking the door code until a
 * valid code is found in the door_code_register. It will handle 20
 * valid codes before terminating.
 */
COBEGIN(door_code_check)

	int i;

	for (i = 0; i < 20; i++) {

      while (1) { 
		   // Check and wait for a valid door code.
         if (CODE_VALID(door_code_register)) {
		
		      // And then call door_unlatch to unlock it, etc.
				MASK_INTERRUPT;
		         door_code_register = 0;
				UNMASK_INTERRUPT;
		      COCALL(door_code_check, door_unlatch);
            
				// exit while, so that simulation runs 20 times (see for loop)
				break; 
                
            }
         }
	   }
	
	exit();

COEND

/*
 * Display Status: Just print out the current value of the display_status
 * buffer and the control_status register.
 */
void
display_status()
{

MASK_INTERRUPT;
	printf("%10s\t\tCS Bits %c %c %c\n", display_buffer,
		(control_status & DOOR_OPEN) ? '*' : 'O',
		(control_status & CAMERA_ACTIVATE) ? '*' : 'O',
		(control_status & DOOR_LATCHED) ? '*' : 'O');
UNMASK_INTERRUPT;

}

/*
 * Just initialize the global shared variables, start the alarm clock
 * and then call door_code_check to get the ball rolling.
 */
main()
{
	struct itimerval val;

	/*
	 * Initialize the global variables.
	 */
	strcpy(display_buffer, "LOCKED");
	control_status = DOOR_LATCHED;
	door_code_register = 0;
	clock_time = 0;

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
	 * Initialize the co-routines with COCREATE().
	 */
	printf("In main: initialize co-routines...");
	
        COCREATE(camera_activate);
        COCREATE(door_unlatch);
        COCREATE(door_code_check);

	/*
	 * Use COSTART() to get the first co-routine going. It should
	 * never return here.
	 */
	printf("and start first co-routine door_code_check\n");
	COSTART(door_code_check);

	/*
	 * Should never happen!
	 */
	printf("EEK: Should never get here!\n");
}

/*
 * Alarm Clock: Tick off clock_time and also tweek certain inputs to
 * drive the emulation. This algorithm should be used by you solution.
 */
void
alarm_clock()
{
	static int door_state = 0;

	clock_time++;

	/*
	 * Rattle through the door states as the clock ticks...
	 */
	switch (door_state) {
	case 0:
		if (!(control_status & DOOR_LATCHED)) {
			control_status |= DOOR_OPEN;
			door_state = 1;
		} else if (!CODE_VALID(door_code_register))
			door_code_register = (random() & 0xf);
		break;
	case 1:
		if (control_status & CAMERA_ACTIVATE) {
			control_status &= ~CAMERA_ACTIVATE;
			door_state = 2;
		}
		break;
	case 2:
		if ((random() & 0x5) == 0x5) {
			control_status &= ~DOOR_OPEN;
			door_state = 3;
		}
		break;
	case 3:
		if (control_status & DOOR_LATCHED)
			door_state = 0;
	};
}
