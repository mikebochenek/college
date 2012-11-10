import org.omg.CORBA.*;
import org.omg.PortableServer.*;

/**
 * @(#)Server.java
 * @author Qusay H. Mahmoud
 */
public class Server {
	public static void main(String argv[]) {
     	try 
		{
		     // Initialize the ORB
			ORB orb = ORB.init(argv, null);
			
			// get a reference to the root POA
			POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
		     
			// Create policies for our persistent POAS
			org.omg.CORBA.Policy[] policies = {
				rootPOA.create_lifespan_policy(LifespanPolicyValue.PERSISTENT)
			};

			// Create the Add servant and activate it on add_poa
			POA NeuralNetDataPOA = rootPOA.create_POA("neural_net_data_poa", rootPOA.the_POAManager (), policies);
			// ___ POA addPOA = rootPOA.create_POA("add_poa", rootPOA.the_POAManager (), policies);
			NeuralNetDataImpl addServant = new NeuralNetDataImpl();
			// ___ AddImpl addServant = new AddImpl();
			
			// Set the Id for the Servant and acivate the ID on the POA
			byte[] addId = "NeuralNetData".getBytes();
			// ____ byte[] addId = "Add".getBytes();
			NeuralNetDataPOA.activate_object_with_id(addId, addServant);

			// Activate the POA managers
			rootPOA.the_POAManager().activate();
					
			// Wait for incomming connections
			System.out.println ("The Server Awaits a connection");
			orb.run();
	    
		}
		catch(Exception se) 
		{
			se.printStackTrace();
		}
	}
}

