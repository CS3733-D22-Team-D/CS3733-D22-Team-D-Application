package edu.wpi.DapperDaemons.controllers;

import static edu.wpi.DapperDaemons.backend.ConnectionHandler.*;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.backend.*;
import edu.wpi.DapperDaemons.entities.Employee;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ParentController extends UIController {

  /* Time, Weather, and Database */
  @FXML private Label time;
  @FXML private ImageView weatherIcon;
  @FXML private Label tempLabel;
  @FXML private ImageView serverIcon;
  @FXML private ToggleButton serverToggle;
  @FXML private ImageView serverSlotOne;
  @FXML private ImageView serverSlotTwo;
  @FXML private Text serverSlotOneText;
  @FXML private Text serverSlotTwoText;
  @FXML private VBox serverDropdown;
  @FXML private Button serverButtonOne;
  @FXML private Button serverButtonTwo;

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

  // TODO get new image for the cloud server
  public final Image CLOUD =
      new Image(
          Objects.requireNonNull(
              getClass()
                  .getClassLoader()
                  .getResourceAsStream("edu/wpi/DapperDaemons/assets/serverIcons/cloud.png")));

  public final Image LOAD =
      new Image(
          Objects.requireNonNull(
              DefaultController.class
                  .getClassLoader()
                  .getResourceAsStream("edu/wpi/DapperDaemons/assets/loading.gif")));

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

    setServerToggleMenu();

    swapPage("default", "Home");
  }

  @FXML
  void changeServer(MouseEvent event) {
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
  //
  //  @FXML
  //  void logout(ActionEvent event) {
  //    swapPage("login", "Login");
  //    SecurityController.setUser(null);
  //  }

  @FXML
  void openUserDropdown(ActionEvent event) {
    if (userSettingsToggle.isSelected()) {
      userDropdown.setVisible(true);
    } else {
      userDropdown.setVisible(false);
    }
  }

  @FXML
  void openServerDropdown() {
    if (serverToggle.isSelected()) {
      serverDropdown.setVisible(true);
    } else {
      serverDropdown.setVisible(false);
    }
  }

  private void setServerToggleMenu() {
    switch (ConnectionHandler.getType()) {
      case EMBEDDED:
        serverSlotOne.setImage(CLOUD);
        serverSlotTwo.setImage(SERVER);
        serverSlotOneText.setText("Firebase");
        serverSlotTwoText.setText("Client Server");
        serverButtonOne.setOnMouseClicked(
            event -> {
              setLoad();
              new Thread(
                      () -> {
                        System.out.println("Switching to cloud");
                        if (switchToCloudServer()) {
                          Platform.runLater(
                              () -> {
                                setServerToggleMenu();
                                serverIcon.setImage(CLOUD);
                              });
                        } else {
                          serverIcon.setImage(EMBEDDED);
                        }
                      })
                  .start();
            });
        serverButtonTwo.setOnMouseClicked(
            event -> {
              setLoad();
              new Thread(
                      () -> {
                        openServerDropdown();
                        if (switchToClientServer()) {
                          Platform.runLater(
                              () -> {
                                setServerToggleMenu();
                                serverIcon.setImage(SERVER);
                              });
                        } else {
                          serverIcon.setImage(EMBEDDED);
                        }
                      })
                  .start();
            });
        break;
      case CLIENTSERVER:
        serverSlotOne.setImage(CLOUD);
        serverSlotTwo.setImage(EMBEDDED);
        serverSlotOneText.setText("Firebase");
        serverSlotTwoText.setText("Embedded");
        serverButtonOne.setOnMouseClicked(
            event -> {
              setLoad();
              new Thread(
                      () -> {
                        openServerDropdown();
                        if (switchToCloudServer()) {
                          Platform.runLater(
                              () -> {
                                setServerToggleMenu();
                                serverIcon.setImage(CLOUD);
                              });
                        } else {
                          serverIcon.setImage(SERVER);
                        }
                      })
                  .start();
            });
        serverButtonTwo.setOnMouseClicked(
            event -> {
              setLoad();
              new Thread(
                      () -> {
                        openServerDropdown();
                        if (switchToEmbedded()) {
                          Platform.runLater(
                              () -> {
                                setServerToggleMenu();
                                serverIcon.setImage(EMBEDDED);
                              });
                        } else {
                          serverIcon.setImage(SERVER);
                        }
                      })
                  .start();
            });
        break;
      case CLOUD:
        serverSlotOne.setImage(SERVER);
        serverSlotTwo.setImage(EMBEDDED);
        serverSlotOneText.setText("Client Server");
        serverSlotTwoText.setText("Embedded");
        serverButtonOne.setOnMouseClicked(
            event -> {
              setLoad();
              new Thread(
                      () -> {
                        openServerDropdown();
                        if (switchToClientServer()) {
                          Platform.runLater(
                              () -> {
                                setServerToggleMenu();
                                serverIcon.setImage(SERVER);
                              });
                        } else {
                          serverIcon.setImage(CLOUD);
                        }
                      })
                  .start();
            });
        serverButtonTwo.setOnMouseClicked(
            event -> {
              setLoad();
              new Thread(
                      () -> {
                        openServerDropdown();
                        if (switchToEmbedded()) {
                          Platform.runLater(
                              () -> {
                                setServerToggleMenu();
                                serverIcon.setImage(EMBEDDED);
                              });
                        } else {
                          serverIcon.setImage(CLOUD);
                        }
                      })
                  .start();
            });
        break;
    }
  }

  @FXML
  void openUserSettings(ActionEvent event) {
    swapPage("userSettings", "User Settings");
  }

  @FXML
  void quitProgram(MouseEvent event) {
    App.LOG.info("Closing program");
    LogSaver.saveAll();
    CSVSaver.saveAll();
    App.LOG.info("Successfully saved all files!");
    if (sceneBox != null && sceneBox.getScene() != null) {
      Stage window = (Stage) sceneBox.getScene().getWindow();
      if (window != null) window.close();
    }
    Platform.exit();
    System.exit(0);
  }

  @FXML
  public void logout() throws IOException {
    switchScene("login.fxml", 575, 575);
    SecurityController.setUser(null);
  }

  @FXML
  void switchToAboutUs(MouseEvent event) {
    swapPage("aboutUs", "About Us");
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
  void switchToMap(MouseEvent event) {
    swapPage("locationMap", "Interactive Map");
  }

  @FXML
  void switchToMapDashboard(MouseEvent event) {
    swapPage("mapDashboard", "Map Dashboard");
  }

  @FXML
  void switchToMeal(MouseEvent event) {
    swapPage("meal", "Patient Meal Delivery Portal");
  }

  @FXML
  void switchToMedicine(MouseEvent event) {
    swapPage("medicine", "Medication Request");
  }

  @FXML
  void switchToPatientTransport(MouseEvent event) {
    swapPage("patientTransport", "Internal Patient Transportation");
  }

  @FXML
  void switchToSanitation(MouseEvent event) {
    swapPage("sanitation", "Sanitation Services");
  }

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

    if (ConnectionHandler.getType().equals(ConnectionHandler.connectionType.EMBEDDED))
      serverIcon.setImage(EMBEDDED);
    else if (ConnectionHandler.getType().equals(ConnectionHandler.connectionType.CLIENTSERVER))
      serverIcon.setImage(SERVER);
    else serverIcon.setImage(CLOUD);
  }

  private boolean tryChange() throws InterruptedException {
    if (ConnectionHandler.getType().equals(ConnectionHandler.connectionType.EMBEDDED)) {
      if (switchToClientServer()) {
        Thread.sleep(1000);
        serverIcon.setImage(SERVER);
        return true;
      } else {
        Thread.sleep(1000);
        serverIcon.setImage(EMBEDDED);
        return false;
      }
    } else if (ConnectionHandler.getType().equals(ConnectionHandler.connectionType.CLIENTSERVER)) {
      if (switchToCloudServer()) {
        Thread.sleep(1000);
        serverIcon.setImage(CLOUD);
        return true;
      } else {
        Thread.sleep(1000);
        serverIcon.setImage(SERVER);
        return false;
      }
    } else {
      if (switchToEmbedded()) {
        Thread.sleep(1000);
        serverIcon.setImage(EMBEDDED);
        return true;
      } else {
        Thread.sleep(1000);
        serverIcon.setImage(CLOUD);
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
                        temp = Weather.getTemp("boston");
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
                              weatherIcon.setImage(Weather.getIcon("boston"));
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
