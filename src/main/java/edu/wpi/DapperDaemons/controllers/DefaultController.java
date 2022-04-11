package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.weather;
import edu.wpi.DapperDaemons.wongSweeper.MinesweeperZN;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
Manages Default Page Navigation
 */
public class DefaultController extends UIController {

  @FXML private ImageView homeIcon;
  @FXML private VBox sceneBox;
  @FXML private Label time;
  @FXML private ImageView weatherIcon;
  @FXML private Label Temp;

  private static Timer timer;
  private static final int timeUpdate = 1;

  private static Timer weatherTimer;
  private static final int weatherUpdate = 300;

  List<KeyCode> easterEggSequence = new ArrayList<>();
  int easterEggInd = 0;

  long startTime;
  int count = 0;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    weatherIcon.setImage(
        new Image(
            DefaultController.class
                .getClassLoader()
                .getResourceAsStream("edu/wpi/DapperDaemons/assets/loading.gif")));
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
                    if (time != null) {
                      time.setText(formatter.format(now));
                    }
                  }
                });
          }
        },
        0,
        timeUpdate * 1000); // Every 1 second
    if (weatherTimer != null) weatherTimer.cancel();
    weatherTimer = new Timer();
    weatherTimer.schedule(
        new TimerTask() { // timer task to update the seconds
          @Override
          public void run() {
            // use Platform.runLater(Runnable runnable) If you need to update a GUI component from a
            // non-GUI thread.
            Platform.runLater(
                new Runnable() {
                  public void run() {
                    Float temp = null;
                    try {
                      temp = weather.getTemp("boston");
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                    if (Temp != null && temp != null) {
                      Temp.setText(String.valueOf(temp));
                    }
                    Image icon = null;
                    try {
                      icon = weather.getIcon("boston");
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                    if (weatherIcon != null && icon != null) {
                      weatherIcon.setImage(icon);
                    }
                  }
                });
          }
        },
        0,
        weatherUpdate * 1000); // Every 1 second
    easterEggSequence.add(KeyCode.UP);
    easterEggSequence.add(KeyCode.UP);
    easterEggSequence.add(KeyCode.DOWN);
    easterEggSequence.add(KeyCode.DOWN);
    easterEggSequence.add(KeyCode.LEFT);
    easterEggSequence.add(KeyCode.RIGHT);
    easterEggSequence.add(KeyCode.LEFT);
    easterEggSequence.add(KeyCode.RIGHT);
    easterEggSequence.add(KeyCode.A);
    easterEggSequence.add(KeyCode.B);
    easterEggSequence.add(KeyCode.ENTER);
  }

  @FXML
  public void konami(KeyEvent e) {
    if (e.getCode().equals(easterEggSequence.get(easterEggInd))) {
      easterEggInd++;
      if (easterEggInd == easterEggSequence.size()) {
        easterEggInd = 0;
        try {
          //          switchScene("konami.fxml", 700, 500);
          MinesweeperZN ms = new MinesweeperZN();
          ms.begin((Stage) new Stage());
        } catch (IOException ex) {
          ex.printStackTrace();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    } else {
      easterEggInd = 0;
    }
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
