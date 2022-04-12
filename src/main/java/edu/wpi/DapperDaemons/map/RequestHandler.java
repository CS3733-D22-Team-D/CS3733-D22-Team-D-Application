package edu.wpi.DapperDaemons.map;

import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.requests.Request;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RequestHandler {

  public RequestHandler() {}

  public static List<Request> getAllRequests() throws SQLException {
    LinkedList<Request> allReq = new LinkedList<Request>();
    allReq.addAll(DAOPouch.getLabRequestDAO().getAll());
    allReq.addAll(DAOPouch.getMealDeliveryRequestDAO().getAll());
    allReq.addAll(DAOPouch.getMedicalEquipmentRequestDAO().getAll());
    allReq.addAll(DAOPouch.getPatientTransportRequestDAO().getAll());
    allReq.addAll(DAOPouch.getSanitationRequestDAO().getAll());
    allReq.addAll(DAOPouch.getMedicineRequestDAO().getAll());

    return allReq;
  }

  public static List<Request> getFilteredRequests(String locationID) throws SQLException {
    LinkedList<Request> allReq = new LinkedList<>();
    allReq.addAll(DAOPouch.getLabRequestDAO().filter(3, locationID));
    allReq.addAll(DAOPouch.getMealDeliveryRequestDAO().filter(3, locationID));
    allReq.addAll(DAOPouch.getMedicalEquipmentRequestDAO().filter(3, locationID));
    allReq.addAll(DAOPouch.getPatientTransportRequestDAO().filter(3, locationID));
    allReq.addAll(DAOPouch.getSanitationRequestDAO().filter(3, locationID));
    allReq.addAll(DAOPouch.getMedicineRequestDAO().filter(3, locationID));

    return allReq;
  }

  public List<Request> getSearchedRequestsByLongName(String reqType) throws SQLException {
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