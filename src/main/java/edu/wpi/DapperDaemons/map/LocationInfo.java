package edu.wpi.DapperDaemons.map;

/** Allows for GUI keep track of Location Information that needs to be displayed */
public class LocationInfo implements Displayable {

  private String shortName;
  private String longName;
  private String nodeType;
  private int xPos;
  private int yPos;
  private String floor;
  private String building;

  public LocationInfo(PositionInfo p) {
    this.longName = p.getLoc().getLongName();
    this.shortName = p.getLoc().getShortName();
    this.nodeType = p.getType();
    this.xPos = p.getX();
    this.yPos = p.getY();
    this.floor = p.getFloor();
    this.building = p.getBuilding();
  }

  /** Getters */
  public String getShortName() {
    return shortName;
  }

  public String getLongName() {
    return this.longName;
  }

  public String getNodeType() {
    return nodeType;
  }

  public int getX() {
    return xPos;
  }

  public int getY() {
    return yPos;
  }

  public String getFloor() {
    return floor;
  }

  public String getBuilding() {
    return building;
  }
}
