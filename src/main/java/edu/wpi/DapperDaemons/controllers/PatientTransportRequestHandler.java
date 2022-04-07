package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.entities.Location;
import java.util.ArrayList;
import java.util.List;

public class PatientTransportRequestHandler extends MainHandler {
  /**
   * Gets all long names
   *
   * @return a list of long names
   */
  public List<String> getAllLongNames() {
    List<String> names = new ArrayList<String>();
    for (Location loc : this.locations) {
      names.add(loc.getLongName());
    }
    return names;
  }
}
