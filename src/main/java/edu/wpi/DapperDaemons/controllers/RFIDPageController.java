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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    if (errorOS != null) {
      resultLabel.setText("Invalid Operating system: " + errorOS);
      resultLabel.setTextFill(Paint.valueOf("#eb4034"));
      sLabel.setText("Initialization Error");
      backButton.setVisible(true);
      continueButton.setVisible(false);
    } else if (COM == null) {
      resultLabel.setText("Unable to Connect");
      resultLabel.setTextFill(Paint.valueOf("#eb4034"));
      sLabel.setText("Initialization Error");
      backButton.setVisible(true);
      continueButton.setVisible(false);
    } else {
      System.out.println("Port was determined to be: " + COM);
      sButton.setVisible(true);
      backButton.setVisible(false);
      continueButton.setVisible(false);
      resultLabel.setText("");
      sLabel.setText("Ready to Scan");
    }
  }

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

  @FXML
  public void setScanning() {
    sLabel.setText("Waiting for scan...");
  }

  @FXML
  public void goToLogin() throws IOException {
    switchScene("login.fxml", 780, 548);
  }

  @FXML
  public void goHome() throws IOException {
    switchScene("default.fxml", 635, 510);
  }
}
