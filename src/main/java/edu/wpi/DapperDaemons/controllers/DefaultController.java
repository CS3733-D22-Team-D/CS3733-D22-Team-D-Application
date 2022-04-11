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
  @FXML private Pane mapPageContainer;
  @FXML private ImageView mapPageImage;
  @FXML private Pane patientPageContainer;
  @FXML private ImageView patientPageImage;
  @FXML private Pane backendPageContainer;
  @FXML private ImageView backendPageImage;

  private static Timer timer;
  private static final int timeUpdate = 1;

  private static Timer weatherTimer;
  private static final int weatherUpdate = 300;

  private List<KeyCode> easterEggSequence = new ArrayList<>();
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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);

    initConnectionImage();
    updateDate();
    updateWeather();

    if (serverIcon != null) {
      bindImage(BGImage, BGContainer);
      bindImage(labPageImage, labPageContainer);
      bindImage(equipmentPageImage, equipmentPageContainer);
      bindImage(sanitationPageImage, sanitationPageContainer);
      bindImage(medicinePageImage, medicinePageContainer);
      bindImage(mealPageImage, mealPageContainer);
      bindImage(mapPageImage, mapPageContainer);
      bindImage(patientPageImage, patientPageContainer);
      bindImage(backendPageImage, backendPageContainer);
    }

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
    Platform.runLater(this::tryChange);
  }

  private void initConnectionImage() {
    if (!SecurityController.getUser().getEmployeeType().equals(Employee.EmployeeType.ADMINISTRATOR))
      return;
    if (serverBox != null) {
      serverBox.setVisible(true);
      serverIcon.setVisible(true);
      ColorAdjust ca = new ColorAdjust();
      ca.setBrightness(1.0);
      serverIcon.setEffect(ca);

      if (connectionHandler.getConnection() instanceof EmbedConnection) {
        serverIcon.setImage(EMBEDDED);
      } else {
        serverIcon.setImage(SERVER);
      }
    }
  }

  private void tryChange() {
    if (connectionHandler.getConnection() instanceof EmbedConnection) {
      if (connectionHandler.switchToClientServer()) {
        serverIcon.setImage(SERVER);
      } else {
        serverIcon.setImage(EMBEDDED);
        showError("Connection could not be switched");
      }
    } else {
      if (connectionHandler.switchToEmbedded()) {
        serverIcon.setImage(EMBEDDED);
      } else {
        serverIcon.setImage(SERVER);
        showError("Connection could not be switched");
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
