package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.Location;
import java.util.ArrayList;
import java.util.List;

public abstract class MainHandler {

  /* DAO Object to access all room numbers */
  List<Location> locations;

  public MainHandler() {
    DAO<Location> dao = DAOPouch.getLocationDAO();
    /* Used the DAO object to get list */
    try {
      this.locations = dao.getAll();
    } catch (Exception e) {
      this.locations = new ArrayList<>();
    }
  }
}
