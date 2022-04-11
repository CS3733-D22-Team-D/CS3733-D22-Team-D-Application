package edu.wpi.DapperDaemons.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class UserSettingsController extends UIController {

  @FXML private Pane userBackgroundContainer;
  @FXML private ImageView userSettingsBackground;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);

    bindImage(userSettingsBackground, userBackgroundContainer);
  }
}
