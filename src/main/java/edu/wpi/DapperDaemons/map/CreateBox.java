package edu.wpi.DapperDaemons.map;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.tables.TableHelper;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class CreateBox {

  private VBox box;
  private TextField roomNameTxt;
  private TextField roomNumTxt;
  private JFXComboBox<String> typeBox;
  private Label selectLoc;
  private boolean validLocSelected;

  public CreateBox(
      VBox box,
      TextField roomNameTxt,
      TextField roomNumTxt,
      JFXComboBox<String> typeBox,
      Label selectLoc) {
    this.box = box;
    this.roomNameTxt = roomNameTxt;
    this.roomNumTxt = roomNumTxt;
    this.typeBox = typeBox;
    this.selectLoc = selectLoc;

    typeBox.setEditable(false);
    typeBox.getItems().addAll(TableHelper.convertEnum(PositionInfo.RoomType.class));
  }

  public void close() {
    box.setVisible(false);
  }

  public void open() {
    roomNameTxt.setText("");
    roomNumTxt.setText("");
    typeBox.setValue("");
    selectLoc.setText("Please select a valid location");
    validLocSelected = false;
    box.setVisible(true);
  }

  public void select() {
    selectLoc.setText("Valid location selected");
    validLocSelected = true;
  }

  public boolean isOpen() {
    return box.isVisible();
  }

  public boolean allFilled() {
    return !roomNameTxt.getText().trim().equals("")
        && !roomNumTxt.getText().trim().equals("")
        && !typeBox.getValue().equals("")
        && validLocSelected;
  }

  public Location create(int x, int y, String floor) {
    String building = "TOWER";
    String nodeType = typeBox.getValue();
    String longName = roomNameTxt.getText();
    String shortName;
    if (longName.length() > 3) {
      shortName = floor + roomNameTxt.getText().substring(0, 3);
    } else {
      shortName = floor + longName;
    }
    String nodeID = building + shortName + nodeType;
    return new Location(nodeID, x, y, floor, building, nodeType, longName, shortName);
  }
}
