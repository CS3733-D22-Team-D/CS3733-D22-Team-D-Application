package edu.wpi.DapperDaemons.controllers;

// import edu.wpi.cs3733.D22.teamD.API.*;
// import edu.wpi.cs3733.D22.teamD.entities.Location;
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

  // TODO: Implement 3 Other APIs

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    teamDLoc.setText("");
    errorLabel.setText("");
    destID = "null";
  }

  /** Starts Team D Sanitation Request API (ours) */
  public void startTeamDApi() {
    //    if (!isInTeamDLocations(teamDLoc.getText().trim())) {
    //      errorLabel.setText("Error Invalid Location ID");
    //      return;
    //    }
    //    destID = teamDLoc.getText();
    //    teamDLoc.setText("");
    //    errorLabel.setText("");
    //    StartAPI api = null;
    //    try {
    //      api = new StartAPI();
    //    } catch (ServiceException e) {
    //      e.printStackTrace();
    //    }
    //    try {
    //      api.run(0, 0, 500, 800, "edu/wpi/DapperDaemons/assets/themeDark.css", destID);
    //    } catch (Exception e) {
    //      errorLabel.setText("Something Went Wrong");
    //    }
  }

  /**
   * Checks team D's location database for a given location ID
   *
   * @return true if the location is present in the API database
   */
  public boolean isInTeamDLocations(String locID) {
    //    LocationAPI locationAPI = new LocationAPI();
    //    for (Location loc : locationAPI.getAllLocations()) {
    //      if (loc.getNodeID().equals(locID)) return true;
    //    }
    return false;
  }
}
