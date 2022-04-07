package edu.wpi.DapperDaemons.backend;

import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.entities.TableObject;

public class SecurityController {
  private Employee user;
  static SecurityController instance;

  private SecurityController() {}

  public static SecurityController getInstance() {
    if (instance == null) {
      instance = new SecurityController();
    }

    return instance;
  }

  public void setUser(Employee user) {
    this.user = user;
  }

  public Employee getUser() {

    return user;
  }

  public boolean permissionToAdd(TableObject type) {

    String className = type.getClass().getSimpleName();
    int clearance = user.getSecurityClearance();
    if (className.equals("")) {
      return false;
    } else if (className.equals("LabRequest")) {
      return clearance >= 4;
    } else if (className.equals("MealDeliveryRequest")) {
      return clearance >= 3;
    } else if (className.equals("MedicalEquipmentRequest")) {
      return clearance >= 3;
    } else if (className.equals("MedicineRequest")) {
      return clearance >= 3;
    } else if (className.equals("PatientTransportRequest")) {
      return clearance >= 3;
    } else if (className.equals("SanitationRequest")) {
      return clearance >= 3;
    } else if (className.equals("Account")) {
      return clearance >= 5;
    } else if (className.equals("Employee")) {
      return clearance >= 5;
    } else if (className.equals("Location")) {
      return clearance >= 5;
    } else if (className.equals("MedicalEquipment")) {
      return clearance >= 3;
    } else if (className.equals("Patient")) {
      return clearance >= 3;
    }
    return false;
  }

  public boolean permissionToUpdate(TableObject type) {
    String className = type.getClass().getSimpleName();
    int clearance = user.getSecurityClearance();
    if (className.equals("")) {
      return false;
    } else if (className.equals("LabRequest")) {
      return clearance >= 3;
    } else if (className.equals("MealDeliveryRequest")) {
      return clearance >= 1;
    } else if (className.equals("MedicalEquipmentRequest")) {
      return clearance >= 3;
    } else if (className.equals("MedicineRequest")) {
      return clearance >= 3;
    } else if (className.equals("PatientTransportRequest")) {
      return clearance >= 3;
    } else if (className.equals("SanitationRequest")) {
      return clearance >= 2;
    } else if (className.equals("Account")) {
      return clearance >= 5;
    } else if (className.equals("Employee")) {
      return clearance >= 5;
    } else if (className.equals("Location")) {
      return clearance >= 5;
    } else if (className.equals("MedicalEquipment")) {
      return clearance >= 3;
    } else if (className.equals("Patient")) {
      return clearance >= 3;
    }
    return false;
  }

  public boolean permissionToDelete(TableObject type) {
    String className = type.getClass().getSimpleName();
    int clearance = user.getSecurityClearance();
    if (className.equals("")) {
      return false;
    } else if (className.equals("LabRequest")) {
      return clearance >= 3;
    } else if (className.equals("MealDeliveryRequest")) {
      return clearance >= 1;
    } else if (className.equals("MedicalEquipmentRequest")) {
      return clearance >= 3;
    } else if (className.equals("MedicineRequest")) {
      return clearance >= 3;
    } else if (className.equals("PatientTransportRequest")) {
      return clearance >= 3;
    } else if (className.equals("SanitationRequest")) {
      return clearance >= 2;
    } else if (className.equals("Account")) {
      return clearance >= 5;
    } else if (className.equals("Employee")) {
      return clearance >= 5;
    } else if (className.equals("Location")) {
      return clearance >= 5;
    } else if (className.equals("MedicalEquipment")) {
      return clearance >= 3;
    } else if (className.equals("Patient")) {
      return clearance >= 3;
    }
    return false;
  }
}
