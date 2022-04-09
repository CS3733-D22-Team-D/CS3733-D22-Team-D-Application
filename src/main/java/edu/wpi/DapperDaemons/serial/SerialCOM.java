package edu.wpi.DapperDaemons.serial;

import arduino.*;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.ArduinoTimeOutException;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.UnableToConnectException;

/**
 * Handles interaction with Arduino
 */
public class SerialCOM {
    Arduino arduino;

    /**
     * Opens connection to Arduino
     *
     * @param serialPort indicates the name of the serial port that the Arduino is using
     * @return the data received from the serial port
     */
    public String readData(String serialPort) throws ArduinoTimeOutException, UnableToConnectException {
        // Collect System time and Init input string
        long startTime = System.currentTimeMillis();
        String input = "";

        // Init arduino at the specified COM port
        arduino = new Arduino(serialPort, 9600);

        // Open Connection
        boolean connection = arduino.openConnection();

        // Throw exception if unable to connect to serial port
        if (!connection){
          arduino.closeConnection();
          throw new UnableToConnectException();
        }
        // otherwise, attempt to collect data, timeout if it has been over 10 seconds
        else {
            while (input.equals("")) {
                long currentTime = System.currentTimeMillis();
                if (currentTime == startTime + 10000){
                  arduino.closeConnection();
                  throw new ArduinoTimeOutException();
                }
                else input = arduino.serialRead();
            }
          System.out.println("Received input: " + input);
          input = input.trim();
          arduino.closeConnection();
        }
        return input;
    }
}
