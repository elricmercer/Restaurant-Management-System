package OrderServer;

import CustomerServer.OrderServer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

public class OrderSer extends UnicastRemoteObject implements OrderServer
{  
    public OrderSer()throws RemoteException
    {
        super();
    }
    
    @Override
    public void list(ArrayList<String> items) throws RemoteException
    {
        GUI_1 g = new GUI_1();
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/restaurent","root","root");
            Random rand = new Random();
            Statement stRead = conn.createStatement();
            ResultSet r1Read = stRead.executeQuery("SELECT * from ORDER_DETAILS");
            boolean loop1 = true;
            int orderID=0;
            while(loop1)
            {
                boolean flag = false;
                int tempOrderNo = rand.nextInt(1000)+1;
                while(r1Read.next())
                {
                    int orderNo = (r1Read.getInt("ORDER_ID")); 
                    if(tempOrderNo==orderNo)
                       flag = true;
                }
                if(flag==false)
                {
                    orderID = tempOrderNo;
                    loop1 = false;
                } 
            }
            conn.close();
            double total=0.0;
            g.jTextArea1.append("NAME\t QUANTITY\t PRICE\t TOTAL PRICE");
            g.jTextArea1.append("\n\n");
            for(int i=0;i<items.size();i+=4)
            {
                double price = Double.parseDouble(items.get(i+3));
                int qty = Integer.parseInt(items.get(i+2));
                double sum = price*qty;
                total = total + sum;
                g.jTextArea1.append(items.get(i+1)+"\t "+items.get(i+2)+"\t "+items.get(i+3)+"\t "+sum);
                g.jTextArea1.append("\n");
            }
            g.total = total;
            g.id = orderID;
            g.items = items;
            g.jTextArea1.append("\n\n");
            g.jTextArea1.append("TOTAL\t= RM"+total);
        }
        catch(Exception e)
        {
            
        }
        g.setVisible(true);
    }
}
