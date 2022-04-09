package edu.wpi.DapperDaemons.serial.ArduinoExceptions;

import edu.wpi.DapperDaemons.entities.Employee;

public class UserNotAuthorizedException extends Exception{
    private Employee.EmployeeType employeeType;

    public UserNotAuthorizedException(Employee.EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public Employee.EmployeeType getEmployeeType(){
        return this.employeeType;
    }
}
