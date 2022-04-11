package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.backend.connectionHandler;
import edu.wpi.DapperDaemons.backend.weather;
import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.wongSweeper.MinesweeperZN;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.derby.impl.jdbc.EmbedConnection;

/*
Manages Default Page Navigation
 */
public class DefaultController extends UIController {

  @FXML private ImageView homeIcon;
  @FXML private VBox sceneBox;
  @FXML private Label time;
  @FXML private ImageView weatherIcon;
  @FXML private Label Temp;
  @FXML private ImageView serverIcon;
  @FXML private HBox serverBox;

  private static Timer timer;
  private static final int timeUpdate = 1;

  private static Timer weatherTimer;
  private static final int weatherUpdate = 300;

  private List<KeyCode> easterEggSequence = new ArrayList<>();
  private int easterEggInd = 0;

  private long startTime;
  private int count = 0;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    if (SecurityController.getUser()
        .getEmployeeType()
        .equals(Employee.EmployeeType.ADMINISTRATOR)) {
      serverBox.setVisible(true);
      serverIcon.setVisible(true);
      ColorAdjust ca = new ColorAdjust();
      ca.setBrightness(1.0);
      serverIcon.setEffect(ca);
      if (connectionHandler.getConnection() instanceof EmbedConnection) {
        serverIcon.setImage(
            new Image(
                DefaultController.class
                    .getClassLoader()
                    .getResourceAsStream("edu/wpi/DapperDaemons/assets/serverIcons/embedded.png")));
      } else {
        serverIcon.setImage(
            new Image(
                DefaultController.class
                    .getClassLoader()
                    .getResourceAsStream("edu/wpi/DapperDaemons/assets/serverIcons/server.png")));
      }
    } else {
      serverBox.setVisible(false);
      serverIcon.setVisible(false);
    }
    //    weatherIcon.setImage(
    //        new Image(
    //            Objects.requireNonNull(
    //                DefaultController.class
    //                    .getClassLoader()
    //                    .getResourceAsStream("edu/wpi/DapperDaemons/assets/loading.gif"))));
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
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm - MM/dd");
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
    updateWeather();
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

  void setLoad() {
    serverIcon.setImage(
        new Image(
            Objects.requireNonNull(
                DefaultController.class
                    .getClassLoader()
                    .getResourceAsStream("edu/wpi/DapperDaemons/assets/loading.gif"))));
  }

  @FXML
  void changeServer() {
    setLoad();
    Thread t =
        new Thread(
            new Runnable() {

              @Override
              public void run() {
                tryChange();
              }
            });
    t.start();
  }

  void tryChange() {
    if (connectionHandler.getConnection() instanceof EmbedConnection) {
      if (connectionHandler.switchToClientServer()) {
        serverIcon.setImage(
            new Image(
                DefaultController.class
                    .getClassLoader()
                    .getResourceAsStream("edu/wpi/DapperDaemons/assets/serverIcons/server.png")));
      } else {
        serverIcon.setImage(
            new Image(
                DefaultController.class
                    .getClassLoader()
                    .getResourceAsStream("edu/wpi/DapperDaemons/assets/serverIcons/embedded.png")));
        showError("Connection could not be switched");
      }
    } else {
      if (connectionHandler.switchToEmbedded()) {
        serverIcon.setImage(
            new Image(
                DefaultController.class
                    .getClassLoader()
                    .getResourceAsStream("edu/wpi/DapperDaemons/assets/serverIcons/embedded.png")));
      } else {
        serverIcon.setImage(
            new Image(
                DefaultController.class
                    .getClassLoader()
                    .getResourceAsStream("edu/wpi/DapperDaemons/assets/serverIcons/server.png")));
        showError("Connection could not be switched");
      }
    }
  }

  @FXML
  void updateWeather() {
    // loading image
    //    weatherIcon.setImage(
    //        new Image(
    //            DefaultController.class
    //                .getClassLoader()
    //                .getResourceAsStream("edu/wpi/DapperDaemons/assets/loading.gif")));
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
                    int temp = -1;
                    try {
                      temp = weather.getTemp("boston");
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                    if (Temp != null && temp != -1) {
                      Temp.setText(String.valueOf(temp) + "\u00B0F");
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
