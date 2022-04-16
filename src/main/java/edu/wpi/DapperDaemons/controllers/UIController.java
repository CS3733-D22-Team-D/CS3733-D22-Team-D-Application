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
    //    super.initialize(location, resources);
    // Init a menu slider for all that extend this class
    menuSlider(slider, burg, burgBack);

    /* Used the DAO object to get list */
    DAO<Location> dao = DAOPouch.getLocationDAO();
    try {
      this.locations = new ArrayList(dao.getAll().values());
    } catch (Exception e) {
      this.locations = new ArrayList<>();
    }

    // Init graphics
    try {
      initAccountGraphics();
    } catch (Exception e) {
      System.out.println("Oop");
      //      showError("We could not find your profile picture.", Pos.TOP_LEFT);
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
    Set<Node> backs = sceneBox.lookupAll("#background");
    Set<Node> fields = sceneBox.lookupAll("#field");
    Set<Node> fores = sceneBox.lookupAll("#foreground");
    Set<Node> jButtons = sceneBox.lookupAll("#jButton");
    Set<Node> specialFields = sceneBox.lookupAll("#specialField");
    Set<Node> texts = sceneBox.lookupAll("#label");
    Set<Node> tableCols = sceneBox.lookupAll("#col");

    if (isDark) {
      darkSwitch.setImage(
          new Image(
              getClass()
                  .getClassLoader()
                  .getResource("edu/wpi/DapperDaemons/assets/Glyphs/sun.png")
                  .toString()));

      for (Node back : backs) {
        back.getStyleClass().add("backgroundDark");
      }

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

      for (Node back : backs) {
        back.getStyleClass().remove("backgroundDark");
      }

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
  public void openUserSettings() throws IOException {
    switchScene("userSettings.fxml", 941, 592);
  }

  @FXML
  public void goToServicePage() {
    try {
      switchScene("serviceRequestPage.fxml", 635, 510);
    } catch (Exception e) {
      // TODO: maybe fix this one day?
    }
  }

  /**
   * Gets all long names
   *
   * @return a list of long names
   */
  protected List<String> getAllLongNames() {
    List<String> names = new ArrayList<>();
    for (Location loc : new ArrayList<Location>(DAOPouch.getLocationDAO().getAll().values())) {
      names.add(loc.getLongName());
    }
    return names;
  }
}
