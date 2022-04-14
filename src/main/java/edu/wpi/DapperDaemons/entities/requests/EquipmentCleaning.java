package edu.wpi.DapperDaemons.entities.requests;

import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import edu.wpi.DapperDaemons.entities.TableObject;
import edu.wpi.DapperDaemons.map.tables.TableHandler;

public class EquipmentCleaning extends TableObject implements Request {

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
  private String equipmentID;

  @TableHandler(table = 0, col = 6)
  private MedicalEquipment.EquipmentType equipmentType;

  @TableHandler(table = 0, col = 7)
  private MedicalEquipment.CleanStatus cleanStatus;

  @TableHandler(table = 0, col = 8)
  private String cleanBy; // The time in which the equipment is needed to be cleaned by

  @Override
  public String getTableInit() {
    return "CREATE TABLE EQUIPMENTCLEANINGREQUESTS(nodeid varchar(80) PRIMARY KEY,"
        + "priority varchar(20),"
        + "roomID varchar(60),"
        + "requesterID varchar(60),"
        + "assigneeID varchar(60),"
        + "equipmentID varchar(20),"
        + "equipmentType varchar(20),"
        + "cleanStatus varchar(20),"
        + "timerequested varchar(20),"
        + "timeneeded varchar(20))";
  }

  @Override
  public String getTableName() {
    return "EQUIPMENTCLEANINGREQUESTS";
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
        return equipmentID;
      case 7:
        return equipmentType.toString();
      case 8:
        return cleanStatus.toString();
      case 9:
        return cleanBy;
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
        equipmentID = newAttribute;
        break;
      case 7:
        equipmentType = MedicalEquipment.EquipmentType.valueOf(newAttribute);
        break;
      case 8:
        cleanStatus = MedicalEquipment.CleanStatus.valueOf(newAttribute);
        break;
      case 9:
        cleanBy = newAttribute;
        break;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public Object get() {
    return new EquipmentCleaning();
  }

  @Override
  public String getRequestType() {
    return "Medical Equipment Cleaning Request";
  }

  @Override
  public Priority getPriority() {
    return priority;
  }

  @Override
  public boolean requiresTransport() {
    return false;
  }

  @Override
  public String getRoomID() {
    return roomID;
  }

  public String getNodeID() {
    return nodeID;
  }

  public void setNodeID(String nodeID) {
    this.nodeID = nodeID;
  }

  public void setPriority(Priority priority) {
    this.priority = priority;
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

  public String getEquipmentID() {
    return equipmentID;
  }

  public void setEquipmentID(String equipmentID) {
    this.equipmentID = equipmentID;
  }

  public MedicalEquipment.EquipmentType getEquipmentType() {
    return equipmentType;
  }

  public void setEquipmentType(MedicalEquipment.EquipmentType equipmentType) {
    this.equipmentType = equipmentType;
  }

  public MedicalEquipment.CleanStatus getCleanStatus() {
    return cleanStatus;
  }

  public void setCleanStatus(MedicalEquipment.CleanStatus cleanStatus) {
    this.cleanStatus = cleanStatus;
  }

  public String getCleanBy() {
    return cleanBy;
  }

  public void setCleanBy(String cleanBy) {
    this.cleanBy = cleanBy;
  }
}
