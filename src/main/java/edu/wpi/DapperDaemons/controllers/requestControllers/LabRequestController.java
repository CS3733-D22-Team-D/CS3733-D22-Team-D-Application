package edu.wpi.DapperDaemons.controllers.requestControllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.controllers.UIController;
import edu.wpi.DapperDaemons.entities.Patient;
import edu.wpi.DapperDaemons.entities.requests.LabRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.map.tables.TableHelper;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LabRequestController extends UIController {

  /* Table Object and Helper */
  @FXML private TableView<LabRequest> labReqTable;
  private TableHelper<LabRequest> tableHelper;

  /* UI Fields */
  @FXML private TextField patientName;
  @FXML private TextField patientLastName;
  @FXML private DatePicker patientDOB;
  @FXML private JFXComboBox<String> priorityChoiceBox;
  @FXML private JFXComboBox<String> procedureComboBox;

  /* Lab request DAO */
  private DAO<LabRequest> labRequestDAO = DAOPouch.getLabRequestDAO();
  private DAO<Patient> patientDAO = DAOPouch.getPatientDAO();

  /* Labels */
  @FXML private Label errorLabel;

  /** Initializes the controller objects (After runtime, before graphics creation) */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    onClearClicked();
    super.initialize(location, resources);
    LabRequestInitializer init = new LabRequestInitializer();

    init.initializeTable();
    init.initializeInputs();

    try {
      labReqTable.getItems().addAll(labRequestDAO.getAll());
    } catch (Exception e) {
      e.printStackTrace();
      System.err.print("Error, Lab Req table was unable to be created\n");
    }
  }

  @FXML
  public void onClearClicked() {
    procedureComboBox.setValue("");
    patientName.clear();
    patientLastName.clear();
    patientDOB.setValue(null);
    priorityChoiceBox.setValue("");
  }

  @FXML
  public void onSubmitClicked() {

    if (allItemsFilled()) {
      Request.Priority priority = Request.Priority.valueOf(priorityChoiceBox.getValue());
      String roomID = "";
      String requesterID = SecurityController.getUser().getNodeID();
      String assigneeID = "null";
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
      try {
        patient = patientDAO.get(patientID);
      } catch (SQLException e) {
        e.printStackTrace();
      }
      try {
        isAPatient = patient.getFirstName().equals(patientName.getText());
      } catch (NullPointerException e) {
        e.printStackTrace();
      }
      if (isAPatient) {
        roomID = patient.getLocationID();

        boolean hadClearance =
            addItem(
                new LabRequest(
                    priority, roomID, requesterID, assigneeID, patientID, labType, status));

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
    return !(procedureComboBox.getValue().trim().equals("")
        || patientName.getText().trim().equals("")
        || patientLastName.getText().trim().equals("")
        || patientDOB.getValue() == null
        || priorityChoiceBox.getValue().trim().equals(""));
  }

  private boolean addItem(LabRequest request) {
    boolean hadClearance = false;
    try {
      hadClearance = labRequestDAO.add(request);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    if (hadClearance) {
      labReqTable.getItems().add(request);
    }

    return hadClearance;
  }

  /** Saves a given service request to a CSV by opening the CSV window */
  public void saveToCSV() {
    super.saveToCSV(new LabRequest());
  }

  private class LabRequestInitializer {
    private void initializeTable() {
      // Bind values to column values
      tableHelper = new TableHelper<>(labReqTable, 0);
      tableHelper.linkColumns(LabRequest.class);
    }

    private void initializeInputs() {
      procedureComboBox.setItems(
          FXCollections.observableArrayList(TableHelper.convertEnum(LabRequest.LabType.class)));
      priorityChoiceBox.setItems(
          FXCollections.observableArrayList(TableHelper.convertEnum(Request.Priority.class)));
    }
  }
}
