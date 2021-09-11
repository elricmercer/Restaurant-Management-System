package LoginRegisterServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface UserInterface extends Remote{
  
  public ArrayList<User> fetch(int role) throws RemoteException;
  public boolean removeUser(User user) throws RemoteException;
  public boolean updateUser(User user) throws RemoteException;
  
}
