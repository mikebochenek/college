/**
 * CIS*4430 Information Organization and Retrieval
 * Assignment #2, due: Thursday, March 22, 2001
 * Michael Bochenek [ID: 0041056]
 * email: mboche01@uoguelph.ca
 */

import java.util.Vector;


public class PlanGenerator
{
	private DataDictionary dict;
	private SelectStructure struct;

	private boolean validPlan = true;

	private Vector select;
	private Vector projection;
	private Vector product;
	private Vector join;

	private int tempNumber = 0;

	public PlanGenerator (DataDictionary d, SelectStructure s)
	{
		struct = s;
		dict = d;
		select = new Vector();
		projection = new Vector();
		product = new Vector();
		join = new Vector();
	}


	public void generate ()
	{
		if (struct == null || dict == null)
		{
			validPlan = false;
			return;
		}
		else
		{
			generateSelect();
			generateJoin();
			generateProduct();
			generateProjection();
		}
	}


	private void generateSelect()
	{
		Vector v = struct.getWhere();

		for (int i = 0; i < v.size(); i++)
		{
			Predicate p = (Predicate) v.elementAt (i);

			if (p.isConstant())
			{
				tempNumber++;
				String tmp = new String ();
				tmp += "temp" + tempNumber;
				tmp += " = select (";
				tmp += dict.findTable(p.getLeft()).getTmpName();
				tmp += ", ";
				tmp += p.getLeft() + "=" + p.getRight();
				tmp += ");";

				TableDef t = dict.findTable (p.getLeft());
				Vector tables = struct.getFrom();
				boolean found = false;
				for (int j = 0; j < tables.size(); j++)
				{
					if (t.getName().equals ( (String) tables.elementAt (j)))
					{
						found = true;
					}
				}
				if (found == false)
				{
					validPlan = false;
					Main.invalidCommand ("Attribute " + p.getLeft() + " is not in any of the selected tables (E18).");
				}
	
				dict.findTable(p.getLeft()).setTmpName ("temp" + tempNumber);
				select.add (tmp);
			}
		}

	}


	private void generateJoin()
	{
		Vector v = struct.getWhere();

		for (int i = 0; i < v.size(); i++)
		{
			Predicate p = (Predicate) v.elementAt (i);

			if (! p.isConstant())
			{
				if (dict.findTable (p.getLeft()).getTmpName().equals (dict.findTable (p.getRight()).getTmpName()))
				{
					validPlan = false;
					Main.invalidCommand ("Both attributes belong to the same table in join (E19).");
					return;
				}

				TableDef t = dict.findTable (p.getLeft());
				Vector tables = struct.getFrom();
				boolean found = false;
				for (int j = 0; j < tables.size(); j++)
				{
					if (t.getName().equals ( (String) tables.elementAt (j)))
					{
						found = true;
					}
				}
				if (found == false)
				{
					validPlan = false;
					Main.invalidCommand ("Attribute " + p.getLeft() + " is in table " + t.getName() + " is not SELECTED (E20).");
					return;
				}

				t = dict.findTable (p.getRight());
				found = false;
				for (int j = 0; j < tables.size(); j++)
				{
					if (t.getName().equals ( (String) tables.elementAt (j)))
					{
						found = true;
					}
				}
				if (found == false)
				{
					validPlan = false;
					Main.invalidCommand ("Attribute " + p.getRight() + " is in table " + t.getName() + " is not SELECTED (E21).");
					return;
				}


				tempNumber++;
				String tmp = new String ();
				tmp += "temp" + tempNumber;
				tmp += " = join (";
				tmp += dict.findTable(p.getLeft()).getTmpName();
				tmp += ", ";
				tmp += dict.findTable(p.getRight()).getTmpName();
				tmp += ", ";
				tmp += p.getLeft() + "=" + p.getRight();
				tmp += ");";

				if (! dict.findTable(p.getLeft()).getTmpName().equals (dict.findTable(p.getLeft()).getName()) && ! dict.findTable(p.getRight()).getTmpName().equals (dict.findTable(p.getRight()).getName()))
				{
					//System.out.println ("Warning: there might be a cycle in the graph.");
				}

				dict.findTable(p.getLeft()).setTmpName ("temp" + tempNumber);
				dict.findTable(p.getRight()).setTmpName ("temp" + tempNumber);

				dict.findTable(p.getLeft()).setJoinName ("temp" + tempNumber);
				dict.findTable(p.getRight()).setJoinName ("temp" + tempNumber);

				join.add (tmp);
			}
		}

	}


	private void generateProduct()
	{
		Vector v = struct.getFrom();


		// hack #1
		if (v.size() == 1)
		{
			return;
		}

		for (int i = 0; i < v.size(); i++)
		{
			String str = (String) v.elementAt (i);
			TableDef t = dict.getTable (str); 


			if (! dict.getTable(str).getJoinName().equals ("temp" + (tempNumber-1)))
			{
				if (new String("temp" + tempNumber).equals (dict.getTable(str).getJoinName()))
				{
				}
				else if (new String("temp" + (tempNumber-1)).equals (dict.getTable(str).getJoinName()))
				{
				}
				else
				{
					tempNumber++;
					String tmp = new String ();
					tmp += "temp" + tempNumber;
					tmp += " = product (";
					tmp += "temp" + (tempNumber - 1);
					tmp += ", ";
					tmp += t.getTmpName();
					tmp += ");";

					t.setTmpName ("temp" + tempNumber);
			
					dict.getTable(str).setJoinName ("temp" + (tempNumber));
					dict.getTable(i).setJoinName ("temp" + (tempNumber));

					product.add (tmp);

					// hack #2
					if (i == 0) i++;

					for (int j = 0; j < v.size(); j++)
					{
						if (dict.getTable(j).getJoinName().startsWith ("temp" + (tempNumber - 1)))
						{
							dict.getTable(j).setJoinName ("temp" + tempNumber);
						}
						if (dict.getTable(j).getJoinName().startsWith ("temp" + (tempNumber - 0)))
						{
							dict.getTable(j).setJoinName ("temp" + tempNumber);
						}
					}
				}
			}
		}
	}


	private void generateProjection()
	{
		Vector v = struct.getSelect();

		tempNumber++;

		String tmp = new String ();
		tmp += "temp" + tempNumber;
		tmp += " = projection (";
		tmp += "temp" + (tempNumber - 1);

		for (int i = 0; i < v.size(); i++)
		{
			tmp += ", ";
			tmp += v.elementAt(i);

			TableDef t = dict.findTable ( (String) v.elementAt(i));
			Vector tables = struct.getFrom();
			boolean found = false;
			for (int j = 0; j < tables.size(); j++)
			{
				if (t.getName().equals ( (String) tables.elementAt (j)))
				{
					found = true;
				}
			}
			if (found == false)
			{
				validPlan = false;
				Main.invalidCommand ("Attribute " + v.elementAt(i) + " is in table " + t.getName() + " is not SELECTED (E22).");
			}
		}

		tmp += ");";

		projection.add (tmp);
	}


	public void print ()
	{
		System.out.println ("   ---");

		for (int i = 0; i < select.size(); i++)
		{
			System.out.println (select.elementAt (i));
		}

		for (int i = 0; i < join.size(); i++)
		{
			System.out.println (join.elementAt (i));
		}

		for (int i = 0; i < product.size(); i++)
		{
			System.out.println (product.elementAt (i));
		}

		for (int i = 0; i < projection.size(); i++)
		{
			System.out.println (projection.elementAt (i));
		}

		System.out.println ("   ---");
	}

	public boolean isValid ()
	{
		return validPlan;
	}


	public void resetDataDictionary ()
	{
		dict.resetTmpNames();
	}

}



