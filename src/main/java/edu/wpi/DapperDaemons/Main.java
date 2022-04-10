package edu.wpi.DapperDaemons;

import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.backend.csvLoader;
import edu.wpi.DapperDaemons.entities.Employee;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

  private static final Scanner in = new Scanner(System.in);
  private static final String username = "admin";
  private static final String password = "admin";

  public static void main(String[] args) throws IOException, SQLException {
    csvLoader.loadAll();
    DAOPouch.init();
    Employee admin = DAOPouch.getEmployeeDAO().get("admin");
    SecurityController.setUser(admin);
    App.launch(App.class, args);

    // Backend stuff
    /*ORMController ormc = new ORMController();
    boolean running;
    // continue with program if credentials are valid
    running = logIn();
    if (running) {
      System.out.println("Login Successful\n");
    } else {
      System.out.println("Login Unsuccessful\n");
    }

    // start of loop
    int resp;
    while (running) {
      // prompt user for task to perform
      System.out.println(
          "1 – Location Information\n"
              + "2 – Change Floor and Type\n"
              + "3 – Enter Location\n"
              + "4 – Delete Location\n"
              + "5 – Save Locations to CSV file\n"
              + "6 – Exit Program");
      resp = in.nextInt();
      String nodeID;
      switch (resp) {
        case 1:
          ormc.getInformation();
          break;
        case 2:
          // confirm that location being edited exists
          nodeID = null;
          boolean validEntry = false;
          while (!validEntry) {
            nodeID = getID();
            if (ormc.validID(nodeID)) {
              validEntry = true;
            } else {
              System.out.println("Location with that ID does not exist. Please enter valid ID");
            }
          }
          // prompt user for the new Floor for Node
          System.out.println("Enter New Floor:\n");
          String floor = in.next();
          // set new Floor for the Node
          //          ormc.changeFloor(nodeID, floor);
          // prompt user for the new Type for Node
          System.out.println("Enter New Node Type:\n");
          String type = in.next();
          // set new Type for the Node
          //          ormc.changeType(nodeID, type);
          break;
        case 3:
          nodeID = getID();
          while (ormc.validID(nodeID) & !nodeID.equals("x")) {
            System.out.println(
                "Location with that ID already exists. Please enter a new ID or type x to exit");
            nodeID = getID();
          }
          ormc.addLocation(nodeID);
          break;
        case 4:
          nodeID = getID();
          while (!ormc.validID(nodeID) & !nodeID.equals("x")) {
            System.out.println(
                "Location with that ID doesn't exist. Please enter a new ID or type x to exit");
            nodeID = getID();
          }
          ormc.deleteLocation(nodeID);
          break;
        case 5:
          System.out.println("Enter the name of the file:");
          String filename = in.next();
          if (filename.indexOf(".csv") != filename.length() - 4) {
            ormc.saveToCSV(filename + ".csv");
          } else ormc.saveToCSV(filename);
          break;
        case 6:
          ormc.exitProgram();
          running = false;
          break;
        default:
          System.out.println("Not a valid input!");
          break;
      }
    }*/
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
