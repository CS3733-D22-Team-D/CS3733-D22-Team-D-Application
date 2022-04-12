package edu.wpi.DapperDaemons.entities;

import edu.wpi.DapperDaemons.map.tables.TableHandler;
import java.lang.reflect.Array;

public class Location extends TableObject {

  @TableHandler(table = 0, col = 0)
  private String nodeID;

  @TableHandler(table = 0, col = 1)
  private int xcoord = -1;

  @TableHandler(table = 0, col = 2)
  private int ycoord = -1;

  @TableHandler(table = 0, col = 3)
  private String floor = "unknown";

  @TableHandler(table = 0, col = 4)
  private String building = "unknown";

  @TableHandler(table = 0, col = 5)
  private String nodeType = "unassigned";

  @TableHandler(table = 0, col = 6)
  private String longName = "room";

  @TableHandler(table = 0, col = 7)
  private String shortName = "r";

  public Location() {}

  public Location(
      String nodeID,
      int xcoord,
      int ycoord,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {
    this.nodeID = nodeID;
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    this.floor = floor;
    this.building = building;
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;
  }
  // TableObject Implementation
  public Location(Array[] attributes) {}

  public int getNumAttributes() {
    return 8;
  }

  public String getTableInit() {

    return "CREATE TABLE LOCATIONS(nodeid varchar(20) PRIMARY KEY,"
        + "xcoord varchar(20) DEFAULT '-1',"
        + "ycoord varchar(20) DEFAULT '-1',"
        + "floor varchar(20) DEFAULT 'unknown',"
        + "building varchar(20) DEFAULT 'unknown',"
        + "nodetype varchar(20) DEFAULT 'unassigned',"
        + "longname varchar(255) DEFAULT 'room',"
        + "shortname varchar(255) DEFAULT 'r')";
  }

  public String getTableName() {
    return "LOCATIONS";
  }

  @Override
  public String getAttribute(int columnNumber) {
    switch (columnNumber) {
      case 1:
        return nodeID;
      case 2:
        return Integer.toString(xcoord);
      case 3:
        return Integer.toString(ycoord);
      case 4:
        return floor;
      case 5:
        return building;
      case 6:
        return nodeType;
      case 7:
        return longName;
      case 8:
        return shortName;
    }
    return null;
  }

  @Override
  public void setAttribute(int columnNumber, String newAttribute) {
    switch (columnNumber) {
      case 1:
        nodeID = newAttribute;
        break;
      case 2:
        xcoord = Integer.parseInt(newAttribute);
        break;
      case 3:
        ycoord = Integer.parseInt(newAttribute);
        break;
      case 4:
        floor = newAttribute;
        break;
      case 5:
        building = newAttribute;
        break;
      case 6:
        nodeType = newAttribute;
        break;
      case 7:
        longName = newAttribute;
        break;
      case 8:
        shortName = newAttribute;
        break;
    }
  }

  // Generic Setters and Getters
  public Location(String nodeID) {
    this.nodeID = nodeID;
  }

  public String getNodeID() {
    return nodeID;
  }

  public int getXcoord() {
    return xcoord;
  }

  public int getYcoord() {
    return ycoord;
  }

  public String getFloor() {
    return floor;
  }

  public String getBuilding() {
    return building;
  }

  public String getNodeType() {
    return nodeType;
  }

  public String getLongName() {
    return longName;
  }

  public String getShortName() {
    return shortName;
  }

  public void setNodeID(String nodeID) {
    this.nodeID = nodeID;
  }

  public void setXcoord(int xcoord) {
    this.xcoord = xcoord;
  }

  public void setYcoord(int ycoord) {
    this.ycoord = ycoord;
  }

  public void setFloor(String floor) {
    this.floor = floor;
  }

  public void setBuilding(String building) {
    this.building = building;
  }

  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  public void setLongName(String longName) {
    this.longName = longName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  @Override
  public Object get() {
    return new Location();
  }

  public String toString() {
    return nodeID
        + ","
        + String.valueOf(xcoord)
        + ","
        + String.valueOf(ycoord)
        + ","
        + floor
        + ","
        + building
        + ","
        + nodeType
        + ","
        + longName
        + ","
        + shortName
        + "\n";
  }

  public boolean equals(Location l) {
    return l.getNodeID().equals(this.nodeID);
  }
}
