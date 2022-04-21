package edu.wpi.DapperDaemons.controllers;

import edu.wpi.cs3733.D22.teamD.API.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutUsController extends ParentController {

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  public void startAPI() {
    try {
      StartAPI.appLaunch();
    } catch (IOException e) {
      System.err.println("API BROKE");
    }
  }
}
