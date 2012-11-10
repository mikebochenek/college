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
#include <netinet/tcp.h>
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
int main(int args, char* argv[])
{
   int YESNO_PORT = 2389; 
   int *test_info;
	int server_socket, i, sub_socket;
	char req[TEST_TYPE + 1];
	struct sockaddr_in socket_name;
   int j, size = 0, reps, rand, direction, response_size, port, delay;
   char *bblank = NULL;
   struct timeval start_time;
   struct timeval end_time;
   struct timezone tz;

   if (args != 2)
   {
      printf("Error you must specify the port number.\n");
      exit();
   }
   if ( (YESNO_PORT = atoi (argv[1])) == 0)
   {
      printf("Error you must specify the port number as an int.\n");
      exit();
   }

   test_info = (int *) malloc (10);
   if (test_info == NULL)
   {
      fprintf(stderr, "Could not allocate memory (40)\n");
      exit();
   }

	/*
	 * Create the TCP sockets for the server.
	 */
	server_socket = socket(AF_INET, SOCK_STREAM, 0);
	sub_socket = socket(AF_INET, SOCK_STREAM, 0);
	if (server_socket < 0 || sub_socket < 0) {
		fprintf(stderr, "Server can't create socket\n");
		exit();
	}

	/*
	 * Set up the address of the server in the socket address
	 * structure and bind it to the server's socket.
	 */
#ifdef BSD
	socket_name.sin_len = sizeof (struct sockaddr_in);
#endif
	socket_name.sin_family = AF_INET;
	socket_name.sin_addr.s_addr = INADDR_ANY;	/* From anyone */
	socket_name.sin_port = htons(YESNO_PORT);	/* at yesno port */
	if (bind(server_socket, (struct sockaddr *)&socket_name,
		 sizeof (socket_name)) < 0) {
		fprintf(stderr, "Server, can't bind socket address\n");
		exit();
	}

	/*
	 * And listen on the well known port for connection requests.
	 */
	if (listen(server_socket, 1) < 0) {
		fprintf(stderr, "Listen for connection\n");
		exit();
	}

	/*
	 * Accept a connection from a client.
	 */
	if ((sub_socket = accept(server_socket, 0, 0)) < 0) {
		fprintf(stderr, "Server accept failed\n");
		exit();
	}

	/*
	 * Just loop around getting requests and
	 * always reply disagreeably. (no for yes or yes for no)
	 */
	while (1) 
   {
      recv_n (sub_socket, req);

      test_info = memcpy (test_info, req, 40);
      for (i = 0; i < TEST_TYPE / 4; i++)
      {
		   test_info[i] = ntohl (test_info[i]);
         // printf (" [%d] ", test_info[i]);
      }

      if (test_info[0] == 0)
      {
#ifdef DEBUG
      	fprintf(stderr, "Server is done\n");
#endif
         printf ("\n(Server) Total bytes send: %d\n", total_bytes_sent);
         printf ("(Server) Total bytes received: %d\n", total_bytes_recv);
         exit();
      }

      size = test_info[2];
      reps = test_info[3];
      rand = test_info[4];
      direction = test_info[5];
      response_size = test_info[6];
      port = test_info[7];
      delay = test_info[8];

      // printf ("size %d, reps %d, rand %d, direction %d, response_size %d, port %d, delay %d\n", size, reps, rand, direction, response_size, port, delay);


      delay = setsockopt (0, SOL_SOCKET, TCP_NODELAY, &delay, 1); 



      if (test_info[1] == TEST_1)
      {
         test_info[1] = 0;
         /* gettimeofday (&start_time, &tz); */

         bblank = (char *) calloc (size);
         if (bblank == NULL)
         {
            fprintf (stderr, "Server-could not allocate memory %d\n", size);
            exit();
         }

         for (j = 0; j < reps; j++)
         {
            if (direction == 0)
            {
               recv_n (sub_socket, bblank);
            }
            if (direction == 1)
            {
               if (rand == 1)
               {
                  size = random() % (size + 1);
               }
               send_n (sub_socket, bblank, size);
            }

         }

         /* gettimeofday (&end_time, &tz); */
      }
     
      if (test_info[1] == TEST_2)
      {
         int k;
         char *blank;
         char *bblank;
         test_info[1] = 0;
         /* gettimeofday (&start_time, &tz); */

         bblank = (char *) malloc (size);
         blank = (char *) malloc (response_size);
         if (bblank == NULL || blank == NULL)
         {
            fprintf (stderr, "Server-could not allocate memory %d\n", response_size);
            exit();
         }

         for (j = 0; j < reps; j++)
         {
            for (k = 0; k < rand; k++)
            {
               recv_n (sub_socket, bblank);
            }

            send_n (sub_socket, blank, response_size);
         }
      }
     

      if (test_info[1] == TEST_3)
      {
         char *blank = NULL;
         test_info[1] = 0;
         /* gettimeofday (&start_time, &tz); */

         bblank = (char *) calloc (size);
         blank = (char *) calloc (response_size);
         if (bblank == NULL || blank == NULL)
         {
            fprintf (stderr, "Server-could not allocate memory %d\n", size);
            exit();
         }

         for (j = 0; j < reps; j++)
         {
            if (rand == 1)
            {
               response_size = random() % (response_size + 1);
            }

            recv_n (sub_socket, bblank);

            send_n (sub_socket, blank, response_size);
         }
      }
     
	}
   return 0;
}

void recv_n (int client_socket, void *data)
{
   int return_value = 0;
   int size = 0;

//   printf ("inside recv_n at server\n");

   if ((recv(client_socket, &size, sizeof(size), 0)) != sizeof(size)) 
   {
      fprintf(stderr, "Server recv at recv_n(), getting the size.\n");
      exit();
   }
   
   size = ntohl (size);
   while ((return_value += recv(client_socket, data, size, 0)) != size) 
   {
      printf ("sr-recv_n: bytes recv'ed=%d of size=%d\n", return_value, size);
      /* trying to get more data... */
   }
   total_bytes_recv += (size + 4);
}
  
void send_n (int client_socket, void *data, int size)
{
   int real_size = htonl (size);
//   printf ("inside send_n at server\n");

   if (send(client_socket, &real_size, 4, 0) < 0) {
      fprintf(stderr, "Server send at send_n(), sending the size.\n");
      exit();
   }

   if (send(client_socket, data, size, 0) < 0) {
      fprintf(stderr, "Server send at send_n(), sending the data.\n");
      exit();
   }
   total_bytes_sent += (size + 4);
}

