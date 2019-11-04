package com.example.campuseetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventDescription extends AppCompatActivity {

    String eventName;
    String eventDescription;
    String eventLocation;

    GoogleSignInClient mGoogleSignInClient;

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

        TextView showName=(TextView)findViewById(R.id.nameShowing);
        showName.setText("Event Name: "+eventName);

        TextView showDes=(TextView)findViewById(R.id.descriptionShowing);
        showDes.setText("Event Description: "+eventDescription);

        TextView showLoc=(TextView)findViewById(R.id.locationShowing);
        showLoc.setText("Event Location: "+eventLocation);

    }

    public void sendAddition(View button) {

//        Event catchEvent=new Event(nameId,descriptionId,0,null,locationId);
//
//        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
//
//        DatabaseReference mPublisherRef = mRootRef.child("Publisher");
//
//        String key=identifierVal.replace('.', ',');
//        mPublisherRef.child(key).child("Event").child(nameId).setValue(catchEvent);
//
//
//        Intent intent = new Intent(ConfirmDetailsActivity.this, PublisherHomeActivity.class);
//        startActivity(intent);
    }
}
