/**
 * CIS*4430 Information Organization and Retrieval
 * Assignment #2, due: Thursday, March 22, 2001
 * Michael Bochenek [ID: 0041056]
 * email: mboche01@uoguelph.ca
 */

import java.util.Vector;


public class SelectStructure
{
	private Vector select;
	private Vector from;
	private Vector where;
	private DataDictionary dict;
	private boolean validStruct = true;

	public SelectStructure(DataDictionary d)
	{
		dict = d;
		select = new Vector(5);
		from = new Vector(5);
		where = new Vector(5);
	}

	public boolean isValid ()
	{
		boolean nonEmpty = true;

		if (select.size() == 0)
		{
			nonEmpty = false;
		}
		if (from.size() == 0)
		{
			nonEmpty = false;
		}
		if (where.size() == 0)
		{
			nonEmpty = false;
		}

		return validStruct && nonEmpty;
	}

	public void addSelect (String s)
	{
		if (dict.attribFound (s))
		{
			select.add (s);
		}
		else
		{
			validStruct = false;
			Main.invalidCommand ("Attribute " + s + " not found in data dictionary (E23).");
		}
	}

	public void addFrom (String s)
	{
		if (dict.tableFound (s))
		{
			from.add (s);
		}
		else
		{
			validStruct = false;
			Main.invalidCommand ("Table " + s + " not found in data dictionary (E24).");
		}
	}

	public void addWhere (String s)
	{
		Predicate p = new Predicate (dict, s);

		if (p.isValid() == false)
		{
			validStruct = false;
			Main.invalidCommand ("Invalid predicate (E25).");
		}
		else
		{
			where.add (p);
		}
	}


	public Vector getSelect()
	{
		return select;
	}


	public Vector getFrom()
	{
		return from;
	}


	public Vector getWhere()
	{
		return where;
	}
}

