package com.example.campuseetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ConfirmDetailsActivity extends AppCompatActivity {

    String nameId="";
    String descriptionId="";
    String locationId="";
    String identifierVal="";
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_details);

        nameId = getIntent().getStringExtra("NAME_ID");
        descriptionId = getIntent().getStringExtra("DESCRIPTION_ID");
        locationId = getIntent().getStringExtra("LOCATION_ID");

        FirebaseApp.initializeApp(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(ConfirmDetailsActivity.this);

        identifierVal=acct.getEmail();

        TextView showName=(TextView)findViewById(R.id.nameShowing);
        showName.setText("Event Name: "+nameId);

        TextView showDes=(TextView)findViewById(R.id.descriptionShowing);
        showDes.setText("Event Description: "+descriptionId);

        TextView showLoc=(TextView)findViewById(R.id.locationShowing);
        showLoc.setText("Event Location: "+locationId);
    }

    public void sendFeedback(View button) {
        Intent intent = new Intent(ConfirmDetailsActivity.this, AddEventActivity.class);
        startActivity(intent);
    }

    public void sendAddition(View button) {

        Event catchEvent=new Event(nameId,descriptionId,0,null,locationId);

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference mPublisherRef = mRootRef.child("Publisher");

        String key=identifierVal.replace('.', ',');
        mPublisherRef.child(key).child("Event").child(nameId).setValue(catchEvent);

        /*
        *   String key=emailKey.replace('.', ',');
            Publisher publisher = new Publisher(nameKey, emailKey);
            mPublisherRef.child(key).setValue(publisher);
        * */

        Intent intent = new Intent(ConfirmDetailsActivity.this, PublisherHomeActivity.class);
        startActivity(intent);
    }
}
