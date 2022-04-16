package edu.wpi.DapperDaemons.backend;

import edu.wpi.DapperDaemons.App;
import java.io.*;

public class LogSaver {

  public static final String LOG_FOLDER =
      LogSaver.class.getClassLoader().getResource("logs").getFile() + "/";

  private LogSaver() {}

  public static void saveAll() {

    try {
      File src = new File(LOG_FOLDER + "app.log");
      File dest = new File(LOG_FOLDER + "test.log");

      InputStream in = new BufferedInputStream(new FileInputStream(src));
      OutputStream out = new BufferedOutputStream(new FileOutputStream(dest));

    } catch (IOException e) {
      System.out.println("FAILED");
      App.LOG.error("Could not copy log file.");
    }
  }
}
