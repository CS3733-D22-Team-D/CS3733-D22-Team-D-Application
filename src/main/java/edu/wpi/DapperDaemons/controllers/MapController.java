package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import edu.wpi.DapperDaemons.entities.Patient;
import edu.wpi.DapperDaemons.map.GlyphHandler;
import edu.wpi.DapperDaemons.map.LocationInfo;
import edu.wpi.DapperDaemons.map.MapHandler;
import edu.wpi.DapperDaemons.map.PositionInfo;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/** Controller Class for interactive Map Page */
public class MapController extends UIController implements Initializable {

  /* UI Assets */
  @FXML private ImageView mapFloor1;
  @FXML private ImageView mapFloor2;
  @FXML private ImageView mapFloor3;
  @FXML private ImageView mapFloorG;
  @FXML private ImageView mapFloorL1;
  @FXML private ImageView mapFloorL2;
  @FXML private AnchorPane glyphsLayer;
  @FXML private AnchorPane pinPane;
  @FXML private StackPane mapAssets;

  /* Labels for Room Information */
  @FXML private TextField floorLabel;
  @FXML private TextField nameLabel;
  @FXML private TextField nodeTypeLabel;
  @FXML private TextField buildingLabel;

  /* Create Location */
  @FXML private Label createTitle;
  @FXML private Label selectLocationText;
  @FXML private VBox createBox;
  @FXML private TextField roomNameIn;
  @FXML private TextField roomNumberIn;
  @FXML private JFXComboBox<String> typeIn;

  /* Map Handlers */
  private MapHandler maps;
  private GlyphHandler glyphs;
  public final double ZOOM_PROP = 0.025;
  private boolean createActive = false;
  private ImageView pin;
  private PositionInfo selected;

  /* Database stuff */
  private DAO<Location> dao;
  private DAO<MedicalEquipment> equipmentDAO;
  private DAO<Patient> patientDAO;
  private List<PositionInfo> iconPositions;

  /* Info Assets */
  @FXML private Button closeButton;
  @FXML private VBox roomInfoBox;

  @FXML private ImageView equipList;
  @FXML private ImageView personList;
  @FXML private ImageView requestList;
  @FXML private VBox tableContainer;
  @FXML private TableView<MedicalEquipment> listTable;
  private TableHelper<MedicalEquipment> equipHelper;

  // TODO: Initialize table with a DAO<Location>, fill values automagically
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    try {
      dao = new DAO<>(new Location());
      equipmentDAO = new DAO<>(new MedicalEquipment());
      patientDAO = new DAO<>(new Patient());
      iconPositions = new ArrayList<>();
      dao.getAll().forEach(l -> iconPositions.add(new PositionInfo(l)));
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("DAO could not be created in MapController\n");
    }
    this.maps = new MapHandler(mapFloorL2, mapFloorL1, mapFloorG, mapFloor1, mapFloor2, mapFloor3);
    maps.setMap(mapFloor1);

    this.glyphs = new GlyphHandler(glyphsLayer, iconPositions, this);
    glyphs.filterByFloor("1");

    pin = new ImageView(glyphs.GLYPH_PATH + "pin.png");
    pinPane.getChildren().add(pin);
    pin.setVisible(false);

    equipHelper = new TableHelper<MedicalEquipment>(listTable, 1);
    equipHelper.linkColumns(MedicalEquipment.class);

    disableCreateLocationPopup();
    disableRoomInfoPopup();
  }

  /**
   * Runs on mouse click, finds the nearest location within 20 pixels and displays it to a text box
   *
   * @param click UI action
   */
  @FXML
  public void onMapClicked(MouseEvent click) {
    if (!createActive) {
      PositionInfo pos = getPositionInfo((int) click.getX(), (int) click.getY());
      if (pos == null || !click.isStillSincePress()) {
        closeRoomInfoPopUp();
        tableContainer.setVisible(false);
      } else {
        // System.out.println("Clicking on: (" + (int) click.getX() + ", " + (int) click.getY() +
        // ")"); // For debug
        List<MedicalEquipment> equipment = new ArrayList<>();
        try {
          equipment = equipmentDAO.filter(6, pos.getId());
        } catch (Exception e) {
          System.err.println("Could not filter equipmentDAO");
        }
        listTable.getItems().addAll(equipment);
        System.out.println(equipment);
        LocationInfo lloc = new LocationInfo(pos);
        this.selected = pos;
        initRoomInfoPopUp();
        // System.out.println("Location Found!");
        nameLabel.setText(lloc.getLongName());
        floorLabel.setText(lloc.getFloor());
        nodeTypeLabel.setText(lloc.getNodeType());
        buildingLabel.setText(lloc.getBuilding());
      }
    } else {
      selectLocationText.setText("Valid location selected");

      pin.setX((int) click.getX() - 16);
      pin.setY((int) click.getY() - 32);

      pin.setVisible(true);
    }
  }

  /** Removes all data from the popup window when the close button is pressed */
  @FXML
  public void onRoomInfoClose() {
    disableRoomInfoPopup();
    tableContainer.setVisible(false);
  }

  /** Initializes the popup for room info */
  @FXML
  public void initRoomInfoPopUp() {
    // Enable Vbox
    roomInfoBox.setVisible(true);
    // Add Text for title
  }
  /** Closes the popup for room info */
  private void closeRoomInfoPopUp() {
    nameLabel.setText("");
    floorLabel.setText("");
    nodeTypeLabel.setText("");
    roomInfoBox.setVisible(false);
  }

  /** Disables the pop-up for room info */
  @FXML
  public void disableRoomInfoPopup() {
    // Disable Vbox
    roomInfoBox.setVisible(false);
    // Remove Text
    nameLabel.setText("");
    floorLabel.setText("");
    nodeTypeLabel.setText("");
  }

  @FXML
  public void openCreateLocation() {
    if (!createActive) {
      initCreateLocationPopUp();
      createActive = true;
    }
  }

  /** Removes all data from the create popup window when the close button is pressed */
  @FXML
  public void onCreateLocationClose() {
    disableCreateLocationPopup();
  }

  @FXML
  void onSubmitCreate() {
    if (!roomNameIn.getText().trim().equals("")
        && !roomNumberIn.getText().trim().equals("")
        && !typeIn.getValue().equals("")
        && !selectLocationText.getText().equals("Please select a valid location")) {
      int xcoord = (int) pin.getX();
      int ycoord = (int) pin.getY();
      String floor = getFloor(maps.getFloor());
      String building = "TOWER";
      String nodeType = typeIn.getValue();
      String longName = roomNameIn.getText();
      String shortName;
      if (longName.length() > 3) {
        shortName = floor + roomNameIn.getText().substring(0, 3);
      } else {
        shortName = floor + longName;
      }
      String nodeID = building + shortName + nodeType;

      Location create =
          new Location(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName);
      try {
        dao.add(create);
        PositionInfo p = new PositionInfo(create);
        glyphs.addPosition(p);
        iconPositions.add(p);
        disableCreateLocationPopup();
      } catch (Exception e) {
        selectLocationText.setText("Create Location Error");
      }
    }
  }

  @FXML
  public void onConfirmChanges() {
    if (!nameLabel.getText().trim().equals("")
        && !nodeTypeLabel.getText().trim().equals("")
        && !buildingLabel.getText().trim().equals("")) {
      String type = nodeTypeLabel.getText();
      String building = buildingLabel.getText();
      String longName = nameLabel.getText();
      String shortName;
      if (longName.length() > 3) {
        shortName = longName.substring(0, 3);
      } else shortName = longName;
      Location editedLoc =
          new Location(
              selected.getId(),
              selected.getX(),
              selected.getY(),
              selected.getFloor(),
              building,
              type,
              longName,
              shortName);
      try {
        //        dao.delete(selected.getLoc());
        //        dao.add(editedLoc);
        dao.update(editedLoc);
        PositionInfo p = new PositionInfo(editedLoc);
        glyphs.remove(selected);
        glyphs.addPosition(p);
        iconPositions.add(p);
      } catch (Exception e) {
        System.err.println("Location could not be updated");
      }
      closeRoomInfoPopUp();
    }
  }

  /** Initializes the popup for room info */
  @FXML
  public void initCreateLocationPopUp() {
    // Enable Vbox
    createBox.setVisible(true);
    // Add Text for title
    createTitle.setText("Create Location");
  }

  /** Disables the pop-up for create location */
  @FXML
  public void disableCreateLocationPopup() {
    createBox.setVisible(false);
    createActive = false;
    roomNameIn.setText("");
    roomNumberIn.setText("");
    typeIn.setValue("");
    selectLocationText.setText("Please select a valid location");
    pin.setVisible(false);
  }

  @FXML
  public void zoomIn(MouseEvent click) {
    zoom(ZOOM_PROP * 3);
  }

  @FXML
  public void zoomOut(MouseEvent click) {
    zoom(-ZOOM_PROP * 3);
  }

  @FXML
  public void scrollMap(ScrollEvent scroll) {
    zoom(ZOOM_PROP * scroll.getDeltaY() / scroll.getMultiplierY());
    closeRoomInfoPopUp();
    scroll.consume();
  }

  private void zoom(double value) {
    mapAssets.setScaleX(mapAssets.getScaleX() + value);
    mapAssets.setScaleY(mapAssets.getScaleY() + value);
  }

  @FXML
  public void setFloor1(MouseEvent event) {
    maps.setMap(mapFloor1);
    glyphs.filterByFloor("1");
  }

  @FXML
  public void setFloor2(MouseEvent event) {
    maps.setMap(mapFloor2);
    glyphs.filterByFloor("2");
  }

  @FXML
  public void setFloor3(MouseEvent event) {
    maps.setMap(mapFloor3);
    glyphs.filterByFloor("3");
  }

  @FXML
  public void setFloorG(MouseEvent event) {
    maps.setMap(mapFloorG);
    glyphs.filterByFloor("G");
  }

  @FXML
  public void setFloorL1(MouseEvent event) {
    maps.setMap(mapFloorL1);
    glyphs.filterByFloor("L1");
  }

  @FXML
  public void setFloorL2(MouseEvent event) {
    maps.setMap(mapFloorL2);
    glyphs.filterByFloor("L2");
  }

  @FXML
  public void onDeleteLocation(MouseEvent event) {
    try {
      dao.delete(selected.getLoc());
      glyphs.remove(selected);
    } catch (Exception e) {
      System.err.println("Failed to remove location.");
    }
    selected = null;
    closeRoomInfoPopUp();
  }

  /**
   * Searches array list of positions for a nearby location
   *
   * @param x position to look on x-axis
   * @param y position to look on y-axis
   * @return The nearby position info, null otherwise
   */
  private PositionInfo getPositionInfo(int x, int y) {
    for (PositionInfo p : iconPositions) {
      if (p.isNear(x, y, getFloor(maps.getFloor()))) return p;
    }
    return null;
  }

  /**
   * Gets the floor num based on the mapNum
   *
   * @param mapNum floor int
   * @return floor name as string
   */
  private String getFloor(int mapNum) {
    switch (mapNum) {
      case 0:
        return "L2";
      case 1:
        return "L1";
      case 2:
        return "G";
      case 3:
        return "1";
      case 4:
        return "2";
      case 5:
        return "3";
      default:
        return "ERROR";
    }
  }

  @FXML
  void showEquipList(MouseEvent event) {
    if (tableContainer.isVisible()) tableContainer.setVisible(false);
    else tableContainer.setVisible(true);
  }

  @FXML
  void showPersonList(MouseEvent event) {
    tableContainer.setVisible(true);
  }

  @FXML
  void showReqList(MouseEvent event) {
    tableContainer.setVisible(true);
  }
}
