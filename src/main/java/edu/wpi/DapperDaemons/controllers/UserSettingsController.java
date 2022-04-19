package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.controllers.homePage.ThemeHandler;
import edu.wpi.DapperDaemons.entities.Account;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
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

public class UserSettingsController extends ParentController {

  // account profile
  @FXML private Circle profilePic;
  @FXML private Text accountName;
  @FXML private Text accountUserName;

  // account settings
  @FXML private Label username;
  @FXML private Label name;
  @FXML private Label birthday;
  @FXML private TextField email;
  @FXML private JFXComboBox<String> themeBox;
  @FXML private JFXButton resetButton;
  @FXML private JFXButton saveChangesButton;

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
    name.setText(employeeName);

    accountUserName.setText(
        DAOPouch.getAccountDAO().get(SecurityController.getUser().getNodeID()).getAttribute(1));

    username.setText(
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

    // set birthday
    String employeeBirth = SecurityController.getUser().getDateOfBirth();
    birthday.setText(employeeBirth);

    // set email
    email.setText(
        DAOPouch.getAccountDAO().get(SecurityController.getUser().getNodeID()).getAttribute(7));

    // set themeBox
    themeBox.setItems(
        FXCollections.observableArrayList(TableHelper.convertEnum(ThemeHandler.Theme.class)));
  }

  public void onSaveChanges() {
    // set email
    String newEmail = email.getText();
    Account toChange = DAOPouch.getAccountDAO().get(SecurityController.getUser().getNodeID());
    if (!newEmail.equals(toChange.getAttribute(7))) {
      toChange.setAttribute(7, newEmail);
      DAOPouch.getAccountDAO().update(toChange);
    }

    // set theme
    if (themeBox.getValue() != null && !themeBox.getValue().equals("")) {
      ThemeHandler.toggleTheme(ThemeHandler.Theme.valueOf(themeBox.getValue()));
    }
  }

  public void onReset() {

    // reset email
    email.setText(
        DAOPouch.getAccountDAO().get(SecurityController.getUser().getNodeID()).getAttribute(7));

    // reset theme
    themeBox.setValue("");
  }
}
