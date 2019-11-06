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


    private static final String TAG = "Notification";
    String locationIdentifier = "";
    GoogleSignInClient mGoogleSignInClient;

    ArrayList<String> matchedEvents=new ArrayList<String>();

    Date currentTime = Calendar.getInstance().getTime();

    String[] event= {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

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
                                                date1 = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS").parse(dt);
                                                if(date1.compareTo(currentTime) > 0){
                                                    tester=datas2.child("eventName").getValue(String.class);
                                                }
                                                else {
                                                    tester = "notanevent";
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    }
                                    matchedEvents.add(event[0]);
                                    //Continue here
                                    if(!tester.equals("notanevent"))
                                        printEvents(tester);
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

//        TextView publisherName= new TextView(this);
//        publisherName.setText("Events in my location");
//        publisherName.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT));
//        publisherName.setWidth(50);
//
//        row.addView(publisherName);
//
//        ll.addView(row);

//        for(int i=0;i<matchedEvents.size();++i){
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

//            final String identify=entry.getValue();
        eventTextBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Intent intent= new Intent(AllPublishersActivity.this, PublishersEvents.class);
//                    intent.putExtra("publisherId",entry.getValue());
//                    startActivity(intent);
            }
        });

        row.addView(eventTextBox);
        ll.addView(row);
//        }

    }
}

