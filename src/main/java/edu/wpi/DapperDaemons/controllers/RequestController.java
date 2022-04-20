package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOFacade;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.entities.Notification;
import edu.wpi.DapperDaemons.entities.TableObject;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;

public class RequestController extends ParentController implements Initializable {
  private TableHelper<Request> tableHelper;
  private TableHelper<Request> tableHelper1;
  private TableHelper<Request> tableHelper2;
  @FXML private TableColumn<Request, String> Assignee22;
  @FXML private TableColumn<Request, String> Assignee221;
  @FXML private TableColumn<Request, String> Assignee222;
  @FXML private TableColumn<Request, String> Priority22;
  @FXML private TableColumn<Request, String> Priority221;
  @FXML private TableColumn<Request, String> Priority222;
  @FXML private TableColumn<Request, String> ReqID22;
  @FXML private TableColumn<Request, String> ReqID221;
  @FXML private TableColumn<Request, String> ReqID222;
  @FXML private TableColumn<Request, String> Requester22;
  @FXML private TableColumn<Request, String> Requester221;
  @FXML private TableColumn<Request, String> Requester222;
  @FXML private TableColumn<Request, String> RoomID22;
  @FXML private TableColumn<Request, String> RoomID221;
  @FXML private TableColumn<Request, String> RoomID222;
  @FXML private TableColumn<Request, Request.RequestStatus> Status22;
  @FXML private TableColumn<Request, String> Status221;
  @FXML private TableColumn<Request, Request.RequestStatus> Status222;
  @FXML private ToggleButton assignedRequests;
  @FXML private TableView<Request> assignedRequestsTable;
  @FXML private ToggleButton createdRequests;
  @FXML private TableView<Request> createdRequestsTable;
  @FXML private ToggleButton relevantRequests;
  @FXML private TableView<Request> relevantRequestsTable;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    tableinit();
    selectCreated(new ActionEvent());
  }

  @FXML
  void selectAssigned(ActionEvent event) {
    assignedRequestsTable.setVisible(true);
    createdRequestsTable.setVisible(false);
    relevantRequestsTable.setVisible(false);
    assignedRequestsTable.setPickOnBounds(true);
    createdRequestsTable.setPickOnBounds(false);
    relevantRequestsTable.setPickOnBounds(false);
  }

  @FXML
  void selectCreated(ActionEvent event) {
    assignedRequestsTable.setVisible(false);
    createdRequestsTable.setVisible(true);
    relevantRequestsTable.setVisible(false);
    assignedRequestsTable.setPickOnBounds(false);
    createdRequestsTable.setPickOnBounds(true);
    relevantRequestsTable.setPickOnBounds(false);
  }

  @FXML
  void selectRelevant(ActionEvent event) {
    assignedRequestsTable.setVisible(false);
    createdRequestsTable.setVisible(false);
    relevantRequestsTable.setVisible(true);
    assignedRequestsTable.setPickOnBounds(false);
    createdRequestsTable.setPickOnBounds(false);
    relevantRequestsTable.setPickOnBounds(true);
  }

  @FXML
  void assigneeNewPleb(TableColumn.CellEditEvent<Request, String> event) {
    Request request = event.getRowValue();
    DAO<TableObject> requestDAO = DAOPouch.getDAO((TableObject) request);

    ((TableObject) request).setAttribute(5, event.getNewValue());
    if (requestDAO.update(((TableObject) request))) {
      DAOPouch.getNotificationDAO()
          .add(
              new Notification(
                  request.requestType(),
                  "You have been assigned by"
                      + SecurityController.getUser().getFirstName()
                      + " "
                      + SecurityController.getUser().getLastName()
                      + ".",
                  event.getNewValue()));
    }
    tableupdate();
  }

  @FXML
  void onEditStatus(TableColumn.CellEditEvent<Request, String> event) {
    Request request = event.getRowValue();
    DAO<TableObject> requestDAO = DAOPouch.getDAO((TableObject) request);

    ((TableObject) request).setAttribute(6, event.getNewValue());
    if (requestDAO.update(((TableObject) request))) {
      DAOPouch.getNotificationDAO()
          .add(
              new Notification(
                  request.requestType(),
                  "You have been assigned by"
                      + SecurityController.getUser().getFirstName()
                      + " "
                      + SecurityController.getUser().getLastName()
                      + ".",
                  SecurityController.getUser().getNodeID()));
    }
    tableupdate();
  }

  @FXML
  void onVolunteer(TableColumn.CellEditEvent<Request, String> event) {
    Request request = event.getRowValue();
    DAO<TableObject> requestDAO = DAOPouch.getDAO((TableObject) request);

    ((TableObject) request).setAttribute(5, SecurityController.getUser().getNodeID());
    ((TableObject) request).setAttribute(6, Request.RequestStatus.IN_PROGRESS.toString());
    if (requestDAO.update(((TableObject) request))) {
      DAOPouch.getNotificationDAO()
          .add(
              new Notification(
                  request.requestType(),
                  "You have been assigned by"
                      + SecurityController.getUser().getFirstName()
                      + " "
                      + SecurityController.getUser().getLastName()
                      + ".",
                      SecurityController.getUser().getNodeID()));
    }
    tableupdate();
  }

  public void tableinit() {
    tableHelper = new TableHelper<>(assignedRequestsTable, 2);
    tableHelper.linkColumns(Request.class);

    tableHelper1 = new TableHelper<>(createdRequestsTable, 2);
    tableHelper1.linkColumns(Request.class);

    tableHelper2 = new TableHelper<>(relevantRequestsTable, 2);
    tableHelper2.linkColumns(Request.class);

    tableupdate();

    assignedRequestsTable.setPickOnBounds(false);
    createdRequestsTable.setPickOnBounds(false);
    relevantRequestsTable.setPickOnBounds(false);

    String[] plebs = DAOFacade.getAllPlebs().toArray(new String[DAOFacade.getAllPlebs().size()]);
    tableHelper.addDropDownEditProperty(
        Status221,
        Request.RequestStatus.REQUESTED.toString(),
        Request.RequestStatus.IN_PROGRESS.toString(),
        Request.RequestStatus.CANCELLED.toString(),
        Request.RequestStatus.COMPLETED.toString());
    tableHelper1.addDropDownEditProperty(Assignee22, plebs);
    tableHelper2.addDropDownEditProperty(Assignee222, "yourself");
  }

  private void tableupdate() {

    assignedRequestsTable.getItems().clear();
    createdRequestsTable.getItems().clear();
    relevantRequestsTable.getItems().clear();
    assignedRequestsTable.getItems().addAll(DAOFacade.getAllRequests());
    createdRequestsTable.getItems().addAll(DAOFacade.getAllRequests());
    relevantRequestsTable.getItems().addAll(DAOFacade.getAllRequests());

    tableHelper.filterTable(Assignee221, SecurityController.getUser().getNodeID());
    tableHelper1.filterTable(Requester22, SecurityController.getUser().getNodeID());
    tableHelper2.filterTable(Status222, Request.RequestStatus.REQUESTED);
  }
}
