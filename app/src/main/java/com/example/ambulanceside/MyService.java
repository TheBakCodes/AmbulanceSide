package com.example.ambulanceside;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyService extends Service {
    MediaPlayer mediaPlayer;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefAge,myRefBloodgr,lat,lon,name,relmob1,puid,accheck;
    FirebaseAuth mAuth;
    FirebaseUser user;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    Context context=this;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Service Started");
        mediaPlayer = MediaPlayer.create(this, R.raw.siren);
        // Read from the database
        sharedpreferences = getSharedPreferences("Ambulance", Context.MODE_PRIVATE);
        editor= sharedpreferences.edit();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
         myRefAge= database.getReference("Ambulances/"+user.getUid()+"/PAge");
         myRefBloodgr=database.getReference("Ambulances/"+user.getUid()+"/PBloodGr");
         lat=database.getReference("Ambulances/"+user.getUid()+"/PLat");
         lon=database.getReference("Ambulances/"+user.getUid()+"/PLon");
         name=database.getReference("Ambulances/"+user.getUid()+"/PName");
         relmob1=database.getReference("Ambulances/"+user.getUid()+"/PRelMob1");
         puid=database.getReference("Ambulances/"+user.getUid()+"/PUID");
         accheck=database.getReference("Ambulances/"+user.getUid()+"/AccCheck");
        myRefAge.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue().toString();
                editor.putString("PAge", value);
                editor.commit();
               // System.out.println("Page"+value);
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
                //System.out.println("Failed1");
            }
        });

        myRefBloodgr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue().toString();
                editor.putString("PBloodgr", value);
                editor.commit();
               // System.out.println("Pbloodgr"+value);
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
               // System.out.println("Failed2");
            }
        });

        lat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue().toString();
                editor.putString("PLat", value);
                editor.commit();
               // System.out.println("Plat"+value);
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
              //  System.out.println("Failed3");
            }
        });

        lon.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue().toString();
                editor.putString("PLon", value);
                editor.commit();
               // System.out.println("Plon"+value);
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
                //System.out.println("Failed4");
            }
        });

        name.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue().toString();
                editor.putString("PName", value);
                editor.commit();
              //  System.out.println("Pname"+value);
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
              //  System.out.println("Failed5");
            }
        });

        relmob1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue().toString();
                editor.putString("PRelMob1", value);
                editor.commit();
               // System.out.println("Prelmob1"+value);
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
              //  System.out.println("Failed6");
            }
        });

        puid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue().toString();
                editor.putString("PUID", value);
                editor.commit();
               // System.out.println("Puid"+value);
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
                //System.out.println("Failed7");
            }
        });

        accheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue().toString();
                editor.putString("AccCheck", value);
                editor.commit();
                //System.out.println("acccheck"+value);
                Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
                if(value.equals("1")) {
                    Intent dialogIntent = new Intent(context, MainActivity.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                }
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // Log.w(TAG, "Failed to read value.", error.toException());
               // System.out.println("Failed8");
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null)
            mediaPlayer.stop();
    }
}
