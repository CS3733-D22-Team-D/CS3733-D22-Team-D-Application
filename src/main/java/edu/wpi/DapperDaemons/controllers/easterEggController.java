package edu.wpi.DapperDaemons.controllers;

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
  public void stopEasterEgg() throws IOException, InterruptedException {
    sound.interrupt();
    switchScene("default.fxml", 635, 510);
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
}
