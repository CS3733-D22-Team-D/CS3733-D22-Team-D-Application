package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.backend.preload.Images;
import edu.wpi.DapperDaemons.controllers.homePage.ThemeHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class UserSettingsController extends ParentController {

  @FXML private Circle profilePic1;

  @FXML private Text accountName1;
  @FXML private Text accountUserName;
  @FXML private Text accessLevel;
  @FXML private Label protectionMessage;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //    super.initialize(location, resources);

    String employeeName =
        SecurityController.getUser().getFirstName()
            + " "
            + SecurityController.getUser().getLastName();
    accountName1.setText(employeeName);
    accountUserName.setText(
        DAOPouch.getAccountDAO().filter(2, SecurityController.getUser().getNodeID()).values()
            .stream()
            .findFirst()
            .get()
            .getAttribute(1));
    accessLevel.setText(String.valueOf(SecurityController.getUser().getSecurityClearance()));
    protectionMessage.setText("Must make this refer to users 2FA setting");

    profilePic1.setFill(Images.getAccountImage());
  }

  @FXML
  private void lightSwitch() {
    ThemeHandler.toggleTheme(ThemeHandler.Theme.Light);
  }

  @FXML
  private void darkSwitch() {
    ThemeHandler.toggleTheme(ThemeHandler.Theme.Dark);
  }

  @FXML
  private void blueSwitch() {
    ThemeHandler.toggleTheme(ThemeHandler.Theme.Blue);
  }

  @FXML
  private void redSwitch() {
    ThemeHandler.toggleTheme(ThemeHandler.Theme.Red);
  }
}
