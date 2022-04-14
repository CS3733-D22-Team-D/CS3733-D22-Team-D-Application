package edu.wpi.DapperDaemons.backend;

import java.util.Timer;
import java.util.TimerTask;

public class AutoSave {
  private static Timer autoSave;

  private AutoSave() {}

  /** @param interval-the time in minutes between each autosave */
  public static void start(int interval) {
    if (autoSave != null) autoSave.cancel();
    autoSave = new Timer();
    autoSave.schedule(
        new TimerTask() {
          @Override
          public void run() {
            System.out.println("AutoSaving...");
            csvSaver.saveAll();
            LogSaver.saveAll();
          }
        },
        interval * 60000L,
        interval * 60000L);
  }
}
