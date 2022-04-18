package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.entities.requests.MedicalEquipmentRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.TableHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class RequestController extends ParentController implements Initializable {
    private TableHelper<Request> tableHelper;
    private TableHelper<Request> tableHelper1;
    private TableHelper<Request> tableHelper2;
    @FXML private TableColumn<Request, String> Assignee;
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
    @FXML private TableColumn<Request, String> Status22;
    @FXML private TableColumn<Request, String> Status221;
    @FXML private TableColumn<Request, String> Status222;
    @FXML private ToggleButton assignedRequests;
    @FXML private TableView<Request> assignedRequestsTable;
    @FXML private ToggleButton createdRequests;
    @FXML private TableView<Request> createdRequestsTable;
    @FXML private ToggleButton relevantRequests;
    @FXML private TableView<Request> relevantRequestsTable;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableinit();



    }



    @FXML
    void selectAssigned(ActionEvent event) {

    }

    @FXML
    void selectCreated(ActionEvent event) {

    }

    @FXML
    void selectRelevant(ActionEvent event) {

    }

    public void tableinit(){
        tableHelper = new TableHelper<>(assignedRequestsTable, 2);
        tableHelper.linkColumns(Request.class);

        tableHelper1 = new TableHelper<>(createdRequestsTable, 2);
        tableHelper1.linkColumns(Request.class);

        tableHelper2 = new TableHelper<>(relevantRequestsTable, 2);
        tableHelper2.linkColumns(Request.class);







    }




}
