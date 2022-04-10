package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.requests.PatientTransportRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.entities.requests.SanitationRequest;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SanitationController extends UIController {

  /* Table Object */
  @FXML private TableView<SanitationRequest> pendingRequests;

  /* Table Helper */
  private TableHelper<SanitationRequest> helper;

  /* Table Columns */
  @FXML private TableColumn<PatientTransportRequest, String> ReqID;
  @FXML private TableColumn<PatientTransportRequest, String> Priority;
  @FXML private TableColumn<PatientTransportRequest, String> RoomID;
  @FXML private TableColumn<PatientTransportRequest, String> Requester;
  @FXML private TableColumn<PatientTransportRequest, String> Assignee;
  @FXML private TableColumn<PatientTransportRequest, String> Service;
  @FXML private TableColumn<PatientTransportRequest, String> Status;

  /* Dropdown Boxes */
  @FXML private JFXComboBox<String> sanitationBox;
  @FXML private JFXComboBox<String> priorityBox;

  /* Text Field */
  @FXML private TextField locationText;

  private final DAO<SanitationRequest> dao = DAOPouch.getSanitationRequestDAO();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    onClearClicked();
    helper = new TableHelper<>(pendingRequests, 0);
    helper.linkColumns(SanitationRequest.class);

    priorityBox.setItems(
        FXCollections.observableArrayList(TableHelper.convertEnum(Request.Priority.class)));
    sanitationBox.setItems(
        FXCollections.observableArrayList(
            "Mopping/Sweeping", "Sterilize", "Trash", "Bio-Hazard Contamination"));

    try {
      pendingRequests.getItems().addAll(dao.getAll());
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Something went wrong making Patient Transport Req table");
    }

    onClearClicked();
  }

  /** clear the current information * */
  @FXML
  public void onClearClicked() {
    sanitationBox.setValue("");
    priorityBox.setValue("");
    locationText.setText("");
  }

  /** What happens when the submit button is clicked * */
  @FXML
  public void onSubmitClicked() {
    if (!((sanitationBox.getValue().equals(""))
        || priorityBox.getValue().equals("")
        || locationText.getText().equals(""))) {

      Request.Priority priority = Request.Priority.valueOf(priorityBox.getValue());

      addItem(
          new SanitationRequest(
              priority,
              locationText.getText(),
              "REQUESTERID",
              "ASSIGNEEID",
              sanitationBox.getValue(),
              Request.RequestStatus.REQUESTED));
      onClearClicked();
    }
  }
  /** Saves a given service request to a CSV by opening the CSV window */
  public void saveToCSV() {
    super.saveToCSV(new SanitationRequest());
  }

  /** Adds new sanitationRequest to table of pending requests * */
  private void addItem(SanitationRequest request) {
    try {
      dao.add(request);
      pendingRequests.getItems().add(request);
    } catch (SQLException e) {
      System.err.println("Sanitation request could not be added to DAO");
    }
  }
}
