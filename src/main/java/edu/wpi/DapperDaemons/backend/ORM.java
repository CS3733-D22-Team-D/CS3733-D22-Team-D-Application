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
    new Thread(
            () -> {
              ref.addValueEventListener(
                  new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                      System.out.println(tableName + " data updating");
                      //            for (DataSnapshot ignored : snapshot.getChildren()) {
                      //                      List<String> values = new ArrayList((Collection)
                      // snapshot.getValue());
                      //                      for (String attribute : values) {
                      //                        System.out.println(attribute);
                      //                      }
                      //                      Account post = snapshot.getValue(Account.class);
                      //                      map.put(post.getAttribute(1), (T) post);

                      ((HashMap<String, List<String>>) snapshot.getValue())
                          .forEach(
                              (k, v) -> {
                                map.put(k, (T) type.newInstance(v));
                              });
                      System.out.println(snapshot.toString());
                      //            }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                      System.out.println("There was an error in the event listener");
                    }
                  });
            })
        .start();
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
