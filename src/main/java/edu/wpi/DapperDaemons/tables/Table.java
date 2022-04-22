package edu.wpi.DapperDaemons.tables;

import edu.wpi.DapperDaemons.entities.TableObject;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class Table {

  private Table() {}

  public static int getRowIndexAsInteger(Node node) {
    final var a = GridPane.getRowIndex(node);
    if (a == null) {
      return 0;
    }
    return a;
  }

  public static void removeRow(GridPane grid, Integer targetRowIndexIntegerObject) {
    final int targetRowIndex =
        targetRowIndexIntegerObject == null ? 0 : targetRowIndexIntegerObject;

    // Remove children from row
    grid.getChildren().removeIf(node -> getRowIndexAsInteger(node) == targetRowIndex);

    // Update indexes for elements in further rows
    grid.getChildren()
        .forEach(
            node -> {
              final int rowIndex = getRowIndexAsInteger(node);
              if (targetRowIndex < rowIndex) {
                GridPane.setRowIndex(node, rowIndex - 1);
              }
            });
  }

  public static void setHeader(HBox header, List<String> labels) {
    for (String label : labels) {
      header.getChildren().add(new Text(label));
    }
  }

  public static void removeRow(GridPane table, TableObject type, int tableNum, int rowNum) {
    int numCols = TableHelper.getNumCols(type, tableNum);
    table.getChildren().remove(rowNum * numCols, rowNum * numCols + numCols);
  }

  public static List<Node> getRow(GridPane table, int rowNum) {
    List<Node> ret = new ArrayList<>();
    table
        .getChildren()
        .forEach(
            node -> {
              if (getRowIndexAsInteger(node) == rowNum) {
                ret.add(node);
              }
            });
    return ret;
  }

  public static int addRow(GridPane table, TableObject type, int tableNum, int rowNum) {
    // EF5353 - RED
    // F5EC42 - YELLOW
    // 69FF69 - GREEN
    List<Node> row = RowFactory.createRow(type, tableNum);
    if (row.size() > 0) {
      ((VBox) row.get(0))
          .setStyle("-fx-background-color: FFFEFE;" + "-fx-background-radius: 10 0 0 10;");
    }
    table.addRow(rowNum, row.toArray(new Node[] {}));
    ColumnConstraints c = new ColumnConstraints();
    c.setFillWidth(true);
    c.setHgrow(Priority.ALWAYS);
    table.getColumnConstraints().add(c);
    table.getColumnConstraints().get(0).setHgrow(Priority.NEVER);
    table
        .getColumnConstraints()
        .get(0)
        .setMaxWidth(((HBox) row.get(row.size() - 1)).getPrefWidth());
    List<Node> r = getRow(table, rowNum);
    Platform.runLater(
        () -> {
          for (Node node : r) {
            node.setEffect(new ColorAdjust(0.9, 0.5, 0.5, 0.3));
          }
          new Thread(
                  () -> {
                    try {
                      Thread.sleep(5000);
                    } catch (InterruptedException e) {
                      throw new RuntimeException(e);
                    }
                    Platform.runLater(
                        () -> {
                          for (Node node : r) {
                            node.setEffect(null);
                          }
                        });
                  })
              .start();
        });
    return table.getRowCount() - 1;
  }
}
