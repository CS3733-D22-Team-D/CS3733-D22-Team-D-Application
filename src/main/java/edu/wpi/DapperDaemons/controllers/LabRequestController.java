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
  @FXML private TextField patientDOB;
  @FXML private ChoiceBox<String> priorityChoiceBox;
  @FXML private JFXComboBox<String> procedureComboBox;

  /* Lab request DAO */
  private final DAO<LabRequest> dao = DAOPouch.getLabRequestDAO();

  /* Labels */
  @FXML private Label errorLabel;

  /** Initializes the controller objects (After runtime, before graphics creation) */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    tableHelper = new TableHelper<>(labReqTable, 0);
    tableHelper.linkColumns(LabRequest.class);

    procedureComboBox.setItems(FXCollections.observableArrayList(TableHelper.convertEnum(LabRequest.LabType.class)));
    priorityChoiceBox.setItems(FXCollections.observableArrayList(TableHelper.convertEnum(Request.Priority.class)));

    try {
      labReqTable.getItems().addAll(dao.getAll());
    } catch (Exception e) {
      e.printStackTrace();
      System.err.print("Error, Lab Req table was unable to be created\n");
    }
    onClearClicked();
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

      addItem(new LabRequest(
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
    super.saveToCSV(new LabRequest());
  }
}
