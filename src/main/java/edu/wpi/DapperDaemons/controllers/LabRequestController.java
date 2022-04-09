package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.requests.LabRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
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
    tableHelper.update();
  }

  @FXML
  public void onClearClicked() {
    procedureComboBox.setValue("");
    patientName.clear();
    patientLastName.clear();
    patientDOB.setValue(null);
    priorityChoiceBox.setValue("");
    errorLabel.setText("");
  }

  @FXML
  public void onSubmitClicked() {}

  private boolean allItemsFilled() {
    return !(procedureComboBox.getValue().trim().equals("")
        || patientName.getText().trim().equals("")
        || patientLastName.getText().trim().equals("")
        || patientDOB.getValue() == null
        || priorityChoiceBox.getValue().trim().equals(""));
  }

  private void addItem(LabRequest request) {}

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
