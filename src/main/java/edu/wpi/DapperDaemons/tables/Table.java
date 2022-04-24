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
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Table<R> {

  private List<R> rows = new ArrayList<>();
  private GridPane table;
  private final int tableNum;
  private R instance;

  public Table(GridPane table, int tableNum) {
    this.table = table;
    this.tableNum = tableNum;
  }

  public void setListeners(R type) {
    this.instance = type;
    TableListeners.addListener(
        ((TableObject) type).tableName(),
        TableListeners.eventListener(
            () -> {
              Platform.runLater(
                  () -> {
                    difference(
                        new ArrayList<R>(DAOPouch.getDAO(((TableObject) type)).getAll().values()),
                        rows);
                  });
            }));
  }

  private void difference(List<R> cons, List<R> update) {
    List<R> dif = new ArrayList<>(update);
    for (int i = 0; i < cons.size(); i++) {
      if (!update.contains(cons.get(i))) {
        boolean added = false;
        for (int j = 0; j < update.size(); j++) {
          if (((TableObject) cons.get(i))
              .getAttribute(1)
              .equals(((TableObject) update.get(j)).getAttribute(1))) {
            added = true;
            dif.remove(update.get(j));
            updateRow(update.get(j), cons.get(i));
          }
        }
        if (!added) {
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

  public static int getColumnIndexAsInteger(Node node) {
    final var a = GridPane.getColumnIndex(node);
    if (a == null) {
      return 0;
    }
    return a;
  }

  public void removeRow(R type) {
    final int targetRowIndex = rows.indexOf(type);
    List<Node> r = getRow(targetRowIndex);
    animate(0.92F, 0.25F, 0.11F, r);
    Platform.runLater(
        () -> {
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
        });
    // Remove children from row
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

  public List<Node> getColumn(int colNum) {
    List<Node> ret = new ArrayList<>();
    table
        .getChildren()
        .forEach(
            node -> {
              if (getColumnIndexAsInteger(node) == colNum) {
                ret.add(node);
              }
            });
    return ret;
  }

  public void setRows(List<R> newRows) {
    this.rows = newRows;
    for (R r : newRows) {
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
    rows.remove(old);
    rows.add(targetRowIndex, newObj);
  }

  private void restyleRow(List<Node> row) {
    row.forEach(
        n -> {
          ((VBox) n).setBackground(Background.EMPTY);
          n.setStyle("-fx-background-color: FFFEFE;");
        });
    if (row.size() > 0) {
      row.get(0).setStyle("-fx-background-color: FFFEFE;" + "-fx-background-radius: 10 0 0 10;");
      ((VBox) row.get(0)).setPadding(new Insets(0, 0, 0, 15));
      row.get(row.size() - 1)
          .setStyle("-fx-background-color: FFFEFE;" + "-fx-background-radius: 0 10 10 0;");
      Insets norm = ((VBox) row.get(row.size() - 1)).getPadding();
      ((VBox) row.get(row.size() - 1))
          .setPadding(new Insets(norm.getTop(), 15, norm.getBottom(), norm.getLeft()));
    }
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
    List<Node> row = RowFactory.createRow((TableObject) type, tableNum);
    restyleRow(row);
    table.addRow(ind, row.toArray(new Node[] {}));
    ColumnConstraints c = new ColumnConstraints();
    c.setFillWidth(true);
    c.setHgrow(Priority.ALWAYS);
    table.getColumnConstraints().clear();
    row.forEach(e -> table.getColumnConstraints().add(c));
  }

  public void addRow(R type) {
    List<Node> row = RowFactory.createRow((TableObject) type, tableNum);
    restyleRow(row);
    table.addRow(table.getRowCount(), row.toArray(new Node[] {}));
    ColumnConstraints c = new ColumnConstraints();
    c.setFillWidth(true);
    c.setHgrow(Priority.ALWAYS);
    table.getColumnConstraints().clear();
    row.forEach(e -> table.getColumnConstraints().add(c));
    List<Node> r = getRow(table.getRowCount() - 1);
    if (!rows.contains(type)) {
      rows.add(type);
      animate(0.38F, 1, 0.51F, r);
      new Thread(
              () -> {
                try {
                  Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                System.out.println("RESTLING");
                restyleRow(row);
              })
          .start();
    }
  }

  public void addDropDownEditProperty(int col, int sqlCol, String... elements) {
    List<Node> boxesInCol = getColumn(col);
    boxesInCol.forEach(
        box ->
            box.setOnMouseClicked(
                mouse -> {
                  // Store old value
                  Node old = ((VBox) box).getChildren().get(0);
                  ((VBox) box).getChildren().clear();

                  ComboBox<String> elems = new ComboBox<>();
                  elems.getItems().addAll(elements);
                  elems.setValue(getTextWithin(old));
                  elems.setOnAction(
                      e -> {
                        TableObject item = (TableObject) getItem(getRowIndexAsInteger(box));
                        item.setAttribute(sqlCol, elems.getValue());
                        DAOPouch.getDAO(item).update(item);

                        // Reset
                        ((VBox) box).getChildren().clear();
                        ((VBox) box).getChildren().add(old);
                        editTextWithin(
                            old,
                            ((TableObject) getItem(getRowIndexAsInteger(box)))
                                .getAttribute(sqlCol));
                        addDropDownEditProperty(col, sqlCol, elements);
                      });
                  ((VBox) box).getChildren().add(elems);

                  // Toggle reset line
                  box.setOnMouseClicked(
                      toggle -> {
                        ((VBox) box).getChildren().clear();
                        ((VBox) box).getChildren().add(old);
                        editTextWithin(
                            old,
                            ((TableObject) getItem(getRowIndexAsInteger(box)))
                                .getAttribute(sqlCol));
                        addDropDownEditProperty(col, sqlCol, elements);
                      });
                }));
  }

  public <E extends Enum<E>> void addEnumEditProperty(int col, int sqlCol, Class<E> enumClass) {
    List<Node> boxesInCol = getColumn(col);
    boxesInCol.forEach(
        box -> {
          Node editable = ((VBox) box).getChildren().get(0);
          if (editable instanceof ComboBox) {
            ComboBox<String> editBox = ((ComboBox<String>) editable);
            editBox.setItems(FXCollections.observableArrayList(TableHelper.convertEnum(enumClass)));
            editBox.setOnAction(
                e -> {
                  TableObject item = (TableObject) getItem(getRowIndexAsInteger(box));
                  item.setAttribute(sqlCol, editBox.getValue());
                  DAOPouch.getDAO(item).update(item);
                });
          }
        });
  }

  private void editTextWithin(Node n, String toEdit) {
    if (n instanceof Text) ((Text) n).setText(toEdit);
    else if (n instanceof ComboBox) ((ComboBox<String>) n).setValue(toEdit);
    else if (n instanceof TextField) ((TextField) n).setText(toEdit);
  }

  private String getTextWithin(Node n) {
    if (n instanceof Text) return ((Text) n).getText();
    else if (n instanceof ComboBox) return ((ComboBox<String>) n).getValue();
    else if (n instanceof TextField) return ((TextField) n).getText();
    else return "";
  }

  public R getItem(int row) {
    return rows.get(row);
  }
}
