package edu.wpi.DapperDaemons.tables;

import java.util.List;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class Table {

  private Table() {}

  public void setHeader(HBox header, List<String> labels) {
    for (String label : labels) {
      header.getChildren().add(new Text(label));
    }
  }
}
