package edu.wpi.DapperDaemons;

import edu.wpi.DapperDaemons.backend.AutoSave;
import edu.wpi.DapperDaemons.backend.connectionHandler;
import java.io.IOException;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws IOException, SQLException {
    connectionHandler.init();
    connectionHandler.switchToClientServer();
    AutoSave.start(10);
    App.launch(App.class, args);
  }
}
