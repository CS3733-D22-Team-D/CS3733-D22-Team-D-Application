package edu.wpi.DapperDaemons.backend;

import edu.wpi.DapperDaemons.App;
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
  private static DAO<LanguageRequest> languageRequestDAO;

  private DAOPouch() {}

  public static void init() throws IOException {
    labRequestDAO = new DAO<>(new LabRequest());
    App.LOG.info("Successfully constructed Lab Request DAO");

    App.LOG.info("Initializing Meal Delivery Request DAO");
    mealDeliveryRequestDAO = new DAO<>(new MealDeliveryRequest());
    App.LOG.info("Successfully constructed Meal Delivery Request DAO");

    App.LOG.info("Initializing Medical Equipment Request DAO");
    medicalEquipmentRequestDAO = new DAO<>(new MedicalEquipmentRequest());
    App.LOG.info("Successfully constructed Medical Equipment Request DAO");

    App.LOG.info("Initializing Medicine Request DAO");
    medicineRequestDAO = new DAO<>(new MedicineRequest());
    App.LOG.info("Successfully constructed Medicine Request DAO");

    App.LOG.info("Initializing Patient Transport Request DAO");
    patientTransportRequestDAO = new DAO<>(new PatientTransportRequest());
    App.LOG.info("Successfully constructed Patient Transport Request DAO");

    App.LOG.info("Initializing Sanitation Request DAO");
    sanitationRequestDAO = new DAO<>(new SanitationRequest());
    App.LOG.info("Successfully constructed Sanitation Request DAO");

    App.LOG.info("Initializing Account DAO");
    accountDAO = new DAO<>(new Account());
    App.LOG.info("Successfully constructed Account DAO");

    App.LOG.info("Initializing Employee DAO");
    employeeDAO = new DAO<>(new Employee());
    App.LOG.info("Successfully constructed Employee DAO");

    App.LOG.info("Initializing Location DAO");
    locationDAO = new DAO<>(new Location());
    App.LOG.info("Successfully constructed Location DAO");

    App.LOG.info("Initializing Medical Equipment DAO");
    medicalEquipmentDAO = new DAO<>(new MedicalEquipment());
    App.LOG.info("Successfully constructed Medical Equipment DAO");

    App.LOG.info("Initializing Patient DAO");
    patientDAO = new DAO<>(new Patient());
    App.LOG.info("Successfully constructed Patient DAO");

    App.LOG.info("Initializing Node Connections");
    nodeDAO = new DAO<>(new LocationNodeConnections());
    App.LOG.info("Node connections have been produced");

    App.LOG.info("Initializing Languages");
    languageRequestDAO = new DAO<>(new LanguageRequest());
    App.LOG.info("Languages connections has been produced");
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

  public static DAO<LanguageRequest> getLanguageRequestDAO() {
    return languageRequestDAO;
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
