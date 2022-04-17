package edu.wpi.DapperDaemons.controllers;

import static edu.wpi.DapperDaemons.backend.ConnectionHandler.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jfoenix.controls.JFXHamburger;
import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.backend.*;
import edu.wpi.DapperDaemons.backend.preload.Images;
import edu.wpi.DapperDaemons.controllers.helpers.TableListeners;
import edu.wpi.DapperDaemons.controllers.homePage.AccountHandler;
import edu.wpi.DapperDaemons.controllers.homePage.DBSwitchHandler;
import edu.wpi.DapperDaemons.controllers.homePage.DateHandler;
import edu.wpi.DapperDaemons.controllers.homePage.WeatherHandler;
import edu.wpi.DapperDaemons.entities.Notification;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javax.sound.sampled.LineUnavailableException;

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
  @FXML private ToggleButton alertButton;
  @FXML private VBox notifications;

  private static ValueEventListener notifListener;

  // names are formatted this way so enums can easily reference css files
  protected static Theme theme;

  public enum Theme {
    Light,
    Dark,
    Blue,
    Red
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    menuSlider(slider, burg, burgBack);
    bindImage(BGImage, BGContainer);
    if (childContainer != null) mainBox = childContainer;
    if (headerNameField != null) headerName = headerNameField;

    setServerToggleMenu();

    dbSwitch = new DBSwitchHandler(serverIcon);
    new DateHandler(time);
    new AccountHandler(accountName, profilePic);
    weather = new WeatherHandler(weatherIcon, tempLabel);

    setNotificationListener();

    if (headerNameField != null) {
      headerName = headerNameField;
    }

    updateWeather();

    //    setNotifications();

    swapPage("default", "Home");
  }

  public void swapPage(String page, String pageName) {
    TableListeners.removeAllListeners();
    mainBox.getChildren().clear();
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
    setTheme();
  }

  @FXML
  void goHome() {
    swapPage("default", "Home");
    if (burgBack.isVisible()) {
      closeSlider();
    }
  }

  @FXML
  void openUserDropdown() {
    userDropdown.setVisible(userSettingsToggle.isSelected());
  }

  @FXML
  void openServerDropdown() {
    serverDropdown.setVisible(serverToggle.isSelected());
  }

  void setNotificationListener() {
    if (ConnectionHandler.getType().equals(connectionType.CLOUD)) {
      DatabaseReference ref = FireBase.getReference().child("NOTIFICATIONS");
      notifListener =
          new ValueEventListener() {
            @Override
            public synchronized void onDataChange(DataSnapshot snapshot) {
              System.out.println(
                  "Notification listener for " + SecurityController.getUser().getAttribute(1));
              new Thread(() -> Platform.runLater(() -> setNotifications())).start();
            }

            @Override
            public void onCancelled(DatabaseError error) {
              System.out.println("Cancelled in notification listener");
            }
          };
      ref.addValueEventListener(notifListener);
    }
  }

  void addNotification(Notification n) {
    SoundPlayer sp = new SoundPlayer("edu/wpi/DapperDaemons/notifications/Bloop.wav");
    try {
      sp.play();
    } catch (LineUnavailableException e) {
      throw new RuntimeException(e);
    }
    this.notifications.getChildren().add(createNotification(n));
  }

  void setNotifications() {
    this.notifications.getChildren().clear();
    List<Notification> notifications =
        new ArrayList<>(
            DAOPouch.getNotificationDAO()
                .filter(2, SecurityController.getUser().getAttribute(1))
                .values());
    List<Notification> unRead =
        new ArrayList<>(DAOPouch.getNotificationDAO().filter(notifications, 5, "false").values());
    List<Notification> unReadUnChimed =
        new ArrayList<>(DAOPouch.getNotificationDAO().filter(unRead, 6, "false").values());
    if (notifications.size() == 0) {
      Text t = new Text();
      t.setText("Looks empty in here");
      this.notifications.getChildren().add(t);
      return;
    }
    if (unRead.size() > 0) {
      if (unReadUnChimed.size() > 0) {
        SoundPlayer sp = new SoundPlayer("edu/wpi/DapperDaemons/notifications/Bloop.wav");
        try {
          sp.play();
        } catch (LineUnavailableException e) {
          throw new RuntimeException(e);
        }
        for (Notification n : unReadUnChimed) {
          n.setAttribute(6, "true");
          DAOPouch.getNotificationDAO().add(n);
        }
        for (Notification n : unRead) {
          this.notifications.getChildren().add(createNotification(n));
        }
      }
    }
  }

  VBox createNotification(Notification n) {
    VBox notif = new VBox();

    try {
      notif =
          FXMLLoader.load(
              Objects.requireNonNull(App.class.getResource("views/" + "notification" + ".fxml")));
      notifications.getChildren().add(notif);
      Label notifSubject = (Label) notif.getChildren().get(0);
      Label notifBody = (Label) notif.getChildren().get(1);

      notifSubject.setText(n.getSubject());
      notifBody.setText(n.getBody());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return notif;
  }

  @FXML
  void openNotifications() {
    notifications.setVisible(alertButton.isSelected());
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
                        System.out.println("Switching to cloud");
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
  void openUserSettings() {
    swapPage("userSettings", "User Settings");
  }

  @FXML
  public void logout() throws IOException {
    FireBase.getReference().child("NOTIFICATIONS").removeEventListener(notifListener);
    switchScene("login.fxml", 575, 575);
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

  public static void bindImage(ImageView pageImage, Pane parent) {
    pageImage.fitHeightProperty().bind(parent.heightProperty());
    pageImage.fitWidthProperty().bind(parent.widthProperty());
  }

  public static void bindChild(HBox child) {
    HBox.setHgrow(child, Priority.ALWAYS);
  }

  @FXML
  private void updateWeather() {
    weather.update();
  }

  @FXML
  public void toggleTheme(Theme newTheme) {
    theme = newTheme;
    setTheme();
  }

  public void setTheme() {
    Set<Node> backs = mainBox.lookupAll("#background");
    Set<Node> fields = mainBox.lookupAll("#field");
    Set<Node> fores = mainBox.lookupAll("#foreground");
    Set<Node> jButtons = mainBox.lookupAll("#jButton");
    Set<Node> specialFields = mainBox.lookupAll("#specialField");
    Set<Node> texts = mainBox.lookupAll("#label");
    Set<Node> tableCols = mainBox.lookupAll("#col");

    for (Node back : backs) {
      back.getStyleClass().clear();
    }

    for (Node field : fields) {
      field.getStyleClass().clear();
    }

    for (Node fore : fores) {
      fore.getStyleClass().clear();
    }

    for (Node jButton : jButtons) {
      jButton.getStyleClass().clear();
    }

    for (Node specialField : specialFields) {
      specialField.getStyleClass().clear();
    }

    for (Node text : texts) {
      text.getStyleClass().clear();
    }

    for (Node col : tableCols) {
      col.getStyleClass().clear();
    }

    if (theme != Theme.Light && theme != null) {

      for (Node back : backs) {
        back.getStyleClass().add("background" + theme.toString());
      }

      for (Node field : fields) {
        field.getStyleClass().add("field" + theme.toString());
      }

      for (Node fore : fores) {
        fore.getStyleClass().add("foreground" + theme.toString());
      }

      for (Node jButton : jButtons) {
        jButton.getStyleClass().add("field" + theme.toString());
      }

      for (Node specialField : specialFields) {
        specialField.getStyleClass().add("specialField" + theme.toString());
      }

      for (Node text : texts) {
        text.getStyleClass().add("text" + theme.toString());
      }

      for (Node col : tableCols) {
        col.getStyleClass().add("table" + theme.toString());
      }
    }
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
