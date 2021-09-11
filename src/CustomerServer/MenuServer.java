package CustomerServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MenuServer extends Remote
{
    public void Menu() throws RemoteException;
}
