package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.backend.csvSaver;
import edu.wpi.DapperDaemons.entities.Account;
import edu.wpi.DapperDaemons.entities.Employee;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController {

  @FXML private TextField username;
  @FXML private PasswordField password;
  @FXML private VBox sceneBox;

  SecurityController sc = SecurityController.getInstance();
  DAO<Employee> employeeDAO = DAOPouch.getEmployeeDAO();
  DAO<Account> accountDAO = DAOPouch.getAccountDAO();

  @FXML
  void login(MouseEvent event) throws Exception {
    // TODO for final product remove admin bypass
    if (username.getText().equals("") && password.getText().equals("")) {
      switchScene("default.fxml", 635, 510);
      return;
    }
    Account acc = accountDAO.get(username.getText());
    if (acc != null && acc.checkPassword(password.getText())) {
      List<Employee> user =
          employeeDAO.filter(1, accountDAO.get(username.getText()).getAttribute(2));
      if (user.size() == 1) {
        sc.setUser(user.get(0));
        switchScene("default.fxml", 635, 510);
      } else {
        throw new Exception(
            "More than one user with the same username?"); // theoretically this is unreachable
      }
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
  }
}
