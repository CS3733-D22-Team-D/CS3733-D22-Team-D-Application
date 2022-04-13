package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.serial.RFIDMachine;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

public class RFIDPageController extends AppController {

  @FXML private Label sLabel;
  @FXML private Label resultLabel;
  @FXML private Button sButton;
  @FXML private Button continueButton;
  @FXML private Button backButton;
  public static String COM;
  public static String errorOS;

  /**
   * On page init, determines status of Arduino port
   * @param location
   * The location used to resolve relative paths for the root object, or
   * {@code null} if the location is not known.
   *
   * @param resources
   * The resources used to localize the root object, or {@code null} if
   * the root object was not localized.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    if (errorOS != null) { // The Arduino Lib only works on Windows, other operating systems will get caught here
      resultLabel.setText("Unsupported Operating system: " + errorOS);
      resultLabel.setTextFill(Paint.valueOf("#eb4034"));
      sLabel.setText("OS Error");
      backButton.setVisible(true);
      continueButton.setVisible(false);
    } else if (COM == null) { // The Arduino was not detected, possibly not plugged in
      resultLabel.setText("Unable to Connect");
      resultLabel.setTextFill(Paint.valueOf("#eb4034"));
      sLabel.setText("Initialization Error");
      backButton.setVisible(true);
      continueButton.setVisible(false);
    } else { //the Arduino was detected on the given port
      System.out.println("Port was determined to be: " + COM);
      sButton.setVisible(true);
      backButton.setVisible(false);
      continueButton.setVisible(false);
      resultLabel.setText("");
      sLabel.setText("Ready to Scan");
    }
  }

  /**
   * Conducts the overall RFID scan, invoked by the SCAN NOW button on the FXML page
   */
  @FXML
  public void loginRFID() {
    RFIDMachine rfid =
        new RFIDMachine(
            new Employee("RFID", "Test", "Jan2 2002", Employee.EmployeeType.ADMINISTRATOR, 5));
    RFIDMachine.LoginState state = rfid.login(COM);
    if (state.equals(RFIDMachine.LoginState.SUCCESS)) {
      resultLabel.setText(
          "Access Granted: Hello "
              + rfid.getEmployee().getFirstName()
              + " "
              + rfid.getEmployee().getLastName());
      sLabel.setText("");
      resultLabel.setTextFill(Paint.valueOf("#059DA7"));
      sButton.setVisible(false);
      backButton.setVisible(false);
      continueButton.setVisible(true);
    } else if (state.equals(RFIDMachine.LoginState.INVALIDUSER)) {
      sLabel.setText("");
      resultLabel.setTextFill(Paint.valueOf("#eb4034"));
      resultLabel.setText("Access Denied");
      sButton.setVisible(false);
      backButton.setVisible(true);
    } else if (state.equals(RFIDMachine.LoginState.TIMEOUT)) {
      sLabel.setText("");
      resultLabel.setTextFill(Paint.valueOf("#eb4034"));
      resultLabel.setText("RFID Scan Timeout: Please Try Again");
      backButton.setVisible(true);
      continueButton.setVisible(false);
    } else if (state.equals(RFIDMachine.LoginState.UNABLETOCONNECT)) {
      sLabel.setText("");
      resultLabel.setTextFill(Paint.valueOf("#eb4034"));
      resultLabel.setText("Unable to Connect to RFID Sensor");
      backButton.setVisible(true);
    }
  }

  /**
   * sets the label outside the RFID login method
   * (It breaks if put into the RFID login method idk)
   */
  @FXML
  public void setScanning() {
    sLabel.setText("Waiting for scan...");
  }

  /**
   * changes to login page
   * @throws IOException if unable to change
   */
  @FXML
  public void goToLogin() throws IOException {
    switchScene("login.fxml", 780, 548);
  }

  /**
   * changes to go home
   * @throws IOException
   */
  @FXML
  public void goHome() throws IOException {
    switchScene("default.fxml", 635, 510);
  }
}
