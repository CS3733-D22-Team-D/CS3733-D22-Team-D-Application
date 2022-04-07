package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.requests.PatientTransportRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
  @FXML private TextField patientDOB;

  List<String> names;
  PatientTransportRequestHandler handler = new PatientTransportRequestHandler();

  DAO<PatientTransportRequest> dao = DAOPouch.getPatientTransportRequestDAO();

  /** Initializes the controller objects (After runtime, before graphics creation) */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    PatientTransportInitializer init = new PatientTransportInitializer();
    init.initializeTable();
    init.initializeInputs();
    init.initializeRequests();

    try {
      transportRequests.getItems().addAll(dao.getAll());
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
    patientDOB.setText("");
  }

  @FXML
  public void onSubmitClicked() {
    if (!(roomBox.getValue().trim().equals("") || pBox.getValue().trim().equals(""))) {
      Request.Priority priority = Request.Priority.valueOf(pBox.getValue());

      addItem(
          new PatientTransportRequest(
              priority,
              "CURRENTROOM OF PATIENT",
              "REQUESTERID",
              "ASSIGNEEID",
              patientFirstName.getText() + patientLastName.getText() + patientDOB.getText(),
              roomBox.getValue(),
              Request.RequestStatus.REQUESTED));

      onClearClicked();
    }
  }

  private void addItem(PatientTransportRequest request) {
    transportRequests.getItems().add(request);
    // TODO: Add request to database
  }

  private class PatientTransportInitializer {
    private void initializeTable() {
      tableHelper = new TableHelper<>(transportRequests, 0);
      tableHelper.linkColumns(PatientTransportRequest.class);
    }

    // TODO: Pull inputs for drop-down from database
    private void initializeInputs() {
      pBox.setItems(FXCollections.observableArrayList("LOW", "MEDIUM", "HIGH"));
      roomBox.setItems(FXCollections.observableArrayList(handler.getAllLongNames()));
    }

    // TODO: Pull transport requests from database
    private void initializeRequests() {}
  }
}
