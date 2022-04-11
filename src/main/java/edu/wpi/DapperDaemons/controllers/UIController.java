package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXHamburger;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.backend.csvSaver;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.TableObject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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

  static void menuSlider(VBox slider, JFXHamburger burg, JFXHamburger burgBack) {
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
  public void openUserDropdown() {
    userDropdown.setVisible(userSettingsToggle.isSelected());
  }

  @FXML
  public void openUserSettings() {
    // TODO : Create a userSettings.fxml page
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
    switchScene("locationMap.fxml", 100, 100);
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
  public void goHomeDark() throws IOException {
    switchScene("defaultDark.fxml", 635, 510);
  }

  @FXML
  public void switchToEquipmentDark() throws IOException {
    switchScene("equipmentDark.fxml", 761, 626);
  }

  @FXML
  public void switchToLabRequestDark() throws IOException {
    switchScene("labRequestDark.fxml", 575, 575);
  }

  @FXML
  public void switchToMealDark() throws IOException {
    switchScene("mealDark.fxml", 500, 500);
  }

  @FXML
  public void switchToMedicineDark() throws IOException {
    switchScene("medicineDark.fxml", 842, 530);
  }

  @FXML
  public void switchToMapDark() throws IOException {
    switchScene("locationMapDark.fxml", 100, 100);
  }

  @FXML
  public void switchToPatientTransportDark() throws IOException {
    switchScene("patientTransportDark.fxml", 831, 582);
  }

  @FXML
  public void switchToSanitationDark() throws IOException {
    switchScene("sanitationDark.fxml", 780, 548);
  }

  @FXML
  public void switchToDBDark() throws IOException {
    switchScene("backendInfoDispDark.fxml", 842, 530);
  }

  protected List<String> getAllLongNames() {
    List<String> names = new ArrayList<>();
    for (Location loc : this.locations) {
      names.add(loc.getLongName());
    }
    return names;
  }

  protected void saveToCSV(TableObject type) {
    FileChooser fileSys = new FileChooser();
    Stage window = (Stage) homeIcon.getScene().getWindow();
    fileSys.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
    File csv = fileSys.showSaveDialog(window);
    try {
      csvSaver.save(type, csv.getAbsolutePath());
    } catch (Exception e) {
      System.err.println("Unable to Save CSV of type: " + type);
    }
  }

  public void bindImage(ImageView pageImage, Pane parent) {
    pageImage.fitHeightProperty().bind(parent.heightProperty());
    pageImage.fitWidthProperty().bind(parent.widthProperty());
  }
}
