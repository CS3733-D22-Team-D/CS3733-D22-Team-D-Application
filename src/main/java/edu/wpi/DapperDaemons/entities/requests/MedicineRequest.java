package edu.wpi.DapperDaemons.entities.requests;

import edu.wpi.DapperDaemons.entities.TableObject;
import edu.wpi.DapperDaemons.map.tables.TableHandler;
import java.time.LocalDateTime;

public class MedicineRequest extends TableObject implements Request {

  /*


  DO NOT CHANGE THIS CLASS EVER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

   */

  // TABLE OBJECT AND REQUEST METHODS
  @Override
  public String getTableInit() {
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
  public String getTableName() {
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
  public Object get() {
    return new MedicineRequest();
  }

  @Override
  public String getRequestType() {
    return "Medicine Request";
  }

  @Override
  public Priority getPriority() {
    return priority;
  }

  @Override
  public boolean requiresTransport() {
    return false;
  }

  // ATTRIBUTES
  @TableHandler(table = 0, col = 0)
  private String nodeID;

  @TableHandler(table = 0, col = 1)
  private Priority priority;

  @TableHandler(table = 0, col = 2)
  private String roomID;

  @TableHandler(table = 0, col = 3)
  private String requesterID;

  @TableHandler(table = 0, col = 4)
  private String assigneeID;

  @TableHandler(table = 0, col = 5)
  private String patientID;

  @TableHandler(table = 0, col = 6)
  private String medicationName;

  @TableHandler(table = 0, col = 7)
  private int quantity;

  // CONTSTRUCTORS

  public MedicineRequest(
      Priority priority,
      String roomID,
      String requesterID,
      String assigneeID,
      String patientID,
      String medicationName,
      int quantity) {
    this.nodeID = priority.toString() + requesterID + LocalDateTime.now().toString();

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

  public String getNodeID() {
    return nodeID;
  }

  public void setNodeID(String nodeID) {
    this.nodeID = nodeID;
  }

  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  public String getRoomID() {
    return roomID;
  }

  public void setRoomID(String roomID) {
    this.roomID = roomID;
  }

  public String getRequesterID() {
    return requesterID;
  }

  public void setRequesterID(String requesterID) {
    this.requesterID = requesterID;
  }

  public String getAssigneeID() {
    return assigneeID;
  }

  public void setAssigneeID(String assigneeID) {
    this.assigneeID = assigneeID;
  }

  public String getPatientID() {
    return patientID;
  }

  public void setPatientID(String patientID) {
    this.patientID = patientID;
  }

  public String getMedicationName() {
    return medicationName;
  }

  public void setMedicationName(String medicationName) {
    this.medicationName = medicationName;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
