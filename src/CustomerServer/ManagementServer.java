package CustomerServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ManagementServer extends Remote
{
    public void AddNewItems() throws RemoteException;
    public void DeleteItems() throws RemoteException;
    public void EditItem() throws RemoteException;
    public void ViewItems() throws RemoteException;
}
