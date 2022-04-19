package edu.wpi.DapperDaemons.controllers;

import static edu.wpi.DapperDaemons.backend.ConnectionHandler.*;

import com.jfoenix.controls.JFXHamburger;
import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.backend.*;
import edu.wpi.DapperDaemons.backend.preload.Images;
import edu.wpi.DapperDaemons.controllers.helpers.TableListeners;
import edu.wpi.DapperDaemons.controllers.homePage.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ParentController extends AppController {

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

  /* Account */
  @FXML private Text accountName;
  @FXML private Circle profilePic;
  @FXML private VBox userDropdown;
  @FXML private ToggleButton userSettingsToggle;

  /* Background */
  @FXML private ImageView BGImage;
  @FXML private Pane BGContainer;

  /* Common UI */
  @FXML private JFXHamburger burg;
  @FXML private JFXHamburger burgBack;
  @FXML private VBox slider;
  @FXML private HBox childContainer;
  @FXML private Text headerNameField;

  /* Static across all home pages */
  private static HBox mainBox;
  private static Text headerName;
  private static WeatherHandler weather;
  private static DBSwitchHandler dbSwitch;
  private static NotificationHandler notifs;
  @FXML private ToggleButton alertButton;
  @FXML private VBox notifications;
  @FXML private ScrollPane notificationsScroller;

  @FXML private ImageView notifBell;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    bindImage(BGImage, BGContainer);
    menuSlider(slider, burg, burgBack);
    if (childContainer != null) mainBox = childContainer;
    if (headerNameField != null) headerName = headerNameField;

    setServerToggleMenu();

    dbSwitch = new DBSwitchHandler(serverIcon);
    new DateHandler(time);
    new AccountHandler(accountName, profilePic);
    weather = new WeatherHandler(weatherIcon, tempLabel);
    notifs = new NotificationHandler(notifications, notifBell);
    new ThemeHandler(mainBox);

    updateWeather();
    swapPage("default", "Home");
  }

  public void swapPage(String page, String pageName) {
    SessionTimeout.reset();
    TableListeners.removeAllListeners();
    App.LOG.info("Switching to page: <" + page + ">");
    mainBox.getChildren().clear();
    mainBox.setOnMouseMoved(e -> SessionTimeout.reset());
    if (burgBack != null && burgBack.isVisible()) closeSlider();

    try {
      HBox childPage =
          FXMLLoader.load(Objects.requireNonNull(App.class.getResource("views/" + page + ".fxml")));
      mainBox.getChildren().add(childPage);
      bindChild(childPage);
      headerName.setText(pageName);
    } catch (IOException e) {
      e.printStackTrace();
    }
    ThemeHandler.setTheme();
  }

  @FXML
  void goHome() {
    swapPage("default", "Home");
  }

  @FXML
  void openUserDropdown() {
    userDropdown.setVisible(userSettingsToggle.isSelected());
  }

  @FXML
  void openServerDropdown() {
    serverDropdown.setVisible(serverToggle.isSelected());
  }

  @FXML
  void openNotifications() {
    notificationsScroller.setVisible(alertButton.isSelected());
  }

  private void setServerToggleMenu() {
    switch (ConnectionHandler.getType()) {
      case EMBEDDED:
        serverSlotOne.setImage(Images.CLOUD);
        serverSlotTwo.setImage(Images.SERVER);
        serverSlotOneText.setText("Firebase");
        serverSlotTwoText.setText("Client Server");
        serverButtonOne.setOnMouseClicked(
            event -> {
              dbSwitch.setLoad();
              new Thread(
                      () -> {
                        serverToggle.setSelected(false);
                        openServerDropdown();
                        if (switchToCloudServer()) {
                          Platform.runLater(
                              () -> {
                                setServerToggleMenu();
                                serverIcon.setImage(Images.CLOUD);
                              });
                        } else {
                          serverIcon.setImage(Images.EMBEDDED);
                        }
                      })
                  .start();
            });
        serverButtonTwo.setOnMouseClicked(
            event -> {
              dbSwitch.setLoad();
              new Thread(
                      () -> {
                        serverToggle.setSelected(false);
                        openServerDropdown();
                        if (switchToClientServer()) {
                          Platform.runLater(
                              () -> {
                                setServerToggleMenu();
                                serverIcon.setImage(Images.SERVER);
                              });
                        } else {
                          serverIcon.setImage(Images.EMBEDDED);
                        }
                      })
                  .start();
            });
        break;
      case CLIENTSERVER:
        serverSlotOne.setImage(Images.CLOUD);
        serverSlotTwo.setImage(Images.EMBEDDED);
        serverSlotOneText.setText("Firebase");
        serverSlotTwoText.setText("Embedded");
        serverButtonOne.setOnMouseClicked(
            event -> {
              dbSwitch.setLoad();
              new Thread(
                      () -> {
                        serverToggle.setSelected(false);
                        openServerDropdown();
                        if (switchToCloudServer()) {
                          Platform.runLater(
                              () -> {
                                setServerToggleMenu();
                                serverIcon.setImage(Images.CLOUD);
                              });
                        } else {
                          serverIcon.setImage(Images.SERVER);
                        }
                      })
                  .start();
            });
        serverButtonTwo.setOnMouseClicked(
            event -> {
              dbSwitch.setLoad();
              new Thread(
                      () -> {
                        serverToggle.setSelected(false);
                        openServerDropdown();
                        if (switchToEmbedded()) {
                          Platform.runLater(
                              () -> {
                                setServerToggleMenu();
                                serverIcon.setImage(Images.EMBEDDED);
                              });
                        } else {
                          serverIcon.setImage(Images.SERVER);
                        }
                      })
                  .start();
            });
        break;
      case CLOUD:
        serverSlotOne.setImage(Images.SERVER);
        serverSlotTwo.setImage(Images.EMBEDDED);
        serverSlotOneText.setText("Client Server");
        serverSlotTwoText.setText("Embedded");
        serverButtonOne.setOnMouseClicked(
            event -> {
              dbSwitch.setLoad();
              new Thread(
                      () -> {
                        serverToggle.setSelected(false);
                        openServerDropdown();
                        if (switchToClientServer()) {
                          Platform.runLater(
                              () -> {
                                setServerToggleMenu();
                                serverIcon.setImage(Images.SERVER);
                              });
                        } else {
                          serverIcon.setImage(Images.CLOUD);
                        }
                      })
                  .start();
            });
        serverButtonTwo.setOnMouseClicked(
            event -> {
              dbSwitch.setLoad();
              new Thread(
                      () -> {
                        serverToggle.setSelected(false);
                        openServerDropdown();
                        if (switchToEmbedded()) {
                          Platform.runLater(
                              () -> {
                                setServerToggleMenu();
                                serverIcon.setImage(Images.EMBEDDED);
                              });
                        } else {
                          serverIcon.setImage(Images.CLOUD);
                        }
                      })
                  .start();
            });
        break;
    }
  }

  @FXML
  public void openUserSettings(ActionEvent event) {
    swapPage("userHome", "User Home Page");
  }

  @FXML
  public void logout() throws IOException {
    FireBase.getReference().child("NOTIFICATIONS").removeEventListener(notifs.getListener());
    switchScene("login.fxml", 575, 575);
    SecurityController.setUser(null);
  }

  public static void logoutUser() {
    App.LOG.info("Session timeout, user logged out.");
    FireBase.getReference().child("NOTIFICATIONS").removeEventListener(notifs.getListener());
    try {
      mainBox
          .getScene()
          .setRoot(
              FXMLLoader.load(Objects.requireNonNull(App.class.getResource("views/login.fxml"))));
      AppController.showError("Session Timed Out");
    } catch (Exception e) {
    }
    SecurityController.setUser(null);
  }

  @FXML
  void switchToAboutUs() {
    swapPage("aboutUs", "About Us");
  }

  @FXML
  void switchToEquipment() {
    swapPage("equipment", "Equipment Delivery");
  }

  @FXML
  void switchToLabRequest() {
    swapPage("labRequest", "Lab Request");
  }

  @FXML
  void switchToMap() {
    swapPage("locationMap", "Interactive Map");
  }

  @FXML
  void switchToMapDashboard() {
    swapPage("mapDashboard", "Map Dashboard");
  }

  @FXML
  void switchToMeal() {
    swapPage("meal", "Patient Meal Delivery Portal");
  }

  @FXML
  void switchToMedicine() {
    swapPage("medicine", "Medication Request");
  }

  @FXML
  void switchToPatientTransport() {
    swapPage("patientTransport", "Internal Patient Transportation");
  }

  @FXML
  void switchToSanitation() {
    swapPage("sanitation", "Sanitation Services");
  }

  @FXML
  void switchToSecurity() {
    swapPage("SecurityRequest", "Security Services");
  }

  @FXML
  void switchToLanguage() {
    swapPage("language", "Interpreter Request");
  }

  @FXML
  void switchToDB() {
    swapPage("backendInfoDisp", "Backend Information Display");
  }

  @FXML
  void goToServicePage() {
    swapPage("serviceRequestPage", "Service Page");
  }

  @FXML
  void switchToUserRequests() {
    swapPage("UserRequests", "User Requests");
  }

  @FXML
  private void updateWeather() {
    weather.update();
  }

  private void menuSlider(VBox slider, JFXHamburger burg, JFXHamburger burgBack) {
    slider.setTranslateX(-225);
    burg.setOnMouseClicked(
        event -> {
          TranslateTransition slide = new TranslateTransition();
          slide.setDuration(Duration.seconds(0.4));
          slide.setNode(slider);

          slide.setToX(0);
          slide.play();

          slider.setTranslateX(-225);

          slide.setOnFinished(
              (ActionEvent e) -> {
                burg.setVisible(false);
                burgBack.setVisible(true);
              });
        });

    burgBack.setOnMouseClicked(e -> closeSlider());
  }

  public void closeSlider() {
    TranslateTransition slide = new TranslateTransition();
    slide.setDuration(Duration.seconds(0.4));
    slide.setNode(slider);
    slide.setToX(-225);
    slide.play();

    slider.setTranslateX(0);

    slide.setOnFinished(
        (ActionEvent e) -> {
          burg.setVisible(true);
          burgBack.setVisible(false);
        });
  }

  // TODO: Remove this or move it
  protected List<String> getAllLongNames() {
    return new ArrayList<>();
  }
}
