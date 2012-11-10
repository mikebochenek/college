/**
 * Michael Bochenek (ID: 0041056)
 * CIS*4400 Distributed Information Systems Architectures
 * Assignment #1.  Friday, January 26, 2001
 */

import java.io.*;

class FileIO 
{
	public static final int BUFFER_SIZE = 10000;
	byte[] buffer = new byte[BUFFER_SIZE];
	RandomAccessFile fd;


	public FileIO() 
	{
	}


	public void openWriteFile (String filename)
	{
		// open file for writing
		try
		{
			fd = new RandomAccessFile (filename, "rw");
		}
		catch (Exception e)
		{
			System.out.println ("Could not open the writeFile");
		}
	}


	public void readFileFromSocket (BufferedReader br, String filename, int filesize) throws Exception
	{
		int bytesWritten = 0;
		boolean continueReading = true;
		//System.out.println ("readFileFromSocket");

		openWriteFile (filename);
	
		// receive file from socket
		do
		{
			int bytesReceived = 0;
			do
			{
				try
				{
					byte tempByte = (byte) br.read();
					buffer[bytesReceived] = tempByte;
				}
				catch (Exception e)
				{
					System.out.println ("Could not read from socket.");
					continueReading = false;
				}
				bytesReceived++;
				bytesWritten++;
			} while (bytesReceived < BUFFER_SIZE && bytesWritten < filesize);

			// write to file
			try
			{
				fd.write (buffer, 0, bytesReceived);
			}
			catch (Exception e)
			{
				System.out.println ("Could not write to the file");
			}

			//if (bytesWritten % 10000 == 0)
			//System.out.println ("written " + bytesWritten);

		} while (continueReading == true && bytesWritten < filesize);

		// close file
		fd.setLength (bytesWritten);
		fd.close();
		//System.out.println ("file here also closed.  also all done.");
	}


	public void openReadFile (String filename)
	{
		// open file for reading
		try
		{
			fd = new RandomAccessFile (filename, "r");
		}
		catch (Exception e)
		{
			System.out.println ("Could not open the readFile");
		}
	}
	

	public void writeFileToSocket (DataOutputStream out, String filename, int filesize) throws Exception
	{
		//System.out.println ("writeFileToSocket"); 
		int bytesRead = 0;
		int totalBytesRead = 0;
		boolean continueFlag = true;
		openReadFile (filename);

		do
		{
			// read from file
			bytesRead = 0;

			do
			{
				try
				{
					buffer[bytesRead] = fd.readByte ();
				}
				catch (IOException e)
				{
					//System.out.println ("Could not read from the file.");
					continueFlag = false;
				}
				bytesRead++;
				totalBytesRead++;
			} while (bytesRead < BUFFER_SIZE && totalBytesRead < filesize);

			// send file to socket
			if (continueFlag == true)
			{
				try
				{
					out.write (buffer, 0, bytesRead);
				}
				catch (Exception e)
				{
					System.out.println ("Could not write to socket.");
					System.out.println (bytesRead);
					System.exit(0);
				}
			}

			//if (totalBytesRead % 10000 == 0)
			//System.out.println ("read " + totalBytesRead);

		} while (bytesRead != 0 && continueFlag == true);

		// close file
		fd.close();
		//System.out.println ("File closed. all done!");
	}
	
}  


