package edu.wpi.DapperDaemons.controllers.requestControllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.controllers.UIController;
import edu.wpi.DapperDaemons.entities.Patient;
import edu.wpi.DapperDaemons.entities.requests.MealDeliveryRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/** Controller for Meal UI Page UPDATED 4/5/22 at 12:08 AM */
public class MealController extends UIController {

  /* Table Helper */
  private TableHelper<MealDeliveryRequest> helper;

  /* Table Object */
  @FXML private TableView<MealDeliveryRequest> mealRequestsTable;

  /* Table Columns for Request Table */
  @FXML private TableColumn<MealDeliveryRequest, String> reqID;
  @FXML private TableColumn<MealDeliveryRequest, String> priority;
  @FXML private TableColumn<MealDeliveryRequest, String> roomID;
  @FXML private TableColumn<MealDeliveryRequest, String> requester;
  @FXML private TableColumn<MealDeliveryRequest, String> assignee;
  @FXML private TableColumn<MealDeliveryRequest, String> patient;
  @FXML private TableColumn<MealDeliveryRequest, String> entree;
  @FXML private TableColumn<MealDeliveryRequest, String> side;
  @FXML private TableColumn<MealDeliveryRequest, String> drink;
  @FXML private TableColumn<MealDeliveryRequest, String> dessert;

  /* Text Fields */
  @FXML private TextField patientName;
  @FXML private TextField patientLastName;
  @FXML private DatePicker patientDOB;

  /* Buttons */
  @FXML private Button clearButton;
  @FXML private Button submitButton;

  /* Dropdown Boxes */
  @FXML private JFXComboBox<String> entreeBox;
  @FXML private JFXComboBox<String> sideBox;
  @FXML private JFXComboBox<String> drinkBox;
  @FXML private JFXComboBox<String> dessertBox;

  /* Unknown room label */
  @FXML private Label errorLabel;

  private DAO<MealDeliveryRequest> mealDeliveryRequestDAO = DAOPouch.getMealDeliveryRequestDAO();
  private DAO<Patient> patientDAO = DAOPouch.getPatientDAO();

  /**
   * Runs at compile time, specified from Initializable interface Sets up meal request table and
   * adds a test value
   *
   * @param location specified at runtime
   * @param resources specified at runtime
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);

    /* Init Request table */
    helper = new TableHelper<>(mealRequestsTable, 0);
    helper.linkColumns(MealDeliveryRequest.class);
    initBoxes();
    onClear();

    try {
      mealRequestsTable.getItems().addAll(mealDeliveryRequestDAO.getAll());
      System.out.println("Created Table");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error, Mead Delivery table was unable to be created");
    }
  }

  /** Creates service request, executes when submit button is pressed */
  public void onSubmit() {
    Request.Priority priority = Request.Priority.LOW;
    String roomID;
    String requesterID;
    String assigneeID = "null";
    String patientID;
    String entree = entreeBox.getValue();
    String side = sideBox.getValue();
    String drink = drinkBox.getValue();
    String dessert = dessertBox.getValue();

    // Check if all inputs are filled
    if (allFilled()) {

      // Check if the patient exists
      patientID =
          patientName.getText()
              + patientLastName.getText()
              + patientDOB.getValue().getMonthValue()
              + patientDOB.getValue().getDayOfMonth()
              + patientDOB.getValue().getYear();
      Patient patient = new Patient();
      boolean isAPatient = true;
      try {
        patient = patientDAO.get(patientID);
      } catch (SQLException e) {
        e.printStackTrace();
      }
      isAPatient =
          patient
              .getFirstName()
              .equals(patientName.getText()); // TODO : This creates a NULL pointer exception
      if (isAPatient) {

        // request is formed correctly and the patient exists send it and check for clearance
        roomID = patient.getLocationID();
        requesterID = SecurityController.getUser().getNodeID();
        boolean hadClearance =
            addMealRequest(
                new MealDeliveryRequest(
                    priority,
                    roomID,
                    requesterID,
                    assigneeID,
                    patientID,
                    entree,
                    side,
                    drink,
                    dessert));

        if (!hadClearance) {
          errorLabel.setText("Error: Access Denied");
        }

      } else {
        errorLabel.setText("Error: Patient Does Not Exist");
      }

    } else {
      errorLabel.setText("Error: One or More Fields were left empty");
    }
    onClear();
  }

  /** clears all options for creating service request, executes when clear button is pressed */
  public void onClear() {
    entreeBox.setValue("");
    sideBox.setValue("");
    drinkBox.setValue("");
    dessertBox.setValue("");
    errorLabel.setText("");
    patientName.clear();
    patientLastName.clear();
    patientDOB.setValue(null);
  }

  /**
   * Adds a meal request to the JFX table view
   *
   * @param request request object to be added
   */
  public boolean addMealRequest(MealDeliveryRequest request) {
    boolean hadClearance = false;
    try {
      hadClearance = mealDeliveryRequestDAO.add(request);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    if (hadClearance) mealRequestsTable.getItems().add(request);

    return hadClearance;
  }

  /** Initializes the options for JFX boxes */
  public void initBoxes() {
    entreeBox.setItems(FXCollections.observableArrayList("Pasta", "Sandwich", "Salad", "Steak"));
    sideBox.setItems(FXCollections.observableArrayList("Fries", "Fruit", "Chips", "None"));
    drinkBox.setItems(FXCollections.observableArrayList("Water", "Soda", "Juice", "None"));
    dessertBox.setItems(FXCollections.observableArrayList("IceCream", "Cake", "Milkshake", "None"));
  }

  /**
   * checks if all needed boxes are filled Sam WUZ HERE :p
   *
   * @return true if all the input boxes are filled
   */
  public boolean allFilled() {
    return (!(entreeBox.getValue().equals("")
        || sideBox.getValue().equals("")
        || drinkBox.getValue().equals("")
        || dessertBox.getValue().equals("")
        || patientName.getText().equals("")
        || patientDOB.getValue().toString().equals("")
        || patientLastName.getText().equals("")));
  }

  public void saveToCSV() {
    super.saveToCSV(new MealDeliveryRequest());
  }
}
