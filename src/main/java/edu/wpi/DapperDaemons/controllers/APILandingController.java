package edu.wpi.DapperDaemons.controllers;

import edu.wpi.cs3733.D22.teamD.API.*;
import edu.wpi.cs3733.D22.teamD.entities.LocationObj;
import edu.wpi.cs3733.D22.teamZ.api.API;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class APILandingController implements Initializable {

  @FXML private TextField teamDLoc;
  @FXML private Label errorLabel;
  private static String destIDTeamD;

  @FXML private TextField zDest;
  @FXML private Label zErrorLabel;
  @FXML private TextField zOrigin;
  private static String teamZOriginID;
  private static String teamZDestinationID;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // Team D API Init
    teamDLoc.setText("");
    errorLabel.setText("");
    destIDTeamD = "null";

    // Team Z API Init
    zDest.setText("");
    zOrigin.setText("");
    zErrorLabel.setText("");
    teamZOriginID = "null";
    teamZDestinationID = "null";

  }

  /** Starts Team D Sanitation Request API (ours) */
  public void startTeamDApi() {
    if (!isInTeamDLocations(teamDLoc.getText().trim())) {
      errorLabel.setText("Error Invalid Location ID");
      return;
    }
    destIDTeamD = teamDLoc.getText();
    teamDLoc.setText("");
    errorLabel.setText("");
    StartAPI api = new StartAPI();
    try {
      api.run(0, 0, 500, 800, "edu/wpi/DapperDaemons/assets/themeBlue.css", destIDTeamD);
    } catch (Exception e) {
      errorLabel.setText("Something Went Wrong");
    }
  }

  /**
   * Checks team D's location database for a given location ID
   *
   * @return true if the location is present in the API database
   */
  public boolean isInTeamDLocations(String locID) {
    LocationAPI locationAPI = new LocationAPI();
    for (LocationObj loc : locationAPI.getAllLocations()) {
      if (loc.getNodeID().equals(locID)) return true;
    }
    return false;
  }

  /**
   * Starts Team-Z's External Patient Request API
   * Note: Uses custom CSS to fix some styling issues
   */
  public void startTeamZApi() {
    // TODO: Use API converter classes to update locations and employees
    API api = new API();
    try {
      api.run(
          0, 0, 800, 500, "edu/wpi/DapperDaemons/assets/teamZAPI.css", "dPATI01505", "dPATI01505");
    } catch (Exception e) {
      System.err.println("Team Z's API Broke");
    }
  }
}
