package com.example.campuseetest;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class NotificationsActivity extends AppCompatActivity {


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


    private static final String TAG = "Notification";
    String locationIdentifier = "";


    Date currentTime = Calendar.getInstance().getTime();

    String[] event= {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Email of user is: ");
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Email of user is: ");
        setContentView(R.layout.activity_notifications);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

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

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        String userEmail = acct.getEmail();
        final String key = userEmail.replace('.', ',');

        Log.d(TAG, "Email of user is: " + key);

        //get the location the user is in
        final DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference user = root.child("User").child(key).child("Location");


        TableLayout ll = (TableLayout) findViewById(R.id.tableLayout2);

        TableRow row= new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 60);

        TextView publisherName= new TextView(this);
        publisherName.setText("Events in my location");
        publisherName.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
        publisherName.setWidth(50);
        publisherName.setTextSize(30);

        row.addView(publisherName);

        ll.addView(row);



        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                locationIdentifier=dataSnapshot.getValue(String.class);
                Log.d(TAG, "Location of user is: " + locationIdentifier);

                //HERE PLZ

                //now get all events in this location
                final DatabaseReference mainRef = root.child("Publisher");

                mainRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot datas: snapshot.getChildren()) {
                            DatabaseReference mEventsRef=mainRef.child(datas.getKey()).child("Event").getRef();


                            mEventsRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String tester="";
                                    for(DataSnapshot datas2: dataSnapshot.getChildren()){
                                        String location=datas2.child("location").getValue(String.class);

                                        if(location.equals(locationIdentifier)){
                                            event[0] = (datas2.child("eventName").getValue(String.class));
                                            String dt = datas2.child("dateTime").getValue(String.class);
                                            Date date1;
                                            try {

                                                date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dt);
                                                if(date1.compareTo(currentTime) > 0){
                                                    Log.d(TAG, "currentTime: "+currentTime+" event time: "+date1);
                                                    tester=datas2.child("eventName").getValue(String.class);
                                                    printEvents(tester);
                                                }

                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    }
                                    //Continue here


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void printEvents(String matchedEvents) {

        TableLayout ll = (TableLayout) findViewById(R.id.tableLayout2);

        TableRow row= new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 60);


        row= new TableRow(this);
        lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 60);
        row.setLayoutParams(lp);

        TextView eventTextBox= new TextView(this);
        eventTextBox.setText(matchedEvents);
        eventTextBox.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));

        eventTextBox.setWidth(50);
        eventTextBox.setMaxLines(1);
        eventTextBox.setPadding(12,12,12,12);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            eventTextBox.setAutoSizeTextTypeUniformWithConfiguration(7,25,1,1);
        }
        eventTextBox.setClickable(true);

        eventTextBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        row.addView(eventTextBox);
        ll.addView(row);

    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }

        Log.d("a", "Setting up intent");

        Intent intent = new Intent(NotificationsActivity.this, GeofenceBroadcastReceiver.class);
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
                .setNotificationResponsiveness(50)
                .setLoiteringDelay(0)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());


    }


    private void addGeofence() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {


            geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("a", "Error Adding Geofence: ", e);
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
                                ActivityCompat.requestPermissions( NotificationsActivity.this, new String[]{ACCESS_FINE_LOCATION}, REQUEST_LOCATION );
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

    String notification = "";

    public void setNotification(String n){
        notification = n;
    }

    public String getNotification(){
        return notification;
    }

    public void sendHome(View button) {
        Intent i = new Intent(this, UserHomeActivity.class);
        startActivity(i);
    }



}

