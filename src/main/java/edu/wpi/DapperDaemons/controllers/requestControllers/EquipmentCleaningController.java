package edu.wpi.DapperDaemons.controllers.requestControllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.controllers.ParentController;
import edu.wpi.DapperDaemons.controllers.helpers.AutoCompleteFuzzy;
import edu.wpi.DapperDaemons.controllers.helpers.FuzzySearchComparatorMethod;
import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import edu.wpi.DapperDaemons.entities.requests.EquipmentCleaning;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.Table;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/** Equipment Request UI Controller UPDATED 4/5/22 12:30AM */
public class EquipmentCleaningController extends ParentController {

  /* Sexy MOTHERFUCKING  JFXComboBoxes */
  @FXML private JFXComboBox<String> priorityIn;
  @FXML private JFXComboBox<String> equipmentIDBox;
  @FXML private JFXComboBox<String> assigneeBox;
  @FXML private DatePicker dateNeeded;
  @FXML private TextField notes;

  /* Table Columns */
  @FXML private TableColumn<EquipmentCleaning, String> reqID;
  @FXML private TableColumn<EquipmentCleaning, Request.Priority> priority;
  @FXML private TableColumn<EquipmentCleaning, String> roomID;
  @FXML private TableColumn<EquipmentCleaning, String> requester;
  @FXML private TableColumn<EquipmentCleaning, String> assignee;
  @FXML private TableColumn<EquipmentCleaning, String> equipID;
  @FXML private TableColumn<EquipmentCleaning, MedicalEquipment.EquipmentType> equipType;
  @FXML private TableColumn<EquipmentCleaning, MedicalEquipment.CleanStatus> cleanStatus;
  @FXML private TableColumn<EquipmentCleaning, String> dateRequested;

  /* DAO Object */
  private final DAO<EquipmentCleaning> equipmentCleaningDAO = DAOPouch.getEquipmentCleaningDAO();
  private final DAO<MedicalEquipment> medicalEquipmentDAO = DAOPouch.getMedicalEquipmentDAO();
  private final DAO<Employee> employeeDAO = DAOPouch.getEmployeeDAO();
  @FXML private GridPane table;
  @FXML private HBox header;
  private Table<EquipmentCleaning> t;

  @FXML
  public void startFuzzySearch() {
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(priorityIn, new FuzzySearchComparatorMethod());
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(equipmentIDBox, new FuzzySearchComparatorMethod());
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(assigneeBox, new FuzzySearchComparatorMethod());
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    initBoxes();

    t = new Table<>(EquipmentCleaning.class, table, 0);
    createTable();
    onClearClicked();
  }

  private void createTable() {
    //    t.setHeader(header, new ArrayList<>(List.of(new String[] {"Test", "Test", "Test"})));
    List<EquipmentCleaning> reqs =
        new ArrayList<>(DAOPouch.getEquipmentCleaningDAO().getAll().values());
    t.setRows(reqs);
    t.setListeners(new EquipmentCleaning());
  }

  public boolean addItem(EquipmentCleaning request) {
    boolean hadClearance = false;

    hadClearance = equipmentCleaningDAO.add(request);

    return hadClearance;
  }

  @FXML
  public void onClearClicked() {
    priorityIn.setValue("");
    equipmentIDBox.setValue("");
    assigneeBox.setValue("");
    dateNeeded.setValue(null);
    notes.setText("");
  }

  @FXML
  public void onSubmitClicked() {
    if (allFieldsFilled()) {
      Request.Priority priority = Request.Priority.valueOf(priorityIn.getValue());
      String roomID = "";
      String requesterID = SecurityController.getUser().getNodeID();
      
      MedicalEquipment medicalEquipment =
          medicalEquipmentDAO.get(equipmentIDBox.getValue()); // Gets the current EQ
      if (medicalEquipment == null) {
        showError("Invalid Medical Equipment");
      } else {
        String dateStr =
            ""
                + dateNeeded.getValue().getMonthValue()
                + dateNeeded.getValue().getDayOfMonth()
                + dateNeeded.getValue().getYear();
        roomID = medicalEquipment.getLocationID();

        medicalEquipment.setCleanStatus(
            MedicalEquipment.CleanStatus
                .INPROGRESS); // TODO : Add a REQUESTED field to clean status?
        medicalEquipmentDAO.update(
            medicalEquipment); // Show in the medical equipment that it has been requested / put in
        // progress

        if (assigneeBox.getValue().equals("")
            || assigneeBox
                .getValue()
                .equals("Auto Assign")) { // check if the user inputs an assignee
          boolean hadClearance =
              addItem(
                  new EquipmentCleaning(
                      priority,
                      roomID,
                      requesterID,
                      notes.getText(),
                      medicalEquipment.getNodeID(),
                      medicalEquipment.getEquipmentType(),
                      MedicalEquipment.CleanStatus.INPROGRESS,
                      dateStr));
          // check if user has permission
          if (!hadClearance) {
            showError("You do not have permission to do this.");
          } else {
            onClearClicked();
          }
        } else {
          if (DAOPouch.getEmployeeDAO()
              .get(assigneeBox.getValue())
              .getEmployeeType()
              .equals(Employee.EmployeeType.JANITOR)) {
            boolean hadClearance =
                addItem(
                    new EquipmentCleaning(
                        priority,
                        roomID,
                        requesterID,
                        assigneeBox.getValue(),
                        notes.getText(),
                        medicalEquipment.getNodeID(),
                        medicalEquipment.getEquipmentType(),
                        MedicalEquipment.CleanStatus.INPROGRESS,
                        dateStr));
            // check if user has permission
            if (!hadClearance) {
              showError("You do not have permission to do this.");
            } else {
              onClearClicked();
            }
          } else {
            showError("Invalid Assignee ID!");
          }
        }
      }
    } else {
      showError("All fields but be filled");
    }
  }

  private boolean allFieldsFilled() {
    return !(priorityIn.getValue().equals("")
        || equipmentIDBox.getValue().equals("")
        || dateNeeded.getValue() == null);
  }

  public void initBoxes() {
    priorityIn.setItems(
        FXCollections.observableArrayList(TableHelper.convertEnum(Request.Priority.class)));

    List<MedicalEquipment> medicalEquipmentList =
        new ArrayList<>(medicalEquipmentDAO.getAll().values());
    List<String> idNames = new ArrayList<>();
    for (MedicalEquipment equipment : medicalEquipmentList) idNames.add(equipment.getNodeID());

    equipmentIDBox.setItems(FXCollections.observableArrayList(idNames));

    List<Employee> employees = new ArrayList<>(employeeDAO.filter(5, "JANITOR").values());
    List<String> employeeNames = new ArrayList<>();
    employeeNames.add("Auto Assign");
    for (Employee employee : employees) employeeNames.add(employee.getNodeID());
    assigneeBox.setItems(FXCollections.observableArrayList(employeeNames));
  }
  /** Saves a given service request to a CSV by opening the CSV window */
  public void saveToCSV() {
    super.saveToCSV(new EquipmentCleaning(), (Stage) priorityIn.getScene().getWindow());
  }
}
