package edu.wpi.DapperDaemons.entities;

import java.time.LocalDateTime;
import java.util.List;

public class Notification extends TableObject {

  private String nodeID;
  private String userID;
  private String subject;
  private String body;
  private boolean read = false;

  public Notification() {}

  public Notification(String subject, String body, String userID) {
    this.subject = subject;
    this.body = body;
    this.userID = userID;
    this.nodeID = userID + subject + LocalDateTime.now();
  }

  @Override
  public String tableInit() {
    return "CREATE TABLE NOTIFICATIONS(nodeID varchar(255) PRIMARY KEY,"
        + "userID varchar(100),"
        + "subject varchar(100),"
        + "body varchar(255))";
  }

  @Override
  public String tableName() {
    return "NOTIFICATIONS";
  }

  @Override
  public String getAttribute(int columnNumber) {
    switch (columnNumber) {
      case 1:
        return nodeID;
      case 2:
        return userID;
      case 3:
        return subject;
      case 4:
        return body;
      case 5:
        return String.valueOf(read);
      default:
        throw new ArrayIndexOutOfBoundsException();
    }
  }

  @Override
  public void setAttribute(int columnNumber, String newAttribute) {
    switch (columnNumber) {
      case 1:
        this.nodeID = newAttribute;
        break;
      case 2:
        this.userID = newAttribute;
        break;
      case 3:
        this.subject = newAttribute;
        break;
      case 4:
        this.body = newAttribute;
        break;
      case 5:
        this.read = Boolean.parseBoolean(newAttribute);
        break;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public TableObject newInstance(List<String> l) {
    return new Notification("Subject", "Body", "Default");
  }

  @Override
  public void setAttribute(String attribute, String newAttribute) {
    switch (attribute) {
      case "nodeID":
        this.nodeID = newAttribute;
        break;
      case "user":
        this.userID = newAttribute;
        break;
      case "subject":
        this.subject = newAttribute;
        break;
      case "body":
        this.body = newAttribute;
        break;
      case "read":
        this.read = Boolean.parseBoolean(newAttribute);
        break;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  public boolean getRead() {
    return read;
  }

  public String getSubject() {
    return subject;
  }

  public String getBody() {
    return body;
  }
}
