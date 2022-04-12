package edu.wpi.DapperDaemons.map.pathfinder;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;

public class PathfinderHandler {
  @FXML private JFXComboBox<String> toLocation;
  @FXML private JFXComboBox<String> fromLocation;

  AStar ppPlanner;

  public PathfinderHandler() {
    try {
      ppPlanner = new AStar();
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Was unable to find or access the locations or nodeConnections tables");
    }
  }

  @FXML
  public void showPath() {
    if (!toLocation.getValue().isEmpty() && !toLocation.getValue().isEmpty()) {}
  }
}
