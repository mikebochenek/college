/**
 * Michael Bochenek (ID: 0041056)
 * CIS*4400 Distributed Information Systems Architectures
 * Assignment #1.  Friday, January 26, 2001
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

public class MainFS
{
	// main program
	public static void main(String argv[]) throws IOException 
	{
		int port = 0;

		try
		{
			port = Integer.parseInt (argv[0]);
		}
		catch (Exception e)
		{
			System.out.println ("Incorrect usage!  Correct invocation is:\n\njava MainFS port#\n   where port# is the port number on which to listen on\n");
			System.exit(-1);
		}
		
		Server s = new Server(port, 0, null, 0, null, null);
	}

}


