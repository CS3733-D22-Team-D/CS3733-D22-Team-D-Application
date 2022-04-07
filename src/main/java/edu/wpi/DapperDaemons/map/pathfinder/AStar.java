package edu.wpi.DapperDaemons.map.pathfinder;

import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.LocationNodeConnections;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class AStar {
  private DAO<Location> locationDAO;
  private List<Location> locations;
  private DAO<LocationNodeConnections> nodeConnectionsDAO;
  private List<LocationNodeConnections> nodeConnections;

  /**
   * Returns a list of location nodeID's for the path from one place to the next
   *
   * @param startLocation the starting location's nodeID
   * @param endLocation the ending location's nodeID
   * @throws SQLException only throws when locationDAO.getAll() doesn't work
   */
  public AStar(String startLocation, String endLocation) throws SQLException {
    PriorityQueue<WalkableNode> queue = new PriorityQueue<>();
    HashMap<String, WalkableNode> moveOrder = new HashMap<>();
    // Uses the location's nodeID as a key for the previous node's WalkableNode
    HashMap<String, Double> costSoFar = new HashMap<>();
    // Uses the location's nodeID as a key for the previous node's WalkableNode
    locations = locationDAO.getAll();
    nodeConnections = nodeConnectionsDAO.getAll();

    queue.add(new WalkableNode(startLocation, 0.0));

    while (!queue.isEmpty()) {
      WalkableNode current = queue.remove(); // Grab the next node

      if (current.getLocationName().equals(endLocation)) break;

      for (String nextLocation : getNeighbors(current)) {
        Double new_cost =
            costSoFar.get(current)
                + getDistance(
                    current.getLocationName(),
                    nextLocation); // add the distance from current to next
        if (!costSoFar.keySet().contains(nextLocation)
            || new_cost < costSoFar.get(nextLocation)
                && !moveOrder
                    .keySet()
                    .contains(nextLocation)) { // If nextLocation isn't in the queue,
          // the cost is less than the one already there, and the node is not in the moveOrder yet
          costSoFar.put(nextLocation, new_cost); // save it in the costSoFar and add it to the queue
          Double priority = new_cost + getDistance(nextLocation, endLocation);
          // Priority is the distance from this node to the goal + costSoFar of this node
          queue.add(new WalkableNode(nextLocation, priority));
          moveOrder.put(nextLocation, current);
        }
      }
    }
  }

  private List<String> getNeighbors(WalkableNode currentLocation) {
    List<LocationNodeConnections> connected;
    List<String> walkableNode = new ArrayList<>();
    // List of walkable connected nodes as strings using their nodeID
    connected = nodeConnectionsDAO.filter(nodeConnections, 2, currentLocation.getLocationName());
    for (LocationNodeConnections location : connected) {
      String nodeID = location.getConnectionTwo();
      walkableNode.add(nodeID); // Gets the connected node as a String
    }
    return walkableNode; // returns connected nodeID's
  }

  private Double getDistance(String currentLocation, String nextLocation) {
    DAO<Location> locationDAO = DAOPouch.getLocationDAO();
    // If it can't find the position, then this is basically saying that the node parser won't let
    // it break everything
    Location current =
        new Location(
            "Unknown", -1000, -1000, "Unknown", "Unknown", "Unknown", "Unknown", "Unknown");
    Location next =
        new Location("Unknown", 1000, 1000, "Unknown", "Unknown", "Unknown", "Unknown", "Unknown");
    try {
      current = locationDAO.filter(locations, 1, currentLocation).get(0);
      next = locationDAO.filter(locations, 1, nextLocation).get(0);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Couldn't find location in table");
    }
    Double distance =
        Math.sqrt(
            (current.getXcoord() - next.getXcoord())
                ^ 2 + (current.getYcoord() - next.getYcoord())
                ^ 2);
    if (!current.getFloor().equals(next.getFloor())) {
      System.out.println(
          "Its on a separate floor, adding 50"); // Comment out after it works - which it will first
      // time
      distance += 50.0;
    }
    return distance;
  }
}
