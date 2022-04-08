package edu.wpi.DapperDaemons.map;

import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.requests.Request;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RequestHandler {

  public RequestHandler() {}

  public List<Request> getAllRequests() throws SQLException {
    LinkedList<Request> allReq = new LinkedList<Request>();
    allReq.addAll(DAOPouch.getLabRequestDAO().getAll());
    allReq.addAll(DAOPouch.getMealDeliveryRequestDAO().getAll());
    allReq.addAll(DAOPouch.getMedicalEquipmentRequestDAO().getAll());
    allReq.addAll(DAOPouch.getPatientTransportRequestDAO().getAll());
    allReq.addAll(DAOPouch.getSanitationRequestDAO().getAll());
    allReq.addAll(DAOPouch.getMedicineRequestDAO().getAll());

    return allReq;
  }
}
