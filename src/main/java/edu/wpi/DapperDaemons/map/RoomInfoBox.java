package edu.wpi.DapperDaemons.map;

import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import edu.wpi.DapperDaemons.entities.Patient;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.util.List;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class RoomInfoBox {

  public enum TableDisplayType {
    EQUIPMENT,
    PATIENT,
    REQUEST
  }

  private VBox roomInfoBox;
  private VBox infoTables;
  private TextField nameTxt;
  private TextField floorTxt;
  private TextField typeTxt;
  private TextField buildingTxt;
  private TableView<MedicalEquipment> equipTable;
  private TableView<Patient> patientTable;
  private TableView<Request> requestTable;

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

    this.equipTable =
        (TableView<MedicalEquipment>)
            ((StackPane) infoTables.getChildren().get(0)).getChildren().get(0);
    this.patientTable =
        (TableView<Patient>) ((StackPane) infoTables.getChildren().get(0)).getChildren().get(1);
    this.requestTable =
        (TableView<Request>) ((StackPane) infoTables.getChildren().get(0)).getChildren().get(2);

    new TableHelper<>(equipTable, 1).linkColumns(MedicalEquipment.class);
    new TableHelper<>(patientTable, 1).linkColumns(Patient.class);
    new TableHelper<>(requestTable, 1).betterLinkColumns(Request.class);
    /*TableColumn<Request, String> col1 =
        (TableColumn<Request, String>) requestTable.getColumns().get(0);
    TableColumn<Request, String> col2 =
        (TableColumn<Request, String>) requestTable.getColumns().get(1);
    TableColumn<Request, Boolean> col3 =
        (TableColumn<Request, Boolean>) requestTable.getColumns().get(2);

    col1.setCellValueFactory(
        request -> new SimpleStringProperty(request.getValue().getPriority().toString()));
    col2.setCellValueFactory(
        request -> new SimpleStringProperty(request.getValue().getRequestType().toString()));
    col3.setCellValueFactory(
        request -> new SimpleBooleanProperty(request.getValue().requiresTransport()));*/
  }

  public void open() {
    roomInfoBox.setVisible(true);
  }

  public void toggleTable(TableDisplayType type) {
    infoTables.setVisible(true);
    switch (type) {
      case EQUIPMENT:
        if (equipTable.isVisible()) {
          equipTable.setVisible(false);
          infoTables.setVisible(false);
          return;
        }
        equipTable.setVisible(true);
        patientTable.setVisible(false);
        requestTable.setVisible(false);
        break;
      case PATIENT:
        if (patientTable.isVisible()) {
          patientTable.setVisible(false);
          infoTables.setVisible(false);
          return;
        }
        equipTable.setVisible(false);
        patientTable.setVisible(true);
        requestTable.setVisible(false);
        break;
      case REQUEST:
        if (requestTable.isVisible()) {
          requestTable.setVisible(false);
          infoTables.setVisible(false);
          return;
        }
        equipTable.setVisible(false);
        patientTable.setVisible(false);
        requestTable.setVisible(true);
        break;
      default:
        equipTable.setVisible(false);
        patientTable.setVisible(false);
        requestTable.setVisible(false);
    }
  }

  public void close() {
    roomInfoBox.setVisible(false);
    infoTables.setVisible(false);
  }

  public void openLoc(
      PositionInfo pos,
      List<MedicalEquipment> equipment,
      List<Patient> patients,
      List<Request> requests) {
    nameTxt.setText(pos.getLongName());
    floorTxt.setText(pos.getFloor());
    typeTxt.setText(pos.getType());
    buildingTxt.setText(pos.getBuilding());

    equipTable.getItems().clear();
    equipTable.getItems().addAll(equipment);

    patientTable.getItems().clear();
    patientTable.getItems().addAll(patients);

    requestTable.getItems().clear();
    requestTable.getItems().addAll(requests);
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
