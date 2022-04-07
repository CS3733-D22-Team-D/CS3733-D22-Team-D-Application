package edu.wpi.DapperDaemons.controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.DapperDaemons.backend.DAO;
import edu.wpi.DapperDaemons.backend.DAOPouch;
import edu.wpi.DapperDaemons.entities.requests.MedicineRequest;
import edu.wpi.DapperDaemons.entities.requests.Request;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MedicineController extends UIController {
  @FXML private TableView<MedicineRequest> medicineRequests;
  private TableHelper<MedicineRequest> helper;

  @FXML private JFXComboBox<String> medNameIn;
  @FXML private TextField quantityIn;
  @FXML private ChoiceBox<String> priorityIn;
  @FXML private TextField patientName;
  @FXML private TextField patientLastName;
  @FXML private TextField patientDOB;

  DAO<MedicineRequest> dao = DAOPouch.getMedicineRequestDAO();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    MedicineRequestInitializer init = new MedicineRequestInitializer();

    init.initalizeInputs();
    init.initializeRequests();
    init.initializeTable();

    try {
      medicineRequests.getItems().addAll(dao.getAll());
//      System.out.println("Created table");
    } catch (Exception e) {
      e.printStackTrace();
      System.err.print("Unable to get all from DAO \n");
    }
  }

  @FXML
  public void onClearClicked() {
    medNameIn.setValue("");
    quantityIn.clear();
    priorityIn.setValue("");
    patientName.clear();
    patientLastName.clear();
    patientDOB.clear();
  }

  @FXML
  public void onSubmitClicked() {
    if (!(medNameIn.getValue().trim().equals("")
        || quantityIn.getText().trim().equals("")
        || priorityIn.getValue().equals("")
        || patientName.getText().equals("")
        || patientLastName.getText().equals("")
        || patientDOB.getText().equals(""))) {

      int tempQuan;
      try {
        tempQuan = Integer.parseInt(quantityIn.getText());
      } catch (Exception e) {
        quantityIn.clear();
        return;
      }

      String medName = medNameIn.getValue();
      String requesterID = "TEST";
      Request.Priority priority = Request.Priority.valueOf(priorityIn.getValue());
      String patientID = patientName.getText() + patientLastName.getText() + patientDOB.getText();

      addItem(
          new MedicineRequest(
              priority,
              "ROOMIDofPATIENT",
              "REQUESTERID",
              "ASSIGNEEID",
              patientID,
              medName,
              tempQuan));
      onClearClicked();
    }
  }

  @FXML
  private void addItem(MedicineRequest request) {
    medicineRequests.getItems().add(request);
    // TODO: Add request to database
  }

  private class MedicineRequestInitializer {
    private void initializeTable() {
      helper = new TableHelper<>(medicineRequests, 0);
      helper.linkColumns(MedicineRequest.class);
    }

    private void initalizeInputs() {
      medNameIn.setItems(FXCollections.observableArrayList("One", "Two", "Three"));
      priorityIn.getItems().addAll("LOW", "MEDIUM", "HIGH");
    }

    private void initializeRequests() {
      // TODO: Pull data from database
    }
  }
}
