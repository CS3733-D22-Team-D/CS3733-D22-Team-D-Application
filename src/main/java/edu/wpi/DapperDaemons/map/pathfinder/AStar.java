package edu.wpi.DapperDaemons.map.pathfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class AStar {
    public AStar(String startLocation, String endLocaiton){
        PriorityQueue<String> queue = new PriorityQueue<>();
        HashMap<String,Double> moveOrder = new HashMap<>();
        HashMap<String,Double> costSoFar = new HashMap<>();
        queue.add(startLocation);
        while(!queue.isEmpty()){
            String current = queue.remove(); // Touch next node

            if(current.equals(endLocaiton))
                break;

            for(String nextLocation:getNeighbors(current)){
                Double new_cost = costSoFar.get(current); // add
                if(!costSoFar.keySet().contains(nextLocation) || new_cost < costSoFar.get(nextLocation)){ // If nextLocation isn't in the queue or the cost is less than the one already there
                    costSoFar.put(nextLocation,new_cost); // save it in the costSoFar and add it to the queue
                    Double priority = new_cost + 0; // Priority is the distance from this node to the goal + costSoFar
                    queue.add(nextLocation);
                }
            }
        }
    }

    private static List<String> getNeighbors(String currentLocation){
        List<String> connected = new ArrayList<>();
        return null;
    }
}
