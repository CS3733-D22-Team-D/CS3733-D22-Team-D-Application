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
import javafx.scene.control.Control;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
      VBox item = new VBox();
      item.setAlignment(Pos.CENTER_LEFT);
      HBox.setHgrow(item, Priority.ALWAYS);
      item.setPrefHeight(50);
      item.setMinHeight(Control.USE_PREF_SIZE);
      item.setMaxHeight(Control.USE_PREF_SIZE);
      item.setPadding(new Insets(0, 0, 0, 30));
      if (attr instanceof Node) row.getChildren().add((Node) attr);
      if (attr instanceof Enum) {
        Enum<?> e = (Enum<?>) attr;
        List<String> allAttrs = TableHelper.convertEnum(e.getClass());
        ComboBox<String> box = new ComboBox<>(FXCollections.observableArrayList(allAttrs));
        box.setValue(attr.toString());
        box.setBackground(Background.EMPTY);
        box.setMaxWidth(100);
        box.setMinWidth(100);
        box.getEditor()
            .setFont(
                Font.font(
                    box.getEditor().getFont().getFamily(),
                    FontWeight.BOLD,
                    box.getEditor().getFont().getSize()));
        item.getChildren().add(box);
      } else {
        Text text = new Text(attr.toString());
        text.setFont(
            Font.font(text.getFont().getFamily(), FontWeight.BOLD, text.getFont().getSize()));
        item.getChildren().add(text);
      }
      row.getChildren().add(item);
    }
    List<Node> ret = new ArrayList<>();
    ret.addAll(row.getChildren());
    return ret;
  }
}
