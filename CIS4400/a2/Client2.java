import org.omg.CORBA.*;
import org.omg.Messaging.*;
/**
 * @(#)Client.java
 * @author Qusay H. Mahmoud
 */
public class Client2 {
	public static void main(String argv[])
	{
		if (argv.length < 2)
		{
			System.out.println ("Parameters required:  (1) name of dataset and (2) hostname or host IP");
			System.exit (-1);
		}

		// Initialize the ORB
		ORB orb = ORB.init(argv, null);

		// Bind to the Added on ID "Add"
		byte[] addId = "NeuralNetData".getBytes();

		NeuralNetDataManager.NeuralNetData data = NeuralNetDataManager.NeuralNetDataHelper.bind (orb, "/neural_net_data_poa", addId, argv[1], null);

		// this connects to localhost, as opposed to user specified host
		//NeuralNetDataManager.NeuralNetData data = NeuralNetDataManager.NeuralNetDataHelper.bind (orb, "/neural_net_data_poa", addId);



		String t = data.getRandomSampleSet (argv[0]);
		System.out.println (t);
		System.out.println ("-------------------------------------------------------");
		System.out.println (data.getRemainingSampleSet (argv[0], t));

	}
}
