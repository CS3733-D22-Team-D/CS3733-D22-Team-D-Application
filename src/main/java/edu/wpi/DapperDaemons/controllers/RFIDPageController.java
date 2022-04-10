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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    sLabel.setText("");
  }

  @FXML
  public void loginRFID() throws IOException {
    RFIDMachine rfid =
        new RFIDMachine(
            new Employee("Sam", "Karkache", "Jan2 2002", Employee.EmployeeType.ADMINISTRATOR, 5),
            "COM4");
    RFIDMachine.LoginState state = rfid.login();
    if (state.equals(RFIDMachine.LoginState.SUCCESS)) {
      sLabel.setText("Success");
      goHome();
    } else {
      sLabel.setText("Failure");
      goToLogin();
    }
  }

  @FXML
  public void setScanning() {
    sLabel.setText("Waiting for scan...");
  }
}

