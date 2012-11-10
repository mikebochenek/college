/**
 * Michael Bochenek (ID: 0041056)
 * CIS*4400 Distributed Information Systems Architectures
 * Assignment #1.  Friday, January 26, 2001
 */

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class FClient 
{    
	public final static int REMOTE_PORT = 3333;
	private static int commandType;
	private static boolean continueLooping = true;


	public static void main(String argv[]) throws Exception 
	{
		Socket cl = null, cl2 = null;
		BufferedReader is = null;
		DataOutputStream os = null;

		String proxyName = null;
		int proxyPort = 0;

		try
		{
			proxyName = new String (argv[0]);
			proxyPort = Integer.parseInt(argv[1]);
		}
		catch (Exception e)
		{
			System.out.println ("Incorrect usage!  Correct invocation is:\n\njava FClient remote_machine_name remote_port#\n   where remote_machine_name is the name of the machine that hosts the proxy\n   where remote_port# is the port number that the proxy is listening on\n"); 
			System.exit(-1);
		}

		do
		{
			System.out.print ("\n- to request the number of files that reside on the server type 'count'\n- to request the names of all the files that reside on the server type 'ls'\n- to request a file by name type 'get filename'\n  (where filename is the name of the file).\n- to quite type 'quit'\nCommand: ");

			BufferedReader stdin = new BufferedReader (new InputStreamReader (System.in));
			String command = stdin.readLine();
			commandType = CommandType.parseCommand (command);
			System.out.println ();

			try 
			{
				cl = new Socket(proxyName, proxyPort);
				is = new BufferedReader(new InputStreamReader(cl.getInputStream()));
				os = new DataOutputStream(cl.getOutputStream());
			} 
			catch(UnknownHostException e1) 
			{
				System.out.println("Unknown Host: "+e1);
			} 
			catch (IOException e2) 
			{
				System.out.println("Erorr io: "+e2);
			}
	
			String requestFilename = null;

			try 
			{	    
				// send request for file here...
				os.writeBytes (command + "\n");
				//System.out.println ("Client sends command: " + command);
			} 
			catch (Exception ex) 
			{
				System.out.println("error writing to server..."+ex);
			}	


			switch (commandType)
			{
				case CommandType.GET_COMMAND:
				{
					int filesize = 0;
					boolean some_error = false;

					requestFilename = command.substring (4);

					try
					{
						String reply = is.readLine ();
						filesize = Integer.parseInt(reply);
						//System.out.println ("Client receives filesize: " + filesize);
					}
					catch (Exception e)
					{
						//some_error = true;
					}	
	
					if (filesize != -1)
					{
						// receive results 
						int result[] = new int[10];
						try 
						{
							FileIO fio = new FileIO();
							fio.readFileFromSocket (is, requestFilename, filesize);
						} 
						catch(Exception e) 
						{
							some_error = true;
							e.printStackTrace();
						}
					}
					else if (some_error == false)
					{
						System.out.println ("Error: file not found");
					}
					else if (some_error == true)
					{
						System.out.println ("ERROR: File transfer INCOMPLETE!");
					}
					else
					{
						System.out.println ("File transfer completed.");
					}

					break;
				}

				case CommandType.LS_COMMAND:
				{
					try
					{
						String reply = is.readLine ();
						StringTokenizer tokenizer = new StringTokenizer (reply, "\"");
						while (tokenizer.hasMoreTokens())
						{
							System.out.println (tokenizer.nextToken());
							tokenizer.nextToken();
						}
					}
					catch (Exception e)
					{
					}	

					break;
				}

				case CommandType.COUNT_COMMAND:
				{
					try
					{
						String reply = is.readLine ();
						int count = Integer.parseInt(reply);
						System.out.println ("There are " + count + " files on the server");
					}
					catch (Exception e)
					{
					}	

					break;
				}

				case CommandType.QUIT_COMMAND:
				{
					continueLooping = false;
					continue;
				}

				case CommandType.INVALID_COMMAND:
				default:
				{
					System.out.println ("Invalid Command");
					break;
				}
			}
		} while (continueLooping == true);

		// close input stream, output stream and connection
		try 
		{
			is.close();
			os.close();
			cl.close();
		} 
		catch (IOException x) 
		{
			System.out.println("Error writing...."+x);
		}
	}
}


