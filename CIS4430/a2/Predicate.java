/**
 * CIS*4430 Information Organization and Retrieval
 * Assignment #2, due: Thursday, March 22, 2001
 * Michael Bochenek [ID: 0041056]
 * email: mboche01@uoguelph.ca
 */


public class Predicate
{
	private String left;
	private String right;
	private boolean constant;
	private boolean validPredicate = true;

	public Predicate (DataDictionary d, String s)
	{
		DataDictionary dict = d;

		int index = s.indexOf ("=");

		if (index == -1)
		{
			validPredicate = false;
			Main.invalidCommand ("There is no equal sign in the predicate '" + s + "' (E26).");
			return;
		}

		left = s.substring (0, index);
		right = s.substring (index+1, s.length());

		if (right.equals ("") || left.equals (""))
		{
			validPredicate = false;
			Main.invalidCommand ("There must be something on both sides of equal sign (E27).");
			return;
		}

		if (dict.attribFound (left))
		{
			left = left;
		}
		else
		{
			validPredicate = false;
			Main.invalidCommand ("Attribute (" + left + ") is not in the data dictionary (E28).");
			return;
		}

		if (dict.attribFound (right))
		{
			constant = false;
		}
		else
		{
			constant = true;
		}
	}


	public boolean isValid ()
	{
		return validPredicate;
	}


	public String getRight ()
	{
		return right;
	}


	public String getLeft ()
	{
		return left;
	}


	public boolean isConstant ()
	{
		return constant;
	}
}


