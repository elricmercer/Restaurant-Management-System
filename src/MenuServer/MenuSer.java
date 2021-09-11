package MenuServer;

import CustomerServer.MenuServer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MenuSer extends UnicastRemoteObject implements MenuServer
{
    public MenuSer() throws RemoteException
    {
        super();
    }
    
    @Override
    public void Menu() throws RemoteException
    {
        MS_GUI_1 g = new MS_GUI_1();
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/restaurent","root","root");
            Statement stRead = conn.createStatement();
            ResultSet rRead = stRead.executeQuery("SELECT * from MENU_DETAILS");
            int i=0;
            while(rRead.next())
            {
                g.jComboBox1.addItem(rRead.getString("FOOD_NAME"));
            }
            conn.close();
        }
        catch(Exception e)
        {
            
        }
        g.setVisible(true);
    }
}
