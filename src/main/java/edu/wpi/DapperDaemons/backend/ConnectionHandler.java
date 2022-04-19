package edu.wpi.DapperDaemons.backend;

import com.google.firebase.database.DatabaseReference;
import edu.wpi.DapperDaemons.entities.*;
import edu.wpi.DapperDaemons.entities.requests.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

public class ConnectionHandler {
  static Connection connection;

  static connectionType type = connectionType.CLOUD;

  public enum connectionType {
    EMBEDDED,
    CLIENTSERVER,
    CLOUD
  }

  private ConnectionHandler() {}

  public static void init() {}

  public static connectionType getType() {
    return type;
  }

  public static Connection getConnection() {
    return connection;
  }

  public static DatabaseReference getCloudConnection() {
    return FireBase.getReference();
  }

  public static boolean switchToCloudServer() {
    // Getting everything currently in the embedded or client database
    HttpsURLConnection testCon = null;
    boolean connected = false;
    try {
      URL url = new URL("https://google.com");
      testCon = (HttpsURLConnection) url.openConnection();
      testCon.connect();
      connected = true;
    } catch (MalformedURLException e) {
      System.out.println("Not connected to the internet");
    } catch (IOException e) {
      System.out.println("Not connected to the internet");
    }

    if (connected) {
      try { // TODO fix
        DAO<LabRequest> labRequestDAO = DAOPouch.getLabRequestDAO();
        DAO<MealDeliveryRequest> mealDeliveryRequestDAO = DAOPouch.getMealDeliveryRequestDAO();
        DAO<MedicalEquipmentRequest> medicalEquipmentRequestDAO =
            DAOPouch.getMedicalEquipmentRequestDAO();
        DAO<MedicineRequest> medicineRequestDAO = DAOPouch.getMedicineRequestDAO();
        DAO<PatientTransportRequest> patientTransportRequestDAO =
            DAOPouch.getPatientTransportRequestDAO();
        DAO<SanitationRequest> sanitationRequestDAO = DAOPouch.getSanitationRequestDAO();
        DAO<Account> accountDAO = DAOPouch.getAccountDAO();
        DAO<Employee> employeeDAO = DAOPouch.getEmployeeDAO();
        DAO<Location> locationDAO = DAOPouch.getLocationDAO();
        DAO<MedicalEquipment> medicalEquipmentDAO = DAOPouch.getMedicalEquipmentDAO();
        DAO<Patient> patientDAO = DAOPouch.getPatientDAO();
        DAO<LocationNodeConnections> locationNodeConnectionsDAO = DAOPouch.getLocationNodeDAO();
        DAO<LanguageRequest> languageRequestDAO = DAOPouch.getLanguageRequestDAO();

        Map<String, LabRequest> labRequestMap = labRequestDAO.getAll();
        Map<String, MealDeliveryRequest> mealDeliveryRequestMap = mealDeliveryRequestDAO.getAll();
        Map<String, MedicalEquipmentRequest> medicalEquipmentRequestMap =
            medicalEquipmentRequestDAO.getAll();
        Map<String, MedicineRequest> medicineRequestMap = medicineRequestDAO.getAll();
        Map<String, PatientTransportRequest> patientTransportRequestMap =
            patientTransportRequestDAO.getAll();
        Map<String, SanitationRequest> sanitationRequestMap = sanitationRequestDAO.getAll();
        Map<String, Account> accountMap = accountDAO.getAll();
        Map<String, Employee> employeeMap = employeeDAO.getAll();
        Map<String, Location> locationMap = locationDAO.getAll();
        Map<String, MedicalEquipment> medicalEquipmentMap = medicalEquipmentDAO.getAll();
        Map<String, Patient> patientMap = patientDAO.getAll();
        Map<String, LocationNodeConnections> locationNodeConnectionsMap =
            locationNodeConnectionsDAO.getAll();
        Map<String, LanguageRequest> languageRequestMap = languageRequestDAO.getAll();

        for (LabRequest lr : labRequestMap.values()) {
          labRequestDAO.add(lr);
        }
        for (MealDeliveryRequest lr : mealDeliveryRequestMap.values()) {
          mealDeliveryRequestDAO.add(lr);
        }
        for (MedicalEquipmentRequest lr : medicalEquipmentRequestMap.values()) {
          medicalEquipmentRequestDAO.add(lr);
        }
        for (MedicineRequest lr : medicineRequestMap.values()) {
          medicineRequestDAO.add(lr);
        }
        for (PatientTransportRequest lr : patientTransportRequestMap.values()) {
          patientTransportRequestDAO.add(lr);
        }
        for (SanitationRequest lr : sanitationRequestMap.values()) {
          sanitationRequestDAO.add(lr);
        }
        for (Account lr : accountMap.values()) {
          accountDAO.add(lr);
        }
        for (Employee lr : employeeMap.values()) {
          employeeDAO.add(lr);
        }
        for (Location lr : locationMap.values()) {
          locationDAO.add(lr);
        }
        for (MedicalEquipment lr : medicalEquipmentMap.values()) {
          medicalEquipmentDAO.add(lr);
        }
        for (Patient lr : patientMap.values()) {
          patientDAO.add(lr);
        }
        for (LocationNodeConnections lr : locationNodeConnectionsMap.values()) {
          locationNodeConnectionsDAO.add(lr);
        }
        for (LanguageRequest lr : languageRequestMap.values()) {
          languageRequestDAO.add(lr);
        }
        new FireBaseLoader(labRequestDAO, new LabRequest());
        new FireBaseLoader(mealDeliveryRequestDAO, new MealDeliveryRequest());
        new FireBaseLoader(medicalEquipmentRequestDAO, new MedicalEquipmentRequest());
        new FireBaseLoader(medicineRequestDAO, new MedicineRequest());
        new FireBaseLoader(patientTransportRequestDAO, new PatientTransportRequest());
        new FireBaseLoader(sanitationRequestDAO, new SanitationRequest());
        new FireBaseLoader(accountDAO, new Account());
        new FireBaseLoader(employeeDAO, new Employee());
        new FireBaseLoader(locationDAO, new Location());
        new FireBaseLoader(medicalEquipmentDAO, new MedicalEquipment());
        new FireBaseLoader(patientDAO, new Patient());
        new FireBaseLoader(locationNodeConnectionsDAO, new LocationNodeConnections());
        new FireBaseLoader(languageRequestDAO, new LanguageRequest());
      } catch (Exception e) {
        return false;
      }
      connection = null;
      type = connectionType.CLOUD;
      try {
        DAOPouch.init();
      } catch (IOException e) {
        System.out.println("DAOPouch could not initialize");
      }
    } else {
      return false;
    }
    return true;
  }

  public static boolean switchToClientServer() {
    try {
      if (!type.equals(connectionType.CLOUD) && connection != null) CSVSaver.saveAll();
      Class.forName("org.apache.derby.jdbc.ClientDriver");
      System.out.println("Connecting to client");
      connection =
          DriverManager.getConnection("jdbc:derby://localhost:1527/BaW_Database;create=true");
      System.out.println("Connected to the client server");
      CSVLoader.loadAll();
      type = connectionType.CLIENTSERVER;
      try {
        DAOPouch.init();
      } catch (Exception e) {
        System.out.println("DAOPouch could not initialize");
      }
    } catch (SQLException e) {
      System.out.println("Could not connect to the client server");
      //      type = connectionType.EMBEDDED;
      return false;
    } catch (ClassNotFoundException e) {
      System.out.println("Driver error, try making sure you don't have any other instances open!");
      //      type = connectionType.EMBEDDED;
      return false;
    }
    return true;
  }

  public static boolean switchToEmbedded() {
    try {
      if (!type.equals(connectionType.CLOUD) && connection != null) CSVSaver.saveAll();
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      System.out.println("Connecting to embedded");
      connection = DriverManager.getConnection("jdbc:derby:BaW_database;create = true");
      CSVLoader.loadAll();
      System.out.println("Connected to the embedded server");
      type = connectionType.EMBEDDED;
      try {
        DAOPouch.init();
      } catch (Exception e) {
        System.out.println("DAOPouch could not initialize");
      }
    } catch (SQLException e) {
      System.out.println("Could not connect to the embedded server");
      return false;
    } catch (ClassNotFoundException e) {
      //      System.out.println("Driver error, try making sure you don't have any other instances
      // open!");
      return false;
    }
    return true;
  }
}
