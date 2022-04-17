package edu.wpi.DapperDaemons.controllers.helpers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import edu.wpi.DapperDaemons.backend.FireBase;

public class TableListeners {

  private DatabaseReference ref = FireBase.getReference();

  public void setLabRequestListener(ValueEventListener labRequestListener) {
    ref.child("LABREQUESTS").addValueEventListener(labRequestListener);
  }

  public void setLanguageRequestListener(ValueEventListener languageRequestListener) {
    ref.child("LANGUAGEREQUESTS").addValueEventListener(languageRequestListener);
  }

  public void setMealDeliveryRequestListener(ValueEventListener mealDeliveryRequestListener) {
    ref.child("MEALDELIVERYREQUESTS").addValueEventListener(mealDeliveryRequestListener);
  }

  public void setMedicalEquipmentRequestListener(
      ValueEventListener medicalEquipmentRequestListener) {
    ref.child("MEDICALEQUIPMENTREQUESTS").addValueEventListener(medicalEquipmentRequestListener);
  }

  public void setMedicinRequestListener(ValueEventListener medicinRequestListener) {
    ref.child("MEDICINEREQUESTS").addValueEventListener(medicinRequestListener);
  }

  public void setPatientTrasportRequestListener(ValueEventListener patientTrasportRequestListener) {
    ref.child("PATIENTTRANSPORTREQUESTS").addValueEventListener(patientTrasportRequestListener);
  }

  public void setSanitationRequestListener(ValueEventListener sanitationRequestListener) {
    ref.child("SANITATIONREQUESTS").addValueEventListener(sanitationRequestListener);
  }

  public void setAccountListener(ValueEventListener accountListener) {
    ref.child("ACCOUNTS").addValueEventListener(accountListener);
  }

  public void setEmployeeListener(ValueEventListener employeeListener) {
    ref.child("EMPLOYEES").addValueEventListener(employeeListener);
  }

  public void setLocationListener(ValueEventListener locationListener) {
    ref.child("LOCATIONS").addValueEventListener(locationListener);
  }

  public void setLocationNodeConnectionsListener(
      ValueEventListener locationNodeConnectionsListener) {
    ref.child("LOCATIONNODECONNECTIONS").addValueEventListener(locationNodeConnectionsListener);
  }

  public void setMedicalEquipmentListener(ValueEventListener medicalEquipmentListener) {
    ref.child("MEDICALEQUIPMENT").addValueEventListener(medicalEquipmentListener);
  }

  public void setNotificationListener(ValueEventListener notificationListener) {
    ref.child("NOTIFICATIONS").addValueEventListener(notificationListener);
  }

  public void setPatientListener(ValueEventListener patientListener) {
    ref.child("PATIENTS").addValueEventListener(patientListener);
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

  public TableListeners() {}
}
