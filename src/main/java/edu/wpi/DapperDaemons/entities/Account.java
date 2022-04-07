package edu.wpi.DapperDaemons.entities;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

public class Account extends TableObject{
    private String employeeID;
    private String username;
    private String password;
    private String settingsFile;

    public Account(String employeeID, String username, String password){
        this.employeeID = employeeID;
        this.username = username;
        this.password = password;
    }

    public Account(String employeeID, String username, String password, String fileName){
        this.employeeID = employeeID;
        this.username = username;
        this.password = password;
        this.settingsFile = fileName;
    }

    private Account(){}

    @Override
    public String getTableInit() {
        return "CREATE TABLE ACCOUNTS(employeeID varchar(20) PRIMARY KEY," +
                "username varchar(100) UNIQUE," +
                "password varchar(255))";
    }

    @Override
    public String getTableName() {
        return "ACCOUNTS";
    }

    @Override
    public String getAttribute(int columnNumber) {
        switch(columnNumber){
            case 1:
                return this.employeeID;
            case 2:
                return this.username;
            case 3:
                return this.password;
            case 4:
                return this.settingsFile;
            default:
                break;
        }
        return null;
    }

    @Override
    public void setAttribute(int columnNumber, String newAttribute) {
        switch(columnNumber){
            case 1:
                this.employeeID = newAttribute;
            case 2:
                this.username = newAttribute;
            case 3:
                this.password = newAttribute;
            case 4:
                this.settingsFile = newAttribute;
            default:
                break;
        }
    }

    @Override
    public Object get() {
        return new Account();
    }

}
