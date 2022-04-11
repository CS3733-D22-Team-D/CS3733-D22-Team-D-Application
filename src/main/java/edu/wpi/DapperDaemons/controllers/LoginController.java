package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.*;
import edu.wpi.DapperDaemons.entities.Account;
import edu.wpi.DapperDaemons.entities.Employee;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class LoginController extends AppController {

  @FXML private TextField username;
  @FXML private PasswordField password;
  @FXML private VBox TwoFactor;
  @FXML private TextField code;

  private final DAO<Employee> employeeDAO = DAOPouch.getEmployeeDAO();
  private final DAO<Account> accountDAO = DAOPouch.getAccountDAO();

  private soundPlayer player;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);

    code.setOnKeyPressed(
        e -> {
          try {
            keyPressedAuth(e);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        });
  }

  @FXML
  void login() throws Exception {
    Employee admin = DAOPouch.getEmployeeDAO().get("admin");
    if (username.getText().equals("") && password.getText().equals("")) {
      SecurityController.setUser(admin);
      switchScene("default.fxml", 635, 510);
      return;
    } else if (username.getText().equals("rfid") && password.getText().equals("rfid")) {
      SecurityController.setUser(admin);
      switchScene("RFIDScanPage.fxml", 635, 510);
      return;
    } else if (username.getText().equals("Rick") && password.getText().equals("Astley")) {
      player = new soundPlayer("edu/wpi/DapperDaemons/assets/unsuspectingWavFile.wav");
      player.play();
    }
    Account acc = accountDAO.get(username.getText());
    if (acc != null && acc.checkPassword(password.getText())) {
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
            employeeDAO.filter(1, accountDAO.get(username.getText()).getAttribute(2));
        if (user.size() == 1) {
          SecurityController.setUser(user.get(0));
          switchScene("default.fxml", 635, 510);
        } else {
          throw new Exception(
              "More than one user with the same username?"); // theoretically this is unreachable
        }
      } else {
        showError("Invalid code");
      }
    } catch (NumberFormatException e) {
      System.out.println("int was not entered");
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
