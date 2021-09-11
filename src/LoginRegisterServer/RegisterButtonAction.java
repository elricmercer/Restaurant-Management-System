package LoginRegisterServer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class RegisterButtonAction implements ActionListener {

  private final JFrame frame;
  private final JTextField textFields[];
  private final JPasswordField passwordField;
  private Socket client = null;
  private ObjectOutputStream oOutputStream = null;
  private ObjectInputStream oInputStream = null;
  private JLabel message = null;
  private boolean flag = false;
  private int role;

  public RegisterButtonAction(JFrame frame, JTextField textFields[], JPasswordField passwordField, int role) {
    super();
    this.frame = frame;
    this.textFields = textFields;
    this.passwordField = passwordField;
    this.message = new JLabel();
    this.role = role;
  }

  @Override
  public void actionPerformed(ActionEvent ae) {

    validate();

    if (!flag) {
      try {
        this.client = new Socket("localhost", Port.PORT_NUMBER);

        User newUser = new User(textFields[0].getText(), textFields[1].getText(), textFields[2].getText(),
            textFields[3].getText(), new String(passwordField.getPassword()));

        oOutputStream = new ObjectOutputStream(client.getOutputStream());
        oOutputStream.writeObject(newUser);
        oOutputStream.writeUTF("register");
        oOutputStream.writeInt(role);
        oOutputStream.flush();

        while (true) {

          oInputStream = new ObjectInputStream(client.getInputStream());

          boolean status = oInputStream.readBoolean();

          if(status) {
            JOptionPane.showMessageDialog(frame, "Successfully Registered");
          frame.setVisible(false);
          } else {
            JOptionPane.showMessageDialog(frame, "Sorry that username already exist.");
          }

          this.client.close();
          break;
        }
      } catch (IOException e) {
      }
    }

    message.setForeground(Color.RED);
    message.setOpaque(true);
    frame.add(message, BorderLayout.PAGE_END);
    frame.validate();
    frame.repaint();
  }

  private boolean validate() {

    if (textFields[0].getText().isEmpty()) {
      message.setText("First Name Field should not be Empty");
      return flag = true;
    }

    if (textFields[1].getText().isEmpty()) {
      message.setText("Last Name Field should not be Empty");
      return flag = true;
    }

    if (textFields[2].getText().isEmpty()) {
      message.setText("IC/Passport Field should not be Empty");
      return flag = true;
    }

    if (textFields[3].getText().isEmpty()) {
      message.setText("Username Field should not be Empty");
      return flag = true;
    }

    if (passwordField.getPassword().length == 0) {
      message.setText("Password Field should not be Empty");
      return flag = true;
    }

    return flag = false;
  }
}