package edu.wpi.DapperDaemons.serial;

import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.ArduinoTimeOutException;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.UnableToConnectException;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.UserNotAuthorizedException;
import java.util.ArrayList;
import java.util.List;

/** Handles RFID scan and determines validity */
public class RFIDHandler {

  private String COM;
  private Employee user;
  private List<String> UIDs; // TODO: Try to integrate with Backend

  public RFIDHandler(Employee user, String COM) {
    this.user = user;
    this.COM = COM;
    this.UIDs = new ArrayList<String>();
    this.UIDs.add("354");
  }

  /**
   * Opens serial communication to get incoming UID and checks if it is valid
   *
   * @return true if the scan was valid, false otherwise
   */
  public boolean scan()
      throws UnableToConnectException, ArduinoTimeOutException, UserNotAuthorizedException {
    SerialCOM reader = new SerialCOM(this.COM);
    String inputID;
    if (!(user.getEmployeeType().equals(Employee.EmployeeType.ADMINISTRATOR)))
      throw new UserNotAuthorizedException(user.getEmployeeType());
    else {
      inputID = reader.readData();
      return UIDs.contains(inputID);
    }
  }
}
