package edu.wpi.DapperDaemons.controllers.helpers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.FireBase;
import edu.wpi.DapperDaemons.entities.requests.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OverdueHandler {
  private static DAO<LabRequest> labRequestDAO;
  private static DAO<MealDeliveryRequest> mealDeliveryRequestDAO;
  private static DAO<MedicalEquipmentRequest> medicalEquipmentRequestDAO;
  private static DAO<MedicineRequest> medicineRequestDAO;
  private static DAO<PatientTransportRequest> patientTransportRequestDAO;
  private static DAO<SanitationRequest> sanitationRequestDAO;
  private static DAO<EquipmentCleaning> equipmentCleaningDAO;
  private static OverdueHandler handler;
  private static int dateRepresentation;
  private static Timer overdueTimer;
  private static final int updateTime = 180; // Every 3 minutes check if something is overdue

  public static void init() {
    labRequestDAO = DAOPouch.getLabRequestDAO();
    mealDeliveryRequestDAO = DAOPouch.getMealDeliveryRequestDAO();
    medicalEquipmentRequestDAO = DAOPouch.getMedicalEquipmentRequestDAO();
    medicineRequestDAO = DAOPouch.getMedicineRequestDAO();
    patientTransportRequestDAO = DAOPouch.getPatientTransportRequestDAO();
    sanitationRequestDAO = DAOPouch.getSanitationRequestDAO();
    equipmentCleaningDAO = DAOPouch.getEquipmentCleaningDAO();
    handler = new OverdueHandler();

    updateOverdue();
  }

  public static void updateOverdue() {
    if (overdueTimer != null)
      overdueTimer.cancel();
    overdueTimer = new Timer();
    overdueTimer.schedule(
            new TimerTask() {
              @Override
              public void run() {
                App.LOG.info("Checking for overdue things");
                try {
                  Date dateDat = new Date();
                  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                  dateRepresentation = Integer.parseInt(dateFormat.format(dateDat));
                  handler.checkLabReq();
                  handler.checkEquipmentCleanReq();
                  handler.checkMealReq();
                  handler.checkMedicineReq();
                  handler.checkMedicalEqReq();
                  handler.checkPatientTransportReq();
                  handler.checkSanitaitonReq();
                } catch (Exception e) {

                }
              }
            },
            0,
            updateTime * 1000 // every 180 * 1000ms so 180 seconds
    );
  }

  private List<Request> checkOverdue(List<Request> requestList) {
    List<Request> overdueList = new ArrayList<>();
    for (Request req : requestList) {
      // Convert date into the same format to get a linear reqresentation
      String reqDate = req.getDateNeeded();
      int dateOf = Integer.parseInt(reqDate.substring(4) + reqDate.substring(0, 4));
      if (dateOf < dateRepresentation) // If the due date has passed,
      overdueList.add(req); // Add the req to the list
      App.LOG.info("Request " + req.getNodeID() + " Was overdue, updating priority");
    }
    return overdueList;
  }

  private void checkLabReq() throws SQLException {
    List<Request> requestList = new ArrayList<>();
    for (Request labReq : (List<Request>) labRequestDAO.getAll()) {
      requestList.add(labReq);
    }
    requestList = checkOverdue(requestList);
    for (Request req : requestList) {
      LabRequest overdueReq = labRequestDAO.get(req.getNodeID());
      overdueReq.setPriority(Request.Priority.OVERDUE);
      labRequestDAO.update(overdueReq);
    }
  }

  private void checkMealReq() throws SQLException {
    List<Request> requestList = new ArrayList<>();
    for (Request mealReq : (List<Request>) mealDeliveryRequestDAO.getAll()) {
      requestList.add(mealReq);
    }
    requestList = checkOverdue(requestList);
  }

  private void checkMedicalEqReq() throws SQLException {
    List<Request> requestList = new ArrayList<>();
    for (Request medEqReq : (List<Request>) medicalEquipmentRequestDAO.getAll()) {
      requestList.add(medEqReq);
    }
    requestList = checkOverdue(requestList);
    for (Request req : requestList) {
      MedicalEquipmentRequest overdueReq = medicalEquipmentRequestDAO.get(req.getNodeID());
      overdueReq.setPriority(Request.Priority.OVERDUE);
      medicalEquipmentRequestDAO.update(overdueReq);
    }
  }

  private void checkMedicineReq() throws SQLException {
    List<Request> requestList = new ArrayList<>();
    for (Request medicineReq : (List<Request>) medicineRequestDAO.getAll()) {
      requestList.add(medicineReq);
    }
    requestList = checkOverdue(requestList);
    for (Request req : requestList) {
      MedicineRequest overdueReq = medicineRequestDAO.get(req.getNodeID());
      overdueReq.setPriority(Request.Priority.OVERDUE);
      medicineRequestDAO.update(overdueReq);
    }
  }

  private void checkPatientTransportReq() throws SQLException {
    List<Request> requestList = new ArrayList<>();
    for (Request patTranReq : (List<Request>) patientTransportRequestDAO.getAll()) {
      requestList.add(patTranReq);
    }
    requestList = checkOverdue(requestList);
    for (Request req : requestList) {
      PatientTransportRequest overdueReq = patientTransportRequestDAO.get(req.getNodeID());
      overdueReq.setPriority(Request.Priority.OVERDUE);
      patientTransportRequestDAO.update(overdueReq);
    }
  }

  private void checkSanitaitonReq() throws SQLException {
    List<Request> requestList = new ArrayList<>();
    for (Request sanitationReq : (List<Request>) sanitationRequestDAO.getAll()) {
      requestList.add(sanitationReq);
    }
    requestList = checkOverdue(requestList);
    for (Request req : requestList) {
      SanitationRequest overdueReq = sanitationRequestDAO.get(req.getNodeID());
      overdueReq.setPriority(Request.Priority.OVERDUE);
      sanitationRequestDAO.update(overdueReq);
    }
  }

  private void checkEquipmentCleanReq() throws SQLException {
    List<Request> requestList = new ArrayList<>();
    for (Request sanitationReq : (List<Request>) equipmentCleaningDAO.getAll()) {
      requestList.add(sanitationReq);
    }
    requestList = checkOverdue(requestList);
    for (Request req : requestList) {
      EquipmentCleaning overdueReq = equipmentCleaningDAO.get(req.getNodeID());
      overdueReq.setPriority(Request.Priority.OVERDUE);
      equipmentCleaningDAO.update(overdueReq);
    }
  }
}
