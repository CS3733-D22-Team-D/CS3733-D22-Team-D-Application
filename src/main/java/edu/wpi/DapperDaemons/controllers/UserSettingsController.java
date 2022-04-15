package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.controllers.helpers.SecuritySettingsHelper;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class UserSettingsController extends ParentController {

  @FXML private Circle profilePic1;

  @FXML private Text accountName1;
  @FXML private Text accountUserName;

  @FXML private Text securityLevel;
  @FXML private Label securityLevelDescription;

  @FXML private Label currentProtection;

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

    securityLevel.setText(Integer.toString(SecurityController.getUser().getSecurityClearance()));

    securityLevelDescription.setText(SecurityController.getUser().getSecurityDescription());

    currentProtection.setText(SecuritySettingsHelper.getSecurityAuthentication());

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

  /**
   * A short description of the security level the user has, can probably be moved to employee
   *
   * @param securityLevel
   */
  private void securityDescription(String securityLevel) {
    switch (securityLevel) {
      case "1":
        break;
      case "2":
        break;
      case "3":
        break;
      case "4":
        break;
      case "5":
        break;
    }
  }
}
