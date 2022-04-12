package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.backend.csvSaver;
import edu.wpi.DapperDaemons.entities.TableObject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AppController implements Initializable {

  @FXML protected Node mainNode;
  @FXML private VBox error;
  @FXML private StackPane windowContents;
  @FXML private VBox sceneBox;
  private boolean updatedSize;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      error =
          FXMLLoader.load(
              Objects.requireNonNull(App.class.getResource("views/" + "errorMessage.fxml")));
    } catch (IOException e) {
      e.printStackTrace();
    }
    error.setVisible(false);
    error.setPickOnBounds(false);
    HBox errorContainer = new HBox();
    errorContainer.setPickOnBounds(false);
    windowContents.getChildren().add(errorContainer);
    errorContainer.getChildren().add(error);
    errorContainer.setAlignment(Pos.CENTER);
    errorContainer.setPadding(new Insets(20, 20, 20, 20));
  }

  /** Creates an error box pop-up on the screen */
  @FXML
  protected void showError(String errorMessage) {
    error.setVisible(true);
    Node nodeOut = error.getChildren().get(1);
    if (nodeOut instanceof VBox) {
      for (Node nodeIn : ((VBox) nodeOut).getChildren()) {
        if (nodeIn instanceof Label) {
          ((Label) nodeIn).setText(errorMessage);
        }
      }
    }
  }

  /** Creates an error box pop-up based on a specific location */
  @FXML
  protected void showError(String errorMessage, Pos pos) {
    ((HBox) error.getParent()).setAlignment(pos);
    showError(errorMessage);
  }

  protected void switchScene(String fileName, int minWidth, int minHeight) throws IOException {
    Parent root =
        FXMLLoader.load(Objects.requireNonNull(App.class.getResource("views/" + fileName)));
    Stage window = (Stage) sceneBox.getScene().getWindow();
    window.setMinWidth(minWidth);
    window.setMinHeight(minHeight);
    window.setOnCloseRequest(e -> quitProgram());
    double width = sceneBox.getPrefWidth();
    double height = sceneBox.getPrefHeight();
    window.setScene(new Scene(root));
    sceneBox.setPrefWidth(width);
    sceneBox.setPrefHeight(height);
    if (updatedSize) window.setWidth(window.getWidth() + 0.001);
    else window.setWidth(window.getWidth() - 0.001); // To update size because JavaFX is bad
    window.setHeight(window.getHeight());
  }

  @FXML
  protected void quitProgram() {
    Stage window = (Stage) sceneBox.getScene().getWindow();
    csvSaver.saveAll();
    if (window != null) window.close();
    Platform.exit();
    System.exit(0);
  }

  public static void bindImage(ImageView pageImage, Pane parent) {
    pageImage.fitHeightProperty().bind(parent.heightProperty());
    pageImage.fitWidthProperty().bind(parent.widthProperty());
  }

  protected void saveToCSV(TableObject type) {
    FileChooser fileSys = new FileChooser();
    Stage window = (Stage) sceneBox.getScene().getWindow();
    fileSys.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
    File csv = fileSys.showSaveDialog(window);
    try {
      csvSaver.save(type, csv.getAbsolutePath());
    } catch (Exception e) {
      System.err.println("Unable to Save CSV of type: " + type);
    }
  }
}