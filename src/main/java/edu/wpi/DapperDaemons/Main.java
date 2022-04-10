package edu.wpi.DapperDaemons;

import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.backend.connectionHandler;
import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.wongSweeper.MinesweeperZN;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

  private static final Scanner in = new Scanner(System.in);
  private static final String username = "admin";
  private static final String password = "admin";

  public static void main(String[] args) throws IOException, SQLException {
    connectionHandler.init();
    connectionHandler.switchToClientServer();
    Employee admin = DAOPouch.getEmployeeDAO().get("admin");
    SecurityController.setUser(admin);
    App.launch(App.class, args);
  }

}
