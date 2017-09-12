package next.whatsnext.mainRecycler;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import next.whatsnext.Profil;
import next.whatsnext.R;
import next.whatsnext.RecyclerItemClickListener;
import next.whatsnext.Settings;
import next.whatsnext.UserRecycler.UserRV;
import next.whatsnext.fireChat.FireChat;
import next.whatsnext.login.LoginMain;
import next.whatsnext.notifyService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String username;
    MainAdapter adapter;
    private RecyclerView recyclerView;
    private List<MainData> list = new ArrayList<>();
    String mesuser = "";
    ArrayList<String> chatlist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean boo = prefs1.getBoolean("pref_notif",true);
        if (boo) {
            if (!isMyServiceRunning(notifyService.class)) {
                startService(new Intent(this, notifyService.class));
            }
        }else {
            if (isMyServiceRunning(notifyService.class)){
                Intent myService = new Intent(MainActivity.this, notifyService.class);
                stopService(myService);
                Toast.makeText(getApplicationContext(),"Servis Sonlandirildi",Toast.LENGTH_SHORT).show();}
        }


        recyclerView= findViewById(R.id.mainrecycler);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);


        adapter = new MainAdapter(getApplicationContext(),list);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                TextView chattv = v.findViewById(R.id.MainName);
                String chatna1 = chattv.getText().toString();

                for (int i15 = 0; i15<chatlist.size();i15++){
                    String tempname = chatlist.get(i15);

                    if (tempname.contains(chatna1)){
                        Intent intent = new Intent(MainActivity.this, FireChat.class);
                        Bundle bun = new Bundle();
                        bun.putString("databkey",tempname);
                        bun.putInt("classint",1);
                        intent.putExtras(bun);
                        startActivity(intent);
                    }
                }
            }
        }));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("WhatsNext",0);
        username = prefs.getString("username","");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, UserRV.class);
                startActivity(i);
                MainActivity.this.overridePendingTransition(R.anim.fromright, R.anim.toleft);

                //Intent i = new Intent(MainActivity.this, FireChat.class);
                //startActivity(i);
            }
        });

        final View contentv = findViewById(R.id.content1);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.setDrawerElevation(0);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                contentv.setTranslationX(slideOffset * drawerView.getWidth());
                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
                drawer.setScrimColor(Color.TRANSPARENT);
            }
        };

        //noinspection deprecation
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        crawler1();

        View view1 = navigationView.getHeaderView(0);
        FirebaseUser us = FirebaseAuth.getInstance().getCurrentUser();
        TextView navEmail = view1.findViewById(R.id.navMail);
        assert us != null;
        navEmail.setText(us.getEmail());

        final TextView navAd = view1.findViewById(R.id.navAd);
        final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference q2 = ref1.child("UserProfile").child(username).child("ad");
        q2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                String name1 = dataSnapshot1.getValue(String.class);
                navAd.setText(name1);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        final ImageView navimg = (ImageView) view1.findViewById(R.id.navImg);
        StorageReference reference = FirebaseStorage.getInstance().getReference();

        StorageReference ref = reference.child("profil").child(username);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String urlst = uri.toString();
                Glide.with(MainActivity.this).load(urlst)
                        .into(navimg);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Programdan çixmaq istəyirsinizmi?")
                    .setCancelable(false)
                    .setPositiveButton("Bəli", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(android.os.Build.VERSION.SDK_INT>16){
                                MainActivity.this.finishAffinity();}
                            else{
                                MainActivity.this.finish();
                                System.exit(0);}}
                    }).setNegativeButton("Xeyr", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}});
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, Settings.class);
            startActivity(i);
            MainActivity.this.overridePendingTransition(R.anim.fromright, R.anim.toleft);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.nav_profile) {
            Intent i1 = new Intent(MainActivity.this, Profil.class);
            startActivity(i1);
        }  else if (id == R.id.nav_manage) {
            Intent i2 = new Intent(MainActivity.this, Settings.class);
            startActivity(i2);

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(MainActivity.this,LoginMain.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        } else if (id == R.id.nav_exit) {
            MainActivity.this.finishAffinity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void crawler1(){
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("messages");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                list.clear();
                for (DataSnapshot data1 :dataSnapshot1.getChildren()){

                    String keyv = data1.getKey();

                    if (keyv.contains(username)){

                        chatlist.add(keyv);

                        if (keyv.contains("-"+username)){
                        mesuser = keyv.replace("-"+username,"");}
                        else{mesuser = keyv.replace(username+"-","");}
                        String mes = "";
                        String mesdate = "";
                        for (DataSnapshot data11: data1.getChildren()){

                            mes = data11.child("messageText").getValue(String.class);
                            //noinspection ConstantConditions
                            long datem =data11.child("messageTime").getValue(Long.class);

                            mesdate = (String) DateFormat.format("dd/MM/yy",
                                    datem);

                        }
                        list.add(new MainData(mesuser,mes,mesdate));
                    }



                    /*if (data1.child(username).getValue()!=null){
                        String mes = null;
                        String mesdate = null;

                        String to1 = data1.getKey();

                        for (DataSnapshot data11: data1.child(username).getChildren()){
                            mes = data11.child("messageText").getValue(String.class);
                            long datem =data11.child("messageTime").getValue(Long.class);
                            mesdate = (String) DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                                    datem);


                        }


                        list.add(new MainData(to1,mes,mesdate));
                    }*/
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
