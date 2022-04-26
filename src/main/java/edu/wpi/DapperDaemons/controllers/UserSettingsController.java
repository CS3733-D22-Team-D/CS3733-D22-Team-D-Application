package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAOFacade;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.backend.SoundPlayer;
import edu.wpi.DapperDaemons.controllers.homePage.AccountHandler;
import edu.wpi.DapperDaemons.controllers.homePage.ThemeHandler;
import edu.wpi.DapperDaemons.entities.Account;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.sound.sampled.LineUnavailableException;

public class UserSettingsController extends ParentController {

  // account profile
  @FXML private Circle profilePic;
  @FXML private Text accountName;
  @FXML private Text accountUserName;
  @FXML private Label email;
  @FXML private Label employeeID;

  // account settings
  @FXML private Label username;
  @FXML private Label name;
  @FXML private Label birthday;
  @FXML private TextField emailBox;
  @FXML private JFXComboBox<String> themeBox;
  @FXML private JFXComboBox<String> soundBox;
  @FXML private JFXButton resetButton;
  @FXML private JFXButton saveChangesButton;

  // switch pages
  @FXML
  public void openAboutUs(ActionEvent event) {
    swapPage("aboutUs", "About Us");
  }

  @FXML
  public void goUserHome(ActionEvent event) {
    swapPage("NewUserHome", "User Home");
  }

  @FXML
  public void openAccountSettings(ActionEvent event) {
    swapPage("NewUserSettings", "User Account Settings");
  }

  @FXML
  public void openUserSecurity(ActionEvent event) {
    swapPage("NewUserSecurity", "User Security");
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

    accountUserName.setText(DAOFacade.getUsername());
    username.setText(DAOFacade.getUsername());

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

    // set employeeID
    String empID = DAOFacade.getUserAccount().getAttribute(2);
    employeeID.setText(empID);

    // set email
    email.setText(DAOFacade.getUserAccount().getAttribute(7));

    // set themeBox
    themeBox.setItems(
        FXCollections.observableArrayList(TableHelper.convertEnum(ThemeHandler.Theme.class)));

    // set soundBox
    soundBox.setItems(
        FXCollections.observableArrayList(
            "Bloop",
            "BloopBloop",
            "BloopBlop",
            "BuDaLing",
            "BuDoi",
            "DaDing",
            "DooDoDaLoo",
            "DooDooooo",
            "Ring",
            "Shing",
            "Ayo",
            "TeamD"));
  }

  @FXML
  private void saveToCSV() {
    saveToCSV((Stage) email.getScene().getWindow());
  }

  @FXML
  private void loadFromCSV() {
    loadFromCSV((Stage) email.getScene().getWindow());
  }

  public void onSaveChanges() {
    // set email
    String newEmail = email.getText();
    Account toChange = DAOFacade.getUserAccount();
    if (!newEmail.equals(toChange.getAttribute(7))) {
      toChange.setAttribute(7, newEmail);
      DAOPouch.getAccountDAO().update(toChange);
    }

    // set theme
    if (themeBox.getValue() != null && !themeBox.getValue().equals("")) {
      ThemeHandler.toggleTheme(ThemeHandler.Theme.valueOf(themeBox.getValue()));
    }

    // set sound
    if (soundBox.getValue() != null && !soundBox.getValue().equals("")) {
      SoundPlayer newSound =
          new SoundPlayer("edu/wpi/DapperDaemons/notifications/" + soundBox.getValue() + ".wav");
      DAOFacade.getUserAccount()
          .setAttribute(5, "edu/wpi/DapperDaemons/notifications/" + soundBox.getValue() + ".wav");
      DAOPouch.getAccountDAO().add(DAOPouch.getAccountDAO().get(DAOFacade.getUsername()));
    }
  }

  public void onReset() {

    // reset email
    email.setText(
        DAOPouch.getAccountDAO().get(SecurityController.getUser().getNodeID()).getAttribute(7));

    // reset theme
    themeBox.setValue("");

    // reset sound
    soundBox.setValue("");
  }

  public void onTestSound() throws LineUnavailableException {
    String sound = soundBox.getValue();
    SoundPlayer player = new SoundPlayer("edu/wpi/DapperDaemons/notifications/" + sound + ".wav");
    player.play();
  }
}
