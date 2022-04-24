package edu.wpi.DapperDaemons.map.pathfinder;

import edu.wpi.DapperDaemons.backend.DAOFacade;
import edu.wpi.DapperDaemons.controllers.MapController;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.LocationNodeConnections;
import edu.wpi.DapperDaemons.entities.requests.Request;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.AnchorPane;

public class ShowRequestPaths {
  private static AnchorPane lineLayer;
  private static MapController controller;
  List<LocationNodeConnections> actualConnections;

  private final Double lineSize = 4.0;
  private static String currentFloor;

  private final int necessaryOffsetX = -110;
  private final int necessaryOffsetY = -5;
  private static int lineOffset = 0;
  private static int numberOfLines = 0;

  public ShowRequestPaths(AnchorPane lineLayer, MapController controller) {
    this.lineLayer = lineLayer;
    this.controller = controller;
    actualConnections = new ArrayList<>();
  }

  public void showAllPaths(Location location) {
    List<Request> requests = DAOFacade.getFilteredRequests(location.getNodeID());
    for (Request request : requests) {
      String reqType = request.requestType();
    }
  }

  public void setCurrentFloor(String floor) {
    this.currentFloor = floor;
  }
}
