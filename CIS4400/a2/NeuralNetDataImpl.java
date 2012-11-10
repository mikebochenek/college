import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.Messaging.*;

import java.io.*;
import java.util.*;
import java.util.TreeSet;
import java.util.AbstractCollection.*;

public class NeuralNetDataImpl extends NeuralNetDataManager.NeuralNetDataPOA
{
	private Vector vname = new Vector();
	private Vector vfeatureCount = new Vector();
	private Vector vfeatureTypes = new Vector();
	private Vector vdata = new Vector ();
	private Vector vrandom = new Vector ();

	public void init (String name, boolean [] featureType, int featureCount) throws NeuralNetDataManager.NeuralNetDataPackage.datasetExistsException
	{
		for (int i = 0; i < vname.size(); i++)
		{
			if ( ((String) vname.elementAt (i)).equals (name) )
			{
				throw new NeuralNetDataManager.NeuralNetDataPackage.datasetExistsException ("Dataset '" + name + "' already exists");
			}
		}

		vname.add (name);
		vfeatureCount.add (new Integer (featureCount));
		vfeatureTypes.add (featureType);
		vdata.add (new Vector());
		vrandom.add (new Vector());
	}


	public void addSingleSample (String name, double [] featureData)
	{
		for (int i = 0; i < vname.size(); i++)
		{
			if ( ((String) vname.elementAt (i)).equals (name) )
			{
				int size = (((Integer) vfeatureCount.elementAt (i)).intValue());
	
				Double [] d = new Double [size];

				for (int j = 0; j < size; j++)
				{
					d[j] = new Double (featureData[j]);
				}

				((Vector) vdata.elementAt (i)).add (d);
			}
		}

	}


	public int getFeatureCount (String name)
	{
		int retVal = -1;

		for (int i = 0; i < vname.size(); i++)
		{
			if ( ((String) vname.elementAt (i)).equals (name) )
			{
				retVal = ((Integer) vfeatureCount.elementAt (i)).intValue(); 
			}
		}

		return retVal;
	}


	public int getSampleCount (String name)
	{
		int retVal = -1;

		for (int i = 0; i < vname.size(); i++)
		{
			if ( ((String) vname.elementAt (i)).equals (name) )
			{
				retVal = ((Vector) vdata.elementAt (i)).size();
			}
		}

		return retVal;
	}


	public boolean getFeatureType (String name, int featureNum)
	{
		boolean retVal = false;

		for (int i = 0; i < vname.size(); i++)
		{
			if ( ((String) vname.elementAt (i)).equals (name) )
			{
				retVal = ((boolean []) vfeatureTypes.elementAt (i))[featureNum];
			}
		}

		return retVal;

	}


	public double getMaxFeatureValue (String name, int featureNum)
	{
		double retVal = 0.0;

		for (int i = 0; i < vname.size(); i++)
		{
			if ( ((String) vname.elementAt (i)).equals (name) )
			{
				Vector v = (Vector) vdata.elementAt (i);

				double max = ((Double []) v.elementAt (0))[featureNum].doubleValue();

				for (int j = 0; j < v.size(); j++)
				{
					double tmp = ((Double []) v.elementAt (j))[featureNum].doubleValue();

					if (tmp > max)
					{
						max = tmp;
					}
				}

				retVal = max;
			}
		}


		return retVal;
	}


	public double getMinFeatureValue (String name, int featureNum)
	{
		double retVal = 0.0;

		for (int i = 0; i < vname.size(); i++)
		{
			if ( ((String) vname.elementAt (i)).equals (name) )
			{
				Vector v = (Vector) vdata.elementAt (i);

				double max = ((Double []) v.elementAt (0))[featureNum].doubleValue();

				for (int j = 0; j < v.size(); j++)
				{
					double tmp = ((Double []) v.elementAt (j))[featureNum].doubleValue();

					if (tmp < max)
					{
						max = tmp;
					}
				}

				retVal = max;
			}
		}

		return retVal;
	}


	public double getMeanFeatureValue (String name, int featureNum)
	{
		double retVal = 0.0;

		for (int i = 0; i < vname.size(); i++)
		{
			if ( ((String) vname.elementAt (i)).equals (name) )
			{
				Vector v = (Vector) vdata.elementAt (i);

				for (int j = 0; j < v.size(); j++)
				{
					double tmp = ((Double []) v.elementAt (j))[featureNum].doubleValue();

					retVal += tmp / v.size();
				}
			}
		}

		return retVal;
	}


	public double getMedianFeatureValue (String name, int featureNum)
	{
		double retVal = 0.0;

		for (int i = 0; i < vname.size(); i++)
		{
			if ( ((String) vname.elementAt (i)).equals (name) )
			{
				Vector v = (Vector) vdata.elementAt (i);

				TreeSet tree = new TreeSet (new BasicComparator());

				for (int j = 0; j < v.size(); j++)
				{
					double tmp = ((Double []) v.elementAt (j))[featureNum].doubleValue();

					tree.add (new Double (tmp));
				}

				Iterator t = tree.iterator();

				int cnt = 0;

				while (t.hasNext())
				{
					retVal = ((Double) t.next()).doubleValue();

					if (cnt == (v.size() / 2))
					{
						break;
					}

					cnt++;
				}
			}
		}

		return retVal;
	}

	public String getSample (String name, int sampleNum)
	{
		String retStr = "";

		for (int i = 0; i < vname.size(); i++)
		{
			if ( ((String) vname.elementAt (i)).equals (name) )
			{
				Vector v = ((Vector) vdata.elementAt (i));

				Double [] d = (Double []) v.elementAt (sampleNum);

				retStr += "Sample " + (sampleNum + 1) + ": ";

				for (int j = 0; j < d.length; j++)
				{
					if ( ((boolean []) vfeatureTypes.elementAt (i))[j] == true)
					{
						retStr += d[j].intValue() + " ";
					}
					else
					{
						retStr += d[j] + " ";
					}
				}
			}
		}

		return retStr;
	}


	public String getNormalizedSample (String name, int sampleNum)
	{

		return " ";
	}


	public String getRandomSampleSet (String name)
	{
		String retStr = "";

		for (int i = 0; i < vname.size(); i++)
		{
			if ( ((String) vname.elementAt (i)).equals (name) )
			{
				Random r = new Random();
				Vector v = ((Vector) vdata.elementAt (i));

				boolean [] selected = new boolean [v.size()];

				int done = 0;
				do
				{
					int tmp = r.nextInt (v.size());

					if (selected[tmp] == false)
					{
						done++;
						selected[tmp] = true;

						int sampleNum = tmp;

						Double [] d = (Double []) v.elementAt (sampleNum);

						retStr += "Sample " + (sampleNum + 1) + ": ";

						for (int j = 0; j < d.length; j++)
						{
							if ( ((boolean []) vfeatureTypes.elementAt (i))[j] == true)
							{
								retStr += d[j].intValue() + " ";
							}
							else
							{
								retStr += d[j] + " ";
							}
						}

						retStr += "\n";
					}

				} while (done < (v.size() * 0.7));
			}
		}

		return retStr;
	}

	public String getRemainingSampleSet (String name, String delimitedList)
	{
		String retStr = "";

		for (int i = 0; i < vname.size(); i++)
		{
			if ( ((String) vname.elementAt (i)).equals (name) )
			{
				StringTokenizer tokenizer = new StringTokenizer (delimitedList, "\n");
				Vector v = ((Vector) vdata.elementAt (i));
				boolean [] selected = new boolean [v.size()];

				while (tokenizer.hasMoreTokens())
				{
					String str = tokenizer.nextToken();
					int left = str.indexOf (" ", 3);
					int right = str.indexOf (":");
					int sampleNum = Integer.parseInt (str.substring (left, right).trim());
					selected[sampleNum - 1] = true;
				}

				for (int j = 0; j < v.size(); j++)
				{
					if (selected[j] == false)
					{
						int sampleNum = j;

						Double [] d = (Double []) v.elementAt (sampleNum);

						retStr += "Sample " + (sampleNum + 1) + ": ";

						for (int k = 0; k < (d.length - 1); k++)
						{
							if ( ((boolean []) vfeatureTypes.elementAt (i))[k] == true)
							{
								retStr += d[k].intValue() + " ";
							}
							else
							{
								retStr += d[k] + " ";
							}
						}

						retStr += "\n";
					}
				}

			}
		}

		return retStr;
	}

}

