// Michael Bochenek (ID) 0041056
// CIS 3430 Assignment #1

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import java.util.StringTokenizer;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.plaf.metal.MetalIconFactory;
import java.io.*;
import java.net.*;


public class ClientGUI extends JPanel {
   // main frame that holds everything, ie main window
   static JFrame frame;
   JTabbedPane tabbedPane;

   // customerData is the tree for all the customers
   private DatabaseInterface customerData; 

   // common customer information
   private String emailOfCustomer;
						
   // 6 panels, one for each function
	private JPanel mainMenu = new JPanel();
	private JPanel loginMenu = new JPanel();
   private JPanel addCustomer = new JPanel();
	private JPanel purchaseItems = new JPanel();
   private JPanel modifyCustomer = new JPanel();
	private JPanel changeLogin = new JPanel();
   //private JPanel deleteCustomer = new JPanel();
   //private JPanel queryCustomer = new JPanel();
   //private JPanel emailCustomer = new JPanel();


   // ------------------------------------
   // Create MainMenu Tab
   // 
	private void buildMainMenuTab() 
	{
      // setup grid bag layout manager
      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();
      mainMenu.setLayout (gridbag);

      MetalIconFactory z = new MetalIconFactory();

      // common attributes
      c.fill = GridBagConstraints.BOTH;
      c.insets = new Insets (5, 5, 5, 5);

      JLabel a1 =  new JLabel ("Yes, that's right now you can finally purchase one the Big servers.  These leading");
		JLabel a2 =  new JLabel ("edge machines have been selling faster than the manufactures can provide them.");
		JLabel a3 = new JLabel ("For the low price of 12999.99 this Big server can be yours as soon as your cheque clears.");
		JLabel a4 = new JLabel ("Do not wait because these puppies are selling faster than you can click.");
		JLabel c1 = new JLabel ("Another really really hot product that is currently being offered is the");
      JLabel c2 = new JLabel ("beautiful printing paper that will make your documents shine with brilliance");
      JLabel c3 = new JLabel ("The High Quality Paper is the brand the graphics professionals use for");
      JLabel c4 = new JLabel ("mission critical documents.  You should too.");
      JLabel d1 = new JLabel ("And last, but not least, we are very excited to present to you a special deal");
      JLabel d2 = new JLabel ("on the lovely floppy disks shown.  These disks are only 0.18 and they have");
      JLabel d3 = new JLabel ("won countless awards for quality and robustness.  They offer increased speed");
      JLabel d4 = new JLabel ("and reliability.  Special offers avaiable for bulk buyers.");
		JLabel b1 = (new JLabel (z.getTreeFloppyDriveIcon()));
		JLabel b2 = (new JLabel (z.getTreeLeafIcon()));
		JLabel b3 = (new JLabel (z.getTreeComputerIcon()));
      
		c.anchor = GridBagConstraints.WEST;
      c.gridwidth = GridBagConstraints.RELATIVE;
		gridbag.setConstraints (b3, c);
		mainMenu.add (b3);
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints (a1, c);
      mainMenu.add (a1);
		gridbag.setConstraints (a2, c);
      mainMenu.add (a2);
		gridbag.setConstraints (a3, c);
      mainMenu.add (a3);
		gridbag.setConstraints (a4, c);
      mainMenu.add (a4);
   
		c.anchor = GridBagConstraints.WEST;
      c.gridwidth = GridBagConstraints.RELATIVE;
		gridbag.setConstraints (b2, c);
		mainMenu.add (b2);
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints (c1, c);
      mainMenu.add (c1);
		gridbag.setConstraints (c2, c);
      mainMenu.add (c2);
		gridbag.setConstraints (c3, c);
      mainMenu.add (c3);
		gridbag.setConstraints (c4, c);
      mainMenu.add (c4);
   
		c.anchor = GridBagConstraints.WEST;
      c.gridwidth = GridBagConstraints.RELATIVE;
		gridbag.setConstraints (b1, c);
		mainMenu.add (b1);
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints (d1, c);
      mainMenu.add (d1);
		gridbag.setConstraints (d2, c);
      mainMenu.add (d2);
		gridbag.setConstraints (d3, c);
      mainMenu.add (d3);
		gridbag.setConstraints (d4, c);
      mainMenu.add (d4);

      JLabel b = new JLabel ("Remember, you must be a member to purchase products");
		c.ipadx = 25;
		c.ipady = 100;
      c.insets = new Insets (5, 5, 25, 25);
		c.gridheight = GridBagConstraints.REMAINDER;
		gridbag.setConstraints (b, c);
		mainMenu.add (b);
	}


   // ------------------------------------
   // Create LoginMenu Tab
   // 
	private void buildLoginMenuTab() 
   {
	   JPanel temp = new JPanel();
		
      // use gridbag layout for this panel
      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();
      temp.setLayout (gridbag);

      // common attributes
      c.fill = GridBagConstraints.NONE;
      c.insets = new Insets (2, 2, 2, 2);
   
      //
      // NAME label and text field
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel nameLabel = new JLabel ("E-mail address", JLabel.RIGHT);
      gridbag.setConstraints (nameLabel, c);
      temp.add (nameLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField emailAddress = new JTextField (30);
      gridbag.setConstraints (emailAddress, c);
      temp.add (emailAddress);
		
		//
      // PASSWORD label and text field
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel passLabel = new JLabel ("Password", JLabel.RIGHT);
      gridbag.setConstraints (passLabel, c);
      temp.add (passLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JPasswordField passwordText = new JPasswordField (30);
      gridbag.setConstraints (passwordText, c);
      temp.add (passwordText);

      gridbag = new GridBagLayout();
      c = new GridBagConstraints();
      loginMenu.setLayout (gridbag);
      c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints (temp, c);

      temp.setPreferredSize (new Dimension (600, 200));
      loginMenu.add (temp);
      

      // 
      // OK and CANCEL BUTTONS 
      //
      c.insets = new Insets (15, 15, 15, 15);
      c.anchor = GridBagConstraints.CENTER;
      c.ipadx = 10;
      c.ipady = 10;
		
      JButton okButton = new JButton ("Login");
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints (okButton, c);
      loginMenu.add (okButton);
   
      //
      // LOGOUT BUTTON
      //
      c.insets = new Insets (15, 15, 15, 15);
      c.anchor = GridBagConstraints.CENTER;
      c.ipadx = 10;
      c.ipady = 10;
      JButton logoutButton = new JButton ("Logout");
      c.gridwidth = GridBagConstraints.REMAINDER;
      gridbag.setConstraints (logoutButton, c);
      loginMenu.add (logoutButton);
		
      //
      // LOGOUT BUTTON ACTION LISTENER
      //
      logoutButton.addActionListener (new ActionListener() 
         {
            public void actionPerformed (ActionEvent e)
            {
               tabbedPane.setEnabledAt (2, true);
               tabbedPane.setEnabledAt (4, false);
               tabbedPane.setEnabledAt (5, false);

					emailOfCustomer = new String();
               JOptionPane x = new JOptionPane();
               x.showMessageDialog (loginMenu, 
                                    "Logout Success.", 
                                    "Logout Status", 
                                    JOptionPane.INFORMATION_MESSAGE);
            }        
         } 
      ); // end of saveButton LISTENER
 
      // 
      // ACTION LISTENER FOR "OK BUTTON"
      //
      okButton.addActionListener (new ActionListener() 
         {
            public void actionPerformed (ActionEvent e)
            {
					String password = new String(passwordText.getPassword());
					String email = new String(emailAddress.getText());
					String sendToServer = new String();
					//sendToServer = "l_fnd_" + email + ";" + password;
					sendToServer = "l_fnd_" + email + ";" + password + ";";
					String receiveFromServer = new String();
					receiveFromServer = talkToServer (sendToServer);
               
					if (receiveFromServer.equals ("_T") &&
					    (! email.equals("")) && 
						 (! password.equals("")) )
					{
					   // the entry was found in the database
                  JOptionPane x = new JOptionPane();
                  x.showMessageDialog (loginMenu, 
                                       "Login Success.", 
                                       "Login Status", 
                                       JOptionPane.INFORMATION_MESSAGE);
                
                  tabbedPane.setEnabledAt (2, false);
                  tabbedPane.setEnabledAt (4, true);
                  tabbedPane.setEnabledAt (5, true);

						emailOfCustomer = email;
					}
					else
					{
					   // the entry was not found in the database
	               JOptionPane x = new JOptionPane();
                  x.showMessageDialog (loginMenu, 
                                       "Sorry, Login Failed.", 
                                       "Login Status", 
                                       JOptionPane.ERROR_MESSAGE);
    				}

					passwordText.setText ("");
					emailAddress.setText ("");
            }        
         } 
      ); // end of okButton LISTENER
	}
	


   // ------------------------------------
   // Create PurchaseItems Tab
   // 
	private void buildPurchaseItemsTab() 
	{
      JPanel temp = new JPanel();
		
      final JLabel supplierEmail = new JLabel();
		
      // use gridbag layout for this panel
      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();
      temp.setLayout (gridbag);

      // common attributes
      c.fill = GridBagConstraints.NONE;
      c.insets = new Insets (2, 2, 2, 2);
   
	
      //
      // NAME label and text field
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel nameLabel = new JLabel ("Reference Name:", JLabel.RIGHT);
      gridbag.setConstraints (nameLabel, c);
      temp.add (nameLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JLabel nameOfProduct = new JLabel ();
      gridbag.setConstraints (nameOfProduct, c);
      temp.add (nameOfProduct);

      //
      // UPC label and text field
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel UPCLabel = new JLabel ("UPC:", JLabel.RIGHT);
      gridbag.setConstraints (UPCLabel, c);
      temp.add (UPCLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JLabel UPCOfProduct = new JLabel();
      gridbag.setConstraints (UPCOfProduct, c);
      temp.add (UPCOfProduct);

      //
      // PRICE label and text field
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel priceLabel = new JLabel ("Price:", JLabel.RIGHT);
      gridbag.setConstraints (priceLabel, c);
      temp.add (priceLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JLabel priceOfProduct = new JLabel();
      gridbag.setConstraints (priceOfProduct, c);
      temp.add (priceOfProduct);

      //
      // AMOUNT label and text field
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel amountLabel = new JLabel ("Amount on hand:", JLabel.RIGHT);
      gridbag.setConstraints (amountLabel, c);
      temp.add (amountLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JLabel amountOfProduct = new JLabel();
      gridbag.setConstraints (amountOfProduct, c);
      temp.add (amountOfProduct);

      //
      // DELIEVERY TIME label and textbox
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel timeLabel = new JLabel ("Shipping Time:", JLabel.RIGHT);
      gridbag.setConstraints (timeLabel, c);
      temp.add (timeLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JLabel timeOfProduct = new JLabel();
      gridbag.setConstraints (timeOfProduct, c);
      temp.add (timeOfProduct);

      gridbag = new GridBagLayout();
      c = new GridBagConstraints();
      purchaseItems.setLayout (gridbag);
      c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints (temp, c);

      temp.setPreferredSize (new Dimension (600, 200));
      purchaseItems.add (temp);
 
      //
      // PREVIOUS BUTTON
      //
      c.insets = new Insets (15, 15, 15, 15);
      c.anchor = GridBagConstraints.CENTER;
      c.ipadx = 10;
      c.ipady = 10;
      JButton previousButton = new JButton ("Previous Product");
      c.gridwidth = GridBagConstraints.REMAINDER;
      gridbag.setConstraints (previousButton, c);
      purchaseItems.add (previousButton);

      // 
      // NEXT BUTTONS 
      //
      c.insets = new Insets (15, 15, 15, 15);
      c.anchor = GridBagConstraints.CENTER;
      c.ipadx = 10;
      c.ipady = 10;
      JButton nextButton = new JButton ("Next Product");
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints (nextButton, c);
      purchaseItems.add (nextButton);
 
      // 
      // BUY BUTTONS 
      //
      c.insets = new Insets (15, 15, 15, 15);
      c.anchor = GridBagConstraints.CENTER;
      c.ipadx = 10;
      c.ipady = 10;
      JButton buyButton = new JButton ("Purchase Product");
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints (buyButton, c);
      purchaseItems.add (buyButton);

      //
		// get the first item
		//
		String sendToServer1 = new String();
		sendToServer1 = "p_prv_" + ";";
		String receiveFromServer1 = new String();
		receiveFromServer1 = talkToServer (sendToServer1);
      if ( ! receiveFromServer1.equals ("_F"))
		{
         StringTokenizer t1 = new StringTokenizer (receiveFromServer1, ";");
         nameOfProduct.setText (t1.nextToken());	
         UPCOfProduct.setText (t1.nextToken());	
		   supplierEmail.setText (t1.nextToken());
         priceOfProduct.setText (t1.nextToken());	
         amountOfProduct.setText (t1.nextToken());	
         timeOfProduct.setText (t1.nextToken());	
		}


      //
      // NEXT BUTTON ACTION LISTENER
      //
      nextButton.addActionListener (new ActionListener() 
         {
            public void actionPerformed (ActionEvent e)
            {
					String sendToServer = new String();
					sendToServer = "p_get_" + ";";
					String receiveFromServer = new String();
					receiveFromServer = talkToServer (sendToServer);

               if ( ! receiveFromServer.equals ("_F"))
					{
                  StringTokenizer t = new StringTokenizer (receiveFromServer, ";");
                  nameOfProduct.setText (t.nextToken());	
                  UPCOfProduct.setText (t.nextToken());	
					   supplierEmail.setText (t.nextToken());
                  priceOfProduct.setText (t.nextToken());	
                  amountOfProduct.setText (t.nextToken());	
                  timeOfProduct.setText (t.nextToken());	
					}

            }        
         } 
      ); // end of saveButton LISTENER
  
      //
      // PREVIOUS BUTTON ACTION LISTENER
      //
      previousButton.addActionListener (new ActionListener() 
         {
            public void actionPerformed (ActionEvent e)
            {
					String sendToServer = new String();
					sendToServer = "p_prv_" + ";";
					String receiveFromServer = new String();
					receiveFromServer = talkToServer (sendToServer);

               if ( ! receiveFromServer.equals ("_F"))
					{
                  StringTokenizer t = new StringTokenizer (receiveFromServer, ";");
                  nameOfProduct.setText (t.nextToken());	
                  UPCOfProduct.setText (t.nextToken());	
					   supplierEmail.setText (t.nextToken());
                  priceOfProduct.setText (t.nextToken());	
                  amountOfProduct.setText (t.nextToken());	
                  timeOfProduct.setText (t.nextToken());	
					}

            }        
         } 
      ); // end of saveButton LISTENER
 
      // 
      // ACTION LISTENER FOR "PURCHASE BUTTON"
      //
      buyButton.addActionListener (new ActionListener() 
         {
            public void actionPerformed (ActionEvent e)
            {
					// abcd
					if (emailOfCustomer.equals (""))
					{
	               JOptionPane x = new JOptionPane();
                  x.showMessageDialog (purchaseItems, 
                                       "You cannot purchase items yet.\nYou may:\na) login\nb)leave the shop\nc)subscribe\n", 
													"Purchase Status.",
                                       JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						JOptionPane x = new JOptionPane();
						String tempx = x.showInputDialog ("Enter the quantity you wish to purchase");
						
						try 
						{
                     if (tempx.equals(""))
							{
						      return;
							}
						}
						catch (Exception excpt)
						{
							return;
						}
						
					   int qDemanded = (new Integer (tempx)).intValue();
                  int qAvailable = (new Integer (amountOfProduct.getText())).intValue();
						if (qDemanded > qAvailable)
						{
	                  JOptionPane z = new JOptionPane();
                     z.showMessageDialog (purchaseItems, 
                                          "The quantity that you requested is not available.  However it will be ordered \nimmediately and you will receive it as soon as possible.", 
								   					"Purchase Status.",
                                          JOptionPane.ERROR_MESSAGE);

							// notify the supplier
							String message = new String();
							message += "We would like to order more of: \n";
							message += "Product name: ";
							message += nameOfProduct.getText();
							message += "\nUPC Code: ";
							message += UPCOfProduct.getText();
							message += "\nWe would like to order 1000 items.\n";

							mail (supplierEmail.getText(), message);
						}
						else
						{
							JOptionPane z = new JOptionPane();
							Object[] possibleValues = {"Reqular Mail ($4.99)", "Express Post ($8.99)", "Courier ($14.99)"};
							
							Object selectedValue = z.showInputDialog
							   (null,
								 "Choose a shipping method",
								 "Input",
								 JOptionPane.INFORMATION_MESSAGE,
								 null,
								 possibleValues,
								 possibleValues[0]);

							 
	                  JOptionPane y = new JOptionPane();
                     y.showMessageDialog (purchaseItems, 
                                         "Your order has been placed.\nYou will receive a confirmation E-mail.", 
							   						"Purchase Status.",
                                          JOptionPane.INFORMATION_MESSAGE);

						   int newAmount = qAvailable - qDemanded; 
                     amountOfProduct.setText( (new Integer(newAmount)).toString());

                     if (newAmount <= 0)
							{
							   // notify the supplier
							   String message = new String();
							   message += "We would like to order more of: \n";
							   message += "Product name: ";
							   message += nameOfProduct.getText();
							   message += "\nUPC Code: ";
							   message += UPCOfProduct.getText();
							   message += "\nWe would like to order 1000 items.\n";

							   mail (supplierEmail.getText(), message);
							}
							
							
					      String sendToServer = new String();
					      sendToServer = "p_upd_" + amountOfProduct.getText();
					      String receiveFromServer = new String();
					      receiveFromServer = talkToServer (sendToServer);
							
                  // send email here...
                  String entireEmail = "";
                  
                  //entireEmail += emailOfCustomer;
                  entireEmail += "\n";
                  entireEmail += "Thank you for making your purchase\n";
						entireEmail += "Product ordered: ";
						entireEmail += nameOfProduct.getText();
						entireEmail += "\nPrice: ";
						entireEmail += priceOfProduct.getText();
						entireEmail += "\nQuantity Ordered: ";
						entireEmail += qDemanded;
						entireEmail += "\nDelievery Option: ";
                  entireEmail += (String) selectedValue;
						entireEmail += "\n\nThanks for shopping at buy.com\n";

                  mail (emailOfCustomer, entireEmail);

						}
					}
            }        
         } 
      ); // end of okButton LISTENER
	}
        

   // ------------------------------------
   // Create ChangeLogin Tab
   // 
	private void buildChangeLoginTab() 
	{
	   JPanel temp = new JPanel();
		
      // use gridbag layout for this panel
      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();
      temp.setLayout (gridbag);

      // common attributes
      c.fill = GridBagConstraints.NONE;
      c.insets = new Insets (2, 2, 2, 2);
   
      //
      // NAME label and text field
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel nameLabel = new JLabel ("E-mail address", JLabel.RIGHT);
      gridbag.setConstraints (nameLabel, c);
      temp.add (nameLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField emailAddress = new JTextField (30);
      gridbag.setConstraints (emailAddress, c);
      temp.add (emailAddress);
		
		//
      // PASSWORD label and text field
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel passLabel = new JLabel ("Password", JLabel.RIGHT);
      gridbag.setConstraints (passLabel, c);
      temp.add (passLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JPasswordField passwordText = new JPasswordField (30);
      gridbag.setConstraints (passwordText, c);
      temp.add (passwordText);
	
		//
      // NEW PASSWORD label and text field
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel pass2Label = new JLabel ("New Password", JLabel.RIGHT);
      gridbag.setConstraints (pass2Label, c);
      temp.add (pass2Label);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JPasswordField newPasswordText = new JPasswordField (30);
      gridbag.setConstraints (newPasswordText, c);
      temp.add (newPasswordText);
	
		//
      // REENTER NEW PASSWORD label and text field
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel pass3Label = new JLabel ("Re-enter New Password", JLabel.RIGHT);
      gridbag.setConstraints (pass3Label, c);
      temp.add (pass3Label);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JPasswordField reEnterPasswordText = new JPasswordField (30);
      gridbag.setConstraints (reEnterPasswordText, c);
      temp.add (reEnterPasswordText);

      gridbag = new GridBagLayout();
      c = new GridBagConstraints();
      changeLogin.setLayout (gridbag);
      c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints (temp, c);

      temp.setPreferredSize (new Dimension (600, 200));
      changeLogin.add (temp);
      

      // 
      // OK and CANCEL BUTTONS 
      //
      c.insets = new Insets (15, 15, 15, 15);
      c.anchor = GridBagConstraints.CENTER;
      c.ipadx = 10;
      c.ipady = 10;
		
      JButton okButton = new JButton ("Change");
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.CENTER;
      gridbag.setConstraints (okButton, c);
      changeLogin.add (okButton);
    
      // 
      // ACTION LISTENER FOR "OK BUTTON"
      //
      okButton.addActionListener (new ActionListener() 
         {
            public void actionPerformed (ActionEvent e)
            {
					String password = new String(passwordText.getPassword());
					String newPassword = new String(newPasswordText.getPassword());
					String reEnterPassword = new String(reEnterPasswordText.getPassword());
					String email = new String(emailAddress.getText());

					if ( ! (newPassword.equals (reEnterPassword)))
					{
                  // the entry was not found in the database
	               JOptionPane x = new JOptionPane();
                  x.showMessageDialog (changeLogin, 
						                     "The two new passwords that you entered did not match,\nand thus the password was NOT changed.",
                                       "Change Login Status", 
                                       JOptionPane.ERROR_MESSAGE);
    				}
					else if ( newPassword.length() < 6)
					{
                  // the entry was not found in the database
	               JOptionPane x = new JOptionPane();
                  x.showMessageDialog (changeLogin, 
						                     "The new password that you entered is too short,\nand thus the password was NOT changed.  The password must \nbe at least 6 characters in length.",
                                       "Change Login Status", 
                                       JOptionPane.ERROR_MESSAGE);
    				}
               else
					{
					   String sendToServer = new String();
					   sendToServer = "l_fnd_" + email + ";" + password + ";";
					   String receiveFromServer = new String();
					   receiveFromServer = talkToServer (sendToServer);
               
					   if (receiveFromServer.equals ("_T") &&
					      (! email.equals("")) && 
						   (! password.equals("")) )
					   {
					      // the entry was not found in the database
					      sendToServer = "l_add_" + email + ";" + newPassword + ";";
					      receiveFromServer = new String();
					      receiveFromServer = talkToServer (sendToServer);
               
					      if (receiveFromServer.equals ("_T"))
                     {
	                     JOptionPane x = new JOptionPane();
                        x.showMessageDialog (changeLogin, 
                                             "Your password has been changed.", 
                                             "Change Login Status", 
                                             JOptionPane.INFORMATION_MESSAGE);
								
								// must delete old entry from database...
								// (do not really care if successful or not)
					         sendToServer = "l_del_" + email + ";" + password + ";";
					         receiveFromServer = new String();
					         receiveFromServer = talkToServer (sendToServer);
							}
							else
							{
	                     JOptionPane x = new JOptionPane();
                        x.showMessageDialog (changeLogin, 
                                             "Unable to change password.", 
                                             "Change Login Status", 
                                             JOptionPane.ERROR_MESSAGE);
							}
                  }	
					   else
						{
					      // the entry was found in the database
                     JOptionPane x = new JOptionPane();
                     x.showMessageDialog (changeLogin, 
                                          "Sorry, old password is incorrect.", 
                                          "Change Login Status", 
                                          JOptionPane.ERROR_MESSAGE);
					   }
     				}

					newPasswordText.setText ("");
					reEnterPasswordText.setText ("");
					passwordText.setText ("");
					emailAddress.setText ("");
            }        
         } 
      ); // end of okButton LISTENER
	}


   // ------------------------------------
   // Create Customer Tab
   // 
   private void buildCustomerTab() {
      // use gridbag layout for this panel
      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();
      addCustomer.setLayout (gridbag);

      // common attributes
      c.fill = GridBagConstraints.NONE;
      c.insets = new Insets (2, 2, 2, 2);
      
      //
      // SURNAME label and text field 
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel surnameLabel = new JLabel ("Surname:", JLabel.RIGHT);
      gridbag.setConstraints (surnameLabel, c);
      addCustomer.add (surnameLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField surnameOfCustomer = new JTextField (40);
      gridbag.setConstraints (surnameOfCustomer, c);
      addCustomer.add (surnameOfCustomer);

      //
      // NAME label and text field 
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel nameLabel = new JLabel ("Given name:", JLabel.RIGHT);
      gridbag.setConstraints (nameLabel, c);
      addCustomer.add (nameLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField nameOfCustomer = new JTextField (40);
      gridbag.setConstraints (nameOfCustomer, c);
      addCustomer.add (nameOfCustomer);

      //
      // E-MAIL ADDRESS label and text field 
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel emailLabel = new JLabel ("E-mail address:", JLabel.RIGHT);
      gridbag.setConstraints (emailLabel, c);
      addCustomer.add (emailLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField emailOfCustomer2 = new JTextField (40);
      gridbag.setConstraints (emailOfCustomer2, c);
      addCustomer.add (emailOfCustomer2);

      //
      // PHONE NUMBER label and text field 
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel phoneLabel = new JLabel ("Phone number:", JLabel.RIGHT);
      gridbag.setConstraints (phoneLabel, c);
      addCustomer.add (phoneLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField phoneOfCustomer = new JTextField (20);
      gridbag.setConstraints (phoneOfCustomer, c);
      addCustomer.add (phoneOfCustomer);

      //
      // MAILING ADDRESS (3) labels and (3) textfields
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel mailLabel = new JLabel ("Mailing Address:", JLabel.RIGHT);
      gridbag.setConstraints (mailLabel, c);
      addCustomer.add (mailLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField mailingAddressOfCustomer1 = new JTextField (40);
      gridbag.setConstraints (mailingAddressOfCustomer1, c);
      addCustomer.add (mailingAddressOfCustomer1);
      // ....
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel emptyLabelOne = new JLabel (" ", JLabel.RIGHT);
      gridbag.setConstraints (emptyLabelOne, c);
      addCustomer.add (emptyLabelOne);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField mailingAddressOfCustomer2 = new JTextField (40);
      gridbag.setConstraints (mailingAddressOfCustomer2, c);
      addCustomer.add (mailingAddressOfCustomer2);
      // ....
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel emptyLabelTwo = new JLabel (" ", JLabel.RIGHT);
      gridbag.setConstraints (emptyLabelTwo, c);
      addCustomer.add (emptyLabelTwo);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField mailingAddressOfCustomer3 = new JTextField (40);
      gridbag.setConstraints (mailingAddressOfCustomer3, c);
      addCustomer.add (mailingAddressOfCustomer3);

      //
      // AGE label and age combo
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel ageLabel = new JLabel ("Age:", JLabel.RIGHT);
      gridbag.setConstraints (ageLabel, c);
      addCustomer.add (ageLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final String ageGroups[] = {"0-9", 
                                  "10-19", 
                                  "20-29", 
                                  "30-39", 
                                  "40-49", 
                                  "50+"};
      final JComboBox ageCombo = new JComboBox (ageGroups);
      gridbag.setConstraints (ageCombo, c);
      addCustomer.add (ageCombo);

      //
      // SEX label and combo box
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel sexLabel = new JLabel ("Sex:", JLabel.RIGHT);
      gridbag.setConstraints (sexLabel, c);
      addCustomer.add (sexLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final String sexGroups[] = {"Female", 
                                  "Male"};
      final JComboBox sexCombo = new JComboBox (sexGroups);
      gridbag.setConstraints (sexCombo, c);
      addCustomer.add (sexCombo);

      //
      // MONEY SPENT label and text box
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel moneyLabel = new JLabel ("Credit Card:", JLabel.RIGHT);
      gridbag.setConstraints (moneyLabel, c);
      addCustomer.add (moneyLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField moneySpentByCustomer = new JTextField (10);
      gridbag.setConstraints (moneySpentByCustomer, c);
      addCustomer.add (moneySpentByCustomer);

      //
      // DATE OF LAST PURCHASE MADE label and textbox 
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel dateLabel = new JLabel ("Date of last purchase:", JLabel.RIGHT);
      gridbag.setConstraints (dateLabel, c);
      addCustomer.add (dateLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField dateOfLastPurchase = new JTextField (20);
      gridbag.setConstraints (dateOfLastPurchase, c);
      addCustomer.add (dateOfLastPurchase);

      //
      // PERMISSION TO SEND PROMOS label and combo box 
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel permissionLabel = new JLabel ("Receive promos:", JLabel.RIGHT);
      gridbag.setConstraints (permissionLabel, c);
      addCustomer.add (permissionLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final String perOptions[] = {"Yes", 
                                   "No"};
      final JComboBox perCombo = new JComboBox (perOptions);
      gridbag.setConstraints (perCombo, c);
      addCustomer.add (perCombo);

      // 
      // ADD BUTTON 
      //
      c.insets = new Insets (5, 5, 5, 5);
      c.anchor = GridBagConstraints.CENTER;
      c.ipadx = 10;
      c.ipady = 10;
      JButton addButton = new JButton ("Subscribe");
      c.gridwidth = GridBagConstraints.REMAINDER;
      gridbag.setConstraints (addButton, c);
      addCustomer.add (addButton);
   
      // 
      // ACTION LISTENER FOR "ADD BUTTON"
      //
      addButton.addActionListener (new ActionListener() 
         {
            public void actionPerformed (ActionEvent e)
            {
					String newE = new String();
				
				   // quirky thing, textboxes must contain something (ie 1 blank)
					if (surnameOfCustomer.getText().equals(""))
					   surnameOfCustomer.setText(" ");
					if (nameOfCustomer.getText().equals(""))
					   nameOfCustomer.setText(" ");
					if (emailOfCustomer2.getText().equals(""))
					   emailOfCustomer2.setText(" ");
					if (phoneOfCustomer.getText().equals(""))
					   phoneOfCustomer.setText(" ");
					if (mailingAddressOfCustomer1.getText().equals(""))
					   mailingAddressOfCustomer1.setText(" ");
					if (mailingAddressOfCustomer2.getText().equals(""))
					   mailingAddressOfCustomer2.setText(" ");
					if (mailingAddressOfCustomer3.getText().equals(""))
					   mailingAddressOfCustomer3.setText(" ");
					if (moneySpentByCustomer.getText().equals(""))
					   moneySpentByCustomer.setText(" ");
					if (dateOfLastPurchase.getText().equals(""))
					   dateOfLastPurchase.setText(" ");
						
               // gather the data from sources
               newE += (surnameOfCustomer.getText()) + ";";
               newE += (nameOfCustomer.getText()) + ";";
               newE += (emailOfCustomer2.getText()) + ";";
               newE += (phoneOfCustomer.getText()) + ";";
               newE += (mailingAddressOfCustomer1.getText()) + ";";
               newE += (mailingAddressOfCustomer2.getText()) + ";";
               newE += (mailingAddressOfCustomer3.getText()) + ";";
               newE += (ageGroups [ageCombo.getSelectedIndex()]) + ";";
               newE += (sexGroups [sexCombo.getSelectedIndex()]) + ";";
               newE += (moneySpentByCustomer.getText()) + ";";
               newE += (dateOfLastPurchase.getText()) + ";";
               newE += (perOptions [perCombo.getSelectedIndex()]);
               //newE += (perOptions [perCombo.getSelectedIndex()]) + ";";
					
					emailOfCustomer = new String (emailOfCustomer2.getText());
 
               // find the old entry in the tree (and then delete it)
					String sendToServer = new String();
					sendToServer = "c_fnd_" + emailOfCustomer + ";";
					String receiveFromServer = new String();
					receiveFromServer = talkToServer (sendToServer);
               
               // if nothing is returned, clear the result boxes
               if ( (receiveFromServer.equals ("_F"))) 
					{
					   sendToServer = new String();
					   sendToServer = "c_add_" + newE;
					   receiveFromServer = new String();
					   receiveFromServer = talkToServer (sendToServer);
                  
						if ( ! (receiveFromServer.equals ("_F"))) 
					   {
						   Random randomGenerator = new Random();
							int randomInt = randomGenerator.nextInt();
							if (randomInt < 0)
							{
							   randomInt = randomInt * (-1);
							}
							Integer tempInt = new Integer (randomInt);
						   String password = new String(tempInt.toString());
						   String message = new String();
							message += "Your profile has been added.\n" +
							           "Your 'login' is your E-mail address.\n" +
										  "And your 'password' is " +
										  password +
										  ".\n";
                     JOptionPane x = new JOptionPane();
                     x.showMessageDialog (addCustomer, 
                                          message, 
                                          "Subscribe Status.", 
                                          JOptionPane.INFORMATION_MESSAGE);

                     // clear all the input fields
                     surnameOfCustomer.setText("");
                     nameOfCustomer.setText("");
                     emailOfCustomer2.setText("");
                     phoneOfCustomer.setText("");
                     mailingAddressOfCustomer1.setText("");
                     mailingAddressOfCustomer2.setText("");
                     mailingAddressOfCustomer3.setText("");
                     ageCombo.setSelectedIndex(0);
                     sexCombo.setSelectedIndex(0);
                     moneySpentByCustomer.setText("");
                     dateOfLastPurchase.setText("");
                     perCombo.setSelectedIndex(0);
					      
							sendToServer = new String();
					      sendToServer = "c_wrt_" + ";";
					      receiveFromServer = new String();
					      receiveFromServer = talkToServer (sendToServer);
                  
					      sendToServer = new String();
					      sendToServer = "l_add_" + emailOfCustomer + ";" + password + ";";
					      receiveFromServer = new String();
					      receiveFromServer = talkToServer (sendToServer);
                  }
						else
						{
                     JOptionPane x = new JOptionPane();
                     x.showMessageDialog (addCustomer, 
                                          "Unable to subcribe new user.", 
                                          "Subscribe Status.", 
                                          JOptionPane.ERROR_MESSAGE);
						}
					}
					else
					{
                  JOptionPane x = new JOptionPane();
                  x.showMessageDialog (addCustomer, 
                                       "Enter a different E-mail address.", 
                                       "Subscribe Status.", 
                                       JOptionPane.ERROR_MESSAGE);
					}
					
			      sendToServer = new String();
			      sendToServer = "c_rea_" + ";";
			      receiveFromServer = new String();
			      receiveFromServer = talkToServer (sendToServer);
			   }
         } 
      ); // end of saveButton LISTENER
   }


   // ------------------------------------
   // Create Modify Tab
   // 
   private void buildModifyTab() 
   {
      // setup grid bag layout manager
      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();
      modifyCustomer.setLayout (gridbag);

      // common attributes
      c.fill = GridBagConstraints.NONE;
      
      //
      // RETRIEVE BUTTON
      // 
      c.insets = new Insets (5, 5, 5, 5);
      c.anchor = GridBagConstraints.CENTER;
      c.ipadx = 10;
      c.ipady = 10;
      JButton loadButton = new JButton ("Load present settings");
      c.gridwidth = GridBagConstraints.REMAINDER;
      gridbag.setConstraints (loadButton, c);
      modifyCustomer.add (loadButton);
     
	   // common to other labels/textboxes
      c.insets = new Insets (2, 2, 2, 2);
      c.ipadx = 0;
      c.ipady = 0;
		
      //
      // SURNAME label and textbox 
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel surnameLabel2 = new JLabel ("Surname:", JLabel.RIGHT);
      gridbag.setConstraints (surnameLabel2, c);
      modifyCustomer.add (surnameLabel2);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField surnameOfCustomer2 = new JTextField (40);
      gridbag.setConstraints (surnameOfCustomer2, c);
      modifyCustomer.add (surnameOfCustomer2);

      //
      // NAME label and textbox 
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel nameLabel2 = new JLabel ("Given name:", JLabel.RIGHT);
      gridbag.setConstraints (nameLabel2, c);
      modifyCustomer.add (nameLabel2);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField nameOfCustomer2 = new JTextField (40);
      gridbag.setConstraints (nameOfCustomer2, c);
      modifyCustomer.add (nameOfCustomer2);

      //
      // E-MAIL ADDRESS label and textbox 
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel emailLabel2 = new JLabel ("E-mail address:", JLabel.RIGHT);
      gridbag.setConstraints (emailLabel2, c);
      modifyCustomer.add (emailLabel2);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField emailOfCustomer2 = new JTextField (40);
      gridbag.setConstraints (emailOfCustomer2, c);
      modifyCustomer.add (emailOfCustomer2);

      //
      // PHONE NUMBER label and textbox 
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel phoneLabel = new JLabel ("Phone number:", JLabel.RIGHT);
      gridbag.setConstraints (phoneLabel, c);
      modifyCustomer.add (phoneLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField phoneOfCustomer = new JTextField (20);
      gridbag.setConstraints (phoneOfCustomer, c);
      modifyCustomer.add (phoneOfCustomer);

      //
      // MAILING ADDRESS (3) labels and (3) textboxes
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel mailLabel = new JLabel ("Mailing Address:", JLabel.RIGHT);
      gridbag.setConstraints (mailLabel, c);
      modifyCustomer.add (mailLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField mailingAddressOfCustomer1 = new JTextField (40);
      gridbag.setConstraints (mailingAddressOfCustomer1, c);
      modifyCustomer.add (mailingAddressOfCustomer1);
      // ....
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel emptyLabelOne = new JLabel (" ", JLabel.RIGHT);
      gridbag.setConstraints (emptyLabelOne, c);
      modifyCustomer.add (emptyLabelOne);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField mailingAddressOfCustomer2 = new JTextField (40);
      gridbag.setConstraints (mailingAddressOfCustomer2, c);
      modifyCustomer.add (mailingAddressOfCustomer2);
      // ....
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel emptyLabelTwo = new JLabel (" ", JLabel.RIGHT);
      gridbag.setConstraints (emptyLabelTwo, c);
      modifyCustomer.add (emptyLabelTwo);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField mailingAddressOfCustomer3 = new JTextField (40);
      gridbag.setConstraints (mailingAddressOfCustomer3, c);
      modifyCustomer.add (mailingAddressOfCustomer3);

      //
      // AGE label and combobox
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel ageLabel = new JLabel ("Age:", JLabel.RIGHT);
      gridbag.setConstraints (ageLabel, c);
      modifyCustomer.add (ageLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final String ageGroups[] = {"0-9", 
                                  "10-19", 
                                  "20-29", 
                                  "30-39", 
                                  "40-49", 
                                  "50+"};
      final JComboBox ageCombo = new JComboBox (ageGroups);
      gridbag.setConstraints (ageCombo, c);
      modifyCustomer.add (ageCombo);

      //
      // SEX label and combobox
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel sexLabel = new JLabel ("Sex:", JLabel.RIGHT);
      gridbag.setConstraints (sexLabel, c);
      modifyCustomer.add (sexLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final String sexGroups[] = {"Female", 
                                  "Male"};
      final JComboBox sexCombo = new JComboBox (sexGroups);
      gridbag.setConstraints (sexCombo, c);
      modifyCustomer.add (sexCombo);

      //
      // MONEY SPENT label and textbox
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel moneyLabel = new JLabel ("Credit Card:", JLabel.RIGHT);
      gridbag.setConstraints (moneyLabel, c);
      modifyCustomer.add (moneyLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField moneySpentByCustomer = new JTextField (10);
      gridbag.setConstraints (moneySpentByCustomer, c);
      modifyCustomer.add (moneySpentByCustomer);

      //
      // DATE OF LAST PURCHASE MADE label and textbox
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel dateLabel = new JLabel ("Date of last purchase:", JLabel.RIGHT);
      gridbag.setConstraints (dateLabel, c);
      modifyCustomer.add (dateLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField dateOfLastPurchase = new JTextField (20);
      gridbag.setConstraints (dateOfLastPurchase, c);
      modifyCustomer.add (dateOfLastPurchase);

      //
      // PERMISSION TO SEND PROMOS label and combobox
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel permissionLabel = new JLabel ("Receive promos:", JLabel.RIGHT);
      gridbag.setConstraints (permissionLabel, c);
      modifyCustomer.add (permissionLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final String perOptions[] = {"Yes", 
                                    "No"};
      final JComboBox perCombo = new JComboBox (perOptions);
      gridbag.setConstraints (perCombo, c);
      modifyCustomer.add (perCombo);

      //
      // SAVE BUTTON
      // 
      c.insets = new Insets (5, 5, 5, 5);
      c.anchor = GridBagConstraints.CENTER;
      c.ipadx = 10;
      c.ipady = 10;
      JButton saveButton = new JButton ("Save");
      c.gridwidth = GridBagConstraints.REMAINDER;
      gridbag.setConstraints (saveButton, c);
      modifyCustomer.add (saveButton);
    
      JButton deleteButton = new JButton ("Delete Myself!");
      c.gridwidth = GridBagConstraints.REMAINDER;
      gridbag.setConstraints (deleteButton, c);
      modifyCustomer.add (deleteButton);
    
 
      //
      // BUTTON ACTION LISTENER
      //
      saveButton.addActionListener (new ActionListener() 
         {
            public void actionPerformed (ActionEvent e)
            {
               // will contain data from textboxes and comboboxes 
               Vector newEntry = new Vector();
					String newE = new String();
				
				   // quirky thing, textboxes must contain something (ie 1 blank)
					if (surnameOfCustomer2.getText().equals(""))
					   surnameOfCustomer2.setText(" ");
					if (nameOfCustomer2.getText().equals(""))
					   nameOfCustomer2.setText(" ");
					if (emailOfCustomer2.getText().equals(""))
					   emailOfCustomer2.setText(" ");
					if (phoneOfCustomer.getText().equals(""))
					   phoneOfCustomer.setText(" ");
					if (mailingAddressOfCustomer1.getText().equals(""))
					   mailingAddressOfCustomer1.setText(" ");
					if (mailingAddressOfCustomer2.getText().equals(""))
					   mailingAddressOfCustomer2.setText(" ");
					if (mailingAddressOfCustomer3.getText().equals(""))
					   mailingAddressOfCustomer3.setText(" ");
					if (moneySpentByCustomer.getText().equals(""))
					   moneySpentByCustomer.setText(" ");
					if (dateOfLastPurchase.getText().equals(""))
					   dateOfLastPurchase.setText(" ");
						
               // gather the data from sources
               newE += (surnameOfCustomer2.getText()) + ";";
               newE += (nameOfCustomer2.getText()) + ";";
               newE += (emailOfCustomer2.getText()) + ";";
               newE += (phoneOfCustomer.getText()) + ";";
               newE += (mailingAddressOfCustomer1.getText()) + ";";
               newE += (mailingAddressOfCustomer2.getText()) + ";";
               newE += (mailingAddressOfCustomer3.getText()) + ";";
               newE += (ageGroups [ageCombo.getSelectedIndex()]) + ";";
               newE += (sexGroups [sexCombo.getSelectedIndex()]) + ";";
               newE += (moneySpentByCustomer.getText()) + ";";
               newE += (dateOfLastPurchase.getText()) + ";";
               newE += (perOptions [perCombo.getSelectedIndex()]);
               //newE += (perOptions [perCombo.getSelectedIndex()]) + ";";

               // find the old entry in the tree (and then delete it)
					String sendToServer = new String();
					sendToServer = "c_fnd_" + emailOfCustomer + ";";
					String receiveFromServer = new String();
					receiveFromServer = talkToServer (sendToServer);
               
               // if nothing is returned, clear the result boxes
               if ( ! (receiveFromServer.equals ("_F"))) 
					{
					   sendToServer = new String();
					   sendToServer = "c_del_" + receiveFromServer;
					   String receiveFromServer2 = new String();
					   receiveFromServer2 = talkToServer (sendToServer);

                  if ( ! (receiveFromServer2.equals ("_F"))) 
					   {
					      sendToServer = new String();
					      sendToServer = "c_add_" + newE;
					      receiveFromServer2 = new String();
					      receiveFromServer2 = talkToServer (sendToServer);
                  
						   if ( ! (receiveFromServer2.equals ("_F"))) 
					      {
                        JOptionPane x = new JOptionPane();
                        x.showMessageDialog (modifyCustomer, 
                                             "Your profile has been changed.", 
                                             "Change Status.", 
                                             JOptionPane.INFORMATION_MESSAGE);

                        // clear all the input fields
                        surnameOfCustomer2.setText("");
                        nameOfCustomer2.setText("");
                        emailOfCustomer2.setText("");
                        phoneOfCustomer.setText("");
                        mailingAddressOfCustomer1.setText("");
                        mailingAddressOfCustomer2.setText("");
                        mailingAddressOfCustomer3.setText("");
                        ageCombo.setSelectedIndex(0);
                        sexCombo.setSelectedIndex(0);
                        moneySpentByCustomer.setText("");
                        dateOfLastPurchase.setText("");
                        perCombo.setSelectedIndex(0);
					      
							   sendToServer = new String();
					         sendToServer = "c_wrt_" + ";";
					         receiveFromServer2 = new String();
					         receiveFromServer2 = talkToServer (sendToServer);
                  
                     }
							else
							{
                        JOptionPane x = new JOptionPane();
                        x.showMessageDialog (modifyCustomer, 
                                             "Could not perform changes.", 
                                             "Change Status.", 
                                             JOptionPane.ERROR_MESSAGE);
							}
						}
						else
						{
                     JOptionPane x = new JOptionPane();
                     x.showMessageDialog (modifyCustomer, 
                                          "Could not perform changes.", 
                                          "Change Status.", 
                                          JOptionPane.ERROR_MESSAGE);
						}
					}
					else
					{
                  JOptionPane x = new JOptionPane();
                  x.showMessageDialog (modifyCustomer, 
                                       "Could not perform changes.", 
                                       "Change Status.", 
                                       JOptionPane.ERROR_MESSAGE);
					}
				   sendToServer = new String();
				   sendToServer = "c_rea_" + ";";
				   receiveFromServer = new String();
				   receiveFromServer = talkToServer (sendToServer);
            }        
         } 
      ); // end of saveButton LISTENER


      //
      // DELETE BUTTON ACTION LISTENER
      //
      deleteButton.addActionListener (new ActionListener() 
         {
            public void actionPerformed (ActionEvent e)
            {
					String sendToServer = new String();
					sendToServer = "c_fnd_" + emailOfCustomer + ";";
					String receiveFromServer = new String();
					receiveFromServer = talkToServer (sendToServer);

               // if nothing is returned, clear the result boxes
               if ( ! (receiveFromServer.equals ("_F"))) 
					{
					   sendToServer = new String();
					   sendToServer = "c_del_" + receiveFromServer;
					   String receiveFromServer2 = new String();
					   receiveFromServer2 = talkToServer (sendToServer);

                  if ( ! (receiveFromServer2.equals ("_F"))) 
					   {
                     JOptionPane x = new JOptionPane();
                     x.showMessageDialog (modifyCustomer, 
                                          "Your profile has been deleted!.", 
                                          "Change Status.", 
                                          JOptionPane.INFORMATION_MESSAGE);

                     // clear all the input fields
                     surnameOfCustomer2.setText("");
                     nameOfCustomer2.setText("");
                     emailOfCustomer2.setText("");
                     phoneOfCustomer.setText("");
                     mailingAddressOfCustomer1.setText("");
                     mailingAddressOfCustomer2.setText("");
                     mailingAddressOfCustomer3.setText("");
                     ageCombo.setSelectedIndex(0);
                     sexCombo.setSelectedIndex(0);
                     moneySpentByCustomer.setText("");
                     dateOfLastPurchase.setText("");
                     perCombo.setSelectedIndex(0);
					      
						   sendToServer = new String();
				         sendToServer = "c_wrt_" + ";";
				         receiveFromServer2 = new String();
				         receiveFromServer2 = talkToServer (sendToServer);
                
                  }
						else
						{
                     JOptionPane x = new JOptionPane();
                     x.showMessageDialog (modifyCustomer, 
                                          "Could not perform changes.", 
                                          "Change Status.", 
                                          JOptionPane.ERROR_MESSAGE);
						}
               }
            }
         } 
      ); // end of saveButton LISTENER

      //
      // LOAD BUTTON ACTION LISTENER
      //
      loadButton.addActionListener (new ActionListener() 
         {
            public void actionPerformed (ActionEvent e)
            {
					String sendToServer = new String();
					sendToServer = "c_fnd_" + emailOfCustomer + ";";
					String receiveFromServer = new String();
					receiveFromServer = talkToServer (sendToServer);
               
               // if nothing is returned, clear the result boxes
               if (receiveFromServer.equals ("_F")) 
               {
                  surnameOfCustomer2.setText("");
                  nameOfCustomer2.setText("");
                  emailOfCustomer2.setText("");
                  phoneOfCustomer.setText("");
                  mailingAddressOfCustomer1.setText("");
                  mailingAddressOfCustomer2.setText("");
                  mailingAddressOfCustomer3.setText("");
                  ageCombo.setSelectedIndex(0);
                  sexCombo.setSelectedIndex(0);
                  moneySpentByCustomer.setText("");
                  dateOfLastPurchase.setText("");
                  perCombo.setSelectedIndex(0);
               }
               else
               {
                  // otherwise, take the result and apply the StringTokenizer
                  // take each next token and display in the fields
                  StringTokenizer t = new StringTokenizer (receiveFromServer, ";");
                  surnameOfCustomer2.setText (t.nextToken());
                  nameOfCustomer2.setText (t.nextToken());
                  emailOfCustomer2.setText (t.nextToken());
                  phoneOfCustomer.setText (t.nextToken());
                  mailingAddressOfCustomer1.setText (t.nextToken());
                  mailingAddressOfCustomer2.setText (t.nextToken());
                  mailingAddressOfCustomer3.setText (t.nextToken());
                  ageCombo.setSelectedItem (t.nextToken());
                  sexCombo.setSelectedItem (t.nextToken());
                  moneySpentByCustomer.setText (t.nextToken());
                  dateOfLastPurchase.setText (t.nextToken());
                  perCombo.setSelectedItem (t.nextToken());
               }
            }
         }
      ); // end of findButton LISTENER

   }


   public static void mail(String to, String message) 
	{
      try {
		   System.getProperties().put("mail.host", "snowhite.cis.uoguelph.ca");
         // A Reader stream to read from the console
         BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

         // Establish a network connection for sending mail
         URL u = new URL("mailto:" + to);      // Create a mailto: URL 
         URLConnection c = u.openConnection(); // Create a URLConnection for it
         c.setDoInput(false);                  // Specify no input from this URL
         c.setDoOutput(true);                  // Specify we'll do output
         c.connect();                          // Connect to mail host
         PrintWriter out =                     // Get output stream to mail host
           new PrintWriter(new OutputStreamWriter(c.getOutputStream()));

         // Write out mail headers.  Don't let users fake the From address
         out.println("From: info@buy.com"); 
         out.println("To: " + to);
         out.println("Subject: Information");
         out.println();  // blank line to end the list of headers

         out.print (message);

         // Close the stream to terminate the message 
         out.close();
      }
      catch (Exception e) 
		{  // Handle any exceptions, print error message.
         System.out.println ("Could not send e-mail");
      }
   }


   private static String talkToServer (String sentenceToServer)  
	{
	   try {
		   // get a datagram socket
         DatagramSocket socket = new DatagramSocket();

         // send request
         byte[] buf = new byte[256];

			// fix the sentenceToServer 
			for (int i = sentenceToServer.length(); i < buf.length; i++)
		      sentenceToServer += " ";
			
		   String s = new String (sentenceToServer);
		   buf = s.getBytes();
         //InetAddress address = InetAddress.getByName(args[0]);
		   InetAddress address = InetAddress.getLocalHost();
         DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
         //DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
         socket.send(packet);

         // get response
			buf = new byte[256];
         packet = new DatagramPacket(buf, buf.length);
         socket.receive(packet);

         // display response
         String received = new String(packet.getData());
         //String received = new String(packet.getData(), 0);

         socket.close();

			return received.trim();
		} 
		catch (Exception e)
		{
		   System.out.println ("Unable to open Socket");
		}
		
		return new String();
   }
   

   //
   // CONSTRUCTOR 
   //
   public ClientGUI() 
   {
      // create instance of object, read data into tree
      customerData = new DatabaseInterface("customer.txt");

		emailOfCustomer = new String ();
      
      // build the tabs
		buildMainMenuTab();
		buildLoginMenuTab();
      buildCustomerTab();
		buildPurchaseItemsTab();
		buildChangeLoginTab();
      buildModifyTab();
      
		// create a tabbedPane and add the tabs
		tabbedPane = new JTabbedPane();
      tabbedPane.add ("Home", mainMenu);
      tabbedPane.add ("Login", loginMenu);
      tabbedPane.add ("Subscribe", addCustomer);
      tabbedPane.add ("Browse Product", purchaseItems);
      tabbedPane.add ("Modify", modifyCustomer);
      tabbedPane.add ("Change Password", changeLogin);

      tabbedPane.setEnabledAt (4, false);
      tabbedPane.setEnabledAt (5, false);

      // add the tabbedPane
      tabbedPane.setPreferredSize (new Dimension (800, 600));
      add (tabbedPane);
   }

   //
   // CLASS DESTRUCTOR
   //
   protected void finalize ()
   {
      // write stuff to file and display error if errors exist
      int value = customerData.writeDatabase();
      if (value != 0)
      {
         JOptionPane y = new JOptionPane();
         y.showMessageDialog (addCustomer, 
                              "Could not save all the changes to database.", 
                              "Database Save Failed", 
                               JOptionPane.ERROR_MESSAGE);
      }
   }


   //
   // MAIN !!!!!!
   //
   public static void main(String s[]) 
   {
      // GIVE HER!
      ClientGUI panel = new ClientGUI();
   
      // display the main JFRAME
      frame = new JFrame("Customer Manager");
      frame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {System.exit(0);} });
      frame.getContentPane().add("Center", panel);
      frame.pack();
      frame.setVisible(true);
   
   }
}
