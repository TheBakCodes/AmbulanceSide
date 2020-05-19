package com.example.ambulanceside;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.vistrav.ask.Ask;
import com.vistrav.ask.annotations.AskDenied;
import com.vistrav.ask.annotations.AskGranted;

public class Main2Activity extends AppCompatActivity {
    //a constant for detecting the login intent result
    private static final int RC_SIGN_IN = 234;

    //Tag for the logs optional
    private static final String TAG = "simplifiedcoding";

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;
    int INT_ID_OF_YOUR_REQUEST=69;
    //And also a Firebase Auth object
    FirebaseAuth mAuth;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //first we intialized the FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();
        sharedpreferences = getSharedPreferences("Ambulance", Context.MODE_PRIVATE);

        Ask.on(this)
                .id(INT_ID_OF_YOUR_REQUEST) // in case you are invoking multiple time Ask from same activity or fragment
                .forPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.FOREGROUND_SERVICE,
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withRationales("Location permission need for map to work properly","Location permission need for map to work properly","Background Call",
                        "In order to save file you will need to grant storage permission") //optional
                .go();




        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("513248493176-dk4i4n73jr37fq5at88nm6sa5vmfo8ka.apps.googleusercontent.com")
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Now we will attach a click listener to the sign_in_button
        //and inside onClick() method we are calling the signIn() method that will open
        //google sign in intent
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity

        if (mAuth.getCurrentUser() != null) {
            finish();
            SharedPreferences sh = getSharedPreferences("Ambulance", Context.MODE_PRIVATE);
            if(sh.getInt("stage", 0)==2)
            {
                Intent intent= new Intent(this,MainActivity.class);
                startActivity(intent);
            }
            else
            startActivity(new Intent(this, FillDetails.class));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(Main2Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(Main2Activity.this, "User Signed In", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putInt("stage", 1);
                            editor.commit();
                            Intent myIntent = new Intent(Main2Activity.this, FillDetails.class);
                            //myIntent.putExtra("key", value); //Optional parameters
                            startActivity(myIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Main2Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


    //this method is called on click
    private void signIn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //optional
    @AskGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void fileAccessGranted(int id) {
        Log.i(TAG, "FILE  GRANTED");
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }

    //optional
    @AskDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void fileAccessDenied(int id) {
        Log.i(TAG, "FILE  DENiED");
    }
    //optional
    @AskGranted(Manifest.permission.ACCESS_FINE_LOCATION)
    public void finelocationGranted(int id) {
        Log.i(TAG, "Location grapnted1");
        Toast.makeText(this, "Location1 Granted", Toast.LENGTH_SHORT).show();
    }

    //optional
    @AskDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    public void finelocationDenied(int id) {
        Log.i(TAG, "Location DENiED");
        Toast.makeText(this, "Location1 Denied", Toast.LENGTH_SHORT).show();
    }
    //optional
    @AskGranted(Manifest.permission.FOREGROUND_SERVICE)
    public void foregroundserviceGranted(int id) {
        Log.i(TAG, "ForegroundService  GRANTED");
        Toast.makeText(this, "ForegroundService Granted", Toast.LENGTH_SHORT).show();
    }

    //optional
    @AskDenied(Manifest.permission.FOREGROUND_SERVICE)
    public void foregroundserviceDenied(int id) {
        Log.i(TAG, "ForegroundService  DENiED");
        Toast.makeText(this, "ForegroundService Denied", Toast.LENGTH_SHORT).show();
    }
    //optional
    @AskGranted(Manifest.permission.SYSTEM_ALERT_WINDOW)
    public void alertwindowGranted(int id) {
        Log.i(TAG, "AlertWindow  GRANTED");
        Toast.makeText(this, "AlertWindow Granted", Toast.LENGTH_SHORT).show();
    }

    //optional
    @AskDenied(Manifest.permission.SYSTEM_ALERT_WINDOW)
    public void alertwindowDenied(int id) {
        Log.i(TAG, "FILE  DENiED");
        Toast.makeText(this, "AlertWindow Denied", Toast.LENGTH_SHORT).show();
    }

    //optional
    @AskGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
    public void mapAccessGranted(int id) {
        Log.i(TAG, "Location2 GRANTED");
        Toast.makeText(this, "Location2 Granted", Toast.LENGTH_SHORT).show();
    }

    //optional
    @AskDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
    public void mapAccessDenied(int id) {
        Log.i(TAG, "MAP DENIED");
        Toast.makeText(this, "Location2 Denied", Toast.LENGTH_SHORT).show();
    }

}
