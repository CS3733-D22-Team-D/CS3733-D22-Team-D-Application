package edu.wpi.DapperDaemons;

import edu.wpi.DapperDaemons.loadingScreen.loadingScreen;
import java.io.IOException;
import javafx.application.Application;
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
    loadingScreen.display(primaryStage);

    //    new Thread(
    //            new Runnable() {
    //              @Override
    //              public void run() {
    //                Platform.runLater(
    //                    new Runnable() {
    //                      @Override
    //                      public void run() {
    //                        try {
    //                          loadingScreen.displayLoadingScreen(primaryStage);
    //                        } catch (IOException e) {
    //                          e.printStackTrace();
    //                        }
    //                      }
    //                    });
    //                new Thread(
    //                        () -> {
    //                          // initialize things
    //                          connectionHandler.init();
    //                          connectionHandler.switchToClientServer();
    //                          AutoSave.start(10);
    //                          Platform.runLater(
    //                              () -> {
    //                                Parent root = null;
    //                                try {
    //                                  root =
    //                                      FXMLLoader.load(
    //                                          Objects.requireNonNull(
    //                                              getClass().getResource("views/login.fxml")));
    //                                } catch (IOException e) {
    //                                  e.printStackTrace();
    //                                }
    //                                Scene scene = new Scene(root);
    //                                primaryStage.setMinWidth(635);
    //                                primaryStage.setMinHeight(510);
    //                                primaryStage.setScene(scene);
    //                                primaryStage.show();
    //                                primaryStage
    //                                    .getIcons()
    //                                    .add(
    //                                        new Image(
    //                                            String.valueOf(
    //                                                App.class.getResource(
    //                                                    "assets/"
    //                                                        +
    // "Brigham_and_Womens_Hospital_logo.png"))));
    //                                primaryStage.setTitle("BWH");
    //                                loadingScreen.stop();
    //                              });
    //                        })
    //                    .start();
    //              }
    //            })
    //        .start();
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
