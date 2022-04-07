/**
 * @author Jessie Baskauf and Ellie Mamantov Sets up the controller, loads the fxml file, and runs
 *     the application.
 */
package edu.wpi.DapperDaemons.easterEgg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class wongMan extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("default.fxml"));
    primaryStage.setTitle("WongMan");
    //    double sceneWidth = controller.getBoardWidth() + 20.0;
    //    double sceneHeight = controller.getBoardHeight() + 100.0;
    //    primaryStage.setScene(new Scene(root, sceneWidth, sceneHeight));
    primaryStage.show();
    root.requestFocus();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
