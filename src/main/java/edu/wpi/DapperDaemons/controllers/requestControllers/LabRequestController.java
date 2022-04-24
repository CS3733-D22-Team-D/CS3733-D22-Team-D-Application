package edu.wpi.DapperDaemons.controllers.requestControllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.controllers.ParentController;
import edu.wpi.DapperDaemons.controllers.helpers.AutoCompleteFuzzy;
import edu.wpi.DapperDaemons.controllers.helpers.FuzzySearchComparatorMethod;
import edu.wpi.DapperDaemons.controllers.helpers.TableListeners;
import edu.wpi.DapperDaemons.entities.Patient;
import edu.wpi.DapperDaemons.entities.requests.LabRequest;
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

public class LabRequestController extends ParentController {

  /* Table Object and Helper */
  @FXML private HBox header;
  private TableHelper<LabRequest> tableHelper;

  /* UI Fields */
  @FXML private TextField patientName;
  @FXML private TextField patientLastName;
  @FXML private DatePicker patientDOB;
  @FXML private JFXComboBox<String> priorityChoiceBox;
  @FXML private JFXComboBox<String> procedureComboBox;
  @FXML private TextField notes;
  @FXML private DatePicker dateNeeded;

  /* Lab request DAO */
  private DAO<LabRequest> labRequestDAO = DAOPouch.getLabRequestDAO();
  private DAO<Patient> patientDAO = DAOPouch.getPatientDAO();
  @FXML private GridPane table;
  private Table<LabRequest> t;

  /* Labels */
  @FXML private Label errorLabel;

  /** Initializes the controller objects (After runtime, before graphics creation) */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    onClearClicked();
    LabRequestInitializer init = new LabRequestInitializer();

    init.initializeTable();
    init.initializeInputs();

    //    try {
    //      labReqTable.getItems().addAll(new ArrayList(labRequestDAO.getAll().values()));
    //    } catch (Exception e) {
    //      e.printStackTrace();
    //      System.err.print("Error, Lab Req table was unable to be created\n");
    //    }
    //    setListeners();
    t = new Table(table, 0);
    createTable();
  }

  private void createTable() {
    //    t.setHeader(header, new ArrayList<>(List.of(new String[] {"Test", "Test", "Test"})));
    List<LabRequest> reqs = new ArrayList<>(DAOPouch.getLabRequestDAO().getAll().values());
    t.setRows(reqs);
    t.setListeners(new LabRequest());
  }

  private void setListeners() {
    TableListeners.addListener(
        new LabRequest().tableName(),
        TableListeners.eventListener(
            () -> {
              //              labReqTable.getItems().clear();
              //              labReqTable.getItems().addAll(new
              // ArrayList(labRequestDAO.getAll().values()));
            }));
  }

  @FXML
  public void startFuzzySearch() {
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(
        priorityChoiceBox, new FuzzySearchComparatorMethod());
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(
        procedureComboBox, new FuzzySearchComparatorMethod());
  }

  @FXML
  public void onClearClicked() {
    procedureComboBox.setValue("");
    patientName.clear();
    patientLastName.clear();
    patientDOB.setValue(null);
    priorityChoiceBox.setValue("");
    notes.setText("");
    dateNeeded.setValue(null);
  }

  @FXML
  public void onSubmitClicked() {

    if (allItemsFilled()) {
      String dateStr = "";
      Request.Priority priority = Request.Priority.valueOf(priorityChoiceBox.getValue());
      String roomID = "";
      String requesterID = SecurityController.getUser().getNodeID();
      String assigneeID = "none";
      String patientID =
          patientName.getText()
              + patientLastName.getText()
              + patientDOB.getValue().getMonthValue()
              + patientDOB.getValue().getDayOfMonth()
              + patientDOB.getValue().getYear();
      LabRequest.LabType labType = LabRequest.LabType.valueOf(procedureComboBox.getValue());
      Request.RequestStatus status = Request.RequestStatus.REQUESTED;

      // Check if the patient info points to a real patient
      boolean isAPatient = false;
      Patient patient = new Patient();
      patient = patientDAO.get(patientID);
      try {
        isAPatient = patient.getFirstName().equals(patientName.getText());
      } catch (NullPointerException e) {
        e.printStackTrace();
      }
      if (isAPatient) {
        roomID = patient.getLocationID();
        dateStr =
            ""
                + dateNeeded.getValue().getMonthValue()
                + dateNeeded.getValue().getDayOfMonth()
                + dateNeeded.getValue().getYear();

        boolean hadClearance =
            addItem(
                new LabRequest(
                    priority,
                    roomID,
                    requesterID,
                    assigneeID,
                    notes.getText(),
                    patientID,
                    labType,
                    dateStr));

        if (!hadClearance) {
          //  throw error saying that the user does not have clearance yada yada

          showError("You do not have permission to do this.");
        }

      } else {
        // throw error saying that the patient does not exist
        showError("Could not find a patient that matches.");
      }
    } else {
      // throws error saying that all fields must be filled
      showError("All fields must be filled.");
    }
    onClearClicked();
  }

  private boolean allItemsFilled() {
    System.out.println(patientDOB.getValue());
    return !(procedureComboBox.getValue().trim().equals("")
        || patientName.getText().trim().equals("")
        || patientLastName.getText().trim().equals("")
        || patientDOB.getValue() == null
        || priorityChoiceBox.getValue().trim().equals("")
        || dateNeeded.getValue() == null);
  }

  private boolean addItem(LabRequest request) {
    boolean hadClearance = false;
    hadClearance = labRequestDAO.add(request);
    if (hadClearance) {
      //      labReqTable.getItems().add(request);
    }

    return hadClearance;
  }

  /** Saves a given service request to a CSV by opening the CSV window */
  public void saveToCSV() {
    super.saveToCSV(new LabRequest(), (Stage) patientName.getScene().getWindow());
  }

  private class LabRequestInitializer {
    private void initializeTable() {
      // Bind values to column values
      //      tableHelper = new TableHelper<>(labReqTable, 0);
      //      tableHelper.linkColumns(LabRequest.class);
    }

    private void initializeInputs() {
      procedureComboBox.setItems(
          FXCollections.observableArrayList(TableHelper.convertEnum(LabRequest.LabType.class)));
      priorityChoiceBox.setItems(
          FXCollections.observableArrayList(TableHelper.convertEnum(Request.Priority.class)));
    }
  }
}
