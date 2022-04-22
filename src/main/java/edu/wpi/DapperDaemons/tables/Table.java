package edu.wpi.DapperDaemons.tables;

import edu.wpi.DapperDaemons.entities.TableObject;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Table {

  private Table() {}

  public static void setHeader(HBox header, List<String> labels) {
    for (String label : labels) {
      header.getChildren().add(new Text(label));
    }
  }

  public static void addRow(VBox table, TableObject type, int tableNum) {
    HBox row = RowFactory.createRow(type, tableNum);
    table.getChildren().add((Node) row);
  }
}
