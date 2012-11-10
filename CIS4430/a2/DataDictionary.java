/**
 * CIS*4430 Information Organization and Retrieval
 * Assignment #2, due: Thursday, March 22, 2001
 * Michael Bochenek [ID: 0041056]
 * email: mboche01@uoguelph.ca
 */

import java.util.Vector;


public class DataDictionary
{
	private Vector tables;

	public DataDictionary ()
	{
		tables = new Vector (10);
	}


	public void resetTmpNames()
	{
		for (int i = 0; i < tables.size(); i++)
		{
			TableDef t = (TableDef) tables.elementAt (i);

			t.setTmpName (t.getName());
			t.setJoinName (t.getName());
		}
	}

	public void addTable (String n, Vector a)
	{
		TableDef t = new TableDef ();
		t.setName (n);

		if (tableFound (n))
		{
			Main.invalidCommand ("Table " + n + " already exists in data dictionary (E29).");
			return;
		}

		for (int i = 0; i < a.size(); i++)
		{
			if (attribFound ((String) a.elementAt (i)) || t.attribFound ((String) a.elementAt (i)))
			{
				Main.invalidCommand ("Attribute with the name " + a.elementAt (i) + " already exists in the data dictionary (E01).");
				return;
			}
			else
			{
				t.addAttribute ((String) a.elementAt (i));
			}
		}

		tables.add (t);
	}


	public void printDebug ()
	{
		for (int i = 0; i < tables.size(); i++)
		{
			((TableDef) tables.elementAt (i)).printDebug();
		} 
	}

	public boolean tableFound (String s)
	{
		for (int i = 0; i < tables.size(); i++)
		{
			TableDef t = ((TableDef) tables.elementAt (i));
			if (t.getName().equals (s))
			{
				return true;
			}
		}
		return false;
	}


	public boolean attribFound (String a)
	{
		for (int i = 0; i < tables.size(); i++)
		{
			TableDef t = ((TableDef) tables.elementAt (i));
			if (t.attribFound (a))
			{
				return true;
			}
		}
		return false;
	}


	public TableDef findTable (String a)
	{
		for (int i = 0; i < tables.size(); i++)
		{
			TableDef t = ((TableDef) tables.elementAt (i));
			if (t.attribFound (a))
			{
				return t;
			}
		}
		return null;
	}


	public TableDef getTable (int i)
	{
		return (TableDef) tables.elementAt (i);
	}

	public TableDef getTable (String s)
	{
		for (int i = 0; i < tables.size(); i++)
		{
			TableDef t = ((TableDef) tables.elementAt (i));
			if (t.getName().equals (s))
			{
				return t;
			}
		}
		return null;
	}


	public int findTableIndex (String s)
	{
		for (int i = 0; i < tables.size(); i++)
		{
			TableDef t = ((TableDef) tables.elementAt (i));
			if (t.attribFound(s))
			{
				return i;
			}
		}
		return -1;
	}


	public int getTableIndex (String s)
	{
		for (int i = 0; i < tables.size(); i++)
		{
			TableDef t = ((TableDef) tables.elementAt (i));
			if (t.getName().equals (s))
			{
				return i;
			}
		}
		return -1;
	}


}


