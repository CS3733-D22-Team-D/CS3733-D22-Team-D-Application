package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.Location;
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
  public void onSubmitClicked() {}

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

    try {
      locations = (ArrayList) locationDAO.search(7, roomBox.getValue());
    } catch (SQLException e) {
      e.printStackTrace();
    }
    for (Location l : locations) {
      locationNames.add(l.getAttribute(7));
    }
    System.out.println(locationNames);
    roomBox.setItems(FXCollections.observableArrayList(locationNames));
  }

  private class PatientTransportInitializer {

    private void initializeTable() {
      tableHelper = new TableHelper<>(transportRequests, 0);
      tableHelper.linkColumns(PatientTransportRequest.class);
    }

    private void initializeInputs() {

      pBox.setItems(FXCollections.observableArrayList("LOW", "MEDIUM", "HIGH"));
      roomBox.setItems(FXCollections.observableArrayList(getAllLongNames()));
      roomBox.getEditor().setOnKeyPressed(E -> searchRoomsDropDown());
    }
  }
}
