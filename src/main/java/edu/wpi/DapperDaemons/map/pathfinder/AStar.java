package edu.wpi.DapperDaemons.map.pathfinder;

import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.LocationNodeConnections;
import java.sql.SQLException;
import java.util.*;

public class AStar {
  private List<Location> locations;
  private List<LocationNodeConnections> nodeConnections;

  /**
   * Creates an AStar helper class!!
   *
   * @throws SQLException only throws exception when the getAll calls no work
   */
  public AStar() throws SQLException {
    locations = DAOPouch.getLocationDAO().getAll();
    nodeConnections = DAOPouch.getLocationNodeDAO().getAll();
  }

  /**
   * Returns a list of location nodeID's for the path from one place to the next
   *
   * @param startLocation the starting location's nodeID
   * @param endLocation the ending location's nodeID
   */
  public List<String> getPath(String startLocation, String endLocation) {
    PriorityQueue<WalkableNode> queue = new PriorityQueue<>();
    HashMap<String, WalkableNode> moveOrder = new HashMap<>(); // The path actually taken
    // Uses the location's nodeID as a key for the previous node's WalkableNode
    HashMap<String, Double> costSoFar = new HashMap<>();
    // Uses the location's nodeID as a key for the previous node's WalkableNode

    queue.add(new WalkableNode(startLocation, 0.0));
    moveOrder.put(startLocation, null);
    costSoFar.put(startLocation, 0.0);

    while (!queue.isEmpty()) {
      WalkableNode current = queue.remove(); // Grab the next node
      //      System.out.println("Currently at " + current.getLocationName());

      if (current.getLocationName().equals(endLocation)) {
        System.out.println("It Reached the goal!!");
        break;
      }

      for (String nextLocation : getNeighbors(current)) {
        Double new_cost =
            costSoFar.get(current.getLocationName())
                + getDistance(
                    current.getLocationName(),
                    nextLocation); // add the distance from current to next
        if (!costSoFar.keySet().contains(nextLocation)
            || new_cost < costSoFar.get(nextLocation)) { // If nextLocation isn't in the queue,
          //          System.out.println("Saving the location " + nextLocation + " in the path");
          // the cost is less than the one already there, and the node is not in the moveOrder yet
          costSoFar.put(nextLocation, new_cost); // save it in the costSoFar and add it to the queue
          Double priority =
              Math.sqrt(new_cost) + Math.pow(getDistance(nextLocation, endLocation), 3);
          // Priority is the distance from this node to the goal + costSoFar of this node
          queue.add(new WalkableNode(nextLocation, priority));
          moveOrder.put(nextLocation, current);
        }
      }
    }

    List<String> path = new ArrayList<>();
    String current = endLocation;
    if (moveOrder.get(current) != null) {
      while (moveOrder.get(current) != null) {
        path.add(current);
        current = moveOrder.get(current).getLocationName();
      }
    }

    List<String> forwardPath = new ArrayList<>();
    for (int i = 0, j = path.size() - 1; i < j; i++) {
      path.add(i, path.remove(j));
    }
    if (path.isEmpty()) {
      path.add("Path Not Found");
    }
    return path;
  }

  /**
   * gets all neighbors by doing some fancy pancy flip flops and utilizing ALlEdges / Location Node
   * database thingy
   *
   * @param currentLocation
   * @return
   */
  private List<String> getNeighbors(WalkableNode currentLocation) {
    List<LocationNodeConnections> connected;
    List<String> walkableNode = new ArrayList<>();
    // List of walkable connected nodes as strings using their nodeID
    connected =
        DAOPouch.getLocationNodeDAO().filter(nodeConnections, 3, currentLocation.getLocationName());
    List<Boolean> flipFlop = new ArrayList<>(Arrays.asList(new Boolean[connected.size()]));
    Collections.fill(flipFlop, Boolean.FALSE); // Helps the program decide which column to look in
    for (LocationNodeConnections connection :
        DAOPouch.getLocationNodeDAO()
            .filter(nodeConnections, 2, currentLocation.getLocationName())) {
      flipFlop.add(Boolean.TRUE); // Switches the column to the second one to look in
      connected.add(connection);
    }
    for (LocationNodeConnections location : connected) {
      String nodeID = location.getConnectionOne();
      if (flipFlop.get(connected.indexOf(location))) nodeID = location.getConnectionTwo();
      walkableNode.add(nodeID); // Gets the connected node as a String
      //      System.out.println("Neighbor node " + nodeID); // For letting me see stuff
    }
    return walkableNode; // returns connected nodeID's
  }

  /**
   * Gets the distance utilizing the Location database and the XCoord and YCoord
   *
   * @param currentLocation
   * @param nextLocation
   * @return
   */
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
      //      e.printStackTrace();
      //      System.out.println("Couldn't find location in table");
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
