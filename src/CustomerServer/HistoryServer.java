package CustomerServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HistoryServer extends Remote
{
    public void showGUI(String username) throws RemoteException;
}
