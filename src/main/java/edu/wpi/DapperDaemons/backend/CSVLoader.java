package edu.wpi.DapperDaemons.backend;

import com.google.firebase.database.DatabaseReference;
import com.opencsv.CSVReader;
import edu.wpi.DapperDaemons.entities.*;
import edu.wpi.DapperDaemons.entities.requests.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class csvLoader {

  static HashMap<String, TableObject> filenames = new HashMap<>();

  static {
    filenames.put("TowerLocations", new Location());
    filenames.put("MedEquipReq", new MedicalEquipmentRequest());
    filenames.put("LabRequest", new LabRequest());
    filenames.put("Employee", new Employee());
    filenames.put("MealDeliveryRequest", new MealDeliveryRequest());
    filenames.put("PatientTransportRequest", new PatientTransportRequest());
    filenames.put("SanitationRequest", new SanitationRequest());
    filenames.put("MedicalEquipment", new MedicalEquipment());
    filenames.put("Patient", new Patient());
    filenames.put("MedicineRequest", new MedicineRequest());
    filenames.put("Accounts", new Account());
    filenames.put("AllEdges", new LocationNodeConnections());
  }

  private csvLoader() {}

  public static void loadAll() {
    filenames.forEach(
        (k, v) -> {
          try {
            load(v, k + ".csv");
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }

  public static void load(TableObject type, String filename) throws IOException {
    InputStreamReader f =
        new InputStreamReader(
            Objects.requireNonNull(csvLoader.class.getClassLoader().getResourceAsStream(filename)));
    CSVReader read = new CSVReader(f);
    List<String[]> entries = read.readAll();
    if (entries.size() < 1) return;
    entries.remove(0);
    String tableName = type.tableName();

    DatabaseReference ref = firebase.getReference();
    ref = ref.child(type.tableName());
    Map<String, Map<String, String>> map = new HashMap<>();
    Map<String, String> data;
    for (String[] line : entries) {
      data = new HashMap<>();
      for (Integer i = 0; i < line.length; i++) {
        data.put(i.toString(), encodeForFirebaseKey(line[i]));
      }
      map.put(line[0], data);
    }
    ref.setValueAsync(map);
  }

  private static String encodeForFirebaseKey(String s) {
    return s.replace("_", "____")
        .replace(".", "___P")
        .replace("$", "___D")
        .replace("#", "___H")
        .replace("[", "___O")
        .replace("]", "___C")
        .replace("/", "___S");
  }
}
