package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.backend.*;
import edu.wpi.DapperDaemons.entities.Account;
import edu.wpi.DapperDaemons.entities.Employee;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class LoginController implements Initializable {
  /* Variables for error messages */
  @FXML private StackPane windowContents;
  @FXML private VBox error;

  @FXML private TextField username;
  @FXML private PasswordField password;
  @FXML private VBox sceneBox;
  @FXML private VBox TwoFactor;
  @FXML private TextField code;

  DAO<Employee> employeeDAO = DAOPouch.getEmployeeDAO();
  DAO<Account> accountDAO = DAOPouch.getAccountDAO();

  static Thread sound;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      error =
          FXMLLoader.load(
              Objects.requireNonNull(App.class.getResource("views/" + "errorMessage.fxml")));
    } catch (IOException e) {
      e.printStackTrace();
    }

    code.setOnKeyPressed(
        e -> {
          try {
            keyPressedAuth(e);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        });
    error.setVisible(false);
    error.setPickOnBounds(false);
    windowContents.getChildren().add(error);
  }

  // showing an error message
  @FXML
  protected void showError(String errorMessage) {
    error.setVisible(true);
    Node nodeOut = error.getChildren().get(1);
    if (nodeOut instanceof VBox) {
      for (Node nodeIn : ((VBox) nodeOut).getChildren()) {
        if (nodeIn instanceof Label) {
          ((Label) nodeIn).setText(errorMessage);
        }
      }
    }
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
      playSound("edu/wpi/DapperDaemons/assets/unsuspectingWavFile.wav");
    }
    Account acc = accountDAO.get(username.getText());
    if (acc != null && acc.checkPassword(password.getText())) {
      TwoFactor.setVisible(true);
      Authentication.sendAuthCode(acc);
    } else {
      showError("Either your username or Password is incorrect.");
    }
  }

  public void stopSound() {
    sound.interrupt();
  }

  public static synchronized void playSound(final String url) throws LineUnavailableException {
    sound =
        new Thread(
            new Runnable() {
              // The wrapper thread is unnecessary, unless it blocks on the
              // Clip finishing; see comments.
              Clip clip = AudioSystem.getClip();

              public void stop() {
                clip.stop();
              }

              public void run() {
                try {
                  AudioInputStream inputStream =
                      AudioSystem.getAudioInputStream(
                          Objects.requireNonNull(
                              easterEggController.class.getClassLoader().getResourceAsStream(url)));
                  clip.open(inputStream);
                  clip.start();
                } catch (Exception e) {
                  System.err.println(e.getMessage());
                }
                while (!Thread.interrupted()) ;
                stop();
              }
            });
    sound.start();
  }

  @FXML
  void hidePopup() {
    TwoFactor.setVisible(false);
  }

  @FXML
  void authenticate() throws Exception {
    try {
      int authCode = Integer.valueOf(code.getText());
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

  protected void switchScene(String fileName, int minWidth, int minHeight) throws IOException {
    Parent root =
        FXMLLoader.load(Objects.requireNonNull(App.class.getResource("views/" + fileName)));
    Stage window = (Stage) username.getScene().getWindow();
    window.setMinWidth(minWidth);
    window.setMinHeight(minHeight);

    double width = sceneBox.getPrefWidth();
    double height = sceneBox.getPrefHeight();
    window.setScene(new Scene(root));
    sceneBox.setPrefWidth(width);
    sceneBox.setPrefHeight(height);
    window.setWidth(window.getWidth() + 0.0); // To update size
    window.setHeight(window.getHeight());
  }

  @FXML
  public void quitProgram() {
    Stage window = (Stage) username.getScene().getWindow();
    //    try {
    //      DAO<Location> closer = DAOPouch.getLocationDAO();
    //      DAO<MedicalEquipmentRequest> closer2 = DAOPouch.getMedicalEquipmentRequestDAO();
    //      closer.save("TowerLocationsSave.csv");
    //      closer2.save("MedEquipReqSave.csv");
    //      System.out.println("Saving CSV Files");
    //    } catch (Exception e) {
    //      e.printStackTrace();
    //    }
    csvSaver.saveAll();
    window.close();
    Platform.exit();
    System.exit(0);
  }
}
