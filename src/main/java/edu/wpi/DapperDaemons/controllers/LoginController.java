package edu.wpi.DapperDaemons.controllers;

import arduino.Arduino;
import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.backend.*;
import edu.wpi.DapperDaemons.backend.loadingScreen.LoadingScreen;
import edu.wpi.DapperDaemons.controllers.helpers.TableListeners;
import edu.wpi.DapperDaemons.entities.Account;
import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.map.serial.ArduinoExceptions.UnableToConnectException;
import edu.wpi.DapperDaemons.map.serial.SerialCOM;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController extends AppController {

  @FXML private TextField username;
  @FXML private PasswordField password;
  @FXML private VBox TwoFactor;
  @FXML private TextField code;

  /* Background */
  @FXML private ImageView BGImage;
  @FXML private Pane BGContainer;

  private final DAO<Employee> employeeDAO = DAOPouch.getEmployeeDAO();
  private final DAO<Account> accountDAO = DAOPouch.getAccountDAO();

  private SoundPlayer player;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    bindImage(BGImage, BGContainer);

    code.setOnKeyPressed(
        e -> {
          try {
            keyPressedAuth(e);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        });
  }

  /**
   * The login method invoked when the user attempts to log into the program
   *
   * @throws Exception if there is an issue :p (This should not happen)
   */
  @FXML
  void login() throws Exception {
    if (username.getText().trim().equals("Rick") && password.getText().trim().equals("Astley")) {
      player = new SoundPlayer("edu/wpi/DapperDaemons/assets/unsuspectingWavFile.wav");
      player.play();
    }
    Account acc = accountDAO.get(username.getText());
    if (acc != null && acc.checkPassword(password.getText())) {
      if (acc.getAttribute(4).equals("") || acc.getAttribute(6).equals("false")) {
        List<Employee> user =
            new ArrayList<>(
                employeeDAO.filter(1, accountDAO.get(username.getText()).getAttribute(2)).values());
        if (user.size() == 1) {
          if (acc.getAttribute(6).equals("rfid")) {
            Stage window = (Stage) username.getScene().getWindow();
            LoadingScreen ls = new LoadingScreen(window);
            ls.display(
                () -> {
                  if (!System.getProperty("os.name").trim().toLowerCase().contains("windows")) {
                    RFIDPageController.errorOS = System.getProperty("os.name").trim();
                  } else {
                    RFIDPageController.errorOS = null;
                    Arduino arduino;
                    SerialCOM serialCOM = new SerialCOM();
                    try {
                      arduino = serialCOM.setupArduino();
                      RFIDPageController.COM = arduino.getPortDescription();
                    } catch (UnableToConnectException e) {
                      RFIDPageController.COM = null;
                    }
                  }
                },
                () -> {
                  SecurityController.setUser(user.get(0));
                  // TODO: Find out why this is not working
                  try {
                    switchScene("RFIDScanPage.fxml", 635, 510);
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                });
            return;
          }
          SecurityController.setUser(user.get(0)); // Correctly finds the user
          switchScene("parentHeader.fxml", 635, 510);
          return;
        } else {
          throw new Exception(
              "More than one user with the same username?"); // theoretically this is unreachable
        }
      }
      TwoFactor.setVisible(true);
      Authentication.sendAuthCode(acc);
    } else {

      // incorrect username or password
      showError("Either your username or password is incorrect.");
    }
  }

  @FXML
  void hidePopup() {
    TwoFactor.setVisible(false);
  }

  @FXML
  void authenticate() throws Exception {
    try {
      int authCode = Integer.parseInt(code.getText());
      if (Authentication.authenticate(authCode)) {
        List<Employee> user =
            new ArrayList<>(
                employeeDAO.filter(1, accountDAO.get(username.getText()).getAttribute(2)).values());
        if (user.size() == 1) {
          SecurityController.setUser(user.get(0));
          switchScene("parentHeader.fxml", 635, 510);
        } else {
          throw new Exception(
              "More than one user with the same username?"); // theoretically this is unreachable
        }
      } else {
        showError("Invalid code");
      }
    } catch (NumberFormatException e) {
      System.out.println("int was not entered");
      showError("Invalid code");
    }
  }

  @FXML
  public void keyPressed(KeyEvent event) throws Exception {
    if (event.getCode().equals(KeyCode.ENTER)) {
      login();
    }
  }

  @FXML
  public void keyPressedAuth(KeyEvent event) throws Exception {
    if (event.getCode().equals(KeyCode.ENTER)) {
      authenticate();
    }
  }
}
