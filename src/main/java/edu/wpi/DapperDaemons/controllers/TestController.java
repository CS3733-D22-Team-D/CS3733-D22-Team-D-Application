package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.*;
import edu.wpi.DapperDaemons.backend.preload.Images;
import edu.wpi.DapperDaemons.entities.requests.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class TestController extends ParentController {
  @FXML private ImageView floorLL1;
  @FXML private ImageView floorLL2;
  @FXML private ImageView floor1;
  @FXML private ImageView floor2;
  @FXML private ImageView floor3;
  @FXML private ImageView floor4;
  @FXML private ImageView floor5;
  @FXML private Text floorNumberLabel;
  @FXML private Label floorSummary;
  @FXML private Pane mapContainer;
  @FXML private ImageView mapImage;
  public static List<Boolean> floorsInAnimation =
      new ArrayList<Boolean>(Arrays.asList(new Boolean[7]));
  public static List<Boolean> isSelected = new ArrayList<Boolean>(Arrays.asList(new Boolean[7]));
  private final double ANIMATION_TIME = 0.3;

  private int floorNum = 2;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Collections.fill(floorsInAnimation, Boolean.FALSE);
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

    initSlide(floorLL2, 0);
    initSlide(floorLL1, 1);
    initSlide(floor1, 2);
    initSlide(floor2, 3);
    initSlide(floor3, 4);
    initSlide(floor4, 5);
    initSlide(floor5, 6);
  }

  private void initSlide(ImageView floor, int level) {
    floor.setOnMouseEntered(
        event -> {
          TestController.isSelected.set(level, true);
          if (!TestController.floorsInAnimation.get(level)) {
            floorNum = level;
            floor.setImage(Images.selectedSegment);
            TranslateTransition slide = new TranslateTransition();
            TestController.floorsInAnimation.set(level, true);
            slide.setDuration(Duration.seconds(ANIMATION_TIME));
            slide.setNode(floor);

            slide.setToX(20);
            slide.setToY(floor.getTranslateY() + 23);
            slide.play();

            slide.setOnFinished(
                (ActionEvent e) -> {
                  try {
                    String floorTxtPath = "floorSummary.txt";
                    String floorText = getFileText(floorTxtPath, floorNum);
                    floorSummary.setText(floorText);
                    floorNumberLabel.setText(getFloor());
                    mapImage.setImage(getImage());
                  } catch (IOException ex) {
                    ex.printStackTrace();
                  }

                  TestController.floorsInAnimation.set(level, false);

                  if (!TestController.isSelected.get(level)) {
                    TranslateTransition slideBack = new TranslateTransition();
                    TestController.floorsInAnimation.set(level, true);
                    slideBack.setDuration(Duration.seconds(ANIMATION_TIME));
                    slideBack.setNode(floor);

                    slideBack.setToX(0);
                    slideBack.setToY(floor.getTranslateY() - 23);
                    slideBack.play();

                    slideBack.setOnFinished(
                        (ActionEvent e2) -> {
                          floor.setImage(Images.floorSegment);
                          TestController.floorsInAnimation.set(level, false);
                        });
                  }
                });
          }
        });

    floor.setOnMouseExited(
        event -> {
          if (TestController.isSelected.get(level)
              && !TestController.floorsInAnimation.get(level)) {
            TranslateTransition slideBack = new TranslateTransition();
            slideBack.setDuration(Duration.seconds(ANIMATION_TIME));
            slideBack.setNode(floor);

            slideBack.setToX(0);
            slideBack.setToY(floor.getTranslateY() - 23);
            slideBack.play();

            slideBack.setOnFinished(
                (ActionEvent e2) -> {
                  floor.setImage(Images.floorSegment);
                  TestController.floorsInAnimation.set(level, true);
                });
          }
          TestController.isSelected.set(level, false);
        });
  }

  private String getFloor() {
    switch (floorNum) {
      case 0:
        return "LL2";
      case 1:
        return "LL1";
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
