// Michael Bochenek (ID) 0041056
// CIS 3430 Assignment #1

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import java.util.StringTokenizer;

public class ProductManager extends JPanel {
   // main frame that holds everything, ie main window
   static JFrame frame;

   // productData is the tree for all the products
   private DatabaseInterface productData;

   // 4 panels, one for each function
   private JPanel addProduct = new JPanel();
   private JPanel modifyProduct = new JPanel();
   private JPanel deleteProduct = new JPanel();
   private JPanel queryProduct = new JPanel();

   // ------------------------------------
   // Create Product Tab
   //
   private void buildProductTab() {
      // use gridbag layout for this panel
      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();
      addProduct.setLayout (gridbag);

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
      addProduct.add (nameLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField nameOfProduct = new JTextField (40);
      gridbag.setConstraints (nameOfProduct, c);
      addProduct.add (nameOfProduct);

      //
      // UPC label and text field
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel UPCLabel = new JLabel ("UPC:", JLabel.RIGHT);
      gridbag.setConstraints (UPCLabel, c);
      addProduct.add (UPCLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField UPCOfProduct = new JTextField (40);
      gridbag.setConstraints (UPCOfProduct, c);
      addProduct.add (UPCOfProduct);

      //
      // PRICE label and text field
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel priceLabel = new JLabel ("Price:", JLabel.RIGHT);
      gridbag.setConstraints (priceLabel, c);
      addProduct.add (priceLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField priceOfProduct = new JTextField (40);
      gridbag.setConstraints (priceOfProduct, c);
      addProduct.add (priceOfProduct);

      //
      // AMOUNT label and text field
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel amountLabel = new JLabel ("Amount on hand:", JLabel.RIGHT);
      gridbag.setConstraints (amountLabel, c);
      addProduct.add (amountLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField amountOfProduct = new JTextField (20);
      gridbag.setConstraints (amountOfProduct, c);
      addProduct.add (amountOfProduct);

      //
      // DELIEVERY TIME label and textbox
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel timeLabel = new JLabel ("Delievery Time:", JLabel.RIGHT);
      gridbag.setConstraints (timeLabel, c);
      addProduct.add (timeLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField timeOfProduct = new JTextField (40);
      gridbag.setConstraints (timeOfProduct, c);
      addProduct.add (timeOfProduct);

      //
      // COST label and text box
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel costLabel = new JLabel ("Supplier Cost:", JLabel.RIGHT);
      gridbag.setConstraints (costLabel, c);
      addProduct.add (costLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField costOfProduct = new JTextField (10);
      gridbag.setConstraints (costOfProduct, c);
      addProduct.add (costOfProduct);

      //
      // SUPPLIER label and textbox
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel supplierLabel = new JLabel ("Supplier:", JLabel.RIGHT);
      gridbag.setConstraints (supplierLabel, c);
      addProduct.add (supplierLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField supplierOfProduct = new JTextField (20);
      gridbag.setConstraints (supplierOfProduct, c);
      addProduct.add (supplierOfProduct);

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
      addProduct.add (addButton);
   
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
               newEntry.addElement (nameOfProduct.getText());
               newEntry.addElement (UPCOfProduct.getText());
               newEntry.addElement (supplierOfProduct.getText());
               newEntry.addElement (priceOfProduct.getText());
               newEntry.addElement (amountOfProduct.getText());
               newEntry.addElement (timeOfProduct.getText());
               newEntry.addElement (costOfProduct.getText());

               // add this element to the tree
               int value = productData.addEntry (newEntry);
               
               // if value returned is not 0, display an error message
               if (value != 0)
               {
                  JOptionPane x = new JOptionPane();
                  x.showMessageDialog (addProduct,
                                       "The product could not be added.",
                                       "Product Add Error",
                                       JOptionPane.ERROR_MESSAGE);
               }
               else
               {
                  // write the informarion to the database
                  value = productData.writeDatabase();
               
                  // if value returned is not 0, display an error message
                  if (value != 0)
                  {
                     JOptionPane x = new JOptionPane();
                     x.showMessageDialog (addProduct,
                                          "The changes could not be saved.",
                                          "Database Update Failed",
                                          JOptionPane.ERROR_MESSAGE);
                  }
                  else
                  {  
                     JOptionPane x = new JOptionPane();
                     x.showMessageDialog (addProduct,
                                          "The product has been added.",
                                          "Database Update Successful",
                                          JOptionPane.INFORMATION_MESSAGE);

                     // clear all the text boxes and reset combo boxes
                     nameOfProduct.setText("");
                     UPCOfProduct.setText("");
                     priceOfProduct.setText("");
                     amountOfProduct.setText("");
                     timeOfProduct.setText("");
                     costOfProduct.setText("");
                     supplierOfProduct.setText("");
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
      modifyProduct.setLayout (gridbag);

      // common attributes
      c.fill = GridBagConstraints.NONE;
      c.insets = new Insets (2, 2, 2, 2);

      //
      // NAME of product
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel nameLabel = new JLabel ("Reference Name:", JLabel.RIGHT);
      gridbag.setConstraints (nameLabel, c);
      modifyProduct.add (nameLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField nameOfProduct = new JTextField (40);
      gridbag.setConstraints (nameOfProduct, c);
      modifyProduct.add (nameOfProduct);

      //
      // UPC label and textbox
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel UPCLabel = new JLabel ("UPC:", JLabel.RIGHT);
      gridbag.setConstraints (UPCLabel, c);
      modifyProduct.add (UPCLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField UPCOfProduct = new JTextField (40);
      gridbag.setConstraints (UPCOfProduct, c);
      modifyProduct.add (UPCOfProduct);

      //
      // SUPPLIER label and text field
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel supplierLabel = new JLabel ("Supplier:", JLabel.RIGHT);
      gridbag.setConstraints (supplierLabel, c);
      modifyProduct.add (supplierLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField supplierOfProduct = new JTextField (40);
      gridbag.setConstraints (supplierOfProduct, c);
      modifyProduct.add (supplierOfProduct);

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
      modifyProduct.add (findButton);

      // stuff common to all the other labels and textboxes
      c.insets = new Insets (2, 2, 2, 2);
      c.ipadx = 0;
      c.ipady = 0;
      
      //
      // NAME label and textbox
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel nameLabel2 = new JLabel ("Reference Name:", JLabel.RIGHT);
      gridbag.setConstraints (nameLabel2, c);
      modifyProduct.add (nameLabel2);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField nameOfProduct2 = new JTextField (40);
      gridbag.setConstraints (nameOfProduct2, c);
      modifyProduct.add (nameOfProduct2);

      //
      // UPC label and textbox
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel UPCLabel2 = new JLabel ("UPC:", JLabel.RIGHT);
      gridbag.setConstraints (UPCLabel2, c);
      modifyProduct.add (UPCLabel2);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField UPCOfProduct2 = new JTextField (40);
      gridbag.setConstraints (UPCOfProduct2, c);
      modifyProduct.add (UPCOfProduct2);

      //
      // PRICE label and textbox
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel priceLabel = new JLabel ("Price:", JLabel.RIGHT);
      gridbag.setConstraints (priceLabel, c);
      modifyProduct.add (priceLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField priceOfProduct = new JTextField (40);
      gridbag.setConstraints (priceOfProduct, c);
      modifyProduct.add (priceOfProduct);

      //
      // AMOUNT label and textbox
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel amountLabel = new JLabel ("Amount:", JLabel.RIGHT);
      gridbag.setConstraints (amountLabel, c);
      modifyProduct.add (amountLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField amountOfProduct = new JTextField (20);
      gridbag.setConstraints (amountOfProduct, c);
      modifyProduct.add (amountOfProduct);

      //
      // TIME label and textboxe
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel timeLabel = new JLabel ("Delievery Time:", JLabel.RIGHT);
      gridbag.setConstraints (timeLabel, c);
      modifyProduct.add (timeLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField timeOfProduct = new JTextField (40);
      gridbag.setConstraints (timeOfProduct, c);
      modifyProduct.add (timeOfProduct);

      //
      // COST label and textbox
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel costLabel = new JLabel ("Cost:", JLabel.RIGHT);
      gridbag.setConstraints (costLabel, c);
      modifyProduct.add (costLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField costOfProduct = new JTextField (10);
      gridbag.setConstraints (costOfProduct, c);
      modifyProduct.add (costOfProduct);

      //
      // SUPPLIER label and textbox
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel supplierLabel2 = new JLabel ("Supplier:", JLabel.RIGHT);
      gridbag.setConstraints (supplierLabel2, c);
      modifyProduct.add (supplierLabel2);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField supplierOfProduct2 = new JTextField (20);
      gridbag.setConstraints (supplierOfProduct2, c);
      modifyProduct.add (supplierOfProduct2);

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
      modifyProduct.add (saveButton);

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
               newEntry.addElement (nameOfProduct2.getText());
               newEntry.addElement (UPCOfProduct2.getText());
               newEntry.addElement (supplierOfProduct2.getText());
               newEntry.addElement (priceOfProduct.getText());
               newEntry.addElement (amountOfProduct.getText());
               newEntry.addElement (timeOfProduct.getText());
               newEntry.addElement (costOfProduct.getText());

               // find the old entry in the tree (and then delete it)
               Vector queryResults = new Vector();
               queryResults = productData.queryEntryByThreeFields (
                  nameOfProduct.getText(),
                  UPCOfProduct.getText(),
                  supplierOfProduct.getText());

               if (queryResults.size() != 0)
               {
                  // it will always be able to remove the entry
                  // since we just found it in the tree
                  int value = productData.removeEntry ((String) queryResults.elementAt(0));

                  // add the newer (modified) entry
                  value = productData.addEntry (newEntry);

                  // display error message if necessary
                  if (value != 0)
                  {
                     JOptionPane x = new JOptionPane();
                     x.showMessageDialog (modifyProduct,
                                          "The product could not be added.",
                                          "Product Add Error",
                                          JOptionPane.ERROR_MESSAGE);
                  }

                  // write the changes to the database
                  value = productData.writeDatabase();
                  if (value != 0)
                  {
                     JOptionPane x = new JOptionPane();
                     x.showMessageDialog (modifyProduct,
                                          "The changes could not be saved.",
                                          "Database Update Failed",
                                          JOptionPane.ERROR_MESSAGE);
                  }
                  else
                  {  
                     JOptionPane x = new JOptionPane();
                     x.showMessageDialog (modifyProduct,
                                          "The product has been modified.",
                                          "Database Modify Successful",
                                          JOptionPane.INFORMATION_MESSAGE);

                     // clear all the input fields
                     nameOfProduct.setText("");
                     UPCOfProduct.setText("");
                     supplierOfProduct.setText("");
                     nameOfProduct2.setText("");
                     UPCOfProduct2.setText("");
                     supplierOfProduct2.setText("");
                     priceOfProduct.setText("");
                     amountOfProduct.setText("");
                     timeOfProduct.setText("");
                     costOfProduct.setText("");
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
               queryResults = productData.queryEntryByThreeFields (
                  nameOfProduct.getText(),
                  UPCOfProduct.getText(),
                  supplierOfProduct.getText());

               String queryResultsString = new String();
               
               // if nothing is returned in vector, clear the result boxes
               if (queryResults.size() == 0)
               {
                  nameOfProduct2.setText("");
                  UPCOfProduct2.setText("");
                  supplierOfProduct2.setText("");
                  priceOfProduct.setText("");
                  amountOfProduct.setText("");
                  timeOfProduct.setText("");
                  costOfProduct.setText("");
               }
               else
               {
               
                  // otherwise, take the result and apply the StringTokenizer
                  // take each next token and display in the fields
                  StringTokenizer t = new StringTokenizer ((String) queryResults.elementAt(0), ";");
                  nameOfProduct2.setText (t.nextToken());
                  UPCOfProduct2.setText (t.nextToken());
                  supplierOfProduct2.setText (t.nextToken());
                  priceOfProduct.setText (t.nextToken());
                  amountOfProduct.setText (t.nextToken());
                  timeOfProduct.setText (t.nextToken());
                  costOfProduct.setText (t.nextToken());
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
      queryProduct.setLayout (gridbag);

      // common attributes
      c.fill = GridBagConstraints.NONE;
      c.insets = new Insets (2, 2, 2, 2);

      //
      // NAME label and textfield
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel nameLabel = new JLabel ("Reference Name:", JLabel.RIGHT);
      gridbag.setConstraints (nameLabel, c);
      queryProduct.add (nameLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField nameOfProduct = new JTextField (40);
      gridbag.setConstraints (nameOfProduct, c);
      queryProduct.add (nameOfProduct);

      //
      // UPC label and text field
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel UPCLabel = new JLabel ("UPC:", JLabel.RIGHT);
      gridbag.setConstraints (UPCLabel, c);
      queryProduct.add (UPCLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField UPCOfProduct = new JTextField (40);
      gridbag.setConstraints (UPCOfProduct, c);
      queryProduct.add (UPCOfProduct);

      //
      // SUPPLIER label and textfield
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel supplierLabel = new JLabel ("Supplier:", JLabel.RIGHT);
      gridbag.setConstraints (supplierLabel, c);
      queryProduct.add (supplierLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField supplierOfProduct = new JTextField (40);
      gridbag.setConstraints (supplierOfProduct, c);
      queryProduct.add (supplierOfProduct);

      //
      // QueryButton added here
      //
      c.insets = new Insets (5, 5, 5, 5);
      c.anchor = GridBagConstraints.CENTER;
      c.ipadx = 10;
      c.ipady = 10;
      JButton queryButton = new JButton ("Query Product Supply");
      c.gridwidth = GridBagConstraints.REMAINDER;
      gridbag.setConstraints (queryButton, c);
      queryProduct.add (queryButton);

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
      queryProduct.add (scrollPaneForText);
   
      //
      // QUERY BUTTON ACTION LISTENER
      //
      queryButton.addActionListener (new ActionListener()
         {
            public void actionPerformed (ActionEvent e)
            {
               Vector queryResults = new Vector();

               // perform query
               queryResults = productData.queryEntryByThreeFields (
                  nameOfProduct.getText(),
                  UPCOfProduct.getText(),
                  supplierOfProduct.getText());

               String queryResultsString = new String();
               queryResultsString += "The following products have less " +
                                     "than 10 items on hand.\n" +
                                     "You should order these products.\n\n";
               
               // for each results string ...
               for (int i = 0; i < queryResults.size(); i++)
               {
                  Vector oneEntry = new Vector();
                  
                  // parse each line into a vector
                  oneEntry = productData.parseEntry ((String) queryResults.elementAt(i));

                  String line = new String();

                  // parse Vector into meaningful message with labels
                  line += productData.createProductString (oneEntry);
                  
                  // add a break if needed between records
                  if (i != (queryResults.size() - 1))
                  {
                     line += "\n--------------------------------------\n";
                  }

                  String amountString = (String) oneEntry.elementAt(4);
                  if (amountString.length() <= 1)
                  {
                     queryResultsString += line;
                  }
               }

               // after for loop, add the string to the textbox
               resultsText.setText (queryResultsString);

            }
         }
      ); // end of queryButton LISTENER
   }


   // ------------------------------------
   // Create Delete Tab
   //
   private void buildDeleteTab() {
      // setup layout manager for this panel
      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();
      deleteProduct.setLayout (gridbag);

      // common attributes
      c.fill = GridBagConstraints.NONE;
      c.insets = new Insets (2, 2, 2, 2);

      //
      // NAME label and textfield
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel nameLabel = new JLabel ("Reference Name:", JLabel.RIGHT);
      gridbag.setConstraints (nameLabel, c);
      deleteProduct.add (nameLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField nameOfProduct = new JTextField (40);
      gridbag.setConstraints (nameOfProduct, c);
      deleteProduct.add (nameOfProduct);

      //
      // UPC label and textfield
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel UPCLabel = new JLabel ("UPC:", JLabel.RIGHT);
      gridbag.setConstraints (UPCLabel, c);
      deleteProduct.add (UPCLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField UPCOfProduct = new JTextField (40);
      gridbag.setConstraints (UPCOfProduct, c);
      deleteProduct.add (UPCOfProduct);

      //
      // SUPPLIER label and textfield
      //
      c.gridwidth = 1;
      c.anchor = GridBagConstraints.EAST;
      JLabel supplierLabel = new JLabel ("Supplier:", JLabel.RIGHT);
      gridbag.setConstraints (supplierLabel, c);
      deleteProduct.add (supplierLabel);
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.anchor = GridBagConstraints.WEST;
      final JTextField supplierOfProduct = new JTextField (40);
      gridbag.setConstraints (supplierOfProduct, c);
      deleteProduct.add (supplierOfProduct);

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
      deleteProduct.add (deleteButton);

      //
      // DELETE BUTTON ACTION LISTENER
      //
      deleteButton.addActionListener (new ActionListener()
         {
            public void actionPerformed (ActionEvent e)
            {
               Vector queryResults = new Vector();

               // find all the requested items
               queryResults = productData.queryEntryByThreeFields (
                  nameOfProduct.getText(),
                  UPCOfProduct.getText(),
                  supplierOfProduct.getText());
               
               // clear the textboxes
               nameOfProduct.setText("");
               UPCOfProduct.setText("");
               supplierOfProduct.setText("");
               
               // if there is no results, then display message
               if (queryResults.size() == 0)
               {
                  JOptionPane x = new JOptionPane();
                  x.showMessageDialog (deleteProduct,
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
                  oneEntry = productData.parseEntry ((String) queryResults.elementAt(i));
                  String messageField = new String();

                  messageField += "Are you sure you want to delete: \n\n";
                  messageField += productData.createProductString (oneEntry);
                  messageField += "\n\n";

                  // pop up a window that accepts YES or NO
                  JOptionPane x = new JOptionPane();
                  int y = x.showConfirmDialog (deleteProduct,
                                               messageField,
                                               "Deletion Confirmation",
                                               JOptionPane.YES_NO_OPTION);

                  // if user presses YES
                  if (y == 0)
                  {
                     // actually perform the deletion here...
                     int retValue = productData.removeEntry ((String) queryResults.elementAt(i));
                     
                     // display error if function returns nonzero
                     if (retValue != 0)
                     {
                        JOptionPane z = new JOptionPane();
                        z.showMessageDialog (addProduct,
                                             "Unable to delete this entry.",
                                             "Deletion Failed",
                                             JOptionPane.ERROR_MESSAGE);
                     }
                     
                     // save the database
                     int value = productData.writeDatabase();

                     // display error if function returns nonzero
                     if (value != 0)
                     {
                        JOptionPane z = new JOptionPane();
                        z.showMessageDialog (addProduct,
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


   //
   // CONSTRUCTOR
   //
   public ProductManager()
   {
      // create instance of object, read data into tree
      productData = new DatabaseInterface("product.txt");
      
      // build the tabs
      buildProductTab();
      buildModifyTab();
      buildDeleteTab(); 
      buildQueryTab();

      // create a tabbedPane and add the tabs
      JTabbedPane tabbedPane = new JTabbedPane();
      tabbedPane.add ("Add", addProduct);
      tabbedPane.add ("Modify", modifyProduct);
      tabbedPane.add ("Delete", deleteProduct);
      tabbedPane.add ("Query", queryProduct);

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
      int value = productData.writeDatabase();
      if (value != 0)
      {
         JOptionPane y = new JOptionPane();
         y.showMessageDialog (addProduct,
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
      ProductManager panel = new ProductManager();
   
      // display the main JFRAME
      frame = new JFrame("Product Manager");
      frame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {System.exit(0);} });
      frame.getContentPane().add("Center", panel);
      frame.pack();
      frame.setVisible(true);
   
   }
}
