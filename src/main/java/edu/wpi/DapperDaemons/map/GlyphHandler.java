package edu.wpi.DapperDaemons.map;

import edu.wpi.DapperDaemons.controllers.MapController;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
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

  public GlyphHandler(
      AnchorPane glyphLayer, List<PositionInfo> imageLocs, MapController controller) {
    this.glyphLayer = glyphLayer;
    this.controller = controller;
    this.imageLocs = new ArrayList<>();
    imageLocs.forEach(this::addPosition);
    this.imageLocs = imageLocs;
  }

  public void addPosition(PositionInfo pos) {
    ImageView image = getIconImage(pos.getType());
    image.setVisible(true);
    image.setX(pos.getX() - 16);
    image.setY(pos.getY() - 16);
    image.setOnMouseClicked(e -> controller.onMapClicked(e));
    glyphLayer.getChildren().add(image);
    imageLocs.add(pos);
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
    borderGlow.setColor(Color.web("0x012D5A").brighter());
    borderGlow.setOffsetX(0f);
    borderGlow.setOffsetY(0f);
    node.setEffect(borderGlow);
    node.setScaleX(node.getScaleX() + 1);
    node.setScaleY(node.getScaleY() + 1);
  }

  public void deselect() {
    if (selected != null && imageLocs.contains(selected)) {
      Node node = glyphLayer.getChildren().get(imageLocs.indexOf(selected));
      node.setEffect(null);
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

  public void filterByFloor(String floor) {
    makeAllInVisible();
    for (int i = 0; i < imageLocs.size(); i++) {
      if (imageLocs.get(i).getFloor().equals(floor)) {
        glyphLayer.getChildren().get(i).setVisible(true);
      }
    }
  }

  public void filterByDisplay(String floor, String nodeType) {
    makeAllInVisible();
    for (int i = 0; i < imageLocs.size(); i++) {
      if (imageLocs.get(i).getFloor().equals(floor)
          && imageLocs.get(i).getType().equals(nodeType)) {
        glyphLayer.getChildren().get(i).setVisible(true);
      }
    }
  }
}
