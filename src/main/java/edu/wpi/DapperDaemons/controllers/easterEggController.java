package edu.wpi.DapperDaemons.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class easterEggController extends UIController {

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);

    playSound("easterEgg.wav");
  }

  public static synchronized void playSound(final String url) {
    new Thread(
            new Runnable() {
              // The wrapper thread is unnecessary, unless it blocks on the
              // Clip finishing; see comments.
              public void run() {
                try {
                  Clip clip = AudioSystem.getClip();
                  AudioInputStream inputStream =
                      AudioSystem.getAudioInputStream(
                          easterEggController.class.getResourceAsStream(
                              "/resources/edu/wpi/DapperDaemons/assets" + url));
                  clip.open(inputStream);
                  clip.start();
                } catch (Exception e) {
                  System.err.println(e.getMessage());
                }
              }
            })
        .start();
  }
}
