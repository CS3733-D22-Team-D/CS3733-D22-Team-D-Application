package edu.wpi.DapperDaemons.tables;

import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.entities.TableObject;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class RowFactory {

  private RowFactory() {}

  public static HBox createRow(TableObject type, int tableNum) {
    HBox row = new HBox();
    FXMLLoader loader;
    loader =
        new FXMLLoader(Objects.requireNonNull(App.class.getResource("views/" + "row" + ".fxml")));
    try {
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    row = (HBox) loader.getNamespace().get("contents");
    List<Object> attributes = TableHelper.getDataList(type, tableNum);
    for (Object attr : attributes) {
      if (attr instanceof Node) row.getChildren().add((Node) attr);
      if (attr instanceof Enum) {
        Enum<?> e = (Enum<?>) attr;
        List<String> allAttrs = TableHelper.convertEnum(e.getClass());
        ComboBox<String> box = new ComboBox<>(FXCollections.observableArrayList(allAttrs));
        box.setValue(attr.toString());
        row.getChildren().add(box);
      } else {
        row.getChildren().add(new Text(attr.toString()));
      }
    }
    return row;
  }
}
