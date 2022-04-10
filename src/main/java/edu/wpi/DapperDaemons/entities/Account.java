package edu.wpi.DapperDaemons.entities;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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
    this.password = toHexString(getSHA(password));
  }

  public Account(String employeeID, String username, String password, String phoneNumber, String fileName)
      throws NoSuchAlgorithmException {
    this.username = username;
    this.employeeID = employeeID;
    this.password = toHexString(getSHA(password));
    this.phoneNumber = phoneNumber;
    this.settingsFile = fileName;
  }

  public Account() {}

  @Override
  public String getTableInit() {
    return "CREATE TABLE ACCOUNTS(username varchar(100) PRIMARY KEY," +
            "employeeID varchar(20) UNIQUE," +
            "password varchar(255))," +
            "phoneNumber varchar(12)," +
            "settingsFile varchar(255)";
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
        this.employeeID = newAttribute;
      case 2:
        this.username = newAttribute;
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

  public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
    // Static getInstance method is called with hashing SHA
    MessageDigest md = MessageDigest.getInstance("SHA-256");

    // digest() method called
    // to calculate message digest of an input
    // and return array of byte
    return md.digest(input.getBytes(StandardCharsets.UTF_8));
  }

  public static String toHexString(byte[] hash) {
    // Convert byte array into signum representation
    BigInteger number = new BigInteger(1, hash);

    // Convert message digest into hex value
    StringBuilder hexString = new StringBuilder(number.toString(16));

    // Pad with leading zeros
    while (hexString.length() < 32) {
      hexString.insert(0, '0');
    }

    return hexString.toString();
  }

  public boolean checkPassword(String password) throws NoSuchAlgorithmException {
    return toHexString(getSHA(password)).equals(this.password);
  }
}
