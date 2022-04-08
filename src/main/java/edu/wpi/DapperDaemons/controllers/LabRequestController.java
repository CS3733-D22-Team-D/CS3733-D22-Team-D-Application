package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.csvSaver;
import edu.wpi.DapperDaemons.entities.requests.LabRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class LabRequestController extends UIController {

  /* Table Object and Helper */
  @FXML private TableView<LabRequest> labReqTable;
  private TableHelper<LabRequest> tableHelper;

  /* UI Fields */
  @FXML private TextField patientName;
  @FXML private TextField patientLastName;
  @FXML private TextField patientDOB;
  @FXML private ChoiceBox<String> priorityChoiceBox;
  @FXML private JFXComboBox<String> procedureComboBox;

  /* Lab request DAO */
  private DAO<LabRequest> dao = DAOPouch.getLabRequestDAO();

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
    init.initializeRequests();

    try {
      labReqTable.getItems().addAll(dao.getAll());
    } catch (Exception e) {
      e.printStackTrace();
      System.err.print("Error, Lab Req table was unable to be created\n");
    }
  }

  @FXML
  public void editTechnician(TableColumn.CellEditEvent<LabRequest, String> editEvent) {
    editEvent
        .getRowValue()
        .setAssigneeID(
            editEvent.getNewValue()); // TODO : Need to link this to person database since its an ID
    tableHelper.update();
  }

  // TODO : Either change this to Priority, add a Status to LabRequest, or delete it
  @FXML
  public void editStatus(TableColumn.CellEditEvent<LabRequest, String> editEvent) {
    editEvent.getRowValue().setStatus(editEvent.getNewValue());
    tableHelper.update(); // Commented this out so the program can run
  }

  @FXML
  public void onClearClicked() {
    procedureComboBox.setValue("");
    patientName.clear();
    patientLastName.clear();
    patientDOB.clear();
    priorityChoiceBox.setValue("");
    errorLabel.setText("");
  }

  @FXML
  public void onSubmitClicked() {
    if (!(procedureComboBox.getValue().trim().equals("")
        || patientName.getText().trim().equals("")
        || patientLastName.getText().trim().equals("")
        || patientDOB.getText().trim().equals("")
        || priorityChoiceBox.getValue().trim().equals(""))) {

      LabRequest.LabType labType = LabRequest.LabType.valueOf(procedureComboBox.getValue());
      String patientID = patientName.getText() + patientLastName.getText() + patientDOB.getText();
      Request.Priority priority = Request.Priority.valueOf(priorityChoiceBox.getValue());

      addItem(
          new LabRequest(
              priority,
              "LAB ROOM ID",
              "REQUESTERID",
              "ASSIGNEEID",
              patientID,
              labType,
              Request.RequestStatus.REQUESTED));

      onClearClicked();
    } else {
      errorLabel.setText("Error: One or more fields are empty!");
    }
  }

  // TODO : The table probably has to be updated for the new LabRequest thing
  private void addItem(LabRequest request) {
    try {
      dao.add(request);
      labReqTable.getItems().add(request);
    } catch (Exception e) {
      e.printStackTrace();
      // TODO : show an error on the screen since adding went wrong
    }
  }
  /** Saves a given service request to a CSV by opening the CSV window */
  public void saveToCSV() {
    FileChooser fileSys = new FileChooser();
    Stage window = (Stage) labReqTable.getScene().getWindow();
    fileSys.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
    File csv = fileSys.showSaveDialog(window);
    try {
      csvSaver.save((new LabRequest()), csv.getAbsolutePath());
    } catch (Exception e) {
      System.err.println("Unable to Save CSV");
    }
  }

  private class LabRequestInitializer {
    private void initializeTable() {
      // Bind values to column values
      tableHelper = new TableHelper<>(labReqTable, 0);
      tableHelper.linkColumns(LabRequest.class);
    }

    // TODO: Pull inputs for drop-down from database
    private void initializeInputs() {
      procedureComboBox.setItems(
          FXCollections.observableArrayList(TableHelper.convertEnum(LabRequest.LabType.class)));
      priorityChoiceBox.setItems(
          FXCollections.observableArrayList(TableHelper.convertEnum(Request.Priority.class)));
    }

    // TODO: Pull lab requests from database
    private void initializeRequests() {}
  }
}
