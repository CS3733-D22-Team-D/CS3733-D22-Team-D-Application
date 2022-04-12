package edu.wpi.DapperDaemons.loadingScreen;

import edu.wpi.DapperDaemons.controllers.loadingScreenController;
import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class loadingScreen {
  Stage primaryStage;

  public loadingScreen(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public void display() throws IOException {
    Parent root =
        FXMLLoader.load(
            Objects.requireNonNull(
                getClass().getClassLoader().getResource("views/loadingScreen.fxml")));
    Scene scene = new Scene(root);
    primaryStage.setMinWidth(635);
    primaryStage.setMinHeight(510);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public void stop() {
    loadingScreenController.stop();
  }
}
