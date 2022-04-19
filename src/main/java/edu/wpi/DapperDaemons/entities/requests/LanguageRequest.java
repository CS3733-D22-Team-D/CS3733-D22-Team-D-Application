package edu.wpi.DapperDaemons.entities.requests;

import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.entities.TableObject;
import edu.wpi.DapperDaemons.tables.TableHandler;
import java.time.LocalDateTime;
import java.util.List;

public class LanguageRequest extends TableObject implements Request  {

  @Override
  @TableHandler(table = 0, col = 0)
  public String getNodeID() {
    return nodeID;
  }

  @Override
  public String getRequesterID() {
    return requester;
  }

  @Override
  public String getAssigneeID() {
    return assignee;
  }

  @Override
  public RequestStatus getStatus() {
    return getStatus();
  }

  @TableHandler(table = 0, col = 1)
  public Language getLanguage() {
    return language;
  }

  @Override
  public String requestType() {
    return "Language Request";
  }

  @Override
  public Request.Priority getPriority() {
    return priority;
  }

  @Override
  public boolean requiresTransport() {
    return false;
  }

  @TableHandler(table = 0, col = 2)
  public String getRoomID() {
    return roomID;
  }

  @TableHandler(table = 0, col = 3)
  public String getRequester() {
    return requester;
  }

  @TableHandler(table = 0, col = 4)
  public String getAssignee() {
    return assignee;
  }

  public void setNodeID(String nodeID) {
    this.nodeID = nodeID;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

  public void setRoomID(String roomID) {
    this.roomID = roomID;
  }

  public void setRequester(String requester) {
    this.requester = requester;
  }

  public void setAssignee(String assignee) {
    this.assignee = assignee;
  }

  public void setDateNeeded(String dateNeeded) {
    this.dateNeeded = dateNeeded;
  }

  public void setPriority(Request.Priority priority) {
    this.priority = priority;
  }

  @Override
  @TableHandler(table = 0, col = 5)
  public String getDateNeeded() {
    return dateNeeded;
  }

  private String nodeID;
  private Request.Priority priority = Request.Priority.LOW;
  private String roomID;
  private String requester;
  private String assignee;
  private Language language;
  private String dateNeeded;

  public enum Language {
    CHINESE,
    SPANISH,
    ENGLISH,
    HINDI,
    BENGALI,
    PORTUGUESE,
    RUSSIAN,
    JAPANESE,
    TURKISH,
    KOREAN,
    FRENCH,
    ITALIAN,
    ARABIC,
    GERMAN,
    VIETNAMESE,
    POLISH,
    THAI,
    DUTCH,
    GREEK,
    SWEDISH,
    NORWEGIAN,
    FINNISH
  }

  public LanguageRequest() {}

  public LanguageRequest(Language language, String roomID, String dateNeeded) {
    this.language = language;
    this.roomID = roomID;
    this.assignee = "none";
    this.nodeID = String.valueOf(language) + roomID + LocalDateTime.now();
    this.requester = SecurityController.getUser().getAttribute(1);
    this.dateNeeded = dateNeeded;
  }

  @Override
  public String tableInit() {
    return "CREATE TABLE LANGUAGEREQUESTS(nodeID varchar(20) PRIMARY KEY,"
        + "language varchar(20),"
        + "roomID varchar(100),"
        + "requester varchar(100),"
        + "assignee varchar(100),"
        + "dateNeed varchar(10))";
  }

  @Override
  public String tableName() {
    return "LANGUAGEREQUESTS";
  }

  @Override
  public String getAttribute(int columnNumber) throws ArrayIndexOutOfBoundsException {
    switch (columnNumber) {
      case 1:
        return nodeID;
      case 2:
        return priority.toString();
      case 3:
        return roomID;
      case 4:
        return requester;
      case 5:
        return assignee;
      case 6:
        return language.toString();
      case 7:
        return dateNeeded;
      default:
        throw new ArrayIndexOutOfBoundsException();
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
        requester = newAttribute;
        break;
      case 5:
        assignee = newAttribute;
        break;
      case 6:
        language = Language.valueOf(newAttribute);
        break;
      case 7:
        dateNeeded = newAttribute;
        break;
      default:
        throw new ArrayIndexOutOfBoundsException();
    }
  }

  @Override
  public TableObject newInstance(List<String> l) {
    LanguageRequest temp = new LanguageRequest();
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
      case "language":
        language = Language.valueOf(newAttribute);
        break;
      case "roomID":
        roomID = newAttribute;
        break;
      case "requester":
        requester = newAttribute;
        break;
      case "assignee":
        assignee = newAttribute;
        break;
      case "dateNeeded":
        dateNeeded = newAttribute;
        break;
      case "priority":
        priority = Priority.valueOf(newAttribute);
        break;
      default:
        throw new ArrayIndexOutOfBoundsException();
    }
  }
}
