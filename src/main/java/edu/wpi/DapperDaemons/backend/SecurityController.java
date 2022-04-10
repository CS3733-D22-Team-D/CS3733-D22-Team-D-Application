package edu.wpi.DapperDaemons.backend;

import edu.wpi.DapperDaemons.entities.Employee;
import edu.wpi.DapperDaemons.entities.TableObject;

public class SecurityController {
  static Employee user;
  static SecurityController instance;

  private SecurityController() {}

  public static void setUser(Employee u) {
    user = u;
  }

  public static Employee getUser() {
    return user;
  }

  public static boolean permissionToAdd(TableObject type) {
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

  public static boolean permissionToUpdate(TableObject type) {
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

  public static boolean permissionToDelete(TableObject type) {
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
