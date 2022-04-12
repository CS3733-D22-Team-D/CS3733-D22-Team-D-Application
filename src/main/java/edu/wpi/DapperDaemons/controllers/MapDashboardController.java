package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import edu.wpi.DapperDaemons.entities.Patient;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.map.RequestHandler;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;

public class MapDashboardController extends UIController {

  @FXML private TableView<MedicalEquipment> equipTable;
  private final DAO<MedicalEquipment> equipmentDAO = DAOPouch.getMedicalEquipmentDAO();
  @FXML private TableView<Location> locTable;
  private final DAO<Location> locationDAO = DAOPouch.getLocationDAO();
  @FXML private TableView<Patient> patientTable;
  private final DAO<Patient> patientDAO = DAOPouch.getPatientDAO();
  @FXML private TableView<Request> reqTable;

  @FXML private ToggleButton L1;
  @FXML private ToggleButton L10;
  @FXML private ToggleButton L11;
  @FXML private ToggleButton L12;
  @FXML private ToggleButton L14;
  @FXML private ToggleButton L15;
  @FXML private ToggleButton L16;
  @FXML private ToggleButton L2;
  @FXML private ToggleButton L3;
  @FXML private ToggleButton L4;
  @FXML private ToggleButton L5;
  @FXML private ToggleButton L6;
  @FXML private ToggleButton L7;
  @FXML private ToggleButton L8;
  @FXML private ToggleButton L9;
  @FXML private ToggleButton LL1;
  @FXML private ToggleButton LL2;
  @FXML private ToggleButton M1;
  @FXML private ToggleButton M2;

  private String floor;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);

    // Init tables
    new TableHelper<>(equipTable, 2).linkColumns(MedicalEquipment.class);
    new TableHelper<>(locTable, 2).linkColumns(Location.class);
    new TableHelper<>(patientTable, 2).linkColumns(Patient.class);

    TableColumn<Request, String> nameCol =
        (TableColumn<Request, String>) reqTable.getColumns().get(0);
    nameCol.setCellValueFactory(req -> new SimpleStringProperty(req.getValue().getRequestType()));
    TableColumn<Request, String> pCol = (TableColumn<Request, String>) reqTable.getColumns().get(1);
    pCol.setCellValueFactory(req -> new SimpleStringProperty(req.getValue().getPriority().name()));
    TableColumn<Request, Boolean> rTCol =
        (TableColumn<Request, Boolean>) reqTable.getColumns().get(2);
    rTCol.setCellValueFactory(req -> new SimpleBooleanProperty(req.getValue().requiresTransport()));

    // Default floor
    this.floor = "1";
    updateTables();
  }

  // Updates the data based on current floor
  private void updateTables() {
    equipTable.getItems().clear();
    patientTable.getItems().clear();
    reqTable.getItems().clear();
    locTable.getItems().clear();
    List<Location> locsByFloor;
    try {
      locsByFloor = locationDAO.filter(4, floor);
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Failed to get locations.");
      return;
    }
    for (Location l : locsByFloor) {
      try {
        equipTable.getItems().addAll(equipmentDAO.filter(6, l.getNodeID()));
        patientTable.getItems().addAll(patientDAO.filter(6, l.getNodeID()));
        reqTable.getItems().addAll(RequestHandler.getFilteredRequests(l.getNodeID()));
        locTable.getItems().add(l);
      } catch (SQLException e) {
        showError("Failed to show data on tables.");
      }
    }
  }

  @FXML
  void switchToL1(ActionEvent event) {
    if (L1.isSelected()) {
      this.floor = "1";
      updateTables();
    }
  }

  @FXML
  void switchToL10(ActionEvent event) {
    if (L10.isSelected()) {
      this.floor = "10";
      updateTables();
    }
  }

  @FXML
  void switchToL11(ActionEvent event) {
    if (L11.isSelected()) {
      this.floor = "11";
      updateTables();
    }
  }

  @FXML
  void switchToL12(ActionEvent event) {
    if (L12.isSelected()) {
      this.floor = "12";
      updateTables();
    }
  }

  @FXML
  void switchToL14(ActionEvent event) {
    if (L14.isSelected()) {
      this.floor = "14";
      updateTables();
    }
  }

  @FXML
  void switchToL15(ActionEvent event) {
    if (L15.isSelected()) {
      this.floor = "15";
      updateTables();
    }
  }

  @FXML
  void switchToL16(ActionEvent event) {
    if (L16.isSelected()) {
      this.floor = "16";
      updateTables();
    }
  }

  @FXML
  void switchToL2(ActionEvent event) {
    if (L2.isSelected()) {
      this.floor = "2";
      updateTables();
    }
  }

  @FXML
  void switchToL3(ActionEvent event) {
    if (L3.isSelected()) {
      this.floor = "3";
      updateTables();
    }
  }

  @FXML
  void switchToL4(ActionEvent event) {
    if (L4.isSelected()) {
      this.floor = "4";
      updateTables();
    }
  }

  @FXML
  void switchToL5(ActionEvent event) {
    if (L5.isSelected()) {
      this.floor = "5";
      updateTables();
    }
  }

  @FXML
  void switchToL6(ActionEvent event) {
    if (L6.isSelected()) {
      this.floor = "6";
      updateTables();
    }
  }

  @FXML
  void switchToL7(ActionEvent event) {
    if (L7.isSelected()) {
      this.floor = "7";
      updateTables();
    }
  }

  @FXML
  void switchToL8(ActionEvent event) {
    if (L8.isSelected()) {
      this.floor = "8";
      updateTables();
    }
  }

  @FXML
  void switchToL9(ActionEvent event) {
    if (L9.isSelected()) {
      this.floor = "9";
      updateTables();
    }
  }

  @FXML
  void switchToLL1(ActionEvent event) {
    if (LL1.isSelected()) {
      this.floor = "L1";
      updateTables();
    }
  }

  @FXML
  void switchToLL2(ActionEvent event) {
    if (LL2.isSelected()) {
      this.floor = "L2";
      updateTables();
    }
  }

  @FXML
  void switchToM1(ActionEvent event) {
    if (M1.isSelected()) {
      this.floor = "M1";
      updateTables();
    }
  }

  @FXML
  void switchToM2(ActionEvent event) {
    if (M2.isSelected()) {
      this.floor = "M2";
      updateTables();
    }
  }
}
