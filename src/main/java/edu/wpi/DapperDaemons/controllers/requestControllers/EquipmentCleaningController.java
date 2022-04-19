package edu.wpi.DapperDaemons.controllers.requestControllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.controllers.ParentController;
import edu.wpi.DapperDaemons.controllers.helpers.AutoCompleteFuzzy;
import edu.wpi.DapperDaemons.controllers.helpers.FuzzySearchComparatorMethod;
import edu.wpi.DapperDaemons.controllers.helpers.TableListeners;
import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import edu.wpi.DapperDaemons.entities.requests.EquipmentCleaning;
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
public class EquipmentCleaningController extends ParentController {

  /* Table Object */
  @FXML private TableView<EquipmentCleaning> equipmentRequestsTable;

  /* Table Helper */
  private TableHelper<EquipmentCleaning> tableHelper;

  /* Sexy MOTHERFUCKING  JFXComboBoxes */
  @FXML private JFXComboBox<String> priorityBox;
  @FXML private JFXComboBox<String> equipmentIDBox;
  @FXML private DatePicker cleanByDate;

  /* Table Columns */
  @FXML private TableColumn<EquipmentCleaning, String> reqID;
  @FXML private TableColumn<EquipmentCleaning, String> priority;
  @FXML private TableColumn<EquipmentCleaning, String> roomID;
  @FXML private TableColumn<EquipmentCleaning, String> requester;
  @FXML private TableColumn<EquipmentCleaning, String> assignee;
  @FXML private TableColumn<EquipmentCleaning, String> equipID;
  @FXML private TableColumn<EquipmentCleaning, String> equipType;
  @FXML private TableColumn<EquipmentCleaning, String> cleanStatus;
  @FXML private TableColumn<EquipmentCleaning, String> dateRequested;

  /* DAO Object */
  private DAO<EquipmentCleaning> equipmentCleaningDAO = DAOPouch.getEquipmentCleaningDAO();
  private DAO<MedicalEquipment> medicalEquipmentDAO = DAOPouch.getMedicalEquipmentDAO();

  @FXML
  public void startFuzzySearch() {
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(priorityBox, new FuzzySearchComparatorMethod());
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(equipmentIDBox, new FuzzySearchComparatorMethod());
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //        super.initialize(location, resources);
    initBoxes();
    //    bindImage(BGImage, BGContainer);

    tableHelper = new TableHelper<>(equipmentRequestsTable, 0);
    tableHelper.linkColumns(EquipmentCleaning.class);

    try { // Removed second field (filename) since everything is
      // loaded on startup
      equipmentRequestsTable
          .getItems()
          .addAll(new ArrayList(equipmentCleaningDAO.getAll().values()));
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
                  .addAll(new ArrayList(equipmentCleaningDAO.getAll().values()));
            }));
  }

  public boolean addItem(EquipmentCleaning request) {
    boolean hadClearance = false;

    hadClearance = equipmentCleaningDAO.add(request);
    if (hadClearance) {
      equipmentRequestsTable.getItems().add(request);
    }
    return hadClearance;
  }

  @FXML
  public void onClearClicked() {
    priorityBox.setValue("");
    equipmentIDBox.setValue("");
    cleanByDate.setValue(null);
  }

  @FXML
  public void onSubmitClicked() {
    if (allFieldsFilled()) {
      Request.Priority priority = Request.Priority.valueOf(priorityBox.getValue());
      String roomID = "";
      String requesterID = SecurityController.getUser().getNodeID();
      String assigneeID = "null";

      MedicalEquipment medicalEquipment =
          medicalEquipmentDAO.get(equipmentIDBox.getValue()); // Gets the current EQ
      if (medicalEquipment == null) {
        showError("Invalid Medical Equipment");
      } else {
        String dateStr =
            ""
                + cleanByDate.getValue().getMonthValue()
                + cleanByDate.getValue().getDayOfMonth()
                + cleanByDate.getValue().getYear();

        medicalEquipment.setCleanStatus(
            MedicalEquipment.CleanStatus
                .INPROGRESS); // TODO : Add a REQUESTED field to clean status?
        medicalEquipmentDAO.update(
            medicalEquipment); // Show in the medical equipment that it has been requested / put in
        // progress

        boolean hadClearance =
            addItem(
                new EquipmentCleaning(
                    priority,
                    roomID,
                    requesterID,
                    assigneeID,
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
      }
    } else {
      showError("All fields but be filled");
    }

    // make sure all fields are filled
    //    if (allFieldsFilled()) {
    //      ArrayList<MedicalEquipment> equipments = new ArrayList<>();
    //      MedicalEquipment equipment = new MedicalEquipment();
    //      // is there equipment with that Type?
    //      boolean equipmentExists = true;
    //
    //      // get all equipment of that type.
    //      equipments =
    //          new ArrayList(
    //              medicalEquipmentDAO
    //                  .filter(medicalEquipmentDAO.getAll(), 3, equipmentTypeBox.getValue())
    //                  .values());
    //
    //      if (medicalEquipmentDAO
    //              .filter(equipments, 5, MedicalEquipment.CleanStatus.CLEAN.toString())
    //              .size()
    //          != 0) {
    //        equipment =
    //            new ArrayList<MedicalEquipment>(
    //                    (medicalEquipmentDAO.filter(
    //                            equipments, 5, MedicalEquipment.CleanStatus.CLEAN.toString()))
    //                        .values())
    //                .get(0);
    //      } else if (medicalEquipmentDAO
    //              .filter(equipments, 5, MedicalEquipment.CleanStatus.INPROGRESS.toString())
    //              .size()
    //          != 0) {
    //        equipment =
    //            new ArrayList<MedicalEquipment>(
    //                    medicalEquipmentDAO
    //                        .filter(equipments, 5,
    // MedicalEquipment.CleanStatus.INPROGRESS.toString())
    //                        .values())
    //                .get(0);
    //
    //      } else if (medicalEquipmentDAO
    //              .filter(equipments, 5, MedicalEquipment.CleanStatus.UNCLEAN.toString())
    //              .size()
    //          != 0) {
    //        equipment =
    //            new ArrayList<MedicalEquipment>(
    //                    medicalEquipmentDAO
    //                        .filter(equipments, 5,
    // MedicalEquipment.CleanStatus.UNCLEAN.toString())
    //                        .values())
    //                .get(0);
    //      } else {
    //
    //        equipmentExists = false;
    //      }
    //      if (equipmentExists) {
    //
    //        // check if room exists
    //        cleanStatus = equipment.getCleanStatus();
    //        roomID = roomBox.getValue();
    //        int numCorrectLocations = 0;
    //        numCorrectLocations = locationDAO.filter(locationDAO.getAll(), 7, roomID).size();
    //        if (numCorrectLocations >= 1) {
    //
    //          boolean hadClearance =
    //              addItem(
    //                  new MedicalEquipmentRequest(
    //                      priority,
    //                      roomID,
    //                      requesterID,
    //                      assigneeID,
    //                      equipment.getNodeID(),
    //                      equipmentType,
    //                      cleanStatus,
    //                      dateStr));
    //          // check if user has permission
    //          if (!hadClearance) {
    //            showError("You do not have permission to do this.");
    //          }
    //
    //        } else {
    //          // throw error that room does not exist
    //          showError("A room with that name does not exist.");
    //        }
    //
    //      } else {
    //        // Throw error that no equipment of that type exist
    //
    //        showError("No equipment of that type exists.");
    //      }
    //    } else {
    //      showError("All fields must be filled.");
    //    }
  }

  private boolean allFieldsFilled() {
    return !(priorityBox.getValue().equals("")
        || equipmentIDBox.getValue().equals("")
        || cleanByDate.getValue() == null);
  }

  public void initBoxes() {
    priorityBox.setItems(
        FXCollections.observableArrayList(TableHelper.convertEnum(Request.Priority.class)));
    // TODO : Put all the equipment ID types in the box for fuzzy wuzzy
    //    roomBox.setItems(FXCollections.observableArrayList(getAllLongNames()));
  }
  /** Saves a given service request to a CSV by opening the CSV window */
  public void saveToCSV() {
    super.saveToCSV(new MedicalEquipmentRequest());
  }
}
