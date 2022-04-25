package edu.wpi.DapperDaemons.backend.preload;

import edu.wpi.DapperDaemons.backend.AutoSave;
import edu.wpi.DapperDaemons.backend.SecurityController;
import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class Images {

  private static final String ASSETS = "edu/wpi/DapperDaemons/assets/";
  private static final String MAP_PATH =
      Images.class.getClassLoader().getResource(ASSETS + "Maps") + "/";

  private Images() {}

  public static void init() {
    EMBEDDED =
        new Image(
            Objects.requireNonNull(
                Images.class
                    .getClassLoader()
                    .getResourceAsStream(ASSETS + "serverIcons/embedded.png")));
    SERVER =
        new Image(
            Objects.requireNonNull(
                Images.class
                    .getClassLoader()
                    .getResourceAsStream(ASSETS + "serverIcons/server.png")));
    CLOUD =
        new Image(
            Objects.requireNonNull(
                Images.class
                    .getClassLoader()
                    .getResourceAsStream(ASSETS + "serverIcons/cloud.png")));
    LOAD =
        new Image(
            Objects.requireNonNull(
                Images.class
                    .getClassLoader()
                    .getResourceAsStream("edu/wpi/DapperDaemons/assets/loading.gif")));

    UNREAD =
        new Image(
            Objects.requireNonNull(
                Images.class
                    .getClassLoader()
                    .getResourceAsStream("edu/wpi/DapperDaemons/assets/Glyphs/bellNotif.png")));

    BELL =
        new Image(
            Objects.requireNonNull(
                Images.class
                    .getClassLoader()
                    .getResourceAsStream("edu/wpi/DapperDaemons/assets/Glyphs/bell.png")));

    AUTOSAVE =
        new Image(
            Objects.requireNonNull(
                AutoSave.class
                    .getClassLoader()
                    .getResourceAsStream("edu/wpi/DapperDaemons/assets/refresh.gif")));

    floorSegment =
        new Image(
            Objects.requireNonNull(
                AutoSave.class
                    .getClassLoader()
                    .getResourceAsStream(
                        "edu/wpi/DapperDaemons/assets/MapDash/floorLayers/floorlayer.png")));

    selectedSegment =
        new Image(
            Objects.requireNonNull(
                AutoSave.class
                    .getClassLoader()
                    .getResourceAsStream(
                        "edu/wpi/DapperDaemons/assets/MapDash/floorLayers/floorlayerSelected.png")));

    mapFloorL2 = new Image(MAP_PATH + "00_thelowerlevel1.png");
    mapFloorL1 = new Image(MAP_PATH + "00_thelowerlevel2.png");
    mapFloor1 = new Image(MAP_PATH + "01_thefirstfloor.png");
    mapFloor2 = new Image(MAP_PATH + "02_thesecondfloor.png");
    mapFloor3 = new Image(MAP_PATH + "03_thethirdfloor.png");
    mapFloor4 = new Image(MAP_PATH + "04_thefourthfloor.png");
    mapFloor5 = new Image(MAP_PATH + "05_thefifthfloor.png");
  }

  public static Image EMBEDDED;
  public static Image SERVER;
  public static Image CLOUD;
  public static Image LOAD;
  public static Image UNREAD;
  public static Image BELL;
  public static Image AUTOSAVE;

  public static Image mapFloorL2;
  public static Image mapFloorL1;
  public static Image mapFloor1;
  public static Image mapFloor2;
  public static Image mapFloor3;
  public static Image mapFloor4;
  public static Image mapFloor5;

  public static Image floorSegment;
  public static Image selectedSegment;

  public static ImagePattern getAccountImage() {
    try {
      return new ImagePattern(
          new Image(
              Objects.requireNonNull(
                  Images.class
                      .getClassLoader()
                      .getResourceAsStream(
                          "edu/wpi/DapperDaemons/profilepictures/"
                              + SecurityController.getUser().getNodeID()
                              + ".png"))));
    } catch (NullPointerException e) {
      return new ImagePattern(
          new Image(
              Objects.requireNonNull(
                  Images.class
                      .getClassLoader()
                      .getResourceAsStream("edu/wpi/DapperDaemons/profilepictures/wwrong2.png"))));
    }
  }
}
