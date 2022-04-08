package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXHamburger;
import edu.wpi.DapperDaemons.App;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class easterEggController extends UIController {
  @FXML ImageView homeIcon;
  @FXML private VBox sceneBox;
  static Thread sound;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    try {
      playSound("easterEgg.wav");
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    }
  }

  public static synchronized void playSound(final String url) throws LineUnavailableException {
    sound =
        new Thread(
            new Runnable() {
              // The wrapper thread is unnecessary, unless it blocks on the
              // Clip finishing; see comments.
              Clip clip = AudioSystem.getClip();

              public void stop() {
                clip.stop();
              }

              public void run() {
                try {
                  AudioInputStream inputStream =
                      AudioSystem.getAudioInputStream(
                          Objects.requireNonNull(
                              easterEggController
                                  .class
                                  .getClassLoader()
                                  .getResourceAsStream(
                                      "edu/wpi/DapperDaemons/assets/easterEgg.wav")));
                  clip.open(inputStream);
                  clip.start();
                } catch (Exception e) {
                  System.err.println(e.getMessage());
                }
                while (!Thread.interrupted()) ;
                stop();
              }
            });
    sound.start();
  }

  @FXML
  public void stopEasterEgg() {
    sound.interrupt();
  }

  private void switchScene(String fileName, int minWidth, int minHeight) throws IOException {
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
  public void goHome() throws IOException {
    stopEasterEgg();
    switchScene("default.fxml", 635, 510);
  }

  @FXML
  public void switchToEquipment() throws IOException {
    stopEasterEgg();
    switchScene("equipment.fxml", 761, 626);
  }

  @FXML
  public void switchToLabRequest() throws IOException {
    stopEasterEgg();
    switchScene("labRequest.fxml", 575, 575);
  }

  @FXML
  public void switchToMeal() throws IOException {
    stopEasterEgg();
    switchScene("meal.fxml", 500, 500);
  }

  @FXML
  public void switchToMedicine() throws IOException {
    stopEasterEgg();
    switchScene("medicine.fxml", 842, 530);
  }

  @FXML
  public void switchToDB() throws IOException {
    stopEasterEgg();
    switchScene("backendInfoDisp.fxml", 842, 530);
  }

  @FXML
  public void switchToMap() throws IOException {
    stopEasterEgg();
    switchScene("locationMap.fxml", 100, 100);
  }

  @FXML
  public void switchToPatientTransport() throws IOException {
    stopEasterEgg();
    switchScene("patientTransport.fxml", 831, 582);
  }

  @FXML
  public void switchToSanitation() throws IOException {
    stopEasterEgg();
    switchScene("sanitation.fxml", 780, 548);
  }

  @FXML
  public void goHomeDark() throws IOException {
    stopEasterEgg();
    switchScene("defaultDark.fxml", 635, 510);
  }

  @FXML
  public void switchToEquipmentDark() throws IOException {
    stopEasterEgg();
    switchScene("equipmentDark.fxml", 761, 626);
  }

  @FXML
  public void switchToLabRequestDark() throws IOException {
    stopEasterEgg();
    switchScene("labRequestDark.fxml", 575, 575);
  }

  @FXML
  public void switchToMealDark() throws IOException {
    stopEasterEgg();
    switchScene("mealDark.fxml", 500, 500);
  }

  @FXML
  public void switchToMedicineDark() throws IOException {
    stopEasterEgg();
    switchScene("medicineDark.fxml", 842, 530);
  }

  @FXML
  public void switchToMapDark() throws IOException {
    stopEasterEgg();
    switchScene("locationMapDark.fxml", 100, 100);
  }

  @FXML
  public void switchToPatientTransportDark() throws IOException {
    stopEasterEgg();
    switchScene("patientTransportDark.fxml", 831, 582);
  }

  @FXML
  public void switchToSanitationDark() throws IOException {
    stopEasterEgg();
    switchScene("sanitationDark.fxml", 780, 548);
  }

  @FXML
  public void switchToDBDark() throws IOException {
    stopEasterEgg();
    switchScene("backendInfoDispDark.fxml", 842, 530);
  }
}
