package edu.wpi.DapperDaemons.entities;

import edu.wpi.DapperDaemons.map.tables.TableHandler;

public class Patient extends TableObject {

  @TableHandler(table = 0, col = 0)
  private String nodeID;

  @TableHandler(table = 1, col = 0)
  @TableHandler(table = 0, col = 1)
  private String firstName;

  @TableHandler(table = 1, col = 1)
  @TableHandler(table = 0, col = 2)
  private String lastName;

  @TableHandler(table = 0, col = 3)
  private int dateOfBirth;

  @TableHandler(table = 0, col = 4)
  private BloodType bloodType;

  @TableHandler(table = 0, col = 5)
  private String locationID;

  public enum BloodType {
    APOS,
    ANEG,
    BPOS,
    BNEG,
    ABPOS,
    ABNEG,
    OPOS,
    ONEG,
    UNKNOWN;
  }

  public Patient() {
    nodeID = "JohnDoe" + (int) ((double) Integer.MAX_VALUE * Math.random());
  }

  public Patient(
      String firstName, String lastName, int dateOfBirth, BloodType bloodType, String locationID) {
    // candidateID

    this.nodeID = firstName + lastName + dateOfBirth;
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.bloodType = bloodType;
    this.locationID = locationID;
  }

  // TableObject Methods
  @Override
  public String getTableInit() {
    return "CREATE TABLE PATIENTS(nodeid varchar(48) PRIMARY KEY,"
        + "firstname varchar(20) DEFAULT 'John',"
        + "lastname varchar(20) DEFAULT 'Doe',"
        + "dateofbirth varchar(8) DEFAULT '04201969',"
        + "bloodtype varchar(20) DEFAULT 'UNKOWN',"
        + "locationID varchar(20) DEFAULT 'unknown')";
  }

  @Override
  public String getTableName() {
    return "PATIENTS";
  }

  @Override
  public String getAttribute(int columnNumber) {
    switch (columnNumber) {
      case 1:
        return nodeID;
      case 2:
        return firstName;
      case 3:
        return lastName;
      case 4:
        return Integer.toString(dateOfBirth);
      case 5:
        return bloodType.toString();
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
        firstName = newAttribute;
        break;
      case 3:
        lastName = newAttribute;
        break;
      case 4:
        dateOfBirth = Integer.parseInt(newAttribute);
        break;
      case 5:
        bloodType = BloodType.valueOf(newAttribute);
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
    return new Patient();
  }
  // getters and setters
  public String getNodeID() {
    return nodeID;
  }

  public void setNodeID(String nodeID) {
    this.nodeID = nodeID;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public int getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(int dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public BloodType getBloodType() {
    return bloodType;
  }

  public void setBloodType(BloodType bloodType) {
    this.bloodType = bloodType;
  }

  public String getLocationID() {
    return locationID;
  }

  public void setLocationID(String locationID) {
    this.locationID = locationID;
  }
}
