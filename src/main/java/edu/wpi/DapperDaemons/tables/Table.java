package edu.wpi.DapperDaemons.tables;

import edu.wpi.DapperDaemons.entities.TableObject;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class Table {

  private Table() {}

  public static void setHeader(HBox header, List<String> labels) {
    for (String label : labels) {
      header.getChildren().add(new Text(label));
    }
  }

  public static void addRow(GridPane table, TableObject type, int tableNum, int rowNum) {
    //EF5353 - RED
    //F5EC42 - YELLOW
    //69FF69 - GREEN
    List<Node> row = RowFactory.createRow(type, tableNum);
    if (row.size() > 0) {
      ((VBox) row.get(row.size() - 1))
          .setStyle("-fx-background-color: FFFEFE;" + "-fx-background-radius: 0 9 9 0;");
    }
    table.addRow(rowNum, row.toArray(new Node[] {}));
    ColumnConstraints c = new ColumnConstraints();
    c.setFillWidth(true);
    c.setHgrow(Priority.ALWAYS);
    table.getColumnConstraints().add(c);
    table.getColumnConstraints().get(0).setHgrow(Priority.NEVER);
    table.getColumnConstraints().get(0).setMaxWidth(((HBox) row.get(0)).getPrefWidth());
  }
}
