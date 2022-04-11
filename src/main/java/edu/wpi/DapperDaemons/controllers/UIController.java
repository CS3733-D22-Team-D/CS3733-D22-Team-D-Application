package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXHamburger;
import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.csvSaver;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.TableObject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
Contains methods needed for all UI pages
 */
public abstract class UIController implements Initializable {

  /* JFX Variable */
  @FXML private ImageView homeIcon;
  @FXML private JFXHamburger burg;
  @FXML private JFXHamburger burgBack;
  @FXML private VBox slider;
  @FXML private VBox sceneBox;
  @FXML private ImageView darkSwitch;

  private boolean isDark = false;

  /* DAO Object to access all room numbers */
  List<Location> locations;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    DAO<Location> dao = DAOPouch.getLocationDAO();
    /* Used the DAO object to get list */
    try {
      this.locations = dao.getAll();
    } catch (Exception e) {
      this.locations = new ArrayList<>();
    }
    menuSlider(slider, burg, burgBack);
  }

  @FXML
  public void quitProgram() {
    Stage window = (Stage) homeIcon.getScene().getWindow();

    //    try {
    //      DAO<Location> closer = DAOPouch.getLocationDAO();
    //      DAO<MedicalEquipmentRequest> closer2 = DAOPouch.getMedicalEquipmentRequestDAO();
    //      closer.save("TowerLocationsSave.csv");
    //      closer2.save("MedEquipReqSave.csv");
    //      System.out.println("Saving CSV Files");
    //    } catch (Exception e) {
    //      e.printStackTrace();
    //    }
    csvSaver.saveAll();
    window.close();
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
  public void toggleTheme() {
    Node back = homeIcon.getScene().lookup("#background");
    if (!isDark) {
      darkSwitch.setImage(
          new Image(
              getClass()
                  .getClassLoader()
                  .getResource("edu/wpi/DapperDaemons/assets/Glyphs/sun.png")
                  .toString()));

      back.getStyleClass().add("backgroundDark");

      Set<Node> fields = back.lookupAll("#field");
      Set<Node> fores = back.lookupAll("#foreground");
      Set<Node> jButtons = back.lookupAll("#jButton");
      Set<Node> specialFields = back.lookupAll("#specialField");
      Set<Node> texts = back.lookupAll("#label");

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

    } else {
      darkSwitch.setImage(
          new Image(
              getClass()
                  .getClassLoader()
                  .getResource("edu/wpi/DapperDaemons/assets/Glyphs/moon.png")
                  .toString()));

      back.getStyleClass().remove("backgroundDark");

      Set<Node> fields = back.lookupAll("#field");
      Set<Node> fores = back.lookupAll("#foreground");
      Set<Node> jButtons = back.lookupAll("#jButton");
      Set<Node> specialFields = back.lookupAll("#specialField");
      Set<Node> texts = back.lookupAll("#label");

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
    }
    isDark = !isDark;
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
  public void goToLogin() throws IOException {
    switchScene("login.fxml", 780, 548);
  }

  protected void switchScene(String fileName, int minWidth, int minHeight) throws IOException {
    Parent root =
        FXMLLoader.load(Objects.requireNonNull(App.class.getResource("views/" + fileName)));
    Stage window = (Stage) homeIcon.getScene().getWindow();
    window.setMinWidth(minWidth);
    window.setMinHeight(minHeight);

    double width = sceneBox.getPrefWidth();
    double height = sceneBox.getPrefHeight();
    window.setScene(new Scene(root));
    sceneBox.setPrefWidth(width);
    sceneBox.setPrefHeight(height);
    window.setWidth(window.getWidth() + 0.0); // To update size
    window.setHeight(window.getHeight());
  }

  @FXML
  public void goHomeDark() throws IOException {
    switchScene("defaultDark.fxml", 635, 510);
  }

  @FXML
  public void switchToMedicineDark() throws IOException {
    switchScene("medicineDark.fxml", 842, 530);
  }

  @FXML
  public void switchToMapDark() throws IOException {
    switchScene("locationMapDark.fxml", 100, 100);
  }

  /**
   * Gets all long names
   *
   * @return a list of long names
   */
  public List<String> getAllLongNames() {
    List<String> names = new ArrayList<String>();
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
}
