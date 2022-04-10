package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import edu.wpi.DapperDaemons.entities.requests.MealDeliveryRequest;
import edu.wpi.DapperDaemons.entities.requests.MedicalEquipmentRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/** Equipment Request UI Controller UPDATED 4/5/22 12:30AM */
public class EquipmentRequestController extends UIController {

  /* Table Object */
  @FXML private TableView<MedicalEquipmentRequest> equipmentRequestsTable;

  /* Table Helper */
  private TableHelper<MedicalEquipmentRequest> tableHelper;

  /* Sexy MOTHERFUCKING  JFXComboBoxes */
  @FXML private JFXComboBox<String> priorityBox;
  @FXML private JFXComboBox<String> equipmentTypeBox;
  @FXML private JFXComboBox<String> roomBox;
  /* Table Columns */
  @FXML private TableColumn<MealDeliveryRequest, String> reqID;
  @FXML private TableColumn<MealDeliveryRequest, String> priority;
  @FXML private TableColumn<MealDeliveryRequest, String> roomID;
  @FXML private TableColumn<MealDeliveryRequest, String> requester;
  @FXML private TableColumn<MealDeliveryRequest, String> assignee;
  @FXML private TableColumn<MedicalEquipmentRequest, String> equipID;
  @FXML private TableColumn<MedicalEquipmentRequest, String> equipType;
  @FXML private TableColumn<MedicalEquipmentRequest, String> cleanStatus;

  /* DAO Object */
  private DAO<MedicalEquipmentRequest> medicalEquipmentRequestDAO =
      DAOPouch.getMedicalEquipmentRequestDAO();
  private DAO<Location> locationDAO = DAOPouch.getLocationDAO();
  private DAO<MedicalEquipment> medicalEquipmentDAO = DAOPouch.getMedicalEquipmentDAO();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    initBoxes();

    tableHelper = new TableHelper<>(equipmentRequestsTable, 0);
    tableHelper.linkColumns(MedicalEquipmentRequest.class);

    try { // Removed second field (filename) since everything is
      // loaded on startup
      equipmentRequestsTable.getItems().addAll(medicalEquipmentRequestDAO.getAll());
    } catch (Exception e) {
      e.printStackTrace();
      System.err.print("Error, table was unable to be created\n");
    }

    onClearClicked();
  }

  public boolean addItem(MedicalEquipmentRequest request) {
    boolean hadClearance = false;

    try {
      hadClearance = medicalEquipmentRequestDAO.add(request);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    if (hadClearance) {
      equipmentRequestsTable.getItems().add(request);
    }
    return hadClearance;
  }

  @FXML
  public void onClearClicked() {
    priorityBox.setValue("");
    equipmentTypeBox.setValue("");
    roomBox.setValue("");
  }

  @FXML
  public void onSubmitClicked() {
    // get all the variables ready to go
    Request.Priority priority = Request.Priority.valueOf(priorityBox.getValue());
    String roomID = "";
    String requesterID = SecurityController.getUser().getNodeID();
    String assigneeID = "null";
    MedicalEquipment.EquipmentType equipmentType =
        MedicalEquipment.EquipmentType.valueOf(equipmentTypeBox.getValue());
    MedicalEquipment.CleanStatus cleanStatus = MedicalEquipment.CleanStatus.UNCLEAN;
    // make sure all fields are filled
    if (allFieldsFilled()) {
      ArrayList<MedicalEquipment> equipments = new ArrayList<>();
      MedicalEquipment equipment = new MedicalEquipment();
      // is there equipment with that Type?
      boolean equipmentExists = true;

      // get all equipment of that type.
      try {
        equipments =
            (ArrayList<MedicalEquipment>)
                medicalEquipmentDAO.filter(
                    medicalEquipmentDAO.getAll(), 3, equipmentTypeBox.getValue());
      } catch (SQLException e) {
        e.printStackTrace();
      }

      if (medicalEquipmentDAO
              .filter(equipments, 5, MedicalEquipment.CleanStatus.CLEAN.toString())
              .size()
          != 0) {
        equipment =
            medicalEquipmentDAO
                .filter(equipments, 5, MedicalEquipment.CleanStatus.CLEAN.toString())
                .get(0);
      } else if (medicalEquipmentDAO
              .filter(equipments, 5, MedicalEquipment.CleanStatus.INPROGRESS.toString())
              .size()
          != 0) {
        equipment =
            medicalEquipmentDAO
                .filter(equipments, 5, MedicalEquipment.CleanStatus.INPROGRESS.toString())
                .get(0);

      } else if (medicalEquipmentDAO
              .filter(equipments, 5, MedicalEquipment.CleanStatus.UNCLEAN.toString())
              .size()
          != 0) {
        equipment =
            medicalEquipmentDAO
                .filter(equipments, 5, MedicalEquipment.CleanStatus.UNCLEAN.toString())
                .get(0);
      } else {

        equipmentExists = false;
      }
      if (equipmentExists) {

        // check if room exists
        cleanStatus = equipment.getCleanStatus();
        roomID = roomBox.getValue();
        int numCorrectLocations = 0;
        try {
          numCorrectLocations = locationDAO.filter(locationDAO.getAll(), 7, roomID).size();
        } catch (SQLException e) {
          e.printStackTrace();
        }
        if (numCorrectLocations >= 1) {

          boolean hadClearance =
              addItem(
                  new MedicalEquipmentRequest(
                      priority,
                      roomID,
                      requesterID,
                      assigneeID,
                      equipment.getNodeID(),
                      equipmentType,
                      cleanStatus));
          // check if user has permission
          if (!hadClearance) {
            // TODO throw error saying that user does not have permission for that function
          }

        } else {
          // TODO throw error that room does not exist
        }

      } else {
        // TODO Throw error that no equipment of that type exist
      }
    } else {
      // TODO  error message that not all fields are filled in
    }
    onClearClicked();
  }

  private boolean allFieldsFilled() {
    return !(priorityBox.getValue().equals("")
        || equipmentTypeBox.getValue().equals("")
        || roomBox.getValue().equals(""));
  }

  public void initBoxes() {
    priorityBox.setItems(
        FXCollections.observableArrayList(TableHelper.convertEnum(Request.Priority.class)));
    equipmentTypeBox.setItems(
        FXCollections.observableArrayList(
            TableHelper.convertEnum(MedicalEquipment.EquipmentType.class)));
    roomBox.setItems(FXCollections.observableArrayList(getAllLongNames()));
  }
  /** Saves a given service request to a CSV by opening the CSV window */
  public void saveToCSV() {
    super.saveToCSV(new MedicalEquipmentRequest());
  }
}
