package edu.wpi.DapperDaemons.serial;

import edu.wpi.DapperDaemons.serial.ArduinoExceptions.ArduinoTimeOutException;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.UnableToConnectException;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles RFID scan and determines validity
 */
public class RFIDHandler {

  private List<String> validUIDs; // TODO: Get this from a DAO (ask backend)
  private SerialCOM reader;
  private String portCOM;

  public RFIDHandler(String portCOM) {
    reader = new SerialCOM();
    validUIDs = new ArrayList<>();
    validUIDs.add("354");
    this.portCOM = portCOM;
  }

  /**
   * Opens serial communication to get incoming UID and checks if it is valid
   * @return true if the scan was valid, false otherwise
   */
  public boolean scan() throws UnableToConnectException, ArduinoTimeOutException {
    String inputID = "";
    inputID = reader.readData(this.portCOM);

    for (String uid : this.validUIDs) {
      if (inputID.equals(uid)) return true;
    }
    return false;
  }
}
