package edu.wpi.DapperDaemons;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.DapperDaemons.backend.csvSaver;
import edu.wpi.DapperDaemons.entities.requests.MedicalEquipmentRequest;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

class csvSaverTest {

  @Test
  void save() throws SQLException, IOException {
    csvSaver.save(new MedicalEquipmentRequest(), "MedEq.csv");
  }
}
