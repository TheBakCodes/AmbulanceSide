package com.example.ambulanceside;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FillDetails extends AppCompatActivity {
Button btenter;
EditText etname,etage;
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefUser,myRefDriver,myRefAmbuDName,myRefAmbuDUid,myRefAmbuLoclat,myRefAmbuLoclon ;
    SharedPreferences sharedpreferences;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude="00", longitude="00";
    int fl=0;
    private GpsTracker gpsTracker;
    FirebaseUser user;
    String latans[],lonans[],drivname[],drivuid[],to="",ind="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        sharedpreferences = getSharedPreferences("Ambulance", Context.MODE_PRIVATE);
       try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        getLocation();
        setContentView(R.layout.activity_fill_details);
        btenter=(Button)findViewById(R.id.btenter);
        etage=(EditText)findViewById(R.id.etage);
        etname=(EditText)findViewById(R.id.etname);

        myRefUser= database.getReference("Users/"+user.getUid());
        myRefDriver=database.getReference("Ambulances/"+user.getUid());
        myRefAmbuDName = database.getReference("Ambulances/DriverName");
        myRefAmbuDUid = database.getReference("Ambulances/DriverUID");
        myRefAmbuLoclat = database.getReference("Ambulances/Location/latloclist");
        myRefAmbuLoclon = database.getReference("Ambulances/Location/lonloclist");
        myRefAmbuDName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String val=dataSnapshot.getValue().toString();
                drivname=val.split(",");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRefAmbuDUid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String val=dataSnapshot.getValue().toString();
                drivuid=val.split(",");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRefAmbuLoclat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String val=dataSnapshot.getValue().toString();
                latans=val.split(",");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        myRefAmbuLoclon.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String val=dataSnapshot.getValue().toString();
                lonans=val.split(",");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        btenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference myRefto = database.getReference("Ambulances/TotalAmbu");
                // Read from the database
                myRefto.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        int value = Integer.parseInt(dataSnapshot.getValue().toString());
                        Log.d("Total", "Value is: " + value);
                        myRefDriver.child("Index").setValue(value);
                        myRefDriver.child("DName").setValue(etname.getText().toString());
                        myRefDriver.child("PName").setValue("N/A");
                        myRefDriver.child("PUID").setValue("N/A");
                        myRefDriver.child("AccCheck").setValue("0");
                        myRefDriver.child("DAge").setValue(etage.getText().toString());
                        myRefDriver.child("PAge").setValue("N/A");
                        myRefDriver.child("PLat").setValue("N/A");
                        myRefDriver.child("PLon").setValue("N/A");
                        myRefDriver.child("PRelMob1").setValue("N/A");
                        myRefDriver.child("PBloodGr").setValue("B+");
                        myRefDriver.child("DLat").setValue(latitude);
                        myRefDriver.child("DLon").setValue(longitude);
                        String dnw="",duw="",latw="",lonw="";
                        for (int i=0;i<value;i++)
                        {
                            dnw+=drivname[i]+",";
                            duw+=drivuid[i]+",";
                            latw+=latans[i]+",";
                            lonw+=lonans[i]+",";
                        }
                        dnw+=etname.getText().toString()+",";
                        duw+=user.getUid()+",";
                        latw+=latitude+",";
                        lonw+=longitude+",";
                        myRefAmbuDName.setValue(dnw);
                        myRefAmbuDUid.setValue(duw);
                        myRefAmbuLoclat.setValue(latw);
                        myRefAmbuLoclon.setValue(lonw);
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putInt("stage", 2);
                        editor.commit();
                        Intent intent=new Intent(FillDetails.this,MainActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("Total", "Failed to read value.", error.toException());
                    }
                });
            }
        });
    }

    public void getLocation(){
        gpsTracker = new GpsTracker(FillDetails.this);
        if(gpsTracker.canGetLocation()){
            latitude=String.valueOf(gpsTracker.getLatitude());
            longitude=String.valueOf(gpsTracker.getLongitude());
            Toast.makeText(this, String.valueOf(latitude), Toast.LENGTH_SHORT).show();

            Toast.makeText(this, String.valueOf(longitude), Toast.LENGTH_SHORT).show();
            /*if(fl==1)
            {
                myRefDriver.child("DLat").setValue(latitude);
                myRefDriver.child("DLon").setValue(longitude);
            }*/
        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sh = getSharedPreferences("Ambulance", Context.MODE_PRIVATE);
        if(sh.getInt("stage", 0)==2)
        {
            Intent intent= new Intent(FillDetails.this,MainActivity.class);
            startActivity(intent);
        }
    }
}
