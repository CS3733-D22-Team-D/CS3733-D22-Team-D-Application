package edu.wpi.DapperDaemons.entities.requests;

import edu.wpi.DapperDaemons.entities.TableObject;
import edu.wpi.DapperDaemons.tables.TableHandler;
import java.time.LocalDateTime;
import javafx.scene.layout.Priority;

public class PatientTransportRequest extends TableObject implements Request {

  /*


  DO NOT CHANGE THIS CLASS EVER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

   */

  // TABLE OBJECT AND REQUEST METHODS
  @Override
  public String getTableInit() {
    return "CREATE TABLE PATIENTTRANSPORTREQUESTS(nodeid varchar(80) PRIMARY KEY,"
        + "priority varchar(20),"
        + "roomID varchar(60) ,"
        + "requesterID varchar(60) ,"
        + "assigneeID varchar(60) ,"
        + "patientID varchar(60) ,"
        + "nextRoomID varchar(60),"
        + "status varchar(20))";
  }

  @Override
  public String getTableName() {
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
        return patientID;
      case 7:
        return nextRoomID;
      case 8:
        return status.toString();

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
        nextRoomID = newAttribute;
        break;
      case 8:
        status = RequestStatus.valueOf(newAttribute);
        break;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public Object get() {
    return new PatientTransportRequest();
  }

  @Override
  public String getRequestType() {
    return this.getClass().toString();
  }

  @Override
  public Priority getPriority() {
    return priority;
  }

  @Override
  public boolean requiresTransport() {
    return true;
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
  private String nextRoomID;

  @TableHandler(table = 0, col = 7)
  private RequestStatus status;

  // CONSTRUCTORS

  public PatientTransportRequest(
      Priority priority,
      String roomID,
      String requesterID,
      String assigneeID,
      String patientID,
      String nextRoomID,
      RequestStatus status) {
    this.nodeID = priority.toString() + requesterID + LocalDateTime.now().toString();

    this.priority = priority;
    this.roomID = roomID;
    this.requesterID = requesterID;
    this.assigneeID = assigneeID;
    this.patientID = patientID;
    this.nextRoomID = nextRoomID;
    this.status = status;
  }

  public PatientTransportRequest() {}

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

  public String getNextRoomID() {
    return nextRoomID;
  }

  public void setNextRoomID(String nextRoomID) {
    this.nextRoomID = nextRoomID;
  }

  public RequestStatus getStatus() {
    return status;
  }

  public void setStatus(RequestStatus status) {
    this.status = status;
  }
}
