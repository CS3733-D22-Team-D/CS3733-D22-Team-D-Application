package edu.wpi.DapperDaemons.entities.requests;

import edu.wpi.DapperDaemons.entities.TableObject;
import edu.wpi.DapperDaemons.tables.TableHandler;
import java.time.LocalDateTime;
import java.util.List;

public class MealDeliveryRequest extends TableObject implements Request {

  /*


  DO NOT CHANGE THIS CLASS EVER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

   */

  // TABLE OBJECT AND REQUEST METHODS
  @Override
  public String tableInit() {
    return "CREATE TABLE MEALDELIVERYREQUESTS(nodeid varchar(80) PRIMARY KEY,"
        + "priority varchar(20),"
        + "roomID varchar(60),"
        + "requesterID varchar(60),"
        + "assigneeID varchar(60),"
        + "patientID varchar(60),"
        + "entree varchar(20),"
        + "side varchar(20),"
        + "drink varchar(20),"
        + "dessert varchar(20))";
  }

  @Override
  public String tableName() {
    return "MEALDELIVERYREQUESTS";
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
        return patientID;
      case 7:
        return entree;
      case 8:
        return side;
      case 9:
        return drink;
      case 10:
        return dessert;
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
        patientID = newAttribute;
        break;
      case 7:
        entree = newAttribute;
        break;
      case 8:
        side = newAttribute;
        break;
      case 9:
        drink = newAttribute;
        break;
      case 10:
        dessert = newAttribute;
        break;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public String requestType() {
    return "Meal Delivery Request";
  }

  @Override
  @TableHandler(table = 0, col = 1)
  public Priority getPriority() {
    return priority;
  }

  @Override
  public TableObject newInstance(List<String> l) {
    MealDeliveryRequest temp = new MealDeliveryRequest();
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
        patientID = newAttribute;
        break;
      case "entree":
        entree = newAttribute;
        break;
      case "side":
        side = newAttribute;
        break;
      case "drink":
        drink = newAttribute;
      case "dessert":
        dessert = newAttribute;
      default:
        throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public boolean requiresTransport() {
    return false;
  }

  // ATTRIBUTES

  private String nodeID;
  private Priority priority;
  private String roomID;
  private String requesterID;
  private String assigneeID;
  private String patientID;
  private String entree;
  private String side;
  private String drink;
  private String dessert;
  private String dateNeeded;

  // CONSTRUCTORS

  public MealDeliveryRequest(
      Priority priority,
      String roomID,
      String requesterID,
      String assigneeID,
      String patientID,
      String entree,
      String side,
      String drink,
      String dessert) {
    this.nodeID = priority.toString() + requesterID + LocalDateTime.now().toString();

    this.priority = priority;
    this.roomID = roomID;
    this.requesterID = requesterID;
    this.assigneeID = assigneeID;
    this.patientID = patientID;
    this.entree = entree;
    this.side = side;
    this.drink = drink;
    this.dessert = dessert;
  }

  public MealDeliveryRequest() {}

  // SETTERS AND GETTERS

  @Override
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
  public String getPatientID() {
    return patientID;
  }

  public void setPatientID(String patientID) {
    this.patientID = patientID;
  }

  @TableHandler(table = 0, col = 6)
  public String getEntree() {
    return entree;
  }

  public void setEntree(String entree) {
    this.entree = entree;
  }

  @TableHandler(table = 0, col = 7)
  public String getSide() {
    return side;
  }

  public void setSide(String side) {
    this.side = side;
  }

  @TableHandler(table = 0, col = 8)
  public String getDrink() {
    return drink;
  }

  public void setDrink(String drink) {
    this.drink = drink;
  }

  @TableHandler(table = 0, col = 9)
  public String getDessert() {
    return dessert;
  }

  public void setDessert(String dessert) {
    this.dessert = dessert;
  }

  @Override
  @TableHandler(table = 0, col = 10)
  public String getDateNeeded() {
    return dateNeeded;
  }

  public void setDateNeeded(String dateNeeded) {
    this.dateNeeded = dateNeeded;
  }
}
