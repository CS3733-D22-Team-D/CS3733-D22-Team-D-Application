package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.APIConverters.SanitationReqConverter;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.requests.SanitationRequest;
import edu.wpi.cs3733.D22.teamD.API.*;
import edu.wpi.cs3733.D22.teamD.entities.LocationObj;
import edu.wpi.cs3733.D22.teamD.request.SanitationIRequest;
import edu.wpi.cs3733.D22.teamZ.api.API;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;

public class APILandingController implements Initializable {

  @FXML private TextField teamDLoc;
  @FXML private Label errorLabel;
  @FXML private Button dSave;
  private static String destIDTeamD;


  @FXML private TextField zDest;
  @FXML private Label zErrorLabel;
  @FXML private TextField zOrigin;
  private static String teamZOriginID;
  private static String teamZDestinationID;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // Global init
    saveToDatabase();

    // Team D API Init
    teamDLoc.setText("");
    errorLabel.setText("");
    destIDTeamD = "null";
    dSave.setVisible(false);

    // Team Z API Init
    zDest.setText("");
    zOrigin.setText("");
    zErrorLabel.setText("");
    teamZOriginID = "null";
    teamZDestinationID = "null";
  }

  /** Allows for requests submitted by the API to be saved to our database */
  public void saveToDatabase() {
    databaseSaverTeamD();
  }

  /**
   * Checks team D's location database for a given location ID
   *
   * @return true if the location is present in the API database
   */
  public boolean isInLocationDatabase(String locID) {
    LocationAPI locationAPI = new LocationAPI();
    for (LocationObj loc : locationAPI.getAllLocations()) {
      if (loc.getNodeID().equals(locID)) return true;
    }
    return false;
  }

  /**
   * Checks if a given Sanitation Request is in the database
   * @param req request to check
   * @return true if it is in the database
   */
  public boolean checkIfSanitationReqExists(SanitationRequest req) {
    for(SanitationRequest dbReq : DAOPouch.getSanitationRequestDAO().getAll().values()) {
      if(req.getNodeID().equals(dbReq.getNodeID()) && req.getPriority().equals(dbReq.getPriority()))
        return true;
    }
    return false;
  }


  /** Starts Team D Sanitation Request API (ours) */
  public void startTeamDApi() {
    SanitationReqAPI sanitationReqAPI = new SanitationReqAPI();
    if (!isInLocationDatabase(teamDLoc.getText().trim())) {
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
      errorLabel.setText("Team-D API Broke");
      return;
    }
    errorLabel.setText("You may have unsaved requests!");
    errorLabel.setTextFill(Paint.valueOf("EF5353"));
  }

  /**
   * Saves all requests (that do not already exist) from the API to the program database
   */
  public void databaseSaverTeamD() {
    SanitationReqAPI sanitationReqAPI = new SanitationReqAPI();
    for(SanitationIRequest iReq : sanitationReqAPI.getAllRequests()) {
      if(!checkIfSanitationReqExists(SanitationReqConverter.convert(iReq)))
        DAOPouch.getSanitationRequestDAO().add(SanitationReqConverter.convert(iReq));
    }

  }

  /** Starts Team-Z's External Patient Request */
  public void startTeamZApi() {
    if (!isInLocationDatabase(zDest.getText().trim())
        || !isInLocationDatabase(zOrigin.getText().trim())) {
      zErrorLabel.setText("Error Invalid Location ID");
      return;
    }
    teamZOriginID = zOrigin.getText();
    teamZDestinationID = zDest.getText();

    API api = new API();
    try {
      api.run(
          0,
          0,
          800,
          500,
          "edu/wpi/DapperDaemons/assets/teamZAPI.css",
          teamZDestinationID,
          teamZOriginID);
    } catch (Exception e) {
      System.err.println("Team Z's API Broke");
      return;
    }
    zErrorLabel.setText("You may have unsaved requests!");
    zErrorLabel.setTextFill(Paint.valueOf("EF5353"));
  }
}
