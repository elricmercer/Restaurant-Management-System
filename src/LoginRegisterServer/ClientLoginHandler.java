package LoginRegisterServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientLoginHandler implements Runnable {

  private final Socket client;
  private ObjectOutputStream oOutputStream;
  private User user;

  public ClientLoginHandler(Socket client, User user) throws IOException {
    this.client = client;
    this.user = user;
  }

  @Override
  public void run() {
    while (true) {
      try {

        oOutputStream = new ObjectOutputStream(client.getOutputStream());
        oOutputStream.writeObject(UserServices.authenticateUser(user));
        oOutputStream.flush();
        
      } catch (Exception e){
        e.printStackTrace();
      }
      break;
    }
  }
}

