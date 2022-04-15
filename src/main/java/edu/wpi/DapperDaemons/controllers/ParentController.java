package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.backend.connectionHandler;
import edu.wpi.DapperDaemons.backend.weather;
import edu.wpi.DapperDaemons.entities.Employee;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class ParentController extends UIController {

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    if (childContainer != null) {
      mainBox = childContainer;
    }

    if (headerNameField != null) {
      headerName = headerNameField;
    }

    initGraphics();

    updateDate();
    updateWeather();
  }

  /* Time, Weather, and Database */
  @FXML private Label time;
  @FXML private ImageView weatherIcon;
  @FXML private Label tempLabel;
  @FXML private ImageView serverIcon;
  @FXML private HBox serverBox;

  /* Background */
  @FXML private ImageView BGImage;
  @FXML private Pane BGContainer;
  @FXML private Text accountName;
  @FXML private JFXHamburger burg;
  @FXML private JFXHamburger burgBack;
  @FXML private HBox childContainer;
  private static HBox mainBox;
  @FXML private HBox childPage;
  @FXML private ImageView darkSwitch;
  @FXML private JFXButton exitButton;
  @FXML private Text headerNameField;
  private static Text headerName;
  @FXML private ImageView homeIcon;
  @FXML private ImageView homeIcon1;
  @FXML private JFXButton logoutButton;
  @FXML private Circle profilePic;
  @FXML private VBox sceneBox;
  @FXML private VBox slider;
  @FXML private VBox userDropdown;
  @FXML private JFXButton userSettingsButton;
  @FXML private ToggleButton userSettingsToggle;
  @FXML private StackPane windowContents;

  private static Timer timer;
  private static final int timeUpdate = 1;

  private static Timer weatherTimer;
  private static final int weatherUpdate = 300;

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

  @FXML
  void changeServer(MouseEvent event) {}

  public void swapPage(String page, String pageName) {
    mainBox.getChildren().clear();

    try {
      childPage =
          FXMLLoader.load(Objects.requireNonNull(App.class.getResource("views/" + page + ".fxml")));
      mainBox.getChildren().add(childPage);
      bindChild(childPage);
      headerName.setText(pageName);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  void goHome(MouseEvent event) {
    swapPage("default", "Home");
  }

  @FXML
  void logout(ActionEvent event) {}

  @FXML
  void openUserDropdown(ActionEvent event) {}

  @FXML
  void openUserSettings(ActionEvent event) {}

  @FXML
  void quitProgram(ActionEvent event) {
    System.out.println("TRYING TO QUIT THE PROGRAM");
  }

  @FXML
  void switchToAboutUs(ActionEvent event) {
    swapPage("aboutUs", "Meet the Team");
  }

  @FXML
  void switchToEquipment(MouseEvent event) {
    swapPage("equipment", "Equipment Delivery");
  }

  @FXML
  void switchToLabRequest(MouseEvent event) {
    swapPage("labRequest", "Lab Request");
  }

  @FXML
  void switchToMap(MouseEvent event) {}

  @FXML
  void switchToMapDashboard(MouseEvent event) {
    swapPage("mapDashboard", "Map Dashboard");
  }

  @FXML
  void switchToMeal(MouseEvent event) {}

  @FXML
  void switchToMedicine(MouseEvent event) {}

  @FXML
  void switchToPatientTransport(MouseEvent event) {}

  @FXML
  void switchToSanitation(MouseEvent event) {}

  @FXML
  void switchToDB(MouseEvent event) {
    swapPage("backendInfoDisp", "Backend Information Display");
  }

  @FXML
  void toggleTheme(MouseEvent event) {}

  private void initGraphics() {
    bindImage(BGImage, BGContainer);
    initConnectionImage();
  }

  public static void bindImage(ImageView pageImage, Pane parent) {
    pageImage.fitHeightProperty().bind(parent.heightProperty());
    pageImage.fitWidthProperty().bind(parent.widthProperty());
  }

  public static void bindChild(HBox child) {
    HBox.setHgrow(child, Priority.ALWAYS);
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
}
