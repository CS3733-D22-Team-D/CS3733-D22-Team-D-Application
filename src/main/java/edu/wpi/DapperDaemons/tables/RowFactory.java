package edu.wpi.DapperDaemons.tables;

import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.entities.TableObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class RowFactory {

  private RowFactory() {}

  public static List<Node> createRow(TableObject type, int tableNum) {
    HBox row;
    FXMLLoader loader;
    loader =
        new FXMLLoader(Objects.requireNonNull(App.class.getResource("views/" + "row" + ".fxml")));
    HBox loaded;
    try {
      loaded = loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    row = (HBox) loader.getNamespace().get("contents");
    List<Object> attributes = TableHelper.getDataList(type, tableNum);
    for (Object attr : attributes) {
      VBox toMakeThingsEasier = new VBox();
      toMakeThingsEasier.setAlignment(Pos.CENTER);
      HBox.setHgrow(toMakeThingsEasier, Priority.ALWAYS);
      if (attr instanceof Node) row.getChildren().add((Node) attr);
      if (attr instanceof Enum) {
        Enum<?> e = (Enum<?>) attr;
        List<String> allAttrs = TableHelper.convertEnum(e.getClass());
        ComboBox<String> box = new ComboBox<>(FXCollections.observableArrayList(allAttrs));
        box.setValue(attr.toString());
        box.setBackground(Background.EMPTY);
        box.setMinWidth(Region.USE_COMPUTED_SIZE);
        box.setMinHeight(Region.USE_COMPUTED_SIZE);
        box.setPrefWidth(Region.USE_COMPUTED_SIZE);
        box.setPrefHeight(Region.USE_COMPUTED_SIZE);
        box.setMaxWidth(Region.USE_COMPUTED_SIZE);
        box.setMaxHeight(Region.USE_COMPUTED_SIZE);
        toMakeThingsEasier.getChildren().add(box);
      } else {
        Text text = new Text(attr.toString());
        toMakeThingsEasier.getChildren().add(text);
      }
      toMakeThingsEasier.setBackground(
          new Background(
              new BackgroundFill(Paint.valueOf("FFFEFE"), CornerRadii.EMPTY, Insets.EMPTY)));
      row.getChildren().add(toMakeThingsEasier);
    }
    List<Node> ret = new ArrayList<>();
    ret.add(loaded.getChildren().get(0));
    ret.addAll(row.getChildren());
    return ret;
  }
}
