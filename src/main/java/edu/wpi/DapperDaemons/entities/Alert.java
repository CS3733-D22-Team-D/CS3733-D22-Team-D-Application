package edu.wpi.DapperDaemons.entities;

import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.TableHandler;
import java.time.LocalDateTime;

public class Alert extends TableObject {
  private String description;
  private String nodeID;
  private Request.Priority priority;
  private String type;

  public Alert() {}

  public Alert(String description, Request.Priority priority, String type) {
    this.description = description;
    this.nodeID = priority.toString() + description + LocalDateTime.now().toString();
    this.priority = priority;
    this.type = type;
  }

  @Override
  public String getTableInit() {
    return "CREATE TABLE ALERTS(nodeID varchar(100) PRIMARY KEY,"
        + "type varchar(255),"
        + "description varchar(255),"
        + "priority varchar(255))";
  }

  @Override
  public String getTableName() {
    return "ALERTS";
  }

  @Override
  public String getAttribute(int columnNumber) {
    switch (columnNumber) {
      case 1:
        return this.nodeID;
      case 2:
        return this.type;
      case 3:
        return this.description;
      case 4:
        return this.priority.toString();
      default:
        break;
    }
    return null;
  }

  @Override
  public void setAttribute(int columnNumber, String newAttribute) {
    switch (columnNumber) {
      case 1:
        this.nodeID = newAttribute;
      case 2:
        this.type = newAttribute;
      case 3:
        this.description = newAttribute;
      case 4:
        this.priority = Request.Priority.valueOf(newAttribute);
      default:
        break;
    }
  }

  @Override
  public Object get() {
    return new Alert();
  }

  @TableHandler(table = 1, col = 0)
  public String getDescription() {

    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getNodeID() {
    return nodeID;
  }

  public void setNodeID(String nodeID) {
    this.nodeID = nodeID;
  }

  @TableHandler(table = 1, col = 2)
  public Request.Priority getPriority() {
    return priority;
  }

  public void setPriority(Request.Priority priority) {
    this.priority = priority;
  }

  @TableHandler(table = 1, col = 1)
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
