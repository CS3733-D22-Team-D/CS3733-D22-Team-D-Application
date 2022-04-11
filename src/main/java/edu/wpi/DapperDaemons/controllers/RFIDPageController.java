package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.serial.RFIDMachine;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class RFIDPageController extends UIController {

  @FXML private Label sLabel;
  @FXML private Button sButton;
  @FXML private ImageView homeIcon;
  @FXML private VBox sceneBox;
  @FXML private Button continueButton;
  @FXML private Button backButton;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    sLabel.setText("");
    continueButton.setVisible(false);
    backButton.setVisible(false);
  }

  @FXML
  public void loginRFID() throws IOException {
    RFIDMachine rfid =
        new RFIDMachine(
            new Employee("RFID", "Test", "Jan2 2002", Employee.EmployeeType.ADMINISTRATOR, 5),
            "COM4");
    RFIDMachine.LoginState state = rfid.login();
    if (state.equals(RFIDMachine.LoginState.SUCCESS)) {
      sLabel.setText(
          "Access Granted: Hello "
              + rfid.getEmployee().getFirstName()
              + " "
              + rfid.getEmployee().getLastName());
      sButton.setVisible(false);
      backButton.setVisible(false);
      continueButton.setVisible(true);
    } else if (state.equals(RFIDMachine.LoginState.INVALIDUSER)) {
      sLabel.setText("Access Denied");
      sButton.setVisible(false);
      backButton.setVisible(true);
    } else if (state.equals(RFIDMachine.LoginState.TIMEOUT)) {
      sLabel.setText("RFID Scan Timeout: Please Try Again");
      backButton.setVisible(true);
      continueButton.setVisible(false);
    } else if (state.equals(RFIDMachine.LoginState.UNABLETOCONNECT)) {
      sLabel.setText(
          "Unable to Connect to RFID Sensor"); // TODO: Get list of COM Ports to attempt to
      // reconnect
      backButton.setVisible(true);
    }
  }

  @FXML
  public void setScanning() {
    sLabel.setText("Waiting for scan...");
  }
}
