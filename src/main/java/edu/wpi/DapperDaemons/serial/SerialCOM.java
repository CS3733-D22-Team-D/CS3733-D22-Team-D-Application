package edu.wpi.DapperDaemons.serial;

import arduino.*;

public class SerialCOM {
  Arduino arduino;

  public String readData() {
    String input = "";
    arduino = new Arduino("COM4", 9600); // this is where you specify your COM
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
