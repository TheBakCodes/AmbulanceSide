package com.example.ambulanceside;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("MainActivty aa gaya");
        sharedpreferences = getSharedPreferences("Ambulance", Context.MODE_PRIVATE);
        if(sharedpreferences.getString("AccCheck", "0").equals("1")) {
            destinationLatitude=sharedpreferences.getString("PLat", "0");
            destinationLongitude=sharedpreferences.getString("PLon", "0");
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putInt("AccCheck", 0);
            editor.commit();
            sendIntnt();

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
        // if(mediaPlayer!=null)
        //  mediaPlayer.stop();
    }


    public void sendIntnt()
    {

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
}

