/****************************************************
 * CIS*4200 Computer Communication and Networks     *
 * Assignment #1                                    *
 * October 12, 2000                                 *
 * Michael Bochenek ID# 0041056                     *
 ****************************************************
 * The little demo program inplements a client/server pair of processes
 * that communicate via TCP/IP. (TCP stands for Transport Control
 * Protocol and is the reliable virtual circuit protocol of the Internet 
 * Protocol suite. It delivers a byte stream of data reliably from endpoint to
 * endpoint.)
 * Compile with "cc tcp_demo.c" or "cc -DDEBUG tcp_demo.c" to include
 * debugging printfs.
 */
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <sys/time.h>
#include <unistd.h>

/*
 * Define the number of tries and port number.
 */
#define TEST_TYPE 40
#define TEST_1 1
#define TEST_2 2
#define TEST_3 3


void recv_n (int client_socket, void *data);
void send_n (int client_socket, void *data, int size);
void error(void);

int total_bytes_sent = 0;
int total_bytes_recv = 0;

/*
 * The main program sets up the server side for connection reception and
 * then forks off a child to act as the client. (Normally, the child
 * would be a separate program that is usually running on a different
 * machine, but for local testing, this is sufficient.)
 * The server is disagreeable, since it always answers "NO" for "YES"
 * and vice versa.
 */
int main(int args, char *argv[])
{
   int YESNO_PORT = 2389; 
   int test_info[10];
   struct timeval start_time;
   struct timeval end_time;
   struct timezone tz;
   int shutdown[10] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	struct sockaddr_in socket_name;
   char *data;
   char *blank;
	int client_socket, i;
	struct hostent *hp;

   if (args < 2)
   {
      error();
      exit();
   }

   if (strlen (argv[1]) != 4)
   {
      error();
      exit();
   }

   if ((strcmp (argv[1], "-ftp") == 0))
   {
      if (args != 8)
      {
         error();
         exit();
      }
      else
      {
         test_info[1] = 1;
         test_info[2] = atoi (argv[2]);
         test_info[3] = atoi (argv[3]);
         test_info[5] = atoi (argv[4]);
         test_info[7] = atoi (argv[5]);
         test_info[4] = atoi (argv[6]);
         test_info[8] = atoi (argv[7]);
      }
   }
   if ((strcmp (argv[1], "-tel") == 0))
   {
      if (args != 8)
      {
         error();
         exit();
      }
      else
      {
         test_info[1] = 2;
         test_info[2] = atoi (argv[2]);
         test_info[3] = atoi (argv[3]);
         test_info[6] = atoi (argv[4]);
         test_info[4] = atoi (argv[5]);
         test_info[7] = atoi (argv[6]);
         test_info[8] = atoi (argv[7]);
      }
   }
   if ((strcmp (argv[1], "-rpc") == 0))
   {
      if (args != 8)
      {
         error();
         exit();
      }
      else
      {
         test_info[1] = 3;
         test_info[2] = atoi (argv[2]);
         test_info[3] = atoi (argv[3]);
         test_info[6] = atoi (argv[4]);
         test_info[7] = atoi (argv[5]);
         test_info[4] = atoi (argv[6]);
         test_info[8] = atoi (argv[7]);
      }
   }

   test_info[0] = 40;

   if ( (YESNO_PORT = test_info[7]) == 0)
   {
      printf("Error you must specify the port number as an int.\n");
      exit();
   }

   /*
   for (i = 0; i < args; i++)
   {
      printf ("--> %s\n", argv[i]);
   }
   */
 
   data = (char *) malloc (40);
   if (data == NULL)
   {
      fprintf(stderr, "Could not allocate memory at client - malloc (40)\n");
      exit();
   }

	/*
	 * Create the TCP socket for the client.
	 */
	client_socket = socket(AF_INET, SOCK_STREAM, 0);
	if (client_socket < 0) {
		fprintf(stderr, "Client can't create socket\n");
		exit();
	}
	
	/*
	 * Set up the address of the server in the socket address
	 * structure. "localhost" is a generic address that always
	 * refers to the machine the process is running on.
	 * (It always is assigned IP #127.0.0.1)
	 * Copy the host address (h_addr) into the structure, plus
	 * fill in the port# and address family.
	 */
	hp = gethostbyname("localhost");
	if (hp == NULL) {
		fprintf(stderr, "Child can't get host name\n");
		exit(2);
	}
	if (hp->h_length != 4) {
		fprintf(stderr, "EEK! size should be 4\n");
		exit();
	}
#ifdef BSD
	socket_name.sin_len = sizeof (struct sockaddr_in);
#endif
	socket_name.sin_family = AF_INET;
	bcopy(hp->h_addr, &socket_name.sin_addr, hp->h_length);
	socket_name.sin_port = htons(YESNO_PORT);

	/*
	 * Establish a connection with the server.
	 */
	if (connect(client_socket, (struct sockaddr *)&socket_name,
		sizeof (socket_name)) < 0) {
		fprintf(stderr, "Can't connect to server\n");
		exit();
	}

   for (i = 0; i < TEST_TYPE / 4; i++)
   {
      // printf (" %d ", test_info[i]);
      test_info[i] = htonl (test_info[i]);
   }
   // printf ("\n");

   data = memcpy (data, test_info, 40);

   send_n (client_socket, data, TEST_TYPE);

   gettimeofday (&start_time, &tz);

   if (ntohl (test_info[1]) == TEST_1)
   {
      int direction = ntohl (test_info[5]);
      int size = ntohl (test_info[2]);
      int rand = ntohl (test_info[4]);
      blank = (char *) malloc (ntohl (test_info[2]));
      if (blank == NULL)
      {
         printf ("Client-could not malloc memory %d\n", ntohl (test_info[2]));
         exit ();
      }
         
      for (i = 0; i < (ntohl (test_info[3])); i++)
      {
         if (direction == 0)
         {
            if (rand == 1)
            {
               size = random() % (size + 1);
            }
            send_n (client_socket, blank, size);
 			}
         if (direction == 1)
         {
            recv_n (client_socket, blank);
         }

      }
      free (blank);

   }

   
   if (ntohl (test_info[1]) == TEST_2)
   {  
      int j;
      char *bblank;
      int size = ntohl (test_info[2]);
      int rand = ntohl (test_info[4]);
      blank = (char *) malloc (ntohl (test_info[2]));
      bblank = (char *) malloc (ntohl (test_info[2]));
      if (blank == NULL || bblank == NULL)
      {
         printf ("Client-could not malloc memory %d\n", ntohl (test_info[2]));
         exit ();
      }
         
      for (i = 0; i < (ntohl (test_info[3])); i++)
      {
         for (j = 0; j < rand; j++)
         {
            send_n (client_socket, blank, size);
         }

         recv_n (client_socket, blank);
      }
   }

   
   if (ntohl (test_info[1]) == TEST_3)
   {
      int size = ntohl (test_info[2]);
      int rand = ntohl (test_info[4]);
      blank = (char *) malloc (ntohl (test_info[2]));
      if (blank == NULL)
      {
         printf ("Client-could not malloc memory %d\n", ntohl (test_info[2]));
         exit ();
      }
         
      for (i = 0; i < (ntohl (test_info[3])); i++)
      {
         if (rand == 1)
         {
            size = random() % (size + 1);
         }

         send_n (client_socket, blank, size);

         recv_n (client_socket, blank);
      }
   }


   gettimeofday (&end_time, &tz);
   //printf ("start %d %d -- end %d %d\n", start_time.tv_sec, start_time.tv_usec, end_time.tv_sec, end_time.tv_usec);
   printf ("*************************\n");
   printf ("Seconds elapsed: %f\n",((double) (end_time.tv_sec + 0.000001 * (double) end_time.tv_usec) - (double) (start_time.tv_sec + 0.000001 * (double) start_time.tv_usec)));
 
   send_n (client_socket, shutdown, TEST_TYPE);

   printf ("\nTotal bytes send: %d\n", total_bytes_sent);
   printf ("Total bytes received: %d\n", total_bytes_recv);
   return 0;
}

void error(void)
{
      fprintf(stderr, "Correct usage is:\n"
                      "(each argument must be an integer, and all must be specified)\n"
                      "client -ftp size repeat direction port random nagle\n"
                      "  size - size of packet to transmit.\n"
                      "  repeat - number of times to transmit the packet.\n"
                      "  direction - 0 for send and 1 for receive.\n"
                      "  port - port number to connect on.\n"
                      "  random - 0 for random, and 1 for non-random size.\n"
                      "  nagle - 0 for enable, and 1 for disable algorithm.\n"
                      "client -tel size times reply_size repeat port nagle\n"
                      "  size - size of packet to transmit.\n"
                      "  times - number of times to send small packet.\n"
                      "  reply_size - size of the reply.\n"
                      "  repeat - number of time to send and wait for reply.\n"
                      "  port - port number to connect on.\n"
                      "  nagle - 0 for enable, and 1 for disable algorithm.\n"
                      "client -rpc size repeat reply_size port random nagle\n"
                      "  size - size of packet to transmit.\n"
                      "  repeat - number of times to transmit the packet.\n"
                      "  reply_size - size of the reply.\n"
                      "  port - port number to connect on.\n"
                      "  nagle - 0 for enable, and 1 for disable algorithm.\n"
                  );
} 


void recv_n (int client_socket, void *data)
{
   int return_value = 0;
   int size = 0;

//   printf ("inside recv_n at client\n");

   if ((recv(client_socket, &size, sizeof(size), 0)) != sizeof(size)) 
   {
      fprintf(stderr, "Client recv at recv_n(), getting the size.\n");
      exit();
   }
   
   size = ntohl (size);
   while ((return_value += recv(client_socket, data, size, 0)) != size) 
   {
      printf ("cl-recv_n: bytes recv'ed=%d of size=%d\n", return_value, size);
      /* trying to get more data... */
   }
   //printf ("cl-recv_n: done. bytes=%d\n", return_value);
   total_bytes_recv += (size + 4);
}
 
void send_n (int client_socket, void *data, int size)
{
   int real_size = htonl (size);

//   printf ("inside send_n at client\n");

   if (send(client_socket, &real_size, 4, 0) < 0) {
      fprintf(stderr, "Client send at send_n(), sending the size.\n");
      exit();
   }

   if (send(client_socket, data, size, 0) < 0) {
      fprintf(stderr, "Client send at send_n(), sending the data.\n");
      exit();
   }
   total_bytes_sent += (size + 4);
}

