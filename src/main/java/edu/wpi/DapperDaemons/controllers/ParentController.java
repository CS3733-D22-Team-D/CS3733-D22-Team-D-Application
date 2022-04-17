package edu.wpi.DapperDaemons.controllers;

import static edu.wpi.DapperDaemons.backend.ConnectionHandler.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.backend.*;
import edu.wpi.DapperDaemons.controllers.helpers.TableListeners;
import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.entities.Notification;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
import javax.sound.sampled.LineUnavailableException;

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
  @FXML private ToggleButton alertButton;
  @FXML private VBox notifications;

  private static ValueEventListener notifListener;
  private static Timer timer;
  private static final int timeUpdate = 1;

  private static Timer weatherTimer;
  private static final int weatherUpdate = 300;

  private long startTime;
  private int count = 0;

  // names are formatted this way so enums can easily reference css files
  public enum Theme {
    Light,
    Dark,
    Blue,
    Red
  }

  protected static Theme theme;

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

    setNotificationListener();

    if (headerNameField != null) {
      headerName = headerNameField;
    }

    initGraphics();
    updateDate();
    updateWeather();

    setServerToggleMenu();

    //    setNotifications();

    swapPage("default", "Home");
  }

  public void swapPage(String page, String pageName) {
    TableListeners.removeAllListeners();
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
    setTheme();
  }

  @FXML
  void goHome(MouseEvent event) {
    swapPage("default", "Home");
    if (burgBack.isVisible()) {
      closeSlider();
    }
  }

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

  void setNotificationListener() {
    if (ConnectionHandler.getType().equals(connectionType.CLOUD)) {
      DatabaseReference ref = FireBase.getReference().child("NOTIFICATIONS");
      notifListener =
          new ValueEventListener() {
            @Override
            public synchronized void onDataChange(DataSnapshot snapshot) {
              System.out.println(
                  "Notification listener for + " + SecurityController.getUser().getAttribute(1));
              new Thread(
                      () -> {
                        Platform.runLater(
                            () -> {
                              setNotifications();
                            });
                      })
                  .start();
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
        new ArrayList<Notification>(
            DAOPouch.getNotificationDAO()
                .filter(2, SecurityController.getUser().getAttribute(1))
                .values());
    List<Notification> unRead =
        new ArrayList(DAOPouch.getNotificationDAO().filter(notifications, 5, "false").values());
    List<Notification> unReadUnChimed =
        new ArrayList(DAOPouch.getNotificationDAO().filter(unRead, 6, "false").values());
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
    if (alertButton.isSelected()) {
      notifications.setVisible(true);
    } else {
      notifications.setVisible(false);
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
    FireBase.getReference().child("NOTIFICATIONS").removeEventListener(notifListener);
    switchScene("login.fxml", 575, 575);
    if (burgBack != null && burgBack.isVisible()) {
      closeSlider();
    }
    SecurityController.setUser(null);
  }

  @FXML
  void switchToAboutUs(MouseEvent event) {
    swapPage("aboutUs", "About Us");
    if (burgBack != null && burgBack.isVisible()) {
      closeSlider();
    }
  }

  @FXML
  void switchToEquipment(MouseEvent event) {
    swapPage("equipment", "Equipment Delivery");
    if (burgBack != null && burgBack.isVisible()) {
      closeSlider();
    }
  }

  @FXML
  void switchToLabRequest(MouseEvent event) {
    swapPage("labRequest", "Lab Request");
    if (burgBack != null && burgBack.isVisible()) {
      closeSlider();
    }
  }

  @FXML
  void switchToMap(MouseEvent event) {
    swapPage("locationMap", "Interactive Map");
    if (burgBack != null && burgBack.isVisible()) {
      closeSlider();
    }
  }

  @FXML
  void switchToMapDashboard(MouseEvent event) {
    swapPage("mapDashboard", "Map Dashboard");
    if (burgBack != null && burgBack.isVisible()) {
      closeSlider();
    }
  }

  @FXML
  void switchToMeal(MouseEvent event) {
    swapPage("meal", "Patient Meal Delivery Portal");
    if (burgBack != null && burgBack.isVisible()) {
      closeSlider();
    }
  }

  @FXML
  void switchToMedicine(MouseEvent event) {
    swapPage("medicine", "Medication Request");
    if (burgBack != null && burgBack.isVisible()) {
      closeSlider();
    }
  }

  @FXML
  void switchToPatientTransport(MouseEvent event) {
    swapPage("patientTransport", "Internal Patient Transportation");
    if (burgBack != null && burgBack.isVisible()) {
      closeSlider();
    }
  }

  @FXML
  void switchToSanitation(MouseEvent event) {
    swapPage("sanitation", "Sanitation Services");
    if (burgBack.isVisible()) {
      closeSlider();
    }
  }

  @FXML
  void switchToLanguage(MouseEvent event) {
    swapPage("language", "Interpreter Request");
    if (burgBack != null && burgBack.isVisible()) {
      closeSlider();
    }
  }

  @FXML
  void switchToDB(MouseEvent event) {
    swapPage("backendInfoDisp", "Backend Information Display");
    if (burgBack != null && burgBack.isVisible()) {
      closeSlider();
    }
  }

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

    //      for (Node back : backs) {
    //        back.getStyleClass().clear();
    //      }

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

      //      for (Node back : backs) {
      //        back.getStyleClass().add("background" + theme.toString());
      //      }

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
}
