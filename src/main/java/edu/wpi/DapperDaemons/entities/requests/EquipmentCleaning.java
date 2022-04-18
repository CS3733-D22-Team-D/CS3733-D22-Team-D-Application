package edu.wpi.DapperDaemons.entities.requests;

import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import edu.wpi.DapperDaemons.entities.TableObject;
import edu.wpi.DapperDaemons.tables.TableHandler;
import java.time.LocalDateTime;
import java.util.List;

public class EquipmentCleaning extends TableObject implements Request {
  // TABLE OBJECT AND REQUEST METHODS
  @Override
  public String tableInit() {
    return "CREATE TABLE EQUIPMENTCLEANINGREQUESTS(nodeid varchar(80) PRIMARY KEY,"
        + "priority varchar(20),"
        + "roomID varchar(60),"
        + "requesterID varchar(60),"
        + "assigneeID varchar(60),"
        + "equipmentID varchar(20),"
        + "equipmentType varchar(20),"
        + "cleanStatus varchar(20),"
        + "cleanBy varchar(10))";
  }

  @Override
  public String tableName() {
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
        equipmentID = newAttribute;
        break;
      case "labType":
        equipmentType = MedicalEquipment.EquipmentType.valueOf(newAttribute);
        break;
      case "status":
        cleanStatus = MedicalEquipment.CleanStatus.valueOf(newAttribute);
        break;
      case "cleabBy":
        cleanBy = newAttribute;
        break;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public String requestType() {
    return "Medical Equipment Request";
  }

  @Override
  public TableObject newInstance(List<String> l) {
    MedicalEquipmentRequest temp = new MedicalEquipmentRequest();
    for (int i = 0; i < l.size(); i++) {
      temp.setAttribute(i + 1, l.get(i));
    }
    return temp;
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
  private String equipmentID;
  private MedicalEquipment.EquipmentType equipmentType;
  private MedicalEquipment.CleanStatus cleanStatus;
  private String cleanBy;

  // CONSTRUCTORS

  public EquipmentCleaning(
      Priority priority,
      String roomID,
      String requesterID,
      String assigneeID,
      String equipmentID,
      MedicalEquipment.EquipmentType equipmentType,
      MedicalEquipment.CleanStatus cleanStatus,
      String cleanBy) {
    this.nodeID = priority.toString() + requesterID + LocalDateTime.now().toString();

    this.priority = priority;
    this.roomID = roomID;
    this.requesterID = requesterID;
    this.assigneeID = assigneeID;
    this.equipmentID = equipmentID;
    this.equipmentType = equipmentType;
    this.cleanStatus = cleanStatus;
    this.cleanBy = cleanBy;
  }

  public EquipmentCleaning() {}

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
  public String getEquipmentID() {
    return equipmentID;
  }

  public void setEquipmentID(String equipmentID) {
    this.equipmentID = equipmentID;
  }

  @TableHandler(table = 0, col = 6)
  public MedicalEquipment.EquipmentType getEquipmentType() {
    return equipmentType;
  }

  public void setEquipmentType(MedicalEquipment.EquipmentType equipmentType) {
    this.equipmentType = equipmentType;
  }

  @TableHandler(table = 0, col = 7)
  public MedicalEquipment.CleanStatus getCleanStatus() {
    return cleanStatus;
  }

  public void setCleanStatus(MedicalEquipment.CleanStatus cleanStatus) {
    this.cleanStatus = cleanStatus;
  }

  @TableHandler(table = 0, col = 8)
  public String getCleanBy() {
    return cleanBy;
  }

  public void setCleanBy(String cleanBy) {
    this.cleanBy = cleanBy;
  }

  @Override
  public String getDateNeeded() {
    return cleanBy;
  }
}