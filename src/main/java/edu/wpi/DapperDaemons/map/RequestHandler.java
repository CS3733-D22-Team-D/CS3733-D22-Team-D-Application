package edu.wpi.DapperDaemons.map;

import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.requests.Request;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RequestHandler {

  public RequestHandler() {}

  public static List<Request> getAllRequests() throws SQLException {
    LinkedList<Request> allReq = new LinkedList<Request>();
    allReq.addAll(new ArrayList(DAOPouch.getLabRequestDAO().getAll().values()));
    allReq.addAll(new ArrayList(DAOPouch.getMealDeliveryRequestDAO().getAll().values()));
    allReq.addAll(new ArrayList(DAOPouch.getMedicalEquipmentRequestDAO().getAll().values()));
    allReq.addAll(new ArrayList(DAOPouch.getPatientTransportRequestDAO().getAll().values()));
    allReq.addAll(new ArrayList(DAOPouch.getSanitationRequestDAO().getAll().values()));
    allReq.addAll(new ArrayList(DAOPouch.getMedicineRequestDAO().getAll().values()));

    return allReq;
  }

  public static List<Request> getFilteredRequests(String locationID) throws SQLException {
    LinkedList<Request> allReq = new LinkedList<>();
    allReq.addAll(new ArrayList(DAOPouch.getLabRequestDAO().filter(3, locationID).values()));
    allReq.addAll(
        new ArrayList(DAOPouch.getMealDeliveryRequestDAO().filter(3, locationID).values()));
    allReq.addAll(
        new ArrayList(DAOPouch.getMedicalEquipmentRequestDAO().filter(3, locationID).values()));
    allReq.addAll(
        new ArrayList(DAOPouch.getPatientTransportRequestDAO().filter(3, locationID).values()));
    allReq.addAll(new ArrayList(DAOPouch.getSanitationRequestDAO().filter(3, locationID).values()));
    allReq.addAll(new ArrayList(DAOPouch.getMedicineRequestDAO().filter(3, locationID).values()));

    return allReq;
  }

  public static List<Request> getSearchedRequestsByLongName(String reqType) throws SQLException {
    List<Request> allReq = getAllRequests();
    LinkedList<Request> searchReq = new LinkedList<>();
    for (int i = 0; i < allReq.size(); i++) {
      if (allReq.get(i).getRequestType().equals(reqType)) {
        searchReq.add(allReq.get(i));
      }
    }
    return searchReq;
  }
}
