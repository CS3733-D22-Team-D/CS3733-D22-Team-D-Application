package edu.wpi.DapperDaemons.serial;

import arduino.*;

public class serialCOM {
  Arduino arduino;

  public String readData() {
    String input = "";
    arduino = new Arduino("COM4", 9600); // this is where you specify your COM
    boolean connection = arduino.openConnection();
    if (!connection) {
      System.err.println("Error: Unable to Connect to serial port");
    } else {
      while (input.equals("")) {
        input = arduino.serialRead();
      }
    }
    System.out.println(input);
    return input;
  }
}
