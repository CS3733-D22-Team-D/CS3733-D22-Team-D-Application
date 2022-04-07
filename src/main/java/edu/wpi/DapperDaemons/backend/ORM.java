package edu.wpi.DapperDaemons.backend;

import edu.wpi.DapperDaemons.entities.TableObject;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ORM<T extends TableObject> {

  Connection conn = connectionHandler.getConnection();
  Supplier<T> supplier;
  int numAttributes;
  String tableName;
  ArrayList<String> columnNames = new ArrayList<>();
  String initString;

  public ORM(T type) throws SQLException, IOException {
    this.supplier = type;
    initString = type.getTableInit();
    tableName = type.getTableName();

    Statement stmt = conn.createStatement();
    try {
      stmt.execute(initString);
    } catch (SQLException e) {
      System.out.println("Table already created");
    }
    String query = "SELECT * FROM " + tableName;
    ResultSet resultSet = stmt.executeQuery(query);
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
    this.numAttributes = resultSetMetaData.getColumnCount();
    for (int i = 1; i <= numAttributes; i++) {
      columnNames.add(resultSetMetaData.getColumnName(i));
    }
    stmt.close();
  }

  public ORM(T type, String filename) throws SQLException, IOException {
    this.supplier = type;
    initString = type.getTableInit();
    tableName = type.getTableName();

    Statement stmt = conn.createStatement();
    try {
      stmt.execute(initString);
    } catch (SQLException e) {
      System.out.printf("%s table already created\n", tableName);
    }
    String query = "SELECT * FROM " + tableName;
    ResultSet resultSet = stmt.executeQuery(query);
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
    this.numAttributes = resultSetMetaData.getColumnCount();
    for (int i = 1; i <= numAttributes; i++) {
      columnNames.add(resultSetMetaData.getColumnName(i));
    }
    stmt.close();
  }

  private T getInstance() {
    return supplier.get();
  }

  public T get(String primaryKey) throws SQLException {
    String query = "SELECT * FROM " + tableName;
    query += " WHERE " + columnNames.get(0);
    query += " = ?";
    PreparedStatement prepStmt = conn.prepareStatement(query);
    prepStmt.setString(1, primaryKey);
    ResultSet rset = prepStmt.executeQuery();
    T newTemp = getInstance();
    if (rset.next()) {
      for (int i = 1; i <= numAttributes; i++) {
        newTemp.setAttribute(i, rset.getString(columnNames.get(i - 1)));
      }
    }
    prepStmt.close();
    return newTemp;
  }

  public List<T> getAll() throws SQLException {
    ArrayList<T> all = new ArrayList<>();
    Statement stmt = conn.createStatement();
    String query = "SELECT * FROM " + tableName;
    ResultSet rset = stmt.executeQuery(query);
    while (rset.next()) {
      T newTemp = getInstance();
      for (int i = 1; i <= numAttributes; i++) {
        newTemp.setAttribute(i, rset.getString(columnNames.get(i - 1)));
      }
      all.add(newTemp);
    }
    stmt.close();
    return all;
  }

  public void add(T newTableObject) throws SQLException {
    String updateStatement = "INSERT INTO " + tableName + " VALUES(";
    for (int i = 1; i < numAttributes; i++) {
      updateStatement += "?,";
    }
    updateStatement += "?)";
    PreparedStatement prepStmt = conn.prepareStatement(updateStatement);
    if (!keyChecker.validID(newTableObject, newTableObject.getAttribute(1))) {
      for (int i = 1; i <= numAttributes; i++) {
        prepStmt.setString(i, newTableObject.getAttribute(i));
      }
      prepStmt.executeUpdate();
    }
    prepStmt.close();
  }

  public void delete(String primaryKey) throws SQLException {
    String query = "DELETE FROM " + tableName + " WHERE " + columnNames.get(0) + " = ?";
    PreparedStatement prepStmt = conn.prepareStatement(query);
    prepStmt.setString(1, primaryKey);
    prepStmt.executeUpdate();
    prepStmt.close();
  }

  public void updateAttribute(String primaryKey, int columnNum, String newValue)
      throws SQLException {
    // alter T within tableList with nodeID/PrimaryKey and change value in columnNum to NewValue
    // Set the statement String to deal with the table handling T Objects
    T instance = getInstance();
    String statement = "UPDATE " + tableName + " SET " + columnNames.get(0) + " = ?,";
    for (int i = 2; i < numAttributes - 1; i++) {
      statement += columnNames.get(i - 1) + " = ?,";
    }
    statement += columnNames.get(numAttributes - 1) + " = ?";
    statement += "WHERE " + columnNames.get(0) + " = ?";

    // Iterate through the ? in the Statements and replace with values
    PreparedStatement prepStmt = conn.prepareStatement(statement);
    T temp = get(primaryKey);
    for (int i = 1; i < numAttributes; i++) {
      prepStmt.setString(i, temp.getAttribute(i));
    }
    prepStmt.setString(numAttributes, primaryKey);
    prepStmt.executeUpdate();
  }

  public void update(T type) throws SQLException {

    T instance = getInstance();
    String statement = "UPDATE " + tableName + " SET " + columnNames.get(1) + " = ?,";
    for (int i = 2; i < numAttributes - 1; i++) {
      statement += columnNames.get(i) + " = ?,";
    }
    statement += columnNames.get(numAttributes - 1) + " = ?";
    statement += " WHERE " + columnNames.get(0) + " = ?";

    // Iterate through the ? in the Statements and replace with values
    PreparedStatement prepStmt = conn.prepareStatement(statement);
    T temp = get(type.getAttribute(1));
    for (int i = 1; i < numAttributes; i++) {
      prepStmt.setString(i, type.getAttribute(i + 1));
    }
    prepStmt.setString(numAttributes, type.getAttribute(1));
    prepStmt.executeUpdate();
  }
}
