package edu.wpi.DapperDaemons.serial;

import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.ArduinoTimeOutException;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.UnableToConnectException;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.UserNotAuthorizedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles RFID scan and determines validity
 */
public class RFIDHandler {

  private final SerialCOM reader;
  private final String portCOM;
  private Employee user;
  private List<String> UIDs; //TODO: Try to integrate with Backend

  public RFIDHandler(Employee user, String portCOM) {
    if(user.getEmployeeType().equals(Employee.EmployeeType.ADMINISTRATOR)) this.user = user;
    else this.user = null;
    this.portCOM = portCOM;
    this.reader = new SerialCOM();
    this.UIDs = new ArrayList<String>();
    this.UIDs.add("354");
  }

  /**
   * Opens serial communication to get incoming UID and checks if it is valid
   * @return true if the scan was valid, false otherwise
   */
  public boolean scan() throws UnableToConnectException, ArduinoTimeOutException, UserNotAuthorizedException {
    if(this.user == null) throw new UserNotAuthorizedException(user.getEmployeeType());
    else {
      String inputID = "";
      inputID = reader.readData(this.portCOM);
      return UIDs.contains(inputID);
    }
  }
}
