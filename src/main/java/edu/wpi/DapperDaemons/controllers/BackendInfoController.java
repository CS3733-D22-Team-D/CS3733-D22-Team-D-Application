package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import edu.wpi.DapperDaemons.entities.Patient;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/** Controller for Backend Info Page, allows for the database tables to be displayed */
public class BackendInfoController extends UIController {

  /* Patient Table and Columns */
  @FXML private TableView<Patient> patientsTable;
  private TableHelper<Patient> patientTableHelper;

  /* Location Table and Columns */
  @FXML private TableView<Location> locationsTable;
  private TableHelper<Location> locationTableHelper;

  /* Employee Table and Columns */
  @FXML private TableView<Employee> employeesTable;
  private TableHelper<Employee> employeeTableHelper;

  /* Equipment Table and Columns */
  @FXML private TableView<MedicalEquipment> equipmentTable;
  private TableHelper<MedicalEquipment> equipmentTableHelper;

  /* DAO Objects */
  private DAO<Location> locationDAO = DAOPouch.getLocationDAO();
  private DAO<Patient> patientDAO = DAOPouch.getPatientDAO();
  private DAO<Employee> employeeDAO = DAOPouch.getEmployeeDAO();
  private DAO<MedicalEquipment> medicalEquipmentDAO = DAOPouch.getMedicalEquipmentDAO();

  /* Background */
  @FXML private ImageView BGImage;
  @FXML private Pane BGContainer;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    bindImage(BGImage, BGContainer);

    // TODO : The patient DAO is broken :(
    patientTableHelper = new TableHelper<>(patientsTable, 0);
    patientTableHelper.linkColumns(Patient.class);

    locationTableHelper = new TableHelper<>(locationsTable, 0);
    locationTableHelper.linkColumns(Location.class);

    employeeTableHelper = new TableHelper<>(employeesTable, 0);
    employeeTableHelper.linkColumns(Employee.class);

    equipmentTableHelper = new TableHelper<>(equipmentTable, 0);
    equipmentTableHelper.linkColumns(MedicalEquipment.class);

    try {
      locationsTable.getItems().addAll(new ArrayList<>(locationDAO.getAll().values()));

      employeesTable.getItems().addAll(new ArrayList<>(employeeDAO.getAll().values()));

      equipmentTable.getItems().addAll(new ArrayList<>(medicalEquipmentDAO.getAll().values()));

      //      System.out.println(patientDAO.getAll());
      patientsTable.getItems().addAll(new ArrayList<>(patientDAO.getAll().values()));

    } catch (Exception e) {
      e.printStackTrace();
      System.err.print("Error, table was unable to be created\n");
    }
  }
}
