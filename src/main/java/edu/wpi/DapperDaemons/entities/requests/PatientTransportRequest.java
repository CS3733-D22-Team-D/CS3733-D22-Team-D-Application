package edu.wpi.DapperDaemons.entities.requests;

import edu.wpi.DapperDaemons.entities.TableObject;
import edu.wpi.DapperDaemons.tables.TableHandler;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class PatientTransportRequest extends TableObject implements Request {

  /*


  DO NOT CHANGE THIS CLASS EVER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

   */

  // TABLE OBJECT AND REQUEST METHODS
  @Override
  public String tableInit() {
    return "CREATE TABLE PATIENTTRANSPORTREQUESTS(nodeid varchar(80) PRIMARY KEY,"
        + "priority varchar(20),"
        + "roomID varchar(60) ,"
        + "requesterID varchar(60) ,"
        + "assigneeID varchar(60) ,"
            + "status varchar(20),"
            + "notes varchar(255),"
            + "dateTime varchar(20),"
        + "patientID varchar(60) ,"
        + "nextRoomID varchar(60),";
  }

  @Override
  public String tableName() {
    return "PATIENTTRANSPORTREQUESTS";
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
        return status.toString();
      case 7:
        return notes;
      case 8:
        return dateTime;
      case 9:
        return patientID;
      case 10:
        return nextRoomID;

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
        status = RequestStatus.valueOf(newAttribute);
        break;
      case 7:
        notes = newAttribute;
        break;
      case 8:
        dateTime = newAttribute;
        break;
      case 9:
        patientID = newAttribute;
        break;
      case 10:
        nextRoomID = newAttribute;
        break;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public TableObject newInstance(List<String> l) {
    PatientTransportRequest temp = new PatientTransportRequest();
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
      case "status":
        status = RequestStatus.valueOf(newAttribute);
        break;
      case "notes":
        notes = newAttribute;
        break;
      case "dateTime":
        dateTime = newAttribute;
        break;
      case "patientID":
        patientID = newAttribute;
        break;
      case "nextRoomID":
        nextRoomID = newAttribute;
        break;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public String requestType() {
    return "Patient Transport Request";
  }

  @Override
  @TableHandler(table = 0, col = 1)
  public Priority getPriority() {
    return priority;
  }

  @Override
  public boolean requiresTransport() {
    return true;
  }

  // ATTRIBUTES
  private String nodeID;
  private Priority priority;
  private String roomID;
  private String requesterID;
  private String assigneeID;
  private RequestStatus status;
  private String notes;
  private String dateTime;
  private String patientID;
  private String nextRoomID;

  // CONSTRUCTORS

  public PatientTransportRequest(
      Priority priority,
      String roomID,
      String requesterID,
      String assigneeID,
      String notes,
      String patientID,
      String nextRoomID) {
    this.nodeID = priority.toString() + requesterID + LocalDateTime.now().toString();

    this.priority = priority;
    this.roomID = roomID;
    this.requesterID = requesterID;
    this.assigneeID = assigneeID;
    this.notes = notes;
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm - MM/dd");
    Date now = new Date();
    this.dateTime = formatter.format(now);
    this.status = RequestStatus.REQUESTED;
    this.patientID = patientID;
    this.nextRoomID = nextRoomID;
  }

  public PatientTransportRequest() {}

  // SETTERS AND GETTERS
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
  public String getNextRoomID() {
    return nextRoomID;
  }

  public void setNextRoomID(String nextRoomID) {
    this.nextRoomID = nextRoomID;
  }

  @TableHandler(table = 0, col = 7)
  public RequestStatus getStatus() {
    return status;
  }

  public void setStatus(RequestStatus status) {
    this.status = status;
  }


  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getDateTime() {
    return dateTime;
  }

  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }
}
