package edu.wpi.DapperDaemons.backend;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import edu.wpi.DapperDaemons.entities.TableObject;
import java.util.*;

public class ORM<T extends TableObject> {
  String tableName;

  T type;
  HashMap<String, T> map = new HashMap<>();
  DatabaseReference ref;

  public ORM(T type) {
    this.type = type;
    tableName = type.getTableName();
    ref = firebase.getReference().child(type.getTableName());
    ref.addValueEventListener(
        new ValueEventListener() {
          @Override
          public synchronized void onDataChange(DataSnapshot snapshot) {
            System.out.println(tableName + " data updating");
            for (DataSnapshot ignored : snapshot.getChildren()) {
              new Thread(
                      () -> {
                        try {
                          ((HashMap<String, List<String>>) snapshot.getValue())
                              .forEach(
                                  (k, v) -> {
                                    map.put(k, (T) type.newInstance(v));
                                  });
                        } catch (ClassCastException e) {
                          HashMap<String, Object> res =
                              (HashMap<String, Object>) snapshot.getValue();
                          ArrayList<String> attributes = new ArrayList<>();
                          T temp = (T) type.newInstance(new ArrayList<>());
                          res.forEach(
                              (k, v) -> {
                                //                                attributes.add(v.toString());
                                temp.setAttribute(k, String.valueOf(v));
                              });
                          map.put(attributes.get(0), temp);
                        }
                      })
                  .start();
            }
          }

          @Override
          public void onCancelled(DatabaseError error) {
            System.out.println("There was an error in the event listener");
          }
        });
  }

  private T getInstance() {
    return (T) new Object();
  }

  public T get(String primaryKey) {
    return map.get(primaryKey);
  }

  public Map<String, T> getAll() {
    return map;
  }

  public void add(T newTableObject) {
    map.put(newTableObject.getAttribute(1), newTableObject);
    ref.setValueAsync(newTableObject);
  }

  public void delete(String primaryKey) {
    ref.child(primaryKey).setValueAsync(null);
  }

  public void update(T type) {
    ref.setValueAsync(type);
  }
}
