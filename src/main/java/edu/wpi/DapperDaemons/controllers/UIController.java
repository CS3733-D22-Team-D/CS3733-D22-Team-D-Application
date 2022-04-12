package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXHamburger;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.entities.Location;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/*
Contains methods needed for all UI pages
 */
public abstract class UIController extends AppController {

  /* Common to Default page */
  @FXML private ImageView homeIcon;
  @FXML private JFXHamburger burg;
  @FXML private JFXHamburger burgBack;
  @FXML private VBox slider;
  @FXML private VBox sceneBox;
  @FXML private ImageView darkSwitch;

  protected static boolean isDark;

  /* Home page stuff */
  @FXML private VBox userDropdown;
  @FXML private ToggleButton userSettingsToggle;
  @FXML private Text accountName;
  @FXML private Circle profilePic;

  /* DAO Object to access all room numbers */
  private List<Location> locations;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    // Init a menu slider for all that extend this class
    menuSlider(slider, burg, burgBack);

    /* Used the DAO object to get list */
    DAO<Location> dao = DAOPouch.getLocationDAO();
    try {
      this.locations = dao.getAll();
    } catch (Exception e) {
      this.locations = new ArrayList<>();
    }

    // Init graphics
    try {
      initAccountGraphics();
    } catch (Exception e) {
      showError("We could not find your profile picture.", Pos.TOP_LEFT);
    }
    setTheme();
  }

  private void initAccountGraphics() throws NullPointerException {
    String employeeName =
        SecurityController.getUser().getFirstName()
            + " "
            + SecurityController.getUser().getLastName();
    accountName.setText(employeeName);
    accountName.setFont(Font.font("Comic Sans", 14));

    profilePic.setFill(
        new ImagePattern(
            new Image(
                Objects.requireNonNull(
                    getClass()
                        .getClassLoader()
                        .getResourceAsStream(
                            "edu/wpi/DapperDaemons/profilepictures/"
                                + SecurityController.getUser().getNodeID()
                                + ".png")))));
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

  @FXML
  public void toggleTheme() {
    isDark = !isDark;
    setTheme();
  }

  public void setTheme() {
    Node back = sceneBox.lookup("#background");

    Set<Node> fields = back.lookupAll("#field");
    Set<Node> fores = back.lookupAll("#foreground");
    Set<Node> jButtons = back.lookupAll("#jButton");
    Set<Node> specialFields = back.lookupAll("#specialField");
    Set<Node> texts = back.lookupAll("#label");
    Set<Node> tableCols = back.lookupAll("#col");

    if (isDark) {
      darkSwitch.setImage(
          new Image(
              getClass()
                  .getClassLoader()
                  .getResource("edu/wpi/DapperDaemons/assets/Glyphs/sun.png")
                  .toString()));

      back.getStyleClass().add("backgroundDark");

      for (Node field : fields) {
        field.getStyleClass().add("fieldDark");
      }

      for (Node fore : fores) {
        fore.getStyleClass().add("foregroundDark");
      }

      for (Node jButton : jButtons) {
        jButton.getStyleClass().add("fieldDark");
      }

      for (Node specialField : specialFields) {
        specialField.getStyleClass().add("specialFieldDark");
      }

      for (Node text : texts) {
        text.getStyleClass().add("textDark");
      }

      for (Node col : tableCols) {
        col.getStyleClass().add("tableDark");
      }

    } else {
      darkSwitch.setImage(
          new Image(
              getClass()
                  .getClassLoader()
                  .getResource("edu/wpi/DapperDaemons/assets/Glyphs/moon.png")
                  .toString()));

      back.getStyleClass().remove("backgroundDark");

      for (Node field : fields) {
        field.getStyleClass().remove("fieldDark");
      }

      for (Node fore : fores) {
        fore.getStyleClass().remove("foregroundDark");
      }

      for (Node jButton : jButtons) {
        jButton.getStyleClass().remove("fieldDark");
      }

      for (Node specialField : specialFields) {
        specialField.getStyleClass().remove("specialFieldDark");
      }

      for (Node text : texts) {
        text.getStyleClass().remove("textDark");
      }

      for (Node col : tableCols) {
        col.getStyleClass().remove("tableDark");
      }
    }
  }

  public void openUserDropdown() {
    userDropdown.setVisible(userSettingsToggle.isSelected());
  }

  @FXML
  public void logout() throws IOException {
    switchScene("login.fxml", 575, 575);
    SecurityController.setUser(null);
  }

  @FXML
  public void goHome() throws IOException {
    switchScene("default.fxml", 635, 510);
  }

  @FXML
  public void switchToEquipment() throws IOException {
    switchScene("equipment.fxml", 761, 626);
  }

  @FXML
  public void switchToLabRequest() throws IOException {
    switchScene("labRequest.fxml", 575, 575);
  }

  @FXML
  public void switchToMeal() throws IOException {
    switchScene("meal.fxml", 500, 500);
  }

  @FXML
  public void switchToMedicine() throws IOException {
    switchScene("medicine.fxml", 842, 530);
  }

  @FXML
  public void switchToDB() throws IOException {
    switchScene("backendInfoDisp.fxml", 842, 530);
  }

  @FXML
  public void switchToMap() throws IOException {
    switchScene("mapDashboard.fxml", 1080, 100);
  }

  @FXML
  public void switchToRealMap() throws IOException {
    switchScene("locationMap.fxml", 1172, 200);
  }

  @FXML
  public void switchToPatientTransport() throws IOException {
    switchScene("patientTransport.fxml", 831, 582);
  }

  @FXML
  public void switchToSanitation() throws IOException {
    switchScene("sanitation.fxml", 780, 548);
  }

  @FXML
  public void switchToAboutUs() throws IOException {
    switchScene("aboutUs.fxml", 780, 548);
  }

  @FXML
  public void goToLogin() throws IOException {
    switchScene("login.fxml", 780, 548);
  }

  @FXML
  public void openUserSettings() throws IOException {
    switchScene("userSettings.fxml", 941, 592);
  }

  /**
   * Gets all long names
   *
   * @return a list of long names
   */
  protected List<String> getAllLongNames() {
    List<String> names = new ArrayList<>();
    for (Location loc : this.locations) {
      names.add(loc.getLongName());
    }
    return names;
  }
}
