package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.csvSaver;
import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import edu.wpi.DapperDaemons.entities.requests.MealDeliveryRequest;
import edu.wpi.DapperDaemons.entities.requests.MedicalEquipmentRequest;
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

// import edu.wpi.Dapper_Daemons.requests.PatientTransportRequest;

/** Equipment Request UI Controller UPDATED 4/5/22 12:30AM */
public class EquipmentRequestController extends UIController {

  // TODO: Prevent user from inputting multiple equipment choices
  /* Table Object */
  @FXML private TableView<MedicalEquipmentRequest> equipmentRequestsTable;

  /* Table Helper */
  private TableHelper<MedicalEquipmentRequest> tableHelper;

  /* Text Fields */
  @FXML private TextField roomNumber;

  /* Choice Box */
  @FXML private ChoiceBox<String> priorityChoiceBox;

  /* Check Boxes */
  @FXML private CheckBox IPCheck;
  @FXML private CheckBox PRCheck;
  @FXML private CheckBox XRayCheck;
  @FXML private CheckBox BedsCheck;
  @FXML private CheckBox COCheck;

  /* Table Columns */
  @FXML private TableColumn<MealDeliveryRequest, String> reqID;
  @FXML private TableColumn<MealDeliveryRequest, String> priority;
  @FXML private TableColumn<MealDeliveryRequest, String> roomID;
  @FXML private TableColumn<MealDeliveryRequest, String> requester;
  @FXML private TableColumn<MealDeliveryRequest, String> assignee;
  @FXML private TableColumn<MedicalEquipmentRequest, String> equipID;
  @FXML private TableColumn<MedicalEquipmentRequest, String> equipType;
  @FXML private TableColumn<MedicalEquipmentRequest, String> cleanStatus;

  /* Labels */
  @FXML private Label errorLabel;

  /* DAO Object */
  private DAO<MedicalEquipmentRequest> dao = DAOPouch.getMedicalEquipmentRequestDAO();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initBoxes();
    onClearClicked();
    super.initialize(location, resources);
    tableHelper = new TableHelper<>(equipmentRequestsTable, 0);
    tableHelper.linkColumns(MedicalEquipmentRequest.class);

    try { // Removed second field (filename) since everything is
      // loaded on startup
      equipmentRequestsTable.getItems().addAll(dao.getAll());
    } catch (Exception e) {
      e.printStackTrace();
      System.err.print("Error, table was unable to be created\n");
    }

    // tableHelper.addEnumEditProperty(cleanStatus, MedicalEquipment.CleanStatus.class);
  }

  @FXML
  public void onEditCleanStatus(
      TableColumn.CellEditEvent<MedicalEquipmentRequest, String> editEvent) {
    editEvent
        .getRowValue()
        .setCleanStatus(MedicalEquipment.CleanStatus.valueOf(editEvent.getNewValue()));
    tableHelper.update();
  }

  public void addItem(MedicalEquipmentRequest requests) {
    try {
      dao.add(requests);
      equipmentRequestsTable.getItems().add(requests);
    } catch (Exception e) {
      // TODO : Have some sort of catcher for if things got messed up
      e.printStackTrace();
    }
  }

  @FXML
  public void onClearClicked() {
    errorLabel.setText("");
    roomNumber.setText("");
    priorityChoiceBox.setValue("");
    IPCheck.setSelected(false);
    BedsCheck.setSelected(false);
    PRCheck.setSelected(false);
    XRayCheck.setSelected(false);
    COCheck.setSelected(false);
  }

  @FXML
  public void onSubmitClicked() {
    MedicalEquipment.EquipmentType equip;
    if (IPCheck.isSelected()) {
      equip = MedicalEquipment.EquipmentType.INFUSIONPUMP;
    } else if (BedsCheck.isSelected()) {
      equip = MedicalEquipment.EquipmentType.BED;
    } else if (PRCheck.isSelected()) {
      equip = MedicalEquipment.EquipmentType.RECLINER;
    } else if (XRayCheck.isSelected()) {
      equip = MedicalEquipment.EquipmentType.XRAY;
    } else {
      errorLabel.setText("Error: Invalid Selection");
      return;
    }

    if (roomNumber.getText().equals("") || priorityChoiceBox.getValue().equals("")) {
      errorLabel.setText("Error: A field is blank");
    } else {
      errorLabel.setText("");

      String roomNumberTxt = roomNumber.getText();
      Request.Priority priorityBox = Request.Priority.valueOf(priorityChoiceBox.getValue());
      onClearClicked();

      MedicalEquipmentRequest submitted =
          new MedicalEquipmentRequest(
              priorityBox,
              roomNumberTxt,
              "REQUESTERID",
              "ASSIGNEEID",
              "EquipmentID",
              equip,
              MedicalEquipment.CleanStatus.CLEAN);
      addItem(submitted);
    }
  }

  public void initBoxes() {
    priorityChoiceBox.setItems(FXCollections.observableArrayList("LOW", "MEDIUM", "HIGH"));
  }
  /** Saves a given service request to a CSV by opening the CSV window */
  public void saveToCSV() {
    FileChooser fileSys = new FileChooser();
    Stage window = (Stage) equipmentRequestsTable.getScene().getWindow();
    fileSys.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
    File csv = fileSys.showSaveDialog(window);
    try {
      csvSaver.save((new MedicalEquipmentRequest()), csv.getAbsolutePath());
    } catch (Exception e) {
      System.err.println("Unable to Save CSV");
    }
  }
}
