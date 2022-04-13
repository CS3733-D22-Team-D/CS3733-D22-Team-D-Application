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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/*
Manages Default Page Navigation
 */
public class ServicePageController extends UIController {

  /* Time, Weather, and Database */
  @FXML private Label time;
  @FXML private ImageView weatherIcon;
  @FXML private Label tempLabel;
  @FXML private ImageView serverIcon;
  @FXML private HBox serverBox;

  /* Background */
  @FXML private ImageView BGImage;
  @FXML private Pane BGContainer;

  /* Menu Button images */
  @FXML private Pane labPageContainer;
  @FXML private ImageView labPageImage;
  @FXML private Pane equipmentPageContainer;
  @FXML private ImageView equipmentPageImage;
  @FXML private Pane sanitationPageContainer;
  @FXML private ImageView sanitationPageImage;
  @FXML private Pane medicinePageContainer;
  @FXML private ImageView medicinePageImage;
  @FXML private Pane mealPageContainer;
  @FXML private ImageView mealPageImage;
  @FXML private Pane patientPageContainer;
  @FXML private ImageView patientPageImage;

  private static Timer timer;
  private static final int timeUpdate = 1;

  private static Timer weatherTimer;
  private static final int weatherUpdate = 300;

  private final List<KeyCode> easterEggSequence = new ArrayList<>();
  private int easterEggInd = 0;

  private long startTime;
  private int count = 0;

  public final Image EMBEDDED =
      new Image(
          Objects.requireNonNull(
              getClass()
                  .getClassLoader()
                  .getResourceAsStream("edu/wpi/DapperDaemons/assets/serverIcons/embedded.png")));
  public final Image SERVER =
      new Image(
          Objects.requireNonNull(
              getClass()
                  .getClassLoader()
                  .getResourceAsStream("edu/wpi/DapperDaemons/assets/serverIcons/server.png")));

  public final Image LOAD =
      new Image(
          Objects.requireNonNull(
              DefaultController.class
                  .getClassLoader()
                  .getResourceAsStream("edu/wpi/DapperDaemons/assets/loading.gif")));

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);

    initGraphics();

    updateDate();
    updateWeather();

    initSequence();
  }

  private void initGraphics() {
    bindImage(BGImage, BGContainer);
    bindImage(labPageImage, labPageContainer);
    bindImage(equipmentPageImage, equipmentPageContainer);
    bindImage(sanitationPageImage, sanitationPageContainer);
    bindImage(medicinePageImage, medicinePageContainer);
    bindImage(mealPageImage, mealPageContainer);
    bindImage(patientPageImage, patientPageContainer);
    initConnectionImage();
  }

  private void setLoad() {
    serverIcon.setImage(LOAD);
  }

  @FXML
  private void changeServer() {
    setLoad();
    Thread serverChange =
        new Thread(
            () -> {
              try {
                if (!tryChange()) {
                  Platform.runLater(() -> showError("Failed to switch connection"));
                }
              } catch (InterruptedException ignored) {
              }
            });
    serverChange.start();
  }

  private void initConnectionImage() {
    if (!SecurityController.getUser().getEmployeeType().equals(Employee.EmployeeType.ADMINISTRATOR))
      return;
    serverBox.setVisible(true);
    serverIcon.setVisible(true);
    ColorAdjust ca = new ColorAdjust();
    ca.setBrightness(1.0);
    serverIcon.setEffect(ca);

    if (connectionHandler.getType().equals(connectionHandler.connectionType.EMBEDDED))
      serverIcon.setImage(EMBEDDED);
    else serverIcon.setImage(SERVER);
  }

  private boolean tryChange() throws InterruptedException {
    if (connectionHandler.getType().equals(connectionHandler.connectionType.EMBEDDED)) {
      if (connectionHandler.switchToClientServer()) {
        Thread.sleep(1000);
        serverIcon.setImage(SERVER);
        return true;
      } else {
        Thread.sleep(1000);
        serverIcon.setImage(EMBEDDED);
        return false;
      }
    } else {
      if (connectionHandler.switchToEmbedded()) {
        Thread.sleep(1000);
        serverIcon.setImage(EMBEDDED);
        return true;
      } else {
        Thread.sleep(1000);
        serverIcon.setImage(SERVER);
        return false;
      }
    }
  }

  private void updateDate() {
    if (timer != null) timer.cancel();
    timer = new Timer();
    timer.schedule(
        new TimerTask() { // timer task to update the seconds
          @Override
          public void run() {
            // use Platform.runLater(Runnable runnable) If you need to update a GUI component from a
            // non-GUI thread.
            Platform.runLater(
                () -> {
                  SimpleDateFormat formatter = new SimpleDateFormat("HH:mm - MM/dd");
                  Date now = new Date();
                  if (time != null) time.setText(formatter.format(now));
                });
          }
        },
        0,
        timeUpdate * 1000); // Every 1 second
  }

  @FXML
  private void updateWeather() {
    // TODO: animate on refresh
    if (weatherTimer != null) weatherTimer.cancel();
    weatherTimer = new Timer();
    weatherTimer.schedule(
        new TimerTask() { // timer task to update the seconds
          @Override
          public void run() {
            // use Platform.runLater(Runnable runnable) If you need to update a GUI component from a
            // non-GUI thread.
            weatherIcon.setScaleX(0.5);
            weatherIcon.setScaleY(0.5);
            weatherIcon.setImage(LOAD);
            new Thread(
                    () -> {
                      // Gather data
                      int temp = -999;
                      try {
                        temp = weather.getTemp("boston");
                      } catch (Exception ignored) {
                      }

                      try {
                        Thread.sleep(1000);
                      } catch (InterruptedException ignored) {
                      }

                      // Set values
                      int finalTemp = temp;
                      Platform.runLater(
                          () -> {
                            if (finalTemp != -999) tempLabel.setText(finalTemp + "\u00B0F");
                            try {
                              weatherIcon.setImage(weather.getIcon("boston"));
                            } catch (Exception ignored) {
                            }
                            weatherIcon.setScaleX(1);
                            weatherIcon.setScaleY(1);
                          });
                    })
                .start();
          }
        },
        0,
        weatherUpdate * 1000); // Every 1 second
  }

  private void initSequence() {
    easterEggSequence.add(KeyCode.UP);
    easterEggSequence.add(KeyCode.UP);
    easterEggSequence.add(KeyCode.DOWN);
    easterEggSequence.add(KeyCode.DOWN);
    easterEggSequence.add(KeyCode.LEFT);
    easterEggSequence.add(KeyCode.RIGHT);
    easterEggSequence.add(KeyCode.LEFT);
    easterEggSequence.add(KeyCode.RIGHT);
    easterEggSequence.add(KeyCode.B);
    easterEggSequence.add(KeyCode.A);
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
          ms.begin(new Stage());
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
