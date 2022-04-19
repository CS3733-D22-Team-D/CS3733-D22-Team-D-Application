package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.entities.Account;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class UserSecurityController extends ParentController {

  // account profile
  @FXML private Circle profilePic;
  @FXML private Text accountName;
  @FXML private Text accountUserName;

  // security page
  @FXML private Label securityLevel;
  @FXML private TextField oldPasswordBox;
  @FXML private TextField newPasswordBox;
  @FXML private TextField numberBox;
  @FXML private JFXComboBox<String> type2FABox;

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

    accountUserName.setText(
        DAOPouch.getAccountDAO().get(SecurityController.getUser().getNodeID()).getAttribute(1));

    // set profile picture
    profilePic.setFill(
        new ImagePattern(
            new Image(
                Objects.requireNonNull(
                    getClass()
                        .getClassLoader()
                        .getResourceAsStream(
                            "edu/wpi/DapperDaemons/profilepictures/"
                                + SecurityController.getUser().getNodeID()
                                + ".png")))));

    // set security access level
    securityLevel.setText(Integer.toString(SecurityController.getUser().getSecurityClearance()));

    // set phone number
    numberBox.setText(
        DAOPouch.getAccountDAO().get(SecurityController.getUser().getNodeID()).getAttribute(4));

    // set types of 2FA types
    type2FABox.setItems(FXCollections.observableArrayList("SMS", "rfid", "none"));
    type2FABox.setValue(
        DAOPouch.getAccountDAO().get(SecurityController.getUser().getNodeID()).getAttribute(6));
  }

  public void onRequestNewAccess() {
    // TODO: come back maybe
  }

  // TODO: get password to work?
  public void onSaveChanges() throws NoSuchAlgorithmException {
    // set new password
    /*String newPassword = newPasswordBox.getText();
    String oldPassword = oldPasswordBox.getText();
    try {
      Account toChange = DAOPouch.getAccountDAO().get(SecurityController.getUser().getNodeID());
      if (toChange.checkPassword(oldPassword)) {
        toChange.setAttribute(3, newPassword);
        DAOPouch.getAccountDAO().update(toChange);
        System.out.println("Password Reset");
      } else System.out.println("Could not reset password");
    } catch (SQLException e) {
      throw new RuntimeException();
    } */

    // set new phone number
    String newNumber = numberBox.getText();
    Account toChange = DAOPouch.getAccountDAO().get(SecurityController.getUser().getNodeID());
    toChange.setAttribute(4, newNumber);
    DAOPouch.getAccountDAO().update(toChange);
    System.out.println("New Phone Number Reset");

    // set 2FA type
    if (type2FABox.getValue() != null && !type2FABox.getValue().equals("")) {
      String newTwoFactor = type2FABox.getValue();
      toChange.setAttribute(6, newTwoFactor);
      DAOPouch.getAccountDAO().update(toChange);
    }
  }

  public void onReset() {
    // set password
    /*oldPasswordBox.setText("");
    newPasswordBox.setText("");*/

    // set phone number
    numberBox.setText(
        DAOPouch.getAccountDAO().get(SecurityController.getUser().getNodeID()).getAttribute(4));
  }
}
