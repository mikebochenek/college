import java.io.*;
import java.net.*;
import java.util.*;

// exception gets thrown if the port is already in use..
//  (if the program is already running

public class ServerThread extends Thread 
{
   private DatabaseInterface productData;
	private DatabaseInterface customerData;
	private PasswordInterface passwordData;

   protected DatagramSocket socket = null;
   protected BufferedReader in = null;
   protected boolean moreQuotes = true;

   private int productNum;
	private int packetCounter;

   public ServerThread() throws IOException 
	{
      this("ServerThread");
   }


   public ServerThread(String name) throws IOException 
	{
      super(name);
      socket = new DatagramSocket(4445);
      //socket = new DatagramSocket(4446);
   }


   public void run() 
	{
		productData = new DatabaseInterface("product.txt");
		customerData = new DatabaseInterface("customer.txt");
      passwordData = new PasswordInterface("password.txt");
      productNum = -1;

      while (moreQuotes) 
		{
         try 
			{
            byte[] buf = new byte[256];

            // receive request
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            // figure out response
            String dString = null;
            //dString = new Date().toString();
            String rec = new String (packet.getData());
            //String rec = new String (packet.getData(), 0);

            rec = rec.trim();
            String serverReply = processRequest (rec);

				// fix the serverReply
				for (int i = serverReply.length(); i < buf.length; i++)
				   serverReply += " ";

            //dString = "Received: " + rec;
            //buf = dString.getBytes();
				//buf = rec.getBytes();
				buf = serverReply.getBytes();

            // send the response to the client at "address" and "port"
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);

         } 
			catch (IOException e) 
			{
            e.printStackTrace();
            moreQuotes = false;
         }
      }
      socket.close();
   }


	private String processRequest (String request)
	{
		int valueReturned = 0;
		String database = new String (request.substring (0, 2));
		String action = new String (request.substring (2, 6));
		String parameter = new String (request.substring (6, request.length()));
 		
      if (database.equals ("l_"))
		{
         if (action.equals ("fnd_"))
			{
            valueReturned = passwordData.containsEntry (parameter);
				if (valueReturned == 0)
				{  
				   return new String ("_T");
				}
				else
				{
				   return new String ("_F");
			   }	
			}
			else if (action.equals ("add_"))
			{
				valueReturned = passwordData.addEntry (parameter);
				if (valueReturned == 0)
				{
					passwordData.writeDatabase();
				   return new String ("_T");
				}
				else
				{
				   return new String ("_F");
				}
			}
			else if (action.equals ("del_"))
			{
				valueReturned = passwordData.removeEntry (parameter);
				if (valueReturned == 0)
				{
					passwordData.writeDatabase();
				   return new String ("_T");
				}
				else
				{
				   return new String ("_F");
				}
			}
		}
		else if (database.equals ("p_"))
	 	{
		   if (action.equals ("add_"))
			{
				valueReturned = productData.addEntry (parameter);
				if (valueReturned == 0)
				{
				   return new String ("_T");
				}
				else
				{
				   return new String ("_F");
				}
		   }
			else if (action.equals ("upd_"))
	      {
	         String results = productData.getNthEntry (productNum);
			   Vector res = productData.parseEntry (results);
			   productData.removeEntry (results);
				Vector newRes = new Vector();
				newRes.addElement ( res.elementAt(0) );
				newRes.addElement ( res.elementAt(1) );
				newRes.addElement ( res.elementAt(2) );
				newRes.addElement ( res.elementAt(3) );
				//newRes.addElement ( res.elementAt(4) );
				newRes.addElement ( parameter );
				newRes.addElement ( res.elementAt(5) );
				newRes.addElement ( res.elementAt(6) );
				productData.addEntry (newRes);
				productData.writeDatabase();

				if (valueReturned == 0)
				{
				   return new String ("_T");
				}
				else
				{
				   return new String ("_F");
				}
		   }
			else if (action.equals ("del_"))
	      {
			   valueReturned = productData.removeEntry (parameter);
				if (valueReturned == 0)
				{
				   return new String ("_T");
				}
				else
				{
				   return new String ("_F");
				}
		   }
			else if (action.equals ("get_"))
			{
			   String results = new String();
				productNum++;
				if (productNum > (productData.getSize() - 1))
				{
				   productNum = productData.getSize() - 1;
				}
	         results = productData.getNthEntry (productNum);
				if (results.equals(""))
				{
				   results = new String ("_F");
				}
				return results;
		   }
			else if (action.equals ("prv_"))
			{
			   String results = new String();
				productNum--;
				if (productNum < 0)
				{
				   productNum = 0;
				}
	         results = productData.getNthEntry (productNum);
				if (results.equals(""))
				{
				   results = new String ("_F");
				}
				return results;
		   }
			else if (action.equals ("rea_"))
			{
			   productData = new DatabaseInterface ("product.txt"); 
				return new String ("_T");
		   }
			else if (action.equals ("wrt_"))
			{
	         valueReturned = productData.writeDatabase ("product.txt");
				if (valueReturned == 0)
				{
				   return new String ("_T");
				}
				else
				{
				   return new String ("_F");
				}
		   }
			else if (action.equals ("prn_"))
			{
	         productData.printDatabase ();
			}
			else if (action.equals ("srr_"))
			{
			   productData = new DatabaseInterface ("product.txt"); 
				packetCounter = -1;
				return new String ("_T");
			}
			else if (action.equals ("rrr_"))
			{
			   String results = new String();
				packetCounter++;
				if (packetCounter > (productData.getSize() - 1))
				{
				   results = new String ("_F");
				}
				else
				{
	            results = productData.getNthEntry (packetCounter);
				}

				if (results.equals(""))
				{
				   results = new String ("_F");
				}
				return results;
			}
			else if (action.equals ("sww_"))
			{
				valueReturned = productData.deleteAllEntries ();
				packetCounter = -1;
			}
			else if (action.equals ("www_"))
			{
				if ( ! (parameter.equals("_F")))
				{
				   valueReturned = productData.addEntry (parameter);
				   if (valueReturned == 0)
				   {
				      return new String ("_T");
				   }
				   else
				   {
				      return new String ("_F");
				   }
				}
				else
				{
	             valueReturned = productData.writeDatabase ("product.txt");
				}
				return new String ("_T");
			}
		}
		else if (database.equals ("c_"))
		{
		   if (action.equals ("add_"))
			{
				valueReturned = customerData.addEntry (parameter);
				if (valueReturned == 0)
				{
				   return new String ("_T");
				}
				else
				{
				   return new String ("_F");
				}
		   }
			else if (action.equals ("del_"))
	      {
			   valueReturned = customerData.removeEntry (parameter);
				if (valueReturned == 0)
				{
				   return new String ("_T");
				}
				else
				{
				   return new String ("_F");
				}
		   }
			else if (action.equals ("fnd_"))
			{
			   // get rid of trailing ;
            StringTokenizer t = new StringTokenizer (parameter, ";");
            parameter = t.nextToken();

			   Vector results = new Vector();
	         results = customerData.queryEntry (parameter, 3);
				try
				{
				   return new String ( (String) results.elementAt(0));
				}
				catch (Exception e)
				{
				   return new String ("_F");
				}
		   }
			else if (action.equals ("rea_"))
			{
			   customerData = new DatabaseInterface ("customer.txt"); 
				return new String ("_T");
		   }
			else if (action.equals ("wrt_"))
			{
	         valueReturned = customerData.writeDatabase ("customer.txt");
				if (valueReturned == 0)
				{
				   return new String ("_T");
				}
				else
				{
				   return new String ("_F");
				}
		   }
			else if (action.equals ("prn_"))
			{
	         customerData.printDatabase ();
			}
			else if (action.equals ("srr_"))
			{
			   customerData = new DatabaseInterface ("customer.txt"); 
				packetCounter = -1;
				return new String ("_T");
			}
			else if (action.equals ("rrr_"))
			{
			   String results = new String();
				packetCounter++;
				if (packetCounter > (customerData.getSize() - 1))
				{
				   results = new String ("_F");
				}
				else
				{
	            results = customerData.getNthEntry (packetCounter);
				}

				if (results.equals(""))
				{
				   results = new String ("_F");
				}
				return results;
			}
			else if (action.equals ("sww_"))
			{
				valueReturned = customerData.deleteAllEntries ();
				packetCounter = -1;
			}
			else if (action.equals ("www_"))
			{
				if ( ! (parameter.equals("_F")))
				{
				   valueReturned = customerData.addEntry (parameter);
				   if (valueReturned == 0)
				   {
				      return new String ("_T");
				   }
				   else
				   {
				      return new String ("_F");
				   }
				}
				else
				{
	             valueReturned = customerData.writeDatabase ("customer.txt");
				}
				return new String ("_T");
			}
		}

		return new String ("Invalid Protocol, request not understood by server");
   }
}
