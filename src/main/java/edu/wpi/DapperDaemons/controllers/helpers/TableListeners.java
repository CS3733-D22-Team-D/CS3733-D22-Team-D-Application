package edu.wpi.DapperDaemons.controllers.helpers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import edu.wpi.DapperDaemons.backend.FireBase;
import java.util.HashMap;
import java.util.Map;

public class TableListeners {

  private static DatabaseReference ref = FireBase.getReference();
  private static ValueEventListener lbrL;
  private static ValueEventListener lrL;
  private static ValueEventListener mdrL;
  private static ValueEventListener merL;
  private static ValueEventListener mrL;
  private static ValueEventListener ptrL;
  private static ValueEventListener srL;
  private static ValueEventListener aL;
  private static ValueEventListener eL;
  private static ValueEventListener lL;
  private static ValueEventListener lncL;
  private static ValueEventListener meL;
  private static ValueEventListener nL;
  private static ValueEventListener pL;
  private static ValueEventListener ssrL;

  private static Map<String, ValueEventListener> listeners = new HashMap<>();

  public static void addListener(String tableName, ValueEventListener listener) {
    listeners.put(tableName, listener);
    ref.child(tableName).addValueEventListener(listener);
  }

  public static void setLabRequestListener(ValueEventListener labRequestListener) {
    lbrL = labRequestListener;
    ref.child("LABREQUESTS").addValueEventListener(lbrL);
  }

  public static void setLanguageRequestListener(ValueEventListener languageRequestListener) {
    lrL = languageRequestListener;
    ref.child("LANGUAGEREQUESTS").addValueEventListener(lrL);
  }

  public static void setMealDeliveryRequestListener(
      ValueEventListener mealDeliveryRequestListener) {
    mdrL = mealDeliveryRequestListener;
    ref.child("MEALDELIVERYREQUESTS").addValueEventListener(mdrL);
  }

  public static void setMedicalEquipmentRequestListener(
      ValueEventListener medicalEquipmentRequestListener) {
    merL = medicalEquipmentRequestListener;
    ref.child("MEDICALEQUIPMENTREQUESTS").addValueEventListener(merL);
  }

  public static void setMedicinRequestListener(ValueEventListener medicinRequestListener) {
    mrL = medicinRequestListener;
    ref.child("MEDICINEREQUESTS").addValueEventListener(mrL);
  }

  public static void setPatientTrasportRequestListener(
      ValueEventListener patientTrasportRequestListener) {
    ptrL = patientTrasportRequestListener;
    ref.child("PATIENTTRANSPORTREQUESTS").addValueEventListener(ptrL);
  }

  public static void setSanitationRequestListener(ValueEventListener sanitationRequestListener) {
    srL = sanitationRequestListener;
    ref.child("SANITATIONREQUESTS").addValueEventListener(srL);
  }

  public static void setAccountListener(ValueEventListener accountListener) {
    aL = accountListener;
    ref.child("ACCOUNTS").addValueEventListener(aL);
  }

  public static void setEmployeeListener(ValueEventListener employeeListener) {
    eL = employeeListener;
    ref.child("EMPLOYEES").addValueEventListener(employeeListener);
  }

  public static void setLocationListener(ValueEventListener locationListener) {
    lL = locationListener;
    ref.child("LOCATIONS").addValueEventListener(lL);
  }

  public static void setLocationNodeConnectionsListener(
      ValueEventListener locationNodeConnectionsListener) {
    lncL = locationNodeConnectionsListener;
    ref.child("LOCATIONNODECONNECTIONS").addValueEventListener(lncL);
  }

  public static void setMedicalEquipmentListener(ValueEventListener medicalEquipmentListener) {
    meL = medicalEquipmentListener;
    ref.child("MEDICALEQUIPMENT").addValueEventListener(meL);
  }

  public static void setNotificationListener(ValueEventListener notificationListener) {
    nL = notificationListener;
    ref.child("NOTIFICATIONS").addValueEventListener(nL);
  }

  public static void setPatientListener(ValueEventListener patientListener) {
    pL = patientListener;
    ref.child("PATIENTS").addValueEventListener(pL);
  }

  public static void setSecurityRequestListener(ValueEventListener securityRequestListener) {
    ssrL = securityRequestListener;
    ref.child("SECURITYREQUESTS").addValueEventListener(ssrL);
  }

  public ValueEventListener eventListener(Runnable r) {
    return new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot snapshot) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        r.run();
      }

      @Override
      public void onCancelled(DatabaseError error) {}
    };
  }

  public static void removeAllListeners() {
    try {
      ref.child("LABREQUESTS").removeEventListener(lbrL);
    } catch (NullPointerException ignored) {
    }
    try {
      ref.child("LANGUAGEREQUESTS").removeEventListener(lrL);
    } catch (NullPointerException ignored) {
    }
    try {
      ref.child("MEALDELIVERYREQUESTS").removeEventListener(mdrL);
    } catch (NullPointerException ignored) {
    }
    try {
      ref.child("MEDICALEQUIPMENTREQUESTS").removeEventListener(merL);
    } catch (NullPointerException ignored) {
    }
    try {
      ref.child("MEDICINEREQUESTS").removeEventListener(mrL);
    } catch (NullPointerException ignored) {
    }
    try {
      ref.child("PATIENTTRANSPORTREQUESTS").removeEventListener(ptrL);
    } catch (NullPointerException ignored) {
    }
    try {
      ref.child("SANITATIONREQUESTS").removeEventListener(srL);
    } catch (NullPointerException ignored) {
    }
    try {
      ref.child("ACCOUNTS").removeEventListener(aL);
    } catch (NullPointerException ignored) {
    }
    try {
      ref.child("EMPLOYEES").removeEventListener(eL);
    } catch (NullPointerException ignored) {
    }
    try {
      ref.child("LOCATIONS").removeEventListener(lL);
    } catch (NullPointerException ignored) {
    }
    try {
      ref.child("LOCATIONNODECONNECTIONS").removeEventListener(lncL);
    } catch (NullPointerException ignored) {
    }
    try {
      ref.child("MEDICALEQUIPMENT").removeEventListener(meL);
    } catch (NullPointerException ignored) {
    }
    try {
      ref.child("NOTIFICATIONS").removeEventListener(nL);
    } catch (NullPointerException ignored) {
    }
    try {
      ref.child("PATIENTS").removeEventListener(pL);
    } catch (NullPointerException ignored) {
    }
    try {
      ref.child("SECURITYREQUESTS").removeEventListener(ssrL);
    } catch (NullPointerException ignored) {
    }

    listeners.forEach(
        (k, v) -> {
          ref.child(k).removeEventListener(v);
        });
  }

  public TableListeners() {}
}
