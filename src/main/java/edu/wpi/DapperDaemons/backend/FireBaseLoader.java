package edu.wpi.DapperDaemons.backend;

import edu.wpi.DapperDaemons.entities.TableObject;

public class FireBaseLoader {

  // TODO idk what I was doing, but pretty sure this needs to also push from DAO to firebase, this
  // is the reverse and is handled by ORM
  public FireBaseLoader(DAO dao, TableObject type) {
    //    DatabaseReference ref = FireBase.getReference();
    //    ref = ref.child(new ArrayList<TableObject>(dao.getAll().values()).get(0).tableName());
    //    ref.addListenerForSingleValueEvent(
    //        new ValueEventListener() {
    //          @Override
    //          public void onDataChange(DataSnapshot dataSnapshot) {
    //            HashMap<String, List<String>> data =
    //                (HashMap<String, List<String>>) dataSnapshot.getValue();
    //            for (List<String> l : data.values()) {
    //              dao.add(type.newInstance(l));
    //            }
    //          }
    //
    //          @Override
    //          public void onCancelled(DatabaseError databaseError) {
    //            System.out.println("Database Error");
    //          }
    //        });
  }
}
