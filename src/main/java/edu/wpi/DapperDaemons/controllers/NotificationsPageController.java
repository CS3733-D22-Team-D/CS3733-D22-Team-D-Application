package edu.wpi.DapperDaemons.controllers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.backend.*;
import edu.wpi.DapperDaemons.entities.Notification;
import edu.wpi.DapperDaemons.entities.requests.Request;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class NotificationsPageController extends ParentController {
  @FXML private VBox todayBox;
  @FXML private VBox earlierBox;
  private static ValueEventListener notifListener;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setNotifications();
    setNotificationListener();
  }

  public HBox createNotification(Notification n) {
    HBox notif = new HBox();
    try {
      notif =
          FXMLLoader.load(
              Objects.requireNonNull(
                  App.class.getResource("views/" + "notificationNode" + ".fxml")));
    } catch (IOException ignored) {
    }
    notif.setOnMouseClicked(
        event -> {
          n.setAttribute(5, "true"); // sets action when clicking on notification
          DAOPouch.getNotificationDAO().add(n);
        });
    notif.getChildren().get(0).setVisible(!n.getRead());
    //        ((ImageView)notif.getChildren().get(1)).setImage();
    List<Request> reqs = DAOFacade.getAllRequests();
    for (Request r : reqs) {
      if (n.getNodeID().equals("not" + r.getNodeID())) {
        ((Label) ((VBox) notif.getChildren().get(4)).getChildren().get(0))
            .setText(DAOPouch.getEmployeeDAO().get(r.getAssigneeID()).getFirstName());
        switch (r.getPriority()) {
          case LOW:
            ((HBox) notif.getChildren().get(2))
                .setBackground(
                    new Background(
                        new BackgroundFill(
                            Color.color(.47, .87, .47, .8),
                            new CornerRadii(0, 10, 10, 0, false),
                            Insets.EMPTY)));
            break;
          case MEDIUM:
            ((HBox) notif.getChildren().get(2))
                .setBackground(
                    new Background(
                        new BackgroundFill(
                            Color.color(.96, .93, .26, .8),
                            new CornerRadii(0, 10, 10, 0, false),
                            Insets.EMPTY)));
            break;
          case HIGH:
            ((HBox) notif.getChildren().get(2))
                .setBackground(
                    new Background(
                        new BackgroundFill(
                            Color.color(.98, .41, .38, .8),
                            new CornerRadii(0, 10, 10, 0, false),
                            Insets.EMPTY)));
            break;
          case OVERDUE:
            ((HBox) notif.getChildren().get(2))
                .setBackground(
                    new Background(
                        new BackgroundFill(
                            Color.color(0, 0, 0, 1),
                            new CornerRadii(0, 10, 10, 0, false),
                            Insets.EMPTY)));
            break;
          default:
            break;
        }
        break;
      }
    }
    ((Label) ((VBox) notif.getChildren().get(4)).getChildren().get(1)).setText(n.getBody());
    return notif;
  }

  private void setNotificationListener() {
    if (ConnectionHandler.getType().equals(ConnectionHandler.connectionType.CLOUD)) {
      DatabaseReference ref = FireBase.getReference().child("NOTIFICATIONS");
      notifListener =
          new ValueEventListener() {
            @Override
            public synchronized void onDataChange(DataSnapshot snapshot) {
              new Thread(
                      () -> {
                        try {
                          Thread.sleep(100);
                        } catch (InterruptedException e) {
                          throw new RuntimeException(e);
                        }
                        Platform.runLater(() -> setNotifications());
                      })
                  .start();
            }

            @Override
            public void onCancelled(DatabaseError error) {
              System.out.println("Cancelled in notification listener");
            }
          };
      ref.addValueEventListener(notifListener);
    }
  }

  private void setNotifications() {
    this.todayBox.getChildren().clear();
    List<Notification> notifications =
        new ArrayList<>(
            DAOPouch.getNotificationDAO()
                .filter(2, SecurityController.getUser().getAttribute(1))
                .values());

    for (Notification n : notifications) {
      this.todayBox.getChildren().add(createNotification(n));
    }
  }

  public ValueEventListener getListener() {
    return notifListener;
  }

  public static void removeListener() {
    DatabaseReference ref = FireBase.getReference().child("NOTIFICATIONS");
    ref.removeEventListener(notifListener);
  }
}
