package edu.wpi.DapperDaemons.backend;

import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.entities.requests.MealDeliveryRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;

import java.util.ArrayList;
import java.util.List;

public class AutoAssigner {
    private static int findWeight(String priority) {
        switch (priority) {
            case "LOW":
                return 1;
            case "MEDIUM":
                return 2;
            case "HIGH":
                return 3;
            case "OVERDUE":
                return 5;
            default:
                return 4;
        }
    }

    public static String assignKitchenStaff(Request autoAssign) {
        List<Employee> employeeList = new ArrayList<>(DAOPouch.getEmployeeDAO().filter(5,"KITCHEN").values());
        String bestPick = employeeList.get(0).getNodeID();
        MealDeliveryRequest req;
        if(autoAssign instanceof MealDeliveryRequest){
            req = (MealDeliveryRequest) autoAssign;

            int weightedWithFewest = 10000; // No one should have 10000 tasks ever
            for(Employee employee : employeeList) {
                List<Request> requests = DAOFacade.searchRequestsByAssignee(employee.getNodeID());
                int currentWeight = 0;
                for(Request request : requests) {
                    currentWeight += findWeight(request.getPriority().name());
                }
                if(currentWeight < weightedWithFewest) {
                    bestPick = employee.getNodeID();
                    weightedWithFewest = currentWeight;
                }
            }
            return bestPick;
        } else {
            App.LOG.info("For some reason, the input request was not the correct type");
            return "Unassigned";
        }
    }

    public static String assignAdmin(Request autoAssign) {
        List<Employee> employeeList = new ArrayList<>(DAOPouch.getEmployeeDAO().filter(5,"").values());
        String bestPick = employeeList.get(0).getNodeID();
        MealDeliveryRequest req;
        if(autoAssign instanceof MealDeliveryRequest){
            req = (MealDeliveryRequest) autoAssign;

            int weightedWithFewest = 10000; // No one should have 10000 tasks ever
            for(Employee employee : employeeList) {
                List<Request> requests = DAOFacade.searchRequestsByAssignee(employee.getNodeID());
                int currentWeight = 0;
                for(Request request : requests) {
                    currentWeight += findWeight(request.getPriority().name());
                }
                if(currentWeight < weightedWithFewest) {
                    bestPick = employee.getNodeID();
                    weightedWithFewest = currentWeight;
                }
            }
            return bestPick;
        } else {
            App.LOG.info("For some reason, the input request was not the correct type");
            return "Unassigned";
        }
    }

}
