package edu.wpi.DapperDaemons.map.pathfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class AStar {
    public AStar(String startLocation, String endLocaiton){
        PriorityQueue<String> queue = new PriorityQueue<>();
        HashMap<String,String> moveOrder = new HashMap<>();
        HashMap<String,String> costSoFar = new HashMap<>();
        queue.add(startLocation);
        while(!queue.isEmpty()){
            String current = queue.remove(); // Touch next node

            if(current.equals(endLocaiton))
                break;


        }
    }

    private static List<String> getNeighbors(String currentLocation){
        List<String> connected = new ArrayList<>();
        return null;
    }
}
