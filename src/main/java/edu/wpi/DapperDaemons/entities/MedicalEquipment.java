package edu.wpi.DapperDaemons.entities;

import edu.wpi.DapperDaemons.map.tables.TableHandler;

public class MedicalEquipment extends TableObject {

  @TableHandler(table = 0, col = 0)
  private String nodeID;

  @TableHandler(table = 1, col = 0)
  @TableHandler(table = 0, col = 1)
  private String equipmentName;

  @TableHandler(table = 1, col = 1)
  @TableHandler(table = 0, col = 2)
  private EquipmentType equipmentType;

  @TableHandler(table = 0, col = 3)
  private String serialNumber;

  @TableHandler(table = 1, col = 2)
  @TableHandler(table = 0, col = 4)
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
  public String getNodeID() {
    return nodeID;
  }

  public void setNodeID(String nodeID) {
    this.nodeID = nodeID;
  }

  public String getEquipmentName() {
    return equipmentName;
  }

  public void setEquipmentName(String equipmentName) {
    this.equipmentName = equipmentName;
  }

  public EquipmentType getEquipmentType() {
    return equipmentType;
  }

  public void setEquipmentType(EquipmentType equipmentType) {
    this.equipmentType = equipmentType;
  }

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

  public CleanStatus getCleanStatus() {
    return cleanStatus;
  }

  public void setCleanStatus(CleanStatus cleanStatus) {
    this.cleanStatus = cleanStatus;
  }
}
