package edu.wpi.DapperDaemons.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class UserSettingsController extends UIController {

  @FXML private Pane BGContainer;
  @FXML private ImageView BGImage;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);

    bindImage(BGImage, BGContainer);
  }
}
