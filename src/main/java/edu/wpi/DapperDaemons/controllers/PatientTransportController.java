package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.Patient;
import edu.wpi.DapperDaemons.entities.requests.PatientTransportRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/** Patient Transport Controller UPDATED 4/5/22 12:42 PM */
public class PatientTransportController extends UIController implements Initializable {

  /* Table Object */
  @FXML private TableView<PatientTransportRequest> transportRequests;

  /*Table Helper */
  private TableHelper<PatientTransportRequest> tableHelper;

  /* Table Columns */
  @FXML private TableColumn<PatientTransportRequest, String> ReqID;
  @FXML private TableColumn<PatientTransportRequest, String> Priority;
  @FXML private TableColumn<PatientTransportRequest, String> RoomID;
  @FXML private TableColumn<PatientTransportRequest, String> Requester;
  @FXML private TableColumn<PatientTransportRequest, String> Assignee;
  @FXML private TableColumn<PatientTransportRequest, String> Patient;
  @FXML private TableColumn<PatientTransportRequest, String> Destination;
  @FXML private TableColumn<PatientTransportRequest, String> Status;

  /* Dropdown boxes */
  @FXML private JFXComboBox<String> roomBox;
  @FXML private JFXComboBox<String> pBox;

  /* Text Boxes */
  @FXML private TextField patientFirstName;
  @FXML private TextField patientLastName;
  @FXML private DatePicker patientDOB;

  List<String> names;
  // PatientTransportRequestHandler handler = new PatientTransportRequestHandler();

  DAO<PatientTransportRequest> patientTransportRequestDAO =
      DAOPouch.getPatientTransportRequestDAO();
  DAO<edu.wpi.DapperDaemons.entities.Patient> patientDAO = DAOPouch.getPatientDAO();
  DAO<Location> locationDAO = DAOPouch.getLocationDAO();

  /** Initializes the controller objects (After runtime, before graphics creation) */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    PatientTransportInitializer init = new PatientTransportInitializer();
    init.initializeTable();
    init.initializeInputs();

    try {
      transportRequests.getItems().addAll(patientTransportRequestDAO.getAll());
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Something went wrong making Patient Transport Req table");
    }
  }

  @FXML
  public void editStatus(TableColumn.CellEditEvent<PatientTransportRequest, String> editEvent) {
    editEvent.getRowValue().setStatus(Request.RequestStatus.valueOf(editEvent.getNewValue()));
    tableHelper.update(); // Commented out so it can run
  }

  @FXML
  public void onClearClicked() {
    roomBox.setValue("");
    pBox.setValue("");
    patientFirstName.setText("");
    patientLastName.setText("");
    patientDOB.setValue(null);
  }

  @FXML
  public void onSubmitClicked() {
    Request.Priority priority = Request.Priority.valueOf(pBox.getValue());
    String roomID;
    String requesterID = SecurityController.getInstance().getUser().getNodeID();
    String assigneeID = "null";
    String patientID;
    String nextRoomID = "";
    Request.RequestStatus status = Request.RequestStatus.REQUESTED;

    if (fieldsNonEmpty()) {
      // Determine if the next Location exists
      ArrayList<Location> locations = new ArrayList<>();
      boolean nextLocationExists = false;
      try {
        locations = (ArrayList<Location>) locationDAO.getAll();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      for (Location l : locations) {
        if (l.getAttribute(7).equals(roomBox.getValue())) {
          nextRoomID = l.getNodeID();
          nextLocationExists = true;
        }
      }
      if (nextLocationExists) {

        // Now Check of the patient exists
        boolean patientExists = false;
        Patient patient = new Patient();
        patientID =
            patientFirstName.getText()
                + patientLastName.getText()
                + patientDOB.getValue().getMonthValue()
                + patientDOB.getValue().getDayOfMonth()
                + patientDOB.getValue().getYear();

        try {
          patient = patientDAO.get(patientID);
        } catch (SQLException e) {
          e.printStackTrace();
        }
        patientExists = patient.getFirstName().equals(patientFirstName.getText());

        if (patientExists) {

          // now send request and get back whether it went through.

          roomID = patient.getLocationID();
          boolean hadPermission =
              addItem(
                  new PatientTransportRequest(
                      priority, roomID, requesterID, assigneeID, patientID, nextRoomID, status));
          if (!hadPermission) {
            // TODO display error that employee does not have permission
          }
        } else {
          // TODO display error that patient does not exist
        }
      } else {
        // TODO display error that location does not exist
      }
    } else {
      // TODO display error message not all fields filled
    }
    onClearClicked();
  }

  public boolean fieldsNonEmpty() {

    return !(roomBox.getValue().equals("")
        || pBox.getValue().equals("")
        || patientFirstName.getText().equals("")
        || patientLastName.getText().equals("")
        || patientDOB.getValue() == null);
  }

  private boolean addItem(PatientTransportRequest request) {
    boolean hasClearance = false;

    try {
      hasClearance = patientTransportRequestDAO.add(request);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (hasClearance) {
      transportRequests.getItems().add(request);
    }
    return hasClearance;
  }

  private void searchRoomsDropDown() {

    ArrayList<Location> locations = new ArrayList<>();
    ArrayList<String> locationNames = new ArrayList<>();
    String value = roomBox.getValue() + "";

    try {
      locations = (ArrayList) locationDAO.search(locationDAO.getAll(), 7, value);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    for (Location l : locations) {
      locationNames.add(l.getAttribute(7));
    }
    System.out.println(locationNames);
    roomBox.setValue(String.valueOf(locationNames));
  }

  private class PatientTransportInitializer {

    private void initializeTable() {
      tableHelper = new TableHelper<>(transportRequests, 0);
      tableHelper.linkColumns(PatientTransportRequest.class);
    }

    private void initializeInputs() {

      pBox.setItems(FXCollections.observableArrayList("LOW", "MEDIUM", "HIGH"));
      roomBox.setItems(FXCollections.observableArrayList(getAllLongNames()));

      // TODO FIGURE OUT WHY THE FUCK THIS SEARCH SHIT DOESNT WORK
      // AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHHHHH
      // roomBox.getEditor().setOnKeyPressed(E -> searchRoomsDropDown());
    }
  }

  public void saveToCSV() {
    super.saveToCSV(new PatientTransportRequest());
  }
}
