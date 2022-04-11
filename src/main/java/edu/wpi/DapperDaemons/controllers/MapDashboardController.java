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
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MapDashboardController extends UIController {

  @FXML private TableView<MedicalEquipment> equipTable;
  private DAO<MedicalEquipment> equipmentDAO = DAOPouch.getMedicalEquipmentDAO();
  @FXML private TableView<Location> locTable;
  private DAO<Location> locationDAO = DAOPouch.getLocationDAO();
  @FXML private TableView<Patient> patientTable;
  private DAO<Patient> patientDAO = DAOPouch.getPatientDAO();
  @FXML private TableView<Request> reqTable;

  private String floor;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    this.floor = "1";

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

    try {
      updateTables();
    } catch (SQLException e) {
      e.printStackTrace();
      // TODO: Show error
    }
  }

  private void updateTables() throws SQLException {
    equipTable.getItems().clear();
    patientTable.getItems().clear();
    reqTable.getItems().clear();
    List<Location> locsByFloor = locationDAO.filter(4, floor);
    for (Location l : locsByFloor) {
      equipTable.getItems().addAll(equipmentDAO.filter(6, l.getNodeID()));
      patientTable.getItems().addAll(patientDAO.filter(6, l.getNodeID()));
      reqTable.getItems().addAll(RequestHandler.getFilteredRequests(l.getNodeID()));
      locTable.getItems().add(l);
    }
  }
}
