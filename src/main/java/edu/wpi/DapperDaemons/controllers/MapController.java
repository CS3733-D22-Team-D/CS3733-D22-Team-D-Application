package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import edu.wpi.DapperDaemons.entities.Patient;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.map.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/** Controller Class for interactive Map Page */
public class MapController extends UIController implements Initializable {

  /* UI Assets */
  @FXML private ImageView mapView;
  public final String MAP_PATH =
      getClass().getClassLoader().getResource("edu/wpi/DapperDaemons/assets/Maps") + "/";
  @FXML private AnchorPane glyphsLayer;
  @FXML private AnchorPane pinPane;
  @FXML private StackPane mapAssets;

  /* Map Filter */
  @FXML private StackPane mapFilter;
  @FXML private VBox filterMenu;
  @FXML private VBox directionsFields;
  @FXML private ToggleButton filterButton;
  @FXML private ImageView carrotBack;
  @FXML private ImageView carrotOut;
  @FXML private ToggleButton deptTG;
  @FXML private ToggleButton dirtTG;
  @FXML private ToggleButton elevTG;
  @FXML private ToggleButton exitTG;
  @FXML private ToggleButton hallTG;
  @FXML private ToggleButton infoTG;
  @FXML private ToggleButton labsTG;
  @FXML private ToggleButton patiTG;
  @FXML private ToggleButton restTG;
  @FXML private ToggleButton retlTG;
  @FXML private ToggleButton servTG;
  @FXML private ToggleButton staiTG;
  @FXML private ToggleButton storTG;
  @FXML private ToggleButton directionTG;

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
  private final DAO<Location> locationDAO = DAOPouch.getLocationDAO();
  private final DAO<MedicalEquipment> equipmentDAO = DAOPouch.getMedicalEquipmentDAO();
  private final DAO<Patient> patientDAO = DAOPouch.getPatientDAO();

  /* Info Assets */
  @FXML private VBox tableContainer;

  /* Request filter stuff */
  @FXML private TextField searchLongName;
  @FXML private JFXComboBox<String> searchReqLongName;

  // TODO: Initialize table with a DAO<Location>, fill values automagically
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    mapFilter.setTranslateX(160);
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
      locationDAO.getAll().forEach(l -> origPositions.add(new PositionInfo(l)));
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
    glyphs.setFloorFilter("1");

    this.positions = new PositionHandler(origPositions);

    this.pin = new PinHandler(new ImageView(glyphs.GLYPH_PATH + "pin.png"), pinPane);

    this.infoBox =
        new RoomInfoBox(
            roomInfoBox, tableContainer, nameLabel, floorLabel, nodeTypeLabel, buildingLabel);
    this.createLocation =
        new CreateBox(createBox, roomNameIn, roomNumberIn, typeIn, selectLocationText);

    // Initialize search request box
    //    searchReqLongName.setItems(
    //        FXCollections.observableArrayList(
    //            "Search Request...",
    //            "Lab Request",
    //            "Meal Delivery Request",
    //            "Medical Equipment Request",
    //            "Medicine Request",
    //            "Patient Transport Request",
    //            "Sanitation Request"));

    // searchLongName.setOnKeyPressed(e -> onSearchLocation());
    // searchReqLongName.setOnAction(e -> onFilterRequestType());
    closeCreate();
    closeRoom();

    //    filterSlider(mapFilter, burg, burgBack);
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
    System.out.println("Location " + x + " " + y + " clicked!");

    System.out.println(x + " " + y);

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
        locationDAO.add(create);
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
        locationDAO.update(editedLoc);
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
      locationDAO.delete(positions.getSelected().getLoc());
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
  void onFilterRequestType() {
    try {
      RequestHandler reqHelper = new RequestHandler();
      List<Request> searchReq =
          reqHelper.getSearchedRequestsByLongName(searchReqLongName.getValue());
      glyphs.filterByReqType(maps.getFloor(), searchReq);
    } catch (Exception e) {
      System.out.println("Error in search by request type");
    }
  }

  @FXML
  void onSearchLocation() {
    try {
      RequestHandler reqHelper = new RequestHandler();
      glyphs.searchByLongName(maps.getFloor(), searchLongName.getText());
    } catch (Exception e) {
      System.out.println("Error in search location");
    }
  }

  @FXML
  public void setFloor1(MouseEvent event) {
    maps.setMap("1");
    glyphs.setFloorFilter("1");
  }

  @FXML
  public void setFloor2(MouseEvent event) {
    maps.setMap("2");
    glyphs.setFloorFilter("2");
  }

  @FXML
  public void setFloor3(MouseEvent event) {
    maps.setMap("3");
    glyphs.setFloorFilter("3");
  }

  @FXML
  public void setFloor4(MouseEvent event) {
    maps.setMap("4");
    glyphs.setFloorFilter("4");
  }

  @FXML
  public void setFloor5(MouseEvent event) {
    maps.setMap("5");
    glyphs.setFloorFilter("5");
  }

  @FXML
  public void setFloorL1(MouseEvent event) {
    maps.setMap("L1");
    glyphs.setFloorFilter("L1");
  }

  @FXML
  public void setFloorL2(MouseEvent event) {
    maps.setMap("L2");
    glyphs.setFloorFilter("L2");
  }

  @FXML
  public void filterSlider() {
    //    mapFilter.setTranslateX(160);

    if (filterButton.isSelected()) {
      TranslateTransition slide = new TranslateTransition();
      slide.setDuration(Duration.seconds(0.4));
      slide.setNode(mapFilter);

      slide.setToX(0);
      slide.play();

      mapFilter.setTranslateX(160);

      slide.setOnFinished(
          (ActionEvent e) -> {
            carrotOut.setVisible(false);
            carrotBack.setVisible(true);
          });
    } else {
      TranslateTransition slide = new TranslateTransition();
      slide.setDuration(Duration.seconds(0.4));
      slide.setNode(mapFilter);

      slide.setToX(160);
      slide.play();

      mapFilter.setTranslateX(0);

      slide.setOnFinished(
          (ActionEvent e) -> {
            carrotOut.setVisible(true);
            carrotBack.setVisible(false);
          });
    }
  }

  @FXML
  void deptToggle(ActionEvent event) {
    if (deptTG.isSelected()) {
      glyphs.addNodeTypeFilter("DEPT");
    } else {
      glyphs.removeNodeTypeFilter("DEPT");
    }
  }

  @FXML
  void dirtToggle(ActionEvent event) {
    if (dirtTG.isSelected()) {
      glyphs.addNodeTypeFilter("DIRT");
    } else {
      glyphs.removeNodeTypeFilter("DIRT");
    }
  }

  @FXML
  void elevToggle(ActionEvent event) {
    if (elevTG.isSelected()) {
      glyphs.addNodeTypeFilter("ELEV");
    } else {
      glyphs.removeNodeTypeFilter("ELEV");
    }
  }

  @FXML
  void exitToggle(ActionEvent event) {
    if (exitTG.isSelected()) {
      glyphs.addNodeTypeFilter("EXIT");
    } else {
      glyphs.removeNodeTypeFilter("EXIT");
    }
  }

  @FXML
  void hallToggle(ActionEvent event) {
    if (hallTG.isSelected()) {
      glyphs.addNodeTypeFilter("HALL");
    } else {
      glyphs.removeNodeTypeFilter("HALL");
    }
  }

  @FXML
  void infoToggle(ActionEvent event) {
    if (infoTG.isSelected()) {
      glyphs.addNodeTypeFilter("INFO");
    } else {
      glyphs.removeNodeTypeFilter("INFO");
    }
  }

  @FXML
  void labsToggle(ActionEvent event) {
    if (labsTG.isSelected()) {
      glyphs.addNodeTypeFilter("LABS");
    } else {
      glyphs.removeNodeTypeFilter("LABS");
    }
  }

  @FXML
  void patiToggle(ActionEvent event) {
    if (patiTG.isSelected()) {
      glyphs.addNodeTypeFilter("PATI");
    } else {
      glyphs.removeNodeTypeFilter("PATI");
    }
  }

  @FXML
  void restToggle(ActionEvent event) {
    if (restTG.isSelected()) {
      glyphs.addNodeTypeFilter("REST");
      glyphs.addNodeTypeFilter("BATH");
    } else {
      glyphs.removeNodeTypeFilter("REST");
      glyphs.removeNodeTypeFilter("BATH");
    }
  }

  @FXML
  void retlToggle(ActionEvent event) {
    if (retlTG.isSelected()) {
      glyphs.addNodeTypeFilter("RETL");
    } else {
      glyphs.removeNodeTypeFilter("RETL");
    }
  }

  @FXML
  void servToggle(ActionEvent event) {
    if (servTG.isSelected()) {
      glyphs.addNodeTypeFilter("SERV");
    } else {
      glyphs.removeNodeTypeFilter("SERV");
    }
  }

  @FXML
  void staiToggle(ActionEvent event) {
    if (staiTG.isSelected()) {
      glyphs.addNodeTypeFilter("STAI");
    } else {
      glyphs.removeNodeTypeFilter("STAI");
    }
  }

  @FXML
  void storToggle(ActionEvent event) {
    if (storTG.isSelected()) {
      glyphs.addNodeTypeFilter("STOR");
    } else {
      glyphs.removeNodeTypeFilter("STOR");
    }
  }

  @FXML
  void dirToggle(ActionEvent event) {
    if (directionTG.isSelected()) {
      try {
        directionsFields =
            FXMLLoader.load(
                Objects.requireNonNull(App.class.getResource("views/" + "directionsSearch.fxml")));
      } catch (IOException e) {
        e.printStackTrace();
      }
      filterMenu.getChildren().add(directionsFields);
    } else {
      filterMenu.getChildren().remove(2);
    }
  }

  @FXML
  void showCarrot(MouseEvent event) {
    FadeTransition ft = new FadeTransition(Duration.millis(100), filterButton);
    ft.setFromValue(0.0);
    ft.setToValue(1.0);
    ft.setCycleCount(1);
    ft.setAutoReverse(false);

    ft.play();
  }

  @FXML
  void hideCarrot(MouseEvent event) {
    FadeTransition ft = new FadeTransition(Duration.millis(100), filterButton);
    ft.setFromValue(1.0);
    ft.setToValue(0.0);
    ft.setCycleCount(1);
    ft.setAutoReverse(false);

    ft.play();
  }
}
