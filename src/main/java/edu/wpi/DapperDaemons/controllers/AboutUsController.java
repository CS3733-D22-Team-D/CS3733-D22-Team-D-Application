package edu.wpi.DapperDaemons.controllers;


import edu.wpi.cs3733.D22.teamD.API.*;
import edu.wpi.cs3733.D22.teamD.backend.ConnectionHelper;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutUsController extends ParentController {

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  public void startAPI() {
    try {
      StartAPI api = new StartAPI();
    } catch (Exception e) {
      System.err.println("The API is broken: " + e);
    }
  }
}
