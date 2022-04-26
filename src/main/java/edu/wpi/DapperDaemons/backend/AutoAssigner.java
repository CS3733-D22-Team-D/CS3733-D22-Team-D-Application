package edu.wpi.DapperDaemons.backend;

import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.entities.requests.Request;

import java.util.ArrayList;
import java.util.List;

public class AutoAssigner {
    public static String assignKit() {
        List<Employee> employeeList = new ArrayList<>(DAOPouch.getEmployeeDAO().filter(5,"KITCHEN").values());
        List<Integer> numberOfTasks = new ArrayList<>();
        String bestPick = employeeList.get(0).getNodeID();
        for(Employee employee : employeeList) {
            List<Request> requests = DAOFacade.searchRequestsByName()

        }
        return null;
    }
}
