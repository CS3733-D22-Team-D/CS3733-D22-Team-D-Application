package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.*;
import edu.wpi.DapperDaemons.entities.requests.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class TestController extends ParentController {

  public static Boolean inAnimation = false;
  @FXML private ImageView floorLL1;
  @FXML private ImageView floorLL2;
  @FXML private ImageView floor1;
  @FXML private ImageView floor2;
  @FXML private ImageView floor3;
  @FXML private ImageView floor4;
  @FXML private ImageView floor5;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initSlide(floorLL2, 6);
    initSlide(floorLL1, 5);
    initSlide(floor1, 4);
    initSlide(floor2, 3);
    initSlide(floor3, 2);
    initSlide(floor4, 1);
    initSlide(floor5, 0);
  }

  private void initSlide(ImageView floor, int level) {
    floor.setOnMouseEntered(
        event -> {
          if (!inAnimation) {
            TranslateTransition slide = new TranslateTransition();
            TestController.inAnimation = true;
            slide.setDuration(Duration.seconds(0.1));
            slide.setNode(floor);

            slide.setToX(20);
            slide.setToY(floor.getTranslateY() + 23);
            slide.setAutoReverse(true);
            slide.play();

            slide.setOnFinished(
                (ActionEvent e) -> {
                  TranslateTransition slideBack = new TranslateTransition();
                  slideBack.setDuration(Duration.seconds(0.1));
                  slideBack.setNode(floor);

                  slideBack.setToX(0);
                  slideBack.setToY(floor.getTranslateY() - 23);
                  slideBack.setAutoReverse(true);
                  slideBack.play();

                  slideBack.setOnFinished(
                      (ActionEvent e2) -> {
                        inAnimation = false;
                      });
                });
          }
        });

    //    floor.setOnMouseExited(
    //        event -> {
    //          TranslateTransition slide = new TranslateTransition();
    //          slide.setDuration(Duration.seconds(0.4));
    //          slide.setNode(floor);
    //
    //          slide.setToX(0);
    //          slide.setToY(floor.getTranslateY() - 23);
    //          slide.setAutoReverse(true);
    //          slide.play();
    //        });
  }
}
