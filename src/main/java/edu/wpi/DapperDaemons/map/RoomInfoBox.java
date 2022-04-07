package edu.wpi.DapperDaemons.map;

import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import edu.wpi.DapperDaemons.entities.Patient;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class RoomInfoBox {

  private VBox roomInfoBox;
  private VBox infoTables;
  private TextField nameTxt;
  private TextField floorTxt;
  private TextField typeTxt;
  private TextField buildingTxt;

  public RoomInfoBox(
      VBox roomInfoBox,
      VBox infoTables,
      TextField nameTxt,
      TextField floorTxt,
      TextField typeTxt,
      TextField buildingTxt) {
    this.roomInfoBox = roomInfoBox;
    this.infoTables = infoTables;
    this.nameTxt = nameTxt;
    this.floorTxt = floorTxt;
    this.typeTxt = typeTxt;
    this.buildingTxt = buildingTxt;
  }

  public void open() {
    roomInfoBox.setVisible(true);
  }

  public void toggleTable(int n) {
    Node node = infoTables.getChildren().get(n);
    if (node.isVisible()) node.setVisible(false);
    else node.setVisible(true);
  }

  public void close() {
    roomInfoBox.setVisible(false);
    infoTables.setVisible(false);
  }

  public void openLoc(PositionInfo pos, List<MedicalEquipment> equipment, List<Patient> patients) {
    nameTxt.setText(pos.getLongName());
    floorTxt.setText(pos.getFloor());
    typeTxt.setText(pos.getType());
    buildingTxt.setText(pos.getBuilding());

    TableView<MedicalEquipment> equipTable =
        ((TableView<MedicalEquipment>) infoTables.getChildren().get(0));
    equipTable.getItems().clear();
    equipTable.getItems().addAll(equipment);
  }

  public Location change(PositionInfo selected) {
    String type = typeTxt.getText();
    String building = buildingTxt.getText();
    String longName = nameTxt.getText();
    String shortName;
    if (longName.length() > 3) {
      shortName = longName.substring(0, 3);
    } else shortName = longName;

    return new Location(
        selected.getId(),
        selected.getX(),
        selected.getY(),
        selected.getFloor(),
        building,
        type,
        longName,
        shortName);
  }

  public boolean allFilled() {
    return !(nameTxt.getText().trim().equals("")
        || typeTxt.getText().trim().equals("")
        || floorTxt.getText().trim().equals("")
        || buildingTxt.getText().trim().equals(""));
  }
}
