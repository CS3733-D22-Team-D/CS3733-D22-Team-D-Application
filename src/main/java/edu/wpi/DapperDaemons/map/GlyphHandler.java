package edu.wpi.DapperDaemons.map;

import edu.wpi.DapperDaemons.controllers.MapController;
import edu.wpi.DapperDaemons.entities.requests.Request;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class GlyphHandler {

  private List<PositionInfo> imageLocs;
  private AnchorPane glyphLayer;
  private MapController controller;
  private PositionInfo selected;
  public final String GLYPH_PATH =
      getClass().getClassLoader().getResource("edu/wpi/DapperDaemons/assets/Glyphs/MapIcons") + "/";

  private List<String> floorFilter;
  private List<String> nodeTypeFilter;
  private List<String> longNameFilter;

  public GlyphHandler(
      AnchorPane glyphLayer, List<PositionInfo> imageLocs, MapController controller) {
    this.glyphLayer = glyphLayer;
    this.controller = controller;
    this.imageLocs = new ArrayList<>();
    imageLocs.forEach(this::addPosition);
    this.imageLocs = imageLocs;
    clearFilters();
  }

  public void addPosition(PositionInfo pos) {
    ImageView image = getIconImage(pos.getType());
    image.setVisible(true);
    image.setX(pos.getX() - 16);
    image.setY(pos.getY() - 16);
    image.setEffect(getPriorityColor(pos));
    image.setOnMouseClicked(e -> controller.onMapClicked(e));
    glyphLayer.getChildren().add(image);
    imageLocs.add(pos);
  }

  private ColorAdjust getPriorityColor(PositionInfo pos) {
    switch (pos.getHighestPriority()) {
      case LOW:
        return new ColorAdjust(0.5, 1, -0.6, 0.5);
      case MEDIUM:
        return new ColorAdjust(0.3333, 1, -0.2, 0.5);
      case HIGH:
        return new ColorAdjust(0, 1, -0.5, 0.5);
    }
    return new ColorAdjust();
  }

  public void remove(PositionInfo pos) {
    int rmIndex = imageLocs.indexOf(pos);
    imageLocs.remove(rmIndex);
    glyphLayer.getChildren().remove(rmIndex);
  }

  public void update(PositionInfo old, PositionInfo next) {
    remove(old);
    addPosition(next);
  }

  public void select(PositionInfo selected) {
    this.selected = selected;
    Node node = glyphLayer.getChildren().get(imageLocs.indexOf(selected));

    DropShadow borderGlow = new DropShadow();
    borderGlow.setColor(Color.web("0x000000").brighter());
    borderGlow.setOffsetX(0f);
    borderGlow.setOffsetY(0f);
    Blend multiEffect = new Blend(BlendMode.SRC_OVER, borderGlow, node.getEffect());
    node.setEffect(multiEffect);
    node.setScaleX(node.getScaleX() + 1);
    node.setScaleY(node.getScaleY() + 1);
  }

  public void deselect() {
    if (selected != null && imageLocs.contains(selected)) {
      Node node = glyphLayer.getChildren().get(imageLocs.indexOf(selected));
      node.setEffect(((Blend) node.getEffect()).getTopInput());
      node.setScaleX(1);
      node.setScaleY(1);
    }
    this.selected = null;
  }

  private ImageView getIconImage(String type) {
    String png = "";
    switch (type) {
      case "DEPT":
        png = "dept.png";
        break;
      case "EXIT":
        png = "exit.png";
        break;
      case "HALL":
        png = "hall.png";
        break;
      case "INFO":
        png = "infoDesk.png";
        break;
      case "LABS":
        png = "laboratory.png";
        break;
      case "REST":
      case "BATH":
        png = "toilet.png";
        break;
      case "RETL":
        png = "retail.png";
        break;
      case "SERV":
        png = "service.png";
        break;
      case "STAI":
        png = "stairs.png";
        break;
      case "ELEV":
        png = "elevator.png";
        break;
      case "STOR":
        png = "storage.png";
        break;
      case "PATI":
        png = "patientRoom.png";
        break;
      case "DIRT":
        png = "dirty.png";
        break;
      default:
        png = "error.png";
    }

    return new ImageView(GLYPH_PATH + png);
  }

  public void makeAllInVisible() {
    glyphLayer.getChildren().forEach(c -> c.setVisible(false));
  }

  public void makeAllVisible() {
    glyphLayer.getChildren().forEach(c -> c.setVisible(true));
  }

  //  public void filterByFloor(String floor) {
  //    makeAllInVisible();
  //    for (int i = 0; i < imageLocs.size(); i++) {
  //      if (imageLocs.get(i).getFloor().equals(floor)) {
  //        glyphLayer.getChildren().get(i).setVisible(true);
  //      }
  //    }
  //  }
  //
  //  public void filterByNodeType(String floor, String nodeType) {
  //    makeAllInVisible();
  //    for (int i = 0; i < imageLocs.size(); i++) {
  //      if (imageLocs.get(i).getFloor().equals(floor)
  //          && imageLocs.get(i).getType().equals(nodeType)) {
  //        glyphLayer.getChildren().get(i).setVisible(true);
  //      }
  //    }
  //  }
  //
  //  public void filterByLongName(String floor, String longName) {
  //    makeAllInVisible();
  //    for (int i = 0; i < imageLocs.size(); i++) {
  //      if (imageLocs.get(i).getFloor().equals(floor)
  //          && imageLocs.get(i).getLongName().equals(longName)) {
  //        glyphLayer.getChildren().get(i).setVisible(true);
  //      }
  //    }
  //  }
  //
  //  public void filterByShortName(String floor, String shortName) {
  //    makeAllInVisible();
  //    for (int i = 0; i < imageLocs.size(); i++) {
  //      if (imageLocs.get(i).getFloor().equals(floor)
  //          && imageLocs.get(i).getShortName().equals(shortName)) {
  //        glyphLayer.getChildren().get(i).setVisible(true);
  //      }
  //    }
  //  }

  public void filterByReqType(String floor, List<Request> searchReq) {
    makeAllInVisible();
    for (int i = 0; i < imageLocs.size(); i++) {
      if (imageLocs.get(i).getFloor().equals(floor)) {
        for (int j = 0; j < searchReq.size(); j++) {
          if (imageLocs.get(i).getId().equals(searchReq.get(j).getRoomID())) {
            glyphLayer.getChildren().get(i).setVisible(true);
          }
        }
      }
    }
  }

  public void searchByLongName(String floor, String search) {
    makeAllInVisible();
    for (int i = 0; i < imageLocs.size(); i++) {
      if (imageLocs.get(i).getFloor().equals(floor)
          && imageLocs.get(i).getLongName().toLowerCase().contains(search.toLowerCase())) {
        glyphLayer.getChildren().get(i).setVisible(true);
      }
    }
  }

  public void filter() {
    makeAllVisible();
    for (int i = 0; i < imageLocs.size(); i++) {
      if (!floorFilter.isEmpty() && !floorFilter.contains(imageLocs.get(i).getFloor())) {
        glyphLayer.getChildren().get(i).setVisible(false);
      }
      if (!nodeTypeFilter.isEmpty() && !nodeTypeFilter.contains(imageLocs.get(i).getType())) {
        glyphLayer.getChildren().get(i).setVisible(false);
      }
      if (!longNameFilter.isEmpty() && !longNameFilter.contains(imageLocs.get(i).getLongName())) {
        glyphLayer.getChildren().get(i).setVisible(false);
      }
    }
  }

  public void setFloorFilter(String floorFilter) {
    this.floorFilter.clear();
    addFloorFilter(floorFilter);
  }

  public void setNodeTypeFilter(String nodeTypeFilter) {
    this.nodeTypeFilter.clear();
    addNodeTypeFilter(nodeTypeFilter);
  }

  public void setLongNameFilter(String longNameFilter) {
    this.longNameFilter.clear();
    addLongNameFilter(longNameFilter);
  }

  public void addFloorFilter(String floorFilter) {
    this.floorFilter.add(floorFilter);
    filter();
  }

  public void addNodeTypeFilter(String nodeTypeFilter) {
    this.nodeTypeFilter.add(nodeTypeFilter);
    filter();
  }

  public void removeNodeTypeFilter(String nodeTypeFilter) {
    this.nodeTypeFilter.remove(nodeTypeFilter);
    filter();
  }

  public void addLongNameFilter(String longNameFilter) {
    this.longNameFilter.add(longNameFilter);
    filter();
  }

  public void clearFilters() {
    floorFilter = new ArrayList<>();
    nodeTypeFilter = new ArrayList<>();
    longNameFilter = new ArrayList<>();
    filter();
  }
}
