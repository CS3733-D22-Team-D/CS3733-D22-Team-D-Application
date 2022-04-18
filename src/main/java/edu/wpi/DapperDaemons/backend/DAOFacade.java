package edu.wpi.DapperDaemons.backend;

import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import edu.wpi.DapperDaemons.entities.requests.MedicalEquipmentRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DAOFacade {

  private DAOFacade() {}

  /** Gets a list of all long names of locations */
  public static List<String> getAllLocationLongNames() {
    List<String> all = new ArrayList<>();
    DAOPouch.getLocationDAO().getAll().values().forEach(e -> all.add(e.getLongName()));
    return all;
  }

  /** Gets the username of the current user */
  public static String getUsername() {
    String username =
        DAOPouch.getAccountDAO().filter(2, SecurityController.getUser().getNodeID()).values()
            .stream()
            .findFirst()
            .get()
            .getAttribute(1);
    if (username == null) return "";
    else return username;
  }

  /** Gets all Requests (every request DAO) */
  public static List<Request> getAllRequests() {
    LinkedList<Request> allReq = new LinkedList<>();
    allReq.addAll(new ArrayList<>(DAOPouch.getLabRequestDAO().getAll().values()));
    allReq.addAll(new ArrayList<>(DAOPouch.getMealDeliveryRequestDAO().getAll().values()));
    allReq.addAll(new ArrayList<>(DAOPouch.getMedicalEquipmentRequestDAO().getAll().values()));
    allReq.addAll(new ArrayList<>(DAOPouch.getPatientTransportRequestDAO().getAll().values()));
    allReq.addAll(new ArrayList<>(DAOPouch.getSanitationRequestDAO().getAll().values()));
    allReq.addAll(new ArrayList<>(DAOPouch.getMedicineRequestDAO().getAll().values()));
    return allReq;
  }

  /**
   * Gets every request by locationID
   *
   * @param locationID - the locationID of the requests
   */
  public static List<Request> getFilteredRequests(String locationID) {
    LinkedList<Request> allReq = new LinkedList<>();
    allReq.addAll(new ArrayList<>(DAOPouch.getLabRequestDAO().filter(3, locationID).values()));
    allReq.addAll(
        new ArrayList<>(DAOPouch.getMealDeliveryRequestDAO().filter(3, locationID).values()));
    allReq.addAll(
        new ArrayList<>(DAOPouch.getMedicalEquipmentRequestDAO().filter(3, locationID).values()));
    allReq.addAll(
        new ArrayList<>(DAOPouch.getPatientTransportRequestDAO().filter(3, locationID).values()));
    allReq.addAll(
        new ArrayList<>(DAOPouch.getSanitationRequestDAO().filter(3, locationID).values()));
    allReq.addAll(new ArrayList<>(DAOPouch.getMedicineRequestDAO().filter(3, locationID).values()));

    return allReq;
  }

  /**
   * Searches for requests based on their exact name
   *
   * @param reqType - The name of the request
   */
  public static List<Request> searchRequestsByName(String reqType) {
    List<Request> allReq = getAllRequests();
    LinkedList<Request> searchReq = new LinkedList<>();
    for (Request request : allReq) {
      if (request.requestType().equals(reqType)) {
        searchReq.add(request);
      }
    }
    return searchReq;
  }

  /**
   * Filters Medical Equipment in a certain LOCATION by a given TYPE and a given CLEANSTATUS
   *
   * @param loc location to check for equipment
   * @param medicalEquipmentDAO database DAO
   * @param type type of equipment to look for
   * @param cleanStatus the desired clean status
   * @return A Map containing the filtered Equipment
   */
  public static Map<String, MedicalEquipment> filterEquipByTypeAndStatus(
      Location loc, DAO<MedicalEquipment> medicalEquipmentDAO, String type, String cleanStatus) {
    Map<String, MedicalEquipment> tempMap =
        medicalEquipmentDAO.filter(
            medicalEquipmentDAO.filter(6, loc.getAttribute(1)), 5, cleanStatus);
    tempMap = medicalEquipmentDAO.filter(tempMap, 3, type);
    return tempMap;
  }

  /**
   * determines if an AUTOMATIC equip req has already been submitted and added to the DAO
   *
   * @param requestToCheck the request to check
   * @return true if it is already in the DAO
   */
  public static boolean automaticRequestAlreadyExists(MedicalEquipmentRequest requestToCheck) {
    try {
      DAOPouch.init();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Map<String, MedicalEquipmentRequest> requestMap =
        DAOPouch.getMedicalEquipmentRequestDAO().getAll();

    for (MedicalEquipmentRequest request : requestMap.values()) {
      // This should be enough to determine that the automatic request has already been submitted
      if (request.getEquipmentID().equals(requestToCheck.getEquipmentID())
          && request.getPriority().equals(requestToCheck.getPriority())) return true;
    }
    return false;
  }
}