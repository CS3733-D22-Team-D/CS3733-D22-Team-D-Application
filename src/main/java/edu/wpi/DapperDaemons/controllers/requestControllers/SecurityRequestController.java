package edu.wpi.DapperDaemons.controllers.requestControllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.controllers.ParentController;
import edu.wpi.DapperDaemons.controllers.helpers.TableListeners;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.requests.MedicalEquipmentRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.entities.requests.SecurityRequest;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/** Equipment Request UI Controller UPDATED 4/5/22 12:30AM */
public class SecurityRequestController extends ParentController {

    /* Table Object */
    @FXML private TableView<SecurityRequest> SecurityRequestTable;

    /* Table Helper */
    private TableHelper<SecurityRequest> tableHelper;

    /* Sexy MOTHERFUCKING  JFXComboBoxes */
    @FXML private JFXComboBox<String> priorityBox;
    @FXML private JFXComboBox<String> roomBox;

    /* Table Columns */
    @FXML private TableColumn<SecurityRequest, String> reqID;
    @FXML private TableColumn<SecurityRequest, Request.Priority> priority;
    @FXML private TableColumn<SecurityRequest, String> roomID;
    @FXML private TableColumn<SecurityRequest, String> requester;
    @FXML private TableColumn<SecurityRequest, String> assignee;

    /* DAO Object */
    private DAO<SecurityRequest> securityRequestDAO = DAOPouch.getSecurityRequestDAO();
    private DAO<Location> locationDAO = DAOPouch.getLocationDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //        super.initialize(location, resources);
        initBoxes();
        //    bindImage(BGImage, BGContainer);

        tableHelper = new TableHelper<>(SecurityRequestTable, 0);
        tableHelper.linkColumns(SecurityRequest.class);

        try { // Removed second field (filename) since everything is
            // loaded on startup
            SecurityRequestTable.getItems().addAll(new ArrayList(securityRequestDAO.getAll().values()));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.print("Error, table was unable to be created\n");
        }

        onClearClicked();
        setListeners();
    }

    private void setListeners() {
        TableListeners tl = new TableListeners();
        tl.setLanguageRequestListener(
                tl.eventListener(
                        () -> {
                            //              System.out.println("LanguageRequestsTable");
                            SecurityRequestTable.getItems().clear();
                            SecurityRequestTable
                                    .getItems()
                                    .addAll(new ArrayList(securityRequestDAO.getAll().values()));
                        }));
    }

    public boolean addItem(SecurityRequest request) {
        boolean hadClearance = true;

        hadClearance = securityRequestDAO.add(request);
        if (hadClearance) {
            SecurityRequestTable.getItems().add(request);
        }
        return hadClearance;
    }

    @FXML
    public void onClearClicked() {
        priorityBox.setValue("");
        roomBox.setValue("");
    }

    @FXML
    public void onSubmitClicked() {

        // make sure all fields are filled
        if (allFieldsFilled()) {
            addItem(
                    new SecurityRequest(
                            SecurityRequest.priority.valueOf(languageBox.getValue()), roomBox.getValue()));
        } else {
            // TODO uncomment when fixed
            //      showError("All fields must be filled.");
        }
        onClearClicked();
    }

    private boolean allFieldsFilled() {
        return !(priorityBox.getValue().equals("") || roomBox.getValue().equals(""));
    }

    public void initBoxes() {
        priorityBox.setItems(
                FXCollections.observableArrayList(TableHelper.convertEnum(SecurityRequest.Language.class)));
        roomBox.setItems(FXCollections.observableArrayList(getAllLongNames()));
    }
    /** Saves a given service request to a CSV by opening the CSV window */
    public void saveToCSV() {
        super.saveToCSV(new MedicalEquipmentRequest());
    }
}
