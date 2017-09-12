package next.whatsnext.UserRecycler;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import next.whatsnext.R;
import next.whatsnext.RecyclerItemClickListener;
import next.whatsnext.fireChat.FireChat;
import next.whatsnext.mainRecycler.MainActivity;

@SuppressWarnings("ALL")
public class UserRV extends AppCompatActivity {

    private List<UserData> list = new ArrayList<>();
    private RecyclerView recyclerView;

    UserAdapter adapter;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userrv);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("WhatsNext",0);
        username = prefs.getString("username","");

        list.clear();
        prepareData();

        recyclerView= findViewById(R.id.userRecycler);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);


        adapter = new UserAdapter(getApplicationContext(),list);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                TextView nameTV = v.findViewById(R.id.userNa);
                String name = nameTV.getText().toString();

                Intent i = new Intent(UserRV.this, FireChat.class);
                Bundle b = new Bundle();
                b.putInt("classint",0);
                b.putString("from", username);
                b.putString("to", name);
                i.putExtras(b);
                startActivity(i);
                UserRV.this.overridePendingTransition(R.anim.fromright, R.anim.toleft);
            }
        }));
    }


    public void prepareData(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    String user1 = data.getKey();
                    Log.e("user1" , "us1" + user1);
                    if (user1.equals(username)) {
                    continue;
                    }
                    list.add(new UserData(user1));

                }
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserRV.this.overridePendingTransition(R.anim.fromleft, R.anim.toright);
    }
}
