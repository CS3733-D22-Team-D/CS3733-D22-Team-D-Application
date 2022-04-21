package edu.wpi.DapperDaemons.controllers;

import edu.wpi.cs3733.D22.teamD.API.*;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutUsController extends ParentController {

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  public void startAPI() {
    StartAPI api = new StartAPI();
    try {
      api.run(0, 0, 500, 800, "edu/wpi/DapperDaemons/assets/themeDark.css", "null");
    } catch (Exception e) {
      System.err.println("API BROKE");
    }
  }
}
