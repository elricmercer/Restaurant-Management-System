package LoginRegisterServer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class UserServices {
    
    

  public static User authenticateUser(User user) throws SQLException {

    if (!Database.tableExist("USER_DETAILS")) {
      return null;
    }

    User foundUser = null;
    PreparedStatement ps = null;

    ResultSet rs = Database.connect().createStatement().executeQuery("select * from USER_DETAILS");

    while (rs.next()) {
      String username = rs.getString(rs.findColumn("username"));
      String password = rs.getString(rs.findColumn("password"));

      if (user.getUsername().equals(username) && PasswordUtil.verify(user.getPassword(), password)) {
        foundUser = new User(rs.getString(rs.findColumn("first_name")), rs.getString(rs.findColumn("last_name")),
            rs.getString(rs.findColumn("ic_number")), rs.getString(rs.findColumn("username")), rs.getBoolean(rs.findColumn("logged_in")), rs.getInt(rs.findColumn("role")));
        ps = Database.connect().prepareStatement("update USER_DETAILS set logged_in=? where username=?");
        ps.setBoolean(1, true);
        ps.setString(2, foundUser.getUsername());
        ps.executeUpdate();
        ps.close();
      }
    }

    rs.close();

    return foundUser;
  }

  public static boolean registerUser(User user, int role) throws SQLException {
    if (!usernameExists(user.getUsername())) {
      PreparedStatement ps = Database.connect().prepareStatement("insert into USER_DETAILS values(?, ?, ?, ?, ?, ?, ?)");
      ps.setString(1, user.getFirstName());
      ps.setString(2, user.getLastName());
      ps.setString(3, user.getIcNumber());
      ps.setString(4, user.getUsername());
      ps.setString(5, PasswordUtil.hash(user.getPassword()));
      ps.setInt(6, role);
      ps.setBoolean(7, false);
      ps.executeUpdate();
      ps.close();
      return true;
    }

    return false;
  }

  public static boolean usernameExists(String username) throws SQLException {

    boolean flag = false;

    ResultSet rs = Database.connect().createStatement().executeQuery("select * from USER_DETAILS");

    while (rs.next()) {

      String dbUsername = rs.getString(rs.findColumn("username"));

      if (username.equals(dbUsername)) {
        flag = true;
        break;
      }
    }

    return flag;
  }
  
  public static void logout(User user) throws SQLException {

    System.out.println("yo = " + user.getUsername());

    PreparedStatement ps = null;

    ps = Database.connect().prepareStatement("update USER_DETAILS set logged_in=? where username=?");

    ps.setBoolean(1, false);
    ps.setString(2, user.getUsername());

    ps.executeUpdate();
    ps.close();
}
    public static ArrayList<User> fetch(int role) throws SQLException {
        ArrayList<User> lists = new ArrayList();

        ResultSet rs = Database.connect().createStatement().executeQuery("SELECT * FROM USER_DETAILS WHERE ROLE=" + role);

        while (rs.next()) {

            User user = new User(rs.getString(rs.findColumn("first_name")), rs.getString(rs.findColumn("last_name")), rs.getString(rs.findColumn("ic_number")), rs.getString(rs.findColumn("username")), rs.getBoolean(rs.findColumn("logged_in")), rs.getInt(rs.findColumn("role")));

            lists.add(user);
        }

        rs.close();

        return lists;
    }

    public static boolean removeUser(User user) throws SQLException {
        boolean removed = false;

        System.out.println("removed " + user.getUsername());

        PreparedStatement ps = Database.connect().prepareStatement("delete from USER_DETAILS where username=?");
        ps.setString(1, user.getUsername());

        removed = (ps.executeUpdate() == 1) ? true : false;

        ps.close();

        return removed;
    }

    public static boolean updateUser(User user) throws SQLException {
        boolean updated = false;

        PreparedStatement ps = Database.connect().prepareStatement("update USER_DETAILS set first_name=?, last_name=?, ic_number=? where username=?");
        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setString(3, user.getIcNumber());
        ps.setString(4, user.getUsername());
        int res = ps.executeUpdate();

        updated = (res == 1) ? true : false;

        ps.close();

        return updated;
}
}

