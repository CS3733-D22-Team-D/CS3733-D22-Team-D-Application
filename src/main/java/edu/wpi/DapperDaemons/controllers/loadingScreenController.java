package edu.wpi.DapperDaemons.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class loadingScreenController extends AppController {

  @FXML ImageView background;
  @FXML Pane pane;

  public void initialize() {
    bindImage(background, pane);
  }
}
