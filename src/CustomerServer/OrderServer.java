package CustomerServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface OrderServer extends Remote
{
    public void list(ArrayList<String> items) throws RemoteException;
}
