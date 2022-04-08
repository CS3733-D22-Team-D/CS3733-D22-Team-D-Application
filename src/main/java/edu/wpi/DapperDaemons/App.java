package edu.wpi.DapperDaemons;

import edu.wpi.DapperDaemons.entities.*;
import edu.wpi.DapperDaemons.entities.requests.*;
import java.io.IOException;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {
  private static final String brighamIMG =
      "file:src/main/resources/edu/wpi/DapperDaemons/assets/Brigham-Blurred.jpg";
  // Do not edit this file when implementing your UI design

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Parent root =
        FXMLLoader.load(Objects.requireNonNull(getClass().getResource("views/default.fxml")));
    Scene scene = new Scene(root);
    primaryStage.setMinWidth(635);
    primaryStage.setMinHeight(510);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
