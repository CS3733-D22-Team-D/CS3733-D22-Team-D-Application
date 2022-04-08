package edu.wpi.DapperDaemons.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class MapHandler {

  private StackPane mapAssets;
  private List<ImageView> maps;
  private int currentMap;
  public final double ZOOM_PROP = 0.025;

  /** Handles several map icons */
  public MapHandler(StackPane mapPane, ImageView... maps) {
    this.maps = new ArrayList<>(Arrays.asList(maps));
    currentMap = 0;
    this.mapAssets = mapPane;
  }

  public void setMap(ImageView map) {
    maps.forEach(m -> m.setVisible(false));
    map.setVisible(true);
    //    map.setScaleX(getCurrentMap().getScaleX());
    //    map.setScaleY(getCurrentMap().getScaleY());
    currentMap = maps.indexOf(map);
  }

  public ImageView getCurrentMap() {
    return maps.get(currentMap);
  }

  public void resetScales() {
    maps.forEach(
        map -> {
          map.setScaleX(1);
          map.setScaleY(1);
        });
  }

  public String getFloor() {
    return mapNumToString(currentMap);
  }

  /**
   * Gets the floor num based on the mapNum
   *
   * @param mapNum floor int
   * @return floor name as string
   */
  private String mapNumToString(int mapNum) {
    switch (mapNum) {
      case 0:
        return "L2";
      case 1:
        return "L1";
      case 2:
        return "1";
      case 3:
        return "2";
      case 4:
        return "3";
      case 5:
        return "4";
      case 6:
        return "5";
      default:
        return "ERROR";
    }
  }

  public void zoom(double multiplier) {
    mapAssets.setScaleX(mapAssets.getScaleX() + (ZOOM_PROP * multiplier));
    mapAssets.setScaleY(mapAssets.getScaleY() + (ZOOM_PROP * multiplier));
  }
}
