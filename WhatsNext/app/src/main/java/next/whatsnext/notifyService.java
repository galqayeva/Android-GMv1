package next.whatsnext;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import next.whatsnext.mainRecycler.MainActivity;

public class notifyService extends Service{

    private String username;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("WhatsNext",0);
        username = prefs.getString("username","");

        crawler1();



    }

    private void postNotif( String notifString,String message) {
        NotificationManager notificationManager = (NotificationManager)
            getSystemService(NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        Notification n  = null;
        Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            n = new Notification.Builder(this)
                    .setContentTitle(notifString)
                    .setContentText(message)
                    .setTicker("Whatsnext")
                    .setSmallIcon(R.drawable.ic_stat_whatsnext)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .setSound(alarmsound)
                    .setVibrate(new long[] { 500, 500, 500, 500 })
                    .setStyle(new Notification.BigTextStyle().bigText("")).build();

        }
        //  .addAction(R.drawable.line, "", pIntent).build();
        assert n != null;
        n.flags |= Notification.FLAG_AUTO_CANCEL;

        assert notificationManager != null;
        notificationManager.notify(0, n); }



    public void crawler1(){
        final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("notification").child(username);
        ref1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String ad = dataSnapshot.child("messageUser").getValue(String.class);
                String message = dataSnapshot.child("messageText").getValue(String.class);

                postNotif(ad,message);
                String key = dataSnapshot.getKey();
                ref1.child(key).removeValue();


            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }




}
