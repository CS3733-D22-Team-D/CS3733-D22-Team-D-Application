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
  public String readData(String COM) throws ArduinoTimeOutException, UnableToConnectException {

    Arduino arduino = new Arduino(COM, 9600);

    // Collect System time
    long startTime = System.currentTimeMillis();

    // Open Connection
    boolean connection = arduino.openConnection();
    System.out.println("Opening connection...");

    // Throw exception if unable to connect to serial port
    if (!connection) {
      arduino.closeConnection(); // Should be impossible to reach
      throw new UnableToConnectException();
    }
    // otherwise, attempt to collect data, timeout if it has been over 10 seconds
    else {
      System.out.println("Connected!");
      while (this.input.equals("")) {
        input = arduino.serialRead();
        if (containsLetters(input)) input = "";
        long currentTime = System.currentTimeMillis();
        if (currentTime >= startTime + 10000) {
          System.out.println("Timeout!");
          arduino.closeConnection();
          throw new ArduinoTimeOutException();
        }
      }
      arduino.closeConnection();
      System.out.println("Received input: " + input);
      input = input.trim();
    }
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
   *
   * @return the arduino object containing the valid port
   * @throws UnableToConnectException if no ports can establish a connection
   */
  public Arduino setupArduino() throws UnableToConnectException {
    List<String> ports = this.getAvailableCOMs();
    System.out.println("Check system serial ports...");
    // For each available port
    for (String port : ports) {
      // Create arduino object on that port
      Arduino arduino = new Arduino(port, 9600);
      System.out.println("Checking Serial Port: " + port);
      // Check if arduino has a connection and sends buffer message
      if (arduino.openConnection() && this.checkForBuffer(arduino)) {
        System.out.println("Port " + port + " has correct connection");
        arduino.closeConnection();
        return arduino;
      } else arduino.closeConnection();
    }
    throw new UnableToConnectException(); // Throw exception if unable to connect to any of the
    // ports
  }

  /**
   * checks for the arduino buffer indicator for 2.5 seconds
   *
   * @param arduino arduino connect to COM
   * @return true if buffer is received (COM is correct)
   */
  public boolean checkForBuffer(Arduino arduino) {
    long startTime = System.currentTimeMillis();
    String aIn = "";
    while (!containsLetters(aIn)) {
      aIn = arduino.serialRead();
      System.out.println("Test signal: " + aIn);
      long currentTime = System.currentTimeMillis();
      if (currentTime >= startTime + 2000) {
        return false;
      }
    }
    System.out.println("Buffer received on " + arduino.getPortDescription());
    return true;
  }

  /**
   * Checks if a given String has a letter
   *
   * @param input string input
   * @return true if a letter is involved
   */
  public boolean containsLetters(String input) {
    if (input.isEmpty()) return false;
    else {
      for (int i = 0; i < input.length(); ++i) {
        if (Character.isLetter(input.charAt(i))) return true;
      }
    }
    return false;
  }
}
