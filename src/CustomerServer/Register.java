package CustomerServer;

import HistoryServer.HistorySer;
import LoginRegisterServer.UserServer;
import ManagementServer.ManSer;
import MenuServer.MenuSer;
import OrderServer.OrderSer;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Register 
{
    public static void main(String[] args) throws RemoteException
    {
        Registry reg = LocateRegistry.createRegistry(1040);
        reg.rebind("menu", new MenuSer());
        reg.rebind("history", new HistorySer());
        reg.rebind("order", new OrderSer());
        reg.rebind("manage", new ManSer());
        reg.rebind("users", new UserServer());
    }   
}
