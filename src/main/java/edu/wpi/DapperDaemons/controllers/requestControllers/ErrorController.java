package edu.wpi.DapperDaemons.controllers.requestControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ErrorController {
  /*ErrorPopup*/
  @FXML VBox errorPopup;

  /*Label*/
  @FXML Label errorText;

  /*Button*/
  @FXML Button tryAgainButton;

  @FXML
  public void hideError(MouseEvent event) {
    errorPopup.setVisible(false);
  }
}
