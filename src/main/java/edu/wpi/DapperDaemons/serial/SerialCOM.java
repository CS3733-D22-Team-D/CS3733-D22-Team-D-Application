package edu.wpi.DapperDaemons.serial;

import arduino.*;
import com.fazecast.jSerialComm.SerialPort;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.ArduinoTimeOutException;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.UnableToConnectException;
import java.util.ArrayList;
import java.util.List;

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
        input = arduino.serialRead(5);
        long currentTime = System.currentTimeMillis();
        if (currentTime >= startTime + 10000) {
          arduino.closeConnection();
          throw new ArduinoTimeOutException();
        }
      }
    }
    arduino.closeConnection();
    System.out.println("Received input: " + input);
    input = input.trim();
    return input;
  }

  /**
   * Obtains all available COM ports on a host computer
   *
   * @return a list of strings pertaining to all available COM ports
   */
  public List<String> getAvailableCOMs() {
    List<String> available = new ArrayList<>();
    SerialPort[] ports = SerialPort.getCommPorts();
    for (SerialPort s : ports) {
      available.add(s.getSystemPortName().trim());
      System.out.println(s.getSystemPortName().trim());
    }
    return available;
  }
}
