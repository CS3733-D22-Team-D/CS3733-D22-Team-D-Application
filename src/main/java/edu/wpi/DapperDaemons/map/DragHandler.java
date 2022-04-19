package edu.wpi.DapperDaemons.map;

import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class DragHandler {

  private final AnchorPane dragPane;
  private final ImageView draggable;
  private final ScrollPane window;
  private GlyphHandler glyphs;

  private ImageView displayed;

  public DragHandler(
      AnchorPane dragPane, ScrollPane window, ImageView toDragFrom, GlyphHandler glyphs) {
    this.dragPane = dragPane;
    this.draggable = toDragFrom;
    this.window = window;
    this.glyphs = glyphs;
  }

  public void enable() {
    if (displayed != null) dragPane.getChildren().remove(displayed);
    displayed = new ImageView(draggable.getImage());
    displayed.setPickOnBounds(true);
    dragPane.getChildren().add(displayed);
    displayed.setOnMouseDragged(
        e -> {
          displayed.setVisible(true);
          displayed.setX(e.getX() - 16);
          displayed.setY(e.getY() - 16);
        });
    displayed.setOnMouseReleased(
        e -> {
          MedicalEquipment equip =
              new MedicalEquipment(
                  getImageType(),
                  MedicalEquipment.EquipmentType.valueOf(getImageType()),
                  "ID19824",
                  null);
          boolean worked =
              glyphs.addEquipment((int) displayed.getX(), (int) displayed.getY(), equip);
          if (worked) enable();
        });
  }

  private String getImageType() {
    switch (draggable.getId()) {
      case "infusionDragImage":
        return "INFUSIONPUMP";
      case "bedDragImage":
        return "BED";
      default:
        return "ERROR";
    }
  }

  public void disable() {
    dragPane.getChildren().clear();
    displayed = null;
  }
}
