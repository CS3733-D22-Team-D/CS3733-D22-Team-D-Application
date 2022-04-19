package edu.wpi.DapperDaemons.controllers.helpers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import edu.wpi.DapperDaemons.backend.FireBase;
import java.util.HashMap;
import java.util.List;
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

  public static void addListeners(List<String> tableNames, ValueEventListener listener) {
    for (String s : tableNames) {
      listeners.put(s, listener);
      ref.child(s).addValueEventListener(listener);
    }
  }

  public static void addListener(String tableName, ValueEventListener listener) {
    listeners.put(tableName, listener);
    ref.child(tableName).addValueEventListener(listener);
  }

  public static ValueEventListener eventListener(Runnable r) {
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
    listeners.forEach(
        (k, v) -> {
          ref.child(k).removeEventListener(v);
        });
  }

  private TableListeners() {}
}
