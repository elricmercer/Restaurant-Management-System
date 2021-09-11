package LoginRegisterServer;

import CustomerServer.HistoryServer;
import CustomerServer.ManagementServer;
import CustomerServer.MenuServer;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.rmi.Naming;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableModel;

public class GUI {

    public static void loginGUI() {
        JFrame frame = new JFrame("FOS Authentication");

        JLabel labels[] = {new JLabel("Username: "), new JLabel("Password: "),
            new JLabel("Don't have an account? Register")};
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        labels[0].setBounds(10, 120, 100, 50);
        labels[1].setBounds(10, 160, 100, 50);
        labels[2].setBounds(50, 200, 200, 50);

        /**
         * Register Label Actions
         */
        labels[2].addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                registerGUI("Register New User", 0); // customer registration
            }
        });

        usernameField.setBounds(100, 135, 130, 25);
        passwordField.setBounds(100, 170, 130, 25);

        loginButton.setBounds(240, 170, 100, 25);
        loginButton.addActionListener(new LoginButtonAction(frame, usernameField, passwordField));

        frame.add(usernameField);
        frame.add(passwordField);

        frame.add(loginButton);

        for (JLabel l : labels) {
            frame.add(l);
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    public static void registerGUI(String frameLabel, int role) {
        JFrame frame = new JFrame(frameLabel);

        JLabel labels[] = {new JLabel("First Name: "), new JLabel("Last Name: "), new JLabel("IC/Passport: "),
            new JLabel("Username: "), new JLabel("Password: ")};
        JTextField textFields[] = {new JTextField(), new JTextField(), new JTextField(), new JTextField()};
        JPasswordField passwordField = new JPasswordField();
        JButton registerButton = new JButton("Register");
        JButton checkButton = new JButton("Check Availability");

        int yAxis = 25;
        for (int i = 0; i < labels.length; i++) {
            labels[i].setBounds(10, yAxis, 200, 25);
            yAxis += 40;
        }

        textFields[0].setBounds(100, 25, 200, 25);
        textFields[1].setBounds(100, 65, 200, 25);
        textFields[2].setBounds(100, 105, 200, 25);
        textFields[3].setBounds(100, 145, 200, 25);

        passwordField.setBounds(100, 185, 200, 25);

        registerButton.setBounds(100, 225, 100, 25);

        registerButton.addActionListener(new RegisterButtonAction(frame, textFields, passwordField, role));

        frame.add(passwordField);
        frame.add(registerButton);
        frame.add(checkButton);

        for (JLabel l : labels) {
            frame.add(l);
        }

        for (JTextField tf : textFields) {
            frame.add(tf);
        }

        frame.setLayout(new BorderLayout());
        frame.setSize(500, 400);
        frame.setVisible(true);
    }
    
    public static void editUserGUI(User user) {
        JFrame frame = new JFrame("Update User");

        JLabel labels[] = {new JLabel("First Name: "), new JLabel("Last Name: "), new JLabel("IC/Passport: "),
            new JLabel("Username: ")};
        JTextField textFields[] = {new JTextField(), new JTextField(), new JTextField()};
        JButton updateButton = new JButton("Update");

        int yAxis = 25;
        for (int i = 0; i < labels.length; i++) {
            labels[i].setBounds(10, yAxis, 200, 25);
            yAxis += 40;
        }

        textFields[0].setBounds(100, 25, 200, 25);
        textFields[1].setBounds(100, 65, 200, 25);
        textFields[2].setBounds(100, 105, 200, 25);

        textFields[0].setText(user.getFirstName());
        textFields[1].setText(user.getLastName());
        textFields[2].setText(user.getIcNumber());

        JLabel unameLabel = new JLabel(user.getUsername());
        unameLabel.setBounds(100, 145, 200, 25);
        frame.add(unameLabel);

        updateButton.addActionListener((event) -> {
            boolean flag = false;

            if (textFields[0].getText().isEmpty() || textFields[1].getText().isEmpty() || textFields[2].getText().isEmpty()) {
                flag = true;
                JOptionPane.showMessageDialog(frame, "SOMETHING WENT WRONG: MAKE SURE ALL FIELDS ARE FILLED");
            }

            if (!flag) {
                user.setFirstName(textFields[0].getText());
                user.setLastName(textFields[1].getText());
                user.setIcNumber(textFields[2].getText());
                try {
                    UserInterface srv = (UserInterface) Naming.lookup("rmi://localhost:1040/users");
                    boolean res = srv.updateUser(user);
                    if (res) {
                        JOptionPane.showMessageDialog(frame, "SUCCESS: USER HAS BEEN UPDATED!");
                        frame.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(frame, "SOMETHING WENT WRONG: FAILED TO UPDATE USER!");
                    }
                } catch (Exception e) {
                }
            }
        });

        updateButton.setBounds(100, 225, 100, 25);

        frame.add(updateButton);

        for (JLabel l : labels) {
            frame.add(l);
        }

        for (JTextField tf : textFields) {
            frame.add(tf);
        }

        frame.setLayout(new BorderLayout());
        frame.setSize(500, 400);
        frame.setVisible(true);
    }

    private static void employeeGUI() {
        JFrame frame = new JFrame("Employee Dashboard");
        JButton regEmployee = new JButton("Register Employee");
        JButton empDatabase = new JButton("Employee Database");

        regEmployee.setBounds(35, 10, 200, 25);
        empDatabase.setBounds(35, 40, 200, 25);
        frame.add(regEmployee);
        frame.add(empDatabase);

        regEmployee.addActionListener((event) -> {
            registerGUI("Register Employee", 1);
            frame.setVisible(false);
        });

        empDatabase.addActionListener((event) -> {
            try {
                userTableGUI(1);
            } catch (Exception e) {
            }
            frame.setVisible(false);
        });

        frame.setLayout(new BorderLayout());
        frame.setSize(300, 200);
        frame.setVisible(true);
    }

    private static void userTableGUI(int role) throws Exception {
        UserInterface srv = (UserInterface) Naming.lookup("rmi://localhost:1040/users");
        JFrame frame = new JFrame("Employee Database");

        try {
            String header[] = new String[]{"FIRST_NAME", "LAST_NAME", "IC_NUMBER",
                "USERNAME", "LOGGED_IN", "ROLE"};
            JTable table = new JTable();

            DefaultTableModel dtm = new DefaultTableModel(0, 0);
            dtm.setColumnIdentifiers(header);
            table.setModel(dtm);
            table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

            ArrayList<User> users = srv.fetch(role);
            users.forEach((user) -> {
                System.out.println(user.getUsername());
                dtm.addRow(new Object[]{user.getFirstName(), user.getLastName(), user.getIcNumber(), user.getUsername(), user.getLoggedIn(), user.getRole()});
            });

            /*
REFERENCE: https://stackoverflow.com/questions/23465295/remove-a-selected-row-from-jtable-on-button-click
             */
            JButton delete = new JButton("delete");
            JButton edit = new JButton("edit");
            edit.addActionListener((event) -> {
                if (table.getSelectedRow() != -1) {
                    String st = table.getValueAt(table.getSelectedRow(), 3).toString();
                    users.forEach((user) -> {
                        if (user.getUsername().equals(st)) {
                            GUI.editUserGUI(user);
                            frame.setVisible(false);
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a Row to update");
                }
            });
            delete.addActionListener((event) -> {
                if (table.getSelectedRow() != -1) {
                    String st = table.getValueAt(table.getSelectedRow(), 3).toString();

                    int choice = JOptionPane.showConfirmDialog(frame, "Delete User: " + st, "", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        users.forEach((user) -> {
                            if (user.getUsername().equals(st)) {
                                try {
                                    boolean removed = srv.removeUser(user);
                                    if (removed) {
                                        JOptionPane.showMessageDialog(frame, "SUCCESSFULLLY DELETED USER");
                                        dtm.removeRow(table.getSelectedRow());
                                    }
                                } catch (Exception e) {
                                }
                            }
                        });
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a Row to delete");
                }
            });

            JPanel panel = new JPanel();
            frame.add(panel, BorderLayout.SOUTH);
            panel.add(edit, BorderLayout.LINE_START);
            panel.add(delete, BorderLayout.LINE_END);

            JScrollPane sp = new JScrollPane(table);
            frame.add(sp, BorderLayout.NORTH);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "SORRY SOMETHING WENT WRONG WHILE FETCHING DATA, PLEASE TRY AGAIN LATER");
            e.printStackTrace();
        }

        frame.setSize(750, 600);
        frame.setVisible(true);
    }

    private static void menuGUI(User user) {
        JFrame frame = new JFrame("Menu");
        JButton addBtn = new JButton("Add Item");
        JButton editBtn = new JButton("Edit Item");

        addBtn.setBounds(35, 10, 200, 25);
        editBtn.setBounds(35, 40, 200, 25);
        frame.add(addBtn);
        frame.add(editBtn);

        if (user.getRole() == 2) {
            JButton deleteBtn = new JButton("Delete Item");
            deleteBtn.setBounds(35, 70, 200, 25);

            deleteBtn.addActionListener((event) -> {
                System.out.println("delete yo");
                try 
                {
                    ManagementServer obj = (ManagementServer) Naming.lookup("rmi://localhost:1040/manage");
                    obj.DeleteItems();
                } 
                catch (Exception e) 
                {

                }
            });

            frame.add(deleteBtn);
        }

        addBtn.addActionListener((event) -> {
            try 
            {
                ManagementServer obj = (ManagementServer) Naming.lookup("rmi://localhost:1040/manage");
                obj.AddNewItems();
            } 
            catch (Exception e) 
            {

            }
            System.out.println("add yo");
        });

        editBtn.addActionListener((event) -> {
            System.out.println("edit yo");
            System.out.println(user.getUsername());
            try 
            {
                ManagementServer obj = (ManagementServer) Naming.lookup("rmi://localhost:1040/manage");
                obj.EditItem();
            } 
            catch (Exception e) 
            {

            }
        });

        frame.setLayout(new BorderLayout());
        frame.setSize(300, 250);
        frame.setVisible(true);
    }

    public static void mainFrame(User user) {
        if (!Objects.isNull(user)) {
            JFrame frame = new JFrame("Food Ordering System - Logged in as: " + user.getUsername());
            JButton logout = new JButton("Logout");
            JLabel tField = new JLabel("Dashboard");
            JButton menuBtn = new JButton("Menu");

            frame.add(logout);
            frame.add(tField);
            logout.setBounds(200, 200, 200, 25);
            tField.setBounds(275, 0, 200, 25);

            logout.addActionListener((event) -> {
                frame.setVisible(false);
                try {
                    UserServices.logout(user);
                } catch (SQLException e) {
                }
                loginGUI();
            });

            if (user.getRole() == 0) { // Customer
                JButton historyBtn = new JButton("Order History");
                menuBtn.setBounds(325, 75, 175, 25);
                historyBtn.setBounds(115, 75, 175, 25);
                frame.add(historyBtn);
                menuBtn.addActionListener((event) -> {
                    try {
                        MenuServer obj = (MenuServer) Naming.lookup("rmi://localhost:1040/menu");
                        obj.Menu();
                        
                    } catch (Exception e) {

                    }
                    //frame.setVisible(false);
                });
                historyBtn.addActionListener((event) -> {
                    String username = "";
                    try {
                        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/restaurent", "root", "root");
                        Statement stRead = conn.createStatement();
                        ResultSet rRead = stRead.executeQuery("SELECT * from USER_DETAILS");
                        while (rRead.next()) {
                            if (rRead.getBoolean("LOGGED_IN") == true) {
                                username = rRead.getString("USERNAME");
                            }
                        }
                        conn.close();
                        System.out.println("Username: " + user.getUsername());
                        HistoryServer obj = (HistoryServer) Naming.lookup("rmi://localhost:1040/history");
                        obj.showGUI(user.getUsername());
                    } catch (Exception e) {

                    }
                });
            }

            if (user.getRole() == 1) { // Employee
                JButton inventoryBtn = new JButton("Inventory");
                inventoryBtn.setBounds(115, 75, 175, 25);
                menuBtn.setBounds(325, 75, 175, 25);
                frame.add(inventoryBtn);
                menuBtn.addActionListener((event) -> {
                    menuGUI(user);
                });
                inventoryBtn.addActionListener((event) -> {
                    try
                    {
                        ManagementServer obj = (ManagementServer) Naming.lookup("rmi://localhost:1040/manage");
                        obj.ViewItems();
                    }
                    catch(Exception e)
                    {
                        
                    }
                    
                });
                
            }

            if (user.getRole() == 2) { // Admin 
                JButton inventoryBtn = new JButton("Inventory");
                inventoryBtn.setBounds(325, 110, 175, 25);
                frame.add(inventoryBtn);
                JButton empBtn = new JButton("Employee");
                frame.add(empBtn);
                JButton cusBtn = new JButton("Customer Database");
                cusBtn.setBounds(115, 110, 175, 25);
                cusBtn.addActionListener((event) -> {
                try {
                    userTableGUI(0);
                } catch (Exception e) {
                }
                });
                
                inventoryBtn.addActionListener((event) -> {
                    try
                    {
                        ManagementServer obj = (ManagementServer) Naming.lookup("rmi://localhost:1040/manage");
                        obj.ViewItems();
                    }
                    catch(Exception e)
                    {
                        
                    }
                    
                });
                
                
                frame.add(cusBtn);
                

                empBtn.setBounds(115, 75, 175, 25);
                menuBtn.setBounds(325, 75, 175, 25);

                menuBtn.addActionListener((event) -> {
                    menuGUI(user);
                });

                empBtn.addActionListener((event) -> {
                employeeGUI();
                });
            }

            frame.add(menuBtn);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.setSize(625, 300);
            frame.setVisible(true);
        } else {
            loginGUI();
        }
    }
}
