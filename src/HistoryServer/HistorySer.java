package HistoryServer;

import CustomerServer.HistoryServer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Voodoo
 */
public class HistorySer extends UnicastRemoteObject implements HistoryServer {

    public HistorySer() throws RemoteException{
        super();
    }

    @Override
    public void showGUI(String username) throws RemoteException {
        HistoryView hv = new HistoryView();
        hv.showGUI(username);
    }
}
