package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOFacade;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.preload.Images;
import edu.wpi.DapperDaemons.controllers.helpers.AutoCompleteFuzzy;
import edu.wpi.DapperDaemons.controllers.helpers.FuzzySearchComparatorMethod;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import edu.wpi.DapperDaemons.entities.Patient;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.map.*;
import edu.wpi.DapperDaemons.map.pathfinder.PathfinderHandler;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javax.swing.*;

/** Controller Class for interactive Map Page */
public class MapController extends ParentController {

  /* UI Assets */
  @FXML private ImageView mapView;
  @FXML private AnchorPane glyphsLayer;
  @FXML private AnchorPane equipLayer;
  @FXML private AnchorPane dragPane;
  @FXML private AnchorPane pinPane;
  @FXML private StackPane mapAssets;
  @FXML private ScrollPane mapContents;
  @FXML private AnchorPane pathPane;
  @FXML private VBox centerBox;
  @FXML private VBox emptyBox;

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
  @FXML private ToggleButton bedTG;
  @FXML private ToggleButton pumpTG;

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

  /* Drag elements */
  @FXML private ImageView infusionDragImage;
  @FXML private ImageView bedDragImage;
  private static DragHandler bedDrag;
  private static DragHandler infusionDrag;

  @FXML private ToggleButton bubbleMenu;
  @FXML private StackPane circle2;
  @FXML private ToggleButton circle3;
  @FXML private StackPane circle4;

  /* Map Handlers */
  private MapHandler maps;
  private GlyphHandler glyphs;
  private PositionHandler positions;
  private PinHandler pin;

  private PathfinderHandler pathfinder;

  /* Database stuff */
  private final DAO<Location> locationDAO = DAOPouch.getLocationDAO();
  private final DAO<MedicalEquipment> equipmentDAO = DAOPouch.getMedicalEquipmentDAO();
  private final DAO<Patient> patientDAO = DAOPouch.getPatientDAO();

  /* Info Assets */
  @FXML private VBox tableContainer;

  /* Request filter stuff */
  @FXML private JFXComboBox<String> searchBar;

  /*confirm cancel popup*/
  @FXML private VBox confirmPopup;

  @FXML
  public void startFuzzySearch() {
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(searchBar, new FuzzySearchComparatorMethod());
    AutoCompleteFuzzy.autoCompleteComboBoxPlus(typeIn, new FuzzySearchComparatorMethod());
  }
  // TODO: Initialize table with a DAO<Location>, fill values automagically
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    mapFilter.setTranslateX(160);
    //    super.initialize(location, resources);
    //    bindImage(BGImage, BGContainer);
    List<PositionInfo> origPositions = new ArrayList<>();
    // Initialize DAO objects
    try {
      locationDAO.getAll().values().forEach(l -> origPositions.add(new PositionInfo(l)));
    } catch (Exception e) {
      System.err.println("DAO could not be created in MapController\n");
    }

    this.maps =
        new MapHandler(
            mapAssets,
            mapView,
            Images.mapFloorL2,
            Images.mapFloorL1,
            Images.mapFloor1,
            Images.mapFloor2,
            Images.mapFloor3,
            Images.mapFloor4,
            Images.mapFloor5);
    maps.setMap(MapDashboardController.floor);

    this.glyphs = new GlyphHandler(glyphsLayer, equipLayer, origPositions, this);
    glyphs.setFloorFilter(maps.getFloor());

    this.pathfinder = new PathfinderHandler(pathPane, this);

    this.positions = new PositionHandler(origPositions);

    this.pin = new PinHandler(new ImageView(glyphs.GLYPH_PATH + "pin.png"), pinPane);

    this.infoBox =
        new RoomInfoBox(
            roomInfoBox, tableContainer, nameLabel, floorLabel, nodeTypeLabel, buildingLabel);
    this.createLocation =
        new CreateBox(createBox, roomNameIn, roomNumberIn, typeIn, selectLocationText);
    try {
      List<String> allReqNames = new ArrayList<>();
      DAOFacade.getAllRequests()
          .forEach(
              r -> {
                if (!allReqNames.contains(r.requestType())) allReqNames.add(r.requestType());
              });
      searchBar.getItems().addAll(allReqNames);
    } catch (Exception e) {
      searchBar.getItems().setAll(new ArrayList<>());
    }
    searchBar
        .getEditor()
        .setOnKeyPressed(
            e -> {
              // If filtering by request fails, search by location
              if (!onFilterRequestType()) {
                onSearchLocation();
              }
            });
    searchBar.setOnAction(e -> onFilterRequestType());
    closeCreate();
    closeRoom();

    //    filterSlider(mapFilter, burg, burgBack);
  }

  @FXML
  void mapMenu(ActionEvent event) throws InterruptedException {
    if (bubbleMenu.isSelected()) {
      TranslateTransition translateTransition = new TranslateTransition();
      translateTransition.setDuration(Duration.millis(300));
      translateTransition.setNode(circle2);
      translateTransition.setByX(-56);
      translateTransition.play();

      TranslateTransition translateTransition2 = new TranslateTransition();
      translateTransition2.setDuration(Duration.millis(300));
      translateTransition2.setNode(circle3);
      translateTransition2.setByX(-112);
      translateTransition2.play();

      TranslateTransition translateTransition3 = new TranslateTransition();
      translateTransition3.setDuration(Duration.millis(300));
      translateTransition3.setNode(circle4);
      translateTransition3.setByX(-168);
      translateTransition3.play();
    } else {
      TranslateTransition translateTransition = new TranslateTransition();
      translateTransition.setDuration(Duration.millis(300));
      translateTransition.setNode(circle2);
      translateTransition.setByX(56);
      translateTransition.play();

      TranslateTransition translateTransition2 = new TranslateTransition();
      translateTransition2.setDuration(Duration.millis(300));
      translateTransition2.setNode(circle3);
      translateTransition2.setByX(112);
      translateTransition2.play();

      TranslateTransition translateTransition3 = new TranslateTransition();
      translateTransition3.setDuration(Duration.millis(300));
      translateTransition3.setNode(circle4);
      translateTransition3.setByX(168);
      translateTransition3.play();
    }
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
    try {
      equipment = new ArrayList<>(equipmentDAO.filter(6, pos.getId()).values());
      patients = new ArrayList<>(patientDAO.filter(6, pos.getId()).values());
      requests = DAOFacade.getFilteredRequests(pos.getId());
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
  public void zoomIn() {
    maps.zoom(3);
  }

  @FXML
  public void zoomOut() {
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
  public void onDeleteLocation() {
    // confirmation box
    confirmPopup.setVisible(true);
  }

  @FXML
  public void onCancelDelete() {
    // confirmation box
    confirmPopup.setVisible(false);
  }

  @FXML
  public void onConfirmDelete() {
    try {
      locationDAO.delete(positions.getSelected().getLoc());
      glyphs.remove(positions.getSelected());
    } catch (Exception e) {
      System.err.println("Failed to remove location.");
    }
    infoBox.close();
  }

  @FXML
  void showEquipList() {
    infoBox.toggleTable(RoomInfoBox.TableDisplayType.EQUIPMENT);
  }

  @FXML
  void showPersonList() {
    infoBox.toggleTable(RoomInfoBox.TableDisplayType.PATIENT);
  }

  @FXML
  void showReqList() {
    infoBox.toggleTable(RoomInfoBox.TableDisplayType.REQUEST);
  }

  private boolean onFilterRequestType() {
    try {
      List<Request> searchReq = DAOFacade.searchRequestsByName(searchBar.getValue());
      if (searchReq.size() == 0) return false;
      glyphs.filterByReqType(maps.getFloor(), searchReq);
    } catch (Exception e) {
      System.out.println("Error in search by request type");
    }
    return true;
  }

  @FXML
  void onSearchLocation() {
    try {
      glyphs.setLongNameFilter(searchBar.getEditor().getText());
    } catch (Exception e) {
      System.out.println("Error in search location");
    }
  }

  @FXML
  public void setFloor1() {
    maps.setMap("1");
    glyphs.setFloorFilter("1");
    pathfinder.filterByFloor("1");
  }

  @FXML
  public void setFloor2() {
    maps.setMap("2");
    glyphs.setFloorFilter("2");
    pathfinder.filterByFloor("2");
  }

  @FXML
  public void setFloor3() {
    maps.setMap("3");
    glyphs.setFloorFilter("3");
    pathfinder.filterByFloor("3");
  }

  @FXML
  public void setFloor4() {
    maps.setMap("4");
    glyphs.setFloorFilter("4");
    pathfinder.filterByFloor("4");
  }

  @FXML
  public void setFloor5() {
    maps.setMap("5");
    glyphs.setFloorFilter("5");
    pathfinder.filterByFloor("5");
  }

  @FXML
  public void setFloorL1() {
    maps.setMap("L1");
    glyphs.setFloorFilter("L1");
    pathfinder.filterByFloor("L1");
  }

  @FXML
  public void setFloorL2() {
    maps.setMap("L2");
    glyphs.setFloorFilter("L2");
    pathfinder.filterByFloor("L2");
  }

  @FXML
  public void editMode(ActionEvent event) {
    if (circle3.isSelected()) {
      mapContents.setPannable(false);
      glyphs.enableEditing();
      bedDrag = new DragHandler(dragPane, mapAssets, bedDragImage, glyphs);
      infusionDrag = new DragHandler(dragPane, mapAssets, infusionDragImage, glyphs);
      bedDrag.enable();
      infusionDrag.enable();
    } else {
      mapContents.setPannable(true);
      glyphs.disableEditing();
      if (bedDrag != null) bedDrag.disable();
      if (infusionDrag != null) infusionDrag.disable();
    }
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
  void deptToggle() {
    if (deptTG.isSelected()) {
      glyphs.addNodeTypeFilter("DEPT");
    } else {
      glyphs.removeNodeTypeFilter("DEPT");
    }
  }

  @FXML
  void dirtToggle() {
    if (dirtTG.isSelected()) {
      glyphs.addNodeTypeFilter("DIRT");
    } else {
      glyphs.removeNodeTypeFilter("DIRT");
    }
  }

  @FXML
  void elevToggle() {
    if (elevTG.isSelected()) {
      glyphs.addNodeTypeFilter("ELEV");
    } else {
      glyphs.removeNodeTypeFilter("ELEV");
    }
  }

  @FXML
  void exitToggle() {
    if (exitTG.isSelected()) {
      glyphs.addNodeTypeFilter("EXIT");
    } else {
      glyphs.removeNodeTypeFilter("EXIT");
    }
  }

  @FXML
  void hallToggle() {
    if (hallTG.isSelected()) {
      glyphs.addNodeTypeFilter("HALL");
    } else {
      glyphs.removeNodeTypeFilter("HALL");
    }
  }

  @FXML
  void infoToggle() {
    if (infoTG.isSelected()) {
      glyphs.addNodeTypeFilter("INFO");
    } else {
      glyphs.removeNodeTypeFilter("INFO");
    }
  }

  @FXML
  void labsToggle() {
    if (labsTG.isSelected()) {
      glyphs.addNodeTypeFilter("LABS");
    } else {
      glyphs.removeNodeTypeFilter("LABS");
    }
  }

  @FXML
  void patiToggle() {
    if (patiTG.isSelected()) {
      glyphs.addNodeTypeFilter("PATI");
    } else {
      glyphs.removeNodeTypeFilter("PATI");
    }
  }

  @FXML
  void restToggle() {
    if (restTG.isSelected()) {
      glyphs.addNodeTypeFilter("REST");
      glyphs.addNodeTypeFilter("BATH");
    } else {
      glyphs.removeNodeTypeFilter("REST");
      glyphs.removeNodeTypeFilter("BATH");
    }
  }

  @FXML
  void retlToggle() {
    if (retlTG.isSelected()) {
      glyphs.addNodeTypeFilter("RETL");
    } else {
      glyphs.removeNodeTypeFilter("RETL");
    }
  }

  @FXML
  void servToggle() {
    if (servTG.isSelected()) {
      glyphs.addNodeTypeFilter("SERV");
    } else {
      glyphs.removeNodeTypeFilter("SERV");
    }
  }

  @FXML
  void staiToggle() {
    if (staiTG.isSelected()) {
      glyphs.addNodeTypeFilter("STAI");
    } else {
      glyphs.removeNodeTypeFilter("STAI");
    }
  }

  @FXML
  void storToggle() {
    if (storTG.isSelected()) {
      glyphs.addNodeTypeFilter("STOR");
    } else {
      glyphs.removeNodeTypeFilter("STOR");
    }
  }

  @FXML
  void dirToggle() {
    if (directionTG.isSelected()) {
      try {
        directionsFields =
            FXMLLoader.load(
                Objects.requireNonNull(App.class.getResource("views/" + "directionsSearch.fxml")));
        emptyBox.getChildren().add(directionsFields);
        emptyBox.setAlignment(Pos.TOP_LEFT);
        emptyBox.setVisible(true);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      emptyBox.getChildren().remove(directionsFields);
      emptyBox.setVisible(false);
    }
  }

  @FXML
  void bedToggle() {
    if (bedTG.isSelected()) {
      glyphs.addEquipTypeFilter("BED");
    } else {
      glyphs.removeEquipTypeFilter("BED");
    }
  }

  @FXML
  void pumpToggle() {
    if (pumpTG.isSelected()) {
      glyphs.addEquipTypeFilter("INFUSIONPUMP");
    } else {
      glyphs.removeEquipTypeFilter("INFUSIONPUMP");
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

  public String getFloor() {
    return maps.getFloor();
  }
}
