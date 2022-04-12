package edu.wpi.DapperDaemons.controllers;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class loadingScreenController extends AppController {

  @FXML ImageView background;
  @FXML Pane pane;
  @FXML ImageView loadingIcon;

  public final Image LOAD =
      new Image(
          Objects.requireNonNull(
              DefaultController.class
                  .getClassLoader()
                  .getResourceAsStream("edu/wpi/DapperDaemons/assets/loading.gif")));

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    System.out.println("init");
    bindImage(background, pane);
    loadingIcon.setImage(LOAD);
    Timer backgroundImages = new Timer();
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
                    background.setImage(
                        new Image(
                            Objects.requireNonNull(
                                loadingScreenController
                                    .class
                                    .getClassLoader()
                                    .getResourceAsStream(
                                        "edu/wpi/DapperDaemons/loadingScreen/"
                                            + currImg
                                            + ".png"))));
                  }
                });
          }
        },
        0,
        1000);
  }
}
