package edu.wpi.DapperDaemons.serial;

import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.ArduinoTimeOutException;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.UnableToConnectException;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.UserNotAuthorizedException;

/** Handles login of user using RFID, to interact directly with UI */
public class RFIDMachine {

  public enum LoginState {
    INVALIDUSER,
    TIMEOUT,
    UNABLETOCONNECT,
    SUCCESS
  }

  private Employee employee;
  private String COM;

  public RFIDMachine(Employee employee, String COM) {
    this.employee = employee;
    this.COM = COM;
  }

  /**
   * Login Method for RFID, It will only allow users who are Administrators to login with a valid
   * RFID card All other users will be denied
   *
   * @return a login state representing the result of the attempt
   */
  public LoginState login() {
    RFIDHandler handler = new RFIDHandler(this.employee, this.COM);
    boolean loginAttempt;
    try {
      loginAttempt = handler.scan();
    } catch (UserNotAuthorizedException e) {
      System.out.println("ACCESSED DENIED: INVALID USER TYPE " + e.getEmployeeType());
      return LoginState.INVALIDUSER;
    } catch (ArduinoTimeOutException e) {
      System.out.println("ACCESSED DENIED: RFID SCAN TIMEOUT");
      return LoginState.TIMEOUT;
    } catch (UnableToConnectException e) {
      System.err.println("ERROR: UNABLE TO CONNECT TO RFID SCANNER");
      return LoginState.UNABLETOCONNECT;
    }
    if (loginAttempt) return LoginState.SUCCESS;
    else return LoginState.INVALIDUSER;
  }
}
