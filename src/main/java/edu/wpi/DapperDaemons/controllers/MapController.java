package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import edu.wpi.DapperDaemons.entities.Patient;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.map.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/** Controller Class for interactive Map Page */
public class MapController extends UIController implements Initializable {

  /* UI Assets */
  @FXML private ImageView mapView;
  public final String MAP_PATH =
      getClass().getClassLoader().getResource("edu/wpi/DapperDaemons/assets/Maps") + "/";
  @FXML private AnchorPane glyphsLayer;
  @FXML private AnchorPane pinPane;
  @FXML private StackPane mapAssets;

  /* Labels for Room Information */
  private RoomInfoBox infoBox;
  @FXML private VBox roomInfoBox;
  @FXML private TextField floorLabel;
  @FXML private TextField nameLabel;
  @FXML private TextField nodeTypeLabel;
  @FXML private TextField buildingLabel;

  /* Create Location */
  private CreateBox createLocation;
  @FXML private Label selectLocationText;
  @FXML private VBox createBox;
  @FXML private TextField roomNameIn;
  @FXML private TextField roomNumberIn;
  @FXML private JFXComboBox<String> typeIn;

  /* Map Handlers */
  private MapHandler maps;
  private GlyphHandler glyphs;
  private PositionHandler positions;
  private PinHandler pin;

  /* Database stuff */
  private final DAO<Location> dao = DAOPouch.getLocationDAO();
  private final DAO<MedicalEquipment> equipmentDAO = DAOPouch.getMedicalEquipmentDAO();
  private final DAO<Patient> patientDAO = DAOPouch.getPatientDAO();

  /* Info Assets */
  @FXML private VBox tableContainer;

  // TODO: Initialize table with a DAO<Location>, fill values automagically
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Image mapFloorL2 = new Image(MAP_PATH + "00_thelowerlevel1.png");
    Image mapFloorL1 = new Image(MAP_PATH + "00_thelowerlevel2.png");
    Image mapFloor1 = new Image(MAP_PATH + "01_thefirstfloor.png");
    Image mapFloor2 = new Image(MAP_PATH + "02_thesecondfloor.png");
    Image mapFloor3 = new Image(MAP_PATH + "03_thethirdfloor.png");
    Image mapFloor4 = new Image(MAP_PATH + "04_thefourthfloor.png");
    Image mapFloor5 = new Image(MAP_PATH + "05_thefifthfloor.png");
    super.initialize(location, resources);
    List<PositionInfo> origPositions = new ArrayList<>();
    // Initialize DAO objects
    try {
      dao.getAll().forEach(l -> origPositions.add(new PositionInfo(l)));
    } catch (Exception e) {
      System.err.println("DAO could not be created in MapController\n");
    }

    this.maps =
        new MapHandler(
            mapAssets,
            mapView,
            mapFloorL2,
            mapFloorL1,
            mapFloor1,
            mapFloor2,
            mapFloor3,
            mapFloor4,
            mapFloor5);
    maps.setMap("1");

    this.glyphs = new GlyphHandler(glyphsLayer, origPositions, this);
    glyphs.filterByFloor("1");

    this.positions = new PositionHandler(origPositions);

    this.pin = new PinHandler(new ImageView(glyphs.GLYPH_PATH + "pin.png"), pinPane);

    this.infoBox =
        new RoomInfoBox(
            roomInfoBox, tableContainer, nameLabel, floorLabel, nodeTypeLabel, buildingLabel);
    this.createLocation =
        new CreateBox(createBox, roomNameIn, roomNumberIn, typeIn, selectLocationText);

    closeCreate();
    closeRoom();
  }

  /**
   * Runs on mouse click, finds the nearest location within 20 pixels and displays it to a text box
   *
   * @param click UI action
   */
  @FXML
  public void onMapClicked(MouseEvent click) {
    // Stops drag clicking
    if (!click.isStillSincePress()) return;

    // Location of click
    int x = (int) click.getX();
    int y = (int) click.getY();
    String floor = maps.getFloor();

    // Check if clicking should place pins
    if (createBox.isVisible()) {
      pin.move(x, y);
      createLocation.select();
      return;
    }

    PositionInfo pos = positions.get(x, y, floor);
    glyphs.deselect();
    // Close tabs if nothing selected
    if (pos == null) {
      infoBox.close();
      return;
    }
    glyphs.select(pos);

    // Gather data of location
    List<MedicalEquipment> equipment = new ArrayList<>();
    List<Patient> patients = new ArrayList<>();
    List<Request> requests = new LinkedList<>();
    RequestHandler reqHelper = new RequestHandler();
    try {
      equipment = equipmentDAO.filter(6, pos.getId());
      patients = patientDAO.filter(6, pos.getId());
      requests = reqHelper.getFilteredRequests(pos.getId());
    } catch (Exception e) {
      System.err.println("Could not filter through DAO");
    }
    System.out.println(patients);
    infoBox.openLoc(pos, equipment, patients, requests);
    infoBox.open();
  }

  /** Disables the pop-up for room info */
  @FXML
  public void closeRoom() {
    glyphs.deselect();
    infoBox.close();
  }

  @FXML
  public void openCreate() {
    createLocation.open();
  }

  /** Disables the pop-up for create location */
  @FXML
  public void closeCreate() {
    createLocation.close();
    pin.clear();
  }

  @FXML
  void onSubmitCreate() {
    if (createLocation.allFilled()) {
      Location create = createLocation.create(pin.getX(), pin.getY(), maps.getFloor());
      try {
        dao.add(create);
        PositionInfo p = new PositionInfo(create);
        glyphs.addPosition(p);
        closeCreate();
      } catch (Exception e) {
        System.err.println("Could not add to DAO");
      }
    }
  }

  @FXML
  public void onConfirmChanges() {
    if (infoBox.allFilled()) {
      Location editedLoc = infoBox.change(positions.getSelected());
      try {
        dao.update(editedLoc);
        glyphs.update(positions.getSelected(), new PositionInfo(editedLoc));
      } catch (Exception e) {
        System.err.println("Location could not be updated");
      }
      infoBox.close();
    }
  }

  @FXML
  public void zoomIn(MouseEvent click) {
    maps.zoom(3);
  }

  @FXML
  public void zoomOut(MouseEvent click) {
    maps.zoom(-3);
  }

  @FXML
  public void scrollMap(ScrollEvent scroll) {
    maps.zoom(scroll.getDeltaY() / scroll.getMultiplierY());
    glyphs.deselect();
    infoBox.close();
    scroll.consume();
  }

  @FXML
  public void onDeleteLocation(MouseEvent event) {
    try {
      dao.delete(positions.getSelected().getLoc());
      glyphs.remove(positions.getSelected());
    } catch (Exception e) {
      System.err.println("Failed to remove location.");
    }
    infoBox.close();
  }

  @FXML
  void showEquipList(MouseEvent event) {
    infoBox.toggleTable(RoomInfoBox.TableDisplayType.EQUIPMENT);
  }

  @FXML
  void showPersonList(MouseEvent event) {
    infoBox.toggleTable(RoomInfoBox.TableDisplayType.PATIENT);
  }

  @FXML
  void showReqList(MouseEvent event) {
    infoBox.toggleTable(RoomInfoBox.TableDisplayType.REQUEST);
  }

  @FXML
  public void setFloor1(MouseEvent event) {
    maps.setMap("1");
    glyphs.filterByFloor("1");
  }

  @FXML
  public void setFloor2(MouseEvent event) {
    maps.setMap("2");
    glyphs.filterByFloor("2");
  }

  @FXML
  public void setFloor3(MouseEvent event) {
    maps.setMap("3");
    glyphs.filterByFloor("3");
  }

  @FXML
  public void setFloor4(MouseEvent event) {
    maps.setMap("4");
    glyphs.filterByFloor("4");
  }

  @FXML
  public void setFloor5(MouseEvent event) {
    maps.setMap("5");
    glyphs.filterByFloor("5");
  }

  @FXML
  public void setFloorL1(MouseEvent event) {
    maps.setMap("L1");
    glyphs.filterByFloor("L1");
  }

  @FXML
  public void setFloorL2(MouseEvent event) {
    maps.setMap("L2");
    glyphs.filterByFloor("L2");
  }

  @FXML
  public void toggleEXIT(MouseEvent event) {
    glyphs.filterByDisplay(maps.getFloor(), "EXIT");
  }

  @FXML
  public void toggleDEPT(MouseEvent event) {
    glyphs.filterByDisplay(maps.getFloor(), "DEPT");
  }

  @FXML
  public void toggleHALL(MouseEvent event) {
    glyphs.filterByDisplay(maps.getFloor(), "HALL");
  }

  @FXML
  public void toggleINFO(MouseEvent event) {
    glyphs.filterByDisplay(maps.getFloor(), "INFO");
  }

  @FXML
  public void toggleLABS(MouseEvent event) {
    glyphs.filterByDisplay(maps.getFloor(), "LABS");
  }

  @FXML
  public void toggleREST(MouseEvent event) {
    glyphs.filterByDisplay(maps.getFloor(), "REST");
  }

  @FXML
  public void toggleBATH(MouseEvent event) {
    glyphs.filterByDisplay(maps.getFloor(), "BATH");
  }

  @FXML
  public void toggleRETL(MouseEvent event) {
    glyphs.filterByDisplay(maps.getFloor(), "RETL");
  }

  @FXML
  public void toggleSERV(MouseEvent event) {
    glyphs.filterByDisplay(maps.getFloor(), "SERV");
  }

  @FXML
  public void toggleSTAI(MouseEvent event) {
    glyphs.filterByDisplay(maps.getFloor(), "STAI");
  }

  @FXML
  public void toggleELEV(MouseEvent event) {
    glyphs.filterByDisplay(maps.getFloor(), "ELEV");
  }

  @FXML
  public void toggleSTOR(MouseEvent event) {
    glyphs.filterByDisplay(maps.getFloor(), "STOR");
  }

  @FXML
  public void togglePATI(MouseEvent event) {
    glyphs.filterByDisplay(maps.getFloor(), "PATI");
  }

  @FXML
  public void toggleDIRT(MouseEvent event) {
    glyphs.filterByDisplay(maps.getFloor(), "DIRT");
  }
}
