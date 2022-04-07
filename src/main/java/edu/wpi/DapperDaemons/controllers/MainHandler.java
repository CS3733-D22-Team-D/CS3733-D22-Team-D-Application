package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.entities.Location;
import java.util.ArrayList;
import java.util.List;

public abstract class MainHandler {

  /* DAO Object to access all room numbers */
  List<Location> locations;

  public MainHandler() {
    DAO<Location> dao;
    try {
      dao = new DAO<Location>(new Location());
    } catch (Exception e) {
      System.err.print("Error, table was unable to be created\n");
      return;
    }
    /* Used the DAO object to get list */
    try {
      this.locations = dao.getAll();
    } catch (Exception e) {
      this.locations = new ArrayList<>();
    }
  }
}
