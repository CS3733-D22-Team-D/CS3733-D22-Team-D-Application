package edu.wpi.DapperDaemons.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectionHandler {
  static Connection connection;

  static connectionType type;

  public enum connectionType {
    EMBEDDED,
    CLIENTSERVER
  }

  static {
    switchToEmbedded();
  }

  private connectionHandler() {}

  public static void init() {}

  public static connectionType getType() {
    return type;
  }

  public static Connection getConnection() {
    return connection;
  }

  public static boolean switchToClientServer() {
    try {
      csvSaver.saveAll();
      Class.forName("org.apache.derby.jdbc.ClientDriver");
      connection =
          DriverManager.getConnection("jdbc:derby://localhost:1527/BaW_Database;create=true");
      System.out.println("Connected to the client server");
      csvLoader.loadAll();
      type = connectionType.CLIENTSERVER;
    } catch (SQLException e) {
      //      System.out.println("Database already created, continuing");
      type = connectionType.EMBEDDED;
      return false;
    } catch (ClassNotFoundException e) {
      System.out.println("Driver error, try making sure you don't have any other instances open!");
      type = connectionType.EMBEDDED;
      return false;
    }
    try {
      DAOPouch.init();
    } catch (Exception e) {
      System.out.println("DAOPouch could not initialize");
    }
    return true;
  }

  public static boolean switchToEmbedded() {
    try {
      if (connection != null) {
        csvSaver.saveAll();
      }
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      connection = DriverManager.getConnection("jdbc:derby:BaW_database;create = true");
      csvLoader.loadAll();
      System.out.println("Connected to the embedded server");
      type = connectionType.EMBEDDED;
    } catch (SQLException e) {
      //      System.out.println("Database already created, continuing");
      type = connectionType.CLIENTSERVER;
      return false;
    } catch (ClassNotFoundException e) {
      //      System.out.println("Driver error, try making sure you don't have any other instances
      // open!");
      type = connectionType.CLIENTSERVER;
      return false;
    }
    try {
      DAOPouch.init();
    } catch (Exception e) {
      System.out.println("DAOPouch could not initialize");
    }
    return true;
  }
}
