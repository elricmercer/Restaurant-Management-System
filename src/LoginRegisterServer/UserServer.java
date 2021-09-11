package LoginRegisterServer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import LoginRegisterServer.GUI;
import LoginRegisterServer.User;
import LoginRegisterServer.UserServices;

public class UserServer extends UnicastRemoteObject implements UserInterface {

  public UserServer() throws RemoteException {
	super();
  }

  @Override
  public ArrayList<User> fetch(int role) throws RemoteException {
	ArrayList<User> lists = null;
	try {
	  lists = UserServices.fetch(role);
	} catch (Exception e){}
	
	return lists;
  }

  @Override
  public boolean removeUser(User user) throws RemoteException {
	boolean removed = false;
	try {
	  removed = UserServices.removeUser(user);
	} catch (Exception e) {
	  e.printStackTrace();
	}
	return removed;
  }

  @Override
  public boolean updateUser(User user) throws RemoteException {
	boolean updated = false;
	try {
	  updated = UserServices.updateUser(user);
	} catch (Exception e) {
	  e.printStackTrace();
	}
	return updated;
  }
}
