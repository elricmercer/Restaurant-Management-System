package ManagementServer;

import CustomerServer.ManagementServer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.table.DefaultTableModel;

public class ManSer extends UnicastRemoteObject implements ManagementServer
{
    public ManSer() throws RemoteException
    {
        super();
    }
    
    @Override
    public void AddNewItems() throws RemoteException
    {
        AddItem add = new AddItem();
        add.setVisible(true);
    }
    
    @Override
    public void DeleteItems() throws RemoteException
    {
        DeleteItem delete = new DeleteItem();
        delete.setVisible(true);
    }
    
    @Override
    public void EditItem() throws RemoteException
    {
        EditItem edit = new EditItem();
        edit.setVisible(true);
    }
    
    @Override
    public void ViewItems() throws RemoteException
    {
        ViewItems view = new ViewItems();
        ArrayList<Integer> itemID = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> price = new ArrayList<>();
        ArrayList<Integer> inItemID = new ArrayList<>();
        ArrayList<Integer> qty = new ArrayList<>();
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/restaurent","root","root");
            Statement stRead = conn.createStatement();
            ResultSet rRead = stRead.executeQuery("SELECT * FROM MENU_DETAILS");
            Statement st2Read = conn.createStatement();
            ResultSet r2Read = st2Read.executeQuery("SELECT * FROM INVENTORY");
            while(rRead.next())
            {
                itemID.add(rRead.getInt("FOOD_ID"));
                name.add(rRead.getString("FOOD_NAME"));
                price.add(rRead.getString("FOOD_PRICE"));
            }
            while(r2Read.next())
            {
                inItemID.add(r2Read.getInt("ITEM_ID"));
                qty.add(r2Read.getInt("QTY"));
            }
            for(int i=0;i<itemID.size();i++)
            {
                for(int j=0;j<inItemID.size();j++)
                {
                    if(Objects.equals(itemID.get(i), inItemID.get(j)))
                    {
                        String sQty = String.valueOf(qty.get(j));
                        String data[] = {name.get(i),sQty,price.get(i)};
                        DefaultTableModel tb = (DefaultTableModel)view.jTable1.getModel();
                        tb.addRow(data);
                    }
                }
            }
            conn.close();
        }
        catch(Exception e)
        {
            
        }
        view.setVisible(true);
    }
}
