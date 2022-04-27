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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
    if (!n.getRead()) {
      notif.getChildren().get(6).setVisible(true);
      HBox finalNotif = notif;
      ((VBox) notif.getChildren().get(6))
          .getChildren()
          .get(0)
          .setOnMouseClicked(
              e -> {
                n.setAttribute(5, "true");
                finalNotif.getChildren().get(6).setVisible(false);
                DAOPouch.getNotificationDAO().add(n);
              });
    } else {
      notif.getChildren().get(6).setVisible(false);
    }
    notif.getChildren().get(0).setVisible(!n.getRead());
    //        ((ImageView)notif.getChildren().get(1)).setImage();
    List<Request> reqs = DAOFacade.getAllRequests();
    for (Request r : reqs) {
      if (n.getNodeID().equals("not" + r.getNodeID()) && !r.getAssigneeID().equals("none")) {
        ((Label) ((VBox) notif.getChildren().get(5)).getChildren().get(1))
            .setText(r.getDateNeeded());
        ((Label) ((VBox) notif.getChildren().get(3)).getChildren().get(0))
            .setText(
                "Requester: "
                    + DAOPouch.getEmployeeDAO().get(r.getRequesterID()).getFirstName()
                    + " "
                    + DAOPouch.getEmployeeDAO().get(r.getRequesterID()).getLastName());
        Background b =
            new Background(
                new BackgroundFill(new Color(1, 1, 1, 1), CornerRadii.EMPTY, Insets.EMPTY));
        switch (r.getPriority()) {
          case LOW:
            b =
                new Background(
                    new BackgroundFill(
                        Color.color(.47, .87, .47, .8), new CornerRadii(5), Insets.EMPTY));
            break;
          case MEDIUM:
            b =
                new Background(
                    new BackgroundFill(
                        Color.color(.96, .93, .26, .8), new CornerRadii(5), Insets.EMPTY));
            break;
          case HIGH:
            b =
                new Background(
                    new BackgroundFill(
                        Color.color(.98, .41, .38, .8), new CornerRadii(5), Insets.EMPTY));
            break;
          case OVERDUE:
            b =
                new Background(
                    new BackgroundFill(
                        Color.color(0, 0, 0, 0.5), new CornerRadii(5), Insets.EMPTY));
            break;
          default:
            break;
        }
        ((HBox) notif.getChildren().get(2)).setBackground(b);
        break;
      }
    }
    ((Label) ((VBox) notif.getChildren().get(3)).getChildren().get(1)).setText(n.getBody());
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
    this.earlierBox.getChildren().clear();
    List<Notification> notifications =
        new ArrayList<>(
            DAOPouch.getNotificationDAO()
                .filter(2, SecurityController.getUser().getAttribute(1))
                .values());
    SimpleDateFormat f = new SimpleDateFormat("MMddyyy");
    for (Notification n : notifications) {
      try {
        if (f.parse(n.getDate()).before(f.parse(f.format(new Date())))) {
          this.earlierBox.getChildren().add(createNotification(n));
        } else {
          this.todayBox.getChildren().add(createNotification(n));
        }
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
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
