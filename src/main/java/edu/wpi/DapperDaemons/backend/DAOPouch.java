package edu.wpi.DapperDaemons.backend;

import edu.wpi.DapperDaemons.entities.*;
import edu.wpi.DapperDaemons.entities.requests.*;
import java.io.IOException;
import java.sql.SQLException;

public class DAOPouch {
  private static DAO<LabRequest> labRequestDAO;
  private static DAO<MealDeliveryRequest> mealDeliveryRequestDAO;
  private static DAO<MedicalEquipmentRequest> medicalEquipmentRequestDAO;
  private static DAO<MedicineRequest> medicineRequestDAO;
  private static DAO<PatientTransportRequest> patientTransportRequestDAO;
  private static DAO<SanitationRequest> sanitationRequestDAO;
  private static DAO<Account> accountDAO;
  private static DAO<Employee> employeeDAO;
  private static DAO<Location> locationDAO;
  private static DAO<MedicalEquipment> medicalEquipmentDAO;
  private static DAO<Patient> patientDAO;

  private DAOPouch() {}

  public static void init() throws SQLException, IOException {
    labRequestDAO = new DAO(new LabRequest());
    mealDeliveryRequestDAO = new DAO(new MealDeliveryRequest());
    medicalEquipmentRequestDAO = new DAO(new MedicalEquipmentRequest());
    medicineRequestDAO = new DAO(new MedicineRequest());
    patientTransportRequestDAO = new DAO(new PatientTransportRequest());
    sanitationRequestDAO = new DAO(new SanitationRequest());
    accountDAO = new DAO(new Account());
    employeeDAO = new DAO(new Employee());
    locationDAO = new DAO(new Location());
    medicalEquipmentDAO = new DAO(new MedicalEquipment());
    patientDAO = new DAO(new Patient());
  }

  public static DAO<LabRequest> getLabRequestDAO() {
    return labRequestDAO;
  }

  public static DAO<MealDeliveryRequest> getMealDeliveryRequestDAO() {
    return mealDeliveryRequestDAO;
  }

  public static DAO<MedicalEquipmentRequest> getMedicalEquipmentRequestDAO() {
    return medicalEquipmentRequestDAO;
  }

  public static DAO<MedicineRequest> getMedicineRequestDAO() {
    return medicineRequestDAO;
  }

  public static DAO<PatientTransportRequest> getPatientTransportRequestDAO() {
    return patientTransportRequestDAO;
  }

  public static DAO<SanitationRequest> getSanitationRequestDAO() {
    return sanitationRequestDAO;
  }

  public static DAO<Account> getAccountDAO() {
    return accountDAO;
  }

  public static DAO<Employee> getEmployeeDAO() {
    return employeeDAO;
  }

  public static DAO<Location> getLocationDAO() {
    return locationDAO;
  }

  public static DAO<MedicalEquipment> getMedicalEquipmentDAO() {
    return medicalEquipmentDAO;
  }

  public static DAO<Patient> getPatientDAO() {
    return patientDAO;
  }
}
