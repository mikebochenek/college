/**
 * Michael Bochenek (ID: 0041056)
 * CIS*4400 Distributed Information Systems Architectures
 * Assignment #1.  Friday, January 26, 2001
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

public class Proxy
{
	private int proxy_type = 0;
	private ProxyFile fileList[] = new ProxyFile[5];
	Socket clS = null;


	public Proxy (String argv[], int pr_type)
	{
		proxy_type = pr_type;

		int port = 0, remote_port = 0;
		String remote_machine = null;

		try
		{
			port = Integer.parseInt (argv[0]);
			remote_machine = argv[1];
			remote_port = Integer.parseInt (argv[2]);
		}
		catch (Exception e)
		{
			System.out.println ("Incorrect usage!  Correct invocation is:\n\njava ProxyX local_port# remote_machine_name remote_port#\n   where local_port# is the port number on which to listen on for clients\n   where remote_machine_name is name of the machine that hosts MainFS\n   where remote_port# is the port number that MainFS is listening on\n");
			System.exit(-1);
		}

		fileList[0] = null;
		fileList[1] = null;
		fileList[2] = null;
		fileList[3] = null;
		fileList[4] = null;

		// should this whole try be even here. duplicate in connectin.
		try 
		{
			clS = new Socket(remote_machine, remote_port);
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

		Server s = new Server(port, proxy_type, clS, remote_port, remote_machine, fileList);
	}

}
