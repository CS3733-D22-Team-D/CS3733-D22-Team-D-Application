package edu.wpi.DapperDaemons.backend;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import edu.wpi.DapperDaemons.entities.TableObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class FireBaseLoader {

    public FireBaseLoader(DAO dao, TableObject type){
        DatabaseReference ref = FireBase.getReference();
        ref = ref.child(new ArrayList<String>(dao.getAll().values()).get(0));
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, List<String>> data = (HashMap<String, List<String>>) dataSnapshot.getValue();
                for(List<String> l: data.values()){
                    dao.add(type.newInstance(l));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Database Error");
            }
        });
    }
}
