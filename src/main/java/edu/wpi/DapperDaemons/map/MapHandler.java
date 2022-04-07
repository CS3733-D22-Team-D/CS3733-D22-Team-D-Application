package edu.wpi.DapperDaemons.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.image.ImageView;

public class MapHandler {

  private List<ImageView> maps;
  private int currentMap;

  /** Handles several map icons */
  public MapHandler(ImageView... maps) {
    this.maps = new ArrayList<>(Arrays.asList(maps));
    currentMap = 0;
  }

  /** Sets only a specific map visible and the rest not */
  public void setOnlyVisible(int mapNum) {
    maps.forEach(map -> map.setVisible(false));
    maps.get(mapNum).setVisible(true);
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

  public int getFloor() {
    return currentMap;
  }
}
