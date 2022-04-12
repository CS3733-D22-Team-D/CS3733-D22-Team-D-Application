package edu.wpi.DapperDaemons.loadingScreen;

import edu.wpi.DapperDaemons.controllers.loadingScreenController;
import java.io.IOException;
import java.util.Objects;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoadingScreen {
  Stage primaryStage;

  public LoadingScreen(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public void display(Runnable init, Runnable display) throws IOException {
    new Thread(
            () -> {
              Platform.runLater(
                  () -> {
                    try {
                      Parent root =
                          FXMLLoader.load(
                              Objects.requireNonNull(
                                  LoadingScreen.class
                                      .getClassLoader()
                                      .getResource(
                                          "edu/wpi/DapperDaemons/views/loadingScreen.fxml")));
                      Scene scene = new Scene(root);
                      primaryStage.setMinWidth(635);
                      primaryStage.setMinHeight(510);
                      primaryStage.setScene(scene);
                      primaryStage.show();
                    } catch (IOException e) {
                      e.printStackTrace();
                    }
                  });
              new Thread(
                      () -> {
                        init.run();
                        Platform.runLater(
                            () -> {
                              display.run();
                              stop();
                            });
                      })
                  .start();
            })
        .start();
  }

  public void stop() {
    loadingScreenController.stop();
  }
}
