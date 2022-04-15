package edu.wpi.DapperDaemons.entities;

import edu.wpi.DapperDaemons.tables.TableHandler;

public class MedicalEquipment extends TableObject {

  private String nodeID;
  private String equipmentName;
  private EquipmentType equipmentType;
  private String serialNumber;
  private CleanStatus cleanStatus;

  private String locationID;

  public enum CleanStatus {
    UNCLEAN,
    INPROGRESS,
    CLEAN;
  }

  public enum EquipmentType {
    BED,
    RECLINER,
    XRAY,
    INFUSIONPUMP;
  }

  public MedicalEquipment() {}

  public MedicalEquipment(
      String equipmentName, EquipmentType equipmentType, String serialNumber, String locID) {
    this.nodeID = equipmentType.toString() + serialNumber;
    this.locationID = locID;
    this.equipmentName = equipmentName;
    this.equipmentType = equipmentType;
    this.serialNumber = serialNumber;
  }
  // TableObject Methods
  @Override
  public String getTableInit() {
    return "CREATE TABLE MEDICALEQUIPMENT(nodeid varchar(40) PRIMARY KEY,"
        + " equipmentname varchar(20),"
        + "equipmenttype varchar(20),"
        + "serialnumber varchar(20),"
        + "cleanstatus varchar(20),"
        + "locationID varchar(20))";
  }

  @Override
  public String getTableName() {
    return "MEDICALEQUIPMENT";
  }

  @Override
  public String getAttribute(int columnNumber) {
    switch (columnNumber) {
      case 1:
        return nodeID;
      case 2:
        return equipmentName;
      case 3:
        return equipmentType.toString();
      case 4:
        return serialNumber;
      case 5:
        return cleanStatus.toString();
      case 6:
        return locationID;
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
        equipmentName = newAttribute;
        break;
      case 3:
        equipmentType = EquipmentType.valueOf(newAttribute);
        break;
      case 4:
        serialNumber = newAttribute;
        break;
      case 5:
        cleanStatus = CleanStatus.valueOf(newAttribute);
        break;
      case 6:
        locationID = newAttribute;
        break;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public Object get() {
    return new MedicalEquipment();
  }

  // getters and setters
  @TableHandler(table = 0, col = 0)
  public String getNodeID() {
    return nodeID;
  }

  public void setNodeID(String nodeID) {
    this.nodeID = nodeID;
  }

  @TableHandler(table = 2, col = 0)
  @TableHandler(table = 1, col = 0)
  @TableHandler(table = 0, col = 1)
  public String getEquipmentName() {
    return equipmentName;
  }

  public void setEquipmentName(String equipmentName) {
    this.equipmentName = equipmentName;
  }

  @TableHandler(table = 2, col = 1)
  @TableHandler(table = 1, col = 1)
  @TableHandler(table = 0, col = 2)
  public EquipmentType getEquipmentType() {
    return equipmentType;
  }

  public void setEquipmentType(EquipmentType equipmentType) {
    this.equipmentType = equipmentType;
  }

  @TableHandler(table = 2, col = 3)
  @TableHandler(table = 0, col = 3)
  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public void setLocationID(String locationID) {
    this.locationID = locationID;
  }

  public String getLocationID() {
    return locationID;
  }

  @TableHandler(table = 2, col = 2)
  @TableHandler(table = 1, col = 3)
  @TableHandler(table = 0, col = 4)
  public CleanStatus getCleanStatus() {
    return cleanStatus;
  }

  public void setCleanStatus(CleanStatus cleanStatus) {
    this.cleanStatus = cleanStatus;
  }
}
