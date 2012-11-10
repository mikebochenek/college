/*
 ***********************************************************************
 ***************************** C L I E N T *****************************
 ***********************************************************************
 * Michael Bochenek ID [41056]
 * This little program is just like tcp_demo.c, except that it supports
 * several clients and demonstrates the use of a select() system call
 * on the server.
 * Compile with "cc tcp_demo2.c" or "cc -DDEBUG tcp_demo2.c" to include
 * debugging printfs.
 */
#include <stdio.h>
#include <sys/types.h>
#include <sys/time.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>

/*
 * Define the number of tries, number of clients and port number.
 */
#define	NUM_TRY		10
#define	NUM_CLIENT  5 
#define	YESNO_PORT	1999
#define  COM_BUFFER 1024
#define  QUERY_BY_IP 4441 
#define  QUERY_BY_NAME 4442

/*
 * The main program sets up the server side for connection reception and
 * then forks off children to act as the clients. (Normally, the children
 * would be separate programs running on different
 * machines, but for local testing, this is sufficient.)
 * The server is disagreeable, since it always answers "NO" for "YES"
 * and vice versa.
 */
main()
{
	int server_socket, i, num_connect;
	int subsock[NUM_CLIENT], subnum = 0, nfnd, nfds;
	struct sockaddr_in socket_name;
	fd_set readfds, checkfds;

#ifdef DEBUG
	fprintf(stderr, "Start the client by forking a child\n");
#endif
	//  Fork off the child to act as the client, while the main()
	//  parent process acts as the server.

	for (i = 0; i < NUM_CLIENT; i++)
	
   if (fork() == 0) {
		/* the child */
		int client_socket, reply_size;
		struct hostent *hp;

		/*
		 * Create the TCP socket for the client.
		 */
		client_socket = socket(AF_INET, SOCK_STREAM, 0);
		if (client_socket < 0) {
			fprintf(stderr, "FATAL ERROR: Client can't create socket\n");
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
			fprintf(stderr, "FATAL ERROR: Child can't get host name\n");
			exit(2);
		}
		if (hp->h_length != 4) {
			fprintf(stderr, "FATAL ERROR: EEK! size should be 4\n");
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
			fprintf(stderr, "FATAL ERROR: Can't connect to server\n");
			exit();
		}

		/*
		 * Now, just loop around for a while, sending yes or no
		 * at random and receiving responses.
		 */
		for (i = 0; i < NUM_TRY; i++) {
			int yes, data_len;
			char data[COM_BUFFER], reply[COM_BUFFER];
         short request_type1 = QUERY_BY_NAME;
         short request_type2 = QUERY_BY_IP;

			if ((i%10) == 0) {
            memcpy (data, &request_type1, sizeof(short));
				strcpy (&data[sizeof(short)], "snowhite.cis.uoguelph.ca");
				data_len = strlen (data) + 1;
			} else if ((i%10) == 1) {
            memcpy (data, &request_type1, sizeof(short));
				strcpy (&data[sizeof(short)], "dude.cis.uoguelph.ca");
				data_len = strlen (data) + 1;
			} else if ((i%10) == 2) {
            memcpy (data, &request_type1, sizeof(short));
				strcpy (&data[sizeof(short)], "general.uoguelph.ca");
				data_len = strlen (data) + 1;
			} else if ((i%10) == 3) {
            memcpy (data, &request_type1, sizeof(short));
				strcpy (&data[sizeof(short)], "www.bmw32.com");
				data_len = strlen (data) + 1;
			} else if ((i%10) == 4) {
            memcpy (data, &request_type1, sizeof(short));
				strcpy (&data[sizeof(short)], "www.audi.com");
				data_len = strlen (data) + 1;
			} else if ((i%10) == 5) {
            memcpy (data, &request_type2, sizeof(short));
				strcpy (&data[sizeof(short)], "131.104.48.1");
				data_len = strlen (data) + 1;
			} else if ((i%10) == 6) {
            memcpy (data, &request_type2, sizeof(short));
				strcpy (&data[sizeof(short)], "131.104.48.6");
				data_len = strlen (data) + 1;
			} else if ((i%10) == 7) {
            memcpy (data, &request_type2, sizeof(short));
				strcpy (&data[sizeof(short)], "131.104.96.81");
				data_len = strlen (data) + 1;
			} else if ((i%10) == 8) {
            memcpy (data, &request_type2, sizeof(short));
				strcpy (&data[sizeof(short)], "195.27.218.10");
				data_len = strlen (data) + 1;
			} else if ((i%10) == 9) {
            memcpy (data, &request_type2, sizeof(short));
				strcpy (&data[sizeof(short)], "143.164.247.8");
				data_len = strlen (data) + 1;
			}

			/*
			 * Send the data to the server.
			 */
			if (send(client_socket, data, data_len, 0) < 0) {
				fprintf(stderr, "FATAL ERROR: Client send failed\n");
				exit();
			}

			/*
			 * and get reply.
			 * For TCP there are no record boundaries, so we
			 * must loop around until we have a reply, indicated
			 * by a null byte.
			 */
			reply_size = 0;
			do {
				if (recv(client_socket,
				    &reply[reply_size], 1, 0) != 1) {
					fprintf(stderr, "FATAL ERROR: Client rcv err\n");
					exit();
				}
				reply_size++;
				if (reply_size >= COM_BUFFER) {
					fprintf(stderr, "FATAL ERROR: EEK, pkt big\n");
					exit();
				}
			} while (reply[reply_size - 1] != '\0');

#ifdef DEBUG
         if (strcmp (reply, "error") == 0) 
         {
            fprintf (stderr, "Server could not find %s!!!\n", &data[2]);
         } else {
            struct in_addr temp_address;
            memcpy ( &(temp_address.s_addr), reply, sizeof(int));
			   fprintf(stderr, "client reply l=%d ip=%s name=%s\n",
				   reply_size, inet_ntoa (temp_address), &reply[4]);
         }
#endif
		}

		/*
		 * And send an "X" to indicate the client is done.
		 */
		if (send(client_socket, "X", 2, 0) < 0)
			fprintf(stderr, "FATAL ERROR: Client terminate failed\n");
		exit();
	    }  // the end of the for loop

}
