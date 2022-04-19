package edu.wpi.DapperDaemons.map.pathfinder;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.controllers.MapController;
import edu.wpi.DapperDaemons.controllers.helpers.AutoCompleteFuzzy;
import edu.wpi.DapperDaemons.controllers.helpers.FuzzySearchComparatorMethod;
import edu.wpi.DapperDaemons.entities.Location;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;

/** Creates a path on the map */
public class PathfinderHandler implements Initializable {

  private static AnchorPane lineLayer;
  private static MapController controller;
  private static List<Location> locations;

  /* Pathfinder handler info */
  @FXML private JFXComboBox<String> fromLocation;
  @FXML private JFXComboBox<String> toLocation;

  @FXML
  public void startFuzzySearch() {
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(fromLocation, new FuzzySearchComparatorMethod());
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(toLocation, new FuzzySearchComparatorMethod());
  }

  public PathfinderHandler(AnchorPane lineLayer, MapController controller) {
    this.lineLayer = lineLayer;
    this.controller = controller;
    locations = new ArrayList<>();
  }

  public PathfinderHandler() {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  @FXML
  public void showPath() {
    fromLocation.setValue(fromLocation.getValue().trim());
    toLocation.setValue(toLocation.getValue().trim());
    try {
      if (DAOPouch.getLocationDAO().get(fromLocation.getValue()).getXcoord()
          != -1) { // if from location is valid
        if (DAOPouch.getLocationDAO().get(fromLocation.getValue()).getXcoord()
            != -1) { // and to location is valid
          showPather(fromLocation.getValue(), toLocation.getValue());
          makeAllInVisible();
        } else {
          System.out.println("Not a valid end location!");
        }
      } else {
        System.out.println("Not a valid start location!");
      }
      filterByFloor(DAOPouch.getLocationDAO().get(fromLocation.getValue()).getFloor());
    } catch (Exception e) {
      //      e.printStackTrace();
      // TODO : Show the error message?
    }
    makeAllInVisible(); // For some reason I need two of these to make it actually invisible
    try {
      filterByFloor(DAOPouch.getLocationDAO().get(fromLocation.getValue()).getFloor());
    } catch (Exception e) {
      // Do thing
    }
  }

  @FXML
  public void clearPath() {
    makeAllInVisible();
    lineLayer.getChildren().clear();
    locations.clear();
  }

  public void showPather(String startNode, String endNode) {
    makeLinePath(startNode, endNode);
  }

  /**
   * Populates the line list / line image with all of its nodes using the AStar ppPlaner
   *
   * @param startNode
   * @param endNode
   */
  private void makeLinePath(String startNode, String endNode) {
    clearPath(); // Clears the previous path

    AStar ppPlanner = new AStar(); // The path plan planner
    // Gives all nodeID's of the path
    List<String> nodePath = ppPlanner.getPath(startNode, endNode);
    try {
      locations.add(DAOPouch.getLocationDAO().get(endNode));
    } catch (Exception e) {
      System.out.println("Something went wrong adding the last location");
    }
    for (String node : nodePath) {
      try {
        locations.add(DAOPouch.getLocationDAO().get(node));
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Location not found");
      }
    }

    double overflow = 0.0;
    for (int i = 0; i < locations.size(); i++) {
      // Add a new line to the list of lines
      //      System.out.println(
      //          "Position " + locations.get(i).getNodeID() + " to " + locations.get(i +
      // 1).getNodeID());
      //      System.out.println(
      //          "X Start : "
      //              + locations.get(i).getXcoord()
      //              + " Start Y: "
      //              + locations.get(i).getYcoord());
      //      System.out.println(
      //          "X End : "
      //              + locations.get(i + 1).getXcoord()
      //              + " End Y: "
      //              + locations.get(i + 1).getYcoord());
      Line pathLine =
          new Line(
              locations.get(i).getXcoord(),
              locations.get(i).getYcoord(),
              locations.get(i + 1).getXcoord(),
              locations.get(i + 1).getYcoord());
      pathLine.setFill(Color.RED);
      pathLine.setStroke(Color.RED);
      pathLine.setStrokeWidth(6.0);
      pathLine.setStrokeLineCap(StrokeLineCap.SQUARE);
      double lineLength =
          Math.sqrt(
              Math.pow(locations.get(i).getXcoord() + locations.get(i + 1).getXcoord(), 2)
                  + Math.pow(locations.get(i).getYcoord() + locations.get(i + 1).getYcoord(), 2));

      Circle ifNecessary;
      if (!locations
          .get(i)
          .getFloor()
          .equals(
              locations.get(i + 1).getFloor())) { // If on different floor, create point particle
        ifNecessary = new Circle(locations.get(i).getXcoord(), locations.get(i).getYcoord(), 6);
        ifNecessary.setFill(Color.RED);
        lineLayer.getChildren().add(ifNecessary);
        System.out.println("Added new point since it went up a floor");
      }
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

      double maxOffset = pathLine.getStrokeDashArray().stream().reduce(0d, (a, b) -> a + b);
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
      timeline.play(); // maybe this'll work

      lineLayer.getChildren().add(pathLine);
      //      System.out.println("added new line to lineLayer");
    }
  }

  public void makeAllInVisible() {
    lineLayer.getChildren().forEach(c -> c.setVisible(false));
  }

  public void filterByFloor(String floor) {
    makeAllInVisible();
    for (int i = 0;
        i < locations.size();
        i++) { // for every child, add make the locations on this floor visible
      // TODO : For some reason the last node is currently showing up on the wrong floor
      if (locations.get(i).getFloor().equals(floor)) {
        System.out.println("Showing " + locations.get(i).getNodeID());
        lineLayer.getChildren().get(i).setVisible(true);
      }
    }
  }
}
