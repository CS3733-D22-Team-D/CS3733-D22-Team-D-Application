package edu.wpi.DapperDaemons.serial;

import arduino.*;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.ArduinoTimeOutException;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.UnableToConnectException;

/** Handles interaction with Arduino */
public class SerialCOM {
  String COM;
  String input;

  public SerialCOM(String COM) {
    this.COM = COM;
    this.input = "";
  }

  /**
   * Opens connection to Arduino and reads data from the port
   *
   * @return the data received from the serial port
   */
  public String readData() throws ArduinoTimeOutException, UnableToConnectException {
    // Collect System time
    long startTime = System.currentTimeMillis();

    // Init arduino at the specified COM port
    Arduino arduino = new Arduino(this.COM, 9600);

    // Open Connection
    boolean connection = arduino.openConnection();

    // Throw exception if unable to connect to serial port
    if (!connection) {
      arduino.closeConnection();
      throw new UnableToConnectException();
    }
    // otherwise, attempt to collect data, timeout if it has been over 10 seconds
    else {
      while (this.input.equals("")) {
        input = arduino.serialRead();
        long currentTime = System.currentTimeMillis();
        if (currentTime == startTime + 10000) {
          arduino.closeConnection();
          throw new ArduinoTimeOutException();
        }
      }
      System.out.println("Received input: " + input);
      input = input.trim();
      arduino.closeConnection();
    }
    return input;
  }
}
