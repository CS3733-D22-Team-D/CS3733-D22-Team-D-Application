package edu.wpi.DapperDaemons.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/*
Manages Default Page Navigation
 */
public class DefaultController extends UIController {
  @FXML private ImageView homeIcon;
  @FXML private VBox sceneBox;
  @FXML private Label time;

  private SimpleStringProperty currentTime = new SimpleStringProperty("Something Broke :(");
  private String currTime;

  Thread timeThread;

  long startTime;
  int count = 0;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
        time.textProperty().bind(currentTime);
//    Bindings.bindBidirectional(time.textProperty(), currentTime);
    timeThread =
        new Thread(
            () -> {
              SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyy HH:mm:ss");
              while (!Thread.interrupted()) {
                Date now = new Date();
                now.getTime();

//                currentTime = new SimpleStringProperty(formatter.format(now));
//                System.out.println(currentTime);

                //                                time.textProperty().bind(currentTime);
                //                currTime = formatter.format(now);
                //                time.textProperty().bind(new SimpleStringProperty(currTime));
                //                time.setText(formatter.format(now));
                try {
                  TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            });
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
