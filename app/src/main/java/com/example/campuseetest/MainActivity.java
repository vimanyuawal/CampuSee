package com.example.campuseetest;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.AbstractSequentialList;
import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 123;
    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    private FusedLocationProviderClient fusedLocationClient;
    Location userLocation;
    private GeofencingClient geofencingClient;
    ArrayList<Geofence> geofenceList = new ArrayList<Geofence>();
    private PendingIntent geofencePendingIntent;
    GeofencingRequest geofencingRequest;
    private GoogleApiClient mGoogleApiClient;

    private static final String TAG = "GeofenceManager";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Initializing Views
        signInButton = findViewById(R.id.sign_in_button);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        geofencingClient = LocationServices.getGeofencingClient(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {

            requestLocationPermission();
        }

        getUserLocation();

        createGeofence("JFF", -118.2824, 34.0187, 50);
        createGeofence("RTH", -118.289958, 34.020377, 50);
        createGeofence("THH", -118.284505, 34.022333, 50);

        addGeofence();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }


    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }

        Log.d("a", "Setting up intent");

        Intent intent = new Intent(MainActivity.this, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        Log.d("a", "Broadcasting intent");
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        Log.d("a", "Got broadcast back: ");

        if(geofencePendingIntent == null){
            Log.d("a", "Null pending intent");
        }

        return geofencePendingIntent;
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);

        Log.d(TAG, "builder: " + geofenceList.toString());


        return builder.build();
    }

    public void createGeofence(String requestID, double longitude, double latitude, int radius) {
        //1st region
        geofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(requestID)

                .setCircularRegion(
                        latitude,
                        longitude,
                        radius
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());


    }


    private void addGeofence() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {


            geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Added Geofence", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("a", "Error Adding Geofence: ", e);
                            Toast.makeText(MainActivity.this, "Error adding Geofence", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void requestLocationPermission() {
        if ( ContextCompat.checkSelfPermission( this, ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            // Should we show an explanation?
            if ( ActivityCompat.shouldShowRequestPermissionRationale( this, ACCESS_FINE_LOCATION ) ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions( MainActivity.this, new String[]{ACCESS_FINE_LOCATION}, REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();
                getUserLocation();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions( this, new String[]{ACCESS_FINE_LOCATION}, REQUEST_LOCATION );
                getUserLocation();
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if ( ActivityCompat.checkSelfPermission( this, ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {

                }
            }
        }
    }

    public void getUserLocation() {

        Log.d("DEBUG MESSAGE", "222Everything is fine till here");


        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            Log.d("a", "User longitude is: " + longitude + " and user latitude is " + latitude);
                        }
                    }
                });
        fusedLocationClient.getLastLocation().addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("failure","Failed miserable because of: "+ e.toString());
            }
        });

    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.


                String personName = account.getDisplayName();
                String personGivenName = account.getGivenName();
                String personFamilyName = account.getFamilyName();
                String personEmail = account.getEmail();
                String personId = account.getId();

                final String emailKey = personEmail;
                final String nameKey = personName;


            final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

            Query queryToGetData = dbRef.child("Publisher").orderByChild("email").equalTo(emailKey);

            queryToGetData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){

                        final DatabaseReference dbRef2 = FirebaseDatabase.getInstance().getReference();

                        Query queryToGetData2 = dbRef2.child("User").orderByChild("email").equalTo(emailKey);

                        queryToGetData2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot2) {
                                if(!dataSnapshot2.exists()){
                                    Intent intent2 = new Intent(MainActivity.this, Main2Activity.class);
                                    startActivity(intent2);
                                }
                                else{
                                    Intent intent2 = new Intent(MainActivity.this, UserHomeActivity.class);
                                    intent2.putExtra("identifier",nameKey);
                                    startActivity(intent2);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

//                        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
//                        startActivity(intent);
                    }
                    else{

                        Intent intent = new Intent(MainActivity.this, PublisherHomeActivity.class);
                        intent.putExtra("identifier",nameKey);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
        }

    }
}