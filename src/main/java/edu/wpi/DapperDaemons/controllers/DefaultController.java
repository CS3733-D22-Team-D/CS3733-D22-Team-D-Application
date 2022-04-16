package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.wongSweeper.MinesweeperZN;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/*
Manages Default Page Navigation
 */
public class DefaultController extends ParentController {

  /* Menu Button images */
  @FXML private Pane labPageContainer;
  @FXML private ImageView labPageImage;
  @FXML private Pane equipmentPageContainer;
  @FXML private ImageView equipmentPageImage;
  @FXML private Pane sanitationPageContainer;
  @FXML private ImageView sanitationPageImage;
  @FXML private Pane medicinePageContainer;
  @FXML private ImageView medicinePageImage;
  @FXML private Pane mealPageContainer;
  @FXML private ImageView mealPageImage;
  @FXML private Pane mapPageContainer;
  @FXML private ImageView mapPageImage;
  @FXML private Pane patientPageContainer;
  @FXML private ImageView patientPageImage;
  @FXML private Pane backendPageContainer;
  @FXML private ImageView backendPageImage;

  private static Timer timer;
  private static final int timeUpdate = 1;

  private static Timer weatherTimer;
  private static final int weatherUpdate = 300;

  private final List<KeyCode> easterEggSequence = new ArrayList<>();
  private int easterEggInd = 0;

  private long startTime;
  private int count = 0;

  public final Image EMBEDDED =
      new Image(
          Objects.requireNonNull(
              getClass()
                  .getClassLoader()
                  .getResourceAsStream("edu/wpi/DapperDaemons/assets/serverIcons/embedded.png")));
  public final Image SERVER =
      new Image(
          Objects.requireNonNull(
              getClass()
                  .getClassLoader()
                  .getResourceAsStream("edu/wpi/DapperDaemons/assets/serverIcons/server.png")));

  public final Image LOAD =
      new Image(
          Objects.requireNonNull(
              DefaultController.class
                  .getClassLoader()
                  .getResourceAsStream("edu/wpi/DapperDaemons/assets/loading.gif")));

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initGraphics();
    initSequence();
  }

  private void initGraphics() {
    bindImage(labPageImage, labPageContainer);
    bindImage(equipmentPageImage, equipmentPageContainer);
    bindImage(sanitationPageImage, sanitationPageContainer);
    bindImage(medicinePageImage, medicinePageContainer);
    bindImage(mealPageImage, mealPageContainer);
    bindImage(mapPageImage, mapPageContainer);
    bindImage(patientPageImage, patientPageContainer);
    bindImage(backendPageImage, backendPageContainer);
    //    initConnectionImage();
  }

  private void initSequence() {
    easterEggSequence.add(KeyCode.UP);
    easterEggSequence.add(KeyCode.UP);
    easterEggSequence.add(KeyCode.DOWN);
    easterEggSequence.add(KeyCode.DOWN);
    easterEggSequence.add(KeyCode.LEFT);
    easterEggSequence.add(KeyCode.RIGHT);
    easterEggSequence.add(KeyCode.LEFT);
    easterEggSequence.add(KeyCode.RIGHT);
    easterEggSequence.add(KeyCode.B);
    easterEggSequence.add(KeyCode.A);
    easterEggSequence.add(KeyCode.ENTER);
  }

  @FXML
  public void konami(KeyEvent e) {
    if (e.getCode().equals(easterEggSequence.get(easterEggInd))) {
      easterEggInd++;
      if (easterEggInd == easterEggSequence.size()) {
        easterEggInd = 0;
        try {
          //          switchScene("konami.fxml", 700, 500);
          MinesweeperZN ms = new MinesweeperZN();
          ms.begin(new Stage());
        } catch (Exception ex) {
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
