package HistoryServer;

import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Voodoo
 */
public class HistoryView extends JFrame implements ActionListener {
    JLabel titlelbl;
    JLabel filterlbl;
    JButton back;
    JLabel itemlbl;
    JComboBox itembox;
    JLabel datelbl;
    JComboBox datebox;
    JButton clear;

    String[] date = {"Newest", "Oldest"};
    List<String> item = new ArrayList<>();

    // Customer object goes here
    int customerId = 2;
    String username = "";
    
    // Create table
    DefaultTableModel model = new DefaultTableModel();
    JTable tb = new JTable(model);

    public void showGUI(String username) {
        this.username = username;

        System.out.println("Showing history");
        try {
            item = this.getUniqueItems();
        } catch (RemoteException ex) {
            System.out.println("Unable to get all items from menu for filter");
        }

        // Add table columns
        model.addColumn("Order ID");     
        model.addColumn("Item");
        model.addColumn("Price");
        model.addColumn("Date");
        
        updateTable("SELECT ROOT.ORDER_DETAILS.ORDER_ID, ROOT.ITEM_DETAILS.ITEM_NAME, ROOT.ITEM_DETAILS.PRICE_PER_ITEM, " +
                "SUBSTR(ROOT.ITEM_DETAILS.DATE, 1, 10)\n" +
                "FROM ROOT.ORDER_DETAILS\n" +
                "INNER JOIN ROOT.ITEM_DETAILS\n" +
                "    ON ROOT.ORDER_DETAILS.ORDER_ID=ROOT.ITEM_DETAILS.ORDER_ID\n" +
                "    WHERE ROOT.ORDER_DETAILS.USERNAME = '"+username+"'");
        tb.setEnabled(false);
        this.pack();
        
        // Design
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        Color blue = new Color(240,248,255);
        
        tb.setOpaque(true);
        tb.setFillsViewportHeight(true);
        tb.setBackground(blue);
        
        titlelbl = new JLabel("Order History");
        titlelbl.setForeground(Color.black);
        titlelbl.setFont(new Font("Serif", Font.BOLD, 20));
        
        filterlbl = new JLabel("Filter by:");
        
        itemlbl = new JLabel("Item ");       
        itembox = new JComboBox(item.toArray());
        itembox.setSelectedIndex(-1);
        itembox.addActionListener(this);

        datelbl = new JLabel("Date ");
        datebox = new JComboBox(date);
        datebox.setSelectedIndex(-1);
        datebox.addActionListener(this);

        clear = new JButton("Clear filter");
        clear.addActionListener(this);
        
        back = new JButton("Back");
        back.addActionListener(this);
        
        JScrollPane s = new JScrollPane(tb);       
        JPanel newPanel = new JPanel(new GridBagLayout());
        newPanel.setBackground(Color.WHITE);
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
        
        // Positions
        constraints.gridx = 0;
        constraints.gridy = 0;
        newPanel.add(titlelbl, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 1;
        newPanel.add(filterlbl, constraints);
        
        JPanel itemPanel = new JPanel();
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setLayout(new BorderLayout());
        itemPanel.add(itemlbl, BorderLayout.NORTH);
        itemPanel.add(itembox, BorderLayout.WEST);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        newPanel.add(itemPanel, constraints);

        JPanel datePanel = new JPanel();
        datePanel.setBackground(Color.WHITE);
        datePanel.setLayout(new BorderLayout());
        datePanel.add(datelbl, BorderLayout.NORTH);
        datePanel.add(datebox, BorderLayout.WEST);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        newPanel.add(datePanel, constraints);

        JPanel statusPanel = new JPanel();        
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setLayout(new BorderLayout());
        constraints.gridx = 0;
        constraints.gridy = 3;        
        constraints.gridwidth = 2;
        newPanel.add(statusPanel, constraints);
        
        JPanel buttonPanel1 = new JPanel();
        buttonPanel1.setLayout(new BorderLayout());
        buttonPanel1.add(clear, BorderLayout.EAST);
        constraints.gridx = 0;
        constraints.gridy = 3;
        newPanel.add(buttonPanel1, constraints);
        
        JPanel tablePanel = new JPanel();    
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(s, BorderLayout.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 4;
        newPanel.add(tablePanel, constraints);
        
        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.setLayout(new BorderLayout());
        buttonPanel2.add(back, BorderLayout.CENTER);
        constraints.gridx = 0;
        constraints.gridy = 5;
        newPanel.add(buttonPanel2, constraints);
        
        add(newPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);       
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        ResultSet rs = null;
        String query = "SELECT ROOT.ORDER_DETAILS.ORDER_ID, ROOT.ITEM_DETAILS.ITEM_NAME, ROOT.ITEM_DETAILS.PRICE_PER_ITEM, " +
                "SUBSTR(ROOT.ITEM_DETAILS.DATE, 1, 10)\n" +
                "FROM ROOT.ORDER_DETAILS\n" +
                "INNER JOIN ROOT.ITEM_DETAILS\n" +
                "    ON ROOT.ORDER_DETAILS.ORDER_ID=ROOT.ITEM_DETAILS.ORDER_ID\n" +
                "    WHERE ROOT.ORDER_DETAILS.USERNAME = '"+username+"'";

        if (ae.getSource() == clear) {
            itembox.setSelectedItem(null);
        } else if (ae.getSource() == back) {
            this.dispose();
        }

        if (ae.getSource() != back) {
            if (itembox.getSelectedItem() != null) {
                System.out.println(itembox.getSelectedItem());
                query = query + " AND ROOT.ITEM_DETAILS.ITEM_NAME = '"+itembox.getSelectedItem()+"'";
            }
            if (datebox.getSelectedItem() != null) {
                query = query + " ORDER BY ROOT.ITEM_DETAILS.DATE "+ (datebox.getSelectedItem().equals("Newest") ? "DESC" : "ASC");
            }

            
            updateTable(query);
            tb.setEnabled(false);
            this.pack();
        }
    }
    
    void updateTable(String query) {
        while(model.getRowCount()>0){
            model.removeRow(0);
        }
        
        try {
            List<Vector> modelList = query(query);
            modelList.forEach(item -> {
                model.addRow(item);
            });
        } catch (RemoteException ex) {
            Logger.getLogger(HistoryView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception");
        }
    }

    public List<Vector> query(String query) throws RemoteException {
        List<Vector> vectorList = new ArrayList<>();
        ResultSet rs = null;

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/restaurent", "root", "root");
            PreparedStatement pstm = con.prepareStatement(query);
            rs = pstm.executeQuery();

            while (rs.next()) {
                Vector v = new Vector();
                v.add(0, rs.getInt(1));
                v.add(1, rs.getString(2));
                v.add(2, rs.getFloat(3));
                v.add(3, rs.getString(4));
                vectorList.add(v);
            }
        } catch (Exception e) {
            System.out.println("Error from history server");
            System.out.println(e.getMessage());
        }

        return vectorList;
    }

    public List<String> getUniqueItems() throws RemoteException {
        List<String> itemList = new ArrayList<>();
        String query = "SELECT DISTINCT ROOT.MENU_DETAILS.FOOD_NAME " +
                "FROM ROOT.MENU_DETAILS";
        ResultSet rs = null;

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/restaurent", "root", "root");
            PreparedStatement pstm = con.prepareStatement(query);
            rs = pstm.executeQuery();

            while (rs.next()) {
                itemList.add(rs.getString(1));
            }
        } catch (Exception e) {
            System.out.println("Error from history server");
            System.out.println(e.getMessage());
        }

        return itemList;
    }
}