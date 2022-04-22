package edu.wpi.DapperDaemons.controllers;

import edu.wpi.cs3733.D22.teamD.API.*;
import edu.wpi.cs3733.D22.teamD.entities.LocationObj;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class APILandingController implements Initializable {

  @FXML private TextField teamDLoc;
  @FXML private Label errorLabel;
  private static String destID;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    teamDLoc.setText("");
    errorLabel.setText("");
    destID = "null";
  }

  /** Starts Team D Sanitation Request API (ours) */
  public void startTeamDApi() {
    if (!isInTeamDLocations(teamDLoc.getText().trim())) {
      errorLabel.setText("Error Invalid Location ID");
      return;
    }
    destID = teamDLoc.getText();
    teamDLoc.setText("");
    errorLabel.setText("");
    StartAPI api = new StartAPI();
    try {
      api.run(0, 0, 500, 800, "edu/wpi/DapperDaemons/assets/themeDark.css", destID);
    } catch (Exception e) {
      errorLabel.setText("Something Went Wrong");
    }
  }

  /**
   * Checks team D's location database for a given location ID
   *
   * @return
   */
  public boolean isInTeamDLocations(String locID) {
    LocationAPI locationAPI = new LocationAPI();
    for (LocationObj loc : locationAPI.getAllLocations()) {
      if (loc.getNodeID().equals(locID)) return true;
    }
    return false;
  }
}
