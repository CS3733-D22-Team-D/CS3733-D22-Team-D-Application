package edu.wpi.DapperDaemons.entities;

import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.TableHandler;
import java.time.LocalDateTime;
import java.util.List;

public class Alert extends TableObject {
  private String description;
  private String nodeID;
  private Request.Priority priority;
  private String type;

  public Alert() {}

  public Alert(String description, String type, Request.Priority priority) {
    this.description = description;
    this.nodeID = priority.toString() + description + LocalDateTime.now().toString();
    this.priority = priority;
    this.type = type;
  }

  public String tableInit() {
    return "CREATE TABLE ALERTS(nodeID varchar(100) PRIMARY KEY,"
        + "type varchar(255),"
        + "description varchar(255),"
        + "priority varchar(255))";
  }

  public String tableName() {
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
        throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public void setAttribute(int columnNumber, String newAttribute) {
    switch (columnNumber) {
      case 1:
        this.nodeID = newAttribute;
        break;
      case 2:
        this.type = newAttribute;
        break;
      case 3:
        this.description = newAttribute;
        break;
      case 4:
        this.priority = Request.Priority.valueOf(newAttribute);
        break;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public TableObject newInstance(List<String> l) {
    Alert temp = new Alert();
    for (int i = 0; i < l.size(); i++) {
      temp.setAttribute(i + 1, l.get(i));
    }
    return temp;
  }

  @Override
  public void setAttribute(String attribute, String newAttribute) {
    switch (attribute) {
      case "nodeID":
        nodeID = newAttribute;
        break;
      case "description":
        description = newAttribute;
        break;
      case "type":
        type = newAttribute;
        break;
      case "priority":
        priority = Request.Priority.valueOf(newAttribute);
        break;
      default:
        throw new IndexOutOfBoundsException();
    }
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
