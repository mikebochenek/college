/**
 * Michael Bochenek (ID: 0041056)
 * CIS*4400 Distributed Information Systems Architectures
 * Assignment #1.  Friday, January 26, 2001
 */

import java.util.Date;

public class ProxyFile
{
	private String filename;
	private int filesize = 0;
	private int frequency = 0;
	private long accesstime = 0;
	public boolean locked = false;

	public ProxyFile (String name, int size, int freq, long access)
	{
		filename = new String (name);
		filesize = size;
		frequency = freq;
		accesstime = access;
		locked = false;
	}


	public ProxyFile (String name, int size)
	{
		filename = new String (name);
		filesize = size;
		frequency = 0;
		accesstime = 0;
	}


	public String getFilename()
	{
		return filename;
	}


	public int getFilesize()
	{
		return filesize;
	}


	public int getFrequency()
	{
		return frequency;
	}


	public long getAccesstime()
	{
		return accesstime;
	}


	public void access()
	{
		Date now = new Date();
		accesstime = now.getTime();
		frequency++;
	}

}
