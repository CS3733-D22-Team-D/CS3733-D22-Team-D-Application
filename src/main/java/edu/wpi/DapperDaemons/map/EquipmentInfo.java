package edu.wpi.DapperDaemons.map;

/** Allows for GUI keep track of Equipment Information that needs to be displayed */
public class EquipmentInfo implements Displayable {

  private String name;
  private String equipmentType;
  private int xPos;
  private int yPos;
  private String floor;

  public EquipmentInfo(String name, String equipmentType, int xPos, int yPos, String floor) {
    this.name = name;
    this.equipmentType = equipmentType;
    this.xPos = xPos;
    this.yPos = yPos;
    this.floor = floor;
  }

  /** Getters */
  public String getName() {
    return name;
  }

  public String getEquipmentType() {
    return equipmentType;
  }

  public int getxPos() {
    return xPos;
  }

  public int getyPos() {
    return yPos;
  }

  public String getFloor() {
    return floor;
  }
}
