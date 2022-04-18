package edu.wpi.DapperDaemons.backend;

import edu.wpi.DapperDaemons.entities.TableObject;

public class FireBaseLoader {

  public FireBaseLoader(DAO<? extends TableObject> dao, TableObject type) {
    //    DatabaseReference ref = FireBase.getReference().child(type.tableName());
    //    Map<String, Map<String, String>> map = new HashMap<>();
    //    Map<String, String> data = new HashMap<>();
    //    for (TableObject t : dao.getAll().values()) {
    //      for (Integer i = 1; i < 100; i++) {
    //        try {
    //          data.put(i.toString(), t.getAttribute(i));
    //        } catch (IndexOutOfBoundsException ignored) {
    //          break;
    //        }
    //      }
    //      map.put(t.getAttribute(1), data);
    //    }
    //    ref.setValueAsync(map);
  }
}
