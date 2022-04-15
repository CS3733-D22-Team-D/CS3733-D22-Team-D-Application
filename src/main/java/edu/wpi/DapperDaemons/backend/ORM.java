package edu.wpi.DapperDaemons.backend;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import edu.wpi.DapperDaemons.entities.TableObject;
import java.util.*;
import java.util.stream.Collectors;

public class ORM<T extends TableObject> {
  String tableName;

  T type;
  HashMap<String, T> map = new HashMap<>();
  DatabaseReference ref;

  public ORM(T type) {
    this.type = type;
    tableName = type.tableName();
    ref = firebase.getReference().child(type.tableName());
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
                                    map.put(
                                        decodeFirebaseKey(k),
                                        (T)
                                            type.newInstance(
                                                v.stream()
                                                    .map(
                                                        e -> {
                                                          if (e != null) {
                                                            return decodeFirebaseKey(e);
                                                          }
                                                          return e;
                                                        })
                                                    .collect(Collectors.toList())));
                                  });
                        } catch (ClassCastException e) {
                          HashMap<String, Object> res =
                              (HashMap<String, Object>) snapshot.getValue();
                          ArrayList<String> attributes = new ArrayList<>();
                          T temp = (T) type.newInstance(new ArrayList<>());
                          res.forEach(
                              (k, v) -> {
                                if (k.equals("nodeID")) {
                                  attributes.add(0, v.toString());
                                } else {
                                  attributes.add(v.toString());
                                }
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

  // TODO fix

  public void add(T newTableObject) {
    map.put(newTableObject.getAttribute(1), newTableObject);
    Map<String, Map<String, String>> put = new HashMap<>();
    Map<String, String> data = new HashMap<>();
    try {
      for (int i = 1; i < 100; i++) { // not at all how we should do this, but, were lazy
        data.put(Integer.toString(i - 1), encodeForFirebaseKey(newTableObject.getAttribute(i)));
      }
    } catch (IndexOutOfBoundsException ignored) {
    }
    put.put(encodeForFirebaseKey(newTableObject.getAttribute(1)), data);
    ref.child(encodeForFirebaseKey(newTableObject.getAttribute(1))).setValueAsync(data);
  }

  // TODO encode PK
  public void delete(String primaryKey) {
    ref.child(encodeForFirebaseKey(primaryKey)).setValueAsync(null);
  }

  // TODO fix
  public void update(T type) {
    add(type);
  }

  private static String encodeForFirebaseKey(String s) {
    return s.replace("_", "____")
        .replace(".", "___P")
        .replace("$", "___D")
        .replace("#", "___H")
        .replace("[", "___O")
        .replace("]", "___C")
        .replace("/", "___S");
  }

  private static String decodeFirebaseKey(String s) {
    return s.replace("____", "_")
        .replace("___P", ".")
        .replace("___D", "$")
        .replace("___H", "#")
        .replace("___O", "[")
        .replace("___C", "]")
        .replace("___S", "/");
  }
}
