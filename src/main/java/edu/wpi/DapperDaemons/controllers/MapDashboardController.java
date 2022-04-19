package edu.wpi.DapperDaemons.controllers;

import edu.wpi.DapperDaemons.backend.*;
import edu.wpi.DapperDaemons.controllers.helpers.TableListeners;
import edu.wpi.DapperDaemons.entities.Location;
import edu.wpi.DapperDaemons.entities.MedicalEquipment;
import edu.wpi.DapperDaemons.entities.Patient;
import edu.wpi.DapperDaemons.entities.requests.*;
import edu.wpi.DapperDaemons.tables.TableHelper;
import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class MapDashboardController extends ParentController {

  @FXML private TableView<MedicalEquipment> equipTable;
  private final DAO<MedicalEquipment> equipmentDAO = DAOPouch.getMedicalEquipmentDAO();
  @FXML private TableView<Location> locTable;
  private final DAO<Location> locationDAO = DAOPouch.getLocationDAO();
  @FXML private TableView<Patient> patientTable;
  private final DAO<Patient> patientDAO = DAOPouch.getPatientDAO();
  @FXML private TableView<Request> reqTable;

  @FXML private ToggleButton L1;
  @FXML private ToggleButton L10;
  @FXML private ToggleButton L11;
  @FXML private ToggleButton L12;
  @FXML private ToggleButton L14;
  @FXML private ToggleButton L15;
  @FXML private ToggleButton L16;
  @FXML private ToggleButton L2;
  @FXML private ToggleButton L3;
  @FXML private ToggleButton L4;
  @FXML private ToggleButton L5;
  @FXML private ToggleButton L6;
  @FXML private ToggleButton L7;
  @FXML private ToggleButton L8;
  @FXML private ToggleButton L9;
  @FXML private ToggleButton LL1;
  @FXML private ToggleButton LL2;
  @FXML private ToggleButton M1;
  @FXML private ToggleButton M2;
  @FXML private Label floorSummary;
  @FXML private Label locOfInterest;
  @FXML private TabPane tabs;

  @FXML private Text cleanEquipNum;
  @FXML private Text dirtyEquipNum;
  @FXML private Text inUseEquipNum;
  @FXML private Text patientNum;
  @FXML private Text requestNum;

  public static String floor;

  @FXML private ImageView mapImage;
  @FXML private Pane mapImageContainer;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    bindImage(mapImage, mapImageContainer);

    setListeners();

    // Init tables
    new TableHelper<>(equipTable, 2).linkColumns(MedicalEquipment.class);
    new TableHelper<>(locTable, 2).linkColumns(Location.class);
    new TableHelper<>(patientTable, 2).linkColumns(Patient.class);
    new TableHelper<>(reqTable, 1).linkColumns(Request.class);
    new TableHelper<>(reqTable, 1).linkColumns(Request.class);

    // Default floor
    floor = "1";
    updatePage();
  }

  private void setListeners() {
    TableListeners.addListener(
        new MedicalEquipment().tableName(),
        TableListeners.eventListener(
            () -> {
              equipTable.getItems().clear();
              for (Location l : locsByFloor) {
                equipTable
                    .getItems()
                    .addAll(new ArrayList<>(equipmentDAO.filter(6, l.getNodeID()).values()));
              }
            }));
    TableListeners.addListener(
        new Patient().tableName(),
        TableListeners.eventListener(
            () -> {
              patientTable.getItems().clear();
              for (Location l : locsByFloor) {
                patientTable
                    .getItems()
                    .addAll(new ArrayList<>(patientDAO.filter(6, l.getNodeID()).values()));
              }
            }));
    List<String> tableNames =
        Arrays.asList(
            new String[] {
              new EquipmentCleaning().tableName(),
              new LabRequest().tableName(),
              new LanguageRequest().tableName(),
              new MealDeliveryRequest().tableName(),
              new MedicalEquipmentRequest().tableName(),
              new MedicineRequest().tableName(),
              new PatientTransportRequest().tableName(),
              new SanitationRequest().tableName(),
              new SecurityRequest().tableName()
            });
    TableListeners.addListeners(
        tableNames,
        TableListeners.eventListener(
            () -> {
              reqTable.getItems().clear();
              for (Location l : locsByFloor) {
                reqTable.getItems().addAll(DAOFacade.getFilteredRequests(l.getNodeID()));
              }
            }));
  }

  private void updatePage() {
    updateTables();
    updateIcons();
    updateSummary();
    updateLocOfInterest();
  }

  private List<Location> locsByFloor;

  // Updates the data based on current floor
  private void updateTables() {
    equipTable.getItems().clear();
    patientTable.getItems().clear();
    reqTable.getItems().clear();
    locTable.getItems().clear();
    locsByFloor = new ArrayList<>(locationDAO.filter(4, floor).values());

    for (Location l : locsByFloor) {
      equipTable.getItems().addAll(new ArrayList<>(equipmentDAO.filter(6, l.getNodeID()).values()));
      patientTable.getItems().addAll(new ArrayList<>(patientDAO.filter(6, l.getNodeID()).values()));
      reqTable.getItems().addAll(DAOFacade.getFilteredRequests(l.getNodeID()));
      locTable.getItems().add(l);
    }
  }

  private void updateIcons() {
    requestNum.setText(reqTable.getItems().size() + "");
    patientNum.setText(patientTable.getItems().size() + "");

    // Creates list of dirty and clean equipment by filtering the equipment on the floor
    List<MedicalEquipment> dirtyEquipment =
        new ArrayList<>(equipmentDAO.filter(equipTable.getItems(), 5, "UNCLEAN").values());
    List<MedicalEquipment> cleanEquipment =
        new ArrayList<>(equipmentDAO.filter(equipTable.getItems(), 5, "CLEAN").values());

    dirtyEquipNum.setText(dirtyEquipment.size() + "");
    cleanEquipNum.setText(cleanEquipment.size() + "");

    // Checks if the equipment is not within a storage and if it has not been called before
    // (to ensure total is total number of equipment)
    List<MedicalEquipment> notInStorage = new ArrayList<>();
    equipTable
        .getItems()
        .forEach(
            e -> {
              Location equipLoc;
              try {
                equipLoc = locationDAO.get(e.getLocationID());
              } catch (Exception ex) {
                equipLoc = new Location();
              }
              if (!"DIRT".equals(equipLoc.getNodeType())
                  && !"STOR".equals(equipLoc.getNodeType())
                  && !dirtyEquipment.contains(e)
                  && !cleanEquipment.contains(e)) {
                notInStorage.add(e);
              }
            });

    inUseEquipNum.setText(notInStorage.size() + "");
  }

  private void updateSummary() {
    try {
      String floorTxtPath = "floorSummary.txt";
      String floorText = getFileText(floorTxtPath, getFloorNum());
      floorSummary.setText(floorText);
    } catch (IOException e) {
      showError("Error 404: File Not Found");
    }
  }

  private void updateLocOfInterest() {
    try {
      String locOfInterestTxtPath = "locOfInterest.txt";
      String floorText = getFileText(locOfInterestTxtPath, getFloorNum());
      locOfInterest.setText(floorText);
    } catch (IOException ignored) {
    }
  }

  private static String getFileText(String filePath, int line) throws IOException {
    InputStreamReader f =
        new InputStreamReader(
            Objects.requireNonNull(CSVLoader.class.getClassLoader().getResourceAsStream(filePath)));
    BufferedReader reader = new BufferedReader(f);
    // filePath.replace("%20", " ")
    Scanner s = new Scanner(reader);
    int l = 0;
    String current;
    while (s.hasNextLine()) {
      current = s.nextLine();
      if (l == line) return current;
      l++;
    }
    return "";
  }

  @FXML
  public void onEquipIconClicked() {
    tabs.getSelectionModel().select(0);
  }

  @FXML
  public void onPatientIconClicked() {
    tabs.getSelectionModel().select(1);
  }

  @FXML
  public void onRequestIconClicked() {
    tabs.getSelectionModel().select(3);
  }

  private int getFloorNum() {
    switch (floor) {
      case "L2":
        return 0;
      case "L1":
        return 1;
      case "1":
        return 2;
      case "2":
        return 3;
      case "3":
        return 4;
      case "4":
        return 5;
      case "5":
        return 6;
      case "6":
        return 7;
      case "M1":
        return 8;
      case "M2":
        return 9;
      case "7":
        return 10;
      case "8":
        return 11;
      case "9":
        return 12;
      case "10":
        return 13;
      case "11":
        return 14;
      case "12":
        return 15;
      case "14":
        return 16;
      case "15":
        return 17;
      case "16":
        return 18;
      default:
        return -1;
    }
  }

  @FXML
  void switchToL1() {
    if (L1.isSelected()) {
      floor = "1";
      updatePage();
    }
  }

  @FXML
  void switchToL10() {
    if (L10.isSelected()) {
      floor = "10";
      updatePage();
    }
  }

  @FXML
  void switchToL11() {
    if (L11.isSelected()) {
      floor = "11";
      updatePage();
    }
  }

  @FXML
  void switchToL12() {
    if (L12.isSelected()) {
      floor = "12";
      updatePage();
    }
  }

  @FXML
  void switchToL14() {
    if (L14.isSelected()) {
      floor = "14";
      updatePage();
    }
  }

  @FXML
  void switchToL15() {
    if (L15.isSelected()) {
      floor = "15";
      updatePage();
    }
  }

  @FXML
  void switchToL16() {
    if (L16.isSelected()) {
      floor = "16";
      updatePage();
    }
  }

  @FXML
  void switchToL2() {
    if (L2.isSelected()) {
      floor = "2";
      updatePage();
    }
  }

  @FXML
  void switchToL3() {
    if (L3.isSelected()) {
      floor = "3";
      updatePage();
    }
  }

  @FXML
  void switchToL4() {
    if (L4.isSelected()) {
      floor = "4";
      updatePage();
    }
  }

  @FXML
  void switchToL5() {
    if (L5.isSelected()) {
      floor = "5";
      updatePage();
    }
  }

  @FXML
  void switchToL6() {
    if (L6.isSelected()) {
      floor = "6";
      updatePage();
    }
  }

  @FXML
  void switchToL7() {
    if (L7.isSelected()) {
      floor = "7";
      updatePage();
    }
  }

  @FXML
  void switchToL8() {
    if (L8.isSelected()) {
      floor = "8";
      updatePage();
    }
  }

  @FXML
  void switchToL9() {
    if (L9.isSelected()) {
      floor = "9";
      updatePage();
    }
  }

  @FXML
  void switchToLL1() {
    if (LL1.isSelected()) {
      floor = "L1";
      updatePage();
    }
  }

  @FXML
  void switchToLL2() {
    if (LL2.isSelected()) {
      floor = "L2";
      updatePage();
    }
  }

  @FXML
  void switchToM1() {
    if (M1.isSelected()) {
      floor = "M1";
      updatePage();
    }
  }

  @FXML
  void switchToM2() {
    if (M2.isSelected()) {
      floor = "M2";
      updatePage();
    }
  }
}
