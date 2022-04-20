package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.DAOFacade;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.controllers.homePage.AccountHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class UserHomeController extends ParentController {

  // account profile
  @FXML private Circle profilePic;
  @FXML private Text accountName;
  @FXML private Text accountUserName;

  // home
  @FXML private Label fullName;
  @FXML private Label birthday;
  @FXML private Label email;
  @FXML private Text securityLevel;
  @FXML private Label securityLevelDescription;

  // switch pages
  @FXML
  public void openAboutUs(ActionEvent event) {
    swapPage("aboutUs", "About Us");
  }

  @FXML
  public void goUserHome(ActionEvent event) {
    swapPage("userHome", "User Home");
  }

  @FXML
  public void openAccountSettings(ActionEvent event) {
    swapPage("userAccountSettings", "User Account Settings");
  }

  @FXML
  public void openUserSecurity(ActionEvent event) {
    swapPage("userSecurity", "User Security");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //    super.initialize(location, resources);

    // set employee name and username
    String employeeName =
        SecurityController.getUser().getFirstName()
            + " "
            + SecurityController.getUser().getLastName();
    accountName.setText(employeeName);
    fullName.setText(employeeName);

    accountUserName.setText(DAOFacade.getUserAccount().getAttribute(1));

    // set security access level
    securityLevel.setText(Integer.toString(SecurityController.getUser().getSecurityClearance()));

    securityLevelDescription.setText(SecurityController.getUser().getSecurityDescription());

    // set profile picture
    new AccountHandler(accountName, profilePic);

    //    profilePic.setFill(
    //        new ImagePattern(
    //            new Image(
    //                Objects.requireNonNull(
    //                    getClass()
    //                        .getClassLoader()
    //                        .getResourceAsStream(
    //                            "edu/wpi/DapperDaemons/profilepictures/"
    //                                + SecurityController.getUser().getNodeID()
    //                                + ".png")))));

    // set birthday
    String employeeBirth = SecurityController.getUser().getDateOfBirth();
    birthday.setText(employeeBirth);

    // set email
    email.setText(DAOFacade.getUserAccount().getAttribute(7));
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
