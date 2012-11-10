/**
 * CIS*4430 Information Organization and Retrieval
 * Assignment #2, due: Thursday, March 22, 2001
 * Michael Bochenek [ID: 0041056]
 * email: mboche01@uoguelph.ca
 */

import java.util.Vector;
import java.io.FileReader;
import java.io.FileDescriptor;


public class Main
{
	private static DataDictionary dict;
	private static String str;
	private FileReader f;

	public static void main (String args[]) 
	{
		Main m = new Main();

	}


	public Main()
	{
		f = new FileReader (FileDescriptor.in);

		dict = new DataDictionary();

		String command = null;

		do
		{
			System.out.print ("$ ");

			command = readCommand ();

			if (command != null)
			{
				if (command.toUpperCase().startsWith ("PRINT"))
				{
					dict.printDebug();
				}
				else if (command.toUpperCase().startsWith ("C"))
				{
					parseCreate (command);
				}
				else if (command.toUpperCase().startsWith ("S"))
				{
					SelectStructure struct = parseSelect (command);

					PlanGenerator pg = new PlanGenerator (dict, struct); 

					pg.generate();

					if (pg.isValid() == false)
					{
						invalidCommand ("The generated plan is incorrect (E02).");
					}
					else
					{
						pg.print();
					}

					pg.resetDataDictionary();
				}
				else
				{
					invalidCommand("You can only use 'select' or 'create' commands (E17).");
				}
			}

		} while (command != null);


		System.out.println ("\nExiting...");
	}


	private String readCommand ()
	{
		String command = new String ();

		try
		{

			String tmp = new String();

			do
			{
				int in = f.read ();

				Integer i = new Integer (in);

				byte [] bArray = new byte [1];
				bArray[0] = i.byteValue();

				tmp = new String (bArray);

				if ((command.length() == 0 && tmp.equals ("\n")) || (command.equals (" ") && tmp.equals ("\n")))
				{
					return (String) null;
				}

				command += tmp;


			} while ( ! tmp.equals (";"));

			f.read();
			// read in the '\n'...

		}
		catch (Exception e)
		{

		}

		return command;
	}


	public static void invalidCommand (String s)
	{
		System.out.println ("Error: " + s);
	}


	private void parseCreate (String s)
	{
		Vector v = new Vector ();
		String tableName = new String();

		str = new String (s);

		if (! nextW().toUpperCase().equals ("CREATE"))
		{
			invalidCommand("'create' keyword expected (E03).");
			return;
		}

		if (! nextW().toUpperCase().equals ("TABLE"))
		{
			invalidCommand("'table' keyword expected (E04).");
			return;
		}

		tableName = nextW();

		if (! nextW().equals ("("))
		{
			invalidCommand("'(' expected (E05).");
			return;
		}

		String nextStr = "";
		do
		{
			nextStr = nextW();

			if (nextStr.equals (""))
			{
				invalidCommand("more tokens expected (E06).");
				return;
			}
			else
			{
				v.add (nextStr);

				nextStr = nextW();

				if (! nextStr.equals (",") && ! nextStr.equals (")"))
				{
					invalidCommand("',' or ')' expected (E07).");
					return;
				}
			}
		} while (! nextStr.equals (")"));


		if (! nextW().equals (";"))
		{
			invalidCommand("';' expected (E08).");
			return;
		}

		dict.addTable (tableName, v);
	}


	private SelectStructure parseSelect (String s)
	{
		SelectStructure struct = new SelectStructure(dict);

		str = new String (s);

		if (! nextW().toUpperCase().equals ("SELECT"))
		{
			invalidCommand("'select' keyword expected (E09).");
			return null;
		}

		String nextStr = "";
		do
		{
			nextStr = nextW();

			if (nextStr.equals (""))
			{
				invalidCommand("more tokens expected (E10).");
				return null;
			}
			else
			{
				struct.addSelect (nextStr);

				nextStr = nextW();

				if (! nextStr.equals (",") && ! nextStr.toUpperCase().equals ("FROM"))
				{
					invalidCommand("',' or 'from' expected (E11).");
					return null;
				}
			}
		} while (! nextStr.toUpperCase().equals ("FROM"));

		do
		{
			nextStr = nextW();

			if (nextStr.equals (""))
			{
				invalidCommand("more tokens expected (E12).");
				return null;
			}
			else
			{
				struct.addFrom (nextStr);

				nextStr = nextW();

				if (! nextStr.equals (",") && ! nextStr.toUpperCase().equals ("WHERE"))
				{
					invalidCommand("',' or 'where' expected (E13).");
					return null;
				}
			}
		} while (! nextStr.toUpperCase().equals ("WHERE"));

		do
		{
			nextStr = "";

			while (! lookAhead().toUpperCase().equals ("AND") && ! lookAhead().equals (";"))
			{
				nextStr += nextW();
			} 

			if (nextStr.equals (""))
			{
				invalidCommand("more tokens expected (E14).");
				return null;
			}
			else
			{
				struct.addWhere (nextStr);

				nextStr = nextW();

				if (! nextStr.toUpperCase().equals ("AND") && ! nextStr.equals (";"))
				{
					invalidCommand("'AND' or ';' expected (E15).");
					return null;
				}
			}
		} while (! nextStr.equals (";"));

		if (struct.isValid() == false)
		{
			invalidCommand("Invalid select statement (E16).");
			return null;
		}
		else
		{
			return struct;
		}
	}


	private String nextW ()
	{
		String tmp = lookAhead();
		if (tmp.equals (""))
		{
			return tmp;
		}
		else
		{
			str = str.substring (tmp.length(), str.length());
			return tmp;
		}
	}

	private String lookAhead ()
	{
		String tmp = new String();

		if (str.length() == 0)
		{
			return "";
		}

		str = str.trim();

		String t = str.substring (0, 1);

		if (t.equals ("(") || t.equals (")") || t.equals (",") || t.equals (";") || t.equals ("\n"))
		{
			tmp = t;
		}
		else
		{
			for (int i = 0; i < str.length(); i++)
			{
				t = str.substring (i, i+1);
				if (t.equals ("(") || t.equals (")") || t.equals (" ") || t.equals (",") || t.equals (";") || t.equals ("\n"))
				{
					break;
				}
				else
				{
					tmp += t;
				}
			}
		}

		//str = str.substring (tmp.length(), str.length());

		return tmp;
	}

}


