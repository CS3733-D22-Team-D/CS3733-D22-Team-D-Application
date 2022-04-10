package edu.wpi.DapperDaemons.serial;

import edu.wpi.DapperDaemons.backend.SHA;
import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.ArduinoTimeOutException;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.UnableToConnectException;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.UserNotAuthorizedException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/** Handles RFID scan and determines validity */
public class RFIDHandler {

  private Employee user;
  private List<String> UIDs; // TODO: Try to integrate with Backend

  public RFIDHandler(Employee user) {
    this.user = user;
    this.UIDs = new ArrayList<String>();
    this.UIDs.add("9a1b036b82baba3177d83c27c1f7d0beacaac6de1c5fdcc9680c49f638c5fb9");
  }

  /**
   * Opens serial communication to get incoming UID and checks if it is valid
   *
   * @return true if the scan was valid, false otherwise
   */
  public boolean scan()
      throws UnableToConnectException, ArduinoTimeOutException, UserNotAuthorizedException,
          NoSuchAlgorithmException {
    SerialCOM reader = new SerialCOM();
    String inputID;
    if (!(user.getEmployeeType().equals(Employee.EmployeeType.ADMINISTRATOR)))
      throw new UserNotAuthorizedException(user.getEmployeeType());
    else {
      inputID = reader.readData();
      return UIDs.contains(SHA.toHexString(SHA.getSHA(inputID)));
    }
  }
}
