package edu.wpi.DapperDaemons.serial;

import arduino.*;

/** Handles interaction with Arduino */
public class SerialCOM {
  Arduino arduino;

  /**
   * Opens connection to Arduino
   *
   * @param serialPort indicates the name of the serial port that the Arduino is using
   * @return the data received from the serial port
   */
  public String readData(String serialPort) {
    String input = "";
    arduino = new Arduino(serialPort, 9600); // this is where you specify your COM
    boolean connection = arduino.openConnection();
    if (!connection) {
      System.err.println("Error: Unable to Connect to serial port");
    } else {
      while (input.equals("")) {
        input = arduino.serialRead(10000);
      }
    }
    System.out.println("Received input: " + input);
    input = input.trim();
    arduino.closeConnection();
    return input;
  }
}
