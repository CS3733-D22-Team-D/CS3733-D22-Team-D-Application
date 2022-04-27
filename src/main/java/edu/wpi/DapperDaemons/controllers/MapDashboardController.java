package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.*;
import edu.wpi.DapperDaemons.backend.preload.Images;
import edu.wpi.DapperDaemons.entities.requests.*;
import edu.wpi.DapperDaemons.tables.Table;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MapDashboardController extends ParentController {
  @FXML private GridPane table;
  @FXML private ImageView floorLL1;
  @FXML private ImageView floorLL2;
  @FXML private ImageView floor1;
  @FXML private ImageView floor2;
  @FXML private ImageView floor3;
  @FXML private ImageView floor4;
  @FXML private ImageView floor5;
  @FXML private Pane LL2Container;
  @FXML private Pane LL1Container;
  @FXML private Pane OneContainer;
  @FXML private Pane TwoContainer;
  @FXML private Pane ThreeContainer;
  @FXML private Pane FourContainer;
  @FXML private Pane FiveContainer;
  @FXML private Text floorNumberLabel;
  @FXML private Label floorSummary;
  @FXML private Pane mapContainer;
  @FXML private ImageView mapImage;
  public static String floor;

  @FXML private PieChart pumpChart;
  @FXML private PieChart xrayChart;
  @FXML private PieChart reclinerChart;
  @FXML private PieChart bedChart;

  public static List<ImageView> floorList = new ArrayList<ImageView>();
  public static List<PieChart.Data> cleanData = new ArrayList<PieChart.Data>();
  public static List<PieChart.Data> dirtyData = new ArrayList<PieChart.Data>();
  public static List<Boolean> floorsInAnimation =
      new ArrayList<Boolean>(Arrays.asList(new Boolean[7]));
  public static List<Boolean> isHovered = new ArrayList<Boolean>(Arrays.asList(new Boolean[7]));
  public static List<Boolean> isSelected = new ArrayList<Boolean>(Arrays.asList(new Boolean[7]));
  private final double ANIMATION_TIME = 0.2;

  private Table<Request> t;
  private int floorNum = 2;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    PieChart.Data cleanPumps = new PieChart.Data("clean", 45);
    PieChart.Data dirtyPumps = new PieChart.Data("dirty", 55);
    PieChart.Data cleanXray = new PieChart.Data("clean", 80);
    PieChart.Data dirtyXray = new PieChart.Data("dirty", 20);
    PieChart.Data cleanRecliner = new PieChart.Data("clean", 87);
    PieChart.Data dirtyRecliner = new PieChart.Data("dirty", 13);
    PieChart.Data cleanBed = new PieChart.Data("clean", 30);
    PieChart.Data dirtyBed = new PieChart.Data("dirty", 70);

    cleanData.addAll(List.of(cleanPumps, cleanXray, cleanRecliner, cleanBed));
    dirtyData.addAll(List.of(dirtyPumps, dirtyXray, dirtyRecliner, dirtyBed));

    ObservableList<PieChart.Data> pumpData =
        FXCollections.observableArrayList(cleanPumps, dirtyPumps);
    ObservableList<PieChart.Data> xrayData =
        FXCollections.observableArrayList(cleanXray, dirtyXray);
    ObservableList<PieChart.Data> reclinerData =
        FXCollections.observableArrayList(cleanRecliner, dirtyRecliner);
    ObservableList<PieChart.Data> bedData = FXCollections.observableArrayList(cleanBed, dirtyBed);
    pumpChart.setData(pumpData);
    xrayChart.setData(xrayData);
    reclinerChart.setData(reclinerData);
    bedChart.setData(bedData);

    for (PieChart.Data data : cleanData) {
      data.getNode().setStyle("-fx-pie-color: #F1F0F0;");
    }

    for (PieChart.Data data : dirtyData) {
      if (data.getPieValue() < 34) {
        data.getNode().setStyle("-fx-pie-color: #79DE79;");
      } else if (data.getPieValue() < 67) {
        data.getNode().setStyle("-fx-pie-color: #F5EC42;");
      } else {
        data.getNode().setStyle("-fx-pie-color: #FF4C43;");
      }
    }

    floorNum = 2;
    floor = getFloor();
    Collections.fill(floorsInAnimation, Boolean.FALSE);
    Collections.fill(isHovered, Boolean.FALSE);
    Collections.fill(isSelected, Boolean.FALSE);
    try {
      String floorTxtPath = "floorSummary.txt";
      String floorText = getFileText(floorTxtPath, floorNum);
      floorSummary.setText(floorText);
      floorNumberLabel.setText(getFloor());
      mapImage.setImage(getImage());
      bindImage(mapImage, mapContainer);
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    bindImage(floorLL2, LL2Container);
    bindImage(floorLL1, LL1Container);
    bindImage(floor1, OneContainer);
    bindImage(floor2, TwoContainer);
    bindImage(floor3, ThreeContainer);
    bindImage(floor4, FourContainer);
    bindImage(floor5, FiveContainer);

    floorList.addAll(List.of(floorLL2, floorLL1, floor1, floor2, floor3, floor4, floor5));

    initSlide(floorLL2, 0);
    initSlide(floorLL1, 1);
    initSlide(floor1, 2);
    initSlide(floor2, 3);
    initSlide(floor3, 4);
    initSlide(floor4, 5);
    initSlide(floor5, 6);

    createTable();
  }

  private void initSlide(ImageView floor, int level) {
    floor.setOnMouseEntered(
        event -> {
          slideOut(floor, level);
        });

    floor.setOnMouseExited(
        event -> {
          if (MapDashboardController.isHovered.get(level)
              && !MapDashboardController.floorsInAnimation.get(level)
              && !MapDashboardController.isSelected.get(level)) {
            returnToStack(floor, level);
          }
          MapDashboardController.isHovered.set(level, false);
        });

    floor.setOnMouseClicked(
        event -> {
          try {
            floorNum = level;
            for (int i = 0; i < 7; i++) {
              ImageView fromList = floorList.get(i);
              if (fromList.getTranslateX() == 20 && i != level) {
                returnToStack(fromList, i);
                MapDashboardController.isSelected.set(i, false);
                MapDashboardController.isHovered.set(i, false);
              }
            }

            if (floor.getTranslateX() == 0) {
              slideOut(floor, level);
            }

            Collections.fill(isSelected, Boolean.FALSE);
            MapDashboardController.isSelected.set(level, true);
            floor.setImage(Images.selectedSegment);
            String floorTxtPath = "floorSummary.txt";
            String floorText = getFileText(floorTxtPath, floorNum);
            floorSummary.setText(floorText);
            floorNumberLabel.setText(getFloor());
            mapImage.setImage(getImage());
            setFloor(getFloor());
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        });
  }

  private void returnToStack(ImageView floor, int level) {
    MapDashboardController.floorsInAnimation.set(level, true);
    TranslateTransition slideBack = new TranslateTransition();
    slideBack.setDuration(Duration.seconds(ANIMATION_TIME));
    slideBack.setNode(floor);
    slideBack.setToX(0);
    slideBack.setToY(floor.getTranslateY() - 23);
    slideBack.play();
    slideBack.setOnFinished(
        (ActionEvent e2) -> {
          floor.setImage(Images.floorSegment);
          MapDashboardController.floorsInAnimation.set(level, false);
        });
  }

  private void slideOut(ImageView floor, int level) {
    if (!MapDashboardController.floorsInAnimation.get(level)
        && !MapDashboardController.isSelected.get(level)) {
      MapDashboardController.isHovered.set(level, true);
      floor.setImage(Images.hoveredSegment);
      TranslateTransition slide = new TranslateTransition();
      MapDashboardController.floorsInAnimation.set(level, true);
      slide.setDuration(Duration.seconds(ANIMATION_TIME));
      slide.setNode(floor);
      slide.setToX(20);
      slide.setToY(floor.getTranslateY() + 23);
      slide.play();
      slide.setOnFinished(
          (ActionEvent e) -> {
            MapDashboardController.floorsInAnimation.set(level, false);
            if (!MapDashboardController.isHovered.get(level)) {
              returnToStack(floor, level);
            }
          });
    }
  }

  private void setFloor(String floor) {
    this.floor = floor;
    t.clear();
    t.setRows(DAOFacade.getRequestsByFloor(floor));
  }

  private void createTable() {
    t = new Table<>(Request.class, table, 1, 0);
    t.setRows(DAOFacade.getRequestsByFloor(floor));
    t.setHeader(List.of("Type", "Assignee", "Priority"));
    t.setDashboardListeners();
  }

  private String getFloor() {
    switch (floorNum) {
      case 0:
        return "L2";
      case 1:
        return "L1";
      case 2:
        return "1";
      case 3:
        return "2";
      case 4:
        return "3";
      case 5:
        return "4";
      case 6:
        return "5";
      default:
        return "ERROR";
    }
  }

  public Image getImage() {
    switch (floorNum) {
      case 0:
        return Images.floorDashL2;
      case 1:
        return Images.floorDashL1;
      case 2:
        return Images.floorDash1;
      case 3:
        return Images.floorDash2;
      case 4:
        return Images.floorDash3;
      case 5:
        return Images.floorDash4;
      case 6:
        return Images.floorDash5;
      default:
        return null;
    }
  }

  private static String getFileText(String filePath, int line) throws IOException {
    InputStreamReader f =
        new InputStreamReader(
            Objects.requireNonNull(CSVLoader.class.getClassLoader().getResourceAsStream(filePath)));
    BufferedReader reader = new BufferedReader(f);
    // filePath.replace("%20", " ")
    Scanner s = new Scanner(reader);
    int l = 0;
    String current;
    while (s.hasNextLine()) {
      current = s.nextLine();
      if (l == line) return current;
      l++;
    }
    return "";
  }
}
