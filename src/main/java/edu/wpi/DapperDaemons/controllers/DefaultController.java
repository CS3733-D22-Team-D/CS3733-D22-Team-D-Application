package edu.wpi.DapperDaemons.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

/*
Manages Default Page Navigation
 */
public class DefaultController extends UIController {

  @FXML private ImageView homeIcon;
  @FXML private VBox sceneBox;

  List<KeyCode> easterEggSequence = new ArrayList<>();
  int easterEggInd = 0;

  long startTime;
  int count = 0;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    easterEggSequence.add(KeyCode.U);
    easterEggSequence.add(KeyCode.P);
    easterEggSequence.add(KeyCode.U);
    easterEggSequence.add(KeyCode.P);
    easterEggSequence.add(KeyCode.D);
    easterEggSequence.add(KeyCode.O);
    easterEggSequence.add(KeyCode.W);
    easterEggSequence.add(KeyCode.N);
    easterEggSequence.add(KeyCode.D);
    easterEggSequence.add(KeyCode.O);
    easterEggSequence.add(KeyCode.W);
    easterEggSequence.add(KeyCode.N);
    easterEggSequence.add(KeyCode.L);
    easterEggSequence.add(KeyCode.E);
    easterEggSequence.add(KeyCode.F);
    easterEggSequence.add(KeyCode.T);
    easterEggSequence.add(KeyCode.R);
    easterEggSequence.add(KeyCode.I);
    easterEggSequence.add(KeyCode.G);
    easterEggSequence.add(KeyCode.H);
    easterEggSequence.add(KeyCode.T);
    easterEggSequence.add(KeyCode.L);
    easterEggSequence.add(KeyCode.E);
    easterEggSequence.add(KeyCode.F);
    easterEggSequence.add(KeyCode.T);
    easterEggSequence.add(KeyCode.R);
    easterEggSequence.add(KeyCode.I);
    easterEggSequence.add(KeyCode.G);
    easterEggSequence.add(KeyCode.H);
    easterEggSequence.add(KeyCode.T);
    easterEggSequence.add(KeyCode.A);
    easterEggSequence.add(KeyCode.B);
    easterEggSequence.add(KeyCode.S);
    easterEggSequence.add(KeyCode.T);
    easterEggSequence.add(KeyCode.A);
    easterEggSequence.add(KeyCode.R);
    easterEggSequence.add(KeyCode.T);
  }

  @FXML
  public void konami(KeyEvent e) {
    if (e.getCode().equals(easterEggSequence.get(easterEggInd))) {
      easterEggInd++;
      if (easterEggInd == easterEggSequence.size()) {
        easterEggInd = 0;
        try {
          switchScene("konami.fxml", 700, 500);
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    } else {
      easterEggInd = 0;
    }
  }

  @FXML
  public void easterEgg() throws IOException {
    if (count == 0) {
      startTime = System.currentTimeMillis();
    }
    count++;
    if ((System.currentTimeMillis() - startTime) > 10000) {
      count = 0;
    }
    if (count == 10 & (System.currentTimeMillis() - startTime) < 10000) {
      count = 0;
      switchScene("easterEgg.fxml", 761, 626);
    }
  }
}
