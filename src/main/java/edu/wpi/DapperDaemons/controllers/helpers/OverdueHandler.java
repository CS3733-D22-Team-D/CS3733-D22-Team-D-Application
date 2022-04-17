package edu.wpi.DapperDaemons.controllers.helpers;

import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.requests.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OverdueHandler {
    private static DAO<LabRequest> labRequestDAO;
    private static DAO<MealDeliveryRequest> mealDeliveryRequestDAO;
    private static DAO<MedicalEquipmentRequest> medicalEquipmentRequestDAO;
    private static DAO<MedicineRequest> medicineRequestDAO;
    private static DAO<PatientTransportRequest> patientTransportRequestDAO;
    private static DAO<SanitationRequest> sanitationRequestDAO;
    private static OverdueHandler handler;

    public OverdueHandler() { }

    public void init() {
        labRequestDAO = DAOPouch.getLabRequestDAO();
        mealDeliveryRequestDAO = DAOPouch.getMealDeliveryRequestDAO();
        medicalEquipmentRequestDAO = DAOPouch.getMedicalEquipmentRequestDAO();
        medicineRequestDAO = DAOPouch.getMedicineRequestDAO();
        patientTransportRequestDAO = DAOPouch.getPatientTransportRequestDAO();
        sanitationRequestDAO = DAOPouch.getSanitationRequestDAO();
        handler = new OverdueHandler();
    }

    public static void updateOverdue() {
        try {
            handler.checkLabReq();
            handler.checkEquipmentCleanReq();
            handler.checkMealReq();
            handler.checkMedicineReq();
            handler.checkMedicalEqReq();
            handler.checkPatientTransportReq();
            handler.checkSanitaitonReq();
        } catch(Exception e){

        }
    }

    private List<Request> checkOverdue(List<Request> requestList) {
        String currentDate = ""; // Figure out this
        List<Request> overdueList = new ArrayList<>();
        for(Request req : requestList) {
            if(false) // TODO : Add this checked to compare current date to date on request
                overdueList.add(req);
        }
        return overdueList;
    }

    private void checkLabReq() throws SQLException {
        List<Request> requestList = new ArrayList<>();
        for(Request labReq : labRequestDAO.getAll()){
            requestList.add(labReq);
        }
        requestList = checkOverdue(requestList);
    }

    private void checkMealReq() throws SQLException {
        List<Request> requestList = new ArrayList<>();
        for(Request mealReq : mealDeliveryRequestDAO.getAll()){
            requestList.add(mealReq);
        }
        requestList = checkOverdue(requestList);
    }

    private void checkMedicalEqReq() throws SQLException {
        List<Request> requestList = new ArrayList<>();
        for(Request medEqReq : medicalEquipmentRequestDAO.getAll()){
            requestList.add(medEqReq);
        }
        requestList = checkOverdue(requestList);
    }

    private void checkMedicineReq() throws SQLException {
        List<Request> requestList = new ArrayList<>();
        for(Request medicineReq : medicineRequestDAO.getAll()){
            requestList.add(medicineReq);
        }
        requestList = checkOverdue(requestList);
    }

    private void checkPatientTransportReq() throws SQLException {
        List<Request> requestList = new ArrayList<>();
        for(Request patTranReq : patientTransportRequestDAO.getAll()){
            requestList.add(patTranReq);
        }
        requestList = checkOverdue(requestList);
    }

    private void checkSanitaitonReq() throws SQLException {
        List<Request> requestList = new ArrayList<>();
        for(Request sanitationReq : sanitationRequestDAO.getAll()){
            requestList.add(sanitationReq);
        }
        requestList = checkOverdue(requestList);
    }

    private void checkEquipmentCleanReq() throws SQLException {
        // TODO : Add this once the database connection for this gets made
    }
}
