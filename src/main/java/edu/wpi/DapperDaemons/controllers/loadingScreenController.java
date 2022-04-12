package edu.wpi.DapperDaemons.controllers;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class loadingScreenController extends AppController {

  @FXML ImageView loadingIcon;
  @FXML VBox sceneBox;
  @FXML Label loadingLabel;

  private static Timer loading;
  private static Timer backgroundImages;

  private static int ind = 0;

  public final Image LOAD =
      new Image(
          Objects.requireNonNull(
              DefaultController.class
                  .getClassLoader()
                  .getResourceAsStream("edu/wpi/DapperDaemons/assets/loading.gif")));

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    System.out.println("init");
    loadingIcon.setImage(LOAD);
    if (loading != null) loading.cancel();
    loading = new Timer();
    loading.schedule(
        new TimerTask() {
          @Override
          public void run() {
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    loadingLabel.setText("Loading...".substring(0, ind));
                    ind = (ind + 1) % "Loading...".length();
                  }
                });
          }
        },
        0,
        250);
    if (backgroundImages != null) backgroundImages.cancel();
    backgroundImages = new Timer();
    backgroundImages.schedule(
        new TimerTask() {
          @Override
          public void run() {
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    int currImg = (int) (Math.random() * 11) + 1;
                    System.out.println(currImg);
                    sceneBox.setBackground(
                        new Background(
                            new BackgroundImage(
                                new Image(
                                    Objects.requireNonNull(
                                        loadingScreenController
                                            .class
                                            .getClassLoader()
                                            .getResourceAsStream(
                                                "edu/wpi/DapperDaemons/loadingScreen/"
                                                    + currImg
                                                    + ".png"))),
                                BackgroundRepeat.SPACE,
                                BackgroundRepeat.SPACE,
                                BackgroundPosition.CENTER,
                                new BackgroundSize(100, 100, true, true, true, true))));
                  }
                });
          }
        },
        0,
        5000);
  }

  public static void stop() {
    loading.cancel();
    backgroundImages.cancel();
  }
}
