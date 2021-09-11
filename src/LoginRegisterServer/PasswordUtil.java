package LoginRegisterServer;

public class PasswordUtil {

  public static String hash(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt(10));
  }

  public static boolean verify(String password, String hashedPassword) {
    return BCrypt.checkpw(password, hashedPassword);
  }
}
