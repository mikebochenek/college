/*
 ********************************************************************
 ************************* S E R V E R ******************************
 ********************************************************************
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
#define  CACHE_SIZE 10 
#define  QUERY_BY_IP 4441
#define  QUERY_BY_NAME 4442

struct cache_entry {
   unsigned long int ip_address;
   char * domain_name;
};

struct cache_entry cache[CACHE_SIZE];
int cache_ptr;

char * get_host_name(char * ip_address);
char * get_ip_address(char * host_name);

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

   cache_ptr = 0;
   for (i = 0; i < CACHE_SIZE; i++)
   {
      cache[i].ip_address = 0;
      cache[i].domain_name = NULL;
   }

	/*
	 * Create the TCP sockets for the server.
	 */
	server_socket = socket(AF_INET, SOCK_STREAM, 0);
	if (server_socket < 0) {
		fprintf(stderr, "FATAL ERROR: Server can't create socket\n");
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
		fprintf(stderr, "FATAL ERROR: Server, can't bind socket address\n");
		exit();
	}

	/*
	 * And listen on the well known port for connection requests.
	 */
	if (listen(server_socket, 1) < 0) {
#ifdef DEBUG
		fprintf(stderr, "Listen for connection...\n");
#endif
		exit();
	}


	/*
	 * The server. It must do a select() on the master socket for
	 * new connections and also any sub_sockets for existing connections.
	 * It terminates when NUM_CLIENT connections have been severed.
	 */
	num_connect = 0;
	subnum = 0;
	FD_ZERO(&readfds);
	FD_SET(server_socket, &readfds);
	nfds = server_socket + 1;
	do {
		checkfds = readfds;
		nfnd = select(nfds, &checkfds, (fd_set *)0, (fd_set *)0,
			(struct timeval *)0);
#ifdef DEBUG
		fprintf(stderr, "After select()...\n");
#endif
		if (nfnd > 0) {
		    if (FD_ISSET(server_socket, &checkfds)) {
			/*
			 * Accept a connection from a client.
			 */
			if ((subsock[subnum] = accept(server_socket,0,0)) < 0){
				fprintf(stderr, "FATAL ERROR: Server accept failed\n");
				exit();
			}
			FD_SET(subsock[subnum], &readfds);
			if (subsock[subnum] + 1 > nfds)
				nfds = subsock[subnum] + 1;
			subnum++;
			num_connect++;
#ifdef DEBUG
			fprintf(stderr, "Got a new connection !\n");
#endif
		    }
		    for (i = 0; i < subnum; i++)
			if (FD_ISSET(subsock[i], &checkfds))
			    if (do_rpc(subsock[i])) {
				FD_CLR(subsock[i], &readfds);
				num_connect--;
			    }
		}
	} while (num_connect > 0);
	//} while (1);
#ifdef DEBUG
	fprintf(stderr, "EXIT: Server done\n");
#endif
}

/*
 * Do an RPC for the request on the argument socket.
 * Return 0 if ok, 1 if "X" is received.
 */
int
do_rpc(sock)
	int sock;
{
	int request_size, response_len;
	char *response, req[COM_BUFFER];
   short request_type1 = QUERY_BY_NAME, request_type2 = QUERY_BY_IP;

#ifdef DEBUG
	fprintf(stderr, "Do RPC for sock=%d\n", sock);
#endif
	request_size = 0;
	do {
	    if (recv(sock, &req[request_size], 1, 0) != 1) {
		fprintf(stderr, "FATAL ERROR: Server recv failed\n");
		exit();
	    }
	    request_size++;
	    if (request_size >= COM_BUFFER) {
		fprintf(stderr, "FATAL ERROR: Server request too big\n");
		exit();
	    }
	} while (req[request_size - 1] != '\0');
#ifdef DEBUG
	fprintf(stderr, "server received string: len=%d s=%s\n", request_size, req);
#endif
	if (!memcmp(req, &request_type1, sizeof(short))) {
      response = get_ip_address (&req[2]);
		response_len = strlen (response) + 1;
	} else if (!memcmp(req, &request_type2, sizeof(short))) {
      response = get_host_name (&req[2]);
		response_len = strlen (response) + 1;
	} else {
#ifdef DEBUG
		fprintf(stderr, "Connection done for socket=%d\n", sock);
#endif
		return (1);
	}
	send(sock, response, response_len, 0);
	return (0);
}


char * get_host_name(char * ip_address)
{
	struct hostent *hp;
	struct in_addr *sp, sad, tempA;
   char * results;
   int i;   
   unsigned long int temp_result = 0;

   results = (char *) malloc (6 * sizeof (char));
   strcpy (results, "error");

   inet_aton (ip_address, &tempA);

   //
   // look up in cache
   //
   for (i = 0; i < CACHE_SIZE; i++)
   {
      if (cache[i].ip_address != 0)
      {
         if (cache[i].ip_address == tempA.s_addr)
         {
            temp_result = cache[i].ip_address;
            break;
         }
      }
   }
   //
   // if already in cache, just return it.
   //
   if (temp_result != 0)
   {
      free (results);
      results = (char *) malloc ((1 * sizeof (int)) + 1 + strlen (cache[i].domain_name));
      memcpy (results, &temp_result, sizeof(int));
      strcpy (&results[4], cache[i].domain_name);
      return results;
   }

   //
   // if not in cache, look it up using standard functions.
   //

	/*
	 * Now, get an address by number.
	 */
	sad.s_addr = inet_addr(ip_address);
	if ((hp = gethostbyaddr((char *)&sad, sizeof (sad), AF_INET)) == NULL){
#ifdef DEBUG
		fprintf(stderr, "Can't resolve %s\n", ip_address);
#endif
      return results;
	}
	if (hp->h_addrtype != AF_INET || hp->h_length != sizeof(struct in_addr)){
#ifdef DEBUG
		fprintf(stderr, "Bad address\n");
#endif
      return results;
	}

	/*
	 * The address(es) are 32 bit numbers in Network Byte Order
	 * (Big Endian), pointed to by h_addr_list. The routine
	 * inet_ntoa() prints out the 32 bit IP address in "." notation.
	 */
	while (*hp->h_addr_list) {
		sp = (struct in_addr *)(*hp->h_addr_list++);
	}

   //
   // if not there, add it to cache
   //
   if (cache[cache_ptr].ip_address != 0)
   {
      free (cache[cache_ptr].domain_name);
      cache[cache_ptr].ip_address = 0;
   }
   cache[cache_ptr].domain_name = (char *) malloc (1 + sizeof (char) * strlen (hp->h_name));
   strcpy (cache[cache_ptr].domain_name, hp->h_name);
   cache[cache_ptr].ip_address = sp->s_addr;
   cache_ptr = (cache_ptr + 1) % CACHE_SIZE; 


   free (results);
   results = (char *) malloc ((sizeof (int)) + 1 + strlen (hp->h_name));
   memcpy (results, &(sp->s_addr), sizeof(int));
   strcpy (&results[4], hp->h_name);
   return results;
}


char * get_ip_address(char * host_name)
{
	struct hostent *hp;
	struct in_addr *sp, sad;
   char * results;
   int i;   
   unsigned long int temp_result = 0;

   results = (char *) malloc (6 * sizeof (char));
   strcpy (results, "error");
   //
   // look up in cache
   //
   for (i = 0; i < CACHE_SIZE; i++)
   {
      if (cache[i].ip_address != 0)
      {
         if (strcmp (cache[i].domain_name, host_name) == 0)
         {
#ifdef DEBUG
      		fprintf(stderr, "Found in cache at position %d\n", i);
#endif
            temp_result = cache[i].ip_address;
            break;
         }
      }
   }
   //
   // if already in cache, just return it.
   //
   if (temp_result != 0)
   {
      free (results);
      results = (char *) malloc ((1 * sizeof (int)) + 1 + strlen (host_name));
      memcpy (results, &temp_result, sizeof(int));
      strcpy (&results[4], host_name);
#ifdef DEBUG
		fprintf(stderr, "Returning straight from cache (%s)\n", results);
#endif
      return results;
   }

   //
   // if not in cache, look it up using standard functions.
   //

	/*
	 * First, get the address by name.
	 */
#ifdef DEBUG
   fprintf (stderr, "Doing _standard_ lookup for %s\n", host_name);
#endif
	if ((hp = gethostbyname(host_name)) == NULL) {
#ifdef DEBUG
		fprintf(stderr, "Can't resolve %s\n", host_name);
#endif
      return results;
	}
	if (hp->h_addrtype != AF_INET || hp->h_length != sizeof(struct in_addr)){
#ifdef DEBUG
		fprintf(stderr, "Bad address\n");
#endif
      return results;
	}
	/*
	 * The address(es) are 32 bit numbers in Network Byte Order
	 * (Big Endian), pointed to by h_addr_list. The routine
	 * inet_ntoa() prints out the 32 bit IP address in "." notation.
	 */
	while (*hp->h_addr_list) {
		sp = (struct in_addr *)(*hp->h_addr_list++);
	}


   //
   // if not there, add it to cache
   //
#ifdef DEBUG
	fprintf(stderr, "Adding entry to cache\n");
#endif
   if (cache[cache_ptr].ip_address != 0)
   {
      free (cache[cache_ptr].domain_name);
      cache[cache_ptr].ip_address = 0;
   }
   cache[cache_ptr].domain_name = (char *) malloc (1 + sizeof (char) * strlen (host_name));
   strcpy (cache[cache_ptr].domain_name, host_name);
   cache[cache_ptr].ip_address = sp->s_addr;
   cache_ptr = (cache_ptr + 1) % CACHE_SIZE; 

   free (results);
   results = (char *) malloc ((1 * sizeof (int)) + 1 + strlen (host_name));
   memcpy (results, &(sp->s_addr), sizeof(int));
   strcpy (&results[4], host_name);
#ifdef DEBUG
	fprintf(stderr, "Returning using old method (%s)\n", results);
#endif
   return results;
}
