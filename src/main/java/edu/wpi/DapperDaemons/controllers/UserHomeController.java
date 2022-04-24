package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.DAOFacade;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.backend.SecurityController;
import edu.wpi.DapperDaemons.controllers.homePage.AccountHandler;
import edu.wpi.DapperDaemons.entities.Notification;
import edu.wpi.DapperDaemons.entities.requests.Request;
import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class UserHomeController extends ParentController {

  // account profile
  @FXML private Circle profilePic;
  @FXML private Text accountName;
  @FXML private Text accountUserName;
  @FXML private Label email;
  @FXML private Label employeeID;

  // buttons
  @FXML private VBox reqContainer;
  @FXML private ImageView reqImage;
  @FXML private VBox notContainer;
  @FXML private ImageView notImage;
  @FXML private VBox settingContainer;
  @FXML private ImageView settingImage;
  @FXML private Label requestNum;
  @FXML private Label notificationNum;
  @FXML private Circle requestNot;
  @FXML private Circle notificationNot;

  // switch pages
  @FXML
  public void openAboutUs() {
    swapPage("aboutUs", "About Us");
  }

  @FXML
  public void goUserHome() {
    swapPage("NewUserHome", "User Home");
  }

  @FXML
  public void openAccountSettings() {
    swapPage("userAccountSettings", "User Account Settings");
  }

  @FXML
  public void openUserSecurity() {
    swapPage("userSecurity", "User Security");
  }

  @FXML // TODO: make notifications page?
  public void openNotifications() {
    swapPage("", "Notifications");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //    super.initialize(location, resources);

    initButtons();

    // set employee name and username
    String employeeName =
        SecurityController.getUser().getFirstName()
            + " "
            + SecurityController.getUser().getLastName();
    accountName.setText(employeeName);

    accountUserName.setText(DAOFacade.getUserAccount().getAttribute(1));

    // set profile picture
    new AccountHandler(accountName, profilePic);

    //    profilePic.setFill(
    //        new ImagePattern(
    //            new Image(
    //                Objects.requireNonNull(
    //                    getClass()
    //                        .getClassLoader()
    //                        .getResourceAsStream(
    //                            "edu/wpi/DapperDaemons/profilepictures/"
    //                                + SecurityController.getUser().getNodeID()
    //                                + ".png")))));

    // set email
    email.setText(DAOFacade.getUserAccount().getAttribute(7));

    // set notifications
    String empID = DAOFacade.getUserAccount().getAttribute(2);
    int reqNum = getNumRequests(empID);
    int notNum = getNumNotifications(empID);
    if (reqNum != 0) {
      requestNum.setVisible(true);
      requestNot.setVisible(true);
      requestNum.setText("" + reqNum);
    } else {
      requestNum.setVisible(false);
      requestNot.setVisible(false);
    }
    if (notNum != 0) {
      notificationNum.setVisible(true);
      notificationNot.setVisible(true);
      notificationNum.setText("" + notNum);
    } else {
      notificationNum.setVisible(false);
      notificationNot.setVisible(false);
    }

    // set employeeID
    employeeID.setText(empID);
  }

  private int getNumRequests(String employeeID) {
    int numReq = 0;
    List<Request> requests = DAOFacade.getAllRequests();
    for (Request req : requests) {
      if (req.getAssigneeID().equals(employeeID)) {
        numReq++;
      }
    }
    return numReq;
  }

  private int getNumNotifications(String employeeID) {
    Map<String, Notification> notifications = DAOPouch.getNotificationDAO().filter(2, employeeID);
    return notifications.size();
  }

  private void initButtons() {
    bindImage(reqImage, reqContainer);
    bindImage(notImage, notContainer);
    bindImage(settingImage, settingContainer);
  }
}
