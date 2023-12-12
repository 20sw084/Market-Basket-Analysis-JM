package org;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUI extends JFrame {
    private JCheckBox checkBoxOnion, checkBoxPotato, checkBoxMilk, checkBoxChips, checkBoxBeer, checkBoxNoodles;
    private JButton submitButton, generateButton;
    private JTextField transactionIdField;
    private JLabel enterItemsLabel, listItemsLabel,enterTransactionsLabel;
    private JPanel mpanel, panel1, panel2, panel11;
    DefaultTableModel model;
    JTable table;
    TextArea outText = new TextArea();

    public GUI() {
        // Set up the frames
        setTitle("Market Basket Analysis UI");
        setSize(655, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        mpanel = new JPanel();
        mpanel.setLayout(new BorderLayout());

        // Initialize components
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        
        panel11 = new JPanel();
        panel11.setLayout(new GridLayout(0, 2));
        

        panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        
        enterItemsLabel = new JLabel("Enter Items");
        panel1.add(enterItemsLabel, BorderLayout.NORTH);

        enterTransactionsLabel = new JLabel("Transactions ID:");
        panel11.add(enterTransactionsLabel);

        transactionIdField = new JTextField();
        panel11.add(transactionIdField);

        checkBoxOnion = new JCheckBox("Onion");
        panel11.add(checkBoxOnion);

        checkBoxPotato = new JCheckBox("Potato");
        panel11.add(checkBoxPotato);

        checkBoxMilk = new JCheckBox("Milk");
        panel11.add(checkBoxMilk);

        checkBoxChips = new JCheckBox("Chips");
        panel11.add(checkBoxChips);

        checkBoxBeer = new JCheckBox("Beer");
        panel11.add(checkBoxBeer);

        checkBoxNoodles = new JCheckBox("Noodles");
        panel11.add(checkBoxNoodles);
        panel1.add(panel11);
        
        model = new DefaultTableModel();
        model.addColumn("Transaction Id");
        model.addColumn("Onion");
        model.addColumn("Potato");
        model.addColumn("Milk");
        model.addColumn("Chips");
        model.addColumn("Beer");
        model.addColumn("Noodles");

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() { 
        	  public void actionPerformed(ActionEvent e) {
        		  if(transactionIdField.getText().isEmpty()) {
        			  showMessageDialogMine("Please enter a valid integer for Transaction Id.", "Invalid Input");
        		  }
        		  else {
        		    String id = transactionIdField.getText();
        		    transactionIdField.setText("");
        		    Boolean onion = checkBoxOnion.isSelected();
        		    Boolean potato = checkBoxPotato.isSelected();
        		    Boolean milk = checkBoxMilk.isSelected();
        		    Boolean chips = checkBoxChips.isSelected();
        		    Boolean beer = checkBoxBeer.isSelected();
        		    Boolean noodles = checkBoxNoodles.isSelected();
        		    model.addRow(new Object[]{id, onion, potato, milk, chips, beer, noodles});
        		  }
        		  } 
        		} 
        );
        panel1.add(submitButton, BorderLayout.SOUTH);

        listItemsLabel = new JLabel("List of Items");
        panel2.add(listItemsLabel, BorderLayout.NORTH);
        
        panel2.add(scrollPane , BorderLayout.CENTER);

        // Panel for the Generate button
        JPanel generatePanel = new JPanel();
        generatePanel.add(outText);
        generateButton = new JButton("Generate");
        generateButton.addActionListener(
        		new ActionListener() { 
              	  public void actionPerformed(ActionEvent e) {
              		runAprioriAlgorithm();
              		  } 
              		} 
        );
        panel2.add(generateButton, BorderLayout.SOUTH);

        // Add panels to the frame
        mpanel.add(panel1, BorderLayout.WEST);
        mpanel.add(panel2, BorderLayout.EAST);
        mpanel.add(generatePanel, BorderLayout.SOUTH);
        add(mpanel);
    }
    
    private void showMessageDialogMine(String message, String title) {
    	JOptionPane.showMessageDialog(this, message,
                title, JOptionPane.ERROR_MESSAGE);
	}
    
    private void runAprioriAlgorithm() {
    	// Get data from JTable
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = model.getRowCount();
        int colCount = model.getColumnCount();

        List<List<String>> transactions = new ArrayList<>();
        
        // Convert JTable data to a format suitable for Apriori algorithm
        
        for (int i = 0; i < rowCount; i++) {
        	List<String> str = new ArrayList<String>();
            for (int j = 1; j < colCount; j++) {
                List<String> itemsOrder = Arrays.asList("Onion", "Potato", "Milk", "Chips", "Beer", "Noodles");
                
                if((boolean) model.getValueAt(i, j)) {
                	str.add(itemsOrder.get(j-1));
                }
            }
            transactions.add(str);
        }
        
        String userInput = JOptionPane.showInputDialog(this, "Enter Minimum Support (eg: 3) :");
        
     // Check if the user clicked cancel or entered an empty value
        if (userInput != null && !userInput.isEmpty()) {
            try {
                // Minimum support count threshold
                int minSupportCount = Integer.parseInt(userInput);      
                
                // Generate frequent itemsets
                List<List<String>> frequentItemsets = AprioriAlgorithm.generateFrequentItemsets(transactions, minSupportCount);
                
                // Print the frequent itemsets
                outText.append("Frequent Itemsets are:\n");
                System.out.println("Frequent Itemsets are:");
                for (List<String> itemset : frequentItemsets) {
                    System.out.println(itemset);
                    outText.append(itemset.toString()+"\n");
                }

                // For this example, just show a message
                JOptionPane.showMessageDialog(this, "Apriori algorithm executed successfully!");
                
            } catch (NumberFormatException e) {
                // Handle the case where the user did not enter a valid integer
                JOptionPane.showMessageDialog(this, "Please enter a valid integer for Minimum Support.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Handle the case where the user clicked cancel or entered an empty value
            JOptionPane.showMessageDialog(this, "Operation canceled. Please provide a valid input.",
                    "Canceled", JOptionPane.INFORMATION_MESSAGE);
        }
        
    }
}