package edu.wpi.DapperDaemons.serial;

import arduino.*;
import com.fazecast.jSerialComm.SerialPort;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.ArduinoTimeOutException;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.UnableToConnectException;
import java.util.ArrayList;
import java.util.List;

/** Handles interaction with Arduino */
public class SerialCOM {
  String input;

  public SerialCOM() {
    this.input = "";
  }

  /**
   * Opens connection to Arduino and reads data from the port
   *
   * @return the data received from the serial port
   */
  public String readData() throws ArduinoTimeOutException, UnableToConnectException {

    // Obtain a list of all ports available on the PC
    List<String> ports = this.getAvailableCOMs();

    // Obtain an arduino object with the valid port
    Arduino arduino = this.setupArduino(ports); // can throw UnableToConnectException

    // Collect System time
    long startTime = System.currentTimeMillis();

    // Open Connection
    boolean connection = arduino.openConnection();

    // Throw exception if unable to connect to serial port
    if (!connection) {
      arduino.closeConnection(); // Should be impossible to reach
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

  /**
   * check all available ports to attempt to establish a connection with the arduino
   * @param ports a list of all ports on a computer
   * @return the arduino object containing the valid port
   * @throws UnableToConnectException if no ports can establish a connection
   */
  public Arduino setupArduino(List<String> ports) throws UnableToConnectException {
    for (String port : ports) {
      Arduino arduino = new Arduino(port, 9600);
      if(arduino.openConnection()) {
        arduino.closeConnection();
        return arduino;
      }
      arduino.closeConnection();
    }
    throw new UnableToConnectException();
  }
}
