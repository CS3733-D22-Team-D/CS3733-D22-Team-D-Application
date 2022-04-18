package edu.wpi.DapperDaemons.entities.requests;

import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.entities.TableObject;
import edu.wpi.DapperDaemons.tables.TableHandler;
import java.time.LocalDateTime;
import java.util.List;

public class SecurityRequest extends TableObject {

    @TableHandler(table = 0, col = 0)
    public String getNodeID() {
        return nodeID;
    }

    @TableHandler(table = 0, col = 1)
    public Request.Priority getPriority() {
        return priority;
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

    public void setPriority(Request.Priority priority) {
        this.priority = priority;
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

    private String nodeID;
    private Request.Priority priority;
    private String roomID;
    private String requester;
    private String assignee;


    public SecurityRequest() {}

    public SecurityRequest(Request.Priority priority, String roomID) {
        this.priority = priority;
        this.roomID = roomID;
        this.assignee = "none";
        this.nodeID = String.valueOf(priority) + roomID + LocalDateTime.now();
        this.requester = SecurityController.getUser().getAttribute(1);
    }

    @Override
    public String tableInit() {
        return "CREATE TABLE SECURITYREQUESTS(nodeID varchar(20) PRIMARY KEY,"
                + "priority varchar(20),"
                + "roomID varchar(100),"
                + "requester varchar(100),"
                + "assignee varchar(100))";
    }

    @Override
    public String tableName() {
        return "SECURITYREQUESTS";
    }

    @Override
    public String getAttribute(int columnNumber) throws ArrayIndexOutOfBoundsException {
        switch (columnNumber) {
            case 1:
                return nodeID;
            case 2:
                return String.valueOf(priority);
            case 3:
                return roomID;
            case 4:
                return requester;
            case 5:
                return assignee;
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
                priority = Request.Priority.valueOf(newAttribute);
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
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public TableObject newInstance(List<String> l) {
        SecurityRequest temp = new SecurityRequest();
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
                priority = Request.Priority.valueOf(newAttribute);
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
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    }
}
