package com.example.campuseetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EventDescription extends AppCompatActivity {

    String eventName;
    String eventDescription;
    String eventLocation;
    String eventDateTime;

    String publisherIdentifier;

    GoogleSignInClient mGoogleSignInClient;

    DatabaseReference mRootRef;

    String identifierVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);

        FirebaseApp.initializeApp(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(EventDescription.this);

        eventName = getIntent().getStringExtra("eventName");
        eventDescription= getIntent().getStringExtra("eventDescription");
        eventLocation=getIntent().getStringExtra("eventLocation");
        eventDateTime=getIntent().getStringExtra("dateTimeId");

        TextView showName=(TextView)findViewById(R.id.nameShowing);
        showName.setText("Event Name: "+eventName);

        TextView showDes=(TextView)findViewById(R.id.descriptionShowing);
        showDes.setText("Event Description: "+eventDescription);

        TextView showLoc=(TextView)findViewById(R.id.locationShowing);
        showLoc.setText("Event Location: "+eventLocation);

        TextView showDateTime=(TextView)findViewById(R.id.dateTimeShowing);
        showLoc.setText("Event Date and Time: "+eventDateTime);

        final Button following= (Button)findViewById(R.id.Following);

        publisherIdentifier=getIntent().getStringExtra("publisherId");



        identifierVal=acct.getEmail();

        final ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("mylist");


        mRootRef = FirebaseDatabase.getInstance().getReference();

        if(myList.contains(eventName)){
            following.setText("Not Going");
        }
        else{
            following.setText("Going");
        }


        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(following.getText().equals("Going")) {
                    following.setText("Not Going");


                    String key=identifierVal.replace('.', ',');

                    mRootRef.child("User").child(key).child("Going").child(eventName).setValue(eventName);

                    //subtract attendees from publisher

                    //publisherIdentifier
                    final String key2=publisherIdentifier.replace('.', ',');


                    //get number of attendees using data snapshot


                    DatabaseReference mEventRef=mRootRef.child("Publisher").child(key2).child("Event");

                    Query queryToGetData = mRootRef.child("Publisher").child(key2).child("Event").orderByChild("eventName").equalTo(eventName);
                    queryToGetData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            for (DataSnapshot datas2: snapshot.getChildren()) {

                                long x = datas2.child("attendees").getValue(Long.class);

                                mRootRef.child("Publisher").child(key2).child("Event").child(eventName).child("attendees").setValue(x+1);

                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });




                }
                else{
                    following.setText("Going");


                    String test=eventName;
                    String key=identifierVal.replace('.', ',');

                    mRootRef.child("User").child(key).child("Going").child(test).removeValue();

                    //add attendees of publisher

                    DatabaseReference mEventRef=mRootRef.child("Publisher").child(publisherIdentifier.replace('.', ',')).child("Event");

                    Query queryToGetData = mRootRef.child("Publisher").child(publisherIdentifier.replace('.', ',')).child("Event").orderByChild("eventName").equalTo(eventName);
                    queryToGetData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            for (DataSnapshot datas2: snapshot.getChildren()) {

                                long x = datas2.child("attendees").getValue(Long.class);

                                mRootRef.child("Publisher").child(publisherIdentifier.replace('.', ',')).child("Event").child(eventName).child("attendees").setValue(x-1);

                            }



                        }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



                }

            }
        });


    }

    public void sendHome(View button) {
        Intent i = new Intent(this, UserHomeActivity.class);
        startActivity(i);
    }



}
