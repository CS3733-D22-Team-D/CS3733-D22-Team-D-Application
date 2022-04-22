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
import edu.wpi.DapperDaemons.entities.requests.MedicineRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MedicineController extends ParentController {
  @FXML private VBox table;
  @FXML private HBox header;
  private TableHelper<MedicineRequest> helper;
  @FXML private TableColumn<MedicineRequest, Request.Priority> priorityCol;

  @FXML private JFXComboBox<String> medNameIn;
  @FXML private TextField quantityIn;
  @FXML private JFXComboBox<String> priorityIn;
  @FXML private TextField patientName;
  @FXML private TextField patientLastName;
  @FXML private DatePicker patientDOB;
  @FXML private TextField notes;
  @FXML private DatePicker dateNeeded;

  private final DAO<MedicineRequest> medicineRequestDAO = DAOPouch.getMedicineRequestDAO();
  private final DAO<Patient> patientDAO = DAOPouch.getPatientDAO();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //    helper = new TableHelper<>(medicineRequests, 0);
    //    helper.linkColumns(MedicineRequest.class);

    //    helper.addEnumEditProperty(priorityCol, Request.Priority.class);

    medNameIn.setItems(FXCollections.observableArrayList("Morphine", "OxyCodine", "Lexapro"));
    priorityIn.getItems().addAll(TableHelper.convertEnum(Request.Priority.class));

    //    try {
    //      medicineRequests.getItems().addAll(new ArrayList(medicineRequestDAO.getAll().values()));
    //      //      System.out.println("Created table");
    //    } catch (Exception e) {
    //      e.printStackTrace();
    //      System.err.print("Error, Medicine Request table was unable to be created\n");
    //    }
    setListeners();
    onClearClicked();
  }

  private void setListeners() {
    TableListeners.addListener(
        new MedicineRequest().tableName(),
        TableListeners.eventListener(
            () -> {
              //              medicineRequests.getItems().clear();
              //              medicineRequests
              //                  .getItems()
              //                  .addAll(new ArrayList(medicineRequestDAO.getAll().values()));
            }));
  }

  /** Clears the fields when clicked */
  @FXML
  public void onClearClicked() {
    medNameIn.setValue("");
    quantityIn.clear();
    priorityIn.setValue("");
    patientName.clear();
    patientLastName.clear();
    patientDOB.setValue(null);
    notes.setText("");
    dateNeeded.setValue(null);
  }

  @FXML
  public void startFuzzySearch() {
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(priorityIn, new FuzzySearchComparatorMethod());
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(medNameIn, new FuzzySearchComparatorMethod());
  }

  @FXML
  void onEditPriority(TableColumn.CellEditEvent<MedicineRequest, String> event) {
    event.getRowValue().setPriority(Request.Priority.valueOf(event.getNewValue()));
    boolean worked = false;
    try {
      medicineRequestDAO.update(event.getRowValue());
    } catch (Exception e) {

    }
  }

  /**
   * first checks if the request is formed correctly, then checks for user clearance, then sends the
   * request
   */
  @FXML
  public void onSubmitClicked() {

    // declare all request fields

    // Check if all fields have a value if so, proceed
    if (!(medNameIn.getValue().trim().equals("")
        || quantityIn.getText().trim().equals("")
        || priorityIn.getValue().equals("")
        || patientName.getText().equals("")
        || patientLastName.getText().equals("")
        || patientDOB.getValue() == null
        || dateNeeded.getValue() == null)) {

      Request.Priority priority;
      int quantity = 0;
      String medName;
      String patientID;
      String requesterID;
      String assigneeID;
      String roomID;

      String dateStr =
          ""
              + dateNeeded.getValue().getMonthValue()
              + dateNeeded.getValue().getDayOfMonth()
              + dateNeeded.getValue().getYear();

      // check if quantity is an int and not letters
      boolean isAnInt = true;
      try {
        quantity = Integer.parseInt(quantityIn.getText());
      } catch (Exception e) {
        e.printStackTrace();
        isAnInt = false;
      }
      if (isAnInt) {

        // check if the patient info points to a real patient
        boolean isAPatient = false;
        patientID =
            patientName.getText()
                + patientLastName.getText()
                + patientDOB.getValue().getMonthValue()
                + patientDOB.getValue().getDayOfMonth()
                + patientDOB.getValue().getYear();
        Patient patient = new Patient();
        patient = patientDAO.get(patientID);
        try {
          isAPatient = patient.getFirstName().equals(patientName.getText());
        } catch (NullPointerException e) {
          // dont need to print stacktrace here, if its null then nothing happens
        }
        if (isAPatient) {

          // now we can create the request and send it

          roomID = patient.getLocationID();
          requesterID = SecurityController.getUser().getNodeID();
          assigneeID = "none";
          priority = Request.Priority.valueOf(priorityIn.getValue());
          medName = medNameIn.getValue();

          boolean wentThrough =
              addItem(
                  new MedicineRequest(
                      priority,
                      roomID,
                      requesterID,
                      assigneeID,
                      notes.getText(),
                      patientID,
                      medName,
                      quantity,
                      dateStr));

          if (!wentThrough) {

            // throw error saying no clearance allowed
            showError("You do not have permission to do this.");
          }

        } else {
          // throw an error message saying that the patient doesnt exist
          showError("Could not find a patient that matches.");
        }
      } else {
        // throw error message about quantity not being a number
        showError("Please enter a valid number.");
      }
    } else {
      //  throw error message about empty fields
      showError("All fields must be filled.");
    }

    onClearClicked();
  }
  /** Saves a given service request to a CSV by opening the CSV window */
  public void saveToCSV() {
    super.saveToCSV(new MedicineRequest(), (Stage) patientName.getScene().getWindow());
  }

  @FXML
  private boolean addItem(MedicineRequest request) {
    boolean hasClearance = false;
    hasClearance = medicineRequestDAO.add(request);
    //    if (hasClearance) medicineRequests.getItems().add(request);

    return hasClearance;
  }
}
