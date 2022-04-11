package edu.wpi.DapperDaemons.controllers;

import arduino.Arduino;
import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.UnableToConnectException;
import edu.wpi.DapperDaemons.serial.RFIDMachine;
import edu.wpi.DapperDaemons.serial.SerialCOM;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

public class RFIDPageController extends UIController {

  @FXML private Label sLabel;
  @FXML private Label initLabel;
  @FXML private Label resultLabel;
  @FXML private Label helpLabel;
  @FXML private Button sButton;
  @FXML private ImageView homeIcon;
  @FXML private VBox sceneBox;
  @FXML private Button continueButton;
  @FXML private Button backButton;
  @FXML private Button initButton;
  @FXML private String COM;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    sLabel.setText("");
    initLabel.setText("");
    resultLabel.setText("");
    sButton.setVisible(false);
    continueButton.setVisible(false);
    backButton.setVisible(false);
  }

  @FXML
  public void initScanner() {
    Arduino arduino;
    SerialCOM serialCOM = new SerialCOM();
    try {
      arduino = serialCOM.setupArduino(); // can throw UnableToConnectException
    } catch (UnableToConnectException e) {
      resultLabel.setText("Unable to Connect");
      initLabel.setText("Initialization Error!");
      backButton.setVisible(true);
      return;
    }
    this.COM = arduino.getPortDescription();
    System.out.println(this.COM);
    initButton.setVisible(false);
    sButton.setVisible(true);
    initLabel.setVisible(false);
    sLabel.setText("Initialization Complete : Click to Scan!");
    helpLabel.setText(
        "The sensor has successfully initialized, you may press the button below to scan you RFID card");
  }

  @FXML
  public void loginRFID() {
    RFIDMachine rfid =
        new RFIDMachine(
            new Employee("RFID", "Test", "Jan2 2002", Employee.EmployeeType.ADMINISTRATOR, 5));
    RFIDMachine.LoginState state = rfid.login(this.COM);
    if (state.equals(RFIDMachine.LoginState.SUCCESS)) {
      resultLabel.setText(
          "Access Granted: Hello "
              + rfid.getEmployee().getFirstName()
              + " "
              + rfid.getEmployee().getLastName());
      sLabel.setText("");
      helpLabel.setText("");
      resultLabel.setTextFill(Paint.valueOf("#059DA7"));
      sButton.setVisible(false);
      backButton.setVisible(false);
      continueButton.setVisible(true);
    } else if (state.equals(RFIDMachine.LoginState.INVALIDUSER)) {
      sLabel.setText("");
      helpLabel.setText("");
      resultLabel.setTextFill(Paint.valueOf("#eb4034"));
      resultLabel.setText("Access Denied");
      sButton.setVisible(false);
      backButton.setVisible(true);
    } else if (state.equals(RFIDMachine.LoginState.TIMEOUT)) {
      sLabel.setText("");
      helpLabel.setText("");
      resultLabel.setTextFill(Paint.valueOf("#eb4034"));
      resultLabel.setText("RFID Scan Timeout: Please Try Again");
      backButton.setVisible(true);
      continueButton.setVisible(false);
    } else if (state.equals(RFIDMachine.LoginState.UNABLETOCONNECT)) {
      sLabel.setText("");
      helpLabel.setText("");
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
  public void initText() {
    initLabel.setText("Initializing Sensor...");
  }
}
