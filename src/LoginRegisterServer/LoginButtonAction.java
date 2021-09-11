package LoginRegisterServer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class LoginButtonAction implements ActionListener {

  private final JFrame frame;
  private JTextField usernameField;
  private JPasswordField passwordField;
  private JLabel message;
  private boolean flag = false;
  private Socket client = null;
  private ObjectOutputStream oOutputStream = null;
  private ObjectInputStream oInputStream = null;

  public LoginButtonAction(JFrame frame, JTextField usernameField, JPasswordField passwordField) {
    super();
    this.frame = frame;
    this.message = new JLabel();
    this.usernameField = usernameField;
    this.passwordField = passwordField;
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    validate();

    if (flag == false) {

      String username = usernameField.getText();
      String password = new String(passwordField.getPassword());

      System.out.println(username + " " + password);

      try {
        this.client = new Socket("localhost", Port.PORT_NUMBER);

        oOutputStream = new ObjectOutputStream(client.getOutputStream());
        oOutputStream.writeObject(new User(username, password));
        oOutputStream.writeUTF("login");
        oOutputStream.writeInt(-1); // do not change this, the only solution i can think of to prevent socket blocking
        oOutputStream.flush();

        while (true) {
          oInputStream = new ObjectInputStream(client.getInputStream());
          User user = (User) oInputStream.readObject();

          if (Objects.isNull(user)) {
            JOptionPane.showMessageDialog(frame, "Login Failed: Username or Password Incorrect.");
          }

          if(!Objects.isNull(user)) {
            JOptionPane.showMessageDialog(frame, "Login Successful " + user.getFullName());
            /**
             * TODO: Send to next frame
             */
			System.out.println(user.getRole());
            GUI.mainFrame(user);
            frame.setVisible(false);
          }

          client.close();
          break;
        }
      } catch (IOException | ClassNotFoundException ioe) {
      }
    }

    message.setForeground(Color.RED);
    message.setOpaque(true);
    frame.add(message, BorderLayout.PAGE_END);
    frame.validate();
    frame.repaint();
  }

  private boolean validate() {
    if (usernameField.getText().isEmpty()) {
      flag = true;
      message.setText("Username can't be empty.");
      return flag;
    }

    if (passwordField.getPassword().length == 0) {
      flag = true;
      message.setText("Password can't be empty.");
      return flag;
    }

    return flag = false;
  }
}

