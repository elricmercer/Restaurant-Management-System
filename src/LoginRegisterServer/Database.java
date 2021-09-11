package LoginRegisterServer;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.DriverManager;

public class Database {

  private static final String DB_URL = "jdbc:derby://localhost:1527/restaurent;create=true";

  public static Connection connect() throws SQLException {
    return DriverManager.getConnection(DB_URL,"root","root");
  }

  public static boolean tableExist(String tableName) throws SQLException {
    DatabaseMetaData meta = connect().getMetaData();
    ResultSet resultSet = meta.getTables(null, null, tableName, new String[] { "TABLE" });
    return resultSet.next();
  }
}
