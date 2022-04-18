package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.backend.SoundPlayer;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javax.sound.sampled.LineUnavailableException;

public class EasterEggController extends AppController {
  @FXML ImageView homeIcon;
  @FXML private VBox sceneBox;
  private SoundPlayer player;

  /* Background */
  @FXML private ImageView BGImage;
  @FXML private Pane BGContainer;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    bindImage(BGImage, BGContainer);

    try {
      player = new SoundPlayer("edu/wpi/DapperDaemons/assets/easterEgg.wav");
      player.play(0.75F);
    } catch (LineUnavailableException e) {
      System.out.println("Something went wong");
    }
  }

  @FXML
  public void stopEasterEgg() {
    if (player != null) {
      player.stop();
    }
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
  public void switchToAboutUs() throws IOException {
    stopEasterEgg();
    switchScene("aboutUs.fxml", 780, 548);
  }

  @FXML
  public void goToLogin() throws IOException {
    stopEasterEgg();
    switchScene("login.fxml", 780, 548);
  }

  @FXML
  public void openUserSettings() throws IOException {
    stopEasterEgg();
    switchScene("userSettings.fxml", 941, 592);
  }

  @FXML
  public void logout() throws IOException {
    stopEasterEgg();
    switchScene("login.fxml", 575, 575);
    SecurityController.setUser(null);
  }
}
