package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.backend.*;
import edu.wpi.DapperDaemons.backend.preload.Images;
import edu.wpi.DapperDaemons.controllers.homePage.AccountHandler;
import edu.wpi.DapperDaemons.controllers.homePage.DBSwitchHandler;
import edu.wpi.DapperDaemons.controllers.homePage.DateHandler;
import edu.wpi.DapperDaemons.controllers.homePage.WeatherHandler;
import edu.wpi.DapperDaemons.entities.Employee;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import edu.wpi.DapperDaemons.entities.Location;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ParentController extends AppController {

  /* Time, Weather, and Database */
  @FXML private Label time;
  @FXML private ImageView weatherIcon;
  @FXML private Label tempLabel;
  @FXML private ImageView serverIcon;

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
//  private static DateHandler date;
  private static DBSwitchHandler dbSwitch;
  private static AccountHandler accountHandler;

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
    menuSlider(slider,burg,burgBack);
    bindImage(BGImage, BGContainer);
    if (childContainer != null) mainBox = childContainer;
    if (headerNameField != null) headerName = headerNameField;

    accountHandler = new AccountHandler(accountName,profilePic);
    dbSwitch = new DBSwitchHandler(serverIcon);
    new DateHandler(time);
    weather = new WeatherHandler(weatherIcon,tempLabel);

    swapPage("default", "Home");
  }

  public void swapPage(String page, String pageName) {
    mainBox.getChildren().clear();

    try {
      HBox childPage = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("views/" + page + ".fxml")));
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
  }

  @FXML
  void openUserDropdown(ActionEvent event) {
    userDropdown.setVisible(userSettingsToggle.isSelected());
  }

  @FXML
  void openUserSettings(ActionEvent event) {
    swapPage("userSettings", "User Settings");
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

  private static void menuSlider(VBox slider, JFXHamburger burg, JFXHamburger burgBack) {
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

    burgBack.setOnMouseClicked(
            event -> {
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
            });
  }
}
