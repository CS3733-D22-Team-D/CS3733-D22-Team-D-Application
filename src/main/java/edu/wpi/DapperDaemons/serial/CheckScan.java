package edu.wpi.DapperDaemons.serial;

import java.util.ArrayList;
import java.util.List;

public class CheckScan {

  private List<String> validUIDs;
  private SerialCOM reader;

  public CheckScan() {
    reader = new SerialCOM();
    validUIDs = new ArrayList<>();
    validUIDs.add("354");
  }

  public boolean scan() {
    String inputID = reader.readData();
    for (String uid : this.validUIDs) {
      if (inputID.equals(uid)) return true;
    }
    return false;
  }
}
