package edu.wpi.DapperDaemons.controllers.homePage;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import edu.wpi.DapperDaemons.App;
import edu.wpi.DapperDaemons.backend.*;
import edu.wpi.DapperDaemons.backend.preload.Images;
import edu.wpi.DapperDaemons.entities.Notification;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javax.sound.sampled.LineUnavailableException;

public class NotificationHandler {

  private static ValueEventListener notifListener;
  private final VBox notifications;
  private final ImageView notifBell;

  public NotificationHandler(VBox notificationsBox, ImageView notifBell) {
    this.notifications = notificationsBox;
    this.notifBell = notifBell;
    setNotificationListener();
  }

  public void addNotification(Notification n) {
    SoundPlayer sp = new SoundPlayer("edu/wpi/DapperDaemons/notifications/Bloop.wav");
    try {
      sp.play();
    } catch (LineUnavailableException e) {
      throw new RuntimeException(e);
    }
    this.notifications.getChildren().add(createNotification(n));
  }

  public VBox createNotification(Notification n) {
    VBox notif = new VBox();
    try {
      notif =
          FXMLLoader.load(
              Objects.requireNonNull(App.class.getResource("views/" + "notification" + ".fxml")));
    } catch (IOException ignored) {
    }
    notif.setOnMouseClicked(
        event -> {
          System.out.println("Notif Handler");
          n.setAttribute(5, "true"); // sets action when clicking on notification
          DAOPouch.getNotificationDAO().add(n);
        });
    Label sub = (Label) notif.getChildren().get(0);
    sub.setText(n.getSubject());
    Label body = (Label) notif.getChildren().get(1);
    body.setText(n.getBody());
    //    notifications.getChildren().add(notif);
    //    Label notifSubject = new Label(n.getSubject());
    //    notifSubject.setFont(Font.font("System", FontWeight.BOLD, 14));
    //    Label notifBody = new Label(n.getBody());
    //    notifBody.setFont(Font.font("System", FontWeight.NORMAL, 11));
    //    notif.getChildren().add(notifSubject);
    //    notif.getChildren().add(notifBody);
    return notif;
  }

  private void setNotificationListener() {
    if (ConnectionHandler.getType().equals(ConnectionHandler.connectionType.CLOUD)) {
      DatabaseReference ref = FireBase.getReference().child("NOTIFICATIONS");
      notifListener =
          new ValueEventListener() {
            @Override
            public synchronized void onDataChange(DataSnapshot snapshot) {
              System.out.println(
                  "Notification listener for " + SecurityController.getUser().getAttribute(1));
              new Thread(() -> Platform.runLater(() -> setNotifications())).start();
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
    this.notifications.getChildren().clear();
    List<Notification> notifications =
        new ArrayList<>(
            DAOPouch.getNotificationDAO()
                .filter(2, SecurityController.getUser().getAttribute(1))
                .values());
    List<Notification> unRead =
        new ArrayList<>(DAOPouch.getNotificationDAO().filter(notifications, 5, "false").values());
    List<Notification> unReadUnChimed =
        new ArrayList<>(DAOPouch.getNotificationDAO().filter(unRead, 6, "false").values());
    if (unRead.size() == 0) {
      notifBell.setImage(Images.BELL);
      Text t = new Text();
      t.setText("Looks empty in here");
      this.notifications.getChildren().add(new Text("Looks empty in here"));
      return;
    }
    if (unRead.size() > 0) {
      notifBell.setImage(Images.UNREAD);
      if (unReadUnChimed.size() > 0) {
        SoundPlayer sp = new SoundPlayer("edu/wpi/DapperDaemons/notifications/Bloop.wav");
        try {
          sp.play();
        } catch (LineUnavailableException e) {
          throw new RuntimeException(e);
        }
        for (Notification n : unReadUnChimed) {
          n.setAttribute(6, "true");
          DAOPouch.getNotificationDAO().add(n);
        }
      }
      for (Notification n : unRead) {
        //        VBox notif = new VBox();
        //        Label notifSubject = new Label(n.getSubject());
        //        Label notifBody = new Label(n.getBody());
        //        notif.getChildren().add(notifSubject);
        //        notif.getChildren().add(notifBody);
        this.notifications.getChildren().add(createNotification(n));
      }
    }
  }

  public ValueEventListener getListener() {
    return notifListener;
  }
}
