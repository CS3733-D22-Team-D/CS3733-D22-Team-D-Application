package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.csvSaver;
import edu.wpi.DapperDaemons.entities.requests.LabRequest;
import edu.wpi.DapperDaemons.entities.requests.MealDeliveryRequest;
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
  @FXML private TextField patientDOB;

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

  private DAO<MealDeliveryRequest> dao = DAOPouch.getMealDeliveryRequestDAO();

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
    helper = new TableHelper<MealDeliveryRequest>(mealRequestsTable, 0);
    helper.linkColumns(MealDeliveryRequest.class);
    initBoxes();
    onClear();

    try {
      mealRequestsTable.getItems().addAll(dao.getAll());
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error, Mead Delivery table was unable to be created");
    }
  }

  /** Creates service request, executes when submit button is pressed */
  public void onSubmit() {
    if (allFilled()) {
      String patientID =
          patientName.getText()
              + patientLastName.getText()
              + patientDOB.getText(); // TODO : WHat is patientID
      String roomID = "A Room"; // TODO : Determine from backend
      String entree = entreeBox.getValue();
      String side = sideBox.getValue();
      String drink = drinkBox.getValue();
      String dessert = dessertBox.getValue();

      addMealRequest(
          new MealDeliveryRequest(
              Request.Priority.LOW,
              roomID,
              "RequesterID",
              "AssigneeID",
              patientID,
              entree,
              side,
              drink,
              dessert));

    } else {
      errorLabel.setText("Error: One or more fields are empty!");
      return;
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
    patientDOB.clear();
  }

  /**
   * Adds a meal request to the JFX table view
   *
   * @param request request object to be added
   */
  public void addMealRequest(MealDeliveryRequest request) {
    mealRequestsTable.getItems().add(request);
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
        || patientDOB.getText().equals("")
        || patientLastName.getText().equals("")));
  }
  /** Saves a given service request to a CSV by opening the CSV window */
  public void saveToCSV() {
    FileChooser fileSys = new FileChooser();
    Stage window = (Stage) mealRequestsTable.getScene().getWindow();
    fileSys.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
    File csv = fileSys.showSaveDialog(window);
    try {
      csvSaver.save((new MealDeliveryRequest()), csv.getAbsolutePath());
    } catch (Exception e) {
      System.err.println("Unable to Save CSV");
    }
  }
}
