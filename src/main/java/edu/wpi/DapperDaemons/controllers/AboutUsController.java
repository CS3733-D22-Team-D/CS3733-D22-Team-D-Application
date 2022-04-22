package edu.wpi.DapperDaemons.controllers;

import edu.wpi.cs3733.D22.teamD.API.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AboutUsController extends ParentController {

  @FXML private Button apiButton;

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  public void startAPI() throws IOException {
    swapPage("apiLandingPage", "API Landing Page");
    /*
    Stage window = (Stage) apiButton.getScene().getWindow();
    switchScene("apiLandingPage.fxml", 635, 510, window);

     */
  }
}
