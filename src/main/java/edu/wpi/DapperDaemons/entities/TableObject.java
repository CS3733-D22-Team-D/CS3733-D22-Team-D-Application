package edu.wpi.DapperDaemons.entities;

import java.util.List;

public abstract class TableObject {

  public TableObject() {}

  public abstract String getTableInit();

  public abstract String getTableName();

  public abstract String getAttribute(int columnNumber);

  public abstract void setAttribute(int columnNumber, String newAttribute);

  public abstract TableObject newInstance(List<String> l);
}
