package edu.wpi.DapperDaemons.tables;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

public class TableHelper<R> {

  private final TableView<R> table;
  private ObservableList<TableColumn<R, ?>> columns;
  private int tableNum;

  public TableHelper(TableView<R> jfxTable, int tableNum) {
    this.table = jfxTable;
    this.columns = jfxTable.getColumns();
    jfxTable.getColumns().forEach(e -> e.setReorderable(false));
    table.setEditable(true);

    this.tableNum = tableNum;
  }

  /**
   * Maps a class with associated TableHandler tags to columns Above fields,
   * include @TableHandler(table=#,col=#)
   *
   * @param type - (YOUR_REQUEST).class
   */
  public void linkColumns(Class<R> type) {
    for (Field f : type.getDeclaredFields()) {
      f.setAccessible(true);
      TableHandler[] annotations = f.getAnnotationsByType(TableHandler.class);

      boolean match = false;
      int i;
      for (i = 0; i < annotations.length; i++) {
        if (annotations[i].table() == tableNum) {
          match = true;
          break;
        }
      }

      if (match && annotations[i].col() < columns.size()) {
        columns
            .get(annotations[i].col())
            .setCellValueFactory(new PropertyValueFactory<>(f.getName()));
      }
    }
  }

  /** Makes a column containing Strings editable */
  public void addStringEditProperty(TableColumn<R, String> column) {
    column.setEditable(true);
    column.setCellFactory(TextFieldTableCell.forTableColumn());
  }

  /** Makes a column containing integers editable */
  public void addIntegerEditProperty(TableColumn<R, Integer> column) {
    column.setEditable(true);
    column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
  }

  /** Makes a column containing longs editable */
  public void addLongEditProperty(TableColumn<R, Long> column) {
    column.setEditable(true);
    column.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
  }

  /** Converts an enum into a ComboBox of strings as an edit property of the column */
  public <E extends Enum<E>> void addEnumEditProperty(
      TableColumn<R, String> column, Class<E> enumClass) {
    addDropDownEditProperty(
        column, Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).toArray(String[]::new));
  }

  public static <E extends Enum<E>> List<String> convertEnum(Class<E> e) {
    ArrayList<String> values = new ArrayList<>();
    Arrays.stream(e.getEnumConstants()).map(Enum::name).forEach(values::add);
    return values;
  }

  /** Adds a combo box of values as a drop-down for editing cells of the column */
  public void addDropDownEditProperty(TableColumn<R, String> column, String... values) {
    column.setCellFactory(
        cb -> {
          ComboBoxTableCell<R, String> cellDropDown = new ComboBoxTableCell<>();
          cellDropDown.getItems().addAll(values);
          return cellDropDown;
        });
  }

  /** Filters a column for a specific value within it */
  public <T> void filterTable(TableColumn<R, T> column, T toFilter) {
    ObservableList<R> filteredItems = FXCollections.observableArrayList();
    for (R row : column.getTableView().getItems()) {
      if (column.getCellObservableValue(row).getValue().equals(toFilter)) {
        filteredItems.add(row);
      }
    }
    column.getTableView().setItems(filteredItems);
  }

  // TODO: Refresh data from database
  public void update() {
    table.refresh();
    // Database refresh
  }
}
