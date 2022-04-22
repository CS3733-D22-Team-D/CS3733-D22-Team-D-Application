package edu.wpi.DapperDaemons.controllers;

import edu.wpi.cs3733.D22.teamD.API.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AboutUsController extends ParentController {

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  /**
   * Allows for access to the API landing page
   * @throws IOException
   */
  public void startAPI() throws IOException {
    swapPage("apiLandingPage", "API Landing Page");
  }
}
