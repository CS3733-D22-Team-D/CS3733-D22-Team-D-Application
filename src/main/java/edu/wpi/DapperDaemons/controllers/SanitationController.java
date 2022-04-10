package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.requests.PatientTransportRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.entities.requests.SanitationRequest;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
  @FXML private JFXComboBox<String> locationBox;
  /* Text Field */

  DAO<SanitationRequest> sanitationRequestDAO = DAOPouch.getSanitationRequestDAO();
  DAO<Location> locationDAO = DAOPouch.getLocationDAO();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    onClearClicked();
    SanitationServiceInitializer init = new SanitationServiceInitializer();
    init.initializeInputs();
    init.initializeTable();

    try {
      pendingRequests.getItems().addAll(sanitationRequestDAO.getAll());
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Something went wrong making Patient Transport Req table");
    }
  }

  /** clear the current information * */
  @FXML
  public void onClearClicked() {
    sanitationBox.setValue("");
    priorityBox.setValue("");
    locationBox.setValue("");
  }

  /** What happens when the submit button is clicked * */
  @FXML
  public void onSubmitClicked() {
    Request.Priority priority = Request.Priority.valueOf(priorityBox.getValue());
    String roomID = locationBox.getValue();
    String requesterID = SecurityController.getUser().getNodeID();
    String assigneeID = "null";
    String sanitationType = sanitationBox.getValue().toString();
    Request.RequestStatus status = Request.RequestStatus.REQUESTED;
    if (allFieldsFilled()) {
      /*Make sure the room exists*/
      boolean isALocation = false;
      Location location = new Location();
      ArrayList<Location> locations = new ArrayList<>();
      try {
        locations = (ArrayList<Location>) locationDAO.getAll();
      } catch (SQLException e) {
        e.printStackTrace();
      }

      location = locationDAO.filter(locations, 7, roomID).get(0);

      isALocation = location.getAttribute(7).equals(roomID);
      if (isALocation) {

        boolean hadClearance =
            addItem(
                new SanitationRequest(
                    priority, roomID, requesterID, assigneeID, sanitationType, status));

        if (!hadClearance) {
          // TODO throw error saying that the user does not have permission to make the request.
        }
      } else {
        // TODO throw an error that the location does not exist
      }
    } else {
      // TODO throw error message that all fields need to be filled
    }
    // clear the fields
    onClearClicked();
  }

  private boolean allFieldsFilled() {
    return !((sanitationBox.getValue().equals(""))
        || priorityBox.getValue().equals("")
        || locationBox.getValue().equals(""));
  }

  /** Adds new sanitationRequest to table of pending requests * */
  private boolean addItem(SanitationRequest request) {
    boolean hasClearance = false;
    try {
      hasClearance = sanitationRequestDAO.add(request);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    if (hasClearance) {
      pendingRequests.getItems().add(request);
    }
    return hasClearance;
  }

  public enum SanitationTypes {
    MoppingSweeping,
    Sterilize,
    Trash,
    BioHazard;
  }

  private class SanitationServiceInitializer {
    private void initializeTable() {
      helper = new TableHelper<>(pendingRequests, 0);
      helper.linkColumns(SanitationRequest.class);
    }

    private void initializeInputs() {
      priorityBox.setItems(
          FXCollections.observableArrayList(TableHelper.convertEnum(Request.Priority.class)));
      sanitationBox.setItems(
          FXCollections.observableArrayList(TableHelper.convertEnum(SanitationTypes.class)));

      locationBox.setItems((FXCollections.observableArrayList(getAllLongNames())));

      // locationBox.getItems().removeAll();
    }
  }

  /** Saves a given service request to a CSV by opening the CSV window */
  public void saveToCSV() {
    super.saveToCSV(new SanitationRequest());
  }
}
