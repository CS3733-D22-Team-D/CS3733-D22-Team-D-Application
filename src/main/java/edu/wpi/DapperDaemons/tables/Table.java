package edu.wpi.DapperDaemons.tables;

import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.controllers.helpers.TableListeners;
import edu.wpi.DapperDaemons.entities.TableObject;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Table<R extends TableObject> {

  private List<R> rows = new ArrayList<>();
  private GridPane table;
  private final int tableNum;

  public Table(GridPane table, int tableNum) {
    this.table = table;
    this.tableNum = tableNum;
  }

  public void setListeners(R type) {
    TableListeners.addListener(
        type.tableName(),
        TableListeners.eventListener(
            () -> {
              Platform.runLater(
                  () -> {
                    difference(new ArrayList<R>(DAOPouch.getDAO(type).getAll().values()), rows);
                  });
            }));
  }

  private void difference(List<R> cons, List<R> update) {
    List<R> dif = new ArrayList<>(update);
    for (int i = 0; i < cons.size(); i++) {
      if (!update.contains(cons.get(i))) {
        if (cons.get(i).getAttribute(1).equals(update.get(i).getAttribute(1))) {
          dif.remove(update.get(i));
          updateRow(update.get(i), cons.get(i));
        } else {
          addRow(cons.get(i));
        }
      } else {
        dif.remove(cons.get(i));
      }
    }
    for (R r : dif) {
      removeRow(r);
    }
  }

  public static int getRowIndexAsInteger(Node node) {
    final var a = GridPane.getRowIndex(node);
    if (a == null) {
      return 0;
    }
    return a;
  }

  public void removeRow(R type) {
    final int targetRowIndex = rows.indexOf(type);
    // Remove children from row
    table.getChildren().removeIf(node -> getRowIndexAsInteger(node) == targetRowIndex);

    // Update indexes for elements in further rows
    table
        .getChildren()
        .forEach(
            node -> {
              final int rowIndex = getRowIndexAsInteger(node);
              if (targetRowIndex < rowIndex) {
                GridPane.setRowIndex(node, rowIndex - 1);
              }
            });
    rows.remove(type);
  }

  public void removeChildren(R type) {
    final int targetRowIndex = rows.indexOf(type);
    // Remove children from row
    table.getChildren().removeIf(node -> getRowIndexAsInteger(node) == targetRowIndex);
    rows.remove(type);
  }

  public void setHeader(HBox header, List<String> labels) {
    for (String label : labels) {
      header.getChildren().add(new Text(label));
    }
  }

  public List<Node> getRow(int rowNum) {
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

  public void setRows(List<R> rows) {
    this.rows = rows;
    for (R r : rows) {
      addRow(r);
    }
  }

  public void updateRow(R old, R newObj) {
    final int targetRowIndex = rows.indexOf(old);
    // Remove children from row
    removeChildren(old);
    addRow(targetRowIndex, newObj);
    List<Node> r = getRow(targetRowIndex);
    animate(0.98F, 0.73F, 0.01F, r);
    //    table
    //        .getChildren()
    //        .forEach(
    //            node -> {
    //              if (getRowIndexAsInteger(node) == targetRowIndex) {
    //
    //              }
    //            });
    rows.remove(old);
    rows.add(targetRowIndex, newObj);
  }

  private void animate(float r, float g, float b, List<Node> row) {
    Platform.runLater(
        () -> {
          for (Node node : row) {
            final Animation animation =
                new Transition() {
                  {
                    setCycleDuration(Duration.millis(1500));
                    setInterpolator(Interpolator.EASE_OUT);
                  }

                  @Override
                  protected void interpolate(double frac) {
                    Color vColor = new Color(r, g, b, 1 - frac);
                    ((VBox) node)
                        .setBackground(
                            new Background(
                                new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                  }
                };
            animation.play();
          }
        });
  }

  public void addRow(int ind, R type) {
    List<Node> row = RowFactory.createRow(type, tableNum);
    table.addRow(ind, row.toArray(new Node[] {}));
    ColumnConstraints c = new ColumnConstraints();
    c.setFillWidth(true);
    c.setHgrow(Priority.ALWAYS);
    table.getColumnConstraints().add(c);
    table.getColumnConstraints().get(0).setHgrow(Priority.NEVER);
  }

  public void addRow(R type) {
    List<Node> row = RowFactory.createRow(type, tableNum);
    if (row.size() > 0) {
      ((VBox) row.get(0))
          .setStyle("-fx-background-color: FFFEFE;" + "-fx-background-radius: 10 0 0 10;");
    }
    table.addRow(table.getRowCount(), row.toArray(new Node[] {}));
    ColumnConstraints c = new ColumnConstraints();
    c.setFillWidth(true);
    c.setHgrow(Priority.ALWAYS);
    table.getColumnConstraints().add(c);
    table.getColumnConstraints().get(0).setHgrow(Priority.NEVER);
    List<Node> r = getRow(table.getRowCount() - 1);
    if (!rows.contains(type)) {
      rows.add(type);
      animate(0.38F, 1, 0.51F, r);
    }
  }
}
