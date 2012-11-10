// Michael Bochenek (ID) 0041056
// CIS 3430 Assignment #1

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import java.util.StringTokenizer;
import java.io.*;
import java.net.*;

public class RemoteCustomerManager extends JPanel {
   // main frame that holds everything, ie main window
   static JFrame frame;

   // customerData is the tree for all the customers
   private RemoteDatabaseInterface customerData; 

   JTabbedPane tabbedPane = new JTabbedPane();
  
   // 5 panels, one for each function
	private JPanel loginMenu = new JPanel();
   private JPanel addCustomer = new JPanel();
   private JPanel modifyCustomer = new JPanel();
   private JPanel deleteCustomer = new JPanel();
   private JPanel queryCustomer = new JPanel();
   private JPanel emailCustomer = new JPanel();


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
      JLabel nameLabel = new JLabel ("Login", JLabel.RIGHT);
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
               tabbedPane.setEnabledAt (1, false);
               tabbedPane.setEnabledAt (2, false);
               tabbedPane.setEnabledAt (3, false);
               tabbedPane.setEnabledAt (4, false);
               tabbedPane.setEnabledAt (5, false);

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
               
					if (email.equals ("admin") && password.equals ("cis343"))
					{
					   // the entry was found in the database
                  JOptionPane x = new JOptionPane();
                  x.showMessageDialog (loginMenu, 
                                       "Login Success.", 
                                       "Login Status", 
                                       JOptionPane.INFORMATION_MESSAGE);
                
                  tabbedPane.setEnabledAt (1, true);
                  tabbedPane.setEnabledAt (2, true);
                  tabbedPane.setEnabledAt (3, true);
                  tabbedPane.setEnabledAt (4, true);
                  tabbedPane.setEnabledAt (5, true);

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
      final JTextField emailOfCustomer = new JTextField (40);
      gridbag.setConstraints (emailOfCustomer, c);
      addCustomer.add (emailOfCustomer);

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
      JButton addButton = new JButton ("Add");
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
               // will gather all the input from the input boxes and combos   
               Vector newEntry = new Vector();
              
               // gather all the data...
               newEntry.addElement (surnameOfCustomer.getText());
               newEntry.addElement (nameOfCustomer.getText());
               newEntry.addElement (emailOfCustomer.getText());
               newEntry.addElement (phoneOfCustomer.getText());
               newEntry.addElement (mailingAddressOfCustomer1.getText());
               newEntry.addElement (mailingAddressOfCustomer2.getText());
               newEntry.addElement (mailingAddressOfCustomer3.getText());
               newEntry.addElement (ageGroups [ageCombo.getSelectedIndex()]);
               newEntry.addElement (sexGroups [sexCombo.getSelectedIndex()]);
               newEntry.addElement (moneySpentByCustomer.getText());
               newEntry.addElement (dateOfLastPurchase.getText());
               newEntry.addElement (perOptions [perCombo.getSelectedIndex()]);

               // add this element to the tree
               int value = customerData.addEntry (newEntry);
               
               // if value returned is not 0, display an error message
               if (value != 0)
               {
                  JOptionPane x = new JOptionPane();
                  x.showMessageDialog (addCustomer, 
                                       "The customer could not be added.", 
                                       "Customer Add Error", 
                                       JOptionPane.ERROR_MESSAGE);
               }
               else
               {
                  // write the informarion to the database
                  value = customerData.writeDatabase();
               
                  // if value returned is not 0, display an error message
                  if (value != 0)
                  {
                     JOptionPane x = new JOptionPane();
                     x.showMessageDialog (addCustomer, 
                                          "The changes could not be saved.", 
                                          "Database Update Failed", 
                                          JOptionPane.ERROR_MESSAGE);
                  }
                  else
                  {  
                     JOptionPane x = new JOptionPane();
                     x.showMessageDialog (addCustomer, 
                                          "The customer has been added.", 
                                          "Database Update Successful", 
                                          JOptionPane.INFORMATION_MESSAGE);

                     // clear all the text boxes and reset combo boxes
                     surnameOfCustomer.setText("");
                     nameOfCustomer.setText("");
                     emailOfCustomer.setText("");
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
               }
         
            }        
         } 
      ); // end of addButton LISTENER

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
      c.insets = new Insets (2, 2, 2, 2);

      //
      // SURNAME label and textfield 
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel surnameLabel = new JLabel ("Surname:", JLabel.RIGHT);
      gridbag.setConstraints (surnameLabel, c);
      modifyCustomer.add (surnameLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField surnameOfCustomer = new JTextField (40);
      gridbag.setConstraints (surnameOfCustomer, c);
      modifyCustomer.add (surnameOfCustomer);

      //
      // NAME label and text field 
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel nameLabel = new JLabel ("Given name:", JLabel.RIGHT);
      gridbag.setConstraints (nameLabel, c);
      modifyCustomer.add (nameLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField nameOfCustomer = new JTextField (40);
      gridbag.setConstraints (nameOfCustomer, c);
      modifyCustomer.add (nameOfCustomer);

      //
      // E-MAIL ADDRESS label and text field 
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel emailLabel = new JLabel ("E-mail address:", JLabel.RIGHT);
      gridbag.setConstraints (emailLabel, c);
      modifyCustomer.add (emailLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField emailOfCustomer = new JTextField (40);
      gridbag.setConstraints (emailOfCustomer, c);
      modifyCustomer.add (emailOfCustomer);

      //
      // FIND BUTTON
      //
      c.insets = new Insets (15, 15, 15, 15);
      c.anchor = GridBagConstraints.CENTER;
      c.ipadx = 10;
      c.ipady = 10;
      JButton findButton = new JButton ("Find");
      c.gridwidth = GridBagConstraints.REMAINDER;
      gridbag.setConstraints (findButton, c);
      modifyCustomer.add (findButton);

      // stuff common to all the other labels and textboxes
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
     
      //
      // SAVE BUTTON ACTION LISTENER
      //
      saveButton.addActionListener (new ActionListener() 
         {
            public void actionPerformed (ActionEvent e)
            {
               // will contain data from textboxes and comboboxes 
               Vector newEntry = new Vector();
               
               // gather the data from sources
               newEntry.addElement (surnameOfCustomer2.getText());
               newEntry.addElement (nameOfCustomer2.getText());
               newEntry.addElement (emailOfCustomer2.getText());
               newEntry.addElement (phoneOfCustomer.getText());
               newEntry.addElement (mailingAddressOfCustomer1.getText());
               newEntry.addElement (mailingAddressOfCustomer2.getText());
               newEntry.addElement (mailingAddressOfCustomer3.getText());
               newEntry.addElement (ageGroups [ageCombo.getSelectedIndex()]);
               newEntry.addElement (sexGroups [sexCombo.getSelectedIndex()]);
               newEntry.addElement (moneySpentByCustomer.getText());
               newEntry.addElement (dateOfLastPurchase.getText());
               newEntry.addElement (perOptions [perCombo.getSelectedIndex()]);

               // find the old entry in the tree (and then delete it)
               Vector queryResults = new Vector();
               queryResults = customerData.queryEntryByThreeFields (
                  surnameOfCustomer.getText(),
                  nameOfCustomer.getText(),
                  emailOfCustomer.getText());

               if (queryResults.size() != 0)
               {
                  // it will always be able to remove the entry
                  // since we just found it in the tree
                  int value = customerData.removeEntry ((String) queryResults.elementAt(0));

                  // add the newer (modified) entry
                  value = customerData.addEntry (newEntry);

                  // display error message if necessary
                  if (value != 0)
                  {
                     JOptionPane x = new JOptionPane();
                     x.showMessageDialog (modifyCustomer, 
                                          "The customer could not be added.", 
                                          "Customer Add Error", 
                                          JOptionPane.ERROR_MESSAGE);
                  }
               
                  // write the changes to the database
                  value = customerData.writeDatabase();
                  if (value != 0)
                  {
                     JOptionPane x = new JOptionPane();
                     x.showMessageDialog (modifyCustomer, 
                                          "The changes could not be saved.", 
                                          "Database Update Failed", 
                                          JOptionPane.ERROR_MESSAGE);
                  }
                  else
                  {  
                     JOptionPane x = new JOptionPane();
                     x.showMessageDialog (modifyCustomer, 
                                          "The customer has been modified.", 
                                          "Database Modify Successful", 
                                          JOptionPane.INFORMATION_MESSAGE);

                     // clear all the input fields
                     surnameOfCustomer.setText("");
                     nameOfCustomer.setText("");
                     emailOfCustomer.setText("");
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
               }
         
            }        
         } 
      ); // end of saveButton LISTENER

      //
      // FIND BUTTON ACTION LISTENER
      //
      findButton.addActionListener (new ActionListener() 
         {
            public void actionPerformed (ActionEvent e)
            {
               // results of the find in database will be store here
               Vector queryResults = new Vector();

               // perform the actual query
               queryResults = customerData.queryEntryByThreeFields (
                  surnameOfCustomer.getText(),
                  nameOfCustomer.getText(),
                  emailOfCustomer.getText());

               String queryResultsString = new String();
               
               // if nothing is returned in vector, clear the result boxes
               if (queryResults.size() == 0) 
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
                  StringTokenizer t = new StringTokenizer ((String) queryResults.elementAt(0), ";");
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


   // ------------------------------------
   // Create Query Tab
   // 
   private void buildQueryTab() {
      // setup gridbag layout for this panel
      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();
      queryCustomer.setLayout (gridbag);

      // common attributes
      c.fill = GridBagConstraints.NONE;
      c.insets = new Insets (2, 2, 2, 2);

      //
      // SURNAME label and textfield 
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel surnameLabel = new JLabel ("Surname:", JLabel.RIGHT);
      gridbag.setConstraints (surnameLabel, c);
      queryCustomer.add (surnameLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField surnameOfCustomer = new JTextField (40);
      gridbag.setConstraints (surnameOfCustomer, c);
      queryCustomer.add (surnameOfCustomer);

      //
      // NAME label and text field
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel nameLabel = new JLabel ("Given name:", JLabel.RIGHT);
      gridbag.setConstraints (nameLabel, c);
      queryCustomer.add (nameLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField nameOfCustomer = new JTextField (40);
      gridbag.setConstraints (nameOfCustomer, c);
      queryCustomer.add (nameOfCustomer);

      //
      // E-MAIL ADDRESS label and textfield 
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel emailLabel = new JLabel ("E-mail address:", JLabel.RIGHT);
      gridbag.setConstraints (emailLabel, c);
      queryCustomer.add (emailLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField emailOfCustomer = new JTextField (40);
      gridbag.setConstraints (emailOfCustomer, c);
      queryCustomer.add (emailOfCustomer);

      // 
      // QueryButton added here
      //
      c.insets = new Insets (5, 5, 5, 5);
      c.anchor = GridBagConstraints.CENTER;
      c.ipadx = 10;
      c.ipady = 10;
      JButton queryButton = new JButton ("Query");
      c.gridwidth = GridBagConstraints.REMAINDER;
      gridbag.setConstraints (queryButton, c);
      queryCustomer.add (queryButton);
      
      //
      // Results textarea inside a JscrollPane is added here
      //
      final JTextArea resultsText  = new JTextArea (15, 40);
      final JScrollPane scrollPaneForText = new JScrollPane (resultsText);
      scrollPaneForText.setPreferredSize (new Dimension (500, 400));
      c.fill = GridBagConstraints.BOTH;
      c.anchor = GridBagConstraints.WEST;
      c.gridheight = GridBagConstraints.REMAINDER;
      gridbag.setConstraints (scrollPaneForText, c);
      queryCustomer.add (scrollPaneForText);
     
      //
      // QUERY BUTTON ACTION LISTENER
      //
      queryButton.addActionListener (new ActionListener() 
         {
            public void actionPerformed (ActionEvent e)
            {
               Vector queryResults = new Vector();

               // perform query
               queryResults = customerData.queryEntryByThreeFields (
                  surnameOfCustomer.getText(),
                  nameOfCustomer.getText(),
                  emailOfCustomer.getText());

               String queryResultsString = new String();
               
               // for each results string ...
               for (int i = 0; i < queryResults.size(); i++)
               {
                  Vector oneEntry = new Vector();
                  
                  // parse each line into a vector
                  oneEntry = customerData.parseEntry ((String) queryResults.elementAt(i));

                  String line = new String();
                  
                  // parse Vector into meaningful message with labels
                  line += customerData.createCustomerString (oneEntry);
                  
                  // add a break if needed between records
                  if (i != (queryResults.size() - 1))
                  {
                     line += "\n--------------------------------------\n"; 
                  }

                  queryResultsString += line;
               }

               // after for loop, add the string to the textbox
               resultsText.setText (queryResultsString);

            }
         }
      ); // end of queryButton LISTENER
   }


   // ------------------------------------
   // Create E-mail Tab
   // 
   private void buildEmailTab() {
      // create text box area
      final JTextArea emailText  = new JTextArea (15, 40);
      final JScrollPane scrollPaneForText = new JScrollPane (emailText);
      scrollPaneForText.setPreferredSize (new Dimension (700, 400));
      
      // create the emailButton
      JButton emailButton = new JButton ("E-mail Customers");
      emailCustomer.add (emailButton);
      emailCustomer.add (scrollPaneForText);
      
      //
      // EMAIL BUTTON ACTION LISTENER
      //
      emailButton.addActionListener (new ActionListener() 
         {
            public void actionPerformed (ActionEvent e)
            {
               Vector queryResults = new Vector();
              
               // get all the entires that have YES in 12th field
               queryResults = customerData.queryEntry ("Yes", 12);

               // loop through all results
               for (int i = 0; i < queryResults.size(); i++)
               {
                  // send email here...
                  String entireEmail = "";
                  
                  entireEmail += "\n";
                  entireEmail += emailText.getText();
           
                  mail (customerData.getField ( (String) queryResults.elementAt(i), 3), entireEmail);

               }
            }
         }
      ); // end of emailButton LISTENER
   }


   // ------------------------------------
   // Create Delete Tab
   // 
   private void buildDeleteTab() {
      // setup layout manager for this panel
      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();
      deleteCustomer.setLayout (gridbag);

      // common attributes
      c.fill = GridBagConstraints.NONE;
      c.insets = new Insets (2, 2, 2, 2);

      //
      // SURNAME label and textfield 
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel surnameLabel = new JLabel ("Surname:", JLabel.RIGHT);
      gridbag.setConstraints (surnameLabel, c);
      deleteCustomer.add (surnameLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField surnameOfCustomer = new JTextField (40);
      gridbag.setConstraints (surnameOfCustomer, c);
      deleteCustomer.add (surnameOfCustomer);

      //
      // GIVEN NAME label and textfield
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel nameLabel = new JLabel ("Given name:", JLabel.RIGHT);
      gridbag.setConstraints (nameLabel, c);
      deleteCustomer.add (nameLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField nameOfCustomer = new JTextField (40);
      gridbag.setConstraints (nameOfCustomer, c);
      deleteCustomer.add (nameOfCustomer);

      //
      // E-MAIL ADDRESS label and textfield
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel emailLabel = new JLabel ("E-mail address:", JLabel.RIGHT);
      gridbag.setConstraints (emailLabel, c);
      deleteCustomer.add (emailLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField emailOfCustomer = new JTextField (40);
      gridbag.setConstraints (emailOfCustomer, c);
      deleteCustomer.add (emailOfCustomer);

      //
      // delete button add here
      //
      c.insets = new Insets (5, 5, 5, 5);
      c.anchor = GridBagConstraints.CENTER;
      c.ipadx = 10;
      c.ipady = 10;
      JButton deleteButton = new JButton ("Delete");
      c.gridwidth = GridBagConstraints.REMAINDER;
      gridbag.setConstraints (deleteButton, c);
      deleteCustomer.add (deleteButton);
      
      // 
      // DELETE BUTTON ACTION LISTENER
      //
      deleteButton.addActionListener (new ActionListener() 
         {
            public void actionPerformed (ActionEvent e)
            {
               Vector queryResults = new Vector();

               // find all the requested items
               queryResults = customerData.queryEntryByThreeFields (
                  surnameOfCustomer.getText(),
                  nameOfCustomer.getText(),
                  emailOfCustomer.getText());
               
               // clear the textboxes
               surnameOfCustomer.setText("");
               nameOfCustomer.setText("");
               emailOfCustomer.setText("");
               
               // if there is no results, then display message
               if (queryResults.size() == 0)
               {
                  JOptionPane x = new JOptionPane();
                  x.showMessageDialog (deleteCustomer, 
                                       "No such entries found.", 
                                       "Delete failed.", 
                                       JOptionPane.INFORMATION_MESSAGE);
               }
               
               // for each item found ...
               for (int i = 0; i < queryResults.size(); i++)
               {
                  // delete the given record, but first prompt user
                  
                  // prepare a message confirmation message that contains
                  // the entire entry that is to be deleted
                  Vector oneEntry = new Vector();
                  oneEntry = customerData.parseEntry ((String) queryResults.elementAt(i));
                  String messageField = new String();

                  messageField += "Are you sure you want to delete: \n\n";
                  messageField += customerData.createCustomerString (oneEntry);
                  messageField += "\n\n";

                  // pop up a window that accepts YES or NO
                  JOptionPane x = new JOptionPane();
                  int y = x.showConfirmDialog (deleteCustomer, 
                                               messageField, 
                                               "Deletion Confirmation",
                                               JOptionPane.YES_NO_OPTION);

                  // if user presses YES
                  if (y == 0)
                  {
                     // actually perform the deletion here...
                     int retValue = customerData.removeEntry ((String) queryResults.elementAt(i));
                     
                     // display error if function returns nonzero
                     if (retValue != 0)
                     {
                        JOptionPane z = new JOptionPane();
                        z.showMessageDialog (addCustomer, 
                                             "Unable to delete this entry.", 
                                             "Deletion Failed", 
                                             JOptionPane.ERROR_MESSAGE);
                     }
                     
                     // save the database
                     int value = customerData.writeDatabase();
                     
                     // display error if function returns nonzero
                     if (value != 0)
                     {
                        JOptionPane z = new JOptionPane();
                        z.showMessageDialog (addCustomer, 
                                             "The deletion failed.", 
                                             "Database Update Failed", 
                                             JOptionPane.ERROR_MESSAGE);
                     }
                  }
               }
            }
         }
      ); // end of deleteButton LISTENER
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


   //
   // CONSTRUCTOR 
   //
   public RemoteCustomerManager() 
   {
      // create instance of object, read data into tree
      customerData = new RemoteDatabaseInterface("customer.txt");
      
      // build the tabs
		buildLoginMenuTab();
      buildCustomerTab();
      buildModifyTab();
      buildDeleteTab(); 
      buildQueryTab();
      buildEmailTab();
      
      // create a tabbedPane and add the tabs
      tabbedPane = new JTabbedPane();
		tabbedPane.add ("Login", loginMenu);
      tabbedPane.add ("Add", addCustomer);
      tabbedPane.add ("Modify", modifyCustomer);
      tabbedPane.add ("Delete", deleteCustomer);
      tabbedPane.add ("Query", queryCustomer);
      tabbedPane.add ("E-mail", emailCustomer);
   
      tabbedPane.setEnabledAt (1, false);
      tabbedPane.setEnabledAt (2, false);
      tabbedPane.setEnabledAt (3, false);
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
      RemoteCustomerManager panel = new RemoteCustomerManager();
   
      // display the main JFRAME
      frame = new JFrame("Customer Manager");
      frame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {System.exit(0);} });
      frame.getContentPane().add("Center", panel);
      frame.pack();
      frame.setVisible(true);
   
   }
}
