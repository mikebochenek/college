/**
 * Michael Bochenek (ID: 0041056)
 * CIS*4400 Distributed Information Systems Architectures
 * Assignment #1.  Friday, January 26, 2001
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

public class Server extends Thread 
{
	// The port number on which the server will be listening on
	public static int MATH_PORT;
	protected ServerSocket listen;
	protected ProxyFile[] fileList;
	protected ProxyFile[] proxyList;
	private int proxy_type = 0;
	Socket toMainFS = null;
	String remoteMachine = null;
	int remotePort = 0;


	// constructor.
	public Server(int port, int pr_type, Socket s, int rPort, String rMachine, ProxyFile[] pList) 
	{
		MATH_PORT = port;
		proxy_type = pr_type;
		toMainFS = s;
		remoteMachine = rMachine;
		remotePort = rPort;
		proxyList = pList;

		File currentDirectory = new File(".");
		File[] allFiles = currentDirectory.listFiles();
		fileList = new ProxyFile [allFiles.length];
		for (int i = 0; i < allFiles.length; i++)
		{
			String filename = allFiles[i].getName();
			int filesize = (int) allFiles[i].length();

			fileList[i] = new ProxyFile (filename, filesize);
		}

		try 
		{
			listen = new ServerSocket(MATH_PORT);
		} 
		catch(IOException ex) 
		{
			System.out.println("Exception..."+ex);
		}
		this.start();
	}


	// multi-threading -- create a new connection for each request
	public void run() 
	{
		try 
		{
			while(true) 
			{
				Socket client = listen.accept();
				Connection cc = new Connection(client, fileList, proxy_type, toMainFS, remotePort, remoteMachine, proxyList);
			}
		} 
		catch(IOException e) 
		{
			System.out.println("Exception..."+e);
		}
	}

}

