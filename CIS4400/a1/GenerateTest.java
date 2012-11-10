/**
 * Michael Bochenek (ID: 0041056)
 * CIS*4400 Distributed Information Systems Architectures
 * Assignment #1.  Friday, January 26, 2001
 */

import java.io.*;
import java.util.Random;

class GenerateTest 
{
	private static byte[] buffer = new byte[1024];
	private static RandomAccessFile fd;

	public static void main(String argv[])
	{
		int times = 0, var1 = 0, var2 = 0;
		String remoteMachine = null;

		try
		{
			times = Integer.parseInt (argv[0]);
			var1 = Integer.parseInt(argv[1]);
			var2 = Integer.parseInt(argv[2]);
			remoteMachine = new String (argv[3]);
		}
		catch (Exception e)
		{
			System.out.println ("times, maxFileNumber, remotePort, remoteMachine");
			System.exit(-1);
		}


		int bytesWritten = 0;

		openWriteFile ("test.sh");
	
		Random r = new Random();

		// receive file from socket
		for (int i = 0; i < times; i++)
		{
			int digit = r.nextInt (var1) + 1;

			String tempS = "";
			if (digit < 10) tempS += "00"; 
			else if (digit < 100) tempS += "0"; 
			else if (digit < 1000) tempS += ""; 

			String s = new String ("java FClientCommandLine " + remoteMachine + " " + var2 + " \"get test0" + tempS + digit + "\"\n"); 

			try
			{
				fd.writeBytes (s); 
				bytesWritten += s.length();
			}
			catch (Exception e)
			{
				System.out.println ("Could not write to the file");
			}

		}

		try
		{
			fd.setLength (bytesWritten);
			fd.close();
		}
		catch (Exception e)
		{
		}
	}
	

	public static void openWriteFile (String filename)
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

}  


