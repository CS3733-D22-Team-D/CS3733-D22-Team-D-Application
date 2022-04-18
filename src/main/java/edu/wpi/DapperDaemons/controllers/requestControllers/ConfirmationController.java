package edu.wpi.DapperDaemons.controllers.requestControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ConfirmationController {
  /*ErrorPopup*/
  @FXML VBox errorPopup;

  /*Label*/
  @FXML Label errorText;

  /*Button*/
  @FXML Button tryAgainButton;

  /*Button*/
  @FXML Button tryAgainButton1;


  @FXML
  public boolean cancel(MouseEvent event) {
    errorPopup.setVisible(false);
    return false;
  }



  @FXML
  public boolean confirm(MouseEvent event) {
    errorPopup.setVisible(false);
    return true;
  }

}
