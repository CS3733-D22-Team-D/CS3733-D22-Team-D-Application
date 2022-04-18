package edu.wpi.DapperDaemons.controllers.requestControllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.controllers.ParentController;
import edu.wpi.DapperDaemons.controllers.helpers.AutoCompleteFuzzy;
import edu.wpi.DapperDaemons.controllers.helpers.FuzzySearchComparatorMethod;
import edu.wpi.DapperDaemons.controllers.helpers.TableListeners;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.requests.PatientTransportRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.entities.requests.SanitationRequest;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SanitationController extends ParentController {

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
  @FXML private TextField notes;
  DAO<SanitationRequest> sanitationRequestDAO = DAOPouch.getSanitationRequestDAO();
  DAO<Location> locationDAO = DAOPouch.getLocationDAO();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    onClearClicked();
    //    initializeInputs(); TODO: Get all long names problem
    initializeTable();

    try {
      pendingRequests.getItems().addAll(new ArrayList(sanitationRequestDAO.getAll().values()));
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Something went wrong making Patient Transport Req table");
    }
    setListeners();
  }

  private void setListeners() {
    TableListeners tl = new TableListeners();
    tl.setSanitationRequestListener(
        tl.eventListener(
            () -> {
              pendingRequests.getItems().clear();
              pendingRequests
                  .getItems()
                  .addAll(new ArrayList(sanitationRequestDAO.getAll().values()));
            }));
  }

  /** clear the current information * */
  @FXML
  public void onClearClicked() {
    sanitationBox.setValue("");
    priorityBox.setValue("");
    locationBox.setValue("");
    notes.setText("");
  }

  @FXML
  public void startFuzzySearch() {
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(sanitationBox, new FuzzySearchComparatorMethod());
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(locationBox, new FuzzySearchComparatorMethod());
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(priorityBox, new FuzzySearchComparatorMethod());
  }
  /** What happens when the submit button is clicked * */
  @FXML
  public void onSubmitClicked() {

    if (allFieldsFilled()) {
      Request.Priority priority = Request.Priority.valueOf(priorityBox.getValue());
      String roomID = locationBox.getValue();
      String requesterID = SecurityController.getUser().getNodeID();
      String assigneeID = "null";
      String sanitationType = sanitationBox.getValue().toString();
      Request.RequestStatus status = Request.RequestStatus.REQUESTED;

      /*Make sure the room exists*/
      boolean isALocation = false;
      Location location = new Location();
      ArrayList<Location> locations = new ArrayList<>();
      locations = new ArrayList(locationDAO.getAll().values());

      location = new ArrayList<Location>(locationDAO.filter(locations, 7, roomID).values()).get(0);

      isALocation = location.getAttribute(7).equals(roomID);
      if (isALocation) {

        boolean hadClearance =
            addItem(
                new SanitationRequest(
                    priority, roomID, requesterID, assigneeID, notes.getText(), sanitationType));

        if (!hadClearance) {
          // throw error saying that the user does not have permission to make the request.
          showError("You do not have permission to do this.");
        }
      } else {
        // throw an error that the location does not exist
        showError("A room with that name does not exist.");
      }
    } else {
      //  throw error message that all fields need to be filled
      showError("All fields must be filled.");
    }
    // clear the fields
    onClearClicked();
  }

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

  private boolean allFieldsFilled() {
    return !((sanitationBox.getValue().equals(""))
        || priorityBox.getValue().equals("")
        || locationBox.getValue().equals(""));
  }

  /** Adds new sanitationRequest to table of pending requests * */
  private boolean addItem(SanitationRequest request) {
    boolean hasClearance = false;
    hasClearance = sanitationRequestDAO.add(request);

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

  /** Saves a given service request to a CSV by opening the CSV window */
  public void saveToCSV() {
    super.saveToCSV(new SanitationRequest());
  }
}
