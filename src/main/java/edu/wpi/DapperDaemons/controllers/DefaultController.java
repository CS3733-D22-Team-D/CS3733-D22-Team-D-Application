package edu.wpi.DapperDaemons.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/*
Manages Default Page Navigation
 */
public class DefaultController extends UIController {
  @FXML private ImageView homeIcon;
  @FXML private VBox sceneBox;
  @FXML private Label time;

  private static Timer timer;

  long startTime;
  int count = 0;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    if (timer != null) timer.cancel();
    timer = new Timer();
    timer.schedule(
        new TimerTask() { // timer task to update the seconds
          @Override
          public void run() {
            // use Platform.runLater(Runnable runnable) If you need to update a GUI component from a
            // non-GUI thread.
            Platform.runLater(
                new Runnable() {
                  public void run() {
                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyy HH:mm:ss");
                    Date now = new Date();
                    time.setText(formatter.format(now));
                  }
                });
          }
        },
        0,
        1000); // Every 1 second
  }

  @FXML
  public void easterEgg() throws IOException {
    if (count == 0) {
      startTime = System.currentTimeMillis();
    }
    count++;
    if ((System.currentTimeMillis() - startTime) > 10000) {
      count = 0;
    }
    if (count == 10 & (System.currentTimeMillis() - startTime) < 10000) {
      count = 0;
      switchScene("easterEgg.fxml", 761, 626);
    }
  }
}
