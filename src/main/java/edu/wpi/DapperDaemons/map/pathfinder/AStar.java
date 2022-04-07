package edu.wpi.DapperDaemons.map.pathfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;



public class AStar {
  public AStar(String startLocation, String endLocaiton) {
    PriorityQueue<WalkableNode> queue = new PriorityQueue<>();
    HashMap<WalkableNode, WalkableNode> moveOrder = new HashMap<>();
    HashMap<WalkableNode, Double> costSoFar = new HashMap<>();
    queue.add(new WalkableNode(startLocation, 0.0));
    while (!queue.isEmpty()) {
      WalkableNode current = queue.remove(); // Grab the next node

      if (current.equals(endLocaiton)) break;

      for (WalkableNode nextLocation : getNeighbors(current)) {
        Double new_cost = costSoFar.get(current) + 0; // add the distance from current to next
        if (!costSoFar.keySet().contains(nextLocation)
            || new_cost < costSoFar.get(nextLocation) &&
            !moveOrder.keySet().contains(nextLocation)) { // If nextLocation isn't in the queue,
          // the cost is less than the one already there, and the node is not in the moveOrder yet
          costSoFar.put(nextLocation, new_cost); // save it in the costSoFar and add it to the queue
          Double priority =
              new_cost + 0; // Priority is the distance from this node to the goal + costSoFar
          queue.add(nextLocation);
          moveOrder.put(nextLocation, current);
        }
      }
    }
  }

  private static List<WalkableNode> getNeighbors(WalkableNode currentLocation) {
    List<WalkableNode> connected = new ArrayList<>();
    return connected;
  }

  private static Double getDistance(WalkableNode currentLocation, WalkableNode nextLocation){
    return 0.0;
  }
}
