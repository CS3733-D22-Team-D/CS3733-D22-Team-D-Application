package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.backend.csvSaver;
import edu.wpi.DapperDaemons.entities.Patient;
import edu.wpi.DapperDaemons.entities.requests.MedicineRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MedicineController extends UIController {
  @FXML private TableView<MedicineRequest> medicineRequests;
  private TableHelper<MedicineRequest> helper;
  @FXML private TableColumn<MedicineRequest, String> priorityCol;

  @FXML private JFXComboBox<String> medNameIn;
  @FXML private TextField quantityIn;
  @FXML private JFXComboBox<String> priorityIn;
  @FXML private TextField patientName;
  @FXML private TextField patientLastName;
  @FXML private DatePicker patientDOB;

  DAO<MedicineRequest> medicineRequestDAO = DAOPouch.getMedicineRequestDAO();
  DAO<Patient> patientDAO = DAOPouch.getPatientDAO();

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    super.initialize(location, resources);
    MedicineRequestInitializer init = new MedicineRequestInitializer();

    // initialize elements
    init.initializeInputs();
    init.initializeTable();

    try {
      medicineRequests.getItems().addAll(medicineRequestDAO.getAll());
      //      System.out.println("Created table");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.print("Error, Medicine Requesst table was unable to be created\n");
    }
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
    Request.Priority priority;
    int quantity = 0;
    String medName;
    String patientID;
    String requesterID;
    String assigneeID;
    String roomID;

    // Check if all fields have a value if so, proceed
    if (!(medNameIn.getValue().trim().equals("")
        || quantityIn.getText().trim().equals("")
        || priorityIn.getValue().equals("")
        || patientName.getText().equals("")
        || patientLastName.getText().equals("")
        || patientDOB.getValue().toString().equals(""))) {

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
        boolean isAPatient = true;
        patientID =
            patientName.getText()
                + patientLastName.getText()
                + patientDOB.getValue().getMonthValue()
                + patientDOB.getValue().getDayOfMonth()
                + patientDOB.getValue().getYear();
        Patient patient = new Patient();
        try {
          patient = patientDAO.get(patientID);
        } catch (SQLException e) {
          e.printStackTrace();
          isAPatient = false;
        }
        if (isAPatient) {

          // now we can create the request and send it

          roomID = patient.getLocationID();
          requesterID = SecurityController.getInstance().getUser().getNodeID();
          assigneeID = "null";
          priority = Request.Priority.valueOf(priorityIn.getValue());
          medName = medNameIn.getValue();

          boolean wentThrough =
              addItem(
                  new MedicineRequest(
                      priority, roomID, requesterID, assigneeID, patientID, medName, quantity));

          if (!wentThrough) {

            // TODO throw error saying no clearance allowed

          }

        } else {
          // TODO throw an error message saying that the patient doesnt exist
        }
      } else {
        // TODO throw error message about quantity not being a number
      }
    } else {
      // TODO: throw error message about empty fields
    }

    onClearClicked();
  }
  /** Saves a given service request to a CSV by opening the CSV window */
  public void saveToCSV() {
    FileChooser fileSys = new FileChooser();
    Stage window = (Stage) medicineRequests.getScene().getWindow();
    fileSys.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
    File csv = fileSys.showSaveDialog(window);
    try {
      csvSaver.save((new MedicineRequest()), csv.getAbsolutePath());
    } catch (Exception e) {
      System.err.println("Unable to Save CSV");
    }
  }

  @FXML
  private boolean addItem(MedicineRequest request) {
    boolean hasClearance = false;
    try {
      hasClearance = medicineRequestDAO.add(request);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    if (hasClearance) medicineRequests.getItems().add(request);

    return hasClearance;
  }

  private class MedicineRequestInitializer {
    private void initializeTable() {
      helper = new TableHelper<>(medicineRequests, 0);
      helper.linkColumns(MedicineRequest.class);

      helper.addEnumEditProperty(priorityCol, Request.Priority.class);
    }

    private void initializeInputs() {
      medNameIn.setItems(FXCollections.observableArrayList("Morphine", "OxyCodine", "Lexapro"));
      priorityIn.getItems().addAll("LOW", "MEDIUM", "HIGH");
    }
  }
}
