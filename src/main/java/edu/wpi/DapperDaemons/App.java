package edu.wpi.DapperDaemons;

import static edu.wpi.DapperDaemons.backend.ConnectionHandler.switchToEmbedded;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import edu.wpi.DapperDaemons.backend.*;
import edu.wpi.DapperDaemons.backend.loadingScreen.LoadingScreen;
import edu.wpi.DapperDaemons.backend.preload.Images;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import edu.wpi.DapperDaemons.entities.requests.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.net.ssl.HttpsURLConnection;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class App extends Application {
  private static final String brighamIMG =
      "file:src/main/resources/edu/wpi/DapperDaemons/assets/Brigham-Blurred.jpg";
  // Do not edit this file when implementing your UI design

  public static Logger LOG;
  private ValueEventListener valueEventListener;

  @Override
  public void init() {
    createLogger();
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) {
    //    CSVLoader.resetFirebase();
    startWithLoadingScreen(primaryStage);
  }

  private void startWithLoadingScreen(Stage primaryStage) {
    LoadingScreen ls = new LoadingScreen(primaryStage);
    try {
      ls.display(
          () -> {
            Images.init();
            HttpsURLConnection testCon = null;
            boolean connected = false;
            try {
              URL url = new URL("https://google.com");
              testCon = (HttpsURLConnection) url.openConnection();
              testCon.connect();
              connected = true;
            } catch (MalformedURLException e) {
              System.out.println("Not connected to the internet");
            } catch (IOException e) {
              System.out.println("Not connected to the internet");
            }
            if (connected) {
              FireBase.init();
              autoRequestsListeners();
              try {
                DAOPouch.init();
              } catch (IOException e) {
                System.out.println("DAOPouch could not initialize");
              }
            } else {
              switchToEmbedded();
            }

            AutoSave.start(10);
//            try { // this is to save everything from the firebase database
//              Thread.sleep(2000);
//            } catch (InterruptedException e) {
//              throw new RuntimeException(e);
//            }
//            CSVSaver.saveAll();
//            try {
//              CSVLoader.loadToFirebase(new MedicalEquipmentRequest(), "MedEquipReq.csv");
//            } catch (IOException e) {
//              throw new RuntimeException(e);
//            }
          },
          () -> {
            Parent root = null;
            try {
              root =
                  FXMLLoader.load(
                      Objects.requireNonNull(getClass().getResource("views/login.fxml")));
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
    } catch (IOException e) {
      System.out.println("Loading Screen broke :(");
    }
  }

  private void createLogger() {
    try {
      File logDir = new File(getClass().getClassLoader().getResource("logs").getPath());
      if (!logDir.exists()) logDir.mkdirs();

      Properties properties = new Properties();
      InputStream propFile =
          getClass().getClassLoader().getResourceAsStream("simplelogger.properties");
      properties.load(propFile);
    } catch (IOException e) {
      e.printStackTrace();
    }

    LOG = LoggerFactory.getLogger(App.class);
    LOG.info("TESTING LOG FILE");
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }

  // TODO : STOP THIS FROM ADDING THREE REQUESTS EVERYTIME: CHECK DAO

  /**
   * When there are six beds or more in a dirty area an alert appears on the dashboard. Service
   * requests are created to move the beds to the OR Park for cleaning.
   *
   * <p>For a floor, when there are 10 infusion pumps or more in a dirty area, or fewer than 5
   * infusion pumps in the clean area, an alert appears on the dashboard indicating that infusion
   * pumps need to be cleaned. Service requests are created to move the dirty infusion pumps on that
   * floor to the West Plaza for cleaning
   */
  public void autoRequestsListeners() {
    this.valueEventListener =
        new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot snapshot) {
            try {
              Thread.sleep(100);
            } catch (InterruptedException e) {
              System.err.println("Something went wrong");
              throw new RuntimeException(e);
            }
            /* Declare the DAOs we will use */
            DAO<Location> locationDAO = DAOPouch.getLocationDAO();
            DAO<MedicalEquipment> medicalEquipmentDAO = DAOPouch.getMedicalEquipmentDAO();
            Date dateDat = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
            String dateRepresentation = dateFormat.format(dateDat);
            DAO<MedicalEquipmentRequest> equipmentRequestDAO =
                DAOPouch.getMedicalEquipmentRequestDAO();

            new Thread(
                    () -> {
                      // For each dirty storage location...
                      for (Location loc : locationDAO.filter(6, "DIRT").values()) {
                        // Get dirty all dirty beds in current dirty location
                        Map<String, MedicalEquipment> dirtyBedMap =
                            DAOFacade.filterEquipByTypeAndStatus(
                                loc, medicalEquipmentDAO, "BED", "UNClEAN");
                        // Get all dirty infusion pumps in current dirty location
                        Map<String, MedicalEquipment> dirtyInfusionPumpMap =
                            DAOFacade.filterEquipByTypeAndStatus(
                                loc, medicalEquipmentDAO, "INFUSIONPUMP", "UNCLEAN");

                        // If there are more than 6 dirty beds in the current location creat request for each
                        if (dirtyBedMap.size() >= 6) {
                          // TODO: ADD ALERT
                          for (MedicalEquipment equipment : dirtyBedMap.values()) {
                            MedicalEquipmentRequest request =
                                    new MedicalEquipmentRequest(
                                            Request.Priority.OVERDUE,
                                            "dSTOR001L1",
                                            "AUTOMATIC REQUEST",
                                            "NONE",
                                            equipment.getNodeID(),
                                            equipment.getEquipmentType(),
                                            equipment.getCleanStatus(),
                                            dateRepresentation);
                            if (equipmentRequestDAO.get(equipment.getNodeID()) == null) {
                              equipmentRequestDAO.add(request);
                            }
                          }
                        }
                        if (dirtyInfusionPumpMap.size() >= 10) {
                          // TODO: ADD ALERT
                          for (MedicalEquipment equipment : dirtyInfusionPumpMap.values()) {
                            MedicalEquipmentRequest request =
                                  new MedicalEquipmentRequest(
                                      Request.Priority.OVERDUE,
                                      "dEXIT00401",
                                      "AUTOMATIC REQUEST",
                                      "NONE",
                                      equipment.getNodeID(),
                                      equipment.getEquipmentType(),
                                      equipment.getCleanStatus(),
                                      dateRepresentation);
                            if (equipmentRequestDAO.get(equipment.getNodeID()) == null) {
                                  equipmentRequestDAO.add(request);
                            }
                          }
                        }
                        // END LOOP
                      }
                      // ==================================This is Separate======================================
                      // For each clean location...
                      for (Location loc : locationDAO.filter(6, "STOR").values()) {

                        // Get all clean infusion pumps at the given clean location...
                        Map<String, MedicalEquipment> cleanInfusionMap =
                            DAOFacade.filterEquipByTypeAndStatus(
                                loc, medicalEquipmentDAO, "INFUSIONPUMP", "CLEAN");

                        // If there are less than 5 clean infusion pumps in a clean location...
                        if (cleanInfusionMap.size() <= 5) {
                          // Iterate through dirty locations
                          for (Location dirtyLoc : locationDAO.filter(6, "DIRT").values()) {

                            // Find all dirty infusion pumps in that location
                            Map<String, MedicalEquipment> dirtyInfusionMap =
                                DAOFacade.filterEquipByTypeAndStatus(
                                    dirtyLoc, medicalEquipmentDAO, "INFUSIONPUMP", "UNCLEAN");

                            // For all the dirty infusion pumps at the dirty location
                            for (MedicalEquipment equipment : dirtyInfusionMap.values()) {
                              // TODO: Add an alert
                              // create a request
                              MedicalEquipmentRequest request =
                                  new MedicalEquipmentRequest(
                                      Request.Priority.OVERDUE,
                                      "dEXIT00401",
                                      "AUTOMATIC REQUEST",
                                      "NONE",
                                      equipment.getNodeID(),
                                      equipment.getEquipmentType(),
                                      equipment.getCleanStatus(),
                                      dateRepresentation);
                              if (equipmentRequestDAO.get(equipment.getNodeID()) == null) {
                                equipmentRequestDAO.add(request);
                              }
                            }
                          }
                        }
                      }
                    })
                .start();
          }

          @Override
          public void onCancelled(DatabaseError error) {}
        };
    FireBase.getReference()
        .child("MEDICALEQUIPMENT")
        .addValueEventListener(this.valueEventListener);
  }
}
