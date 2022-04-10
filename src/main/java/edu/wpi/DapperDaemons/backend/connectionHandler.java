package edu.wpi.DapperDaemons.backend;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class connectionHandler {
  private static Connection connection;

  static {
    switchToEmbedded();
  }

  private connectionHandler() {}

  static Connection getConnection() {
    return connection;
  }

  static Connection switchToClientServer() throws IOException {
    try {
      csvSaver.saveAll();
      Class.forName("org.apache.derby.jdbc.ClientDriver");
      connection =
          DriverManager.getConnection("jdbc:derby://localhost:1527/BaW_Database;create=true");
      System.out.println("Connected to the client server");
      csvLoader.loadAll();
    } catch (SQLException e) {
      //      System.out.println("Database already created, continuing");
    } catch (ClassNotFoundException e) {
      System.out.println("Driver error, try making sure you don't have any other instances open!");
    }
    return connection;
  }

  static Connection switchToEmbedded() {
    try {
      if (connection != null) {
        csvSaver.saveAll();
      }
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      connection = DriverManager.getConnection("jdbc:derby:BaW_database;create = true");
      csvLoader.loadAll();
      System.out.println("Connected to the embedded server");
    } catch (SQLException e) {
      //      System.out.println("Database already created, continuing");
    } catch (ClassNotFoundException e) {
      //      System.out.println("Driver error, try making sure you don't have any other instances
      // open!");
    }
    return connection;
  }
}
