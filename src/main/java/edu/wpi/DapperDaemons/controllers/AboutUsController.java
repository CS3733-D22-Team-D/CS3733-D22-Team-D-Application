package edu.wpi.DapperDaemons.controllers;

import edu.wpi.cs3733.D22.teamD.API.ServiceException;
import edu.wpi.cs3733.D22.teamD.API.StartAPI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;

public class AboutUsController extends ParentController {

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  @FXML
  public void runAPI() {
    StartAPI api;
    try {
      api = new StartAPI();
    } catch (ServiceException e) {
      System.err.println("Something went wrong with the API");
      return;
    }
    try {
      api.run(0, 0, 800, 500, "edu/wpi/DapperDaemons/assets/themeBlue.css", "FSERV00101");
    } catch (ServiceException e) {
      System.err.println("A service error occurred");
    }
  }
}
