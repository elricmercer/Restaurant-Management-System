package LoginRegisterServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;

public class Server {

  public static void main(String[] args) throws IOException {
	ServerSocket server = new ServerSocket(Port.PORT_NUMBER);
	System.out.println("Server listening at Port: " + Port.PORT_NUMBER);

	while (true) {
	  Socket client = null;
	  try {
		client = server.accept();
		System.out.println("client connected: " + client);

		ObjectInputStream oInputStream = new ObjectInputStream(client.getInputStream());
		User user = (User) oInputStream.readObject();
		String option = (String) oInputStream.readUTF();

		int role = (int) oInputStream.readInt();
		System.out.println(role);


		System.out.println(user.getUsername());
		System.out.println(option);

		if (option.equals("login")) {
		  new Thread(new ClientLoginHandler(client, user)).start();
		}

		if (option.equals("register")) {
		  new Thread(new ClientRegistrationHandler(client, user, role)).start();
		}

	  } catch (Exception e) {
		client.close();
		server.close();
	  }
	}
  }
}
