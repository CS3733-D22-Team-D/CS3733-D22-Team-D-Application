package edu.wpi.DapperDaemons.controllers.requestControllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.controllers.ParentController;
import edu.wpi.DapperDaemons.controllers.helpers.AutoCompleteFuzzy;
import edu.wpi.DapperDaemons.controllers.helpers.FuzzySearchComparatorMethod;
import edu.wpi.DapperDaemons.controllers.helpers.TableListeners;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import edu.wpi.DapperDaemons.entities.requests.MealDeliveryRequest;
import edu.wpi.DapperDaemons.entities.requests.MedicalEquipmentRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/** Equipment Request UI Controller UPDATED 4/5/22 12:30AM */
public class EquipmentRequestController extends ParentController {

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

  @FXML
  public void startFuzzySearch() {
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(priorityBox, new FuzzySearchComparatorMethod());
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(equipmentTypeBox, new FuzzySearchComparatorMethod());
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(roomBox, new FuzzySearchComparatorMethod());
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //        super.initialize(location, resources);
    initBoxes();
    //    bindImage(BGImage, BGContainer);

    tableHelper = new TableHelper<>(equipmentRequestsTable, 0);
    tableHelper.linkColumns(MedicalEquipmentRequest.class);

    try { // Removed second field (filename) since everything is
      // loaded on startup
      equipmentRequestsTable
          .getItems()
          .addAll(new ArrayList(medicalEquipmentRequestDAO.getAll().values()));
    } catch (Exception e) {
      e.printStackTrace();
      System.err.print("Error, table was unable to be created\n");
    }
    setListeners();
    onClearClicked();
  }

  private void setListeners() {
    TableListeners tl = new TableListeners();
    tl.setMedicalEquipmentRequestListener(
        tl.eventListener(
            () -> {
              equipmentRequestsTable.getItems().clear();
              equipmentRequestsTable
                  .getItems()
                  .addAll(new ArrayList(medicalEquipmentRequestDAO.getAll().values()));
            }));
  }

  public boolean addItem(MedicalEquipmentRequest request) {
    boolean hadClearance = false;

    hadClearance = medicalEquipmentRequestDAO.add(request);
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

    // make sure all fields are filled
    if (allFieldsFilled()) {
      // get all the variables ready to go
      Request.Priority priority = Request.Priority.valueOf(priorityBox.getValue());
      String roomID = "";
      String requesterID = SecurityController.getUser().getNodeID();
      String assigneeID = "null";
      MedicalEquipment.EquipmentType equipmentType =
          MedicalEquipment.EquipmentType.valueOf(equipmentTypeBox.getValue());
      MedicalEquipment.CleanStatus cleanStatus = MedicalEquipment.CleanStatus.UNCLEAN;

      ArrayList<MedicalEquipment> equipments = new ArrayList<>();
      MedicalEquipment equipment = new MedicalEquipment();
      // is there equipment with that Type?
      boolean equipmentExists = true;

      // get all equipment of that type.
      equipments =
          new ArrayList(
              medicalEquipmentDAO
                  .filter(medicalEquipmentDAO.getAll(), 3, equipmentTypeBox.getValue())
                  .values());

      if (medicalEquipmentDAO
              .filter(equipments, 5, MedicalEquipment.CleanStatus.CLEAN.toString())
              .size()
          != 0) {
        equipment =
            new ArrayList<MedicalEquipment>(
                    (medicalEquipmentDAO.filter(
                            equipments, 5, MedicalEquipment.CleanStatus.CLEAN.toString()))
                        .values())
                .get(0);
      } else if (medicalEquipmentDAO
              .filter(equipments, 5, MedicalEquipment.CleanStatus.INPROGRESS.toString())
              .size()
          != 0) {
        equipment =
            new ArrayList<MedicalEquipment>(
                    medicalEquipmentDAO
                        .filter(equipments, 5, MedicalEquipment.CleanStatus.INPROGRESS.toString())
                        .values())
                .get(0);

      } else if (medicalEquipmentDAO
              .filter(equipments, 5, MedicalEquipment.CleanStatus.UNCLEAN.toString())
              .size()
          != 0) {
        equipment =
            new ArrayList<MedicalEquipment>(
                    medicalEquipmentDAO
                        .filter(equipments, 5, MedicalEquipment.CleanStatus.UNCLEAN.toString())
                        .values())
                .get(0);
      } else {

        equipmentExists = false;
      }
      if (equipmentExists) {

        // check if room exists
        cleanStatus = equipment.getCleanStatus();
        roomID = roomBox.getValue();
        int numCorrectLocations = 0;
        numCorrectLocations = locationDAO.filter(locationDAO.getAll(), 7, roomID).size();
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
            showError("You do not have permission to do this.");
          }

        } else {
          // throw error that room does not exist
          showError("A room with that name does not exist.");
        }

      } else {
        // Throw error that no equipment of that type exist

        showError("No equipment of that type exists.");
      }
    } else {
      showError("All fields must be filled.");
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
    //    roomBox.setItems(FXCollections.observableArrayList(getAllLongNames()));
  }
  /** Saves a given service request to a CSV by opening the CSV window */
  public void saveToCSV() {
    super.saveToCSV(new MedicalEquipmentRequest());
  }
}
