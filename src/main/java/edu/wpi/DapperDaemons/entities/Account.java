package edu.wpi.DapperDaemons.entities;

import edu.wpi.DapperDaemons.backend.SHA;
import java.security.NoSuchAlgorithmException;

public class Account extends TableObject {
  private String username;
  private String employeeID;
  private String password;
  private String phoneNumber;
  private String settingsFile = "none";

  public Account(String employeeID, String username, String password)
      throws NoSuchAlgorithmException {
    this.username = username;
    this.employeeID = employeeID;
    this.password = SHA.toHexString(SHA.getSHA(password));
  }

  public Account(
      String employeeID, String username, String password, String phoneNumber, String fileName)
      throws NoSuchAlgorithmException {
    this.username = username;
    this.employeeID = employeeID;
    this.password = SHA.toHexString(SHA.getSHA(password));
    this.phoneNumber = phoneNumber;
    this.settingsFile = fileName;
  }

  public Account() {}

  @Override
  public String getTableInit() {
    return "CREATE TABLE ACCOUNTS(username varchar(100) PRIMARY KEY,"
        + "employeeID varchar(20) UNIQUE,"
        + "password varchar(255),"
        + "phoneNumber varchar(12),"
        + "settingsFile varchar(255))";
  }

  @Override
  public String getTableName() {
    return "ACCOUNTS";
  }

  @Override
  public String getAttribute(int columnNumber) {
    switch (columnNumber) {
      case 1:
        return this.username;
      case 2:
        return this.employeeID;
      case 3:
        return this.password;
      case 4:
        return this.phoneNumber;
      case 5:
        return this.settingsFile;
      default:
        break;
    }
    return null;
  }

  @Override
  public void setAttribute(int columnNumber, String newAttribute) {
    switch (columnNumber) {
      case 1:
        this.username = newAttribute;
      case 2:
        this.employeeID = newAttribute;
      case 3:
        this.password = newAttribute;
      case 4:
        this.phoneNumber = newAttribute;
      case 5:
        this.settingsFile = newAttribute;
      default:
        break;
    }
  }

  @Override
  public Object get() {
    return new Account();
  }

  public boolean checkPassword(String password) throws NoSuchAlgorithmException {
    return SHA.toHexString(SHA.getSHA(password)).equals(this.password);
  }
}
