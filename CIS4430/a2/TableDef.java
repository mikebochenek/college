/**
 * CIS*4430 Information Organization and Retrieval
 * Assignment #2, due: Thursday, March 22, 2001
 * Michael Bochenek [ID: 0041056]
 * email: mboche01@uoguelph.ca
 */

import java.util.Vector;


public class TableDef
{
	private String name;
	private Vector attributes;
	private String tmpName;
	private String joinName;


	public TableDef ()
	{
		attributes = new Vector(5);
	}


	public void setName (String n)
	{
		name = new String (n);
		tmpName = new String (n);
		joinName = new String (n);
	}

	public String getName ()
	{
		return name;
	}

	public void setTmpName (String s)
	{
		tmpName = new String (s);
	}

	public String getTmpName ()
	{
		return tmpName;
	}

	public void setJoinName (String s)
	{
		joinName = new String (s);
	}

	public String getJoinName ()
	{
		return joinName;
	}

	public void addAttribute (String a)
	{
		attributes.add (a);
	}

	public String getAttribute (int n)
	{
		try
		{
			return (String) attributes.elementAt (n);
		}
		catch (Exception e)
		{
			return null;
		}
	}


	public void printDebug ()
	{
		System.out.println ("name=" + name);
		for (int i = 0; i < attributes.size(); i++)
		{
			System.out.println ("attribute#" + i + "=" + attributes.elementAt(i));
		}
	}

	public boolean attribFound (String a)
	{
		for (int i = 0; i < attributes.size(); i++)
		{
			if (a.equals ( (String) attributes.elementAt(i) ))
			{
				return true;
			}
		}
		return false;
	}

}


