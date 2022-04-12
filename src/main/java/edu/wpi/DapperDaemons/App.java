package edu.wpi.DapperDaemons;

import arduino.Arduino;
import edu.wpi.DapperDaemons.backend.AutoSave;
import edu.wpi.DapperDaemons.backend.connectionHandler;
import edu.wpi.DapperDaemons.controllers.RFIDPageController;
import edu.wpi.DapperDaemons.loadingScreen.LoadingScreen;
import edu.wpi.DapperDaemons.serial.ArduinoExceptions.UnableToConnectException;
import edu.wpi.DapperDaemons.serial.SerialCOM;
import java.io.IOException;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {
  private static final String brighamIMG =
      "file:src/main/resources/edu/wpi/DapperDaemons/assets/Brigham-Blurred.jpg";
  // Do not edit this file when implementing your UI design

  private LoadingScreen ls;

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    LoadingScreen ls = new LoadingScreen(primaryStage);
    ls.display(
        () -> {
          connectionHandler.init();
          connectionHandler.switchToClientServer();
          AutoSave.start(10);

          Arduino arduino;
          SerialCOM serialCOM = new SerialCOM();
          try {
            arduino = serialCOM.setupArduino(); // can throw UnableToConnectException
            RFIDPageController.COM = arduino.getPortDescription();
          } catch (UnableToConnectException e) {
            RFIDPageController.COM = null;
          }
        },
        () -> {
          Parent root = null;
          try {
            root =
                FXMLLoader.load(Objects.requireNonNull(getClass().getResource("views/login.fxml")));
          } catch (IOException e) {
            e.printStackTrace();
          }
          Scene scene = new Scene(root);
          primaryStage.setMinWidth(635);
          primaryStage.setMinHeight(510);
          primaryStage.setScene(scene);
          primaryStage.show();
          primaryStage
              .getIcons()
              .add(
                  new Image(
                      String.valueOf(
                          App.class.getResource(
                              "assets/" + "Brigham_and_Womens_Hospital_logo.png"))));
          primaryStage.setTitle("BWH");
        });
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
