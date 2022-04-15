package edu.wpi.DapperDaemons.backend;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FireBase {

  private static DatabaseReference ref;

  private FireBase() {}

  public static DatabaseReference getReference() {
    return ref;
  }

  public static void init() {
    FileInputStream serviceAccount = null;
    try {
      serviceAccount = new FileInputStream("C:/Users/jrmad/Downloads/service-account.json");
    } catch (FileNotFoundException e) {
      System.out.println("Service account json not found");
    }

    FirebaseOptions options = null;
    // Initialize the app with a service account, granting admin privileges
    try {
      options =
          FirebaseOptions.builder()
              .setCredentials(GoogleCredentials.fromStream(serviceAccount))
              // The database URL depends on the location of the database
              .setDatabaseUrl("https://bwh-application-default-rtdb.firebaseio.com/")
              .build();
    } catch (IOException e) {
      System.out.println("Connection to the database could not be secured");
    }

    if (options != null) {
      FirebaseApp.initializeApp(options);

      ref = FirebaseDatabase.getInstance().getReference();
    }
  }
}
