/**
 * Michael Bochenek (ID: 0041056)
 * CIS*4400 Distributed Information Systems Architectures
 * Assignment #1.  Friday, January 26, 2001
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

class Connection extends Thread 
{
	private Socket clS = null;
	private BufferedReader isS = null;
	private DataOutputStream osS = null;

	private Socket client;
	private BufferedReader is;
	private DataOutputStream os;

	public static final int INVALID_FILENAME = -1;
	public static final int ONE_SECOND = 1000;
	public static final int CHECK_PASSED = 1001;
	public static final int CHECK_FAILED = 1002;

	private ProxyFile[] fileList = null;
	private int fileRequests = 0;

	private ProxyFile[] tempFileList = null; 
	private int currentTempFile = 0;
	public static final int TEMP_FILES = 5;
	private int proxy_type = 0;

	private String remoteMachine = null;
	private int remotePort = 0;

	private int commandType;


	public Connection(Socket s, ProxyFile[] list, int pr_type, Socket toMainFS, int rPort, String rMachine, ProxyFile[] pList) 
	{ 
		client = s;
		fileList = list;
		proxy_type = pr_type;
		clS = toMainFS;
		remoteMachine = rMachine;
		remotePort = rPort;
		tempFileList = pList;

		try 
		{
			is = new BufferedReader(new InputStreamReader(client.getInputStream()));
			os = new DataOutputStream(client.getOutputStream());
		} 
		catch (IOException e) 
		{
			try 
			{
				client.close();
			} 
			catch (IOException ex) 
			{
				System.out.println("Error while getting socket streams.."+ex);
			}
			return;
		}

		if (proxy_type != 0)
		{

			setupSocketsNow();

			int count = 0;
			try 
			{	    
				osS.writeBytes ("ls\n");
			} 
			catch (Exception ex) 
			{
				System.out.println("error writing to server..."+ex);
			}	

			String reply = null;
			try
			{
				reply = isS.readLine ();

				StringTokenizer tokenizer = new StringTokenizer (new String (reply), "\"");
				while (tokenizer.hasMoreTokens())
				{
					String str = new String (tokenizer.nextToken());
					int size = Integer.parseInt (tokenizer.nextToken());
					count++;
				}
				fileList = new ProxyFile[count];

				tokenizer = new StringTokenizer (new String (reply), "\"");
				int k = 0;
				while (tokenizer.hasMoreTokens())
				{
					String str = new String (tokenizer.nextToken());
					int size = Integer.parseInt (tokenizer.nextToken());
					fileList[k] = new ProxyFile (str, size);
					k++;
				}
			}
			catch (Exception e)
			{
				System.out.println ("My big error here!");
			}	
		}	

		this.start(); // Thread starts here...this start() will call run()
	}


	public void run() 
	{
		String command = null;

		try 
		{
			// get filename request here
			command = is.readLine ();
			//System.out.println ("Server received command: " + command);
			commandType = CommandType.parseCommand (command);
		} 
		catch(Exception ioe) 
		{
		}


		switch (commandType)
		{
			case CommandType.LS_COMMAND:
			{
				try
				{
					os.writeBytes (createFileList() + "\n");
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
					createFileList();
					os.writeBytes (Integer.toString (fileList.length) + "\n");
				}
				catch (Exception e)
				{
				}
				break;
			}

			case CommandType.GET_COMMAND:
			{
				//System.out.println ("Starting to get the file....");

				incrementFileRequests();

				String requestFilename = command.substring (4);

				if (proxy_type != 0)
				{
					int check = checkCache (requestFilename);
					if (check != CHECK_PASSED)
					{
						getFileFromMainFS (requestFilename, check);
					}
				}

				int filesize = getFileSizeByFilename (requestFilename);
				try
				{
					os.writeBytes (Integer.toString (filesize) + "\n");
					//System.out.println ("Server send filesize: " + filesize);
				}
				catch (Exception e)
				{
				}

				if (filesize != INVALID_FILENAME)
				{
					try 
					{
						FileIO fio = new FileIO();
						//if (checkIfFileLocked(requestFilename) != true)
						{
							//lockFile (requestFilename);
							fio.writeFileToSocket (os, requestFilename, filesize);
							//unlockFile (requestFilename);
						}
					} 
					catch(Exception e) 
					{
						e.printStackTrace();
					}
				}
						
				decrementFileRequests();

				break;
			}

			case CommandType.QUIT_COMMAND:
			{
				break;
			}

			case CommandType.INVALID_COMMAND:
			default:
			{
				break;
			}

		}

		if (proxy_type != 0)
		{
			// close input stream, output stream and connection
			try 
			{
				isS.close();
				osS.close();
				clS.close();
			} 
			catch (IOException x) 
			{
				System.out.println("Error writing...."+x);
			}
		}
	}


	private String createFileList ()
	{
		String retValue = new String();
		for (int i = 0; i < fileList.length; i++)
		{
			retValue += fileList[i].getFilename();
			retValue += "\"";
			retValue += fileList[i].getFilesize();
			retValue += "\"";
		}
		return retValue;
	}


	private int getFileSizeByFilename (String filename)
	{
		int retValue = INVALID_FILENAME;
		for (int i = 0; i < fileList.length; i++)
		{
			if (filename.equals (fileList[i].getFilename()))
			{
				retValue = fileList[i].getFilesize();
				return retValue;
			}
		}
		return retValue;
	}


	private synchronized int checkCache (String requestFilename)
	{
		for (int i = 0; i < tempFileList.length; i++)
		{
			if (tempFileList[i] != null)
			{
				currentTempFile++;
			}
		}

		if (getFileSizeByFilename (requestFilename) == -1)
		{
			// file not found
			return CHECK_PASSED;
		}

		for (int i = 0; i < currentTempFile; i++)
		{
			if (requestFilename.equals (tempFileList[i].getFilename()))
			{
				tempFileList[i].access();
				return CHECK_PASSED;
			}
		}


		int replacementIndex = 0;
		if (currentTempFile < TEMP_FILES)
		{
			replacementIndex = currentTempFile;
			currentTempFile++;
			//System.out.println ("replacementIndex is:  " + replacementIndex + " " + currentTempFile);
		} 
		else
		{
			if (proxy_type == 1)
			{
// fuck.
				replacementIndex = 0;
				long firstaccess = tempFileList[0].getAccesstime();
				for (int i = 0; i < tempFileList.length; i++)
				{
					if (firstaccess > tempFileList[i].getAccesstime())
					{
						replacementIndex = i;
						firstaccess = tempFileList[i].getAccesstime();
					}
				}
			}
			if (proxy_type == 2)
			{
// fuck.
				replacementIndex = 0;
				long firstaccess = tempFileList[0].getAccesstime();
				int frequency = tempFileList[0].getFrequency();;
				for (int i = 0; i < tempFileList.length; i++)
				{
					if (frequency > tempFileList[i].getFrequency())
					{
						replacementIndex = i;
						frequency = tempFileList[i].getFrequency();
						firstaccess = tempFileList[i].getAccesstime();
					}
					else if (frequency == tempFileList[i].getFrequency())
					{
						if (firstaccess > tempFileList[i].getAccesstime())
						{
							replacementIndex = i;
							frequency = tempFileList[i].getFrequency();
							firstaccess = tempFileList[i].getAccesstime();
						} 
					}
				}
	
			}

			System.out.println ("Proxy: deleting " + tempFileList[replacementIndex].getFilename());
			System.out.flush();
			File oldFile = new File (tempFileList[replacementIndex].getFilename());
			oldFile.delete();
		}

		return replacementIndex;
	}

	
	private void getFileFromMainFS (String requestFilename, int replacementIndex)
	{
		if (proxy_type == 1 || proxy_type == 2)
		{
			System.out.println ("Proxy: getting file " + requestFilename + " from MainFS");
			System.out.flush();

			int reqFS = getFileSizeByFilename (requestFilename);

			tempFileList[replacementIndex] = new ProxyFile(requestFilename, reqFS);

			tempFileList[replacementIndex].access();

			for (int j = 0; j < tempFileList.length && j < currentTempFile; j++)
			{
				//System.out.print ("[" + tempFileList[j].getFilename() + " : " + tempFileList[j].getAccesstime() + " : " + tempFileList[j].getFrequency() + "]");
			}
			//System.out.println();

			setupSocketsNow();

			try 
			{	    
				// send request for file here...
				osS.writeBytes ("get " + requestFilename + "\n");
			} 
			catch (Exception ex) 
			{
				System.out.println("error writing to server..."+ex);
			}	

			int filesize = 0;
			boolean some_error = false;

			try
			{
				String reply = isS.readLine ();
				filesize = Integer.parseInt(reply);
				//System.out.println ("Proxy receives filesize: " + filesize);
			}
			catch (Exception e)
			{
				some_error = true;
			}	
	
			if (filesize != -1)
			{
				// receive results 
				int result[] = new int[10];
				try 
				{
					FileIO fio = new FileIO();
					//if (checkIfFileLocked (requestFilename) != true)
					{
						//lockFile (requestFilename);
						fio.readFileFromSocket (isS, requestFilename, filesize);
						//unlockFile (requestFilename);
					}	
				} 
				catch(Exception e) 
				{
					some_error = true;
					e.printStackTrace();
				}
			}
			else
			{
				System.out.println ("Error: file not found");
			}

			if (some_error == true)
			{
				System.out.println ("ERROR: File transfer INCOMPLETE!");
			}
			else
			{
				//System.out.println ("File transfer completed.");
			}
		}
	}


	private void setupSocketsNow ()
	{
		try 
		{
			clS = new Socket(remoteMachine, remotePort);
		} 
		catch(UnknownHostException e1) 
		{
			System.out.println("Unknown Host: "+e1);
		} 
		catch (IOException e2) 
		{
			System.out.println("Erorr io: "+e2);
		}
		catch (Exception other)
		{
			System.out.println ("error in socket setup for proxy");
		}

		try 
		{
			isS = new BufferedReader(new InputStreamReader(clS.getInputStream()));
			osS = new DataOutputStream(clS.getOutputStream());
		} 
		catch(UnknownHostException e1) 
		{
			System.out.println("Unknown Host: "+e1);
		} 
		catch (IOException e2) 
		{
			System.out.println("Erorr io: "+e2);
		}
	}


	private synchronized void incrementFileRequests ()
	{
		fileRequests++;
		
		while (fileRequests >= TEMP_FILES)
		{
			try
			{
				sleep (ONE_SECOND);
			}
			catch (Exception e)
			{
			}
		} 
	}


	private synchronized void decrementFileRequests ()
	{
		fileRequests--;
	}


	private synchronized void lockFile (String name)
	{
		for (int i = 0; i < tempFileList.length; i++)
		{
			if (tempFileList[i] != null)
			{
				if (name.equals (tempFileList[i].getFilename()))
				{
					tempFileList[i].locked = true;
				}
			}
		}
	}


	private synchronized void unlockFile (String name)
	{
		for (int i = 0; i < tempFileList.length; i++)
		{
			if (tempFileList[i] != null)
			{
				if (name.equals (tempFileList[i].getFilename()))
				{
					tempFileList[i].locked = false;
				}
			}
		}
	}


	private synchronized boolean checkIfFileLocked (String name)
	{
		do
		{
			for (int i = 0; i < tempFileList.length; i++)
			{
				if (tempFileList[i] != null)
				{
					if (name.equals (tempFileList[i].getFilename()))
					{
						if (tempFileList[i].locked == true)
						{
							break;
						}
						return tempFileList[i].locked;
					}
				}
			}
			try
			{
				sleep (ONE_SECOND);
			}
			catch (Exception e)
			{
			}
		} while (true);
	}

}


