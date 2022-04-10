package edu.wpi.DapperDaemons.backend;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class connectionHandler {
  private static Connection connection;

  static {
    try {
      Class.forName("org.apache.derby.jdbc.ClientDriver");
      connection = DriverManager.getConnection("jdbc:derby://localhost:1527/BaW_Database");
    } catch (SQLException e) {
      //      System.out.println("Database already created, continuing");
    } catch (ClassNotFoundException e) {
      System.out.println("Driver error, try making sure you don't have any other instances open!");
    }
  }

  private connectionHandler() {}

  static Connection getConnection() {
    return connection;
  }

  static Connection switchToClientServer() throws IOException {
    ServerSocket ss = new ServerSocket(1527);
    Socket server = ss.accept();
    Socket client = new Socket("localhost",1527);
    return connection;
  }

  static Connection switchToEmbedded(){
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      connection = DriverManager.getConnection("jdbc:derby:BaW_database;create = true");
    } catch (SQLException e) {
      //      System.out.println("Database already created, continuing");
    } catch (ClassNotFoundException e) {
      //      System.out.println("Driver error, try making sure you don't have any other instances
      // open!");
    }
    return connection;
  }

}
