import org.omg.CORBA.*;
import org.omg.Messaging.*;
import java.io.*;

/**
 * @(#)Client.java
 * @author Qusay H. Mahmoud
 */
public class Client1 {
	public static void main(String argv[])
	{
		// Initialize the ORB
		ORB orb = ORB.init(argv, null);

		// Bind to the Added on ID "Add"
		byte[] addId = "NeuralNetData".getBytes();
		NeuralNetDataManager.NeuralNetData data = NeuralNetDataManager.NeuralNetDataHelper.bind (orb, "/neural_net_data_poa", addId, argv[1], null);

		// this connects to localhost, as opposed to user specified host
		//NeuralNetDataManager.NeuralNetData data = NeuralNetDataManager.NeuralNetDataHelper.bind (orb, "/neural_net_data_poa", addId);

		if (argv.length < 2)
		{
			System.out.println ("Parameters required:  (1) name of dataset and (2) hostname or host IP");
			System.exit(-1);
		}

		try
		{
			RandomAccessFile file = new RandomAccessFile (argv[0], "r");
			long filePointer = 0;
			long length = file.length();

			int outerCounter = 0;
			while (filePointer < length)
			{
				String str = file.readLine();
				filePointer = file.getFilePointer();

				int spaceIndex = str.indexOf (":");
				spaceIndex = str.indexOf (" ", spaceIndex);

				boolean [] isInteger = new boolean [128];
				double [] fdata = new double [128];
				int counter = 0;

				do
				{
					int tmpIndex = str.indexOf (" ", spaceIndex + 1);
					String tmp = "";

					if (tmpIndex != -1)
					{
						tmp = str.substring (spaceIndex, tmpIndex); 
					}
					else
					{
						tmp = str.substring (spaceIndex, str.length());
					}

					if (tmp != "")
					{
						try
						{
							int i = Integer.parseInt (tmp.trim());
							isInteger[counter] = true;
						}
						catch (Exception e)
						{
							isInteger[counter] = false;
						}

						try
						{
							fdata[counter] = Double.parseDouble (tmp);
						}
						catch (Exception e)
						{
							System.out.println ("Number (int/real) expected!");
							System.exit(-1);
						}
					}
					spaceIndex = tmpIndex;

					counter++;

				} while (spaceIndex != -1);

				if (outerCounter == 0)
				{
					// call the init method on object
					data.init (argv[0], isInteger, counter);
				}

				outerCounter++;

				// call the add method on object
				data.addSingleSample (argv[0], fdata);

			}

			file.close();
		}
		catch (Exception e)
		{
			System.out.println ("Exception in main: " + e);
			System.exit(-1);
		}

		System.out.println ("Name of the dataset: " + argv[0]);
		int features = data.getFeatureCount (argv[0]);
		System.out.print ("Number of features: " + features);

		System.out.print (" (");
		for (int i = 0; i < features; i++)
		{
			if (data.getFeatureType (argv[0], i) == true)
			{
				if (i == (features - 1))
				{
					System.out.print ("int");
				}
				else
				{
					System.out.print ("int, ");
				}
			}
			else
			{
				if (i == (features - 1))
				{
					System.out.print ("real");
				}
				else
				{
					System.out.print ("real, ");
				}
			}
		}
		System.out.print (")\n");

		System.out.println ("Number of samples currently in the dataset: " + data.getSampleCount (argv[0]));

		System.out.println ("Feature #: MAX MIN MEAN MEDIAN");
		for (int i = 0; i < features; i++)
		{
			System.out.print ("Feature " + i + ":");
			System.out.print (" " + data.getMaxFeatureValue (argv[0], i));
			System.out.print (" " + data.getMinFeatureValue (argv[0], i));
			System.out.print (" " + data.getMeanFeatureValue (argv[0], i));
			System.out.print (" " + data.getMedianFeatureValue (argv[0], i));
			System.out.print ("\n");
		}
	}
}
