package com.example.ambulanceside;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    SharedPreferences sharedpreferences;
    Button navigateButton;
    String destinationLatitude = "20.9350° ";    //IGIT,Sarang
    String destinationLongitude="85.2633° ";
    String PAge="",PName="",PBloodGr="",PRelMob1="";
    TextView initialtv;
    ConstraintLayout layout ;
    TextView secondtv;
    TextView nametv;
    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("MainActivty aa gaya");
        mediaPlayer =MediaPlayer.create(this, R.raw.siren);
        mediaPlayer.setLooping(true);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        myRef= database.getReference("Ambulances/"+user.getUid()+"/AccCheck");
        initialtv =(TextView)findViewById(R.id.initialtv);
        secondtv =(TextView)findViewById(R.id.tv2);
        nametv =(TextView)findViewById(R.id.nametv);
        navigateButton= (Button)findViewById(R.id.NavigateButton);
        navigateButton.setVisibility(View.GONE);
        secondtv.setVisibility(View.GONE);
        nametv.setVisibility(View.GONE);
        sharedpreferences = getSharedPreferences("Ambulance", Context.MODE_PRIVATE);
        PAge=sharedpreferences.getString("PAge", "0");
        PName=sharedpreferences.getString("PName", "0");
        PBloodGr=sharedpreferences.getString("PBloodGr", "0");
        PRelMob1=sharedpreferences.getString("PRelMob1", "0");
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                sendIntnt();
            }
        });
        if(sharedpreferences.getString("AccCheck", "0").equals("1")) {
            destinationLatitude=sharedpreferences.getString("PLat", "0");
            destinationLongitude=sharedpreferences.getString("PLon", "0");
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("AccCheck", "0");
            editor.commit();


            mediaPlayer.start();
            navigateButton.setVisibility(View.VISIBLE);
            initialtv.setVisibility(View.GONE);
            nametv.setText(PName+" needs your help");
            nametv.setVisibility(View.VISIBLE);
            secondtv.setVisibility(View.VISIBLE);

           // layout.setBackgroundColor(Color.parseColor("#FB0000"));

        }
        startService(new Intent(this, MyService.class));



       /* mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.siren);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                if(value.equalsIgnoreCase("Accident"))
                {
                    mediaPlayer.start();
                }
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
               // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });*/



        navigateButton= (Button)findViewById(R.id.NavigateButton);
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntnt();
            }
        });




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MyService.class));
       /* if(mediaPlayer!=null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }*/
    }


    public void sendIntnt()
    {
        myRef.setValue("0");
        finish();
        mediaPlayer.stop();
        mediaPlayer.release();

        String uri = "http://maps.google.com/maps?daddr=" + destinationLatitude + "," + destinationLongitude;
        Toast.makeText(this, "Google Map Kholuchi", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        try
        {
            startActivity(intent);
        }
        catch(ActivityNotFoundException ex)
        {
            try
            {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            }
            catch(ActivityNotFoundException innerEx)
            {
                Toast.makeText(this, "Abe,Google Map install kar age", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");

        myRef.setValue("0");
        mediaPlayer.stop();
        mediaPlayer.release();
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("AccCheck", "0");
        editor.commit();

    }
}

