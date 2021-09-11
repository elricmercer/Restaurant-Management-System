package LoginRegisterServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;


public class ClientRegistrationHandler implements Runnable {

  private final Socket client;
  private ObjectOutputStream oOutputStream = null;
  private User user = null;
  private int role;

  public ClientRegistrationHandler(Socket client, User user, int role) throws IOException {
    this.client = client;
    this.user = user;
    this.role = role;
  }

  @Override
  public void run() {
    while (true) {
      System.out.println("Register handler called");
      boolean registered = false;

      try {
        registered = UserServices.registerUser(user, role);
      } catch(SQLException e) {e.printStackTrace();}

      try {
        oOutputStream = new ObjectOutputStream(client.getOutputStream());
        oOutputStream.writeBoolean(registered);
        oOutputStream.flush();
      } catch (IOException ioe){}

      break;
    }
  }
}

