package edu.wpi.DapperDaemons.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class AboutUsController extends UIController {
  /* Background */
  @FXML private ImageView BGImage;
  @FXML private Pane BGContainer;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    bindImage(BGImage, BGContainer);
  }
}
