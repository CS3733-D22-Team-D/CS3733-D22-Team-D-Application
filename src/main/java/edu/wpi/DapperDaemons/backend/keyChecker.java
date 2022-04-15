package edu.wpi.DapperDaemons.backend;

import edu.wpi.DapperDaemons.entities.TableObject;
import java.sql.*;

public class keyChecker {
  private keyChecker() {}

  public static boolean validID(TableObject type, String pk) throws SQLException {
    String tableName = type.tableName();

    String query = "SELECT * FROM " + tableName;
    Statement stmt = connectionHandler.getConnection().createStatement();
    ResultSet resultSet = stmt.executeQuery(query);
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
    String columnName = resultSetMetaData.getColumnName(1);
    stmt.close();

    query = "SELECT count(*) AS num FROM " + tableName + " WHERE " + columnName + " = ?";
    PreparedStatement prepStmt = connectionHandler.getConnection().prepareStatement(query);
    prepStmt.setString(1, pk);
    ResultSet rset = prepStmt.executeQuery();
    rset.next();
    boolean isValid = 1 == rset.getInt("num");
    prepStmt.close();
    return isValid;
  }
}
