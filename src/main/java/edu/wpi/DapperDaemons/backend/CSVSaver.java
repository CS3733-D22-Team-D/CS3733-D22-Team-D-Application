package edu.wpi.DapperDaemons.backend;

import edu.wpi.DapperDaemons.entities.TableObject;
import java.io.IOException;

public class CSVSaver {
  private CSVSaver() {}

  public static void saveAll() {
    CSVLoader.filenames.forEach(
        (k, v) -> {
          try {
            save(v, k + "_save.csv");
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }

  public static void save(TableObject type, String filename) throws IOException {
    //    File file = new File(filename);
    //    FileWriter outputFile = new FileWriter(file);
    //    CSVWriter writer =
    //        new CSVWriter(outputFile, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
    //
    //    String tableName = type.getTableName();
    //
    //    while (resultSet.next()) {
    //      for (int i = 1; i <= numAttributes; i++) save_tableObject[i - 1] =
    // resultSet.getString(i);
    //      writer.writeNext(save_tableObject);
    //    }
    //
    //    writer.close();
  }
}
