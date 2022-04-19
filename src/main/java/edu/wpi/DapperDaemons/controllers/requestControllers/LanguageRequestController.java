package edu.wpi.DapperDaemons.controllers.requestControllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOFacade;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.controllers.ParentController;
import edu.wpi.DapperDaemons.controllers.helpers.TableListeners;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.requests.LanguageRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/** Equipment Request UI Controller UPDATED 4/5/22 12:30AM */
public class LanguageRequestController extends ParentController {

  /* Table Object */
  @FXML private TableView<LanguageRequest> languageRequestsTable;

  /* Table Helper */
  private TableHelper<LanguageRequest> tableHelper;

  /* Sexy MOTHERFUCKING  JFXComboBoxes */
  @FXML private JFXComboBox<String> languageBox;
  @FXML private JFXComboBox<String> roomBox;
  @FXML private DatePicker dateNeeded;
 @FXML private TextField notes;
  /* Table Columns */
  @FXML private TableColumn<LanguageRequest, String> reqID;
  @FXML private TableColumn<LanguageRequest, String> language;
  @FXML private TableColumn<LanguageRequest, String> roomID;
  @FXML private TableColumn<LanguageRequest, String> requester;
  @FXML private TableColumn<LanguageRequest, String> assignee;

  /* DAO Object */
  private DAO<LanguageRequest> languageRequestDAO = DAOPouch.getLanguageRequestDAO();
  private DAO<Location> locationDAO = DAOPouch.getLocationDAO();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //        super.initialize(location, resources);
    initBoxes();
    //    bindImage(BGImage, BGContainer);

    tableHelper = new TableHelper<>(languageRequestsTable, 0);
    tableHelper.linkColumns(LanguageRequest.class);

    try { // Removed second field (filename) since everything is
      // loaded on startup
      languageRequestsTable.getItems().addAll(new ArrayList(languageRequestDAO.getAll().values()));
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
              languageRequestsTable.getItems().clear();
              languageRequestsTable
                  .getItems()
                  .addAll(new ArrayList(languageRequestDAO.getAll().values()));
            }));
  }

  public boolean addItem(LanguageRequest request) {
    boolean hadClearance = true;

    hadClearance = languageRequestDAO.add(request);
    if (hadClearance) {
      languageRequestsTable.getItems().add(request);
    }
    return hadClearance;
  }

  @FXML
  public void onClearClicked() {
    languageBox.setValue("");
    roomBox.setValue("");
    dateNeeded.setValue(null);
    notes.setText("");
  }

  @FXML
  public void onSubmitClicked() {

    // make sure all fields are filled
    if (allFieldsFilled()) {
      String dateRep =
          ""
              + dateNeeded.getValue().getMonthValue()
              + dateNeeded.getValue().getDayOfMonth()
              + dateNeeded.getValue().getYear();
      addItem(
          new LanguageRequest(
              ));
    } else {
      // TODO uncomment when fixed
      //   showError("All fields must be filled.");
    }
    onClearClicked();
  }

  private boolean allFieldsFilled() {
    return !(languageBox.getValue().equals("")
        || roomBox.getValue().equals("")
        || dateNeeded.getValue() != null);
  }

  public void initBoxes() {
    languageBox.setItems(
        FXCollections.observableArrayList(TableHelper.convertEnum(LanguageRequest.Language.class)));
    roomBox.setItems(FXCollections.observableArrayList(DAOFacade.getAllLocationLongNames()));
  }
  /** Saves a given service request to a CSV by opening the CSV window */
  public void saveToCSV() {
    super.saveToCSV(new LanguageRequest(), (Stage) roomBox.getScene().getWindow());
  }
}
