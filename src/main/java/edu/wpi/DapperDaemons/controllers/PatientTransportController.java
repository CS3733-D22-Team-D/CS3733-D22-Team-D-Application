package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.requests.PatientTransportRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.sql.SQLException;
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

  // PatientTransportRequestHandler handler = new PatientTransportRequestHandler();

  private final DAO<PatientTransportRequest> dao = DAOPouch.getPatientTransportRequestDAO();

  /** Initializes the controller objects (After runtime, before graphics creation) */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    tableHelper = new TableHelper<>(transportRequests, 0);
    tableHelper.linkColumns(PatientTransportRequest.class);

    pBox.setItems(
        FXCollections.observableArrayList(TableHelper.convertEnum(Request.Priority.class)));
    roomBox.setItems(FXCollections.observableArrayList(getAllLongNames()));

    try {
      transportRequests.getItems().addAll(dao.getAll());
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Something went wrong making Patient Transport Req table");
    }

    onClearClicked();
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
  /** Saves a given service request to a CSV by opening the CSV window */
  public void saveToCSV() {
    super.saveToCSV(new PatientTransportRequest());
  }

  private void addItem(PatientTransportRequest request) {
    try {
      dao.add(request);
      transportRequests.getItems().add(request);
    } catch (SQLException e) {
      System.err.println("Patient Transport Request could not be added to DAO");
    }
  }
}
