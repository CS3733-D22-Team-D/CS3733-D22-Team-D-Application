package edu.wpi.DapperDaemons;

import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.backend.csvLoader;
import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.wongSweeper.MinesweeperZN;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

  private static final Scanner in = new Scanner(System.in);
  private static final String username = "admin";
  private static final String password = "admin";

  public static synchronized void startApp(String[] args) throws SQLException, IOException {
    csvLoader.loadAll();
    DAOPouch.init();
    Employee admin = DAOPouch.getEmployeeDAO().get("admin");
    SecurityController.getInstance().setUser(admin);
    App.launch(App.class, args);
  }

  public static synchronized void startWong(String[] args) {
    MinesweeperZN.launch(MinesweeperZN.class, args);
  }

  public static void main(String[] args) throws IOException, SQLException {
    Thread app =
        new Thread(
            new Runnable() {
              @Override
              public void run() {
                try {
                  startApp(args);
                } catch (SQLException e) {
                  e.printStackTrace();
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            });
    Thread wong =
        new Thread(
            new Runnable() {

              @Override
              public void run() {
                startWong(args);
              }
            });

    app.start();
  }

  static String getID() {
    String nodeID;
    System.out.println("Enter Location node ID:");
    nodeID = in.next();
    return nodeID;
  }

  static boolean logIn() {
    // prompt user for username
    System.out.println("Enter Username:\n");
    String enteredUsername = in.next();

    // prompt user for password
    System.out.println("Enter Password:\n");
    String enteredPassword = in.next();

    // check if entered credentials are valid
    return enteredPassword.equals(password) & enteredUsername.equals(username);
  }

  public void playSound() {}
}
