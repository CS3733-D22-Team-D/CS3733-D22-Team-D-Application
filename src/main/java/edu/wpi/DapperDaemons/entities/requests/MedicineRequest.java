package edu.wpi.DapperDaemons.entities.requests;

import edu.wpi.DapperDaemons.entities.TableObject;
import edu.wpi.DapperDaemons.tables.TableHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MedicineRequest extends TableObject implements Request {

  /*


  DO NOT CHANGE THIS CLASS EVER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

   */

  // TABLE OBJECT AND REQUEST METHODS
  @Override
  public String tableInit() {
    return "CREATE TABLE MEDICINEREQUESTS(nodeid varchar(80) PRIMARY KEY,"
        + "priority varchar(20) DEFAULT 'LOW',"
        + "roomID varchar(20) DEFAULT 'Unknown',"
        + "requesterID varchar(60) ,"
        + "assigneeID varchar(60) DEFAULT 'Unselected',"
        + "patientID varchar(60) DEFAULT 'Someone',"
        + "medicationName varchar(60) ,"
        + "quantity varchar(20))";
  }

  @Override
  public String tableName() {
    return "MEDICINEREQUESTS";
  }

  @Override
  public String getAttribute(int columnNumber) {
    switch (columnNumber) {
      case 1:
        return nodeID;
      case 2:
        return priority.toString();
      case 3:
        return roomID;
      case 4:
        return requesterID;
      case 5:
        return assigneeID;
      case 6:
        return patientID;
      case 7:
        return medicationName;
      case 8:
        return Integer.toString(quantity);
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public void setAttribute(int columnNumber, String newAttribute) {
    switch (columnNumber) {
      case 1:
        nodeID = newAttribute;
        break;
      case 2:
        priority = Priority.valueOf(newAttribute);
        break;
      case 3:
        roomID = newAttribute;
        break;
      case 4:
        requesterID = newAttribute;
        break;
      case 5:
        assigneeID = newAttribute;
        break;
      case 6:
        patientID = newAttribute;
        break;
      case 7:
        medicationName = newAttribute;
        break;
      case 8:
        quantity = Integer.parseInt(newAttribute);
        break;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public TableObject newInstance(List<String> l) {
    MedicineRequest temp = new MedicineRequest();
    for (int i = 0; i < l.size(); i++) {
      temp.setAttribute(i + 1, l.get(i));
    }
    return temp;
  }

  @Override
  public void setAttribute(String attribute, String newAttribute) {
    switch (attribute) {
      case "nodeID":
        nodeID = newAttribute;
        break;
      case "priority":
        priority = Priority.valueOf(newAttribute);
        break;
      case "roomID":
        roomID = newAttribute;
        break;
      case "requesterID":
        requesterID = newAttribute;
        break;
      case "assigneeID":
        assigneeID = newAttribute;
        break;
      case "patientID":
        patientID = newAttribute;
        break;
      case "medicationName":
        medicationName = newAttribute;
        break;
      case "quantity":
        quantity = Integer.valueOf(newAttribute);
        break;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public String requestType() {
    return "Medicine Request";
  }

  @Override
  @TableHandler(table = 0, col = 1)
  public Priority getPriority() {
    return priority;
  }

  @Override
  public boolean requiresTransport() {
    return false;
  }

  // ATTRIBUTES
  private String nodeID;
  private Priority priority;
  private String roomID;
  private String requesterID;
  private String assigneeID;
  private String patientID;
  private String medicationName;
  private int quantity;
  private String dateNeeded;

  // CONTSTRUCTORS

  public MedicineRequest(
      Priority priority,
      String roomID,
      String requesterID,
      String assigneeID,
      String patientID,
      String medicationName,
      int quantity) {
    SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy-HH:MM:SS");
    this.nodeID = priority.toString() + requesterID + format.format(new Date());

    this.priority = priority;
    this.roomID = roomID;
    this.requesterID = requesterID;
    this.assigneeID = assigneeID;
    this.patientID = patientID;
    this.medicationName = medicationName;
    this.quantity = quantity;
  }

  public MedicineRequest() {}
  // SETTERS AND GETTERS
  @Override
  @TableHandler(table = 0, col = 0)
  public String getNodeID() {
    return nodeID;
  }

  public void setNodeID(String nodeID) {
    this.nodeID = nodeID;
  }

  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  @TableHandler(table = 0, col = 2)
  public String getRoomID() {
    return roomID;
  }

  public void setRoomID(String roomID) {
    this.roomID = roomID;
  }

  @TableHandler(table = 0, col = 3)
  public String getRequesterID() {
    return requesterID;
  }

  public void setRequesterID(String requesterID) {
    this.requesterID = requesterID;
  }

  @TableHandler(table = 0, col = 4)
  public String getAssigneeID() {
    return assigneeID;
  }

  public void setAssigneeID(String assigneeID) {
    this.assigneeID = assigneeID;
  }

  @TableHandler(table = 0, col = 5)
  public String getPatientID() {
    return patientID;
  }

  public void setPatientID(String patientID) {
    this.patientID = patientID;
  }

  @TableHandler(table = 0, col = 6)
  public String getMedicationName() {
    return medicationName;
  }

  public void setMedicationName(String medicationName) {
    this.medicationName = medicationName;
  }

  @TableHandler(table = 0, col = 7)
  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public String getDateNeeded() {
    return dateNeeded;
  }

  public void setDateNeeded(String dateNeeded) {
    this.dateNeeded = dateNeeded;
  }
}
