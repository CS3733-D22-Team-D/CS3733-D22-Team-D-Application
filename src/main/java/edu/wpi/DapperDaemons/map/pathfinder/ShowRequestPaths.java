package edu.wpi.DapperDaemons.map.pathfinder;

import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.backend.DAOFacade;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.controllers.MapController;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.LocationNodeConnections;
import edu.wpi.DapperDaemons.entities.requests.Request;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;

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

  private static List<Location> locations;

  public ShowRequestPaths(AnchorPane lineLayer, MapController controller) {
    this.lineLayer = lineLayer;
    this.controller = controller;
    actualConnections = new ArrayList<>();
    locations = new ArrayList<>();
  }

  public void showAllPaths(Location location) {
    List<Request> requests = DAOFacade.getFilteredRequests(location.getNodeID());
    AStar ppHelper = new AStar();
    for (Request request : requests) {
      if (request.requiresTransport()) {
        makeLinePath(
            request.getNodeID(),
            request.transportFromRoomID(),
            getLineColor(request.requestType()));
      }
    }
    filterByFloor(currentFloor);
  }

  public void clearPath() {
    lineLayer.getChildren().clear();
    locations.clear();
  }

  private void makeLinePath(String startNode, String endNode, Color color) {
    AStar ppPlanner = new AStar(); // The path plan planner
    // Gives all nodeID's of the path
    List<String> nodePath = ppPlanner.getPath(startNode, endNode);

    int offsetX = necessaryOffsetX + PathfinderHandler.lineOffset * PathfinderHandler.numberOfLines;
    int offsetY = necessaryOffsetY + PathfinderHandler.lineOffset * PathfinderHandler.numberOfLines;
    try {
      locations.add(DAOPouch.getLocationDAO().get(endNode));
    } catch (Exception e) {
      App.LOG.info("Something went wrong adding the last location");
    }
    for (String node : nodePath) {
      try {
        locations.add(DAOPouch.getLocationDAO().get(node));
      } catch (Exception e) {
        e.printStackTrace();
        App.LOG.info("Location " + node + " not found");
      }
    }
    try {
      locations.add(DAOPouch.getLocationDAO().get(startNode));
    } catch (Exception e) {
      System.out.println("Something went wrong adding the start location");
    }

    double overflow = 0.0;
    for (int i = 0; i < locations.size() - 1; i++) {
      Line pathLine;
      Circle ifNecessary;
      if (!locations
          .get(i)
          .getFloor()
          .equals(
              locations.get(i + 1).getFloor())) { // If on different floor, create point particle
        ifNecessary =
            new Circle(
                locations.get(i).getXcoord() + offsetX, locations.get(i).getYcoord() + offsetY, 6);
        ifNecessary.setFill(color);
        lineLayer.getChildren().add(ifNecessary);
        //        System.out.println("Added new point since it went up a floor");
      } else { // If on the same floor, show the path
        pathLine =
            new Line(
                locations.get(i).getXcoord() + offsetX,
                locations.get(i).getYcoord() + offsetY,
                locations.get(i + 1).getXcoord() + offsetX,
                locations.get(i + 1).getYcoord() + offsetY);
        pathLine.setFill(color);
        pathLine.setStroke(color);
        pathLine.setStrokeWidth(lineSize);
        pathLine.setStrokeLineCap(StrokeLineCap.SQUARE);
        double lineLength =
            Math.sqrt(
                Math.pow(locations.get(i).getXcoord() + locations.get(i + 1).getXcoord(), 2)
                    + Math.pow(locations.get(i).getYcoord() + locations.get(i + 1).getYcoord(), 2));

        // Start is 0d 24d
        double whiteSpace = 24;
        double lineSpace = 32;
        boolean chancla = true;
        while (lineLength > 0) {
          if (chancla) {
            pathLine.getStrokeDashArray().add(lineSpace);
            lineLength -= lineSpace;
            overflow = Math.abs(lineLength);
          } else {
            pathLine.getStrokeDashArray().add(whiteSpace);
            lineLength -= whiteSpace;
            overflow = 0;
          }
        }

        double maxOffset = -pathLine.getStrokeDashArray().stream().reduce(0d, (a, b) -> a + b);
        Timeline timeline =
            new Timeline(
                new KeyFrame(
                    Duration.ZERO,
                    new KeyValue(pathLine.strokeDashOffsetProperty(), 0, Interpolator.LINEAR)),
                new KeyFrame(
                    Duration.seconds(100),
                    new KeyValue(
                        pathLine.strokeDashOffsetProperty(), maxOffset, Interpolator.LINEAR)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        lineLayer.getChildren().add(pathLine);
      }
    }
  }

  private Color getLineColor(String reqType) {
    switch (reqType) {
      case "Equipment Cleaning Request":
        return Color.BLUE;
      case "Language Request":
        return Color.YELLOW;
      case "Meal Delivery Request":
        return Color.MEDIUMVIOLETRED;
      case "Medical Equipment Request":
        return Color.GREEN;
      case "Medicine Request":
        return Color.CORNFLOWERBLUE;
      case "Patient Transport Request":
        return Color.ORANGERED;
      case "Security Request":
        return Color.ORCHID;
      default:
        return Color.GOLD;
    }
  }

  public void setCurrentFloor(String floor) {
    this.currentFloor = floor;
  }

  public void filterByFloor(String floor) {
    setCurrentFloor(floor);
    makeAllInVisible();
    for (int i = 0;
        i < locations.size() - 1;
        i++) { // for every child, add make the locations on this floor visible
      if (locations.get(i).getFloor().equals(floor)) {
        //        System.out.println("Showing " + locations.get(i).getNodeID());
        lineLayer.getChildren().get(i).setVisible(true);
      }
    }
  }

  public void makeAllInVisible() {
    lineLayer.getChildren().forEach(c -> c.setVisible(false));
  }
}
