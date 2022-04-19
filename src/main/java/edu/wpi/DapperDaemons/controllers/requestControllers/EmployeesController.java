package edu.wpi.DapperDaemons.controllers.requestControllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.controllers.ParentController;
import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.entities.requests.MedicalEquipmentRequest;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/** Patient Transport Controller UPDATED 4/5/22 12:42 PM */
public class EmployeesController extends ParentController implements Initializable {

  /* Table Object */
  @FXML private TableView<Employee> employees;

  /*Table Helper */
  private TableHelper<Employee> tableHelper;

  /* Table Columns */
  @FXML private TableColumn<Employee, String> ID;
  @FXML private TableColumn<Employee, String> firstName;
  @FXML private TableColumn<Employee, String> lastName;
  @FXML private TableColumn<Employee, String> dateOfBirth;
  @FXML private TableColumn<Employee, String> type;
  @FXML private TableColumn<Employee, String> clearance;

  /* Dropdown boxes */
  @FXML private JFXComboBox<String> clearanceBox;
  @FXML private JFXComboBox<String> typeBox;

  /* Text Boxes */
  @FXML private TextField employeeFirstName;
  @FXML private TextField employeeLastName;
  @FXML private DatePicker employeeDOB;
  List<String> names;

  /* DAO */
  DAO<Employee> employeeDAO = DAOPouch.getEmployeeDAO();

  /** Initializes the controller objects (After runtime, before graphics creation) */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initializeTable();
    initializeInputs();
  }

  @FXML
  public void onClearClicked() {
    clearanceBox.setValue("");
    typeBox.setValue("");
    employeeFirstName.setText("");
    employeeLastName.setText("");
    employeeDOB.setValue(null);
  }


 private boolean addItem(Employee request) {
    boolean hadClearance = false;

    hadClearance = employeeDAO.add(request);
    if (hadClearance) {
      employees.getItems().add(request);
    }
    return hadClearance;
  }

  @FXML
  public void onSubmitClicked() {
    if(fieldsNonEmpty()){
      String firstName = employeeFirstName.getText();
      String lastName = employeeLastName.getText();
      String dob = "" + employeeDOB.getValue().getMonthValue()
              + employeeDOB.getValue().getDayOfMonth()
              + employeeDOB.getValue().getYear();


      if(!addItem(new Employee(firstName,lastName,dob, Employee.EmployeeType.valueOf(typeBox.getValue()),Integer.parseInt(clearanceBox.getValue())))){

        showError("you do not have access to this function");

      }




    }else{
      showError("not all fields have been filled");
    }






  }

  public boolean fieldsNonEmpty() {
    return !(clearanceBox.getValue().equals("")
        || typeBox.getValue().equals("")
        || employeeFirstName.getText().equals("")
        || employeeLastName.getText().equals("")
        || employeeDOB.getValue() == null);
  }

  private void initializeTable() {
    tableHelper = new TableHelper<>(employees, 0);
    tableHelper.linkColumns(Employee.class);

    try {
      employees.getItems().addAll(new ArrayList<Employee>(employeeDAO.getAll().values()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initializeInputs() {

    typeBox.setItems(
        FXCollections.observableArrayList(TableHelper.convertEnum(Employee.EmployeeType.class)));
    clearanceBox.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5"));

    clearanceBox.setValue("");
    typeBox.setValue("");
    employeeFirstName.setText("");
    employeeLastName.setText("");
  }

  public void saveToCSV() {
    super.saveToCSV(new Employee(), (Stage) employees.getScene().getWindow());
  }
}
