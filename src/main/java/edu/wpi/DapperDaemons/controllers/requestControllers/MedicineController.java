package edu.wpi.DapperDaemons.controllers.requestControllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.controllers.ParentController;
import edu.wpi.DapperDaemons.entities.Patient;
import edu.wpi.DapperDaemons.entities.requests.MedicineRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MedicineController extends ParentController {
  @FXML private TableView<MedicineRequest> medicineRequests;
  private TableHelper<MedicineRequest> helper;
  @FXML private TableColumn<MedicineRequest, String> priorityCol;

  @FXML private JFXComboBox<String> medNameIn;
  @FXML private TextField quantityIn;
  @FXML private JFXComboBox<String> priorityIn;
  @FXML private TextField patientName;
  @FXML private TextField patientLastName;
  @FXML private DatePicker patientDOB;

  private final DAO<MedicineRequest> medicineRequestDAO = DAOPouch.getMedicineRequestDAO();
  private final DAO<Patient> patientDAO = DAOPouch.getPatientDAO();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    helper = new TableHelper<>(medicineRequests, 0);
    helper.linkColumns(MedicineRequest.class);

    helper.addEnumEditProperty(priorityCol, Request.Priority.class);

    medNameIn.setItems(FXCollections.observableArrayList("Morphine", "OxyCodine", "Lexapro"));
    priorityIn.getItems().addAll(TableHelper.convertEnum(Request.Priority.class));

    try {
      medicineRequests.getItems().addAll(new ArrayList(medicineRequestDAO.getAll().values()));
      //      System.out.println("Created table");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.print("Error, Medicine Request table was unable to be created\n");
    }

    onClearClicked();
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
        || patientDOB.getValue() == null)) {

      Request.Priority priority;
      int quantity = 0;
      String medName;
      String patientID;
      String requesterID;
      String assigneeID;
      String roomID;

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
          assigneeID = "null";
          priority = Request.Priority.valueOf(priorityIn.getValue());
          medName = medNameIn.getValue();

          boolean wentThrough =
              addItem(
                  new MedicineRequest(
                      priority, roomID, requesterID, assigneeID, patientID, medName, quantity));

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
    super.saveToCSV(new MedicineRequest());
  }

  @FXML
  private boolean addItem(MedicineRequest request) {
    boolean hasClearance = false;
    hasClearance = medicineRequestDAO.add(request);
    if (hasClearance) medicineRequests.getItems().add(request);

    return hasClearance;
  }
}
