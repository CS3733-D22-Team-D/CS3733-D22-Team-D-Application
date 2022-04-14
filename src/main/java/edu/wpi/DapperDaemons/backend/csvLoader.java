package edu.wpi.DapperDaemons.backend;

import com.google.firebase.database.DatabaseReference;
import com.opencsv.CSVReader;
import edu.wpi.DapperDaemons.entities.*;
import edu.wpi.DapperDaemons.entities.requests.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
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
    String tableName = type.getTableName();

    DatabaseReference ref = firebase.getReference();
    ref = ref.child(type.getTableName());
    Map<String, List<String>> map = new HashMap<>();
    for (String[] line : entries) {
      map.put(line[0], List.of(line));
    }
    ref.setValueAsync(map);
  }
}
