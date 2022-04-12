package edu.wpi.DapperDaemons.map.pathfinder;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.controllers.MapController;
import edu.wpi.DapperDaemons.entities.Location;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

/** Creates a path on the map */
public class PathfinderHandler implements Initializable {

  private AnchorPane lineLayer;
  private MapController controller;
  private List<String> nodePath;
  private List<Location> locations;

  /* Pathfinder handler info */
  @FXML private JFXComboBox<String> fromLocation;
  @FXML private JFXComboBox<String> toLocation;

  public PathfinderHandler(AnchorPane lineLayer, MapController controller) {
    this.lineLayer = lineLayer;
    this.controller = controller;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  @FXML
  public void showPath() {
    try {
      if (DAOPouch.getLocationDAO().get(fromLocation.getValue()).getXcoord()
          != -1) { // if from location is valid
        if (DAOPouch.getLocationDAO().get(fromLocation.getValue()).getXcoord()
            != -1) { // and to location is valid
          showPather(fromLocation.getValue(), toLocation.getValue());
        }
      }
    } catch (Exception e) {
      //      e.printStackTrace();
      // TODO : Show the error message?
    }
  }

  @FXML
  public void clearPath() {
    makeAllInVisible();
    lineLayer.getChildren().removeAll();
    nodePath.clear();
    locations.clear();
  }

  public void showPather(String startNode, String endNode) {
    makeLinePath(startNode, endNode);
    makeAllInVisible();
  }

  /**
   * Populates the line list / line image with all of its nodes using the AStar ppPlaner
   *
   * @param startNode
   * @param endNode
   */
  private void makeLinePath(String startNode, String endNode) {
    lineLayer.getChildren().removeAll();

    AStar ppPlanner = new AStar(); // The path plan planner
    // Gives all nodeID's of the path
    nodePath = ppPlanner.getPath(startNode, endNode);
    locations = new ArrayList<>();
    for (String node : nodePath) {
      try {
        locations.add(DAOPouch.getLocationDAO().get(node));
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Location not found");
      }
    }

    double overflow = 0.0;
    for (int i = 0; i < locations.size() - 1; i++) {
      // Add a new line to the list of lines
      System.out.println(
          "Position " + locations.get(i).getNodeID() + " to " + locations.get(i + 1).getNodeID());
      System.out.println(
          "X Start : "
              + locations.get(i).getXcoord()
              + " Start Y: "
              + locations.get(i).getYcoord());
      System.out.println(
          "X End : "
              + locations.get(i + 1).getXcoord()
              + " End Y: "
              + locations.get(i + 1).getYcoord());
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
      lineLayer.getChildren().add(pathLine);
    }
  }

  public void makeAllInVisible() {
    lineLayer.getChildren().forEach(c -> c.setVisible(false));
  }

  public void filterByFloor(String floor) {
    makeAllInVisible();
    for (int i = 0;
        i < locations.size() - 1;
        i++) { // for every child, add make the locations on this floor visible
      if (locations.get(i).getFloor().equals(floor)) {
        lineLayer.getChildren().get(i).setVisible(true);
      }
    }
  }
}
