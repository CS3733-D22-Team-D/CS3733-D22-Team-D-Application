package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class UserSettingsController extends ParentController {

  @FXML private Circle profilePic1;

  @FXML private Text accountName1;
  @FXML private Text accountUserName;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //    super.initialize(location, resources);

    String employeeName =
        SecurityController.getUser().getFirstName()
            + " "
            + SecurityController.getUser().getLastName();
    accountName1.setText(employeeName);
    try {
      accountUserName.setText(
          DAOPouch.getAccountDAO().get(SecurityController.getUser().getNodeID()).getAttribute(1));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    profilePic1.setFill(
        new ImagePattern(
            new Image(
                Objects.requireNonNull(
                    getClass()
                        .getClassLoader()
                        .getResourceAsStream(
                            "edu/wpi/DapperDaemons/profilepictures/"
                                + SecurityController.getUser().getNodeID()
                                + ".png")))));
  }

  @FXML
  private void lightSwitch() {
    toggleTheme(Theme.Light);
  }

  @FXML
  private void darkSwitch() {
    toggleTheme(Theme.Dark);
  }

  @FXML
  private void blueSwitch() {
    toggleTheme(Theme.Blue);
  }

  @FXML
  private void redSwitch() {
    toggleTheme(Theme.Red);
  }
}
