package edu.wpi.DapperDaemons.backend;

import edu.wpi.DapperDaemons.entities.*;
import edu.wpi.DapperDaemons.entities.requests.*;
import java.io.IOException;

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
  private static DAO<LocationNodeConnections> nodeDAO;

  private DAOPouch() {}

  public static void init() throws IOException {
    labRequestDAO = new DAO<>(new LabRequest());
    mealDeliveryRequestDAO = new DAO<>(new MealDeliveryRequest());
    medicalEquipmentRequestDAO = new DAO<>(new MedicalEquipmentRequest());
    medicineRequestDAO = new DAO<>(new MedicineRequest());
    patientTransportRequestDAO = new DAO<>(new PatientTransportRequest());
    sanitationRequestDAO = new DAO<>(new SanitationRequest());
    accountDAO = new DAO<>(new Account());
    employeeDAO = new DAO<>(new Employee());
    locationDAO = new DAO<>(new Location());
    medicalEquipmentDAO = new DAO<>(new MedicalEquipment());
    patientDAO = new DAO<>(new Patient());
    nodeDAO = new DAO<>(new LocationNodeConnections());
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

  public static DAO<LocationNodeConnections> getLocationNodeDAO() {
    return nodeDAO;
  }

  //  public static DAO getDAO(TableObject type) {
  //    String tableName = type.getTableName();
  //    if (tableName.equals("")) {
  //      return null;
  //    } else if (tableName.equals("LABREQUESTS")) {
  //      return labRequestDAO;
  //    } else if (tableName.equals("MEALDELIVERYREQUESTS")) {
  //      return mealDeliveryRequestDAO;
  //    } else if (tableName.equals("MEDICALEQUIPMENTREQUESTS")) {
  //      return clearance >= 3;
  //    } else if (tableName.equals("MEDICINEREQUESTS")) {
  //      return clearance >= 3;
  //    } else if (tableName.equals("PATIENTTRANSPORTREQUESTS")) {
  //      return clearance >= 3;
  //    } else if (tableName.equals("SANITATIONREQUESTS")) {
  //      return clearance >= 3;
  //    } else if (tableName.equals("ACCOUNTS")) {
  //      return clearance >= 5;
  //    } else if (tableName.equals("EMPLOYEES")) {
  //      return clearance >= 5;
  //    } else if (tableName.equals("LOCATIONS")) {
  //      return clearance >= 5;
  //    } else if (tableName.equals("MEDICALEQUIPMENT")) {
  //      return clearance >= 3;
  //    } else if (tableName.equals("PATIENTS")) {
  //      return patientDAO;
  //    }
  //    return null;
  //  }
}
